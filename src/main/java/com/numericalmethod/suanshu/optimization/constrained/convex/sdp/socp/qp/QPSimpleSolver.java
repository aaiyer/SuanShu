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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp;

import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen.Method;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LinearSystemSolver;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.autoEpsilon;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem.QPProblemOnlyEqualityConstraints;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.operation.VectorSpace;

/**
 * These are the utility functions to solve simple quadratic programming problems that admit analytical solutions.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 13.2, Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
 */
public class QPSimpleSolver {

    /**
     * Solve an unconstrained quadratic programming problem of this form.
     * \[
     * \min_x \left \{ \frac{1}{2} \times x'Hx + x'p \right \}
     * \]
     *
     * @param f       the objective function
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return a quadratic programming solution
     * @throws QPInfeasible when the quadratic programming problem is infeasible
     */
    public static QPSolution solve(QuadraticFunction f, final double epsilon) throws QPInfeasible {
        Matrix H = f.Hessian();
        Vector p = f.p();

        // check positive (semi-)definiteness
        Eigen eigen = new Eigen(H, Method.QR, epsilon);
        double[] ev = eigen.getRealEigenvalues();
        final double min = DoubleArrayMath.min(ev);

        if (DoubleUtils.isNegative(min, 0)) {
            throw new IllegalArgumentException("the Hessian matrix is not positive (semi-)definite");
        }

        if (min < epsilon) {
            VectorSpace vs = new VectorSpace(H, epsilon);
            if (!vs.isSpanned(p)) {
                // p is not a linear combination of the columns of H
                throw new QPInfeasible();
            }
        }

        // compute solution
        LinearSystemSolver solver = new LinearSystemSolver(epsilon);// TODO: use either LDL' or Cholesky decomposition as suggested on p. 409
        final Vector phi = solver.solve(H).getParticularSolution(p.scaled(-1));

        return new QPSolution() {

            @Override
            public boolean isUnique() {
                return min >= epsilon;// H is positive definite
            }

            @Override
            public ImmutableVector minimizer() {
                return new ImmutableVector(phi);
            }
        };
    }

    /**
     * Solve an unconstrained quadratic programming problem of this form.
     * \[
     * \min_x \left \{ \frac{1}{2} \times x'Hx + x'p \right \}
     * \]
     *
     * @param f the objective function
     * @return a quadratic programming solution
     * @throws QPInfeasible when the quadratic programming problem is infeasible
     */
    public static QPSolution solve(QuadraticFunction f) throws QPInfeasible {
        return solve(f, autoEpsilon(f.Hessian()));
    }

    /**
     * Solve a quadratic programming problem subject to equality constraints.
     * \[
     * \min_x \left \{ \frac{1}{2} \times x'Hx + x'p \right \}, Ax = b
     * \]
     *
     * @param f       the objective function
     * @param equal   the equality constraints
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return a quadratic programming solution
     * @throws QPInfeasible when the quadratic programming problem is infeasible
     */
    public static QPSolution solve(QuadraticFunction f, LinearEqualityConstraints equal, final double epsilon) throws QPInfeasible {
        QPProblemOnlyEqualityConstraints f1 = new QPProblemOnlyEqualityConstraints(f, equal);
        final QPSolution soln1 = solve(f1, epsilon);
        final Vector x = f1.getSolutionToOriginalProblem(soln1.minimizer());

        return new QPSolution() {

            @Override
            public boolean isUnique() {
                return soln1.isUnique();
            }

            @Override
            public ImmutableVector minimizer() {
                return new ImmutableVector(x);
            }
        };
    }

    /**
     * Solve a quadratic programming problem subject to equality constraints.
     * \[
     * \min_x \left \{ \frac{1}{2} \times x'Hx + x'p \right \}, Ax = b
     * \]
     *
     * @param f     the objective function
     * @param equal the equality constraints
     * @return a quadratic programming solution
     * @throws QPInfeasible when the quadratic programming problem is infeasible
     */
    public static QPSolution solve(QuadraticFunction f, LinearEqualityConstraints equal) throws QPInfeasible {
        return solve(f, equal, autoEpsilon(f.Hessian()));
    }
}
