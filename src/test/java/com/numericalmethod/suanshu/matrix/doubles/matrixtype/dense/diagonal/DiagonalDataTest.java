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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class DiagonalDataTest {

    //<editor-fold defaultstate="collapsed" desc="tests for constructors">
    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0005() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {2},
                    {1, 4},
                    {3}
                });

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(A, instance.toDense());

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0010() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);

    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0020() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0030() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {0, 0, 0, 0},
                    {1, 4, 7, 10, 13},
                    {0, 0, 0, 0}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0040() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0}
                });

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * BI_DIAGONAL_UPPER
     */
    @Test
    public void test_ctors_0050() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {2},
                    {1, 4}
                });

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(2, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {0, 4}
                });

        assertEquals(A, instance.toDense());

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * BI_DIAGONAL_UPPER
     */
    @Test
    public void test_ctors_0060() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * BI_DIAGONAL_LOWER
     */
    @Test
    public void test_ctors_0070() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * DIAGONAL
     */
    @Test
    public void test_ctors_0080() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    {1, 4, 7, 10, 13}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctors_0090() {
        DiagonalData instance = new DiagonalData(new double[][]{});
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0100() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    null,
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0110() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    null,
                    {1, 4, 7, 10, 13},
                    null
                });

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

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }

    /**
     * Test of constructors, of class DiagonalData.
     * TRI_DIAGONAL
     */
    @Test
    public void test_ctors_0120() {
        DiagonalData instance = new DiagonalData(new double[][]{
                    null,
                    {0, 0, 0, 0, 0},
                    null
                });

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());

        DiagonalData copy = new DiagonalData(instance);
        assertEquals(copy, instance);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set and get methods">
    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0005() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.TRI_DIAGONAL, 2);

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
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0010() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.TRI_DIAGONAL, 5);

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
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0020() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.TRI_DIAGONAL, 5);

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    @Test
    public void test_set_get_0030() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_UPPER, 2);

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
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0040() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_UPPER, 5);

        instance.set(1, 1, 1);
        instance.set(1, 2, 2);
        instance.set(2, 2, 4);
        instance.set(2, 3, 5);
        instance.set(3, 3, 7);
        instance.set(3, 4, 8);
        instance.set(4, 4, 10);
        instance.set(4, 5, 11);
        instance.set(5, 5, 13);

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
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0050() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_UPPER, 5);

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0060() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_LOWER, 2);

        instance.set(1, 1, 1);
        instance.set(2, 1, 3);
        instance.set(2, 2, 4);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(3, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {3, 4}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0070() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_LOWER, 5);

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
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0080() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.BI_DIAGONAL_LOWER, 5);

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0090() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.DIAGONAL, 2);

        instance.set(1, 1, 1);
        instance.set(2, 2, 4);

        assertEquals(0, compare(1, instance.get(1, 1), 0));
        assertEquals(0, compare(0, instance.get(1, 2), 0));
        assertEquals(0, compare(0, instance.get(2, 1), 0));
        assertEquals(0, compare(4, instance.get(2, 2), 0));

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 4}
                });

        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0100() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.DIAGONAL, 5);

        instance.set(1, 1, 1);
        instance.set(1, 2, 0);
        instance.set(2, 2, 4);
        instance.set(2, 3, 0);
        instance.set(3, 3, 7);
        instance.set(3, 4, 0);
        instance.set(4, 4, 10);
        instance.set(4, 5, 0);
        instance.set(5, 5, 13);

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

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test
    public void test_set_get_0110() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.DIAGONAL, 5);

        instance.set(1, 3, 0);

        Matrix A = new DenseMatrix(5, 5).ZERO();
        assertEquals(A, instance.toDense());
    }

    /**
     * Test of set and get methods, of class DiagonalData.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_set_get_0200() {
        DiagonalData instance = new DiagonalData(DiagonalData.Type.TRI_DIAGONAL, 5);

        instance.set(1, 3, 99);//value should be 0
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for math">
    /**
     * Test of math methods, of class DiagonalData.
     */
    @Test
    public void test_math_0010() {
        DiagonalData D1 = new DiagonalData(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        DiagonalData D2 = new DiagonalData(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {3.3, 6.6, 9.9, 12.12}
                });

        DiagonalData D3 = new DiagonalData(new double[][]{
                    {2 + 2.2, 5 + 5.5, 8 + 8.8, 11 + 11.11},
                    {1 + 1.1, 4 + 4.4, 7 + 7.7, 10 + 10.1, 13 + 13.13},
                    {3 + 3.3, 6 + 6.6, 9 + 9.9, 12 + 12.12}
                });

        assertEquals(D3, D1.add(D2));
    }

    /**
     * Test of math methods, of class DiagonalData.
     */
    @Test
    public void test_math_0020() {
        DiagonalData D1 = new DiagonalData(new double[][]{
                    {2, 5, 8, 11},
                    {1, 4, 7, 10, 13},
                    {3, 6, 9, 12}
                });

        DiagonalData D2 = new DiagonalData(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {3.3, 6.6, 9.9, 12.12}
                });

        DiagonalData D3 = new DiagonalData(new double[][]{
                    {2.2 - 2, 5.5 - 5, 8.8 - 8, 11.11 - 11},
                    {1.1 - 1, 4.4 - 4, 7.7 - 7, 10.1 - 10, 13.13 - 13},
                    {3.3 - 3, 6.6 - 6, 9.9 - 9, 12.12 - 12}
                });

        assertEquals(D3, D2.minus(D1));
    }

    /**
     * Test of math methods, of class DiagonalData.
     */
    @Test
    public void test_math_0030() {
        DiagonalData D1 = new DiagonalData(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {3.3, 6.6, 9.9, 12.12}
                });

        DiagonalData D2 = new DiagonalData(new double[][]{
                    {4.4, 11, 17.6, 22.22},
                    {2.2, 8.8, 15.4, 20.2, 26.26},
                    {6.6, 13.2, 19.8, 24.24}
                });


        assertEquals(D2, D1.scaled(2d));
    }

    /**
     * Test of math methods, of class DiagonalData.
     */
    @Test
    public void test_math_0040() {
        DiagonalData D1 = new DiagonalData(new double[][]{
                    {2.2, 5.5, 8.8, 11.11},
                    {1.1, 4.4, 7.7, 10.1, 13.13},
                    {0, 0, 0, 0}
                });

        DiagonalData D2 = new DiagonalData(new double[][]{
                    {-2.2, -5.5, -8.8, -11.11},
                    {-1.1, -4.4, -7.7, -10.1, -13.13},
                    {0, 0, 0, 0}
                });


        assertEquals(D2, D1.opposite());
    }
    //</editor-fold>
}
