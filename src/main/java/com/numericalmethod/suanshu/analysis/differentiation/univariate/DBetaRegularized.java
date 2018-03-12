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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularized;
import com.numericalmethod.suanshu.analysis.function.special.beta.LogBeta;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * This is the first order derivative function of the Regularized Incomplete Beta function, {@link BetaRegularized}, w.r.t the upper limit, <i>x</i>.
 * \[
 * {\partial \over \partial x} \mathrm{B_x}(p, q) = \frac{x^{p-1}(1-x)^{q-1}}{\mathrm{B_x}(p, q)}
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Beta_function#Incomplete_beta_function">Wikipedia: Incomplete beta function</a>
 * @see BetaRegularized
 */
public class DBetaRegularized extends UnivariateRealFunction {

    private final double p;//the shape parameter
    private final double q;//the shape parameter
    private static final LogBeta lbeta = new LogBeta();

    /**
     * Construct the derivative function of the Regularized Incomplete Beta function, {@link BetaRegularized}.
     *
     * @param p the shape parameter
     * @param q the shape parameter
     */
    public DBetaRegularized(double p, double q) {
        this.p = p;
        this.q = q;
    }

    /**
     * Evaluate \({\partial \over \partial x} \mathrm{B_x}(p, q)\).
     *
     * @param x \(0 \le x \le 1\)
     * @return \({\partial \over \partial x} \mathrm{B_x}(p, q)\)
     */
    @Override
    public double evaluate(double x) {
        double fx = (p - 1) * log(x);
        fx += (q - 1) * log(1 - x);
        fx -= lbeta.evaluate(p, q);
        fx = exp(fx);

        return fx;
    }
}
