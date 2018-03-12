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
package com.numericalmethod.suanshu.vector.doubles;

import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.IsVector.VectorAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ImmutableVectorTest {

    @Test
    public void test_ctor_0010() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Vector v1 = new DenseVector(arr1);
        Vector v2 = new ImmutableVector(v1);

        assertArrayEquals(v1.toArray(), v2.toArray(), 1e-15);
    }

    @Test
    public void test_ctor_0020() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Vector v1 = new ImmutableVector(new DenseVector(arr1));
        Vector v2 = new ImmutableVector(v1);

        assertArrayEquals(v1.toArray(), v2.toArray(), 1e-15);
    }

    @Test(expected = VectorAccessException.class)
    public void test_set_0010() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Vector v1 = new DenseVector(arr1);
        Vector v2 = new ImmutableVector(v1);

        v2.set(1, 1);
    }
}
