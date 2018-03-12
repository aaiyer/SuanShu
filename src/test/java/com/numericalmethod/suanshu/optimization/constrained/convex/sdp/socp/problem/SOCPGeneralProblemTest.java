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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.problem;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SOCPGeneralProblemTest {

    /**
     * example 14.5
     */
    @Test
    public void test_0010() {
        Vector f = new DenseVector(1., 0., 0., 0., 0.);

        Matrix A1t = new DenseMatrix(new double[][]{
                    {0, -1, 0, 1, 0},
                    {0, 0, 1, 0, -1}
                });
        Matrix A2t = new DenseMatrix(new double[][]{
                    {0, 0.5, 0, 0, 0},
                    {0, 0, 1, 0, 0}
                });
        Matrix A3t = new DenseMatrix(new double[][]{
                    {0, 0, 0, -0.7071, -0.7071},
                    {0, 0, 0, -0.3536, 0.3536}
                });

        Vector b1 = f;
        Vector b2 = f.ZERO();
        Vector b3 = f.ZERO();

        Vector c1 = new DenseVector(2);//zero
        Vector c2 = new DenseVector(-0.5, 0.);
        Vector c3 = new DenseVector(4.2426, -0.7071);

        double[] d = new double[]{0., 1, 1};

        SOCPGeneralProblem problem = new SOCPGeneralProblem(
                f,
                new Matrix[]{A1t.t(), A2t.t(), A3t.t()},
                new Vector[]{c1, c2, c3},
                new Vector[]{b1, b2, b3},
                d);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, -1, 0, 1, 0},
                    {0, 0, 1, 0, -1}
                }),
                problem.A(1).t(),
                0.));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0.5, 0, 0, 0},
                    {0, 0, 1, 0, 0}
                }),
                problem.A(2).t(),
                0.));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, -0.7071, -0.7071},
                    {0, 0, 0, -0.3536, 0.3536}
                }),
                problem.A(3).t(),
                0.));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, -1, 0, 1, 0},
                    {0, 0, 1, 0, -1},
                    {0, 0, 0, 0, 0},
                    {0, 0.5, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, -0.7071, -0.7071},
                    {0, 0, 0, -0.3536, 0.3536}
                }),
                problem.A().t(),
                0.));

        assertArrayEquals(new double[]{0., 0., 0.}, problem.c(1).toArray(), 0.);
        assertArrayEquals(new double[]{1., -0.5, 0.}, problem.c(2).toArray(), 0.);
        assertArrayEquals(new double[]{1., 4.2426, -0.7071}, problem.c(3).toArray(), 0.);
        assertArrayEquals(new double[]{0., 0., 0., 1., -0.5, 0., 1., 4.2426, -0.7071}, problem.c().toArray(), 0.);

        assertEquals(5, problem.m());
        assertEquals(3, problem.q());
    }
}
