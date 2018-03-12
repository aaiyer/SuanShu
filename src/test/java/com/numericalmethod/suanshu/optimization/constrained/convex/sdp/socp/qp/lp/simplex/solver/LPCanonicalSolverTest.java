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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPBoundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.LPMinimizer;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LPCanonicalSolverTest {

    /**
     * Example 3-1-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() throws LPInfeasible {
        DenseVector c = new DenseVector(new double[]{3, -6});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {2, 1},
                    {1, -1},
                    {1, -4},
                    {-4, 1}
                });
        DenseVector b = new DenseVector(new double[]{-1, 0, -1, -13, -23});

        CanonicalLPProblem1 problem = new CanonicalLPProblem1(c, A, b);

        LPCanonicalSolver instance = new LPCanonicalSolver();
        LPMinimizer minimizer = instance.solve(problem).minimizer();

        assertTrue(minimizer instanceof LPBoundedMinimizer);//bounded
        assertEquals(-15.0, minimizer.minimum(), 0);
        assertArrayEquals(new double[]{3, 4}, minimizer.minimizer().toArray(), 1e-15);
    }
}
