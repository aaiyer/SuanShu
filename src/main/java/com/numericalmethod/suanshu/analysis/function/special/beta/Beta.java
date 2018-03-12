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

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;

/**
 * The beta function defined as:
 * \[
 * B(x,y) = \frac{\Gamma(x)\Gamma(y)}{\Gamma(x+y)}= \int_0^1t^{x-1}(1-t)^{y-1}\,dt, x > 0, y > 0
 * \]
 * <p/>
 * The R equivalent function is {@code beta}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Beta_function">Wikipedia: Beta function</a>
 */
public class Beta extends BivariateRealFunction {

    private LogBeta lbeta = new LogBeta();

    /**
     * Evaluate <i>B(x,y)</i>.
     *
     * @param x <i>x > 0</i>
     * @param y <i>y > 0</i>
     * @return <i>B(x,y)</i>
     */
    @Override
    public double evaluate(double x, double y) {
        double result = lbeta.evaluate(x, y);
        result = Math.exp(result);
        return result;
    }
}
