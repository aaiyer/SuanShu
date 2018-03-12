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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPBoundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.*;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class LPProblemImpl1Test {

    /**
     * Example 3-6-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() {
        LPProblemImpl1 instance = new LPProblemImpl1(
                new DenseVector(1.0, 1.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {2.0, -1.0, 0.0},
                    {1.0, 2.0, -1.0}
                }),
                                                 new DenseVector(2.0, 1.0)),
                new LinearLessThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, 1.0, 1.0},
                    {-1.0, -1.0, 1.0}
                }),
                                              new DenseVector(4.0, 10.0)),
                null,
                null);

        assertArrayEquals(new double[]{1.0, 1.0, -1.0}, instance.c().toArray(), 0);
        assertNull(instance.Aeq());
        assertNull(instance.beq());

        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {2.0, -1.0, 0.0},
                    {1.0, 2.0, -1.0},
                    {-1.0, -1.0, -1.0},
                    {1.0, 1.0, -1.0}
                }),
                                     instance.A(), 0));

        assertArrayEquals(new double[]{2.0, 1.0, -4.0, -10.0}, instance.b().toArray(), 0);

        assertFalse(instance.isFree(1));
        assertFalse(instance.isFree(2));
        assertFalse(instance.isFree(3));
    }

    /**
     * Example 3-6-3.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0020() {
        LPProblemImpl1 instance = new LPProblemImpl1(
                new DenseVector(3.0, 2.0, -1.0),
                null,
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{{1.0, 1.0, 0.0}}), new DenseVector(5.0)),
                new BoxConstraints(3,
                                   new BoxConstraints.Bound(1, -1.0, 6.0),
                                   new BoxConstraints.Bound(2, 3.0, Double.POSITIVE_INFINITY),
                                   new BoxConstraints.Bound(3, 0.0, 5.0)));

        assertArrayEquals(new double[]{3.0, 2.0, -1.0}, instance.c().toArray(), 0);
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{{1.0, 1.0, 0.0}}), instance.Aeq(), 0));
        assertArrayEquals(new double[]{5.0}, instance.beq().toArray(), 0);

        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {-1, 0, 0},
                    {0, 0, -1}
                }),
                                     instance.A(), 0));

        assertArrayEquals(new double[]{-1, 3, -6, -5}, instance.b().toArray(), 0);

        assertTrue(instance.isFree(1));
        assertTrue(instance.isFree(2));
        assertFalse(instance.isFree(3));
    }

    /**
     * Example 3-1-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     *
     * Convert a standard problem in a general problem then solve using the general lp solver.
     */
    @Test
    public void test_0030() throws Exception {
        DenseVector c = new DenseVector(new double[]{3, -6});
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {2, 1},
                    {1, -1},
                    {1, -4},
                    {-4, 1}
                });
        DenseVector b = new DenseVector(new double[]{-1, 0, -1, -13, -23});
        CanonicalLPProblem1 problem1 = new CanonicalLPProblem1(c, A, b);

        LPTwoPhaseSolver instance = new LPTwoPhaseSolver();
        LPSimplexSolution soln = instance.solve(problem1);
        LPSimplexMinimizer minimizer = soln.minimizer();

        assertTrue(minimizer instanceof LPBoundedMinimizer);//bounded
        assertEquals(-15.0, minimizer.minimum(), 0);
        assertArrayEquals(new double[]{3, 4}, minimizer.minimizer().toArray(), 0);
    }
}
