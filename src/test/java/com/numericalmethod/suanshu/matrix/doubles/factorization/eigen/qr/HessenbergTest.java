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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.Hessenberg.Deflation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HessenbergTest {

    //<editor-fold defaultstate="collapsed" desc="tests for isHessenberg of Hessenberg">
    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {0, 12, 13, 14, 15},
                    {0, 0, 18, 19, 20},
                    {0, 0, 0, 24, 25}
                });

        assertTrue(Hessenberg.isHessenberg(H, 0));
    }

    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {0, 0, 0, 0, 0},
                    {0, 0, 18, 19, 20},
                    {0, 0, 0, 24, 25}
                });

        assertTrue(Hessenberg.isHessenberg(H, 0));
    }

    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0030() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {6, 7, 8, 9, 10},
                    {0, 0, 0, 0, 0},
                    {0, 0, 18, 19, 20},
                    {0, 0, 0, 24, 25}
                });

        assertTrue(Hessenberg.isHessenberg(H, 0));
    }

    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0040() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {10000, 12, 13, 14, 15},
                    {0, 0, 18, 19, 20},
                    {0, 0, 0, 24, 25}
                });

        assertFalse(Hessenberg.isHessenberg(H, 0));
    }

    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0050() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {10000, 0, 0, 0, 0}
                });

        assertFalse(Hessenberg.isHessenberg(H, 0));
    }

    /**
     * Test of isHessenberg method, of class Hessenberg.
     */
    @Test
    public void testIsHessenberg_0060() {
        Matrix H = new DenseMatrix(new double[][]{
                    {10000, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        assertTrue(Hessenberg.isHessenberg(H, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for isReducible of Hessenberg">
    @Test
    public void testIsReducible_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 9, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 8, 7}
                });

        assertFalse(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 0, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 8, 7}
                });

        assertTrue(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0030() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {0, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 9, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 8, 7}
                });

        assertTrue(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0040() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 9, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 0, 7}
                });

        assertTrue(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0050() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2},
                    {7, 8}
                });

        assertFalse(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0060() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 8}
                });

        assertTrue(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0070() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        assertTrue(new Hessenberg().isReducible(H, 0));
    }

    @Test
    public void testIsReducible_0080() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0}
                });

        assertFalse(new Hessenberg().isReducible(H, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for backSearch of Hessenberg">
    /**
     * Test of backSearch method, of class QRAlgorithm.
     */
    @Test
    public void testBackSearch_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 9, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 0, 7}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(1, deflation.ul);
        assertEquals(5, deflation.lr);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     */
    @Test
    public void testBackSearch_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 9, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 0.1, 7}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(1, deflation.ul);
        assertEquals(6, deflation.lr);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     */
    @Test
    public void testBackSearch_0030() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 0, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 0, 7}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(1, deflation.ul);
        assertEquals(3, deflation.lr);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     */
    @Test
    public void testBackSearch_0040() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {7, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 1, 1, 2, 3},
                    {0, 0, 0, 0, 5, 6},
                    {0, 0, 0, 0, 0, 7}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(1, deflation.ul);
        assertEquals(4, deflation.lr);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0050() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6},
                    {0, 8, 9, 1, 1, 2},
                    {0, 4, 5, 6, 7, 8},
                    {0, 0, 0, 1, 2, 3},
                    {0, 0, 0, 4, 5, 6},
                    {0, 0, 0, 0, 0, 7}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0060() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0070() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 8}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0080() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 8}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0090() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 0}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0100() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0110() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0120() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 1}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }

    /**
     * Test of backSearch method, of class QRAlgorithm.
     * already in quasi-triangular form
     */
    @Test
    public void testBackSearch_0130() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        Deflation deflation = new Hessenberg().backSearch(H, H.nRows(), 0);
        assertEquals(0, deflation.ul);
        assertEquals(0, deflation.lr);
        assertTrue(deflation.isQuasiTriangular);
    }
    //</editor-fold>
}
