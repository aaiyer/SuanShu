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
package com.numericalmethod.suanshu.optimization.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;

/**
 * This is an optimization problem that minimizes a real valued objective function, one or multi dimension.
 * Optimization, or mathematical programming, refers to choosing the best element from some set of available alternatives.
 * In the simplest case,
 * this means solving problems in which one seeks to minimize (or maximize) a real function by systematically choosing the values of real or integer variables from within an allowed set.
 * The generalization of optimization theory and techniques to other formulations comprises a large area of applied mathematics.
 * More generally, it means finding "best available" values of some objective function given a defined domain,
 * including a variety of different types of objective functions and different types of domains.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Mathematical_programming">Wikipedia: Optimization (mathematics)</a>
 */
public interface OptimProblem {

    /**
     * Get the number of variables.
     *
     * @return the number of variables.
     */
    public int dimension();

    /**
     * Get the objective function.
     *
     * @return the objective function
     */
    public RealScalarFunction f();
}
