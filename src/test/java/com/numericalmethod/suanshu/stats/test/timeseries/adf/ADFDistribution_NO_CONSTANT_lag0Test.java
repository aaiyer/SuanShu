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
public class ADFDistribution_NO_CONSTANT_lag0Test {

    @Test
    public void test_0010() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.NO_CONSTANT, 0, 25);
        assertEquals(-2.66, instance.quantile(0.01), 1e-2);
//        assertEquals(-2.26, instance.quantile(0.025), 1e-2);
        assertEquals(-1.95, instance.quantile(0.05), 1e-2);
        assertEquals(-1.6, instance.quantile(0.1), 1e-2);
//        assertEquals(0.92, instance.quantile(0.9), 1e-2);
        assertEquals(1.33, instance.quantile(0.95), 1e-2);
//        assertEquals(1.7, instance.quantile(0.975), 1e-2);
        assertEquals(2.16, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0020() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.NO_CONSTANT, 0, 50);
//        assertEquals(-2.62, instance.quantile(0.01), 1e-2);
//        assertEquals(-2.25, instance.quantile(0.025), 1e-2);
//        assertEquals(-1.95, instance.quantile(0.05), 1e-2);
        assertEquals(-1.61, instance.quantile(0.1), 1e-2);
        assertEquals(0.91, instance.quantile(0.9), 1e-2);
        assertEquals(1.31, instance.quantile(0.95), 1e-2);
//        assertEquals(1.66, instance.quantile(0.975), 1e-2);
        assertEquals(2.08, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0030() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.NO_CONSTANT, 0, 100);
//        assertEquals(-2.60, instance.quantile(0.01), 1e-2);
//        assertEquals(-2.24, instance.quantile(0.025), 1e-2);
        assertEquals(-1.95, instance.quantile(0.05), 1e-2);
        assertEquals(-1.61, instance.quantile(0.1), 1e-2);
        assertEquals(0.90, instance.quantile(0.9), 1e-2);
//        assertEquals(1.29, instance.quantile(0.95), 1e-2);
//        assertEquals(1.64, instance.quantile(0.975), 1e-2);
//        assertEquals(2.03, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0040() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.NO_CONSTANT, 0, 200);//infinity
//        assertEquals(-2.58, instance.quantile(0.01), 1e-2);
//        assertEquals(-2.23, instance.quantile(0.025), 1e-2);
//        assertEquals(-1.95, instance.quantile(0.05), 1e-2);
//        assertEquals(-1.62, instance.quantile(0.1), 1e-2);
        assertEquals(0.89, instance.quantile(0.9), 1e-2);
        assertEquals(1.28, instance.quantile(0.95), 1e-2);
//        assertEquals(1.62, instance.quantile(0.975), 1e-2);
//        assertEquals(2.00, instance.quantile(0.99), 1e-2);
    }
}
