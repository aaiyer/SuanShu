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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPUnboundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPBoundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.ConstraintsUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.LPMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class LPTwoPhaseSolverTest {

    /**
     * Example 3-6-13 (c), pp. 84.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     *
     * This case is founded unbound during scheme 2.
     */
    @Test
    public void test_0010() throws Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{1.0, 0.0}}),
                                                 new DenseVector(-6.0)),
                null,
                new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {-1.0, 0.0}
                }),
                new DenseVector(-4.0)),
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPUnboundedMinimizer minimizer = (LPUnboundedMinimizer) solver.solve(problem).minimizer();

        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);
        assertArrayEquals(new double[]{0, 1}, minimizer.minimizer().toArray(), 0);
        assertArrayEquals(new double[]{0, 1}, minimizer.v().toArray(), 0);
    }

    /**
     * Example 3-6-13 (b), pp. 84.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     *
     * This case is found infeasible during phase 1.
     */
    @Test(expected = LPInfeasible.class)
    public void test_0020() throws Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{1.0, 0.0}}),
                                                 new DenseVector(6.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{{-1.0, 0.0}}),
                                              new DenseVector(-4.0)),
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPMinimizer minimizer = solver.solve(problem).minimizer();
    }

    /**
     * Exercise 3-3-2
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0030() throws Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(1.0, -2.0, -4.0, 4.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{0.0, 1.0, -2.0, -1.0},
                                                                                {2.0, -1.0, -1.0, 4.0},
                                                                                {-1.0, 1.0, 0.0, -2.0}}),
                                                 new DenseVector(-4.0, -5.0, -3.0)),
                null,
                null,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPUnboundedMinimizer minimizer = (LPUnboundedMinimizer) solver.solve(problem).minimizer();

        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);
    }

    @Test
    public void test_0050() throws Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(0, 0, 0, 0, 1),
                null,
                new LinearLessThanConstraints(new DenseMatrix(new double[][]{{-0.05, 0.2, -0.15, -0.3, -1},
                                                                             {-1, 0, 0, 0, -1},
                                                                             {0, -1, 0, 0, -1},
                                                                             {0, 0, -1, 0, -1},
                                                                             {0, 0, 0, -1, -1},
                                                                             {1, 1, 1, 1, -1}
                }),
                                              new DenseVector(-1000, 0, 0, 0, 0, 10000)),
                null,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPBoundedMinimizer minimizer = (LPBoundedMinimizer) solver.solve(problem).minimizer();

        assertEquals(0, minimizer.minimum(), 0);
    }

    /**
     * This problem has no solution.
     *
     * @throws LPInfeasible
     * throws LPUnbounded
     */
    @Test
    public void test_0060() throws Exception {

        LinearLessThanConstraints less = new LinearLessThanConstraints(new DenseMatrix(new double[][]{{-0.100162, -0.164244, 1}}),
                                                                       new DenseVector(-0.15));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(new DenseMatrix(new double[][]{{1., 1., 0.}}),
                                                                        new DenseVector(1.));

        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(0, 0, 1),
                null,
                less,
                equal,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPBoundedMinimizer minimizer = (LPBoundedMinimizer) solver.solve(problem).minimizer();

        for (Vector v : minimizer.minimizers()) {
            assertTrue(ConstraintsUtils.isSatisfied(less, v));
            assertTrue(ConstraintsUtils.isSatisfied(equal, v));
            assertEquals(0, minimizer.minimum(), 0);
        }
    }

    @Test
    public void test_0070() throws Exception {

        LinearLessThanConstraints less = new LinearLessThanConstraints(new DenseMatrix(new double[][]{{-0.100162, -0.164244, -0.182082, 1.}}),
                                                                       new DenseVector(-0.15));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(new DenseMatrix(new double[][]{{1., 1., 1., 0.}}),
                                                                        new DenseVector(1.0));

        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(0., 0., 0., 1.),
                null,
                less,
                equal,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPBoundedMinimizer minimizer = (LPBoundedMinimizer) solver.solve(problem).minimizer();

        for (Vector v : minimizer.minimizers()) {
            assertTrue(ConstraintsUtils.isSatisfied(less, v));
            assertTrue(ConstraintsUtils.isSatisfied(equal, v));
            assertEquals(0, minimizer.minimum(), 0);
        }
    }

    /**
     * all free variables; no >=0 (non-negativity) constraints
     * feasible initials: (-1.6, -2), (-3.2, 0)
     */
    @Test
    public void test_0100() throws Exception {
        Vector cost = new DenseVector(1., 1.);// x1 + x2

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                });
        Vector b = new DenseVector(new double[]{-4, 0, -2, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        Matrix Aeq = new DenseMatrix(new double[][]{{10, 8}});
        Vector beq = new DenseVector(-32.0);
        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 = -32
                Aeq, beq);

        BoxConstraints.Bound[] bounds = new BoxConstraints.Bound[2];
        for (int i = 0; i < 2; ++i) {
            bounds[i] = new BoxConstraints.Bound(i + 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        BoxConstraints box = new BoxConstraints(2, bounds);

        LPProblemImpl1 problem = new LPProblemImpl1(cost, greater, null, equal, box);
        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPSimplexMinimizer minimizer = solver.solve(problem).minimizer();
        Vector v = minimizer.minimizer();

        assertTrue(ConstraintsUtils.isSatisfied(greater, v));
        assertTrue(ConstraintsUtils.isSatisfied(equal, v));
        assertEquals(-3.6, minimizer.minimum(), 0);
    }

    /**
     * all free variables; no >=0 (non-negativity) constraints
     * feasible initials: (-0.8, -0.92)
     */
    @Test
    public void test_0110() throws Exception {
        Vector cost = new DenseVector(1., 1.);// x1 + x2

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                });
        Vector b = new DenseVector(new double[]{-0.8, -3.2, -2., 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b);

        Matrix Aeq = new DenseMatrix(new double[][]{{3.6, 8.}});
        Vector beq = new DenseVector(-10.24);
        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 = -10.24
                Aeq, beq);

        BoxConstraints.Bound[] bounds = new BoxConstraints.Bound[2];
        for (int i = 0; i < 2; ++i) {
            bounds[i] = new BoxConstraints.Bound(i + 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        BoxConstraints box = new BoxConstraints(2, bounds);

        LPProblemImpl1 problem = new LPProblemImpl1(cost, greater, null, equal, box);
        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPSimplexMinimizer minimizer = solver.solve(problem).minimizer();
        Vector v = minimizer.minimizer();// (-0.8, -0.92)

        assertTrue(ConstraintsUtils.isSatisfied(greater, v));
        assertTrue(ConstraintsUtils.isSatisfied(equal, v));
        assertEquals(-1.72, minimizer.minimum(), 1e-15);
    }

    /**
     * Example 11.1.
     *
     * Applied Integer Programming: Modeling and Solution
     * by Der-San Chen, Robert G. Batson, Yu Dang.
     */
    @Test
    public void test_0120() throws Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(-5.0, 2.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{1.0, 3.0}}),
                                                 new DenseVector(9.0)),
                new LinearLessThanConstraints(new DenseMatrix(new double[][]{{-1.0, 2.0},
                                                                             {3.0, 2.0}}),
                                              new DenseVector(5.0, 19.0)),
                null,
                null);//y1, y2 >= 0

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPBoundedMinimizer minimizer = (LPBoundedMinimizer) solver.solve(problem).minimizer();

        assertEquals(-25.57, minimizer.minimum(), 0.002);
        assertArrayEquals(new double[]{39. / 7., 8. / 7.}, minimizer.minimizer().toArray(), 1e-8);
    }
}
