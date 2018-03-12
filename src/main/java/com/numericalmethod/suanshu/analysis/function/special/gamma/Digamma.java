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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.sequence.Summation;
import static com.numericalmethod.suanshu.number.DoubleUtils.isNegative;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The digamma function is defined as the logarithmic derivative of the gamma function. That is,
 * \[
 * \psi(x) =\frac{d}{dx} \ln{\Gamma(x)}= \frac{\Gamma'(x)}{\Gamma(x)}
 * \]
 * <p/>
 * This implementation is based on Algorithm 610.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"D. E. Amos, "Algorithm 610: A Portable FORTRAN Subroutine for Derivatives of the Psi Function," ACM Transactions on Mathematical Software (TOMS), Volume 9, Issue 4 (December 1983), p. 494 - 502."
 * <li>"Shanjie Zhang, Jianming Jin, Computation of Special Functions. Wiley-Interscience; Har/Dis edition (July 12, 1996)."
 * <li><a href="http://en.wikipedia.org/wiki/Digamma_function">Wikipedia: Digamma function</a>
 * </ul>
 */
public class Digamma extends UnivariateRealFunction {

    /* ± the Bernoulli numbers divided by 2k, except for the first one (-0.5) */
    private static final double[] coefficients = new double[]{
        -0.5,
        -0.0833333333333333,
        0.008333333333333333,
        -0.003968253968253968,
        0.004166666666666667,
        -0.007575757575757576,
        0.02109279609279609,
        -0.08333333333333334,
        0.4432598039215686,
        -3.05395433027012,
        26.45621212121212,
        -281.4601449275362,
        3607.510546398047,
        -54827.5833333333,
        974936.823850575,
        -20052695.79668808,
        472384867.72163,
        -12635724795.91667,
        380879311252.4537,
        -12850850499305.08,
        482414483548501.7
    };
    private static final double BIG_X = 10;

    @Override
    public double evaluate(final double x) {
        double phi = 0;

        if (Double.isNaN(x)) {
            throw new RuntimeException("x = NaN");
        } else if (isZero(x, 0)) {
            phi = Double.NEGATIVE_INFINITY;
        } else if (isNegative(x, 0)) {
            phi = evaluate(-x) - 1 / x + PI / tan(PI * -x);//eq. 3.3.11
        } else if (x >= BIG_X) {//asymptotic expansion
            phi = log(x);
            phi += coefficients[0] / x;
            for (int k = 1; k < coefficients.length; ++k) {
                phi += coefficients[k] / pow(x, 2 * k);
            }
        } else {//for small x, backward recurrence
            int n = (int) Math.ceil(BIG_X - x);
            phi = evaluate(x + n);
            phi -= new Summation(new Summation.Term() {

                @Override
                public double evaluate(double k) {
                    return 1 / (x + k);
                }
            }).sum(0, n - 1);
        }

        return phi;
    }
}
