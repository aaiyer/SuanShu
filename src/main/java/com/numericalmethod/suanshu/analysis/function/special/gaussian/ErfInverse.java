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

import static com.numericalmethod.suanshu.Constant.ROOT_2;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * The inverse of the Error function is defined as:
 * \[
 * \operatorname{erf}^{-1}(x)
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Paul Glasserman, "Section 2.3," Monte Carlo Methods in Financial Engineering."
 * <li><a href="http://en.wikipedia.org/wiki/Error_function">Wikipedia: Error function</a>
 * </ul>
 */
public class ErfInverse extends UnivariateRealFunction {

    private static CumulativeNormalInverse Ninv = new CumulativeNormalInverse();

    @Override
    public double evaluate(double u) {
        double y = Ninv.evaluate((u + 1d) / 2d);
        y /= ROOT_2;
        return y;
    }
}
