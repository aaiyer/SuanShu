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
package com.numericalmethod.suanshu.optimization.constrained.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.NonNegativityConstraints;

/**
 * This is a constrained optimization problem for a function which has all non-negative variables.
 * \[
 * \min_x f(x)
 * \]
 * s.t.,
 * <blockquote><pre><i>
 * x &ge; 0
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 */
public class NonNegativityConstraintOptimProblem implements ConstrainedOptimProblem {

    private final ConstrainedOptimProblemImpl1 problem;

    /**
     * Construct a constrained optimization problem with only non-negative variables.
     *
     * @param f the objective function
     */
    public NonNegativityConstraintOptimProblem(RealScalarFunction f) {
        this.problem = new ConstrainedOptimProblemImpl1(
                f,
                null,
                new NonNegativityConstraints(f).toLessThanConstraints());
    }

    @Override
    public LessThanConstraints getLessThanConstraints() {
        return problem.getLessThanConstraints();
    }

    @Override
    public EqualityConstraints getEqualityConstraints() {
        return problem.getEqualityConstraints();
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }
}
