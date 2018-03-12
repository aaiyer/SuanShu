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
 * This complementary Error function is defined as:
 * \[
 * \operatorname{erfc}(x)
 * = 1-\operatorname{erf}(x)
 * = \frac{2}{\sqrt{\pi}} \int_x^{\infty} e^{-t^2}\,dt
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Error_function">Wikipedia: Error function</a>
 */
public class Erfc extends UnivariateRealFunction {

    private static Erf erf = new Erf();

    @Override
    public double evaluate(double x) {
        double y = 1d - erf.evaluate(x);
        return y;
    }
}
