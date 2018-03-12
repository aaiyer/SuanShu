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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.ContinuedFraction;
import com.numericalmethod.suanshu.analysis.sequence.Summation;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The Regularized Incomplete Gamma Q function is defined as:
 * \[
 * Q(s,x)=\frac{\Gamma(s,x)}{\Gamma(s)}=1-P(s,x), s \geq 0, x \geq 0
 * \]
 * The algorithm used for computing the regularized incomplete Gamma Q function depends on the values of <i>s</i> and <i>x</i>.
 * <ul>
 * <li>For \(s > 100\), <i>Q</i> is approximated using the Gauss-Legendre quadrature.
 * <li>For \(x < s + 1\), <i>Q</i> is approximated using the Pearson's series representation.
 * <li>Otherwise, <i>Q</i> is approximated using the continued fraction expression by Legendre.
 * </ul>
 * The R equivalent function is {@code pgamma}. E.g., {@code pgamma(x, s, lower=FALSE)}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"B Shea." Algorithm AS 239: Chi-squared and Incomplete Gamma Integral," Applied Statistics. Volume 37, Number 3, 1988, pages 466-473."
 * <li><a href="http://en.wikipedia.org/wiki/Regularized_Gamma_function#Regularized_Gamma_functions_and_Poisson_random_variables">Wikipedia: Regularized Gamma functions and Poisson random variables</a>
 * </ul>
 */
public class GammaRegularizedQ extends BivariateRealFunction {//TODO: extend to the Complex domain

    private static final LogGamma lgamma = new LogGamma();
    private static final double epsilon = EPSILON;

    /**
     * Evaluate <i>Q(s,x)</i>.
     *
     * @param s <i>s &ge; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return <i>Q(s,x)</i>
     */
    @Override
    public double evaluate(double s, double x) {
        SuanShuUtils.assertArgument(compare(s, 0, epsilon) >= 0, "s must be >= 0");
        SuanShuUtils.assertArgument(compare(x, 0, epsilon) >= 0, "x < 0 gives complex number; not supported yet");

        //special cases
        if (isZero(s, 0)) {//Q(0, 0) = 0
            return 0;
        } else if (isZero(x, 0)) {
            return 1;
        } else if (x > 1e8) {
            return 0;
//        } else if (s > 100) {//Gauss-Legendre quadrature
//            return evaluateByQuadrature(s, x);
        } else if (x < s + 1) {//{@code true} when x < 1 for all s; series representation converges faster
            return evaluateBySeries(s, x);
        } else {//x <= s + 1 < 100 + 1
            return evaluateByCF(s, x);
        }
    }

    private double evaluateByQuadrature(final double s, final double x) {//TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluate <i>Q(s,x)</i> using the Pearson's series representation.
     *
     * @param s <i>s &ge; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return <i>Q(s,x)</i>
     */
    private double evaluateBySeries(final double s, final double x) {
        Summation series = new Summation(new Summation.Term() {

            double term;

            @Override
            public double evaluate(double n) {
                if (n == 0) {
                    term = 1d / s;
                } else {
                    term *= x / (s + n);
                }
                return term;
            }
        }, epsilon);

        double sum = series.sumToInfinity(0);
        double result = s * log(x) - x - lgamma.evaluate(s);
        result = exp(result);

        return 1d - result * sum;
    }

    /**
     * Evaluate <i>Q(s,x)</i> using the continued fraction expression by Legendre.
     *
     * @param s <i>s &ge; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return <i>Q(s,x)</i>
     */
    private double evaluateByCF(final double s, final double x) {
        ContinuedFraction cf = new ContinuedFraction(new ContinuedFraction.Partials() {

            @Override
            public double A(int n, double u) {
                if (n == 1) {
                    return 1;
                } else {
                    return (n - 1) * (s - (n - 1));
                }
            }

            @Override
            public double B(int n, double u) {
                if (n == 0) {
                    return 0;
                } else {
                    return 2 * n - 1 - s + u;
                }
            }
        });

        double result = s * log(x) - x - lgamma.evaluate(s);
        result = exp(result);
        result *= cf.evaluate(x);

        return result;
    }
}
