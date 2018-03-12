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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ElementaryOperationTest {

    @Test
    public void testElementaryOperation_0010() {
        ElementaryOperation instance = new ElementaryOperation(3);

        instance.swapRow(2, 3);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 1},
                    {0, 1, 0}
                }),
                instance.T());

        instance.scaleRow(2, 2);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 2},
                    {0, 1, 0}
                }),
                instance.T());

        instance.addRow(2, 3, -1);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, -1, 2},
                    {0, 1, 0}
                }),
                instance.T());
    }

    @Test
    public void testElementaryOperation_0020() {
        ElementaryOperation instance = new ElementaryOperation(new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {4, 5, 9},
                    {7, 8, 23}
                }));

        instance.swapRow(2, 3);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {7, 8, 23},
                    {4, 5, 9}
                }),
                instance.T());

        instance.scaleRow(2, 2);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {14, 16, 46},
                    {4, 5, 9}
                }),
                instance.T());

        instance.addRow(2, 3, -1);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {10, 11, 37},
                    {4, 5, 9}
                }),
                instance.T());
    }

    @Test
    public void testElementaryOperation_0030() {
        ElementaryOperation instance1 = new ElementaryOperation(3);
        instance1.swapRow(2, 3);
        instance1.scaleRow(2, 2);
        instance1.addRow(2, 3, -1);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {4, 5, 9},
                    {7, 8, 23}
                });
        ElementaryOperation instance2 = new ElementaryOperation(A);
        instance2.swapRow(2, 3);
        instance2.scaleRow(2, 2);
        instance2.addRow(2, 3, -1);
        assertEquals(instance2.T(), instance1.T().multiply(A));
    }

    @Test
    public void testElementaryOperation_0040() {
        ElementaryOperation instance = new ElementaryOperation(3);

        instance.swapColumn(2, 3);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 1},
                    {0, 1, 0}
                }),
                instance.T());

        instance.scaleColumn(2, 2);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 1},
                    {0, 2, 0}
                }),
                instance.T());

        instance.addColumn(2, 3, -1);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, -1, 1},
                    {0, 2, 0}
                }),
                instance.T());
    }

    @Test
    public void testElementaryOperation_0050() {
        ElementaryOperation instance = new ElementaryOperation(new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {4, 5, 9},
                    {7, 8, 23}
                }));

        instance.swapColumn(2, 3);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 5, 2},
                    {4, 9, 5},
                    {7, 23, 8}
                }),
                instance.T());

        instance.scaleColumn(2, 2);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 10, 2},
                    {4, 18, 5},
                    {7, 46, 8}
                }),
                instance.T());

        instance.addColumn(2, 3, -1);
        assertEquals(new DenseMatrix(new double[][]{
                    {1, 8, 2},
                    {4, 13, 5},
                    {7, 38, 8}
                }),
                instance.T());
    }

    @Test
    public void testElementaryOperation_0060() {
        ElementaryOperation instance1 = new ElementaryOperation(3);
        instance1.swapColumn(2, 3);
        instance1.scaleColumn(2, 2);
        instance1.addColumn(2, 3, -1);

        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 5},
                    {4, 5, 9},
                    {7, 8, 23}
                });
        ElementaryOperation instance2 = new ElementaryOperation(A);
        instance2.swapColumn(2, 3);
        instance2.scaleColumn(2, 2);
        instance2.addColumn(2, 3, -1);
        assertEquals(instance2.T(), A.multiply(instance1.T()));
    }
}
