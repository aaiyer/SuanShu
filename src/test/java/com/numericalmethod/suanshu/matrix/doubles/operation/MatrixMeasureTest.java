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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class MatrixMeasureTest {

    //<editor-fold defaultstate="collapsed" desc="tests for rank">
    @Test
    public void test_rank_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });

        int result = MatrixMeasure.rank(A, 1e-15);
        assertEquals(2, result);
    }

    @Test
    public void test_rank_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(2, result);
    }

    @Test
    public void test_rank_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 4, 1, 3},
                    {-1, -2, 1, 0},
                    {0, 0, 2, 2},
                    {3, 6, 2, 5}
                });

        int result = MatrixMeasure.rank(A, 1e-15);
        assertEquals(2, result);
    }

    @Test
    public void test_rank_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 4, 1, 3},
                    {-1, -2, 1, 0},
                    {0, 0, 2, 2},
                    {3, 6, 2, 5}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(2, result);
    }

    @Test
    public void test_rank_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, -1, 3},
                    {1, 0, 1},
                    {0, 2, -1},
                    {1, 1, 4}
                });

        int result = MatrixMeasure.rank(A, 1e-15);
        assertEquals(3, result);
    }

    @Test
    public void test_rank_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, -1, 3},
                    {1, 0, 1},
                    {0, 2, -1},
                    {1, 1, 4}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(3, result);
    }

    @Test
    public void test_rank_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -1, 1, -1},
                    {-1, 1, -1, 1},
                    {1, -1, 1, -1},
                    {-1, 1, -1, 1}
                });

        int result = MatrixMeasure.rank(A, 1e-15);
        assertEquals(1, result);
    }

    @Test
    public void test_rank_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -1, 1, -1},
                    {-1, 1, -1, 1},
                    {1, -1, 1, -1},
                    {-1, 1, -1, 1}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(1, result);
    }

    @Test
    public void test_rank_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        int result = MatrixMeasure.rank(A, 1e-15);
        assertEquals(0, result);
    }

    @Test
    public void test_rank_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(0, result);
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     * 
     * Test for linear dependent rows/columns.
     */
    @Test
    public void test_rank_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {1, 2, 3},
                    {1, 2, 3}
                });

        int result = MatrixMeasure.rank(A, 1e-14);//choosing the epsilon is very important here
        assertEquals(1, result);
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     *
     * Test for linear dependent rows/columns.
     */
    @Test
    public void test_rank_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {1, 2, 3},
                    {1, 2, 3}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(1, result);
    }

    @Test
    public void test_rank_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 10}
                });

        int result = MatrixMeasure.rank(A, 1e-15);//choosing the epsilon is very important here
        assertEquals(3, result);
    }

    @Test
    public void test_rank_0075() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 10}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(3, result);
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     * 
     * Test for small numbers.
     */
    @Test
    public void test_rank_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 10e-100}
                });

        int result = MatrixMeasure.rank(A, 1e-115);//choosing the epsilon is very important here
        assertEquals(3, result);
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     *
     * Test for small numbers.
     */
    @Test
    public void test_rank_0085() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 10e-100}
                });

        int result = MatrixMeasure.rank(A);
        assertEquals(3, result);
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     *
     * Test for small numbers.
     */
    @Test
    public void test_rank_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 10e-100}
                });

        int result = MatrixMeasure.rank(A, 1e-15);//choosing the epsilon is very important here
        assertFalse(3 == result);//the correct answer is 3
    }

    /**
     * Test of rank method, of class MatrixMeasure.
     *
     * Test for small numbers.
     */
    @Test
    public void test_rank_0095() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {4e-100, 5e-100, 6e-100},
                    {7e-100, 8e-100, 10e-100}
                });

        int result = MatrixMeasure.rank(A);
        assertTrue(3 == result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for det">
    @Test
    public void testDet_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(-23d, result, 1e-15);
    }

    @Test
    public void testDet_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(0d, result, 1e-15);
    }

    @Test
    public void testDet_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {5, 2, 3, 4},
                    {5, 6, 7, 4},
                    {1, 3, 2, 3},
                    {1, 7, 6, 2}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(24d, result, 1e-13);
    }

    @Test
    public void testDet_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {10, 2, 3, 4, 5},
                    {6, 7, 88, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 79, 20},
                    {21, 22, 23, 24, 95}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(0d, (-31317000d - result) / -31317000d, 1e-15);
    }

    @Test
    public void testDet_0050() {
        Matrix A = new DenseMatrix(new double[][]{{0}});

        double result = MatrixMeasure.det(A);
        assertEquals(0d, result, 1e-15);
    }

    @Test
    public void testDet_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(0d, result, 1e-15);
    }

    @Test
    public void testDet_0070() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(new double[][]{
                    {3},
                    {7, 4},
                    {7.6, 4.6, 9.1},
                    {99.966, 0.0064, 901, 12.92},
                    {99.966, 0.0064, 901, 12.92, 1.27},
                    {99.966, 0.0064, 901, 12.92, 1.27, 269.1}
                });

        double result = MatrixMeasure.det(A);
        assertEquals(482172.6480480004, result, 1e-9);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for other measures">
    @Test
    public void test_measure_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2}
                });

        double result = MatrixMeasure.Frobenius(A);
        assertEquals(Math.sqrt(5.), result, 1e-15);
    }
    //</editor-fold>
}
