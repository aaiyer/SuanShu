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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints.Bound;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.*;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * This is an implementation of a linear programming problem, {@link LPProblem}.
 * For convenient construction, this implementation allows all forms of linear constraints:
 * <ul>
 * <li>greater-than-or-equal-to constraints: Agr * x >= bgr
 * <li>less-than-or-equal-to constraints: Ale * x ≤ ble
 * <li>equality constraints: Aeq * x = beq
 * <li>box constraints (bounds): l ≤ x ≤ u
 * </ul>
 * <em>By convention, if no bound is mentioned for a variable, x &ge; 0.</em>
 *
 * @author Haksun Li
 */
public class LPProblemImpl1 implements LPProblem {

    private final ConstrainedOptimProblemImpl1 problem;
    private final ImmutableVector c;//the objective function
    private final BoxConstraints bounds;//used only to determine 'free' variables

    /**
     * Construct a general linear programming problem.
     *
     * @param cost    the objective function
     * @param greater the greater-than-or-equal-to inequality constraints
     * @param less    the less-than-or-equal-to inequality constraints
     * @param equal   the equality constraints
     * @param bounds  the bounds for variables
     */
    public LPProblemImpl1(
            Vector cost,
            LinearGreaterThanConstraints greater,
            LinearLessThanConstraints less,
            LinearEqualityConstraints equal,
            BoxConstraints bounds) {

        this.problem = getConstrainedProblem(cost, greater, less, equal, bounds);
        this.c = new ImmutableVector(cost);
        this.bounds = bounds;
    }

    private static ConstrainedOptimProblemImpl1 getConstrainedProblem(
            final Vector cost,
            final LinearGreaterThanConstraints greater,
            final LinearLessThanConstraints less,
            final LinearEqualityConstraints equal,
            final BoxConstraints bounds) {

        //a standard LP problem
        if (greater == null && less == null && equal != null && bounds == null) {
            return getStandardProblem(cost, equal);
        }

        //convert the less-than-or-equal-to constraints to greater-than-or-equal-to constraints
        LinearGreaterThanConstraints greater2 = less != null ? less.toGreaterThanConstraints() : null;

        //split the box/bound constraints into greater-than-or-equal-to and less-than-or-equal-to constraints
        LinearGreaterThanConstraints greater3 = bounds != null ? bounds.getLowerBounds() : null;

        //convert the less-than-or-equal-to constraints from box/bound to greater-than-or-equal-to constraints
        LinearLessThanConstraints less1 = bounds != null ? bounds.getUpperBounds() : null;
        LinearGreaterThanConstraints greater4 = less1 != null ? less1.toGreaterThanConstraints() : null;

        LinearGreaterThanConstraints greaterAll = (LinearGreaterThanConstraints) LinearConstraints.concat(greater, greater2, greater3, greater4);
        LinearLessThanConstraints lessAll = greaterAll.toLessThanConstraints();

        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(
                new RealScalarFunction() {

                    @Override
                    public Double evaluate(Vector x) {
                        double cx = cost.innerProduct(x);
                        return cx;
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return cost.size();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return 1;
                    }
                },
                equal,
                lessAll);

        return problem;
    }

    private static ConstrainedOptimProblemImpl1 getStandardProblem(
            final Vector cost, final LinearEqualityConstraints equal) {
        Matrix A = equal.A();
        Vector b = equal.b();

        Matrix AA = CreateMatrix.rbind(A, A.scaled(-1));
        Vector bb = CreateVector.concat(b, b.scaled(-1));
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(AA, bb);

        return getConstrainedProblem(cost, greater, null, null, null);
    }

    /**
     * Construct a general linear programming problem with only greater-than-or-equal-to and equality constraints.
     *
     * @param cost    the objective function
     * @param greater the greater-than-or-equal-to inequality constraints
     * @param equal   the equality constraints
     */
    public LPProblemImpl1(
            Vector cost,
            LinearGreaterThanConstraints greater,
            LinearEqualityConstraints equal) {
        this(cost, greater, null, equal, null);
    }

    /**
     * Get the number of greater-than-or-equal-to constraints.
     *
     * @return the number of greater-than-or-equal-to inequalities
     */
    public int nGreaterThanInequalities() {
        return A().nRows();
    }

    /**
     * Get the number of equality constraints.
     *
     * @return the number of equality constraints
     */
    public int nEqualities() {
        return Aeq().nRows();
    }

    @Override
    public int dimension() {
        return c.size();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }

    @Override
    public LinearLessThanConstraints getLessThanConstraints() {
        return (LinearLessThanConstraints) problem.getLessThanConstraints();
    }

    @Override
    public LinearEqualityConstraints getEqualityConstraints() {
        return (LinearEqualityConstraints) problem.getEqualityConstraints();
    }

    @Override
    public ImmutableVector c() {
        return c;
    }

    @Override
    public ImmutableMatrix A() {
        return new ImmutableMatrix(getLessThanConstraints().A().scaled(-1));
    }

    @Override
    public ImmutableVector b() {
        return new ImmutableVector(getLessThanConstraints().b().scaled(-1));
    }

    @Override
    public ImmutableMatrix Aeq() {
        return getEqualityConstraints() != null ? getEqualityConstraints().A() : null;
    }

    @Override
    public ImmutableVector beq() {
        return getEqualityConstraints() != null ? getEqualityConstraints().b() : null;
    }

    @Override
    public boolean isFree(int i) {
        if (bounds == null) {
            return false;
        }

        for (Bound bound : bounds.getBounds()) {
            if (bound.index == i) {//there exits a bounded condition
                if (bound.lower() == 0) {//the usual non-negativity condition, hence not a 'free' variable
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("min. objective:\n");
        str.append(c.toString());
        str.append("\n");

        str.append("less-than-or-equal-to inequalities:\n");
        if (getLessThanConstraints() != null) {
            str.append(getLessThanConstraints().toString());
            str.append("\n");
        }

        str.append("equalities:\n");
        if (getEqualityConstraints() != null) {
            str.append(getEqualityConstraints().toString());
            str.append("\n");
        }

        str.append("free variables:\n");
        for (int i = 1; i <= dimension(); ++i) {
            if (isFree(i)) {
                str.append(i);
                str.append(", ");
            }
        }

        return str.toString();
    }
}
