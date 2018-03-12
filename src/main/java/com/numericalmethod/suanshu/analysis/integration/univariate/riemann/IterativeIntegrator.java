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
 * An iterative integrator computes an integral by a series of sums, which approximates the value of the integral.
 * The sum is refined iteratively, generally, by using a finer discretization of the integrand.
 *
 * @author Haksun Li
 */
public interface IterativeIntegrator extends Integrator {

    /**
     * Get the discretization size for the current iteration.
     *
     * @return the discretization size
     */
    public double h();

    /**
     * Compute a refined sum for the integral.
     *
     * @param iteration the index/count for the current iteration, counting from 1
     * @param f         the integrand
     * @param a         the lower limit
     * @param b         the upper limit
     * @param sum0      the last sum
     * @return a refined sum
     */
    public double next(int iteration, UnivariateRealFunction f, double a, double b, double sum0);

    /**
     * Get the maximum number of iterations for this iterative procedure.
     * For those integrals that do not converge, we need to put a bound on the number of iterations to avoid infinite looping.
     *
     * @return the maximum number of iterations
     */
    public int getMaxIterations();
}
