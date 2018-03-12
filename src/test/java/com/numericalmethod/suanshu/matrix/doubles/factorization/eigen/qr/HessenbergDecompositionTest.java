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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.HessenbergDecomposition;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.Hessenberg;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HessenbergDecompositionTest {

    @Test
    public void testHessenbergDecomposition_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 5, 7},
                    {3, 0, 6},
                    {4, 3, 1}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    @Test
    public void testHessenbergDecomposition_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 5, 7, 9},
                    {3, 0, 6, 3},
                    {4, 3, 1, 0},
                    {7, 13, 2, 10}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    @Test
    public void testHessenbergDecomposition_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 5, 7, 9, 100},
                    {3, 0, 6, 3, 200},
                    {4, 3, 1, 0, 300},
                    {7, 13, 2, 10, 400},
                    {79.56, 131, 22, 0.0001, 500}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 1e-13));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-12));
    }

    @Test
    public void testHessenbergDecomposition_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1.234, 5.6789, 7.777, 99.99, 100, 0.26},
                    {33.33, 0.123456789, 6666, 3399, 200.2123, 239.01},
                    {456.123, 30.123456, 0.01, 90, 123, 1236843},
                    {71.23, 13.1313, 20.02, 10.23, 40, 0.2654},
                    {1879.56, 131131, 2222, 0.0001, 500.567, 0.000097},
                    {1879.56, 131131, 2222, 0.0001, 500.567, 123.56}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 1e-12));//TODO: the loss in precision is quite big

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-9));//TODO: the loss in precision is quite big
    }

    /**
     * Already in Hessenberg form.
     */
    @Test
    public void testHessenbergDecomposition_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 5, 7, 9},
                    {3, 0, 6, 3},
                    {0, 3, 1, 0},
                    {0, 0, 2, 10}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * ZERO.
     */
    @Test
    public void testHessenbergDecomposition_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * ZERO.
     */
    @Test
    public void testHessenbergDecomposition_0070() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * ZERO.
     */
    @Test
    public void testHessenbergDecomposition_0080() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * Identity.
     */
    @Test
    public void testHessenbergDecomposition_0090() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * Identity.
     */
    @Test
    public void testHessenbergDecomposition_0100() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Already in Hessenberg form.
     * Identity.
     */
    @Test
    public void testHessenbergDecomposition_0110() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));
        assertEquals(A1, H);

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * zero rows
     */
    @Test
    public void testHessenbergDecomposition_0120() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {3, 0, 6, 3},
                    {0, 0, 0, 0},
                    {4, 3, 1, 0}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * zero columns
     */
    @Test
    public void testHessenbergDecomposition_0130() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 1, 7, 9},
                    {0, 3, 6, 3},
                    {0, 4, 1, 0},
                    {0, 7, 2, 10}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 1e-15));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }

    /**
     * Very tiny doubles.
     */
    @Test
    public void testHessenbergDecomposition_0140() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 5e-100, 7, 9e-100},
                    {3, 0.567, 6, 3},
                    {4e-100, 3, 19.987, 0},
                    {7e-100, 13e-100, 2, 10.456}
                });
        HessenbergDecomposition instance = new HessenbergDecomposition(A1);

        Matrix H = instance.H();
        assertTrue(Hessenberg.isHessenberg(H, 1e-115));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        Matrix Hexpected = Q.t().multiply(A1).multiply(Q);
        assertTrue(AreMatrices.equal(Hexpected, H, 1e-14));
    }
}
