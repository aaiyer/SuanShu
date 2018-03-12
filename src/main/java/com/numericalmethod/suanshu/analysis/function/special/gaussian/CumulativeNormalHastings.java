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

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * Hastings algorithm is faster but less accurate way to compute the cumulative standard Normal.
 * It has a maximum absolute error less than 7.5e-8.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Hastings, C., Jr. "Approximations for Digital Computers," Princeton University Press, Princeton, NJ. 1995."
 * <li>"Abramowitz, M., and Stegun, I.A, Handbook of Mathematical Functions, National Bureau of Standards, Washington, D.C. Reprinted by Dover, New York. 1964."
 * </ul>
 */
public class CumulativeNormalHastings extends UnivariateRealFunction implements StandardCumulativeNormal {

    private static final double b1 = 0.31938153;
    private static final double b2 = -0.356563782;
    private static final double b3 = 1.781477937;
    private static final double b4 = -1.821255978;
    private static final double b5 = 1.330274429;
    private static final double p = 0.2316419;
    private static final double c = Math.log(Constant.ROOT_2_PI);

    @Override
    public double evaluate(double x) {
        double a = Math.abs(x);
        double t = 1d / (1d + a * p);
        double s = ((((b5 * t + b4) * t + b3) * t + b2) * t + b1) * t;
        double y = s * Math.exp(-0.5 * x * x - c);
        y = x < 0 ? y : 1d - y;
        return y;
    }
}
