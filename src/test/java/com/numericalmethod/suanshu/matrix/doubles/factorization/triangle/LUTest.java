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
package com.numericalmethod.suanshu.matrix.doubles.factorization.triangle;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LUTest {

    @Test
    public void test_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(0, MatrixMeasure.det(A1), 1e-15);
    }

    @Test
    public void test_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(-23, MatrixMeasure.det(A1), 1e-15);
    }

    @Test
    public void test_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {5, 2, 3, 4},
                    {5, 6, 7, 4},
                    {1, 3, 2, 3},
                    {1, 7, 6, 2}
                });
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(24, MatrixMeasure.det(A1), 1e-13);
    }

    @Test
    public void test_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {10, 2, 3, 4, 5},
                    {6, 7, 88, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 79, 20},
                    {21, 22, 23, 24, 95}});
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
        assertEquals(0d, (-31317000d - MatrixMeasure.det(A1)) / -31317000d, 1e-15);
    }

    @Test
    public void test_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
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
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
        assertEquals(235431, MatrixMeasure.det(A1), 1e-9);
    }

    @Test
    public void test_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1, 4, 5, 1, 2, 3, 5, 1, 4},
                    {2, 5, 3, 4, 1, 5, 1, 2, 4, 3},
                    {4, 2, 2, 3, 3, 2, 1, 3, 3, 2},
                    {4, 1, 4, 5, 5, 1, 5, 1, 5, 5},
                    {2, 3, 5, 2, 4, 5, 5, 1, 5, 1},
                    {4, 2, 1, 4, 1, 3, 2, 2, 5, 3},
                    {2, 5, 1, 2, 3, 2, 3, 2, 1, 4},
                    {1, 1, 1, 5, 1, 3, 5, 3, 4, 5},
                    {4, 4, 2, 3, 5, 3, 5, 2, 5, 4},
                    {2, 2, 3, 3, 5, 1, 4, 4, 5, 4}
                });
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-15));
        assertEquals(-16006, MatrixMeasure.det(A1), 1e-9);
    }

    @Test
    public void test_0070() {
        Matrix A1 = new DenseMatrix(new double[][]{{0}});
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(0, MatrixMeasure.det(A1), 1e-15);
    }

    @Test
    public void test_0080() {
        Matrix A1 = new DenseMatrix(new double[][]{{100}});
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(100, MatrixMeasure.det(A1), 1e-15);
    }

    @Test
    public void test_0090() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {2, 3, 0},
                    {4, 5, 6}
                });
        LU instance = new LU(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(18, MatrixMeasure.det(A1), 1e-15);
    }

    @Test
    public void test_0100() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        LU instance = new LU(A1.toDense());
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals((31702850579d - MatrixMeasure.det(A1)) / 31702850579d, 0d, 1e-10);//use % diff
    }

    /**
     * Test of small numbers.
     */
    @Test
    public void test_0110() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        A1 = A1.scaled(1e-100);

        LU instance = new LU(A1.toDense());
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
        assertEquals(0, MatrixMeasure.det(A1), 1e-15);
    }

    /**
     * very tiny singular matrix
     */
    @Test
    public void test_0200() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        A1 = A1.scaled(1e-100);

        LU instance = new LU(A1);

        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-114));
        assertEquals(0, MatrixMeasure.det(A1), 1e-115);
    }
}
