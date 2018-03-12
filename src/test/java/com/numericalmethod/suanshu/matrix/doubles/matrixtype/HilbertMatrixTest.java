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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HilbertMatrixTest {

    /**
     * Test of HilbertMatrix method, of class HilbertMatrix.
     */
    @Test
    public void testHilbertMatrix_001() {
        HilbertMatrix instance = new HilbertMatrix(5);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1d, 1d / 2d, 1d / 3d, 1d / 4d, 1d / 5d},
                    {1d / 2d, 1d / 3d, 1d / 4d, 1d / 5d, 1d / 6d},
                    {1d / 3d, 1d / 4d, 1d / 5d, 1d / 6d, 1d / 7d},
                    {1d / 4d, 1d / 5d, 1d / 6d, 1d / 7d, 1d / 8d},
                    {1d / 5d, 1d / 6d, 1d / 7d, 1d / 8d, 1d / 9d},});

        assertTrue(AreMatrices.equal(expected, instance, 0));
        assertEquals(MatrixMeasure.det(expected), instance.det(), 1e-22);
    }

    /**
     * Test of det method, of class HilbertMatrix.
     */
    @Test
    public void testDet_001() {
        HilbertMatrix instance = new HilbertMatrix(10);
        assertEquals(MatrixMeasure.det(instance), instance.det(), 1e-56);
    }

    /**
     * Test of det method, of class HilbertMatrix.
     */
    @Test
    public void testDet_002() {
        HilbertMatrix instance = new HilbertMatrix(11);
        assertEquals(MatrixMeasure.det(instance), instance.det(), 1e-67);
    }
}
