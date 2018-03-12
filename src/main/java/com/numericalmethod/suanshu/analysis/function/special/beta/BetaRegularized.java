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
package com.numericalmethod.suanshu.analysis.function.special.beta;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.ContinuedFraction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The Regularized Incomplete Beta function is defined as:
 * \[
 * I_x(p,q) = \frac{B(x;\,p,q)}{B(p,q)} = \frac{1}{B(p,q)} \int_0^x t^{p-1}\,(1-t)^{q-1}\,dt, p > 0, q > 0
 * \]
 * <p/>
 * The R equivalent function is {@code pbeta}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Amparo Gil, Javier Segura, and Nico M. Temme. "Sections 6.7," Numerical Methods for Special Functions."
 * <li><a href="http://en.wikipedia.org/wiki/Beta_function#Incomplete_beta_function">Wikipedia: Incomplete beta function</a>
 * </ul>
 */
public class BetaRegularized extends UnivariateRealFunction {

    private final double p;//the shape parameter
    private final double q;//the shape parameter
    private final ContinuedFraction cf;
    private static final LogBeta lbeta = new LogBeta();

    /**
     * Construct an instance of <i>I<sub>x</sub>(p,q)</i> with the parameters <i>p</i> and <i>q</i>.
     *
     * @param p <i>p > 0</i>
     * @param q <i>q > 0</i>
     */
    public BetaRegularized(final double p, final double q) {
        this.p = p;
        this.q = q;
        cf = new ContinuedFraction(new ContinuedFraction.Partials() {

            @Override
            public double A(int n, double x) {
                double An = 0;

                if (n % 2 == 1) {//odd
                    int m = (n - 1) / 2;
                    An = -(p + m) * (p + q + m);
                    An /= (p + 2 * m) * (p + 2 * m + 1);
                    An *= x;
                } else {//even
                    int m = (n - 2) / 2;
                    An = (m + 1) * (q - m - 1);
                    An /= (p + 2 * m + 1) * (p + 2 * m + 2);
                    An *= x;
                }

                return An;
            }

            @Override
            public double B(int n, double x) {
                return 1;
            }
        });
    }

    /**
     * Get <i>p</i>, the shape parameter.
     *
     * @return <i>p</i>
     */
    public double p() {
        return p;
    }

    /**
     * Get <i>q</i>, the shape parameter.
     *
     * @return <i>q</i>
     */
    public double q() {
        return q;
    }

    /**
     * Evaluate <i>I<sub>x</sub>(p,q)</i>.
     *
     * @param x <i>0 <= x <= 1</i>
     * @return <i>I<sub>x</sub>(p,q)</i>
     */
    @Override
    public double evaluate(final double x) {
        if (isZero(x, 0)) {
            return 0;
        }

        if (equal(x, 1., 0)) {
            return 1;
        }

        double x0 = (p + 1) / (p + q + 2);
        if (x > x0) {
            BetaRegularized Ix = new BetaRegularized(q, p);
            return 1d - Ix.evaluate(1 - x);
        }

        double result = p * log(x) + q * log(1 - x) - log(p) - lbeta.evaluate(p, q);
        result = exp(result);
        /*
         * Eqs. 6.78, 6.79
         * Numerical Methods for Special Functions
         */
        result /= cf.evaluate(x);

        return result;
    }
}
