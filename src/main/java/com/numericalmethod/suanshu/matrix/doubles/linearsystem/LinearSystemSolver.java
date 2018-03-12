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
package com.numericalmethod.suanshu.matrix.doubles.linearsystem;

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solve a system of linear equations in the form:
 * <blockquote><i>
 * Ax = b,
 * </i></blockquote>
 * We assume that, after row reduction, <i>A</i> has no more rows than columns.
 * That is, the system must not be over-determined.
 * For example, the following system is <em>not</em> over-determined. One of the
 * rows is linearly dependent.
 * \[
 * \begin{bmatrix}
 * 1 & -1 & 0\\
 * 0 & -2 & 0\\
 * 0 & 0 & -1\\
 * 0 & 0 & -2
 * \end{bmatrix} x =
 * \begin{bmatrix}
 * -0.8\\
 * -1.6\\
 * 0.8\\
 * 1.6
 * \end{bmatrix}
 * \]
 * This linear system of equations is solved in two steps.
 * <ol>
 * <li>First, solve <i>Ax = 0</i>, the homogeneous system, for non trivial
 * solutions.
 * <li>Then, solve <i>Ax = b</i> for a particular solution.
 * </ol>
 * If <i>A</i> has full rank, this implementation solves the system by LU
 * decomposition.
 * Otherwise, a particular solution is found by <i>x = T * b</i>.
 * The final solution is:
 * <blockquote><i>x_particular + {x_null_space_of_A}</i></blockquote>
 * hence, the translation of the null space of <i>A</i> by the vector
 * <i>x_particular</i>.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a
 * href="http://en.wikipedia.org/wiki/System_of_linear_equations">Wikipedia:
 * System of linear equations</a>
 * <li><a
 * href="http://en.wikipedia.org/wiki/Kernel_(matrix)#Nonhomogeneous_equations">Wikipedia:
 * Nonhomogeneous equations</a>
 * <li><a
 * href="http://en.wikipedia.org/wiki/System_of_linear_equations#Homogeneous_systems">Wikipedia:
 * Homogeneous systems</a>
 * </ul>
 */
public class LinearSystemSolver {

    /** This is the runtime exception thrown when it fails to solve a system of linear
     * equations. */
    public static class NoSolution extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /** Construct an {@code LinearSystemSolver.NoSolution} exception.
         *
         * @param msg the error message
         */
        public NoSolution(String msg) {
            super(msg);
        }
    }

    /**
     * This is the solution to a linear system of equations.
     */
    public static interface Solution {

        /**
         * Get a particular solution for the linear system.
         *
         * @param b a vector
         * @return a particular solution
         */
        Vector getParticularSolution(Vector b);

        /**
         * Get the basis of the homogeneous solution for the linear system,
         * <i>Ax = b</i>.
         * That is, the solutions for <i>Ax = 0</i>.
         *
         * @return the homogeneous solution
         */
        List<Vector> getHomogeneousSoln();
    }

    private final double epsilon;

    /**
     * Construct a solver for a linear system of equations.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is
     * considered 0
     */
    public LinearSystemSolver(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Get a particular solution for the linear system, <i>Ax = b</i>.
     *
     * @param A0 a matrix representing a linear system of equations (the
     * homogeneous part)
     * @return a particular solution
     */
    public Solution solve(final Matrix A0) {
        return new Solution() {

            Matrix A = A0;
            SVD svd = null;//reducing the linearly dependent rows

            {
                if (A0.nRows() > A0.nCols()) {// row reduction
                    svd = new SVD(A, true);
                    A = svd.D().multiply(svd.V().t());
                }

                SuanShuUtils.assertArgument(A.nRows() <= A.nCols(),
                                            "Ax = b is an over-determined system. Please consider using the OLS method");
            }
            Kernel nullspace = new Kernel(A, Kernel.Method.QR, epsilon);
            boolean isFullRank = nullspace.isZero();//is A a full rank matrix, i.e., det(A) != 0
            List<Vector> basis = nullspace.basis();//solution for the homogeneous part, Ax = 0
            Matrix T = nullspace.T();
            Matrix U = nullspace.U();

            @Override
            public Vector getParticularSolution(final Vector b) {
                Vector v = b;
                if (svd != null) {
                    v = svd.Ut().multiply(b);// row reduction
                }

                SuanShuUtils.assertArgument(A.nRows() == v.size(), "A's dimension must equal to b's length (after row reduction)");

                Vector x;
                if (isFullRank) {
                    LUSolver impl = new LUSolver();
                    x = impl.solve(new LSProblem(A, v));
                } else if (IsMatrix.zero(v, epsilon)) {//always consistent
                    x = new DenseVector(A.nCols()).ZERO();//only trivial solution
                } else {//check consistency
                    x = T.multiply(v);//a non-trivial solution for a consistent system

                    /*
                     * If the rightmost column of the row-reduced augmented
                     * matrix has a
                     * nonzero entry in a row that is otherwise zero, the system
                     * is
                     * inconsistent.
                     * Otherwise, the rightmost column of the row-reduced
                     * augmented
                     * matrix is a particular solution, i.e., x.
                     *
                     * @see "Marcel Oliver, Gaussian elimination: How to solve
                     * systems
                     * of linear equations."
                     */
                    for (int i = 1; i <= x.size(); ++i) {
                        if (IsMatrix.zero(U.getRow(i), epsilon)) {//TODO: strictly use 0 instead?
                            if (compare(x.get(i), 0, epsilon) != 0) {//TODO: strictly use 0 instead?
                                throw new NoSolution("the system of linear equation is inconsistent");
                            }
                        }
                    }

                    // append x to match the number of variables (# A's columns)
                    if (A.nCols() > x.size()) {
                        x = CreateVector.concat(x, new DenseVector(R.rep(0.0, A.nCols() - x.size())));
                    }
                }

                return x;
            }

            @Override
            public List<Vector> getHomogeneousSoln() {
                return Collections.unmodifiableList(basis);
            }
        };
    }
}
