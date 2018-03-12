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
package com.numericalmethod.suanshu.stats.test.mean;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class OneWayANOVATest {

    /**
     * R code for the One-Way ANOVA test
    x=c(1.3,5.4,7.6,7.2,3.5,2.7,5.21,6.3,4.4,9.8,10.24);
    g=c(1,1,1,1,1,0,0,0,0,0,0)
    summary(aov(x~g))
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        OneWayANOVA instance = new OneWayANOVA(samples);
        assertEquals(0.6978, instance.statistics(), 1e-4);
        assertEquals(0.4252, instance.pValue(), 1e-4);

        //from debugger
        assertEquals(0.6977765754226839, instance.statistics(), 1e-16);
        assertEquals(0.425150899911902, instance.pValue(), 1e-15);
    }

    /**
     * R code for the One-Way ANOVA test
    x=c(1.3,5.4,7.6,7.2,3.5,2.7,5.21,6.3,4.4,9.8,10.24,-2.3,-5.3,-4.33,-5.4,0.21,0.34,0.27,0.86,0.902,0.663);
    g=c(1,1,1,1,1,0,0,0,0,0,0,2,2,2,2,3,3,3,3,3,3)
    summary(aov(x~factor(g)))
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};
        samples[2] = new double[]{-2.3, -5.3, -4.33, -5.4};
        samples[3] = new double[]{0.21, 0.34, 0.27, 0.86, 0.902, 0.663};

        OneWayANOVA instance = new OneWayANOVA(samples);
        assertEquals(23.709, instance.statistics(), 1e-3);
        assertEquals(2.63e-06, instance.pValue(), 1e-9);

        //from debugger
        assertEquals(23.708487028327365, instance.statistics(), 1e-15);
        assertEquals(2.629604777371064E-6, instance.pValue(), 1e-21);
    }
}
