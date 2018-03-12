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
public class ShapiroWilkDistributionTest {

    @Test
    public void test_0010() {
        ShapiroWilkDistribution instance = new ShapiroWilkDistribution(3);
        double result = instance.cdf(0.9283);
        assertEquals(1. - 0.4822, result, 1e-3);
    }

    @Test
    public void test_0020() {
        ShapiroWilkDistribution instance = new ShapiroWilkDistribution(11);
        double result = instance.cdf(0.9426);
        assertEquals(1. - 0.551, result, 1e-3);
    }

    @Test
    public void test_0030() {
        ShapiroWilkDistribution instance = new ShapiroWilkDistribution(12);
        double result = instance.cdf(0.7819);
        assertEquals(1. - 0.005869, result, 1e-3);
    }
}
