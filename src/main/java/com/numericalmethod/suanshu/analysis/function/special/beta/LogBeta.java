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
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;

/**
 * This class represents the log of Beta function {@code log(B(x, y))}.
 *
 * <blockquote><code><pre>
 *                       Γ(x)Γ(y)
 * log(B(x, y)) = log ( ----------- ) = logΓ(x) + logΓ(y) - logΓ(x + y)
 *                        Γ(x+y)
 * </pre></code></blockquote>
 *
 * <code>x > 0, y > 0</code>
 *
 * <p>
 * The R equivalent function is {@code lbeta}.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Beta_function">Wikipedia: Beta function</a>
 */
public class LogBeta extends BivariateRealFunction {

    private final static LogGamma lgamma = new LogGamma();//(LogGamma.Method.LANCZOS, 607 / 128, 15, 30);

    /**
     * Compute {@code log(B(x,y))}.
     *
     * @param x <code>x >= 0</code>
     * @param y <code>y >= 0</code>
     * @return {@code log(B(x,y))}
     */
    @Override
    public double evaluate(double x, double y) {
        double result = lgamma.evaluate(x);
        result += lgamma.evaluate(y);
        result -= lgamma.evaluate(x + y);
        return result;
    }
}
