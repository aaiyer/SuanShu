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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution;

import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.LPMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.NaiveRule;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.FerrisMangasarianWrightPhase2;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPSimplexSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPTwoPhaseSolver;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LPUnboundedMinimizerTest {

    /**
     * Example 3-3-1. An unbounded problem.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() throws Exception {
        DenseVector c = new DenseVector(new double[]{-2, -3, 1});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1, 1},
                    {-1, 1, -1},
                    {1, -1, -2}
                });
        DenseVector b = new DenseVector(new double[]{-3, -4, -1});

        CanonicalLPProblem1 problem = new CanonicalLPProblem1(c, A, b);

        LPSimplexSolver instance = new FerrisMangasarianWrightPhase2(new NaiveRule());
        LPMinimizer minimizer = instance.solve(new SimplexTable(problem)).minimizer();

        assertTrue(minimizer instanceof LPUnboundedMinimizer);
        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);

        //the direction of unboundedness
        assertArrayEquals(new double[]{0, 1, 0}, minimizer.minimizer().toArray(), 1e-15);
        assertArrayEquals(new double[]{1, 1, 0}, ((LPUnboundedMinimizer) minimizer).v().toArray(), 1e-15);
    }

    /**
     * Example 3-3-2. An unbounded problem.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0020() throws Exception {
        DenseVector c = new DenseVector(new double[]{1, -2, -4, 4});

        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, -2, -1},
                    {2, -1, -1, 4},
                    {-1, 1, 0, -2}
                });
        DenseVector b = new DenseVector(new double[]{-4, -5, -3});

        CanonicalLPProblem1 problem = new CanonicalLPProblem1(c, A, b);

        LPSimplexSolver instance = new FerrisMangasarianWrightPhase2(new NaiveRule());
        LPMinimizer minimizer = instance.solve(new SimplexTable(problem)).minimizer();

        assertTrue(minimizer instanceof LPUnboundedMinimizer);
        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);

        //the direction of unboundedness
        assertArrayEquals(new double[]{0, 2, 3, 0}, minimizer.minimizer().toArray(), 1e-15);
        assertArrayEquals(new double[]{0, 3, 1, 1}, ((LPUnboundedMinimizer) minimizer).v().toArray(), 1e-15);
    }

    /**
     * Exercise 3-4-2 (2)
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0030() throws Exception, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(-1.0, 1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{2.0, -1.0},
                                                                                {1.0, 2.0}}),
                                                 new DenseVector(1.0, 2.0)),
                null,
                null,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPUnboundedMinimizer minimizer = (LPUnboundedMinimizer) solver.solve(problem).minimizer();

        assertTrue(minimizer instanceof LPUnboundedMinimizer);
        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);
    }

    /**
     * Exercise 3-4-2 (2)
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0040() throws Exception, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(-1.0, 1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{{2.0, -1.0},
                                                                                {1.0, 2.0}}),
                                                 new DenseVector(1.0, 2.0)),
                null,
                null,
                null);

//        SimplexTable table0 = new SimplexTable(problem);// for debugging

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPUnboundedMinimizer minimizer = (LPUnboundedMinimizer) solver.solve(problem).minimizer();

        assertEquals(Double.NEGATIVE_INFINITY, minimizer.minimum(), 0);
    }
}
