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

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;

/**
 * The Regularized Incomplete Gamma P function is defined as:
 * \[
 * P(s,x) = \frac{\gamma(s,x)}{\Gamma(s)} = 1 - Q(s,x), s \geq 0, x \geq 0
 * \]
 * <p/>
 * The R equivalent function is {@code pgamma}. E.g., {@code pgamma(x, s, lower=TRUE)}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Regularized_Gamma_function#Regularized_Gamma_functions_and_Poisson_random_variables">Wikipedia: Regularized Gamma functions and Poisson random variables</a>
 */
public class GammaRegularizedP extends BivariateRealFunction {

    private final GammaRegularizedQ qgamma = new GammaRegularizedQ();

    /**
     * Evaluate <i>P(s,x)</i>.
     *
     * @param s <i>s &ge; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return <i>P(s,x)</i>
     */
    @Override
    public double evaluate(double s, double x) {
        return 1 - qgamma.evaluate(s, x);
    }
}
