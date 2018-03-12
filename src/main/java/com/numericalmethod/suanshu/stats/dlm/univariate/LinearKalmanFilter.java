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
package com.numericalmethod.suanshu.stats.dlm.univariate;

import com.numericalmethod.suanshu.misc.R;
import java.util.Arrays;

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
    private double[] E_xt_tlag;
    /**
     * the variances of state predictions
     * <blockquote><i>
     * R_{t|t-1} = Var(x_t|y_{1:t-1})
     * </i></blockquote>
     */
    private double[] V_xt_tlag;
    /**
     * the observation predictions
     * <blockquote><i>
     * f_t = E(y_t|y_{1:t-1})
     * </i></blockquote>
     */
    private double[] E_yt_tlag;
    /**
     * the variances of observation predictions
     * <blockquote><i>
     * Q_t = Var(y_t|y_{1:t-1})
     * </i></blockquote>
     */
    private double[] V_yt_tlag;
    /** the optimal Kalman gain matrices */
    private double[] KalmanGain;
    /**
     * the state updates
     * <blockquote><i>
     * x_{t|t} = E(x_t|y_{1:t})
     * </i></blockquote>
     */
    private double[] E_xt_t;
    /**
     * the variances of state updates
     * <blockquote><i>
     * R_{t|t} = Var(x_t|y_{1:t})
     * </i></blockquote>
     */
    private double[] V_xt_t;

    /**
     * Construct a Kalman filter from a univariate controlled dynamic linear model.
     *
     * @param model a univariate controlled DLM
     */
    public LinearKalmanFilter(DLM model) {
        this.model = model;
    }

    /**
     * Filter the observations.
     *
     * @param Y the observations
     * @param U the controls
     */
    public void filtering(double[] Y, double[] U) {
        final StateEquation state = model.getStateModel();
        final ObservationEquation observation = model.getObservationModel();

        final int T = Y.length;

        E_xt_tlag = new double[T];
        V_xt_tlag = new double[T];

        E_yt_tlag = new double[T];
        V_yt_tlag = new double[T];

        KalmanGain = new double[T];

        E_xt_t = new double[T];
        V_xt_t = new double[T];

        for (int t = 0; t < T; ++t) {
            double x_tlag_tlag_temp = t > 0 ? E_xt_t[t - 1] : model.m0();
            // x_{t | t - 1} = G_t * x_{t - 1 | t - 1} + H_t * u_t
            double x_t_tlag = state.xt_mean(t, x_tlag_tlag_temp, U[t]);
            E_xt_tlag[t] = x_t_tlag;

            //R_{t | t - 1} = G_t * R_{t - 1 | t - 1} * G_t' + W_t
            double R_tlag_tlag = t > 0 ? V_xt_t[t - 1] : model.C0();
            double R_t_tlag = state.xt_var(t, R_tlag_tlag);
            V_xt_tlag[t] = R_t_tlag;

            //f_t = F_t * x_{t | t - 1}
            double y_t_tlag = observation.yt_mean(t, x_t_tlag);
            E_yt_tlag[t] = y_t_tlag;

            //Q_{t | t - 1} = F_t * R_{t | t - 1} * F_t' + V_t
            double Q_t_tlag = observation.yt_var(t, R_t_tlag);
            V_yt_tlag[t] = Q_t_tlag;

            //K_t = R_{t | t - 1} * F_t' * (Q_{t | t - 1} ^ (-1))
            double F = observation.F(t);
            KalmanGain[t] = R_t_tlag * F / Q_t_tlag;//TODO: generalize this computation?

            //e_t = y_t - f_t (observation residual)
            double e_t = Y[t] - y_t_tlag;

            //x_{t | t} = x_{t | t - 1} + K_t * e_t
            double x_t_t = x_t_tlag + (KalmanGain[t] * (e_t));
            E_xt_t[t] = x_t_t;

            //R_{t | t} = (I - K_t * F_t) * R_{t | t - 1}
            double R_t_t = R_t_tlag - (KalmanGain[t] * F * R_t_tlag);
            V_xt_t[t] = R_t_t;
        }
    }

    /**
     * Filter the observations without control variable.
     *
     * @param Y the observations
     */
    public void filtering(double[] Y) {
        filtering(Y, R.rep(0.0, Y.length));
    }

    /**
     * Get <i>T</i>, the number of hidden states or observations.
     *
     * @return <i>T</i>
     */
    public int size() {
        return E_xt_t.length;
    }

    /**
     * Get the posterior expected states.
     *
     * @return the fitted states
     */
    public double[] getFittedStates() {
        return Arrays.copyOf(E_xt_t, E_xt_t.length);
    }

    /**
     * Get the prior expected states.
     *
     * @return the predicted states
     */
    public double[] getPredictedStates() {
        return Arrays.copyOf(E_xt_tlag, E_xt_tlag.length);
    }

    /**
     * Get the prior observation predictions.
     *
     * @return the predicted observations
     */
    public double[] getPredictedObservations() {
        return Arrays.copyOf(E_yt_tlag, E_yt_tlag.length);
    }

    /**
     * Get the posterior expected state.
     *
     * @param t time, t &ge; 1
     * @return the fitted state
     */
    public double getFittedState(int t) {
        return E_xt_t[t - 1];
    }

    /**
     * Get the posterior expected state variance.
     *
     * @param t time, t &ge; 1
     * @return the fitted state variance
     */
    public double getFittedStateVariance(int t) {
        return V_xt_t[t - 1];
    }

    /**
     * Get the prior expected state.
     *
     * @param t time, t &ge; 1
     * @return the predicted state
     */
    public double getPredictedState(int t) {
        return E_xt_tlag[t - 1];
    }

    /**
     * Get the prior expected state variance.
     *
     * @param t time, t &ge; 1
     * @return the predicted state variance
     */
    public double getPredictedStateVariance(int t) {
        return V_xt_tlag[t - 1];
    }

    /**
     * Get the prior observation prediction.
     *
     * @param t time, t &ge; 1
     * @return the predicted observation
     */
    public double getPredictedObservation(int t) {
        return E_yt_tlag[t - 1];
    }

    /**
     * Get the prior observation prediction variance.
     *
     * @param t time, t &ge; 1
     * @return the predicted observation variance
     */
    public double getPredictedObservationVariance(int t) {
        return V_yt_tlag[t - 1];
    }

    /**
     * Get the Kalman gain.
     *
     * @param t time, t &ge; 1
     * @return the Kalman gain
     */
    public double getKalmanGain(int t) {
        return KalmanGain[t - 1];
    }
}
