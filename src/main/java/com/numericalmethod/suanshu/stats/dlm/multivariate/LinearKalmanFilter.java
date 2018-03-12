/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/
 *
 * THIS SOFTWARE IS LICENSED, NOT SOLD.
 *
 * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
 * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
 * DO NOT USE THIS SOFTWARE.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
 * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
 * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
 * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT,
 * TITLE AND USEFULNESS.
 *
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.stats.dlm.multivariate;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Kalman filter, also known as linear quadratic estimation (LQE),
 * is an algorithm which uses a series of measurements observed over time,
 * containing noise (random variations) and other inaccuracies,
 * and produces estimates of unknown variables that tend to be more precise than those that would be based on a single measurement alone.
 * More formally, the Kalman filter operates recursively on streams of noisy input data to produce a statistically optimal estimate of the underlying system state.
 *
 * @author Haksun Li
 */
public class LinearKalmanFilter {

    /** the DLM model */
    private final DLM model;
    /**
     * the state predictions
     * <blockquote><i>
     * x_{t|t-1} = E(x_t|y_{1:t-1})
     * </i></blockquote>
     */
    private DenseMatrix E_xt_tlag;
    /**
     * the variances of state predictions
     * <blockquote><i>
     * R_{t|t-1} = Var(x_t|y_{1:t-1})
     * </i></blockquote>
     */
    private Matrix[] V_xt_tlag;
    /**
     * the observation predictions
     * <blockquote><i>
     * f_t = E(y_t|y_{1:t-1})
     * </i></blockquote>
     */
    private DenseMatrix E_yt_tlag;
    /**
     * the variances of observation predictions
     * <blockquote><i>
     * Q_t = Var(y_t|y_{1:t-1})
     * </i></blockquote>
     */
    private Matrix[] V_yt_tlag;
    /** the optimal Kalman gain matrices */
    private Matrix[] KalmanGain;
    /**
     * the state updates
     * <blockquote><i>
     * x_{t|t} = E(x_t|y_{1:t})
     * </i></blockquote>
     */
    private DenseMatrix E_xt_t;
    /**
     * the variances of state updates
     * <blockquote><i>
     * R_{t|t} = Var(x_t|y_{1:t})
     * </i></blockquote>
     */
    private Matrix[] V_xt_t;

    /**
     * Construct a Kalman filter from a multivariate controlled dynamic linear model.
     *
     * @param model a multivariate controlled DLM
     */
    public LinearKalmanFilter(DLM model) {
        this.model = model;
    }

    /**
     * Filter the observations.
     *
     * @param Yt the observations
     * @param Ut the controls
     */
    public void filtering(MultiVariateTimeSeries Yt, MultiVariateTimeSeries Ut) {
        final StateEquation state = model.getStateModel();
        final ObservationEquation observation = model.getObservationModel();

        final int p = model.getStateDimension(); //the dimension of states
        final int d = model.getObsDimension(); //the dimension of observations
        final int T = Yt.size(); //the size of time series

        Matrix Y = new DenseMatrix(Yt.toMatrix());
        Matrix U = Ut != null ? new DenseMatrix(Ut.toMatrix()) : null;

        E_xt_tlag = new DenseMatrix(T, p);
        V_xt_tlag = new Matrix[T];

        E_yt_tlag = new DenseMatrix(T, d);
        V_yt_tlag = new Matrix[T];

        KalmanGain = new Matrix[T];

        E_xt_t = new DenseMatrix(T, p);
        V_xt_t = new DenseMatrix[T];

        for (int t = 1; t <= T; ++t) {
            Vector x_tlag_tlag_temp = t > 1 ? E_xt_t.getRow(t - 1) : model.m0();
            // x_{t | t - 1} = G_t * x_{t - 1 | t - 1} + H_t * u_t
            Vector x_t_tlag = state.xt_mean(t, x_tlag_tlag_temp, U != null ? U.getRow(t) : null);
            E_xt_tlag.setRow(t, x_t_tlag);

            //R_{t | t - 1} = G_t * R_{t - 1 | t - 1} * G_t' + W_t
            Matrix R_tlag_tlag = t > 1 ? V_xt_t[t - 2] : model.C0();
            Matrix R_t_tlag = state.xt_var(t, R_tlag_tlag);
            V_xt_tlag[t - 1] = R_t_tlag;

            //f_t = F_t * x_{t | t - 1}
            Vector y_t_tlag = observation.yt_mean(t, x_t_tlag);
            E_yt_tlag.setRow(t, y_t_tlag);

            //Q_{t | t - 1} = F_t * R_{t | t - 1} * F_t' + V_t
            Matrix Q_t_tlag = observation.yt_var(t, R_t_tlag);
            V_yt_tlag[t - 1] = Q_t_tlag;

            //K_t = R_{t | t - 1} * F_t' * (Q_{t | t - 1} ^ (-1))
            Matrix F = observation.F(t);
            KalmanGain[t - 1] = new DenseMatrix(R_t_tlag.multiply(F.t()).multiply(new Inverse(Q_t_tlag)));//TODO: generalize this computation?

            //e_t = y_t - f_t (observation residual)
            Vector e_t = Y.getRow(t).minus(y_t_tlag);

            //x_{t | t} = x_{t | t - 1} + K_t * e_t
            Vector x_t_t = x_t_tlag.add(KalmanGain[t - 1].multiply(e_t));
            E_xt_t.setRow(t, x_t_t);

            //R_{t | t} = (I - K_t * F_t) * R_{t | t - 1}
            Matrix R_t_t = R_t_tlag.minus(KalmanGain[t - 1].multiply(F).multiply(R_t_tlag));
            V_xt_t[t - 1] = R_t_t;
        }
    }

    /**
     * Filter the observations without control variable.
     *
     * @param Yt the observations
     */
    public void filtering(MultiVariateTimeSeries Yt) {
        filtering(Yt, null);
    }

    /**
     * Get the dimension of the system, i.e., the dimension of the state vector.
     *
     * @return the dimension of the system
     */
    public int dimension() {
        return E_xt_t.nCols();
    }

    /**
     * Get <i>T</i>, the number of hidden states or observations.
     *
     * @return <i>T</i>
     */
    public int size() {
        return E_xt_t.nRows();
    }

    /**
     * Get the posterior expected states.
     *
     * @return the fitted states
     */
    public SimpleMultiVariateTimeSeries getFittedStates() {
        return new SimpleMultiVariateTimeSeries(E_xt_t);
    }

    /**
     * Get the prior expected states.
     *
     * @return the predicted states
     */
    public SimpleMultiVariateTimeSeries getPredictedStates() {
        return new SimpleMultiVariateTimeSeries(E_xt_tlag);
    }

    /**
     * Get the prior observation predictions.
     *
     * @return the predicted observations
     */
    public SimpleMultiVariateTimeSeries getPredictedObservations() {
        return new SimpleMultiVariateTimeSeries(E_yt_tlag);
    }

    /**
     * Get the posterior expected state.
     *
     * @param t time, t &ge; 1
     * @return the fitted state
     */
    public ImmutableVector getFittedState(int t) {
        return new ImmutableVector(E_xt_t.getRow(t));
    }

    /**
     * Get the posterior expected state variance.
     *
     * @param t time, t &ge; 1
     * @return the fitted state variance
     */
    public ImmutableMatrix getFittedStateVariance(int t) {
        return new ImmutableMatrix(V_xt_t[t - 1]);
    }

    /**
     * Get the prior expected state.
     *
     * @param t time, t &ge; 1
     * @return the predicted state
     */
    public ImmutableVector getPredictedState(int t) {
        return new ImmutableVector(E_xt_tlag.getRow(t));
    }

    /**
     * Get the prior expected state variance.
     *
     * @param t time, t &ge; 1
     * @return the predicted state variance
     */
    public ImmutableMatrix getPredictedStateVariance(int t) {
        return new ImmutableMatrix(V_xt_tlag[t - 1]);
    }

    /**
     * Get the prior observation prediction.
     *
     * @param t time, t &ge; 1
     * @return the predicted observation
     */
    public ImmutableVector getPredictedObservation(int t) {
        return new ImmutableVector(E_yt_tlag.getRow(t));
    }

    /**
     * Get the prior observation prediction variance.
     *
     * @param t time, t &ge; 1
     * @return the predicted observation variance
     */
    public ImmutableMatrix getPredictedObservationVariance(int t) {
        return new ImmutableMatrix(V_yt_tlag[t - 1]);
    }

    /**
     * Get the Kalman gain.
     *
     * @param t time, t &ge; 1
     * @return the Kalman gain
     */
    public ImmutableMatrix getKalmanGain(int t) {
        return new ImmutableMatrix(KalmanGain[t - 1]);
    }
}
