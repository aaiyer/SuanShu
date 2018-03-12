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
package com.numericalmethod.suanshu.stats.test.variance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LeveneTest {

    /**
     * R code for the Levene test
    require(car)
    x=c(1,3,5,2,3,5,2,5,6,4,9,8)
    g=c(1,1,1,1,1,1,0,0,0,0,0,0)
    levene.test(x,g)
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 3, 5, 2, 3, 5};
        samples[1] = new double[]{2, 5, 6, 4, 9, 8};

        Levene instance = new Levene(Levene.Type.MEDIAN, samples);
        assertEquals(1.45349, instance.statistics(), 1e-5);
        assertEquals(0.25572, instance.pValue(), 1e-5);
    }

    /**
     * R code for the Levene test
    require(car)
    x=c(1,3,5,2,3,5,2,5,6,4,9,8,1,8,3,4,9,9,7,2,1,3,4)
    g=c(1,1,1,1,1,1,0,0,0,0,0,0,3,3,3,3,3,3,3,4,4,4,4)
    levene.test(x,g)
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{1, 3, 5, 2, 3, 5};
        samples[1] = new double[]{2, 5, 6, 4, 9, 8};
        samples[2] = new double[]{1, 8, 3, 4, 9, 9, 7};
        samples[3] = new double[]{2, 1, 3, 4};

        Levene instance = new Levene(Levene.Type.MEDIAN, samples);
        assertEquals(1.51903, instance.statistics(), 1e-5);
        assertEquals(0.24186, instance.pValue(), 1e-5);
    }

    /**
     * R code for the Levene test
    require(car)
    x=c(1,3,5,2,3,5,2,5,6,4,9,8)
    g=c(1,1,1,1,1,1,0,0,0,0,0,0)
    levene.test(x,g)
     */
    @Test
    public void test_0030() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 3, 5, 2, 3, 5};
        samples[1] = new double[]{2, 5, 6, 4, 9, 8};

        Levene instance = new Levene(Levene.Type.MEDIAN, samples);
        assertEquals(1.45349, instance.statistics(), 1e-5);
        assertEquals(0.25572, instance.pValue(), 1e-5);
    }
}
