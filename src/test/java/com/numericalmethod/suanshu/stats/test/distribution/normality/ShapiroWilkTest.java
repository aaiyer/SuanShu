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
package com.numericalmethod.suanshu.stats.test.distribution.normality;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ShapiroWilkTest {

    /**
     * n = 3
     *
    shapiro.test(c(-1.7, -1, 1))
     */
    @Test
    public void test_0010() {
        double[] sample = new double[]{-1.7, -1, 1};

        ShapiroWilk instance = new ShapiroWilk(sample);
        assertEquals(0.9283, instance.statistics(), 1e-4);
        assertEquals(0.4822, instance.pValue(), 1e-4);
    }

    /**
     * n = 11
     *
    shapiro.test(c(-1.7, -1,-1,-.73,-.61,-.5,-.24, .45,.62,.81,1))
     */
    @Test
    public void test_0020() {
        double[] sample = new double[]{-1.7, -1, -1, -.73, -.61, -.5, -.24, .45, .62, .81, 1};

        ShapiroWilk instance = new ShapiroWilk(sample);
        assertEquals(0.9426, instance.statistics(), 1e-4);
        assertEquals(0.551, instance.pValue(), 1e-4);
    }

    /**
     * n = 12
     *
    shapiro.test(c(-1.7, -1,-1,-.73,-.61,-.5,-.24, .45,.62,.81,1,5))
     */
    @Test
    public void test_0030() {
        double[] sample = new double[]{-1.7, -1, -1, -.73, -.61, -.5, -.24, .45, .62, .81, 1, 5};

        ShapiroWilk instance = new ShapiroWilk(sample);
        assertEquals(0.7819, instance.statistics(), 1e-4);
        assertEquals(0.005869, instance.pValue(), 1e-4);
    }
}
