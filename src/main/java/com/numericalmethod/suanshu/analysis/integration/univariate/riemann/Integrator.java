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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * This defines the interface for the numerical integration of definite integrals of univariate functions.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Numerical_integration">Wikipedia: Numerical integration</a>
 */
public interface Integrator {

    /**
     * Integrate function <i>f</i> from <i>a</i> to <i>b</i>,
     * \[
     * \int_a^b\! f(x)\, dx
     * \]
     *
     * @param f a univariate function
     * @param a the lower limit
     * @param b the upper limit
     * @return \(\int_a^b\! f(x)\, dx\)
     */
    public double integrate(UnivariateRealFunction f, double a, double b);

    /**
     * Get the convergence threshold. The usage depends on the specific integrator.
     * For example, for an {@link IterativeIntegrator},
     * the integral is considered converged if the relative error of two successive sums is less than the threshold.
     *
     * @return the precision
     */
    public double getPrecision();
}
