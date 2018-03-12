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

import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GaussianEliminationTest {

    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {2, 2, -1},
                    {4, -1, 6}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {2, 2, -1},
                    {4, -1, 6}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {1, 2, 3},
                    {4, 5, 6}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {1, 2, 3},
                    {4, 5, 6}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    @Test
    public void test_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 2.3, 69.3},
                    {4, 5, 6, 31, -65},
                    {7, 8, 9, 0.36, 63.9},
                    {17, 8.8, -0.999, 10.36, 263.9},
                    {77, 83.96, 9.6, 37.6, 633.9}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-14));
    }

    @Test
    public void test_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 2.3, 69.3},
                    {4, 5, 6, 31, -65},
                    {7, 8, 9, 0.36, 63.9},
                    {17, 8.8, -0.999, 10.36, 263.9},
                    {77, 83.96, 9.6, 37.6, 633.9}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-14));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 99, 32, 723}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-14));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {4, 5, 6, 11, 23},
                    {7, 8, 9, 32, 86},
                    {71, 18, 99, 32, 723}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-14));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-14));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 23, 6.8, 322.9, 123},
                    {4, 5, 6, 11, 23, 35, 12, 3, 8},
                    {7, 8, 9, 32, 86, 1, 23, 5, 9},
                    {71, 18, 99, 32, 723, 9, 5, 234, 1.23}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-13));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 23, 6.8, 322.9, 123},
                    {4, 5, 6, 11, 23, 35, 12, 3, 8},
                    {7, 8, 9, 32, 86, 1, 23, 5, 9},
                    {71, 18, 99, 32, 723, 9, 5, 234, 1.23}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-13));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-13));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0070() {
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
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-12));
    }

    /**
     * Test of rectangle matrix
     */
    @Test
    public void test_0075() {
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
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 1e-15));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-12));
        assertTrue(AreMatrices.equal(P.multiply(A), L.multiply(U), 1e-12));
    }

    /**
     * Test of small numbers.
     */
    @Test
    public void test_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 9e-100}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-115));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * Test of small numbers.
     */
    @Test
    public void test_0085() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 9e-100}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-115));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * input already a triangular matrix
     */
    @Test
    public void test_0120() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {2, 3, 4}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * input already a triangular matrix
     */
    @Test
    public void test_0130() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {2, 3, 4}
                });
        GaussianElimination instance = new GaussianElimination(A, true, 0);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * input already an upper triangular matrix
     */
    @Test
    public void test_0140() {
        UpperTriangularMatrix A = new UpperTriangularMatrix(new double[][]{
                    {3, 2, 1},
                    {2, 1},
                    {1}
                });
        GaussianElimination instance = new GaussianElimination(A.toDense());

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * not every non-singular matrix can be factored as A = LU
     * http://www.alkires.com/103/6_lu.pdfS
     *
     * Gaussian Elimination cannot be done without pivoting for this matrix because of 0 pivot.
     */
    @Test
    public void test_0150() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 2},
                    {0, 1, -1}
                });

        GaussianElimination instance = new GaussianElimination(A.toDense(), false, 1e-10);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

//        assertTrue(IsMatrix.upperTriangular(U, 0));
//        assertTrue(IsMatrix.lowerTriangular(L, 0));
//        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
//        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * not every non-singular matrix can be factored as A = LU
     *
     * http://www.alkires.com/103/6_lu.pdfS
     */
    @Test
    public void test_0160() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 2},
                    {0, 1, -1}
                });
        GaussianElimination instance = new GaussianElimination(A.toDense(), true, 1e-10);//with pivoting

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * tiny matrix
     */
    @Test
    public void test_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {7e-100, 8e-100, 9e-100},
                    {4e-100, 5e-100, 6e-100}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * zero matrix
     */
    @Test
    public void test_0180() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        GaussianElimination instance = new GaussianElimination(A);

        Matrix U = instance.U();
        Matrix T = instance.T();
        Matrix L = instance.L();
        PermutationMatrix P = instance.P();

        assertTrue(IsMatrix.upperTriangular(U, 0));
        assertTrue(IsMatrix.lowerTriangular(L, 0));
        assertTrue(AreMatrices.equal(U, T.multiply(A), 1e-15));
        assertEquals(P.multiply(A), L.multiply(U));
    }

    /**
     * need a bigger threshold to identify practical 0s to pass this test case
     */
    @Test
    public void test_0190() {
        int dim = 50;
        Matrix A = new DenseMatrix(dim, dim);
        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A.set(i, j, ++count);
            }
        }

        GaussianElimination instance = new GaussianElimination(A);
    }
}
