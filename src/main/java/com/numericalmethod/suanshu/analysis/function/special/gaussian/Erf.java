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
 * The Error function is defined as:
 * \[
 * \operatorname{erf}(x) = \frac{2}{\sqrt{\pi}}\int_{0}^x e^{-t^2} dt
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Paul Glasserman, "Section 2.3," Monte Carlo Methods in Financial Engineering."
 * <li><a href="http://en.wikipedia.org/wiki/Error_function">Wikipedia: Error function</a>
 * </ul>
 */
public class Erf extends UnivariateRealFunction {

    private static StandardCumulativeNormal N = new CumulativeNormalMarsaglia();

    @Override
    public double evaluate(double x) {
        double y = N.evaluate(ROOT_2 * x);
        y = 2. * y - 1;
        return y;
    }
}
