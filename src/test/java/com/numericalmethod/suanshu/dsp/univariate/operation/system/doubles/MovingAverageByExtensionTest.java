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
package com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles;

import com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles.MovingAverageByExtension;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MovingAverageByExtensionTest {

    @Test
    public void test_0010() {
        double[] Xt = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] MAFilter = new double[]{1. / 3, 1. / 3, 1. / 3};

        MovingAverageByExtension instance = new MovingAverageByExtension(MAFilter);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{4. / 3., 2, 3, 4, 5, 6, 7, 8, 26. / 3};
        assertArrayEquals(expResult, result, 1e-15);
    }

    @Test
    public void test_0020() {
        double[] Xt = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] MAFilter = new double[]{1. / 2, 1. / 2};

        MovingAverageByExtension instance = new MovingAverageByExtension(MAFilter);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9};
        assertArrayEquals(expResult, result, 1e-15);
    }

    @Test
    public void test_0030() {
        double[] Xt = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] MAFilter = new double[]{1. / 4, 1. / 4, 1. / 4, 1. / 4};

        MovingAverageByExtension instance = new MovingAverageByExtension(MAFilter);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{7. / 4, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.25, 8.75};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
     * no filtering
     */
    @Test
    public void test_0040() {
        double[] Xt = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] MAFilter = new double[]{1};

        MovingAverageByExtension instance = new MovingAverageByExtension(MAFilter);
        double[] result = instance.transform(Xt);

        assertArrayEquals(Xt, result, 1e-15);
    }
}
