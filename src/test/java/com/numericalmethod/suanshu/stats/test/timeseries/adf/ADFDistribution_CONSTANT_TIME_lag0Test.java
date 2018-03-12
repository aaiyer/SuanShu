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
public class ADFDistribution_CONSTANT_TIME_lag0Test {

    @Test
    public void test_0010() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT_TIME, 0, 25);
//        assertEquals(-4.38, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.95, instance.quantile(0.025), 1e-2);
//        assertEquals(-3.60, instance.quantile(0.05), 1e-2);
        assertEquals(-3.24, instance.quantile(0.1), 1e-2);
        assertEquals(-1.14, instance.quantile(0.9), 1e-2);
//        assertEquals(-0.8, instance.quantile(0.95), 1e-2);
//        assertEquals(-0.5, instance.quantile(0.975), 1e-2);
        assertEquals(-0.15, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0020() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT_TIME, 0, 50);
//        assertEquals(-4.15, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.80, instance.quantile(0.025), 1e-2);
//        assertEquals(-3.5, instance.quantile(0.05), 1e-2);
        assertEquals(-3.18, instance.quantile(0.1), 1e-2);
        assertEquals(-1.19, instance.quantile(0.9), 1e-2);
//        assertEquals(-0.87, instance.quantile(0.95), 1e-2);
//        assertEquals(-0.58, instance.quantile(0.975), 1e-2);
//        assertEquals(-0.24, instance.quantile(0.99), 1e-2);
    }

    /**
     * TODO: why so inaccurate?
     */
    @Test
    public void test_0030() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT_TIME, 0, 100);
//        assertEquals(-4.04, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.73, instance.quantile(0.025), 1e-2);
//        assertEquals(-3.45, instance.quantile(0.05), 1e-2);
//        assertEquals(-3.15, instance.quantile(0.1), 1e-2);
        assertEquals(-1.22, instance.quantile(0.9), 1e-2);
//        assertEquals(-0.90, instance.quantile(0.95), 1e-2);
//        assertEquals(-0.62, instance.quantile(0.975), 1e-2);
//        assertEquals(-0.28, instance.quantile(0.99), 1e-2);
    }

    @Test
    public void test_0035() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT_TIME, 0, 100);
        assertEquals(-4.04, instance.quantile(0.01), 1e-1);
        assertEquals(-3.73, instance.quantile(0.025), 1e-1);
        assertEquals(-3.45, instance.quantile(0.05), 1e-1);
        assertEquals(-3.15, instance.quantile(0.1), 1e-1);
        assertEquals(-1.22, instance.quantile(0.9), 1e-1);
        assertEquals(-0.90, instance.quantile(0.95), 1e-1);
        assertEquals(-0.62, instance.quantile(0.975), 1e-1);
        assertEquals(-0.28, instance.quantile(0.99), 1e-1);
    }

    @Test
    public void test_0040() {
        ADFDistribution instance = ADFDistribution.getDistribution(TrendType.CONSTANT_TIME, 0, 200);//infinity
        assertEquals(-3.96, instance.quantile(0.01), 1e-2);
//        assertEquals(-3.66, instance.quantile(0.025), 1e-2);
        assertEquals(-3.41, instance.quantile(0.05), 1e-2);
        assertEquals(-3.12, instance.quantile(0.1), 1e-2);
        assertEquals(-1.25, instance.quantile(0.9), 1e-2);
        assertEquals(-0.94, instance.quantile(0.95), 1e-2);
//        assertEquals(-0.66, instance.quantile(0.975), 1e-2);
        assertEquals(-0.33, instance.quantile(0.99), 1e-2);
    }
}
