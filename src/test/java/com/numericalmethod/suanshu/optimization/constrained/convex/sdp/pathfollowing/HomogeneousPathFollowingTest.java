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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.pathfollowing;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.problem.SDPDualProblem;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HomogeneousPathFollowingTest {

    @Test
    public void test_0010() throws Exception {
        SymmetricMatrix A0 = new SymmetricMatrix(new double[][]{
                    {2},
                    {-0.5, 2},
                    {-0.6, 0.4, 3}
                });
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{
                    {0},
                    {1, 0},
                    {0, 0, 0}
                });
        SymmetricMatrix A2 = new SymmetricMatrix(new double[][]{
                    {0},
                    {0, 0},
                    {1, 0, 0}
                });
        SymmetricMatrix A3 = new SymmetricMatrix(new double[][]{
                    {0},
                    {0, 0},
                    {0, 1, 0}
                });
        SymmetricMatrix A4 = A3.ONE();
        SymmetricMatrix C = A0.scaled(-1.);
        Vector b = new DenseVector(0., 0., 0., 1.);
        SDPDualProblem problem = new SDPDualProblem(
                b,
                C,
                new SymmetricMatrix[]{A1, A2, A3, A4});

        HomogeneousPathFollowing hsdpf = new HomogeneousPathFollowing(0.00000001);
        IterativeMinimizer<CentralPath> soln = hsdpf.solve(problem);
        CentralPath minimizer = soln.search();
//        assertEquals(0.000412, soln.tau, 1e-6);

        assertArrayEquals(new double[]{0.499999, 0.599999, -0.399999, -3.005419}, minimizer.y.toArray(), 1e-6);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.024157, -0.000000, -0.000000},
                    {-0.000000, 0.024157, 0.000000},
                    {-0.000000, 0.000000, 0.951686}
                }),
                minimizer.X,
                1e-6));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.005442, 0.000000, 0.000000},
                    {0.000000, 1.005442, 0.000000},
                    {0.000000, 0.000000, 0.005444}
                }),
                minimizer.S,
                1e-6));
    }
}
