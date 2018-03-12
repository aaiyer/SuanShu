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

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.special.beta.Beta;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;

/**
 * This is the first order derivative function of the {@link Beta} function w.r.t <i>x</i>, \({\partial \over \partial x} \mathrm{B}(x, y)\).
 * \[
 * {\partial \over \partial x} \mathrm{B}(x, y) = \mathrm{B}(x, y) (\psi(x) - \psi(x + y))
 * \]
 * where \(x > 0, y > 0\), \(\psi\) is the {@link Digamma} function.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Beta_function#Derivatives">Wikipedia: Derivatives</a>
 * @see Beta
 */
public class DBeta extends BivariateRealFunction {

    private static final Beta beta = new Beta();
    private static final Digamma digamma = new Digamma();

    /**
     * Evaluate \({\partial \over \partial x} \mathrm{B}(x, y)\).
     *
     * @param x <i>x > 0</i>
     * @param y <i>y > 0</i>
     * @return \({\partial \over \partial x} \mathrm{B}(x, y)\)
     */
    @Override
    public double evaluate(double x, double y) {
        double fx = beta.evaluate(x, y);
        fx *= digamma.evaluate(x) - digamma.evaluate(x + y);
        return fx;
    }
}
