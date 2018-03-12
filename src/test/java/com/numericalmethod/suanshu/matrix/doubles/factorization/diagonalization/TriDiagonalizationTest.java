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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class TriDiagonalizationTest {

    @Test
    public void test_Tridiagonalization_0010() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 5, 7},
                    {5, 0, 6},
                    {7, 6, 1}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-14));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    @Test
    public void test_Tridiagonalization_0020() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 5, 7, 9},
                    {5, 0, 3, 13},
                    {7, 3, 1, 2},
                    {9, 13, 2, 10}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-14));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-13));
    }

    @Test
    public void test_Tridiagonalization_0030() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 5, 7, 9, 79.56},
                    {5, 0, 3, 13, 131},
                    {7, 3, 1, 2, 300},
                    {9, 13, 2, 10, 0.0001},
                    {79.56, 131, 300, 0.0001, 500}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-13));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-12));
    }

    @Test
    public void test_Tridiagonalization_0040() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1.234, 5.6789, 7.777, 71.23, 100, 0.26},
                    {5.6789, 0.123456789, 30.123456, 13.1313, 131131, 131131},
                    {7.777, 30.123456, 0.01, 20.02, 123, 2222},
                    {71.23, 13.1313, 20.02, 10.23, 0.0001, 0.2654},
                    {100, 131131, 123, 0.0001, 500.567, 500.567},
                    {0.26, 131131, 2222, 0.2654, 500.567, 123.56}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-10));//TODO: very poor precision

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-10));
    }

    /**
     * Experiment with precision.
     */
    @Test
    public void test_Tridiagonalization_0045() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1234000000, 5.6789, 7.777, 71.23, 100, 0.26},
                    {5.6789, 0.000000123456789, 30.123456, 13.1313, 131131, 131131},
                    {7.777, 30.123456, 0.01, 20.02, 123, 2222},
                    {71.23, 13.1313, 20.02, 10.23, 0.0001, 0.2654},
                    {100, 131131, 123, 0.0001, 500.567, 500.567},
                    {0.26, 131131, 2222, 0.2654, 500.567, 123.56}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-10));//TODO: very poor precision

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-9));//worse precision by 1 digit
    }

    /**
     * Already in tridiagonalization form.
     */
    @Test
    public void test_Tridiagonalization_0050() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 3, 0, 0},
                    {3, 0, 6, 0},
                    {0, 6, 1, 0},
                    {0, 0, 0, 10}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * ZERO.
     */
    @Test
    public void test_Tridiagonalization_0060() {
        Matrix S = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * ZERO.
     */
    @Test
    public void test_Tridiagonalization_0070() {
        Matrix S = new DenseMatrix(new double[][]{
                    {0}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * ZERO.
     */
    @Test
    public void test_Tridiagonalization_0080() {
        Matrix S = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * Identity.
     */
    @Test
    public void test_Tridiagonalization_0090() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * Identity.
     */
    @Test
    public void test_Tridiagonalization_0100() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Already in tridiagonalization form.
     * Identity.
     */
    @Test
    public void test_Tridiagonalization_0110() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 0));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }

    /**
     * Not a symmetric matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_Tridiagonalization_0120() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 0},
                    {1, 0}
                });
        TriDiagonalization instance = new TriDiagonalization(S);
    }

    /**
     * Not a symmetric matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_Tridiagonalization_0130() {
        Matrix S = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        TriDiagonalization instance = new TriDiagonalization(S);

        Matrix T = instance.T();
        assertTrue(IsMatrix.tridiagonal(T, 1e-14));

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-14));

        Matrix Texpected = Q.t().multiply(S).multiply(Q);
        assertTrue(AreMatrices.equal(Texpected, T, 1e-14));
    }
}
