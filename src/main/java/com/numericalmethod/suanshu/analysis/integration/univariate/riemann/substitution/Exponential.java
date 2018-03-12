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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * This transformation is good for when the lower limit is finite, the upper limit is infinite, and the integrand falls off exponentially.
 * The integrator for this substitution should use an OPEN formula to avoid computing for the end point where <i>t = 0</i>.
 * The substitution is
 * \[
 * \int_{a}^{\infty}f(x)dx = \int_{0}^{e^a}f(-\log (t))\frac{\mathrm{d} t}{t}
 * \]
 *
 * @author Haksun Li
 */
public class Exponential implements SubstitutionRule {

    private final double a;//the lower limit

    /**
     * Construct an {@code Exponential} substitution rule.
     *
     * @param a the lower limit
     */
    public Exponential(double a) {
        this.a = a;
    }

    @Override
    public UnivariateRealFunction x() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return -log(t);
            }
        };
    }

    @Override
    public UnivariateRealFunction dx() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return 1 / t;
            }
        };
    }

    @Override
    public double ta() {
        return 0;
    }

    @Override
    public double tb() {
        return exp(-a);
    }
}
