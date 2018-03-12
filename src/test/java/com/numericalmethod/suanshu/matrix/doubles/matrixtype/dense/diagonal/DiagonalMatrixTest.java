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
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class DiagonalMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors">
    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_ctors_0005() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2});

        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(2, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 2}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_ctors_0010() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(2, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(3, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(4, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 5), 0));
        assertEquals(0, compare(0, instance.get(5, 4), 0));
        assertEquals(0, compare(5, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0},
                    {0, 0, 3, 0, 0},
                    {0, 0, 0, 4, 0},
                    {0, 0, 0, 0, 5}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_ctors_0020() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{0, 0, 0, 0, 0});

        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(instance.ZERO(), instance.toDense());
    }

    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_ctors_0030() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{0});

        assertEquals(1, instance.nRows());
        assertEquals(1, instance.nCols());
        assertEquals(instance.ZERO(), instance.toDense());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set and get methods">
    /**
     * Test of set and get methods, of class DiagonalMatrix.
     */
    @Test
    public void test_set_get_0005() {
        DiagonalMatrix instance = new DiagonalMatrix(2);
        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(2, 2, 2);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(2, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 2}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalMatrix.
     */
    @Test
    public void test_set_get_0010() {
        DiagonalMatrix instance = new DiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 1, 1);
        instance.set(2, 2, 2);
        instance.set(3, 3, 3);
        instance.set(4, 4, 4);
        instance.set(5, 5, 5);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(2, instance.get(2, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 2), 0));
        assertEquals(0, compare(3, instance.get(3, 3), 0));
        assertEquals(0, compare(0, instance.get(3, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 3), 0));
        assertEquals(0, compare(4, instance.get(4, 4), 0));
        assertEquals(0, compare(0, instance.get(4, 5), 0));
        assertEquals(0, compare(0, instance.get(5, 4), 0));
        assertEquals(0, compare(5, instance.get(5, 5), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0},
                    {0, 0, 3, 0, 0},
                    {0, 0, 0, 4, 0},
                    {0, 0, 0, 0, 5}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalMatrix.
     */
    @Test
    public void test_set_get_0020() {
        DiagonalMatrix instance = new DiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_set_get_0030() {
        DiagonalMatrix instance = new DiagonalMatrix(5);
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.set(1, 3, 99);//value should be 0
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for math methods">
    @Test
    public void test_math_0010() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = new DiagonalMatrix(new double[]{10, 20, 30, 40, 50});

        Matrix A3 = A1.add(A2);
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{11, 22, 33, 44, 55});

        assertEquals(expected, A3);
        assertThat(A3, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }

    @Test
    public void test_math_0020() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = new DiagonalMatrix(new double[]{10, 20, 30, 40, 50});

        Matrix A3 = A2.minus(A1);
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{9, 18, 27, 36, 45});

        assertEquals(expected, A3);
        assertThat(A3, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }

    @Test
    public void test_math_0030() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = A1.scaled(3);
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{3, 6, 9, 12, 15});

        assertEquals(expected, A2);
        assertThat(A2, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }

    @Test
    public void test_math_0040() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = A1.opposite();
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{-1, -2, -3, -4, -5});

        assertEquals(expected, A2);
        assertThat(A2, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }

    @Test
    public void test_math_0050() {
        DiagonalMatrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = A1.minus(A1);

        assertEquals(A2.ZERO(), A2);
    }

    @Test
    public void test_math_0060() {
        DiagonalMatrix A1 = new DiagonalMatrix(new double[]{1, 1, 1, 1, 1});
        assertEquals(A1.ONE(), A1);
    }

    @Test
    public void test_math_0070() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});
        Matrix A2 = new DiagonalMatrix(new double[]{10, 20, 30, 40, 50});

        Matrix A3 = A2.multiply(A1);
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{10, 40, 90, 160, 250});

        assertEquals(expected, A3);
        assertThat(A3, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_math_0080() {
        DiagonalMatrix A1 = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5, 99});//wrong dimension
        DiagonalMatrix A2 = new DiagonalMatrix(new double[]{10, 20, 30, 40, 50});
        Matrix A3 = A2.multiply(A1);
    }

    @Test
    public void test_math_0090() {
        Matrix A1 = new DiagonalMatrix(new double[]{1, 2, 3});
        Matrix A2 = new DiagonalMatrix(new double[]{10, 20, 30});

        Matrix A3 = A1.multiply(A2);
        DiagonalMatrix expected = new DiagonalMatrix(new double[]{10, 40, 90});

        assertEquals(expected, A3);
        assertThat(A3, CoreMatchers.instanceOf(DiagonalMatrix.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for get sub parts methods">
    @Test
    public void test_parts_0050() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 2}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 2}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{0}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{0}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 2}), instance.getRow(2));

        assertEquals(new DenseVector(new double[]{1, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 2}), instance.getColumn(2));
    }

    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_parts_0010() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0},
                    {0, 0, 3, 0, 0},
                    {0, 0, 0, 4, 0},
                    {0, 0, 0, 0, 5}
                });

        assertEquals(A, instance.toDense());

        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5}), instance.getDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSuperDiagonal());
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0}), instance.getSubDiagonal());

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 2, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 0, 3, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 4, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 5}), instance.getRow(5));

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 2, 0, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 3, 0, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 4, 0}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 5}), instance.getColumn(5));
    }

    /**
     * Test of constructors, of class DiagonalMatrix.
     */
    @Test
    public void test_parts_0040() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{0, 0, 0, 0, 0});

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
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 2}
                });

        assertTrue(AreMatrices.equal(A, instance.t(), 0));
    }

    @Test
    public void test_t_0010() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{1, 2, 3, 4, 5});

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0},
                    {0, 0, 3, 0, 0},
                    {0, 0, 0, 4, 0},
                    {0, 0, 0, 0, 5}
                });

        assertTrue(AreMatrices.equal(A, instance.t(), 0));

    }

    @Test
    public void test_t_0040() {
        DiagonalMatrix instance = new DiagonalMatrix(new double[]{0, 0, 0, 0, 0});
        assertTrue(AreMatrices.equal(instance, instance.t(), 0));
    }
    //</editor-fold>
}
