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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution.SubstitutionRule;

/**
 * Change of variable can easy the computation of some integrals, such as improper integrals.
 * The idea is to transform a dependent variable, <i>x</i>, to another variable, <i>t</i>, so that the "new" integral is easier to compute.
 * <p/>
 * We set
 * /[
 * x = x(t)
 * t = x^{-1}(x) = t(x)
 * /]
 * such that,
 * /[
 * \int_{a}^{b} f(x)\,dx = \int_{t(a)}^{t(b)} f(x)x'(t)\, dt
 * /]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Integration_by_substitution">Wikipedia: Integration by substitution</a>
 */
public class ChangeOfVariable implements Integrator {

    private final Integrator integrator;
    private final SubstitutionRule change;

    /**
     * Construct an integrator that uses change of variable to do integration.
     *
     * @param change     the substitution formula
     * @param integrator the integrator.
     * If there is a singularity at an endpoint, the integrator should use an <em>open</em> formula such as {@link Midpoint};
     * otherwise, use an integrator with a <em>closed</em> formula such as {@link Trapezoidal}.
     */
    public ChangeOfVariable(SubstitutionRule change, Integrator integrator) {
        this.integrator = integrator;
        this.change = change;
    }

    @Override
    public double integrate(final UnivariateRealFunction f, double a, double b) {
        UnivariateRealFunction g = fdx(f);

        double ta = change.ta();
        double tb = change.tb();

        return integrator.integrate(g, ta, tb);
    }

    /**
     * Get the integrand in the "transformed" integral, <i>g(t) = f(x(t)) * x'(t)</i>.
     *
     * @param f the integrand in the original integral
     * @return the integrand in the "transformed" integral
     */
    public UnivariateRealFunction fdx(final UnivariateRealFunction f) {
        UnivariateRealFunction g = new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                double xt = change.x().evaluate(t);//x(t)
                double dxt = change.dx().evaluate(t);//x'(t) = dx(t)
                double fxt = f.evaluate(xt);
                return fxt * dxt;//f(x(t)) * x'(t)
            }
        };

        return g;
    }

    @Override
    public double getPrecision() {
        return integrator.getPrecision();
    }
}
