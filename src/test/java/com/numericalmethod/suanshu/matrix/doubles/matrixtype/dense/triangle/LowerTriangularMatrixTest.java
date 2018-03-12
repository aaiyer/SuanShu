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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle;

import org.hamcrest.CoreMatchers;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class LowerTriangularMatrixTest {

    private long timer = 0;

    @Before
    public void setUp() {
        timer = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        timer = System.currentTimeMillis() - timer;
//        System.out.println(String.format("time elapsed: %d msec", timer));
    }

    //<editor-fold defaultstate="collapsed" desc="tests for constructors of LowerTriangularMatrix">
    /**
     * Creating a DenseMatrix from a 2D array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_0010() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10}});
    }

    /**
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void test_ctor_0020() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(15, instance.get(5, 5), 0));
        assertEquals(0, compare(11, instance.get(5, 1), 0));

        Matrix A1 = instance.toDense();
        LowerTriangularMatrix A2 = new LowerTriangularMatrix(A1);
        assertEquals(instance, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_0025() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {2, 3, 0, 0, 0},
                    {4, 5, 6, 0, 0},
                    {11, 12, 13, 14, 15}});

        LowerTriangularMatrix A2 = new LowerTriangularMatrix(A1);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void test_ctor_0027() {
////
//        Matrix A1 = new DenseMatrix(new double[][]{
//                    {1, 0, 0, 0, 999},
//                    {2, 3, 0, 0, 0},
//                    {4, 5, 6, 0, 0},
//                    {7, 8, 9, 10, 0},
//                    {11, 12, 13, 14, 15}});
//
//        LowerTriangularMatrix A2 = new LowerTriangularMatrix(A1);
//    }
    /**
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void test_ctor_0030() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{{0}});
        assertEquals(1, instance.nRows());
        assertEquals(1, instance.nCols());
        assertEquals(0, compare(0, instance.get(1, 1), 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for add, minus, scaled methods of class LowerTriangularMatrix">
    @Test
    public void test_add_minus_scaled_0010() {
        Matrix instance1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix instance2 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix instance3 = instance1.add(instance2);
        Matrix instance4 = instance1.scaled(2.0);
        assertEquals(instance3, instance4);
        assertThat(instance3, CoreMatchers.instanceOf(LowerTriangularMatrix.class));
        assertThat(instance4, CoreMatchers.instanceOf(LowerTriangularMatrix.class));

        Matrix instance5 = instance3.minus(instance2);
        assertEquals(instance1, instance5);
        assertThat(instance5, CoreMatchers.instanceOf(LowerTriangularMatrix.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for multiply method">
    @Test
    public void test_multiply_0010() {
        Matrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix A2 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix A3 = A1.multiply(A2);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {8, 9, 0, 0, 0},
                    {38, 45, 36, 0, 0},
                    {129, 149, 144, 100, 0},
                    {350, 393, 399, 350, 225}
                });

        assertTrue(AreMatrices.equal(A3, expected, 0));
        assertThat(A3, CoreMatchers.instanceOf(LowerTriangularMatrix.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for opposite and scaled">
    /**
     * Test of opposite method, of class LowerTriangularMatrix.
     */
    @Test
    public void test_opposite() {
        LowerTriangularMatrix instance1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        Matrix instance2 = instance1.opposite();
        Matrix instance3 = instance1.scaled(-1.0);
        assertEquals(instance2, instance3);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for row and column">
    /**
     * Test of row method, of class LowerTriangularMatrix.
     */
    @Test
    public void testRow_001() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{2, 3, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{4, 5, 6, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{7, 8, 9, 10, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{11, 12, 13, 14, 15}), instance.getRow(5));
    }

    /**
     * Test of row method, of class LowerTriangularMatrix.
     */
    @Test
    public void testRow_002() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getRow(1));
    }

    /**
     * Test of row method, of class LowerTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRow_003() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{{1}});
        instance.getRow(2);
    }

    /**
     * Test of row method, of class LowerTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRow_004() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{{1}});
        instance.getRow(0);
    }

    /**
     * Test of column method, of class LowerTriangularMatrix.
     */
    @Test
    public void testColumn_001() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        assertEquals(new DenseVector(new double[]{1, 2, 4, 7, 11}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 3, 5, 8, 12}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 6, 9, 13}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 10, 14}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 15}), instance.getColumn(5));
    }

    /**
     * Test of column method, of class LowerTriangularMatrix.
     */
    @Test
    public void testColumn_002() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});

        assertEquals(new DenseVector(new double[]{1, 2, 4}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 3, 5}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 6}), instance.getColumn(3));
    }

    /**
     * Test of row method, of class LowerTriangularMatrix.
     */
    @Test
    public void testColumn_003() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getColumn(1));
    }

    /**
     * Test of column method, of class LowerTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumn_004() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});
        instance.getColumn(4);
    }

    /**
     * Test of column method, of class LowerTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumn_005() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});
        instance.getColumn(0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for toDense">
    @Test
    public void test_toDense_001() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {2, 3, 0, 0, 0},
                    {4, 5, 6, 0, 0},
                    {7, 8, 9, 10, 0},
                    {11, 12, 13, 14, 15}});

        assertEquals(A2, instance);
    }

    @Test
    public void test_toDense_002() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 0},
                    {2, 3}});

        assertEquals(A2, instance);
    }

    @Test
    public void test_toDense_003() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{{0}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{{0}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose">
    @Test
    public void test_t_001() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        UpperTriangularMatrix instance = A1.t();

        UpperTriangularMatrix A2 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 4, 7, 11},
                    {3, 5, 8, 12},
                    {6, 9, 13},
                    {10, 14},
                    {15}});
        assertEquals(A2, instance);
    }

    @Test
    public void test_t_002() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3}});
        UpperTriangularMatrix instance = A1.t();

        UpperTriangularMatrix A2 = new UpperTriangularMatrix(new double[][]{
                    {1, 2},
                    {3}});
        assertEquals(A2, instance);
    }

    @Test
    public void test_t_003() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{{1}});
        UpperTriangularMatrix instance = A1.t();
        UpperTriangularMatrix A2 = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ZERO">
    @Test
    public void test_ZERO_001() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(2).ZERO();

        assertEquals(0, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(0, instance.get(2, 2), 0);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});

        Matrix A2 = A1.multiply(instance);
        assertTrue(AreMatrices.equal(instance, A2, 0));

        Matrix A3 = instance.multiply(A1);
        assertTrue(AreMatrices.equal(instance, A3, 0));
    }

    /**
     * Check if a double array is initialized to 0 by jvm.
     */
    @Test
    public void test_ZERO_002() {
        int size = 1000;
        LowerTriangularMatrix instance = new LowerTriangularMatrix(size).ZERO();

        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                assertEquals(0, instance.get(i, j), 0);
            }
        }
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_ZERO_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix A2 = (new LowerTriangularMatrix(3)).ZERO().multiply(A1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ONE">
    @Test
    public void test_ONE_001() {
        LowerTriangularMatrix instance = new LowerTriangularMatrix(2).ONE();

        assertEquals(1, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(1, instance.get(2, 2), 0);
    }

    @Test
    public void test_ONE_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new LowerTriangularMatrix(2).ONE();

        Matrix instance1 = A1.multiply(I);
        assertEquals(A1, instance1);

        Matrix instance2 = I.multiply(A1);
        assertEquals(A1, instance2);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_ONE_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new LowerTriangularMatrix(3).ONE();

        Matrix instance1 = A1.multiply(I);
    }
    //</editor-fold>
}
