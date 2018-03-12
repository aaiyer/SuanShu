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
package com.numericalmethod.suanshu.analysis.function.rn2r1.univariate;

import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import com.numericalmethod.suanshu.analysis.interpolation.DuplicatedAbscissae;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class StepFunctionTest {

    @Test
    public void test_0010() {
        double[] x = new double[]{1., 2., 3., 4., 5.};
        double[] y = new double[]{2., 4., 6., 8., 10.};
        StepFunction instance = new StepFunction(new BinaryRelation(x, y));

        assertEquals(2., instance.evaluate(1.), 0);
        assertEquals(4., instance.evaluate(2.), 0);
        assertEquals(6., instance.evaluate(3.), 0);
        assertEquals(8., instance.evaluate(4.), 0);
        assertEquals(10., instance.evaluate(5.), 0);

        assertArrayEquals(x, instance.x(), 0);
        assertArrayEquals(y, instance.y(), 0);

        instance.add(new BinaryRelation(
                new double[]{-11., -12.},
                new double[]{21., 22.}));
        assertEquals(21., instance.evaluate(-11.), 0);
        assertEquals(22., instance.evaluate(-12.), 0);

        assertArrayEquals(new double[]{-12., -11., 1., 2., 3., 4., 5.}, instance.x(), 0);
        assertArrayEquals(new double[]{22., 21., 2., 4., 6., 8., 10.}, instance.y(), 0);
    }

    @Test(expected = DuplicatedAbscissae.class)
    public void test_0020() {
        double[] x = new double[]{1., 1., 3., 4., 5.};//x = 1 duplicated
        double[] y = new double[]{2., 4., 6., 8., 10.};
        StepFunction instance = new StepFunction(new BinaryRelation(x, y));
    }

    @Test(expected = DuplicatedAbscissae.class)
    public void test_0030() {
        double[] x = new double[]{1., 1.09, 3., 4., 5.};//x = 1 duplicated
        double[] y = new double[]{2., 4., 6., 8., 10.};
        StepFunction instance = new StepFunction(new BinaryRelation(x, y), 0.1);
    }

    @Test
    public void test_0040() {
        double[] x = new double[]{1., 2., 3., 4., 5.};
        double[] y = new double[]{2., 4., 6., 8., 10.};
        StepFunction instance = new StepFunction(new BinaryRelation(x, y), 0.1);
        assertEquals(4., instance.evaluate(2.05), 0);
    }
}
