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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class SymmetricMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors of SymmetricMatrix">
    /**
     * Test of SymmetricMatrix method, of class SymmetricMatrix.
     * Creating a DenseMatrix from a 2D array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSymmetricMatrix_001() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10}});
    }

    /**
     * Test of SymmetricMatrix method, of class SymmetricMatrix.
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void testSymmetricMatrix_002() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
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
        assertEquals(0, compare(11, instance.get(1, 5), 0));
        assertEquals(0, compare(8, instance.get(2, 4), 0));
        assertEquals(0, compare(8, instance.get(4, 2), 0));
        assertEquals(0, compare(9, instance.get(3, 4), 0));
        assertEquals(0, compare(9, instance.get(4, 3), 0));
        assertEquals(0, compare(14, instance.get(5, 4), 0));
        assertEquals(0, compare(14, instance.get(4, 5), 0));

//        System.out.println(String.format("%s", instance.toString(), 0));
    }

    /**
     * Test of SymmetricMatrix method, of class SymmetricMatrix.
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void testSymmetricMatrix_003() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{{0}});
        assertEquals(1, instance.nRows());
        assertEquals(1, instance.nCols());
        assertEquals(0, compare(0, instance.get(1, 1), 0));

//        System.out.println(String.format("%s", instance.toString(), 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for add, minus, multiply, scaled methods of class SymmetricMatrix">
    /**
     * Test of add, minus, scaled method, of class SymmetricMatrix.
     */
    @Test
    public void testAdd_Minus_Scaled_001() {
        Matrix instance1 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix instance2 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        Matrix instance3 = instance1.add(instance2);
        Matrix instance4 = instance1.scaled(2.0);
        assertEquals(instance3, instance4);

        Matrix instance5 = instance3.minus(instance2);
        assertEquals(instance1, instance5);
    }

    @Test
    public void test_multiply_0010() {
        Matrix A = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });

        Matrix B = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {400, 425, 450, 475, 500},
                    {455, 485, 515, 545, 575},
                    {517, 554, 591, 628, 665},
                    {608, 656, 704, 752, 800},
                    {765, 830, 895, 960, 1025}
                });

        Matrix result = A.multiply(B);
        assertTrue(AreMatrices.equal(expected, result, 0));
    }

    @Test
    public void test_multiply_0020() {
        Matrix A = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });

        Matrix B = new SymmetricMatrix(new double[][]{
                    {1},
                    {6, 7},
                    {11, 12, 13},
                    {16, 17, 18, 19},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {400, 429, 466, 519, 600},
                    {455, 493, 543, 613, 715},
                    {517, 570, 643, 740, 873},
                    {608, 684, 792, 936, 1120},
                    {765, 874, 1031, 1240, 1505}
                });

        Matrix result = A.multiply(B);

        assertEquals(expected, result);
    }

    @Test
    public void test_multiply_0030() {
        Matrix A = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });

        Matrix B = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4},
                    {6, 7, 8, 9},
                    {11, 12, 13, 14},
                    {16, 17, 18, 19},
                    {21, 22, 23, 24}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {400, 425, 450, 475},
                    {455, 485, 515, 545},
                    {517, 554, 591, 628},
                    {608, 656, 704, 752},
                    {765, 830, 895, 960}
                });

        Matrix result = A.multiply(B);

        assertEquals(expected, result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for opposite">
    /**
     * Test of opposite method, of class SymmetricMatrix.
     */
    @Test
    public void testOpposite() {
        Matrix instance1 = new SymmetricMatrix(new double[][]{
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
     * Test of row method, of class SymmetricMatrix.
     */
    @Test
    public void testRow_001() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        assertEquals(new DenseVector(new double[]{1, 2, 4, 7, 11}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{2, 3, 5, 8, 12}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{4, 5, 6, 9, 13}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{7, 8, 9, 10, 14}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{11, 12, 13, 14, 15}), instance.getRow(5));
    }

    /**
     * Test of row method, of class SymmetricMatrix.
     */
    @Test
    public void testRow_002() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getRow(1));
    }

    /**
     * Test of row method, of class SymmetricMatrix.
     */
    @Test(expected = MatrixAccessException.class)
    public void testRow_003() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{{1}});
        instance.getRow(2);
    }

    /**
     * Test of row method, of class SymmetricMatrix.
     */
    @Test(expected = MatrixAccessException.class)
    public void testRow_004() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{{1}});
        instance.getRow(0);
    }

    /**
     * Test of column method, of class SymmetricMatrix.
     */
    @Test
    public void testColumn_001() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});

        assertEquals(new DenseVector(new double[]{1, 2, 4, 7, 11}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 3, 5, 8, 12}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{4, 5, 6, 9, 13}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{7, 8, 9, 10, 14}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{11, 12, 13, 14, 15}), instance.getColumn(5));
    }

    /**
     * Test of column method, of class SymmetricMatrix.
     */
    @Test
    public void testColumn_002() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});

        assertEquals(new DenseVector(new double[]{1, 2, 4}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{2, 3, 5}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{4, 5, 6}), instance.getColumn(3));
    }

    /**
     * Test of row method, of class SymmetricMatrix.
     */
    @Test
    public void testColumn_003() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{{1}});
        assertEquals(new DenseVector(new double[]{1}), instance.getColumn(1));
    }

    /**
     * Test of column method, of class SymmetricMatrix.
     */
    @Test(expected = MatrixAccessException.class)
    public void testColumn_004() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});
        instance.getColumn(4);
    }

    /**
     * Test of column method, of class SymmetricMatrix.
     */
    @Test(expected = MatrixAccessException.class)
    public void testColumn_005() {
        SymmetricMatrix instance = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}});
        instance.getColumn(0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for toDense">
    /**
     * Test of toDense method, of class SymmetricMatrix.
     */
    @Test
    public void testDense_001() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 2, 4, 7, 11},
                    {2, 3, 5, 8, 12},
                    {4, 5, 6, 9, 13},
                    {7, 8, 9, 10, 14},
                    {11, 12, 13, 14, 15}});

        assertEquals(A2, instance);
    }

    /**
     * Test of toDense method, of class SymmetricMatrix.
     */
    @Test
    public void testDense_002() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {2, 3}});

        assertEquals(A2, instance);
    }

    /**
     * Test of toDense method, of class SymmetricMatrix.
     */
    @Test
    public void testDense_003() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{{0}});
        Matrix instance = A1.toDense();

        Matrix A2 = new DenseMatrix(new double[][]{{0}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose">
    /**
     * Test of t method, of class SymmetricMatrix.
     */
    @Test
    public void testT_001() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}});
        SymmetricMatrix instance = A1.t();
        assertEquals(A1, instance);
    }

    /**
     * Test of t method, of class SymmetricMatrix.
     */
    @Test
    public void testT_002() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{
                    {1},
                    {2, 3}});
        SymmetricMatrix instance = A1.t();
        assertEquals(A1, instance);
    }

    /**
     * Test of t method, of class SymmetricMatrix.
     */
    @Test
    public void testT_003() {
        SymmetricMatrix A1 = new SymmetricMatrix(new double[][]{{1}});
        SymmetricMatrix instance = A1.t();
        SymmetricMatrix A2 = new SymmetricMatrix(new double[][]{{1}});
        assertEquals(A2, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ZERO">
    /**
     * Test of ZERO method, of class DenseMatrix.
     */
    @Test
    public void testZERO_001() {
        Matrix instance = new SymmetricMatrix(2).ZERO();

        assertEquals(0, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(0, instance.get(2, 2), 0);

//        System.out.println(String.format("%s", instance.toString(), 0));

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});

        Matrix A2 = A1.multiply(instance);
        assertEquals(instance, A2);

        Matrix A3 = instance.multiply(A1);
        assertEquals(instance, A3);
    }

    /**
     * Test of ZERO method, of class DenseMatrix.
     * Check if a double array is initialized to 0 by jvm.
     */
    @Test
    public void testZERO_002() {
        int size = 1000;
        Matrix instance = new SymmetricMatrix(size).ZERO();

        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                assertEquals(0, instance.get(i, j), 0);
            }
        }
    }

    /**
     * Test of ZERO method, of class DenseMatrix.
     */
    @Test(expected = MatrixMismatchException.class)
    public void testZERO_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix A2 = (new SymmetricMatrix(3)).ZERO().multiply(A1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ONE">
    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_001() {
        Matrix instance = new SymmetricMatrix(2).ONE();

        assertEquals(1, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(1, instance.get(2, 2), 0);

//        System.out.println(String.format("%s", instance.toString(), 0));
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new SymmetricMatrix(2).ONE();

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
        Matrix I = new SymmetricMatrix(3).ONE();

        Matrix instance1 = A1.multiply(I);
    }
    //</editor-fold>
}
