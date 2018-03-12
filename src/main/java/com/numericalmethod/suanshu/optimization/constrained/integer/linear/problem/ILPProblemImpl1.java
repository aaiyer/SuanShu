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
package com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This implementation is an ILP problem, in which the variables can be real or integral.
 *
 * @author Haksun Li
 */
public class ILPProblemImpl1 implements ILPProblem {

    private final LPProblemImpl1 lpProblem;
    private final IPProblemImpl1 ipProblem;

    /**
     * Construct an ILP problem, in which the variables can be real or integral.
     *
     * @param cost     the linear objective function
     * @param greater  the greater-than-or-equal-to constraints
     * @param less     the less-than-or-equal-to constraints
     * @param equal    the equality constraints
     * @param bounds   the box constraints
     * @param integers the indices of the integral variables
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public ILPProblemImpl1(
            Vector cost,
            LinearGreaterThanConstraints greater,
            LinearLessThanConstraints less,
            LinearEqualityConstraints equal,
            BoxConstraints bounds,
            int[] integers,
            double epsilon) {
        lpProblem = new LPProblemImpl1(cost, greater, less, equal, bounds);//TODO: make 'equal' position consistent
        ipProblem = new IPProblemImpl1(lpProblem.f(), equal, less, integers, epsilon);
    }

    @Override
    public double epsilon() {
        return ipProblem.epsilon();
    }

    @Override
    public int[] getIntegerIndices() {
        return ipProblem.getIntegerIndices();
    }

    @Override
    public int[] getNonIntegralIndices(double[] x) {
        return ipProblem.getNonIntegralIndices(x);
    }

    @Override
    public LinearLessThanConstraints getLessThanConstraints() {
        return lpProblem.getLessThanConstraints();
    }

    @Override
    public LinearEqualityConstraints getEqualityConstraints() {
        return lpProblem.getEqualityConstraints();
    }

    @Override
    public int dimension() {
        return lpProblem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return lpProblem.f();
    }

    @Override
    public ImmutableVector c() {
        return lpProblem.c();
    }

    @Override
    public ImmutableMatrix A() {
        return lpProblem.A();
    }

    @Override
    public ImmutableVector b() {
        return lpProblem.b();
    }

    @Override
    public ImmutableMatrix Aeq() {
        return lpProblem.Aeq();
    }

    @Override
    public ImmutableVector beq() {
        return lpProblem.beq();
    }

    @Override
    public boolean isFree(int i) {
        return lpProblem.isFree(i);
    }
}
