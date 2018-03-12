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
package com.numericalmethod.suanshu.matrix.doubles;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class IsTest {

    //<editor-fold defaultstate="collapsed" desc="tests with row echelon form">
    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {0, 2, -1},
                    {0, 0, 6}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 0, 6, 6},
                    {0, 0, 0, 9}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 0, 6, 6},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 0, 0, 6},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 0, -1, 3},
                    {0, 0, 0, 6},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 1, 2},
                    {0, 0, -1, 3},
                    {0, 0, 0, 6},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                });

        assertTrue(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 1, 6, 6},
                    {0, 0, 0, 9}
                });

        assertFalse(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 0, 6, 6},
                    {0, 99, 0, 9}
                });

        assertFalse(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 1, 2},
                    {0, 2, -1, 3},
                    {0, 0, 6, 6},
                    {0, 0, 0, 9}
                });

        assertFalse(IsMatrix.rowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testRowEchelonForm_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 2, -1, 3},
                    {0, 0, 6, 6},
                    {0, 0, 0, 9}
                });

        assertFalse(IsMatrix.rowEchelonForm(A, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests with reduced row echelon form">
    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {0, 2, -1},
                    {0, 0, 6}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    @Test
    public void testReducedRowEchelonForm_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 5, 0},
                    {0, 0, 0, 1},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 1, 0, 11, 0},
                    {0, 1, -1, 0, 22, 0},
                    {0, 0, 0, 1, 33, 0},
                    {0, 0, 0, 0, 0, 1}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 1, 0},
                    {0, 1, -1, 0},
                    {0, 0, 0, 1},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 1, 2},
                    {0, 0, 1, 3},
                    {0, 0, 0, 6},
                    {0, 0, 0, 0}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 0, 2, 0},
                    {0, 0, 1, 3, 0},
                    {0, 0, 0, 0, 1},
                    {0, 0, 0, 0, 0}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                });

        assertTrue(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1, 1, 2},
                    {0, 1, -1, 3},
                    {0, 1, 6, 6},
                    {0, 0, 0, 1}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1, 1, 2},
                    {0, 1, -1, 3},
                    {0, 0, 1, 6},
                    {0, 1, 0, 9}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 1, 2},
                    {0, 1, -1, 3},
                    {0, 0, 1, 6},
                    {0, 0, 0, 1}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }

    /**
     * Test of rowEchelonForm method, of class IsMatrix.
     */
    @Test
    public void testReducedRowEchelonForm_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 1, -1, 3},
                    {0, 0, 1, 6},
                    {0, 0, 0, 1}
                });

        assertFalse(IsMatrix.reducedRowEchelonForm(A, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests with upper bidiagonal form">
    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {0, 2, -1},
                    {0, 0, 6}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {0, 2, 0},
                    {0, 0, 6}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 4}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 4}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 999},
                    {0, 2, -1},
                    {0, 0, 6}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {0, 2, -1},
                    {999, 0, 6}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {888, 2, -1},
                    {999, 777, 6}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {888, 2, -1},
                    {0, 777, 6}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1},
                    {999, 2}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0130() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {0, 2, -1},
                    {0, 0, 6},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {0, 2, -1},
                    {0, 0, 6},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0150() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0},
                    {0, 2, -1},
                    {0, 0, 6},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 999}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0160() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0, 0, 0, 0},
                    {0, 2, -1, 0, 0, 0},
                    {0, 0, 6, 0, 0, 0}
                });

        assertTrue(IsMatrix.upperBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_upperBidiagonal_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 0, 0, 0, 0},
                    {0, 2, -1, 0, 0, 0},
                    {0, 0, 6, 0, 0, 999}
                });

        assertFalse(IsMatrix.upperBidiagonal(A, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests with lower bidiagonal form">
    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {1, 2, 0},
                    {0, -1, 6}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {0, 2, 0},
                    {0, 0, 6}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {2, 4}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 4}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {1, 2, 0},
                    {999, -1, 6}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 999},
                    {0, 2, -1},
                    {0, 0, 6}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 888, 999},
                    {1, 2, 777},
                    {0, -1, 6}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 888, 0},
                    {1, 2, 777},
                    {0, -1, 6}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 999},
                    {1, 2}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0130() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {1, 2, 0},
                    {0, -1, 6},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {1, 2, 0},
                    {0, -1, 6},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of lowerBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0150() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0},
                    {1, 2, 0},
                    {0, -1, 6},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 999}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0160() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0, 0, 0, 0},
                    {1, 2, 0, 0, 0, 0},
                    {0, -1, 6, 0, 0, 0}
                });

        assertTrue(IsMatrix.lowerBidiagonal(A, 0));
    }

    /**
     * Test of upperBidiagonal method, of class IsMatrix.
     */
    @Test
    public void test_lowerBidiagonal_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 0, 0, 0, 0, 0},
                    {1, 2, 0, 0, 0, 0},
                    {0, -1, 6, 0, 0, 999}
                });

        assertFalse(IsMatrix.lowerBidiagonal(A, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for symmetric positive definite">
    /**
     * Test of symmetricPositiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testSymmetricPositiveDefinite_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {1, 2, 1},
                    {1, 1, 2}
                });

        assertTrue(IsMatrix.symmetricPositiveDefinite(A));
    }

    /**
     * Test of symmetricPositiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testSymmetricPositiveDefinite_0020() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}
                });

        assertTrue(IsMatrix.symmetricPositiveDefinite(L.multiply(L.t())));
    }

    /**
     * Test of symmetricPositiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testSymmetricPositiveDefinite_0030() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {10.2},
                    {2.96, 3.36},
                    {456, 59.3, 602.369},
                    {456, 59.3, 602.369, 45.1}
                });

        assertTrue(IsMatrix.symmetricPositiveDefinite(L.multiply(L.t())));
    }

    /**
     * Test of symmetricPositiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testSymmetricPositiveDefinite_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 5, 6},
                    {3, 6, 9}
                });

        assertFalse(IsMatrix.symmetricPositiveDefinite(A));
    }

    /**
     * Test of symmetricPositiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testSymmetricPositiveDefinite_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4.52},
                    {2, 5, 6, 9.35},
                    {3, 6, 7, 0.59},
                    {4.52, 9.35, 0.59, 0.59}
                });

        assertFalse(IsMatrix.symmetricPositiveDefinite(A));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for positive definite">
    /**
     * Test of positiveDefinite method, of class IsMatrix.
     */
    @Test
    public void testPositiveDefinite_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {110, 0, 0, -220},
                    {0, 6, -60, 0},
                    {0, -60, 600, 0},
                    {-220, 0, 0, 440}
                });

        assertFalse(IsMatrix.positiveDefinite(A));
    }
    //</editor-fold>
}
