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
package com.numericalmethod.suanshu.stats.test.timeseries.adf;

import com.numericalmethod.suanshu.stats.test.timeseries.adf.AugmentedDickeyFuller.TrendType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ADFDistribution_CONSTANT_lag0Test {

    @Test
    public void test_0010() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT, 0, 25);
        assertEquals(-3.75, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.33, instance.quantile(0.025), 1e-2);
        assertEquals(-3.00, instance.quantile(0.05), 1e-2);
        assertEquals(-2.63, instance.quantile(0.1), 1e-2);
        assertEquals(-0.37, instance.quantile(0.9), 1e-2);
        assertEquals(0.00, instance.quantile(0.95), 1e-2);
//        assertEquals(0.34, instance.quantile(0.975), 1e-2);
//        assertEquals(0.72, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0020() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT, 0, 50);
//        assertEquals(-3.58, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.22, instance.quantile(0.025), 1e-2);
        assertEquals(-2.93, instance.quantile(0.05), 1e-2);
        assertEquals(-2.6, instance.quantile(0.1), 1e-2);
        assertEquals(-0.4, instance.quantile(0.9), 1e-2);
        assertEquals(-0.03, instance.quantile(0.95), 1e-2);
//        assertEquals(0.29, instance.quantile(0.975), 1e-2);
        assertEquals(0.66, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0030() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT, 0, 100);
        assertEquals(-3.51, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.17, instance.quantile(0.025), 1e-2);
        assertEquals(-2.89, instance.quantile(0.05), 1e-2);
        assertEquals(-2.58, instance.quantile(0.1), 1e-2);
        assertEquals(-0.42, instance.quantile(0.9), 1e-2);
//        assertEquals(-0.05, instance.quantile(0.95), 1e-2);
//        assertEquals(0.26, instance.quantile(0.975), 1e-2);
        assertEquals(0.63, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0040() {
        ADFDistribution instance = ADFDistribution.getDistribution(AugmentedDickeyFuller.TrendType.CONSTANT, 0, 200);//infinity
        assertEquals(-3.43, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.12, instance.quantile(0.025), 1e-2);
        assertEquals(-2.86, instance.quantile(0.05), 1e-2);
        assertEquals(-2.57, instance.quantile(0.1), 1e-2);
        assertEquals(-0.44, instance.quantile(0.9), 1e-2);
//        assertEquals(-0.07, instance.quantile(0.95), 1e-2);
//        assertEquals(0.23, instance.quantile(0.975), 1e-2);
        assertEquals(0.6, instance.quantile(0.99), 1e-2);
    }
}
