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
 * This is the observation equation in a controlled dynamic linear model.
 * <blockquote><i>
 * y<sub>t</sub> = F<sub>t</sub> * x<sub>t</sub> + v<sub>t</sub>
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class ObservationEquation {

    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a constant coefficient of x_t in the observation equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs a time dependent coefficient of x_t in the observation equation.
     */
    private final UnivariateRealFunction F;
    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a constant variance of v_t in the observation equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs a time dependent variance of v_t in the observation equation.
     */
    private final UnivariateRealFunction V;
    private final NormalRng rnorm;

    /**
     * Construct an observation equation.
     *
     * @param F     the coefficient function of <i>x<sub>t</sub></i>, a function of time
     * @param V     the variance function of <i>v<sub>t</sub></i>, a function of time
     * @param rnorm a standard Gaussian random number generator (for seeding)
     */
    public ObservationEquation(UnivariateRealFunction F, UnivariateRealFunction V, NormalRng rnorm) {
        assertArgument(F.dimensionOfDomain() == V.dimensionOfDomain(), "the domain dimensions of F and V must match");

        this.F = F;
        this.V = V;
        this.rnorm = rnorm;
    }

    /**
     * Construct an observation equation.
     *
     * @param F the coefficient function of <i>x<sub>t</sub></i>, a function of time
     * @param V the variance function of <i>v<sub>t</sub></i>, a function of time
     */
    public ObservationEquation(UnivariateRealFunction F, UnivariateRealFunction V) {
        this(F, V, new NormalRng(0, 1));
    }

    /**
     * Construct a time-invariant an observation equation.
     *
     * @param F     the coefficient of <i>x<sub>t</sub></i>
     * @param V     the variance of <i>v<sub>t</sub></i>
     * @param rnorm a standard Gaussian random number generator (for seeding)
     */
    public ObservationEquation(final double F, final double V, NormalRng rnorm) {
        this(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return F;
                    }
                },
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return V;
                    }
                },
                rnorm);
    }

    /**
     * Construct a time-invariant an observation equation.
     *
     * @param F the coefficient of <i>x<sub>t</sub></i>
     * @param V the variance of <i>v<sub>t</sub></i>
     */
    public ObservationEquation(final double F, final double V) {
        this(F, V, new NormalRng(0, 1));
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code ObservationEquation}
     */
    public ObservationEquation(ObservationEquation that) {
        this(that.F, that.V);
    }

    /**
     * Get the dimension of observation <i>y<sub>t</sub></i>.
     *
     * @return the dimension of observations
     */
    public int dimension() {
        return 1;
    }

    /**
     * Get <i>F(t)</i>, the coefficient of <i>x<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>F(t)</i>
     */
    public double F(int t) {
        return F.evaluate(t);
    }

    /**
     * Get <i>V(t)</i>, the variance of <i>v<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>V(t)</i>
     */
    public double V(int t) {
        return V.evaluate(t);
    }

    /**
     * Predict the next observation.
     * <blockquote><i>
     * E(y_t) = F_t * x_t
     * </i></blockquote>
     *
     * @param t  time
     * @param xt state <i>x<sub>t</sub></i>
     * @return the mean observation
     */
    public double yt_mean(int t, double xt) {
        double yt = F.evaluate(t) * xt;
        return yt;
    }

    /**
     * Get the variance of the apriori prediction for the next observation.
     * <blockquote><i>
     * Var(y_{t | t - 1}) = F_t * Var(x_{t | t - 1}) * F_t' + V_t
     * </i></blockquote>
     *
     * @param t          time
     * @param var_t_tlag <i>Var(y_{t | t - 1})</i>, the variance of the apriori prediction
     * @return <i>Var(y_{t | t - 1})</i>
     */
    public double yt_var(int t, double var_t_tlag) {
        double F = F(t);
        double V = V(t);

        double var = F * var_t_tlag * F + V;
        return var;
    }

    /**
     * Evaluate the observation equation.
     * <blockquote><i>
     * y_t = F_t * x_t + v_t
     * </i></blockquote>
     *
     * @param t  time
     * @param xt state <i>x<sub>t</sub></i>
     * @return the mean observation
     */
    public double yt(int t, double xt) {
        double yt = yt_mean(t, xt);
        double vt = rnorm.nextDouble() * Math.sqrt(V.evaluate(t));
        yt += vt;

        return yt;
    }
}
