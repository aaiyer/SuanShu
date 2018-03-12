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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GivensMatrixTest {

    @Test
    public void test_multiply_0010() {
        GivensMatrix G = GivensMatrix.Ctor2x2(2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {4, 5}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_multiply_0020() {
        GivensMatrix G = new GivensMatrix(3, 1, 2, 2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_multiply_0030() {
        GivensMatrix G = new GivensMatrix(5, 3, 4, 2.2, 7.23);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_multiply_0040() {
        GivensMatrix G = new GivensMatrix(5, 3, 4, 2.2, 7.23);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6.1},
                    {6, 7, 8, 9, 10, 11.1},
                    {11, 12, 13, 14, 15, 15.6},
                    {16, 17, 18, 19, 20, 21.6},
                    {21, 22, 23, 24, 25, 63.24}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_multiply_0050() {
        GivensMatrix G = new GivensMatrix(5, 3, 4, 2.2, 7.23);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {6, 7},
                    {11, 12},
                    {16, 17},
                    {21, 22}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_multiply_0060() {
        GivensMatrix G = new GivensMatrix(5, 3, 4, 0, 0);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {6, 7},
                    {11, 12},
                    {16, 17},
                    {21, 22}
                });
        Matrix GA1 = G.multiply(A1);

        Matrix expected = G.multiply(A1);
        assertEquals(expected, GA1);
    }

    @Test
    public void test_rightMultiply_0010() {
        GivensMatrix G = GivensMatrix.Ctor2x2(2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {4, 5}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_rightMultiply_0020() {
        GivensMatrix G = new GivensMatrix(3, 1, 2, 2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_rightMultiply_0030() {
        GivensMatrix G = new GivensMatrix(5, 3, 4, 2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_rightMultiply_0040() {
        GivensMatrix G = new GivensMatrix(6, 3, 4, 2, 7);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6.1},
                    {6, 7, 8, 9, 10, 11.1},
                    {11, 12, 13, 14, 15, 15.6},
                    {16, 17, 18, 19, 20, 21.6},
                    {21, 22, 23, 24, 25, 63.24}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_rightMultiply_0050() {
        GivensMatrix G = new GivensMatrix(3, 1, 2, 2.123, 7.234);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_rightMultiply_0060() {
        GivensMatrix G = new GivensMatrix(3, 1, 2, 0, 0);
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}
                });
        Matrix A1G = G.rightMultiply(A1);

        Matrix expected = A1.multiply(G);
        assertEquals(expected, A1G);
    }

    @Test
    public void test_product_0010() {
        GivensMatrix G1 = new GivensMatrix(5, 1, 2, 1.1, -2.2);
        Matrix M1 = new DenseMatrix(G1);
        GivensMatrix G2 = new GivensMatrix(5, 2, 3, 0.1, 12.2);
        Matrix M2 = new DenseMatrix(G2);

        Matrix instance = G1.multiply(G2);
        Matrix expected = M1.multiply(M2);

        assertEquals(expected, instance);
    }

    @Test
    public void test_product_0020() {
        GivensMatrix G1 = new GivensMatrix(5, 1, 2, 1.1, -2.2);
        Matrix M1 = new DenseMatrix(G1);
        GivensMatrix G2 = new GivensMatrix(5, 2, 3, 0.1, 12.2);
        Matrix M2 = new DenseMatrix(G2);
        GivensMatrix G3 = new GivensMatrix(5, 3, 4, -10.1, 1.2);
        Matrix M3 = new DenseMatrix(G3);
        GivensMatrix G4 = new GivensMatrix(5, 4, 5, 8.6, 25.2);
        Matrix M4 = new DenseMatrix(G4);

        Matrix instance = G1.multiply(G2).multiply(G3).multiply(G4);
        Matrix expected = M1.multiply(M2).multiply(M3).multiply(M4);

        assertEquals(expected, instance);
    }

    @Test
    public void test_product_0030() {
        GivensMatrix G1 = new GivensMatrix(5, 1, 2, 1.1, -2.2);
        Matrix M1 = new DenseMatrix(G1);
        GivensMatrix G2 = new GivensMatrix(5, 2, 3, 0.1, 12.2);
        Matrix M2 = new DenseMatrix(G2);
        GivensMatrix G3 = new GivensMatrix(5, 3, 4, -10.1, 1.2);
        Matrix M3 = new DenseMatrix(G3);
        GivensMatrix G4 = new GivensMatrix(5, 4, 5, 0, 0);
        Matrix M4 = new DenseMatrix(G4);

        Matrix instance = G1.multiply(G2).multiply(G3).multiply(G4);
        Matrix expected = M1.multiply(M2).multiply(M3).multiply(M4);

        assertEquals(expected, instance);
    }

    @Test
    public void test_ZeroOutEntry_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {4, 5}
                });
        GivensMatrix G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 1);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 1), 0);

        GivensMatrix Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 1);
        Matrix GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 1), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 2);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 2), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 2);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 2), 1e-15);
    }

    @Test
    public void test_ZeroOutEntry_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        GivensMatrix G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 1);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 1), 0);

        GivensMatrix Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 1);
        Matrix GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 1), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 3), 0);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 3), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 3), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 3), 1e-15);
    }

    @Test
    public void test_ZeroOutEntry_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        GivensMatrix G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 1);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 1), 0);

        GivensMatrix Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 1);
        Matrix GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 1), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 3), 0);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 3), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 3), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 5, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(5, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 5, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(5, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 2);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 2), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 2);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 2), 1e-15);
    }

    @Test
    public void test_ZeroOutEntry_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 9.1},
                    {6, 7, 8, 9, 10, 10.1},
                    {11, 12, 13, 14, 15, 15.1},
                    {16, 17, 18, 19, 20, 20.1},
                    {21, 22, 23, 24, 25, 26.3}
                });
        GivensMatrix G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 1);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 1), 0);

        GivensMatrix Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 1);
        Matrix GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 1), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 3), 0);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 3), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 3), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 5, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(5, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 5, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(5, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 6);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 6), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 6);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 6), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 6);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 6), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 6);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 6), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 4, 6);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(4, 6), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 4, 6);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(4, 6), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 5, 6);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(5, 6), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 5, 6);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(5, 6), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 5);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 5), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 5);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 5), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 2);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 2), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 2);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 2), 1e-15);
    }

    @Test
    public void test_ZeroOutEntry_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}
                });
        GivensMatrix G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 1);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 1), 0);

        GivensMatrix Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 1);
        Matrix GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 1), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 2, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(2, 3), 0);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 2, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(2, 3), 0);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 3, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-15);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 3, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(3, 3), 1e-15);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 5, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(5, 3), 1e-14);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 5, 3);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(5, 3), 1e-14);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 5, 1);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(5, 1), 1e-14);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 5, 1);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(5, 1), 1e-14);

        G = GivensMatrix.CtorToZeroOutEntry(A1, 1, 2);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 2), 1e-14);

        Gt = GivensMatrix.CtorToZeroOutEntryByTranspose(A1, 1, 2);
        GtA1 = Gt.t().multiply(A1);
        assertEquals(0d, GtA1.get(1, 2), 1e-14);
    }

    @Test
    public void test_Rotation_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4},
                    {4, 5, 6, 9},
                    {7, 8, 9, 10},
                    {72, 81, 19, 70}
                });
        GivensMatrix G = GivensMatrix.CtorToRotateRows(4, 1, 3, 3, 9);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-15);

        G = GivensMatrix.CtorToRotateRows(4, 4, 1, 19, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 3), 1e-15);

        G = GivensMatrix.CtorToRotateColumns(4, 2, 4, 8, 10);
        Matrix A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(3, 4), 0);

        G = GivensMatrix.CtorToRotateColumns(4, 4, 1, 70, 72);
        A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(4, 1), 0);
    }

    @Test
    public void test_Rotation_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100, 4e-100},
                    {4e-100, 5e-100, 6e-100, 9e-100},
                    {7e-100, 8e-100, 9e-100, 10e-100},
                    {72e-100, 81e-100, 19e-100, 70e-100}
                });
        GivensMatrix G = GivensMatrix.CtorToRotateRows(4, 1, 3, 3, 9);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-115);

        G = GivensMatrix.CtorToRotateRows(4, 4, 1, 19, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 3), 1e-115);

        G = GivensMatrix.CtorToRotateColumns(4, 2, 4, 8, 10);
        Matrix A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(3, 4), 1e-114);

        G = GivensMatrix.CtorToRotateColumns(4, 4, 1, 70, 72);
        A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(4, 1), 1e-114);
    }

    @Test
    public void test_Rotation_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1e-200, 2e-200, 3e-200, 4e-200},
                    {4e-200, 5e-200, 6e-200, 9e-200},
                    {7e-200, 8e-200, 9e-200, 10e-200},
                    {72e-200, 81e-200, 19e-200, 70e-200}
                });
        GivensMatrix G = GivensMatrix.CtorToRotateRows(4, 1, 3, 3, 9);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-215);

        G = GivensMatrix.CtorToRotateRows(4, 4, 1, 19, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 3), 1e-215);

        G = GivensMatrix.CtorToRotateColumns(4, 2, 4, 8, 10);
        Matrix A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(3, 4), 0);

        G = GivensMatrix.CtorToRotateColumns(4, 4, 1, 70, 72);
        A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(4, 1), 0);
    }

    @Test
    public void test_Rotation_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1e-100, 2, 3, 4},
                    {4, 5, 6, 9},
                    {7, 8, 9, 10},
                    {72, 81, 19, 70}
                });
        GivensMatrix G = GivensMatrix.CtorToRotateRows(4, 1, 3, 3, 9);
        Matrix GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(3, 3), 1e-15);

        G = GivensMatrix.CtorToRotateRows(4, 4, 1, 19, 3);
        GA1 = G.multiply(A1);
        assertEquals(0d, GA1.get(1, 3), 1e-15);

        G = GivensMatrix.CtorToRotateColumns(4, 2, 4, 8, 10);
        Matrix A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(3, 4), 0);

        G = GivensMatrix.CtorToRotateColumns(4, 4, 1, 70, 72);
        A1G = A1.multiply(G);
        assertEquals(0d, A1G.get(4, 1), 0);
    }

    @Test
    public void test_rowcol_0010() {
        int dim = 5;
        int i = 2;
        int j = 4;
        double theta = Math.PI / 3;
        double cc = Math.cos(theta);
        double s = Math.sin(theta);
        Matrix G = new GivensMatrix(dim, i, j, cc, s);

        Matrix expected = new DenseMatrix(dim, dim).ONE();
        expected.set(i, i, cc);
        expected.set(j, j, cc);
        expected.set(i, j, s);
        expected.set(j, i, -s);

        for (int k = 1; k <= dim; ++k) {
            assertArrayEquals(expected.getRow(k).toArray(), G.getRow(k).toArray(), 1e-15);
            assertArrayEquals(expected.getColumn(k).toArray(), G.getColumn(k).toArray(), 1e-15);
        }
    }

    @Test
    public void test_multiplyVector_0010() {
        int dim = 5;
        int i = 2;
        int j = 4;
        double theta = Math.PI / 3;
        double cc = Math.cos(theta);
        double s = Math.sin(theta);
        Matrix G = new GivensMatrix(dim, i, j, cc, s);

        DenseVector v = new DenseVector(1., 2., 3., 4., 5.);

        DenseVector expected = new DenseVector(1, 2 * cc + 4 * s, 3, 4 * cc - 2 * s, 5);

        assertArrayEquals(expected.toArray(), G.multiply(v).toArray(), 1e-15);
    }
}
