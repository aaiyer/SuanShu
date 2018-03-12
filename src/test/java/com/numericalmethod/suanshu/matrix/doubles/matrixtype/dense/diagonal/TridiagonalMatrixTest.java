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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import org.hamcrest.CoreMatchers;

/**
 *
 * @author Haksun Li
 */
public class TridiagonalMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors">
    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0005() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2},
                    {1, 4},
                    {3}
                });

        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0010() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(5, instance.get(2, 3), 0));
        assertEquals(0, compare(6, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(8, instance.get(3, 4), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(11, instance.get(4, 5), 0));
        assertEquals(0, compare(12, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {3, 4, 5, 0, 0},
                    {0, 6, 7, 8, 0},
                    {0, 0, 9, 10, 11},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0020() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(6, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
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
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0030() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {0, 0, 0, 0}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
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

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0040() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0050() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0, 0},//wrong dimension
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0060() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}//wrong dimension
                });
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0070() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0},
                    {0},
                    {0}
                });
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0080() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    null
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(5, instance.get(2, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(8, instance.get(3, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(11, instance.get(4, 5), 0));
        assertEquals(0, compare(0, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {0, 4, 5, 0, 0},
                    {0, 0, 7, 8, 0},
                    {0, 0, 0, 10, 11},
                    {0, 0, 0, 0, 13}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_ctors_0090() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    null,
                    {1, 4, 7, 10, 13},
                    null
                });

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 5), 0));
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set and get methods">
    /**
     * Test of set and get methods, of class TridiagonalMatrix.
     */
    @Test
    public void test_set_get_0005() {
        TridiagonalMatrix instance = new TridiagonalMatrix(2);
        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(1, 2, 2);
        instance.set(2, 1, 3);
        instance.set(2, 2, 4);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class TridiagonalMatrix.
     */
    @Test
    public void test_set_get_0010() {
        TridiagonalMatrix instance = new TridiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(1, 2, 2);
        instance.set(2, 1, 3);
        instance.set(2, 2, 4);
        instance.set(2, 3, 5);
        instance.set(3, 2, 6);
        instance.set(3, 3, 7);
        instance.set(3, 4, 8);
        instance.set(4, 3, 9);
        instance.set(4, 4, 10);
        instance.set(4, 5, 11);
        instance.set(5, 4, 12);
        instance.set(5, 5, 13);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));
        assertEquals(0, compare(5, instance.get(2, 3), 0));
        assertEquals(0, compare(6, instance.get(3, 2), 0));
        assertEquals(0, compare(7, instance.get(3, 3), 0));
        assertEquals(0, compare(8, instance.get(3, 4), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(10, instance.get(4, 4), 0));
        assertEquals(0, compare(11, instance.get(4, 5), 0));
        assertEquals(0, compare(12, instance.get(5, 4), 0));
        assertEquals(0, compare(13, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {3, 4, 5, 0, 0},
                    {0, 6, 7, 8, 0},
                    {0, 0, 9, 10, 11},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class TridiagonalMatrix.
     */
    @Test
    public void test_set_get_0020() {
        TridiagonalMatrix instance = new TridiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class TridiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_set_get_0030() {
        TridiagonalMatrix instance = new TridiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 99);//value should be 0
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for math methods">
    @Test
    public void test_math_0010() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        TridiagonalMatrix A2 = new TridiagonalMatrix(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {3.3, 6.6, 9.9, 12.12}
                });

        Matrix A3 = A1.add(A2);

        TridiagonalMatrix expected = new TridiagonalMatrix(new double[][]{
                    {4.2, 10.5, 16.8, 22.11},
                    {2.1, 8.4, 14.7, 20.1, 26.13},
                    {6.3, 12.6, 18.9, 24.12}
                });

        assertTrue(AreMatrices.equal(expected, A3, 1e-14));
        assertThat(A3, CoreMatchers.instanceOf(TridiagonalMatrix.class));
    }

    @Test
    public void test_math_0020() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        TridiagonalMatrix A2 = new TridiagonalMatrix(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {3.3, 6.6, 9.9, 12.12}
                });

        Matrix A3 = A2.minus(A1);

        TridiagonalMatrix expected = new TridiagonalMatrix(new double[][]{
                    {0.2, 0.5, 0.8, 0.11},
                    {0.1, 0.4, 0.7, 0.1, 0.13},
                    {0.3, 0.6, 0.9, 0.12}
                });

        assertTrue(AreMatrices.equal(expected, A3, 1e-14));
        assertThat(A3, CoreMatchers.instanceOf(TridiagonalMatrix.class));
    }

    @Test
    public void test_math_0030() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A2 = A1.scaled(3);

        TridiagonalMatrix expected = new TridiagonalMatrix(new double[][]{
                    {6, 15, 24, 33},
                    {3, 12, 21, 30, 39},
                    {9, 18, 27, 36}
                });

        assertEquals(expected, A2);
        assertThat(A2, CoreMatchers.instanceOf(TridiagonalMatrix.class));
    }

    @Test
    public void test_math_0040() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A2 = A1.opposite();

        TridiagonalMatrix expected = new TridiagonalMatrix(new double[][]{
                    {-2, -5, -8, -11},
                    {-1, -4, -7, -10, -13},
                    {-3, -6, -9, -12}
                });

        assertEquals(expected, A2);
        assertThat(A2, CoreMatchers.instanceOf(TridiagonalMatrix.class));
    }

    @Test
    public void test_math_0050() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A2 = A1.minus(A1);

        assertEquals(A2.ZERO(), A2);
        assertThat(A2, CoreMatchers.instanceOf(TridiagonalMatrix.class));
    }

    @Test
    public void test_math_0060() {
        TridiagonalMatrix A1 = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {1, 1, 1, 1, 1},
                    {0, 0, 0, 0}
                });
        assertEquals(A1.ONE(), A1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for get sub parts methods">
    @Test
    public void test_parts_0050() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2},
                    {1, 4},
                    {3}
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 4}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{2}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{3}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 2}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{3, 4}), instance.getRow(2));

        assertEquals(new DenseVector(new double[]{1, 3}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 4}), instance.getColumn(2));
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_parts_0010() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {3, 4, 5, 0, 0},
                    {0, 6, 7, 8, 0},
                    {0, 0, 9, 10, 11},
                    {0, 0, 0, 12, 13}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 4, 7, 10, 13}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{2, 5, 8, 11}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{3, 6, 9, 12}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 2, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{3, 4, 5, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 6, 7, 8, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 9, 10, 11}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 12, 13}), instance.getRow(5));

        assertEquals(new DenseVector(new double[]{1, 3, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 4, 6, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 5, 7, 9, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 8, 10, 12}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 11, 13}), instance.getColumn(5));
    }

    /**
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_parts_0020() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
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
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_parts_0030() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {0, 0, 0, 0}
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
     * Test of constructors, of class TridiagonalMatrix.
     */
    @Test
    public void test_parts_0040() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
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
    public void test_t_0050() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2},
                    {1, 4},
                    {3}
                });

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3},
                    {2, 4}
                });

        assertTrue(AreMatrices.equal(A, instance.t(), 0));
    }

    @Test
    public void test_t_0010() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 3, 0, 0, 0},
                    {2, 4, 6, 0, 0},
                    {0, 5, 7, 9, 0},
                    {0, 0, 8, 10, 12},
                    {0, 0, 0, 11, 13}
                });

        TridiagonalMatrix A2 = new TridiagonalMatrix(new double[][]{
                    {3, 6, 9, 12},
                    {1, 4, 7, 10, 13},
                    {2, 5, 8, 11}
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0020() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
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

        TridiagonalMatrix A2 = new TridiagonalMatrix(new double[][]{
                    {3, 6, 9, 12},
                    {1, 4, 7, 10, 13},
                    null
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0030() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {0, 0, 0, 0}
                });

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 7, 0, 0},
                    {0, 0, 0, 10, 0},
                    {0, 0, 0, 0, 13}
                });

        TridiagonalMatrix A2 = new TridiagonalMatrix(new double[][]{
                    null,
                    {1, 4, 7, 10, 13},
                    null
                });

        assertTrue(AreMatrices.equal(A1, instance.t(), 0));
        assertTrue(AreMatrices.equal(A2, instance.t(), 0));
    }

    @Test
    public void test_t_0040() {
        TridiagonalMatrix instance = new TridiagonalMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertTrue(AreMatrices.equal(instance, instance.t(), 0));
    }
    //</editor-fold>
}
