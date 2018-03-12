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
public class PrimalDualPathFollowingTest {

    /**
     * p.465
     */
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

        DenseMatrix X0 = new DenseMatrix(new double[][]{
                    {1. / 3., 0., 0.},
                    {0., 1. / 3., 0.},
                    {0., 0., 1. / 3.}
                });
        Vector y0 = new DenseVector(0.2, 0.2, 0.2, -4.);
        DenseMatrix S0 = new DenseMatrix(new double[][]{
                    {2, 0.3, 0.4},
                    {0.3, 2, -0.6},
                    {0.4, -0.6, 1}
                });
        CentralPath path0 = new CentralPath(X0, y0, S0);

        PrimalDualPathFollowing pdpf = new PrimalDualPathFollowing(0.9, 0.001);
        IterativeMinimizer<CentralPath> soln = pdpf.solve(problem);
        CentralPath minimizer = soln.search(path0);

        //the solution from the textbook is accurate up to epsilon
        //changing epsilon will change the answers
        assertArrayEquals(new double[]{0.392921, 0.599995, -0.399992, -3.000469}, minimizer.y.toArray(), 1e-6);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.000552, -0.000000, 0.000000},
                    {-0.000000, 0.000552, 0.000000},
                    {0.000000, 0.000000, 0.998896}
                }),
                minimizer.X,
                1e-6));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.000469, 0.107079, 0.000005},
                    {0.107079, 1.000469, -0.000008},
                    {0.000005, -0.000008, 0.000469}
                }),
                minimizer.S,
                1e-6));
    }

    /**
     * The result is more accurate in this test because of the smaller epsilon used.
     *
     * @throws Exception
     */
    @Test
    public void test_0020() throws Exception {
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

        PrimalDualPathFollowing sdp = new PrimalDualPathFollowing(0.9, 0.001);
        IterativeMinimizer<CentralPath> soln = sdp.solve(problem);
        CentralPath minimizer = soln.search();

//        System.out.println(soln.y);
//        System.out.println(soln.X);
//        System.out.println(soln.S);
        assertArrayEquals(new double[]{0.500000, 0.600000, -0.400000, -3.000184}, minimizer.y.toArray(), 1e-6);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.000199, 0.000000, 0.000000},
                    {0.000000, 0.000199, -0.000000},
                    {0.000000, -0.000000, 0.999603}
                }),
                minimizer.X,
                1e-6));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.000184, 0.000000, 0.000000},
                    {0.000000, 1.000184, 0.000000},
                    {0.000000, 0.000000, 0.000184}
                }),
                minimizer.S,
                1e-6));
    }
}
