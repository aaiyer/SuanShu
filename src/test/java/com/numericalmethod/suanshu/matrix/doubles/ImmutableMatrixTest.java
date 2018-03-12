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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ImmutableMatrixTest {

    @Test
    public void test_0010() {
        double[][] arr1 = new double[][]{
            new double[]{1, 2},
            new double[]{3, 4}
        };
        Matrix m1 = new DenseMatrix(arr1);
        Matrix m2 = new ImmutableMatrix(m1);

        assertTrue(AreMatrices.equal(m1, m2, 0));
    }

    @Test
    public void test_0020() {
        double[][] arr1 = new double[][]{
            new double[]{1, 2},
            new double[]{3, 4}
        };
        Matrix m1 = new ImmutableMatrix(new DenseMatrix(arr1));
        Matrix m2 = new ImmutableMatrix(m1);

        assertEquals(m1, m2);
    }

    @Test(expected = RuntimeException.class)
    public void test_0030() {
        double[][] arr1 = new double[][]{
            new double[]{1, 2},
            new double[]{3, 4}
        };
        ImmutableMatrix m1 = new ImmutableMatrix(new DenseMatrix(arr1));
        m1.set(1, 1, 100);
    }
}
