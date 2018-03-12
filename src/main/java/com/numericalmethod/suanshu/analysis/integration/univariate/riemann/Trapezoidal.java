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

/**
 * The Trapezoidal rule is a closed type Newton–Cotes formula, where the integral interval is evenly divided into <i>N</i> sub-intervals.
 * Each sub-interval is divided into two in each iteration.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Trapezoidal_rule">Wikipedia: Trapezoidal rule</a>
 */
public class Trapezoidal extends NewtonCotes {

    /**
     * Construct an integrator that implements the Trapezoidal rule.
     *
     * @param precision     the convergence threshold
     * @param maxIterations the maximum number of iterations
     */
    public Trapezoidal(double precision, int maxIterations) {
        super(2, NewtonCotes.Type.CLOSED, precision, maxIterations);
    }
}
