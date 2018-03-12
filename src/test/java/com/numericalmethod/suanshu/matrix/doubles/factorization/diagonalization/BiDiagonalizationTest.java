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
package com.numericalmethod.suanshu.matrix.doubles.factorization.diagonalization;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.BidiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BiDiagonalizationTest {

    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 5, 7},
                    {5, 0, 6},
                    {7, 6, 1}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Bexpected, B, 1e-14));
    }

    @Test
    public void test_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 5, 7, 5.2, 39.12},
                    {5, 0, 6, 0.36, 9.123},
                    {7, 6, 1, 56.23, 36.17},
                    {123.56, 5.6, 697.2, 364.1, 3639},
                    {65.36, 74.69, 1233.63, 396.1, 56.01}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Bexpected, B, 1e-12));
    }

    @Test
    public void test_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 5, 7},
                    {5, 0, 6},
                    {7, 6, 1},
                    {7.7, 6.6, 1.1}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 1e-14));
    }

    @Test
    public void test_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 5, 7, 7.5},
                    {5, 0, 6, 9.63},
                    {7, 6, 1, 0.123},
                    {7.7, 6.6, 1.1, 12.369},
                    {1.1, 22.22, 333.333, 4.4},
                    {12.1, 92.29, 63.33, 24.4}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-14));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-14));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 1e-12));
    }

    @Test
    public void test_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 5, 0, 7.5},
                    {5, 0, 0, 9.63},
                    {7, 6, 0, 0.123},
                    {7.7, 6.6, 0, 12.369},
                    {1.1, 22.22, 0, 4.4},
                    {12.1, 92.29, 0, 24.4}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-14));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-14));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 1e-13));
    }

    @Test
    public void test_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        BiDiagonalization instance = new BiDiagonalization(A);
    }

    @Test
    public void test_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 1e-14));
    }

    @Test
    public void test_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 1e-14));
    }

    @Test
    public void test_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.123}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-15));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-15));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(CreateMatrix.subMatrix(Bexpected, 1, A.nCols(), 1, A.nCols()), B, 0));
    }

    @Test
    public void test_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 5e-100, 7e-100},
                    {5e-100, 0e-100, 6e-100},
                    {7e-100, 6e-100, 1e-100}
                });
        BiDiagonalization instance = new BiDiagonalization(A);

        BidiagonalMatrix B = instance.B();
        assertTrue(IsMatrix.upperBidiagonal(B, 0));

        Matrix U = instance.U();
        assertTrue(IsMatrix.orthogonal(U, 1e-14));

        Matrix V = instance.V();
        assertTrue(IsMatrix.orthogonal(V, 1e-14));

        Matrix Bexpected = U.t().multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Bexpected, B, 1e-114));
    }
}
