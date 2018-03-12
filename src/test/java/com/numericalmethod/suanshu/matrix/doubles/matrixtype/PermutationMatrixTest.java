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

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PermutationMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors of DenseMatrix">
    @Test
    public void test_ctor_0010() {
        int dim = 10;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));
    }

    @Test
    public void test_ctor_0020() {
        int data[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        PermutationMatrix instance = new PermutationMatrix(data);
        assertEquals(data.length, instance.nRows());
        assertEquals(data.length, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(data.length, data.length).ONE(), instance, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_0030() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_0040() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{0, 1, 2});
    }

    @Test
    public void test_ctor_0110() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapRow(1, 2);
        instance.swapRow(1, 5);
        instance.swapRow(2, 4);
        instance.swapRow(3, 2);
        instance.swapRow(5, 1);

        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 3, 4, 1, 5});
        assertEquals(expected, instance);

        PermutationMatrix copy = new PermutationMatrix(instance);
        assertEquals(instance, copy);
        assertEquals(instance.sign(), copy.sign(), 0);
        assertEquals(instance.nCols(), copy.nCols());
        assertEquals(instance.nRows(), copy.nRows());
    }

    @Test
    public void test_ctor_0120() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 2);
        instance.swapColumn(1, 5);
        instance.swapColumn(2, 4);
        instance.swapRow(3, 2);
        instance.swapRow(5, 1);

        PermutationMatrix copy = new PermutationMatrix(instance);
        assertEquals(instance, copy);
        assertEquals(instance.sign(), copy.sign(), 0);
        assertEquals(instance.nCols(), copy.nCols());
        assertEquals(instance.nRows(), copy.nRows());
    }

    @Test
    public void test_ctor_0130() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapRow(1, 2);
        instance.swapRow(1, 5);
        instance.swapRow(2, 4);
        instance.swapRow(3, 2);

        PermutationMatrix expected = new PermutationMatrix(new int[]{5, 3, 4, 1, 2});
        assertEquals(expected, instance);

        PermutationMatrix copy = new PermutationMatrix(instance);
        assertEquals(instance, copy);
        assertEquals(instance.sign(), copy.sign(), 0);
        assertEquals(instance.nCols(), copy.nCols());
        assertEquals(instance.nRows(), copy.nRows());
    }

    @Test
    public void test_ctor_0140() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 2);
        instance.swapColumn(1, 5);
        instance.swapRow(2, 4);
        instance.swapRow(3, 2);

        PermutationMatrix copy = new PermutationMatrix(instance);
        assertEquals(instance, copy);
        assertEquals(instance.sign(), copy.sign(), 0);
        assertEquals(instance.nCols(), copy.nCols());
        assertEquals(instance.nRows(), copy.nRows());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for row and column">
    /**
     * Test of row and col methods, of class DenseMatrix.
     */
    @Test
    public void testRowCol_001() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 1, 0, 0, 0}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{0, 0, 1, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 1, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 1}), instance.getRow(5));
    }

    /**
     * Test of row and col methods, of class DenseMatrix.
     */
    @Test
    public void testRowCol_002() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());
        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{0, 1, 0, 0, 0}), instance.getColumn(2));
        assertEquals(new DenseVector(new double[]{0, 0, 1, 0, 0}), instance.getColumn(3));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 1, 0}), instance.getColumn(4));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 1}), instance.getColumn(5));
    }

    /**
     * Test of row and col methods, of class DenseMatrix.
     */
    @Test
    public void testRowCol_003() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        assertEquals(5, instance.nRows());
        assertEquals(5, instance.nCols());

        instance.swapRow(1, 3);
        instance.swapColumn(2, 4);
        instance.swapRow(2, 5);
        instance.swapColumn(3, 4);

        assertEquals(new DenseVector(new double[]{0, 0, 0, 1, 0}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{0, 0, 0, 0, 1}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0}), instance.getRow(3));
        assertEquals(new DenseVector(new double[]{0, 1, 0, 0, 0}), instance.getRow(4));
        assertEquals(new DenseVector(new double[]{0, 0, 1, 0, 0}), instance.getRow(5));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for move2End">
    /**
     * Test of moveRow2End methods, of class DenseMatrix.
     */
    @Test
    public void testMoveRow2End_001() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveRow2End(1);
        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 3, 4, 5, 1});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveRow2End methods, of class DenseMatrix.
     */
    @Test
    public void testMoveRow2End_002() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveRow2End(1);
        instance.moveRow2End(3);
        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 3, 5, 1, 4});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveRow2End methods, of class DenseMatrix.
     */
    @Test
    public void testMoveRow2End_003() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveRow2End(1);
        instance.moveRow2End(3);
        instance.moveRow2End(5);
        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 3, 5, 1, 4});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveRow2End methods, of class DenseMatrix.
     */
    @Test
    public void testMoveRow2End_004() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1});
        instance.moveRow2End(1);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveColumn2End methods, of class DenseMatrix.
     */
    @Test
    public void testmoveColumn2End_001() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveColumn2End(5);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveColumn2End methods, of class DenseMatrix.
     */
    @Test
    public void testmoveColumn2End_002() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveColumn2End(5);
        instance.moveColumn2End(2);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1, 5, 2, 3, 4});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveColumn2End methods, of class DenseMatrix.
     */
    @Test
    public void testmoveColumn2End_003() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveColumn2End(5);
        instance.moveColumn2End(2);
        instance.moveColumn2End(2);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1, 4, 5, 2, 3});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveColumn2End methods, of class DenseMatrix.
     */
    @Test
    public void testmoveColumn2End_004() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1, 2, 3, 4, 5});
        instance.moveColumn2End(5);
        instance.moveColumn2End(2);
        instance.moveColumn2End(2);
        instance.moveColumn2End(4);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1, 5, 4, 2, 3});
        assertEquals(expected, instance);
    }

    /**
     * Test of moveRow2End methods, of class DenseMatrix.
     */
    @Test
    public void testmoveColumn2End_005() {
        PermutationMatrix instance = new PermutationMatrix(new int[]{1});
        instance.moveColumn2End(1);
        PermutationMatrix expected = new PermutationMatrix(new int[]{1});
        assertEquals(expected, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for swap of DenseMatrix">
    @Test
    public void test_swap_001() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapRow(1, 2);
        instance.swapRow(1, 5);
        instance.swapRow(2, 4);
        instance.swapRow(3, 2);
        instance.swapRow(5, 1);

        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 3, 4, 1, 5});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_002() {
        int dim = 3;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapRow(1, 3);
        instance.swapRow(3, 2);
        instance.swapRow(3, 1);

        instance.swapRow(1, 1);
        instance.swapRow(2, 2);
        instance.swapRow(3, 3);

        PermutationMatrix expected = new PermutationMatrix(new int[]{2, 1, 3});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_003() {
        int dim = 3;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 3);

        PermutationMatrix expected = new PermutationMatrix(new int[]{3, 2, 1});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_004() {
        int dim = 3;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 3);
        instance.swapColumn(2, 3);
        instance.swapColumn(3, 1);
        instance.swapColumn(2, 1);

        PermutationMatrix expected = new PermutationMatrix(new int[]{1, 2, 3});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_005() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 3);
        instance.swapColumn(2, 4);
        instance.swapColumn(3, 5);
        instance.swapColumn(2, 4);
        instance.swapColumn(4, 5);

        PermutationMatrix expected = new PermutationMatrix(new int[]{4, 2, 1, 5, 3});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_006() {
        int dim = 3;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 3);
        instance.swapRow(1, 3);
        instance.swapColumn(3, 1);
        instance.swapRow(2, 1);
        instance.swapColumn(3, 2);
        instance.swapRow(3, 2);

        PermutationMatrix expected = new PermutationMatrix(new int[]{3, 1, 2});
        assertEquals(expected, instance);
    }

    @Test
    public void test_swap_007() {
        int dim = 5;
        PermutationMatrix instance = new PermutationMatrix(dim);
        assertEquals(dim, instance.nRows());
        assertEquals(dim, instance.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), instance, 0));

        instance.swapColumn(1, 3);
        instance.swapColumn(2, 4);
        instance.swapRow(3, 5);
        instance.swapRow(2, 4);
        instance.swapColumn(2, 5);
        instance.swapColumn(1, 3);
        instance.swapRow(1, 5);
        instance.swapRow(3, 4);
        instance.swapColumn(2, 1);
        instance.swapRow(1, 2);

        PermutationMatrix expected = new PermutationMatrix(new int[]{5, 3, 4, 1, 2});
        assertEquals(expected, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for multiply of DenseMatrix">
    /**
     * Test of swap methods, of class PermutationMatrix.
     * Left muliplication with P swap rows.
     */
    @Test
    public void test_multiply_0010() {
        int dim = 3;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapRow(1, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Matrix instance = P.multiply(A1);


        Matrix expected = new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {4, 5, 6},
                    {1, 2, 3}
                });
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Right muliplication with P swap columns.
     */
    @Test
    public void test_multiply_0020() {
        int dim = 3;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));


        P.swapColumn(1, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Matrix instance = A1.multiply(P);//not efficient; requires full multiplication

        Matrix expected = new DenseMatrix(new double[][]{
                    {3, 2, 1},
                    {6, 5, 4},
                    {9, 8, 7}
                });
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Left muliplication with P swap rows.
     */
    @Test
    public void test_multiply_0030() {
        int dim = 5;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapRow(1, 3);
        P.swapRow(2, 4);
        P.swapRow(5, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Matrix instance = P.multiply(A1);

        Matrix expected = new DenseMatrix(new double[][]{
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25},
                    {6, 7, 8, 9, 10},
                    {1, 2, 3, 4, 5}
                });
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Left muliplication with P swap rows.
     */
    @Test
    public void test_multiply_0035() {
        int dim = 5;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapRow(1, 3);
        P.swapRow(2, 4);
        P.swapRow(5, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4},
                    {6, 7, 8, 9},
                    {11, 12, 13, 14},
                    {16, 17, 18, 19},
                    {21, 22, 23, 24}
                });
        Matrix instance = P.multiply(A1);

        Matrix expected = new DenseMatrix(new double[][]{
                    {11, 12, 13, 14},
                    {16, 17, 18, 19},
                    {21, 22, 23, 24},
                    {6, 7, 8, 9},
                    {1, 2, 3, 4}
                });
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Left muliplication with P swap rows.
     */
    @Test
    public void test_multiply_0040() {
        int dim = 5;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapColumn(1, 3);
        P.swapColumn(2, 4);
        P.swapColumn(5, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Matrix instance = A1.multiply(P);//not efficient; requires full multiplication

        Matrix expected = new DenseMatrix(new double[][]{
                    {3, 4, 5, 2, 1},
                    {8, 9, 10, 7, 6},
                    {13, 14, 15, 12, 11},
                    {18, 19, 20, 17, 16},
                    {23, 24, 25, 22, 21}
                });
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Right muliplication with P swap columns.
     */
    @Test
    public void test_multiply_0050() {
        int dim = 3;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapRow(1, 3);

        Vector v1 = new DenseVector(new double[]{1, 2, 3});
        Vector instance = P.multiply(v1);

        Vector expected = new DenseVector(new double[]{3, 2, 1});
        assertEquals(expected, instance);
    }

    /**
     * Test of swap methods, of class PermutationMatrix.
     * Right muliplication with P swap columns.
     */
    @Test
    public void test_multiply_0060() {
        int dim = 10;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapRow(1, 3);
        P.swapRow(2, 5);
        P.swapRow(4, 2);
        P.swapRow(7, 9);

        Vector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        Vector instance = P.multiply(v1);

        Vector expected = new DenseVector(new double[]{3, 4, 1, 5, 2, 6, 9, 8, 7, 10});
        assertEquals(expected, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for rightMultiply of DenseMatrix">
    /**
     * Test of swap methods, of class PermutationMatrix.
     * Right Left muliplication with P swap columns.
     *
     */
    @Test
    public void testRightTimes_0010() {
        int dim = 3;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapColumn(1, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Matrix instance = A1.multiply(P);


        Matrix expected = new DenseMatrix(new double[][]{
                    {3, 2, 1},
                    {6, 5, 4},
                    {9, 8, 7}
                });
        assertEquals(expected, instance);
    }

    @Test
    public void testRightTimes_0020() {
        int dim = 5;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapColumn(1, 3);
        P.swapColumn(2, 4);
        P.swapColumn(5, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Matrix instance = P.rightMultiply(A1);

        Matrix expected = new DenseMatrix(new double[][]{
                    {3, 4, 5, 2, 1},
                    {8, 9, 10, 7, 6},
                    {13, 14, 15, 12, 11},
                    {18, 19, 20, 17, 16},
                    {23, 24, 25, 22, 21}
                });
        assertEquals(expected, instance);
    }

    @Test
    public void testRightTimes_0030() {
        int dim = 5;
        PermutationMatrix P = new PermutationMatrix(dim);
        assertEquals(dim, P.nRows());
        assertEquals(dim, P.nCols());
        assertTrue(AreMatrices.equal(new DenseMatrix(dim, dim).ONE(), P, 0));

        P.swapColumn(1, 3);
        P.swapColumn(2, 4);
        P.swapColumn(5, 3);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });
        Matrix instance = P.rightMultiply(A1);

        Matrix expected = new DenseMatrix(new double[][]{
                    {3, 4, 5, 2, 1},
                    {8, 9, 10, 7, 6},
                    {13, 14, 15, 12, 11}
                });
        assertEquals(expected, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose">
    /**
     * Permutation matrices are orthogonal matrices.
     */
    @Test
    public void test_t_001() {
        PermutationMatrix A1 = new PermutationMatrix(5);
        PermutationMatrix A2 = A1.t();

        assertEquals(5, A2.nRows());
        assertEquals(5, A2.nCols());

        Matrix A3 = A1.multiply(A2);
        assertEquals(A3, A3.ONE());

        Matrix m4 = A2.multiply(A1);
        assertEquals(m4, m4.ONE());
    }

    /**
     * Permutation matrices are orthogonal matrices.
     */
    @Test
    public void test_t_002() {
        PermutationMatrix A1 = new PermutationMatrix(5);
        A1.swapColumn(1, 2);
        A1.swapRow(3, 4);
        A1.swapColumn(2, 5);
        A1.swapRow(1, 2);
        A1.swapColumn(2, 1);

        assertEquals(A1, new PermutationMatrix(new int[]{2, 5, 4, 3, 1}));

        PermutationMatrix A2 = A1.t();
        assertEquals(5, A2.nRows());
        assertEquals(5, A2.nCols());

        assertEquals(A2, new PermutationMatrix(new int[]{5, 1, 4, 3, 2}));

        Matrix A3 = A1.multiply(A2);
        assertEquals(A3, A3.ONE());

        Matrix m4 = A2.multiply(A1);
        assertEquals(m4, m4.ONE());
    }

    /**
     * Permutation matrices are orthogonal matrices.
     * P.multiply(P.t()) == P.t().multiply(P) == P.toDense().ONE()
     *
     */
    @Test
    public void test_t_004() {
        PermutationMatrix A1 = new PermutationMatrix(5);
        Matrix ONE = A1.ONE();

        assertTrue(AreMatrices.equal(A1.multiply(A1.t()), ONE, 0));
        assertTrue(AreMatrices.equal(A1.t().multiply(A1), ONE, 0));
    }
    //</editor-fold>
}
