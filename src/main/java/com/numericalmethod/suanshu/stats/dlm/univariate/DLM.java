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

/**
 * This is the multivariate controlled DLM (controlled Dynamic Linear Model) specification.
 * A controlled DLM, for <i>(t &ge; 1)</i>, is described by two equations: the observation and state equations.
 * <p/>
 * Observation Equation:
 * <blockquote><i>
 * y<sub>t</sub> = F<sub>t</sub> * x<sub>t</sub> + v<sub>t</sub>,
 * </i></blockquote>
 * State Equation:
 * <blockquote><i>
 * x<sub>t</sub> = G<sub>t</sub> * x<sub>t-1</sub> + H<sub>t</sub> * u<sub>t</sub> + w<sub>t</sub>,
 * </i></blockquote>
 * <i>{y<sub>t</sub>}</i> are the observation vectors; <i>{x<sub>t</sub>}</i> are the state vectors.
 * <i>F<sub>t</sub></i> and <i>G<sub>t</sub></i> are known matrices of dimension (number of observations * number of states) and (number of states * number of states) respectively.
 * <i>{v<sub>t</sub>}</i> and <i>{w<sub>t</sub>}</i> are two independent sequences of independent normal random vectors
 * with mean zero and known variance matrices <i>{V<sub>t</sub>}</i> and <i>{W<sub>t</sub>}</i>, respectively;
 * Furthermore, it is assumed that <i>x<sub>0</sub></i> is independent of <i>{v<sub>t</sub>}</i> and <i>{w<sub>t</sub>}</i> and
 * is normally distributed with mean <i>m<sub>0</sub></i> and covariance matrix <i>C<sub>0</sub></i>,
 * where <i>m<sub>0</sub></i> is a vector of length the same as the number of states
 * and <i>C<sub>0</sub></i> is a matrix of dimension (number of states * number of states);
 *
 * <i>u<sub>t</sub></i> is an <i>m</i>-dimensional vector of control variables, i.e., the variables whose values can be regulated by the user,
 * in order to obtain a desired level of the state <i>x<sub>t</sub></i>.
 * <i>H<sub>t</sub></i> is a known matrix of coefficients, with dimension of (number of states * m).
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li> <a href="http://en.wikipedia.org/wiki/Kalman_filter#Underlying_dynamic_system_model">Wikipedia: Kalman filter - Underlying dynamic system model</a>
 * <li> G. Petris et al., "Dynamic Linear Models with R," New York, Springer, 2009, ch. 2, pp. 31-84.
 * </ul>
 */
public class DLM {

    /**
     * the mean of x_0
     */
    private final double m0;
    /**
     * the variance of x_0
     */
    private final double C0;
    /**
     * the observation equation
     */
    private final ObservationEquation Yt;
    /**
     * the state equation
     */
    private final StateEquation Xt;

    /**
     * Construct a univariate controlled dynamic linear model.
     *
     * @param m0 the mean of <i>x<sub>0</sub></i>
     * @param C0 the variance of <i>x<sub>0</sub></i>
     * @param Yt the observation equation for the model
     * @param Xt the state equation for the model
     */
    public DLM(double m0, double C0, ObservationEquation Yt, StateEquation Xt) {
        this.m0 = m0;
        this.C0 = C0;

        this.Yt = Yt;
        this.Xt = Xt;
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate controlled dynamic linear model
     */
    public DLM(DLM that) {
        this(that.m0, that.C0, that.Yt, that.Xt);
    }

    /**
     * Get the the mean of <i>x<sub>0</sub></i>.
     *
     * @return m0, the mean of <i>x<sub>0</sub></i>
     */
    public double m0() {
        return m0;
    }

    /**
     * Get the variance of <i>x<sub>0</sub></i>.
     *
     * @return C0, the variance of <i>x<sub>0</sub></i>
     */
    public double C0() {
        return C0;
    }

    /**
     * Get the dimension of observations.
     *
     * @return the dimension of observations
     */
    public int getObsDimension() {
        return 1;
    }

    /**
     * Get the dimension of states.
     *
     * @return the dimension of states
     */
    public int getStateDimension() {
        return 1;
    }

    /**
     * Get the observation model.
     *
     * @return the observation model
     */
    public ObservationEquation getObservationModel() {
        return Yt;
    }

    /**
     * Get the state model.
     *
     * @return the state model
     */
    public StateEquation getStateModel() {
        return Xt;
    }
}
