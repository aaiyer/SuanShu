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
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.Cholesky;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class InverseTest {

    //<editor-fold defaultstate="collapsed" desc="tests for inverting a lower triangular matrix">
    @Test
    public void test_InverseL_0010() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4}
                });

        Matrix Linv = new Inverse(L, 0);
        assertTrue(AreMatrices.equal(L.ONE(), L.multiply(Linv), 1e-15));
        assertTrue(AreMatrices.equal(L.ONE(), Linv.multiply(L), 1e-15));
    }

    @Test
    public void test_InverseL_0020() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4},
                    {7.6, 4.6, 9.1},
                    {99.966, 0.0064, 901, 12.92},
                    {99.966, 0.0064, 901, 12.92, 1.27},
                    {99.966, 0.0064, 901, 12.92, 1.27, 269.1}
                });

        Matrix Linv = new Inverse(L, 1e-15);
        assertTrue(AreMatrices.equal(L.ONE(), L.multiply(Linv), 1e-14));
        assertTrue(AreMatrices.equal(L.ONE(), Linv.multiply(L), 1e-13));
    }

    @Test
    public void test_InverseL_0025() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4},
                    {7.6, 4.6, 9.1},
                    {99.966, 0.0064, 901, 12.92},
                    {99.966, 0.0064, 901, 12.92, 1.27},
                    {99.966, 0.0064, 901, 12.92, 1.27, 269.1}
                });

        Matrix Linv = new Inverse(L);//calling the full LU decomposition before calling Inverse on triangular matrixS
        assertTrue(AreMatrices.equal(L.ONE(), L.multiply(Linv), 1e-14));
        assertTrue(AreMatrices.equal(L.ONE(), Linv.multiply(L), 1e-13));
    }

    @Test
    public void test_InverseL_0030() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4},
                    {7.6, 4.6, 9.1},
                    {0, 0.0064, 901, 12.92},
                    {0, 0, 901, 12.92, 1.27},
                    {0, 0, 0, 12.92, 1.27, 269.1}
                });

        Matrix Linv = new Inverse(L);
        assertTrue(AreMatrices.equal(L.ONE(), L.multiply(Linv), 1e-12));
        assertTrue(AreMatrices.equal(L.ONE(), Linv.multiply(L), 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void test_InverseL_0040() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4},
                    {7.6, 4.6, 9.1},
                    {0, 0.0064, 901, 12.92},
                    {0, 0, 901, 12.92, 1.27},
                    {0, 0, 0, 12.92, 1.27, 0}
                });

        Matrix Linv = new Inverse(L);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for inverting an upper triangular matrix">
    @Test
    public void test_InverseU_0010() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {7, 4},
                    {3}
                });

        Matrix Ainv = new Inverse(U);
        assertTrue(AreMatrices.equal(U.ONE(), U.multiply(Ainv), 0));
        assertTrue(AreMatrices.equal(U.ONE(), Ainv.multiply(U), 0));
    }

    @Test
    public void test_InverseU_0020() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {99.966, 0.0064, 901, 12.92, 1.27, 269.1},
                    {99.966, 0.0064, 901, 12.92, 1.27},
                    {99.966, 0.0064, 901, 12.92},
                    {7.6, 4.6, 9.1},
                    {7, 4},
                    {3}
                });

        Matrix Ainv = new Inverse(U);
        assertTrue(AreMatrices.equal(U.ONE(), U.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(U.ONE(), Ainv.multiply(U), 1e-13));
    }

    @Test
    public void test_InverseU_0030() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1, 0, 0, 12.92, 1.27, 269.1},
                    {2.2, 0, 901, 12.92, 1.27},
                    {3.3, 0.0064, 901, 12.92},
                    {7.6, 4.6, 9.1},
                    {7, 4},
                    {3}
                });

        Matrix Ainv = new Inverse(U);
        assertTrue(AreMatrices.equal(U.ONE(), U.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(U.ONE(), Ainv.multiply(U), 1e-13));
    }

    @Test(expected = RuntimeException.class)
    public void test_InverseU_0040() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {0, 0, 0, 12.92, 1.27, 269.1},
                    {2.2, 0, 901, 12.92, 1.27},
                    {3.3, 0.0064, 901, 12.92},
                    {7.6, 4.6, 9.1},
                    {7, 4},
                    {3}
                });

        Matrix Ainv = new Inverse(U);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for inverting a matrix">
    @Test
    public void test_Inverse_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 4},
                    {3, 201.1}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-15));
    }

    @Test
    public void test_Inverse_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 4},
                    {3, 201.1}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-15));
    }

    @Test
    public void test_Inverse_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {99.96, 0.0064, 901, 12.92, 1.27, 269.1},
                    {99.66, 0.0064, 901, 12.92, 1.27, 1.1},
                    {9.966, 0.0064, 901, 12.92, 2.34, 5.6},
                    {7.6, 4.6, 9.1, 0.23, 345.3, 45},
                    {7, 4, 1, 2, 3, 8.8},
                    {3, 4, 223.4, 34.23, 424, 12.12}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }

    @Test
    public void test_Inverse_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {99.96, 0.0064, 901, 12.92, 1.27, 269.1},
                    {99.66, 0.0064, 901, 12.92, 1.27, 1.1},
                    {9.966, 0.0064, 901, 12.92, 2.34, 5.6},
                    {7.6, 4.6, 9.1, 0.23, 345.3, 45},
                    {7, 4, 1, 2, 3, 8.8},
                    {3, 4, 223.4, 34.23, 424, 12.12}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-15));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }

    @Test
    public void test_Inverse_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 12.92, 1.27, 269.1},
                    {2.2, 0, 901, 12.92, 1.27, 5},
                    {3.3, 0.0064, 901, 12.92, 5.34, 2343},
                    {7.6, 4.6, 9.1, 0.23, 34.3, 96.34},
                    {7, 4, 1, 2, 3, 4},
                    {3, 32.34, 425.5, 423, 56.3, 0.23}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-13));
    }

    @Test
    public void test_Inverse_0032() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 12.92, 1.27, 269.1},
                    {2.2, 0, 901, 12.92, 1.27, 5},
                    {3.3, 0.0064, 901, 12.92, 5.34, 2343},
                    {7.6, 4.6, 9.1, 0.23, 34.3, 96.34},
                    {7, 4, 1, 2, 3, 4},
                    {3, 32.34, 425.5, 423, 56.3, 0.23}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-13));
    }

    @Test
    public void test_Inverse_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {5, 4, 4, 1, 5, 4, 2, 4, 1, 1},
                    {4, 5, 2, 2, 1, 2, 4, 5, 5, 2},
                    {5, 5, 3, 3, 5, 2, 3, 4, 1, 3},
                    {1, 2, 5, 5, 3, 1, 4, 3, 3, 3},
                    {2, 2, 4, 2, 3, 1, 3, 5, 4, 4},
                    {5, 4, 5, 1, 1, 3, 2, 3, 3, 4},
                    {3, 4, 4, 3, 4, 3, 2, 5, 5, 5},
                    {3, 4, 3, 3, 2, 1, 4, 2, 2, 1},
                    {4, 1, 1, 1, 1, 4, 4, 2, 2, 1},
                    {1, 5, 5, 5, 1, 1, 2, 4, 1, 4}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-13));
    }

    @Test
    public void test_Inverse_0037() {
        Matrix A = new DenseMatrix(new double[][]{
                    {5, 4, 4, 1, 5, 4, 2, 4, 1, 1},
                    {4, 5, 2, 2, 1, 2, 4, 5, 5, 2},
                    {5, 5, 3, 3, 5, 2, 3, 4, 1, 3},
                    {1, 2, 5, 5, 3, 1, 4, 3, 3, 3},
                    {2, 2, 4, 2, 3, 1, 3, 5, 4, 4},
                    {5, 4, 5, 1, 1, 3, 2, 3, 3, 4},
                    {3, 4, 4, 3, 4, 3, 2, 5, 5, 5},
                    {3, 4, 3, 3, 2, 1, 4, 2, 2, 1},
                    {4, 1, 1, 1, 1, 4, 4, 2, 2, 1},
                    {1, 5, 5, 5, 1, 1, 2, 4, 1, 4}
                });

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-13));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-13));
    }

    @Test
    public void test_Inverse_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                });

        Matrix Ainv = new Inverse(A);
        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A.ONE(), Ainv.multiply(A));
    }

    @Test
    public void test_Inverse_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                });

        Matrix Ainv = new Inverse(A);
        assertEquals(A.ONE(), A.multiply(Ainv));
        assertEquals(A.ONE(), Ainv.multiply(A));
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test(expected = RuntimeException.class)
    public void test_Inverse_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        Matrix Ainv = new Inverse(A);
    }

    /**
     * Test of non-invertible matrix.
     */
    @Test(expected = RuntimeException.class)
    public void test_Inverse_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {2, 4}
                });

        Matrix Ainv = new Inverse(A);
    }

    /**
     * Test of non-square matrix.
     */
    @Test(expected = RuntimeException.class)
    public void test_Inverse_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {2, 4},
                    {2, 5}
                });

        Matrix Ainv = new Inverse(A);
    }

    /**
     * Test of class Inverse of a matrix.
     *
     * TODO: how to handle this kind of precision problem?
     *
     * Very close to non-invertible.
     * The U matrix in the LU decomposition will be considered non-invertible
     * because one of its entry is smaller than Config.getPrecision,
     * hence A non-invertible.
     *
     * By changing the precision, it is possible to invert both L and U.
     * However, Ainv %*% A will not be equal to ONE.
     *
     * R has exactly the same precision problem.
     *
     * a = matrix(
     * c(
     * 110.00000000074328, -3.061255122206001E-9, 0, -219.99999999999997,
     * -3.061255122206001E-9, 6.000000012597035, -59.999999999999964, 0,
     * 0, -59.999999999999964, 600.0000000013468, -1.3468377878912817E-9,
     * -219.99999999999997, 0, -1.3468377878912817E-9, 440.0000000013467
     * ),4,4)
     *
     * b = solve(a)
     *
     * b %*% a # not ONE
     * a %*% b # not ONE
     */
    @Test
    public void test_Inverse_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {110.00000000074328, -3.061255122206001E-9, 0, -219.99999999999997},
                    {-3.061255122206001E-9, 6.000000012597035, -59.999999999999964, 0},
                    {0, -59.999999999999964, 600.0000000013468, -1.3468377878912817E-9},
                    {-219.99999999999997, 0, -1.3468377878912817E-9, 440.0000000013467}
                });

        Cholesky instance = new Cholesky(A);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        assertTrue(AreMatrices.equal(A, L.multiply(Lt), 1e-14));

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-4));//need to decrease the precision for comparison
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-4));//need to decrease the precision for comparison
    }

    @Test
    public void test_Inverse_0085() {
        Matrix A = new DenseMatrix(new double[][]{
                    {110.00000000074328, -3.061255122206001E-9, 0, -219.99999999999997},
                    {-3.061255122206001E-9, 6.000000012597035, -59.999999999999964, 0},
                    {0, -59.999999999999964, 600.0000000013468, -1.3468377878912817E-9},
                    {-219.99999999999997, 0, -1.3468377878912817E-9, 440.0000000013467}
                });

        Cholesky instance = new Cholesky(A);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        assertTrue(AreMatrices.equal(A, L.multiply(Lt), 1e-14));

        Matrix Ainv = new Inverse(A);
        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-4));//need to decrease the precision for comparison
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-4));//need to decrease the precision for comparison
    }

    @Test
    public void test_Inverse_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 5, 4},
                    {8, 7, 9}
                });

        Matrix Ainv = new Inverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }

    @Test
    public void test_Inverse_0095() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 5, 4},
                    {8, 7, 9}
                });

        Matrix Ainv = new Inverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of very small inputs.
     */
    @Test
    public void test_Inverse_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 5, 4},
                    {8, 7, 9}
                });
        A = A.scaled(1e-100);

        Matrix Ainv = new Inverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }

    /**
     * Test of very small inputs.
     */
    @Test
    public void test_Inverse_0105() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 5, 4},
                    {8, 7, 9}
                });
        A = A.scaled(1e-200);

        Matrix Ainv = new Inverse(A);

        assertTrue(AreMatrices.equal(A.ONE(), A.multiply(Ainv), 1e-14));
        assertTrue(AreMatrices.equal(A.ONE(), Ainv.multiply(A), 1e-14));
    }
    //</editor-fold>
}
