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
import static java.lang.Math.*;

/**
 * This transformation is good for the region \((0, +\infty)\).
 * The tricky part of using this transformation is to figure out a good range for <i>t</i>.
 * If there is information about the integrand available, {@link SubstitutionRule#ta()} and {@link SubstitutionRule#tb()} should be overridden.
 * The substitution is
 * <pre><i>
 * x = exp(2c sinh(t))
 * </i></pre>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Tanh-sinh_quadrature">Wikipedia: Tanh-sinh quadrature</a>
 */
public class DoubleExponential4HalfRealLine extends DoubleExponential {

    /**
     * Construct a {@code DoubleExponential4HalfRealLine} substitution rule.
     *
     * @param f the integrand
     * @param a the lower limit
     * @param b the upper limit
     * @param c usually either 0 or 0.5 * PI
     */
    public DoubleExponential4HalfRealLine(final UnivariateRealFunction f, final double a, final double b, final double c) {
        super(xt(c), dxdt(c), f, a, b, c);
    }

    /** x(t) */
    private static UnivariateRealFunction xt(final double c) {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return exp(2 * c * sinh(t));
            }
        };
    }

    /** x'(t) = dx(t)/dt */
    private static UnivariateRealFunction dxdt(final double c) {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return 2 * c * cosh(t) * exp(2 * c * sinh(t));
            }
        };
    }
}
