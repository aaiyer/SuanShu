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
public class UpperTriangularMatrixTest {

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

    //<editor-fold defaultstate="collapsed" desc="tests for constructors of UpperTriangularMatrix">
    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     * Creating a DenseMatrix from a 2D array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpperTriangularMatrix_0010() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10}});
    }

    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     * Creating a UpperTriangularMatrix from a DenseMatrix.
     */
    @Test
    public void testUpperTriangularMatrix_0020() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(15, instance.get(5, 5), 0));
        assertEquals(0, compare(0, instance.get(5, 1), 0));

        Matrix A1 = instance.toDense();
        UpperTriangularMatrix A2 = new UpperTriangularMatrix(A1);
        assertEquals(instance, A2);
    }

    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpperTriangularMatrix_0025() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 0},
                    {10, 11, 12, 0, 0},
                    {15, 0, 0, 0, 999}});

        UpperTriangularMatrix A2 = new UpperTriangularMatrix(A1);
    }

    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     */
//    @Test(expected = IllegalArgumentException.class)
    public void testUpperTriangularMatrix_0027() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 0},
                    {10, 11, 12, 0, 0},
                    {13, 14, 0, 0, 0},
                    {15, 0, 0, 0, 999}});

        UpperTriangularMatrix A2 = new UpperTriangularMatrix(A1);
    }

    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     */
    @Test
    public void testUpperTriangularMatrix_0028() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 0},
                    {10, 11, 12, 0, 0},
                    {13, 14, 0, 0, 0},
                    {15, 0, 0, 0, 999}});

        UpperTriangularMatrix A2 = new UpperTriangularMatrix(A1);

        assertTrue(AreMatrices.equal(A2,
                                     new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {7, 8, 9, 0},
                    {12, 0, 0},
                    {0, 0},
                    {999}}),
                                     0));
    }

    /**
     * Test of UpperTriangularMatrix method, of class UpperTriangularMatrix.
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void testUpperTriangularMatrix_0030() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{0}});
        assertEquals(1, instance.nRows());
        assertEquals(1, instance.nCols());
        assertEquals(0, compare(0, instance.get(1, 1), 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for add, minus, scaled methods of class UpperTriangularMatrix">
    @Test
    public void test_add_minus_scaled_001() {
        Matrix instance1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        Matrix instance2 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        Matrix instance3 = instance1.add(instance2);
        Matrix instance4 = instance1.scaled(2.0);
        assertEquals(instance3, instance4);
        assertThat(instance3, CoreMatchers.instanceOf(UpperTriangularMatrix.class));
        assertThat(instance4, CoreMatchers.instanceOf(UpperTriangularMatrix.class));


        Matrix instance5 = instance3.minus(instance2);
        assertEquals(instance1, instance5);
        assertThat(instance5, CoreMatchers.instanceOf(UpperTriangularMatrix.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for multiply method">
    @Test
    public void test_multiply_0010() {
        Matrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        Matrix A2 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        Matrix A3 = A1.multiply(A2);

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 14, 47, 105, 190},
                    {0, 36, 112, 229, 385},
                    {0, 0, 100, 253, 454},
                    {0, 0, 0, 169, 392},
                    {0, 0, 0, 0, 225}
                });

        assertTrue(AreMatrices.equal(A3, expected, 0));
        assertThat(A3, CoreMatchers.instanceOf(UpperTriangularMatrix.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for opposite and scaled">
    /**
     * Test of opposite method, of class UpperTriangularMatrix.
     */
    @Test
    public void testOpposite() {
        UpperTriangularMatrix instance1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});
        Matrix instance2 = instance1.opposite();
        Matrix instance3 = instance1.scaled(-1.0);
        assertEquals(instance2, instance3);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for row and column">
    /**
     * Test of row method, of class UpperTriangularMatrix.
     */
    @Test
    public void testRow_001() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 6, 7, 8, 9}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 0, 10, 11, 12}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 13, 14}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 15}), instance.getRow(5));
    }

    /**
     * Test of row method, of class UpperTriangularMatrix.
     */
    @Test
    public void testRow_002() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getRow(1));
    }

    /**
     * Test of row method, of class UpperTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRow_003() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        instance.getRow(2);
    }

    /**
     * Test of row method, of class UpperTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRow_004() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        instance.getRow(0);
    }

    /**
     * Test of column method, of class UpperTriangularMatrix.
     */
    @Test
    public void testColumn_001() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});

        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 6, 0, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{3, 7, 10, 0, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{4, 8, 11, 13, 0}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{5, 9, 12, 14, 15}), instance.getColumn(5));
    }

    /**
     * Test of column method, of class UpperTriangularMatrix.
     */
    @Test
    public void testColumn_002() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getRow(1));
    }

    /**
     * Test of column method, of class UpperTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumn_003() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        instance.getRow(2);
    }

    /**
     * Test of column method, of class UpperTriangularMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumn_004() {
        UpperTriangularMatrix instance = new UpperTriangularMatrix(new double[][]{{1}});
        instance.getRow(0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for toDense">
    /**
     * Test of toDense method, of class UpperTriangularMatrix.
     */
    @Test
    public void testDense_001() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {0, 6, 7, 8, 9},
                    {0, 0, 10, 11, 12},
                    {0, 0, 0, 13, 14},
                    {0, 0, 0, 0, 15}});

        assertEquals(A2, instance);
    }

    /**
     * Test of toDense method, of class UpperTriangularMatrix.
     */
    @Test
    public void testDense_002() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2},
                    {3}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 3}});
        assertEquals(A2, instance);
    }

    /**
     * Test of toDense method, of class UpperTriangularMatrix.
     */
    @Test
    public void testDense_003() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{{0}});
        Matrix instance = A1.toDense();
        Matrix A2 = new DenseMatrix(new double[][]{{0}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose">
    /**
     * Test of t method, of class LowerTriangularMatrix.
     */
    @Test
    public void testT_001() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {10, 11, 12},
                    {13, 14},
                    {15}});
        LowerTriangularMatrix instance = A1.t();

        LowerTriangularMatrix A2 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 6},
                    {3, 7, 10},
                    {4, 8, 11, 13},
                    {5, 9, 12, 14, 15}});

        assertEquals(A2, instance);
    }

    /**
     * Test of t method, of class LowerTriangularMatrix.
     */
    @Test
    public void testT_002() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2},
                    {3}});
        LowerTriangularMatrix instance = A1.t();

        LowerTriangularMatrix A2 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3}});

        assertEquals(A2, instance);
    }

    /**
     * Test of t method, of class LowerTriangularMatrix.
     */
    @Test
    public void testT_003() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{{1}});
        LowerTriangularMatrix instance = A1.t();
        LowerTriangularMatrix A2 = new LowerTriangularMatrix(new double[][]{{1}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ZERO">
    @Test
    public void test_ZERO_001() {
        Matrix instance = new UpperTriangularMatrix(2).ZERO();

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
        Matrix instance = new UpperTriangularMatrix(size).ZERO();

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
        Matrix A2 = (new UpperTriangularMatrix(3)).ZERO().multiply(A1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ONE">
    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_001() {
        Matrix instance = new UpperTriangularMatrix(2).ONE();

        assertEquals(1, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(1, instance.get(2, 2), 0);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new UpperTriangularMatrix(2).ONE();

        Matrix instance1 = A1.multiply(I);
        assertEquals(A1, instance1);

        Matrix instance2 = I.multiply(A1);
        assertEquals(A1, instance2);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test(expected = MatrixMismatchException.class)
    public void testONE_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new UpperTriangularMatrix(3).ONE();

        Matrix instance1 = A1.multiply(I);
    }
    //</editor-fold>
}
