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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.activeset;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSolution;
import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem.QPProblem;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class QPPrimalActiveSetSolverTest {

    /**
     * example 13.2 in Andreas Antoniou, Wu-Sheng Lu
     */
    @Test
    public void test_0010() throws QPInfeasible, Exception {
        Matrix H1 = new DenseMatrix(new double[][]{
                    {1, 0, -1, 0},
                    {0, 1, 0, -1},
                    {-1, 0, 1, 0},
                    {0, -1, 0, 1}
                });
        Matrix H2 = H1.add(H1.ONE().scaled(1e-9));
        Vector p = new DenseVector(new double[]{0, 0, 0, 0});
        QuadraticFunction f = new QuadraticFunction(H2, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {-1, -2, 0, 0},
                    {0, 0, 0, 1},
                    {0, 0, 1, 1},
                    {0, 0, -1, -2}
                });
        Vector b = new DenseVector(new double[]{0, 0, -2, 2, 3, -6});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        QPProblem problem = new QPProblem(f, null, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search(new DenseVector(2., 0., 0., 3.));
        assertArrayEquals(new double[]{0.4, 0.8, 1.0, 2.0}, minimizer.minimizer().toArray(), 1e-15);
    }

    /**
     * example 13.2 in Andreas Antoniou, Wu-Sheng Lu
     */
    @Test
    public void test_0015() throws QPInfeasible, Exception {
        Matrix H1 = new DenseMatrix(new double[][]{
                    {1, 0, -1, 0},
                    {0, 1, 0, -1},
                    {-1, 0, 1, 0},
                    {0, -1, 0, 1}
                });
        Matrix H2 = H1.add(H1.ONE().scaled(1e-9));
        Vector p = new DenseVector(new double[]{0, 0, 0, 0});
        QuadraticFunction f = new QuadraticFunction(H2, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {-1, -2, 0, 0},
                    {0, 0, 0, 1},
                    {0, 0, 1, 1},
                    {0, 0, -1, -2}
                });
        Vector b = new DenseVector(new double[]{0, 0, -2, 2, 3, -6});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        QPProblem problem = new QPProblem(f, null, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search();
        assertArrayEquals(new double[]{0.4, 0.8, 1.0, 2.0}, minimizer.minimizer().toArray(), 1e-15);
    }

    /**
     * example 13.3 in Andreas Antoniou, Wu-Sheng Lu
     * w/ equality constraints
     */
    @Test
    public void test_0020() throws QPInfeasible, Exception {
        Matrix H1 = new DenseMatrix(new double[][]{// positive semi-definite
                    {4, 0, 0},
                    {0, 1, -1},
                    {0, -1, 1}
                });
        Matrix H2 = H1.add(H1.ONE().scaled(1e-9));
        Vector p = new DenseVector(new double[]{-8, -6, -6});
        QuadraticFunction f = new QuadraticFunction(H2, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });
        Vector b = new DenseVector(new double[]{0, 0, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);// x >= 0

        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 + x3 = 3
                new DenseMatrix(new double[][]{{1, 1, 1}}),
                new DenseVector(3.0));

        QPProblem problem = new QPProblem(f, equal, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search(new DenseVector(1., 1., 1.));
        assertArrayEquals(new double[]{0.5, 1.25, 1.25}, minimizer.minimizer().toArray(), 1e-9);//TODO: improve accuracy
    }

    /**
     * example 13.3 in Andreas Antoniou, Wu-Sheng Lu
     * w/ equality constraints
     */
    @Test
    public void test_0025() throws QPInfeasible, Exception {
        Matrix H1 = new DenseMatrix(new double[][]{// positive semi-definite
                    {4, 0, 0},
                    {0, 1, -1},
                    {0, -1, 1}
                });
        Matrix H2 = H1.add(H1.ONE().scaled(1e-9));
        Vector p = new DenseVector(new double[]{-8, -6, -6});
        QuadraticFunction f = new QuadraticFunction(H2, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });
        Vector b = new DenseVector(new double[]{0, 0, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);// x >= 0

        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 + x3 = 3
                new DenseMatrix(new double[][]{{1, 1, 1}}),
                new DenseVector(3.0));

        QPProblem problem = new QPProblem(f, equal, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search();
        assertArrayEquals(new double[]{0.5, 1.25, 1.25}, minimizer.minimizer().toArray(), 1e-9);//TODO: improve accuracy
    }

    /**
     * example 16.4 in Jorge Nocedal, Stephen Wright
     *
     * There is a detailed trace (for debugging) on p. 475.
     */
    @Test
    public void test_0030() throws QPInfeasible, Exception {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 0},
                    {0, 2}
                });
        Vector p = new DenseVector(new double[]{-2, -5});
        QuadraticFunction f = new QuadraticFunction(H, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, -2},
                    {-1, -2},
                    {-1, 2},
                    {1, 0},
                    {0, 1}
                });
        Vector b = new DenseVector(new double[]{-2, -6, -2, 0, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);// x >= 0

        QPProblem problem = new QPProblem(f, null, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search(new DenseVector(2., 0.));
        assertArrayEquals(new double[]{1.4, 1.7}, minimizer.minimizer().toArray(), 1e-15);
    }

    /**
     * example 16.4 in Jorge Nocedal, Stephen Wright
     *
     * There is a detailed trace (for debugging) on p. 475.
     */
    @Test
    public void test_0035() throws QPInfeasible, Exception {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 0},
                    {0, 2}
                });
        Vector p = new DenseVector(new double[]{-2, -5});
        QuadraticFunction f = new QuadraticFunction(H, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, -2},
                    {-1, -2},
                    {-1, 2},
                    {1, 0},
                    {0, 1}
                });
        Vector b = new DenseVector(new double[]{-2, -6, -2, 0, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);// x >= 0

        QPProblem problem = new QPProblem(f, null, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search();
        assertArrayEquals(new double[]{1.4, 1.7}, minimizer.minimizer().toArray(), 1e-15);
    }

    @Test
    public void test_0100() throws QPInfeasible, Exception {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                });
        Vector p = new DenseVector(new double[]{10, 1});
        QuadraticFunction f = new QuadraticFunction(H, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                });
        Vector b = new DenseVector(new double[]{-4, 0, -2, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{{10, 8}}),
                new DenseVector(-32.0));

        QPProblem problem = new QPProblem(f, equal, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search(new DenseVector(-3.2, 0.));
//        QPSolution soln = instance.search(new DenseVector(-1.6, -2.));
        assertArrayEquals(new double[]{-3.2, 0.}, minimizer.minimizer().toArray(), 1e-15);
    }

    /**
     * the solution can be analytical computed as following:
     *
     * u = matrix(
     * c(
     * 0.000000, 1.000000, 0.000000,
     * 1.000000, 1.000000, 1.000000,
     * 0.100162, 0.164244, 0.182082
     * ), 3, 3)
     * u = t(u)
     *
     * ans = solve(u) %*% c(0.1, 1., 0.15)
     *
     * @throws QPInfeasible
     */
    @Test
    public void test_0110() throws QPInfeasible, Exception {
        Matrix H = new DenseMatrix(new double[][]{
                    {0.100162, 0.045864, 0.005712},
                    {0.045864, 0.210773, 0.028283},
                    {0.005712, 0.028283, 0.0668884}
                });
        Vector p = new DenseVector(new double[]{0, 0, 0});
        QuadraticFunction f = new QuadraticFunction(H, p);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });
        Vector b = new DenseVector(new double[]{0, 0.1, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1., 1., 1.},
                    {0.100162, 0.164244, 0.182082}
                }),
                new DenseVector(1., 0.15));

        QPProblem problem = new QPProblem(f, equal, greater);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        QPPrimalActiveSetSolver.Solution soln = instance.solve(problem);
        QPSolution minimizer = soln.search(new DenseVector(0.2222777066883057, 0.7777222933116942, 0.));
        assertArrayEquals(new double[]{0.3698511, 0.1, 0.5301489}, minimizer.minimizer().toArray(), 1e-7);// from analytical solution
    }
}
