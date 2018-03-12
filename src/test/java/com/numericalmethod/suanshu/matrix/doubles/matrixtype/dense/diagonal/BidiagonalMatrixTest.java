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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class BidiagonalMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors">
    @Test
    public void test_ctors_0005() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {2},
                    {1, 4},});

        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());
        assertEquals(BidiagonalMatrix.BidiagonalMatrixType.UPPER, instance.getType());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 4}
                });

        assertEquals(A, instance.toDense());
    }

    @Test
    public void test_ctors_0010() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(BidiagonalMatrix.BidiagonalMatrixType.LOWER, instance.getType());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 3), 0));
        assertEquals(0, compare(6, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 4), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 5), 0));
        assertEquals(0, compare(12, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {3, 4, 0, 0, 0},
                    {0, 6, 7, 0, 0},
                    {0, 0, 9, 10, 0},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());
    }

    @Test
    public void test_ctors_0020() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    null,
                    {1, 4, 7, 10, 13}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(BidiagonalMatrix.BidiagonalMatrixType.UPPER, instance.getType());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 7, 0, 0},
                    {0, 0, 0, 10, 0},
                    {0, 0, 0, 0, 13}
                });

        assertEquals(A, instance.toDense());
    }

    @Test
    public void test_ctors_0030() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    null
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(BidiagonalMatrix.BidiagonalMatrixType.UPPER, instance.getType());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 7, 0, 0},
                    {0, 0, 0, 10, 0},
                    {0, 0, 0, 0, 13}
                });

        assertEquals(A, instance.toDense());
    }

    @Test
    public void test_ctors_0040() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0050() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0, 0, 0, 0, 0},//wrong dimension
                    {0, 0, 0, 0, 0}
                });
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0060() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}//wrong dimension
                });
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0070() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0},
                    {0}
                });
    }

    @Test
    public void test_ctors_0080() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4},
                    {3}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 3},
                    {0, 4}
                });

        BidiagonalMatrix A2 = new BidiagonalMatrix(new double[][]{
                    {3},
                    {1, 4}
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set and get methods">
    /**
     * Test of set and get methods, of class BidiagonalMatrix.
     */
    @Test
    public void test_set_get_0005() {
        BidiagonalMatrix instance = new BidiagonalMatrix(2, BidiagonalMatrix.BidiagonalMatrixType.UPPER);
        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(1, 2, 2);
        instance.set(2, 2, 4);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 4}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class BidiagonalMatrix.
     */
    @Test
    public void test_set_get_0010() {
        BidiagonalMatrix instance = new BidiagonalMatrix(5, BidiagonalMatrix.BidiagonalMatrixType.LOWER);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(2, 1, 3);
        instance.set(2, 2, 4);
        instance.set(3, 2, 6);
        instance.set(3, 3, 7);
        instance.set(4, 3, 9);
        instance.set(4, 4, 10);
        instance.set(5, 4, 12);
        instance.set(5, 5, 13);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 3), 0));
        assertEquals(0, compare(6, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 4), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 5), 0));
        assertEquals(0, compare(12, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {3, 4, 0, 0, 0},
                    {0, 6, 7, 0, 0},
                    {0, 0, 9, 10, 0},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class BidiagonalMatrix.
     */
    @Test
    public void test_set_get_0020() {
        BidiagonalMatrix instance = new BidiagonalMatrix(5, BidiagonalMatrix.BidiagonalMatrixType.UPPER);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class BidiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_set_get_0030() {
        BidiagonalMatrix instance = new BidiagonalMatrix(5, BidiagonalMatrix.BidiagonalMatrixType.UPPER);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 99);//value should be 0
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for get sub parts methods">
    @Test
    public void test_parts_0050() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {2},
                    {1, 4}
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 4}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 4}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{2}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{0}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 2}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 4}), instance.getRow(2));

        assertEquals(new DenseVector(new double[]{1, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 4}), instance.getColumn(2));
    }

    /**
     * Test of constructors, of class BidiagonalMatrix.
     */
    @Test
    public void test_parts_0010() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {3, 4, 0, 0, 0},
                    {0, 6, 7, 0, 0},
                    {0, 0, 9, 10, 0},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 4, 7, 10, 13}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{3, 6, 9, 12}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{3, 4, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 6, 7, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 9, 10, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 12, 13}), instance.getRow(5));

        assertEquals(new DenseVector(new double[]{1, 3, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 4, 6, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 7, 9, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 10, 12}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 13}), instance.getColumn(5));
    }

    /**
     * Test of constructors, of class BidiagonalMatrix.
     */
    @Test
    public void test_parts_0030() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    null
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 7, 0, 0},
                    {0, 0, 0, 10, 0},
                    {0, 0, 0, 0, 13}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 4, 7, 10, 13}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 4, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 0, 7, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 10, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 13}), instance.getRow(5));

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 4, 0, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 7, 0, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 10, 0}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 13}), instance.getColumn(5));
    }

    /**
     * Test of constructors, of class BidiagonalMatrix.
     */
    @Test
    public void test_parts_0040() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getRow(5));

        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 0}), instance.getColumn(5));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose methods">
    @Test
    public void test_t_0010() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {2, 4, 0, 0, 0},
                    {0, 5, 7, 0, 0},
                    {0, 0, 8, 10, 0},
                    {0, 0, 0, 11, 13}
                });

        BidiagonalMatrix A2 = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {2, 5, 8, 11}
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0020() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 3, 0, 0, 0},
                    {0, 4, 6, 0, 0},
                    {0, 0, 7, 9, 0},
                    {0, 0, 0, 10, 12},
                    {0, 0, 0, 0, 13}
                });

        BidiagonalMatrix A2 = new BidiagonalMatrix(new double[][]{
                    {3, 6, 9, 12},
                    {1, 4, 7, 10, 13}
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0030() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    null,
                    {1, 4, 7, 10, 13}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 7, 0, 0},
                    {0, 0, 0, 10, 0},
                    {0, 0, 0, 0, 13}
                });

        BidiagonalMatrix A2 = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    null
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0040() {
        BidiagonalMatrix instance = new BidiagonalMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    null
                });

        assertEquals(instance, instance.t());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for multiply methods">
    @Test
    public void test_times_0010() {
        Matrix A1 = new BidiagonalMatrix(new double[][]{
                    {2},
                    {1, 4}
                });

        Matrix A2 = new BidiagonalMatrix(new double[][]{
                    {1, 4},
                    {3}
                });

        Matrix expected = new TridiagonalMatrix(new double[][]{
                    {8},
                    {7, 16},
                    {12}
                });

        assertEquals(expected, A1.multiply(A2));
    }

    @Test
    public void test_times_0020() {
        Matrix A1 = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A2 = new BidiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13}
                });

        Matrix expected = new TridiagonalMatrix(new double[][]{
                    {2, 20, 56, 110},
                    {1, 22, 79, 172, 301},
                    {3, 24, 63, 120}
                });

        assertEquals(expected, A1.multiply(A2));
    }

    @Test
    public void test_times_0030() {
        Matrix A1 = new BidiagonalMatrix(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A2 = new BidiagonalMatrix(new double[][]{
                    null,
                    {0, 0, 0, 0, 0}
                });

        Matrix expected = new TridiagonalMatrix(5).ZERO();

        assertEquals(expected, A1.multiply(A2));
    }

    @Test
    public void test_times_0040() {
        Matrix A1 = new BidiagonalMatrix(new double[][]{
                    {2, 4, 6, 8},
                    {1, 3, 5, 7, 9}
                });

        Matrix A2 = new BidiagonalMatrix(new double[][]{
                    {18, 16, 14, 12},
                    {19, 17, 15, 13, 11}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {19, 52, 32, 0, 0},
                    {0, 51, 108, 56, 0},
                    {0, 0, 75, 148, 72},
                    {0, 0, 0, 91, 172},
                    {0, 0, 0, 0, 99}
                });

        Matrix A1A2 = A1.multiply(A2);
        assertTrue(AreMatrices.equal(expected, A1A2, 0));
    }
    //</editor-fold>
}
