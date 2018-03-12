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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;

/**
 * Quadratic Programming is the problem of optimizing (minimizing) a quadratic function of several variables subject to linear constraints on these variables.
 * The standard form, following the convention in the reference, is:
 * \[
 * \min_x \left \{ \frac{1}{2} \times x'Hx + x'p \right \} \\
 * Ax \geq b, A_{eq}x = b_{eq}
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Quadratic_programming">Wikipedia: Quadratic programming</a>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Chapter 13, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
 * </ul>
 */
public class QPProblem implements ConstrainedOptimProblem {

    private final ConstrainedOptimProblemImpl1 problem;

    /**
     * Construct a quadratic programming problem.
     *
     * @param f       the quadratic objective function to be minimized
     * @param equal   the linear equality constraints
     * @param greater the linear greater-than-or-equal-to constraints
     * @param less    the linear less-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f,
                     LinearEqualityConstraints equal,
                     LinearGreaterThanConstraints greater,
                     LinearLessThanConstraints less) {
        this.problem = new ConstrainedOptimProblemImpl1(
                f,
                equal,
                greater == null ? less : (LinearLessThanConstraints) LinearConstraints.concat(greater.toLessThanConstraints(), less));
    }

    /**
     * Construct a quadratic programming problem with linear inequality constraints.
     *
     * @param f       the quadratic objective function to be minimized
     * @param greater the linear greater-than-or-equal-to constraints
     * @param less    the linear less-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f,
                     LinearGreaterThanConstraints greater,
                     LinearLessThanConstraints less) {
        this(f, null, greater, less);
    }

    /**
     * Construct a quadratic programming problem with linear equality and greater-than-or-equal-to constraints.
     *
     * @param f       the quadratic objective function to be minimized
     * @param equal   the linear equality constraints
     * @param greater the linear greater-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f, LinearEqualityConstraints equal, LinearGreaterThanConstraints greater) {
        this(f, equal, greater, null);
    }

    /**
     * Construct a quadratic programming problem with linear greater-than-or-equal-to constraints.
     *
     * @param f       the quadratic objective function to be minimized
     * @param greater the linear greater-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f, LinearGreaterThanConstraints greater) {
        this(f, null, greater);
    }

    /**
     * Construct a quadratic programming problem with linear equality and less-than-or-equal-to constraints.
     *
     * @param f     the quadratic objective function to be minimized
     * @param equal the linear equality constraints
     * @param less  the linear less-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f, LinearEqualityConstraints equal, LinearLessThanConstraints less) {
        this(f, equal, null, less);
    }

    /**
     * Construct a quadratic programming problem with linear less-than-or-equal-to constraints.
     *
     * @param f    the quadratic objective function to be minimized
     * @param less the linear less-than-or-equal-to constraints
     */
    public QPProblem(QuadraticFunction f, LinearLessThanConstraints less) {
        this(f, null, null, less);
    }

    /**
     * Copy constructor.
     *
     * @param that an quadratic programming problem
     */
    public QPProblem(QPProblem that) {
        this(that.f(), that.getEqualityConstraints(), that.getLessThanConstraints());
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public QuadraticFunction f() {
        return (QuadraticFunction) problem.f();
    }

    /**
     * Get the set of linear greater-than-or-equal-to constraints.
     *
     * @return the set of linear greater-than-or-equal-to constraints
     */
    public LinearGreaterThanConstraints getGreaterThanConstraints() {
        return getLessThanConstraints().toGreaterThanConstraints();
    }

    @Override
    public LinearLessThanConstraints getLessThanConstraints() {
        return (LinearLessThanConstraints) problem.getLessThanConstraints();
    }

    @Override
    public LinearEqualityConstraints getEqualityConstraints() {
        return (LinearEqualityConstraints) problem.getEqualityConstraints();
    }

    /**
     * Get the coefficients of the inequality constraints: <i>A</i> as in \(Ax \geq b\).
     *
     * @return the inequality constraint coefficients
     */
    public ImmutableMatrix A() {
        return new ImmutableMatrix(getGreaterThanConstraints().A());
    }

    /**
     * Get the values of the inequality constraints: <i>b</i> as in \(Ax \geq b\).
     *
     * @return the inequality constraint values
     */
    public ImmutableVector b() {
        return new ImmutableVector(getGreaterThanConstraints().b());
    }

    /**
     * Get the coefficients of the equality constraints: <i>A<sub>eq</sub></i> as in \(A_{eq}x = b_{eq}\).
     *
     * @return the equality constraint coefficients
     */
    public ImmutableMatrix Aeq() {
        return new ImmutableMatrix(getEqualityConstraints().A());
    }

    /**
     * Get the values of the equality constraints: <i>b<sub>eq</sub></i> as in \(A_{eq}x = b_{eq}\).
     *
     * @return the equality constraint values
     */
    public ImmutableVector beq() {
        return new ImmutableVector(getEqualityConstraints().b());
    }
}
