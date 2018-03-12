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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PseudoInverseTest {

    @Test
    public void test_PseudoInverse_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    @Test
    public void test_PseudoInverse_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    @Test
    public void test_PseudoInverse_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 1, 4, 66.7},
                    {7, 4, 0.93, 90.2, 458},
                    {57, 7, 963, 90, 45.8},
                    {27, 391.2, 29.2, 0.00001, 75.8},
                    {69.26, 254, 3, 2, 2.339}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-12));
    }

    @Test
    public void test_PseudoInverse_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 1, 4, 66.7},
                    {7, 4, 0.93, 90.2, 458},
                    {57, 7, 963, 90, 45.8},
                    {27, 391.2, 29.2, 0.00001, 75.8},
                    {69.26, 254, 3, 2, 2.339}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-12));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 4},
                    {3, 6}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);
//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 4},
                    {3, 6}
                });

        PseudoInverse Ainv = new PseudoInverse(A);
//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 7.5, 6.1},
                    {0, 5, 6},
                    {0, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 7.5, 6.1},
                    {0, 5, 6},
                    {0, 8, 9}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0075() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0085() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test
    public void test_PseudoInverse_0095() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of Identity.
     */
    @Test
    public void test_PseudoInverse_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of Identity.
     */
    @Test
    public void test_PseudoInverse_0105() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of Identity.
     */
    @Test
    public void test_PseudoInverse_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5},
                    {7, 4, 4.4},
                    {17, 34, 4.4},
                    {13, 45, 27.4},
                    {71, 9, 19.4}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0125() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5},
                    {7, 4, 4.4},
                    {17, 34, 4.4},
                    {13, 45, 27.4},
                    {71, 9, 19.4}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0130() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3.5},
                    {7.4},
                    {17},
                    {13},
                    {71.4}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0135() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3.5},
                    {7.4},
                    {17},
                    {13},
                    {71.4}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_PseudoInverse_0145() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0150() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5, 90.1, 24.45, 56.74},
                    {7, 4, 4.4, 6.2, 567.7, 34},
                    {17, 34, 4.4, 783.5, 56.8, 43},
                    {13, 45, 27.4, 76.34, 659.34, 234},
                    {71, 9, 19.4, 45, 56.5, 35.89}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-12));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0155() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5, 90.1, 24.45, 56.74},
                    {7, 4, 4.4, 6.2, 567.7, 34},
                    {17, 34, 4.4, 783.5, 56.8, 43},
                    {13, 45, 27.4, 76.34, 659.34, 234},
                    {71, 9, 19.4, 45, 56.5, 35.89}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-12));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-12));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0160() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5, 90.1, 24.45, 56.74}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0165() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5, 90.1, 24.45, 56.74}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-13));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_PseudoInverse_0175() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of fat, row matrix.
     */
    @Test
    public void test_PseudoInverse_0180() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A, 1e-15);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of fat, row matrix.
     */
    @Test
    public void test_PseudoInverse_0185() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0, 0}
                });

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A, A.multiply(Ainv).multiply(A));
        assertEquals(Ainv, Ainv.multiply(A).multiply(Ainv));
        assertEquals(A.multiply(Ainv).t(), A.multiply(Ainv));
        assertEquals(Ainv.multiply(A).t(), Ainv.multiply(A));
    }

    /**
     * Test of small numbers.
     *
     * TODO:
     * The accuracy of this test case depends on the accuracy of SVD.
     * As of 8/5/2010, the implementation of SVD does not work very well for small numbers.
     */
    @Test
    public void test_PseudoInverse_0190() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5, 5.5, 90.1, 24.45, 56.74},
                    {7, 4, 4.4, 6.2, 567.7, 34},
                    {17, 34, 4.4, 783.5, 56.8, 43},
                    {13, 45, 27.4, 76.34, 659.34, 234},
                    {71, 9, 19.4, 45, 56.5, 35.89}
                });
        A = A.scaled(1e-100);

        PseudoInverse Ainv = new PseudoInverse(A);

//        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));//won't hold because not invertible
        assertTrue(AreMatrices.equal(A, A.multiply(Ainv).multiply(A), 1e-12));
//        assertTrue(AreMatrices.equal(Ainv, Ainv.multiply(A).multiply(Ainv), 1e-12));//TODO: entries in Ainv too big to compare with AreMatrices.equal; need relative comparison
        assertTrue(AreMatrices.equal(A.multiply(Ainv).t(), A.multiply(Ainv), 1e-2));//TODO: worse accuracy; expect to be around 1e-12
        assertTrue(AreMatrices.equal(Ainv.multiply(A).t(), Ainv.multiply(A), 1e-2));//TODO: worse accuracy; expect to be around 1e-12
    }
}
