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
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import static java.lang.Math.*;

/**
 * The trigamma function is defined as the logarithmic derivative of the digamma function. That is,
 * \[
 * \psi_1(z) = \frac{d}{dz} \psi(z)
 * \]
 * This implementation is based on Algorithm 121.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"B. E. Schneider, "Algorithm 121: Trigamma Function," JSTOR. Vol. 27, No. 1, 1978."
 * <li><a href="http://en.wikipedia.org/wiki/Trigamma_function">Wikipedia: Trigamma function</a>
 * <li>"http://pmtksupport.googlecode.com/svn/trunk/lightspeed2.3/trigamma.m"
 * <li>"http://www.math.mcmaster.ca/peter/s743/trigamma.html"
 * <li>"http://lib.stat.cmu.edu/apstat/121"
 * </ul>
 */
public class Trigamma extends UnivariateRealFunction {

    private static final double SMALL_X = 1e-4;
    private static final double BIG_X = 30;

    @Override
    public double evaluate(final double x) {
        double fx = 0;

        if (Double.isNaN(x)) {
            throw new RuntimeException("x = NaN");
        } else if (isZero(x, 0)) {
            fx = Double.POSITIVE_INFINITY;
        } else if (isNegative(x, 0)) {
            fx = pow(PI / sin(PI * x), 2) - evaluate(1 - x);//reflection formula
        } else if (isPositive(x, 0) && x < SMALL_X) {
            fx = 1. / x / x + PI * PI / 6. + -2.404113806319188570799476 * x;
        } else if (x >= BIG_X) {//asymptotic expansion
            double z = 1. / x / x;
            fx = 0.5 * z + (1.0 + z * (1. / 6 + z * (-1. / 30 + z * (1. / 42 + z * (-1. / 30 + z * 5. / 66))))) / x;
        } else {//for small x, use the recurrence formula
            fx = evaluate(x + 1);
            fx += 1 / x / x;
        }

        return fx;
    }
}
