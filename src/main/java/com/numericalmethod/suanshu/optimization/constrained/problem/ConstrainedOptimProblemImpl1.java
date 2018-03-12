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
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;

/**
 * This implements a constrained optimization problem for a function <i>f</i> subject to equality and less-than-or-equal-to constraints.
 * \[
 * \min_x f(x)
 * \]
 * s.t.,
 * <blockquote><pre><i>
 * h<sub>i</sub>(x) = 0
 * g<sub>i</sub>(x) &le; 0
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 */
public class ConstrainedOptimProblemImpl1 implements ConstrainedOptimProblem {

    private final C2OptimProblemImpl problem;
    /** the equality constraints */
    private final EqualityConstraints equal;
    /** the less-than-or-equal-to constraints */
    private final LessThanConstraints less;

    /**
     * Construct a constrained optimization problem.
     *
     * @param f     the objective function to be minimized
     * @param equal the collection of equality constraints; if the collection is empty, use {@code null}.
     * @param less  the collection of less-than-or-equal-to constraints; if the collection is empty, use {@code null}.
     */
    public ConstrainedOptimProblemImpl1(RealScalarFunction f, EqualityConstraints equal, LessThanConstraints less) {
        assertArgument(equal == null
                       || f.dimensionOfDomain() == equal.getConstraints().get(0).dimensionOfDomain(),
                       "dimension of equalities does not match that of the objective function");
        assertArgument(less == null
                       || f.dimensionOfDomain() == less.getConstraints().get(0).dimensionOfDomain(),
                       "dimension of less-than inequalities does not match that of the objective function");

        this.problem = new C2OptimProblemImpl(f);
        this.equal = equal;
        this.less = less;
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code ConstrainedOptimProblemImpl1}
     */
    public ConstrainedOptimProblemImpl1(ConstrainedOptimProblemImpl1 that) {//TODO: deep copy the members?
        this(that.f(), that.getEqualityConstraints(), that.getLessThanConstraints());
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }

    @Override
    public LessThanConstraints getLessThanConstraints() {
        return less;
    }

    @Override
    public EqualityConstraints getEqualityConstraints() {
        return equal;
    }
}
