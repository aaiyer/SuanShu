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
package com.numericalmethod.suanshu.matrix.generic.matrixtype;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ComplexMatrixTest {

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0010() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 0), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 0)}
                });

        assertEquals(2, A1.nRows());
        assertEquals(2, A1.nCols());
        assertEquals(new Complex(2, 0), A1.get(1, 2));

        ComplexMatrix A2 = new ComplexMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A3 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(A2, A1);
        assertEquals(A3, A1.doubleValue());
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0020() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 1), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 1)}
                });

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 0), new Complex(2, 1)},
                    {new Complex(3, 1), new Complex(4, 0)}
                });

        ComplexMatrix A3 = new ComplexMatrix(new Complex[][]{
                    {new Complex(2, 1), new Complex(4, 1)},
                    {new Complex(6, 1), new Complex(8, 1)}
                });

        assertEquals(A3, A1.add(A2));
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0030() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 1), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 1)}
                });

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 0), new Complex(2, 1)},
                    {new Complex(3, 1), new Complex(4, 0)}
                });

        ComplexMatrix A3 = new ComplexMatrix(new Complex[][]{
                    {new Complex(0, 1), new Complex(0, -1)},
                    {new Complex(0, -1), new Complex(0, 1)}
                });

        assertEquals(A3, A1.minus(A2));
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0040() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 1), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 1)}
                });

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 0), new Complex(2, 1)},
                    {new Complex(3, 1), new Complex(4, 0)}
                });

        ComplexMatrix A3 = new ComplexMatrix(new Complex[][]{
                    {new Complex(7, 3), new Complex(9, 3)},
                    {new Complex(14, 7), new Complex(22, 7)}
                });

        assertEquals(A3, A1.multiply(A2));
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0050() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 1), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 1)}
                });

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(0, 2), new Complex(2, 2)},
                    {new Complex(3, 3), new Complex(3, 5)}
                });

        assertEquals(A2, A1.scaled(new Complex(1, 1)));
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0060() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 1), new Complex(2, 0)},
                    {new Complex(3, 0), new Complex(4, 1)}
                });

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(-1, -1), new Complex(-2, 0)},
                    {new Complex(-3, 0), new Complex(-4, -1)}
                });

        assertEquals(A2, A1.opposite());
    }

    /**
     * Test of class ComplexMatrix.
     */
    @Test
    public void test_ComplexMatrix_0070() {
        ComplexMatrix A1 = new ComplexMatrix(new Complex[][]{
                    {new Complex(0, 0), new Complex(0, 0)},
                    {new Complex(0, 0), new Complex(0, 0)}
                });

        assertEquals(A1.ZERO(), A1);

        ComplexMatrix A2 = new ComplexMatrix(new Complex[][]{
                    {new Complex(1, 0), new Complex(0, 0)},
                    {new Complex(0, 0), new Complex(1, 0)}
                });

        assertEquals(A2.ONE(), A2);

        assertEquals(A2, A1.add(A2));
        assertEquals(A2.ONE(), A1.add(A2));
        assertEquals(A1.ZERO(), A1.minus(A1));
        assertEquals(A2.ZERO(), A2.minus(A2));
    }
}
