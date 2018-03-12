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
package com.numericalmethod.suanshu.analysis.function.special.gaussian;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * The inverse of the cumulative standard Normal distribution function is defined as:
 * \[
 * N^{-1}(u)
 * /]
 * <p/>
 * This implementation uses the Beasley-Springer-Moro algorithm.
 * It has a maximum absolute error of 3e-9 out to seven standard deviations.
 * The error is maximal when <i>u</i> is around 0.5.
 * <p/>
 * The R equivalent function is {@code qnorm}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Moro, B, The full monte. Risk 8 (Feb): 57-58, 1995"
 * <li><a href="http://en.wikipedia.org/wiki/Cumulative_normal#Cumulative_distribution_function">Wikipedia: Cumulative distribution function</a>
 * </ul>
 */
public class CumulativeNormalInverse extends UnivariateRealFunction {

    private static final double[] a = new double[]{
        2.50662823884,
        -18.61500062529,
        41.39119773534,
        -25.44106049637
    };
    private static final double[] b = new double[]{
        -8.4735109309,
        23.08336743743,
        -21.06224101826,
        3.13082909833
    };
    private static final double[] c = new double[]{
        0.3374754822726147,
        0.9761690190917186,
        0.1607979714918209,
        0.0276438810333863,
        0.0038405729373609,
        0.0003951896511919,
        0.0000321767881768,
        0.0000002888167364,
        0.0000003960315187
    };

    @Override
    public double evaluate(double u) {
        double r, x;
        double y = u - 0.5;
        if (Math.abs(y) < 0.42) {
            r = y * y;
            x = y * (((a[3] * r + a[2]) * r + a[1]) * r + a[0]);
            x /= (((b[3] * r + b[2]) * r + b[1]) * r + b[0]) * r + 1d;
        } else {
            r = y < 0 ? u : 1d - u;
            r = Math.log(-Math.log(r));

            x = c[8];
            for (int i = 7; i >= 0; --i) {
                x = c[i] + r * x;
            }

            x = y > 0 ? x : -x;
        }

        return x;
    }
}
