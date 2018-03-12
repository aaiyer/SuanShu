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
package com.numericalmethod.suanshu.matrix.doubles.factorization.gaussianelimination;

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
public class GaussJordanEliminationTest {

    @Test
    public void test_GaussJordanElimination_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {2, 2, -1},
                    {4, -1, 6}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0105() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {2, 2, -1},
                    {4, -1, 6}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {1, 2, 3},
                    {4, 5, 6}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0115() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {1, 2, 3},
                    {4, 5, 6}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

//        System.out.println(U);
//        System.out.println(T.multiply(A));

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0125() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

//        System.out.println(U);
//        System.out.println(T.multiply(A));

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0130() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 2.3, 69.3},
                    {4, 5, 6, 31, -65},
                    {7, 8, 9, 0.36, 63.9},
                    {17, 8.8, -0.999, 10.36, 263.9},
                    {77, 83.96, 9.6, 37.6, 633.9}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
    }

    @Test
    public void test_GaussJordanElimination_0135() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 2.3, 69.3},
                    {4, 5, 6, 31, -65},
                    {7, 8, 9, 0.36, 63.9},
                    {17, 8.8, -0.999, 10.36, 263.9},
                    {77, 83.96, 9.6, 37.6, 633.9}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 99, 32, 723}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0145() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 99, 32, 723}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0150() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 23, 6.8, 322.9, 123},
                    {4, 5, 6, 11, 23, 35, 12, 3, 8},
                    {7, 8, 9, 32, 86, 1, 23, 5, 9},
                    {71, 18, 99, 32, 723, 9, 5, 234, 1.23}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0155() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 23, 6.8, 322.9, 123},
                    {4, 5, 6, 11, 23, 35, 12, 3, 8},
                    {7, 8, 9, 32, 86, 1, 23, 5, 9},
                    {71, 18, 99, 32, 723, 9, 5, 234, 1.23}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0160() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 6.31, 3.2, 86},
                    {56, 36, 12.69, 68, 2823},
                    {1, 4.2, 72, 26, 28},
                    {99, 0.001, 65, 39, 123},
                    {88, 100, 123, 9.36, 72.3},
                    {73, 202, 207, 125.3, 7.23}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0165() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 6.31, 3.2, 86},
                    {56, 36, 12.69, 68, 2823},
                    {1, 4.2, 72, 26, 28},
                    {99, 0.001, 65, 39, 123},
                    {88, 100, 123, 9.36, 72.3},
                    {73, 202, 207, 125.3, 7.23}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3, 0},
                    {3, -9, 3, 0},
                    {6, -6, 0, 0}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        Matrix expectedU = new DenseMatrix(new double[][]{
                    {1, 0, -0.5, 0},
                    {0, 1, -0.5, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(expectedU, U);
        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0175() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3, 0},
                    {3, -9, 3, 0},
                    {6, -6, 0, 0}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        Matrix expectedU = new DenseMatrix(new double[][]{
                    {1, 0, -0.5, 0},
                    {0, 1, -0.5, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(expectedU, U);
        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0180() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, -3, 3, 0},
                    {3, -3, 3, 0},
                    {6, -6, 6, 0}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        Matrix expectedU = new DenseMatrix(new double[][]{
                    {1, -1, 1, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(expectedU, U);
        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    /**
     * Test of rectangle matrix.
     */
    @Test
    public void test_GaussJordanElimination_0185() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, -3, 3, 0},
                    {3, -3, 3, 0},
                    {6, -6, 6, 0}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        Matrix expectedU = new DenseMatrix(new double[][]{
                    {1, -1, 1, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(expectedU, U);
        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0190() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-10, 2e-10, 3e-10},
                    {4e-10, 5e-10, 6e-10},
                    {7e-10, 8e-10, 9e-10}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0195() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-10, 2e-10, 3e-10},
                    {4e-10, 5e-10, 6e-10},
                    {7e-10, 8e-10, 9e-10}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0200() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 9e-100}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A, true, 1e-115);

        Matrix U = instance.U();
        Matrix T = instance.T();

//        System.out.println(U);
//        System.out.println(T.multiply(A));

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 1e-115));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }

    @Test
    public void test_GaussJordanElimination_0210() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 9e-100}
                });
        GaussJordanElimination instance = new GaussJordanElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();

//        System.out.println(U);
//        System.out.println(T.multiply(A));

        assertTrue(IsMatrix.reducedRowEchelonForm(U, 1e-115));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
    }
}
