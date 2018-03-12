/*
 * Copyright (c)
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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.nonstationary;

import com.numericalmethod.suanshu.algorithm.iterative.monitor.CountMonitor;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.CSRSparseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.algorithm.iterative.tolerance.AbsoluteTolerance;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.SparseVector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.IterativeLinearSystemSolver;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class MinimalResidualSolverTest {

    @Test
    public void test_solve_0010() throws ConvergenceFailure {
        Matrix A = new SymmetricMatrix(
                new double[][]{
                    {4},
                    {1, 3}
                });
        Vector b = new DenseVector(
                new double[]{
                    1, 2
                });
        LSProblem problem = new LSProblem(A, b);

        double tolerance = 1e-4;

        MinimalResidualSolver solver = new MinimalResidualSolver(Integer.MAX_VALUE, new AbsoluteTolerance(tolerance));
        IterativeLinearSystemSolver.Solution soln = solver.solve(problem);
        Vector x = soln.search(new SparseVector(A.nCols()));

        Vector expResult = new DenseVector(
                new double[]{
                    0.0909, 0.6364
                });
        assertArrayEquals(expResult.toArray(), x.toArray(), tolerance);
    }

    @Test
    public void test_solve_0020() throws ConvergenceFailure {
        Matrix A = new SymmetricMatrix(
                new double[][]{
                    {4},
                    {1, 3}
                });
        Vector b = new DenseVector(
                new double[]{
                    1, 2
                });
        LSProblem problem = new LSProblem(A, b);

        double tolerance = 1e-4;

        MinimalResidualSolver solver = new MinimalResidualSolver(Integer.MAX_VALUE, new AbsoluteTolerance(tolerance));
        CountMonitor<Vector> monitor = new CountMonitor<Vector>();
        IterativeLinearSystemSolver.Solution soln = solver.solve(problem, monitor);

        Vector expResult = new DenseVector(
                new double[]{
                    0.0909, 0.6364
                });
        Vector x = soln.search(expResult);

        assertArrayEquals(expResult.toArray(), x.toArray(), tolerance);
        assertEquals(1, monitor.getCount());
    }

    @Test
    public void test_solve_0030() throws ConvergenceFailure {
        /* Symmetric matrix:
         * 8x8
         * [,1] [,2] [,3] [,4] [,5] [,6] [,7] [,8]
         * [1,] 7.000000, 0.000000, 1.000000, 0.000000, 0.000000, 2.000000, 7.000000, 0.000000,
         * [2,] 0.000000, -4.000000, 8.000000, 0.000000, 2.000000, 0.000000, 0.000000, 0.000000,
         * [3,] 1.000000, 8.000000, 1.000000, 0.000000, 0.000000, 0.000000, 0.000000, 5.000000,
         * [4,] 0.000000, 0.000000, 0.000000, 7.000000, 0.000000, 0.000000, 9.000000, 0.000000,
         * [5,] 0.000000, 2.000000, 0.000000, 0.000000, 5.000000, 1.000000, 5.000000, 0.000000,
         * [6,] 2.000000, 0.000000, 0.000000, 0.000000, 1.000000, -1.000000, 0.000000, 5.000000,
         * [7,] 7.000000, 0.000000, 0.000000, 9.000000, 5.000000, 0.000000, 11.000000, 0.000000,
         * [8,] 0.000000, 0.000000, 5.000000, 0.000000, 0.000000, 5.000000, 0.000000, 5.000000,
         */
        Matrix A = new CSRSparseMatrix(8, 8,
                                       new int[]{1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8},
                                       new int[]{1, 3, 6, 7, 2, 3, 5, 1, 2, 3, 8, 4, 7, 2, 5, 6, 7, 1, 5, 6, 8, 1, 4, 5, 7, 3, 6, 8},
                                       new double[]{7, 1, 2, 7, -4, 8, 2, 1, 8, 1, 5, 7, 9, 2, 5, 1, 5, 2, 1, -1, 5, 7, 9, 5, 11, 5, 5, 5});
        Vector b = new DenseVector(
                new double[]{
                    1, 1, 1, 1, 1, 1, 1, 1
                });
        LSProblem problem = new LSProblem(A, b);

        double tolerance = 1e-8;

        MinimalResidualSolver solver = new MinimalResidualSolver(Integer.MAX_VALUE, new AbsoluteTolerance(tolerance));
        CountMonitor<Vector> monitor = new CountMonitor<Vector>();
        IterativeLinearSystemSolver.Solution soln = solver.solve(problem, monitor);
        Vector x = soln.search(new SparseVector(A.nCols()));

        Vector expResult = new DenseVector(
                new double[]{
                    -0.0418602013,
                    -0.00341312416,
                    0.117250377,
                    -0.11263958,
                    0.0241722445,
                    -0.10763334,
                    0.198719673,
                    0.190382964
                });
        assertArrayEquals(expResult.toArray(), x.toArray(), tolerance);
    }

    @Test
    public void test_solve_0040() throws ConvergenceFailure {
        /* Non-symmetric matrix:
         * 5x5
         * [,1] [,2] [,3] [,4] [,5]
         * [1,] 1.000000, -1.000000, 0.000000, -3.000000, 0.000000,
         * [2,] -2.000000, 5.000000, 0.000000, 0.000000, 0.000000,
         * [3,] 0.000000, 0.000000, 4.000000, 6.000000, 4.000000,
         * [4,] -4.000000, 0.000000, 2.000000, 7.000000, 0.000000,
         * [5,] 0.000000, 8.000000, 0.000000, 0.000000, -5.000000,
         */
        Matrix A = new CSRSparseMatrix(5, 5,
                                       new int[]{1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5},
                                       new int[]{1, 2, 4, 1, 2, 3, 4, 5, 1, 3, 4, 2, 5},
                                       new double[]{1, -1, -3, -2, 5, 4, 6, 4, -4, 2, 7, 8, -5});
        Vector b = new DenseVector(
                new double[]{
                    1, 1, 1, 1, 1
                });
        Matrix At = A.t();
        Matrix AtA = At.multiply(A);
        Vector Atb = At.multiply(b);
        LSProblem problem = new LSProblem(AtA, Atb);

        double tolerance = 1e-8;

        MinimalResidualSolver solver = new MinimalResidualSolver(Integer.MAX_VALUE, new AbsoluteTolerance(tolerance));
        CountMonitor<Vector> monitor = new CountMonitor<Vector>();
        IterativeLinearSystemSolver.Solution soln = solver.solve(problem, monitor);
        Vector x = soln.search(new SparseVector(problem.size()));

        Vector expResult = new DenseVector(
                new double[]{
                    -0.522321429,
                    -0.00892857143,
                    1.22098214,
                    -0.504464286,
                    -0.214285714
                });
        assertArrayEquals(expResult.toArray(), x.toArray(), tolerance);
    }

    @Test(expected = ConvergenceFailure.class)
    public void test_solve_0100() throws ConvergenceFailure {
        Matrix A = new DenseMatrix(
                new double[][]{
                    {1, 2},
                    {1, 2}
                });
        Vector b = new DenseVector( // this system has no solution
                new double[]{
                    1, 2
                });
        LSProblem problem = new LSProblem(A, b);

        double tolerance = 1e-4;

        MinimalResidualSolver solver = new MinimalResidualSolver(Integer.MAX_VALUE, new AbsoluteTolerance(tolerance));
        IterativeLinearSystemSolver.Solution soln = solver.solve(problem);
        Vector x = soln.search(new SparseVector(problem.size()));
    }
}
