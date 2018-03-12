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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * A substitution rule specifies \(x(t)\) and \(\frac{\mathrm{d} x}{\mathrm{d} t}\).
 * <p/>
 * We set
 * /[
 * x = x(t)
 * t = x^{-1}(x) = t(x)
 * /]
 * such that,
 * /[
 * \int_{a}^{b} f(x)\,dx = \int_{t(a)}^{t(b)} f(x)x'(t)\, dt
 * /]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Substitution_rule">Wikipedia: Integration by substitution</a>
 */
public interface SubstitutionRule {

    /**
     * the transformation: <i>x(t)</i>
     *
     * @return <i>x(t)</i>
     */
    public UnivariateRealFunction x();

    /**
     * the first order derivative of the transformation: <i>x'(t) = dx(t)/dt</i>
     *
     * @return <i>x'(t) = dx(t)/dt</i>
     */
    public UnivariateRealFunction dx();

    /**
     * Get the lower limit of the integral.
     *
     * @return the lower limit
     */
    public double ta();

    /**
     * Get the upper limit of the integral.
     *
     * @return the upper limit
     */
    public double tb();
}
