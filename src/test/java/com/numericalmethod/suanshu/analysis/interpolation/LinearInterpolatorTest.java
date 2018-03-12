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
package com.numericalmethod.suanshu.analysis.interpolation;

import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearInterpolatorTest {

    @Test
    public void test_0010() {
        double[] x = new double[]{1., 2., 3., 4., 5.};
        double[] y = new double[]{2., 4., 6., 8., 10.};

        BinaryRelation f = new BinaryRelation(x, y);
        LinearInterpolator instance = new LinearInterpolator(f);

        assertEquals(y[0], instance.evaluate(x[0]), 0);
        assertEquals(y[1], instance.evaluate(x[1]), 0);
        assertEquals(y[2], instance.evaluate(x[2]), 0);
        assertEquals(y[3], instance.evaluate(x[3]), 0);
        assertEquals(y[4], instance.evaluate(x[4]), 0);

        assertEquals(3., instance.evaluate(1.5), 0);
        assertEquals(5., instance.evaluate(2.5), 0);
        assertEquals(7., instance.evaluate(3.5), 0);
        assertEquals(9., instance.evaluate(4.5), 0);
        assertEquals(9.6, instance.evaluate(4.8), 0);
    }

    @Test(expected = RuntimeException.class)
    public void test_0020() {
        double[] x = new double[]{1., 2., 3., 4., 5.};
        double[] y = new double[]{2., 4., 6., 8., 10.};

        BinaryRelation f = new BinaryRelation(x, y);
        LinearInterpolator instance = new LinearInterpolator(f);
        instance.evaluate(0.5);
    }

    @Test(expected = RuntimeException.class)
    public void test_0030() {
        double[] x = new double[]{1., 2., 3., 4., 5.};
        double[] y = new double[]{2., 4., 6., 8., 10.};

        BinaryRelation f = new BinaryRelation(x, y);
        LinearInterpolator instance = new LinearInterpolator(f);
        instance.evaluate(5.5);
    }
}
