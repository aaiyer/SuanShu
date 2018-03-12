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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPCanonicalSolver;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BoundedLPMinimizerTest {

    /**
     * Example 3-3-3.
     * Multiple (6) solutions.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() throws LPInfeasible {
        DenseVector c = new DenseVector(new double[]{-1, -1, -1});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, -1, 1},
                    {-1, 1, 1},
                    {1, 1, -1},
                    {-1, -1, -1}
                });
        DenseVector b = new DenseVector(new double[]{-2, -3, -1, -4});

        CanonicalLPProblem1 problem = new CanonicalLPProblem1(c, A, b);

        LPCanonicalSolver instance = new LPCanonicalSolver();
        LPBoundedMinimizer minimizer = (LPBoundedMinimizer) instance.solve(problem).minimizer();

        assertEquals(-4.0, minimizer.minimum(), 0);
        assertArrayEquals(new double[]{3.5, 0, 0.5}, minimizer.minimizer().toArray(), 1e-15);

        ImmutableVector[] minimizers = minimizer.minimizers();
        assertArrayEquals(new double[]{3.5, 0, 0.5}, minimizers[0].toArray(), 1e-15);
        assertArrayEquals(new double[]{1.5, 0, 2.5}, minimizers[1].toArray(), 1e-15);
        assertArrayEquals(new double[]{0, 1.5, 2.5}, minimizers[2].toArray(), 1e-15);
        assertArrayEquals(new double[]{0, 3, 1}, minimizers[3].toArray(), 1e-15);
        assertArrayEquals(new double[]{1, 3, 0}, minimizers[4].toArray(), 1e-15);
        assertArrayEquals(new double[]{3.5, 0.5, 0}, minimizers[5].toArray(), 1e-15);
    }
}
