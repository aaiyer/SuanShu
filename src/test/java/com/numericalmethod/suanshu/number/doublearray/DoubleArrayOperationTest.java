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

package com.numericalmethod.suanshu.number.doublearray;

import com.numericalmethod.suanshu.misc.R;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken
 */
@Ignore("This contains generic testcases for DoubleArrayOperation")
public abstract class DoubleArrayOperationTest {

    public abstract DoubleArrayOperation newInstance();

    @Test
    public void test_add_0010() {
        double[] arr1 = R.rep(1., 10);
        double[] arr2 = R.rep(2., 10);
        DoubleArrayOperation instance = newInstance();
        double[] expResult = R.rep(3., 10);
        double[] result = instance.add(arr1, arr2);
        assertArrayEquals(expResult, result, 1e-15);
    }

    @Test
    public void test_minus_0010() {
        double[] arr1 = R.rep(1., 10);
        double[] arr2 = R.rep(2., 10);
        DoubleArrayOperation instance = newInstance();
        double[] expResult = R.rep(-1., 10);
        double[] result = instance.minus(arr1, arr2);
        assertArrayEquals(expResult, result, 1e-15);
    }

    @Test
    public void test_scaled_0010() {
        double[] arr = R.rep(1., 10);
        double scalar = -1.;
        DoubleArrayOperation instance = newInstance();
        double[] expResult = R.rep(-1., 10);
        double[] result = instance.scaled(arr, scalar);
        assertArrayEquals(expResult, result, 1e-15);
    }
}