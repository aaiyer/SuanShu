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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SubMatrixRefTest {

    @Test
    public void test_0010() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 1, 2, 1, 2);
        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1, 2},
                    {6, 7}
                }), instance,
                0));
        assertEquals(7, instance.get(2, 2), 0);
    }

    @Test
    public void test_0015() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref);
        assertEquals(ref.nRows(), instance.nRows());
        assertEquals(ref.nCols(), instance.nCols());

        assertTrue(AreMatrices.equal(ref, instance, 0));
    }

    @Test
    public void test_0020() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 2, 5, 2, 4);
        assertEquals(4, instance.nRows());
        assertEquals(3, instance.nCols());

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {7, 8, 9},
                    {12, 13, 14},
                    {17, 18, 19},
                    {22, 23, 24}
                }), instance,
                0));

        Vector vv1 = instance.getColumn(2);
        assertEquals(new DenseVector(new double[]{8, 13, 18, 23}), vv1);

        Vector vv2 = instance.getRow(3);
        assertEquals(new DenseVector(new double[]{17, 18, 19}), vv2);

        assertEquals(13, instance.get(2, 2), 0);
    }

    @Test
    public void test_0030() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {0}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 1, 1, 1, 1);
        assertEquals(1, instance.nRows());
        assertEquals(1, instance.nCols());

        Matrix M1 = ref.ZERO();
        assertTrue(AreMatrices.equal(M1, instance, 0));

        Vector vv1 = instance.getColumn(1);
        assertEquals(vv1.ZERO(), vv1);

        Vector vv2 = instance.getRow(1);
        assertEquals(vv2.ZERO(), vv2);

        assertEquals(0, instance.get(1, 1), 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_0040() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 0, 5, 2, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_0050() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 2, 6, 2, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_0060() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 2, 5, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_0070() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 2, 5, 2, 6);
    }

    @Test(expected = MatrixAccessException.class)
    @SuppressWarnings("deprecation")
    public void test_0080() {
        Matrix ref = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        SubMatrixRef instance = new SubMatrixRef(ref, 2, 5, 2, 5);
        instance.set(1, 1, 100);
    }
}
