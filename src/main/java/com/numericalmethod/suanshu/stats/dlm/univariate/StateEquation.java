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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;

/**
 * This is the state equation in a controlled dynamic linear model.
 * <blockquote><i>
 * x<sub>t</sub> = G<sub>t</sub> * x<sub>t-1</sub> + H<sub>t</sub> * u<sub>t</sub> + w<sub>t</sub>,
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class StateEquation {

    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a constant coefficient of x_{t_1} in the state equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs the coefficient of x_{t_1} in the state equation.
     */
    private final UnivariateRealFunction G;
    /**
     * For a time-invariant controlled DLM,
     * this represents a constant coefficient of u_t.
     * <p/>
     * For a time varying controlled DLM,
     * this outputs the coefficient of u_t.
     */
    private final UnivariateRealFunction H;
    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a constant variance of {w_t} in the state equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs the variance of {w_t} in the state equation.
     */
    private final UnivariateRealFunction W;
    private final NormalRng rnorm;

    /**
     * Construct a state equation.
     *
     * @param G     the coefficient function of <i>x<sub>t - 1</sub></i>
     * @param H     the coefficient function of control variables <i>u<sub>t</sub></i>
     * @param W     the variance function of <i>w<sub>t</sub></i>
     * @param rnorm a standard Gaussian random number generator (for seeding)
     */
    public StateEquation(UnivariateRealFunction G, UnivariateRealFunction H, UnivariateRealFunction W, NormalRng rnorm) {
        assertArgument(G.dimensionOfDomain() == H.dimensionOfDomain() && H.dimensionOfDomain() == W.dimensionOfDomain(),
                       "the domain dimensions of F and V and W must match");

        this.G = G;
        this.H = H;
        this.W = W;
        this.rnorm = rnorm;
    }

    /**
     * Construct a state equation.
     *
     * @param G the coefficient function of <i>x<sub>t - 1</sub></i>
     * @param H the coefficient function of control variables <i>u<sub>t</sub></i>
     * @param W the variance function of <i>w<sub>t</sub></i>
     */
    public StateEquation(UnivariateRealFunction G, UnivariateRealFunction H, UnivariateRealFunction W) {
        this(G, H, W, new NormalRng(0, 1));
    }

    /**
     * Construct a state equation without control variables.
     *
     * @param G the coefficient function of <i>x<sub>t - 1</sub></i>
     * @param W the variance function of <i>w<sub>t</sub></i>
     */
    public StateEquation(UnivariateRealFunction G, UnivariateRealFunction W) {
        this(
                G,
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 0;
                    }
                },
                W);
    }

    /**
     * Construct a time-invariant state equation.
     *
     * @param G     the coefficient of <i>x<sub>t - 1</sub></i>
     * @param H     the coefficient of control variables <i>u<sub>t</sub></i>
     * @param W     the variance of <i>w<sub>t</sub></i>
     * @param rnorm a standard Gaussian random number generator
     */
    public StateEquation(final double G, final double H, final double W, NormalRng rnorm) {
        this(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return G;
                    }
                },
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return H;
                    }
                },
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return W;
                    }
                },
                rnorm);
    }

    /**
     * Construct a time-invariant state equation without control variables.
     *
     * @param G the coefficient of <i>x<sub>t - 1</sub></i>
     * @param W the variance of <i>w<sub>t</sub></i>
     */
    public StateEquation(double G, double W) {
        this(G, 0, W, new NormalRng(0, 1));
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code StateEquation}
     */
    public StateEquation(StateEquation that) {
        this(that.G, that.H, that.W);
    }

    /**
     * Get the dimension of state <i>x<sub>t</sub></i>.
     *
     * @return the dimension of states
     */
    public int dimension() {
        return 1;
    }

    /**
     * Get <i>G(t)</i>, the coefficient of <i>x<sub>t - 1</sub></i>.
     *
     * @param t time
     * @return <i>G(t)</i>
     */
    public double G(int t) {
        return G.evaluate(t);
    }

    /**
     * Get <i>H(t)</i>, the variance of <i>u<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>H(t)</i>
     */
    public double H(int t) {
        return H.evaluate(t);
    }

    /**
     * Get <i>W(t)</i>, the variance of <i>w<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>W(t)</i>
     */
    public double W(int t) {
        return W.evaluate(t);
    }

    /**
     * Predict the next state.
     * <blockquote><i>
     * E(x_t) = G_t * x_{t - 1} + H_t * u_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @param ut   the control variable <i>u<sub>t</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public double xt_mean(int t, double xt_1, double ut) {
        double xt = G.evaluate(t) * xt_1;

        if (H != null) {
            double Hu = H.evaluate(t) * ut;
            xt += Hu;
        }

        return xt;
    }

    /**
     * Predict the next state without control variable.
     * <blockquote><i>
     * E(x_t) = G_t * x_{t - 1} + H_t * u_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public double xt_mean(int t, double xt_1) {
        return xt_mean(t, xt_1, 0);
    }

    /**
     * Get the variance of the apriori prediction for the next state.
     * <blockquote><i>
     * Var(x_{t | t - 1}) = G_t * Var(x_{t - 1| t - 1}) * G_t' + W_t
     * </i></blockquote>
     *
     * @param t             time
     * @param var_tlag_tlag <i>Var(x_{t - 1 | t - 1})</i>, the variance of the posterior update
     * @return <i>Var(x_{t | t - 1})</i>
     */
    public double xt_var(int t, double var_tlag_tlag) {
        double G = G(t);
        double W = W(t);

        double var = G * var_tlag_tlag * G + W;
        return var;
    }

    /**
     * Evaluate the state equation.
     * <blockquote><i>
     * x_t = G_t * x_{t - 1} + H_t * u_t + w_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @param ut   the control variable <i>u<sub>t</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public double xt(int t, double xt_1, double ut) {
        double xt = xt_mean(t, xt_1, ut);
        double wt = rnorm.nextDouble() * Math.sqrt(W.evaluate(t));
        xt += wt;

        return xt;
    }

    /**
     * Evaluate the state equation without the control variable.
     * <blockquote><i>
     * x_t = G_t * x_{t - 1} + H_t * u_t + w_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public double xt(int t, double xt_1) {
        return xt(t, xt_1, 0);
    }
}
