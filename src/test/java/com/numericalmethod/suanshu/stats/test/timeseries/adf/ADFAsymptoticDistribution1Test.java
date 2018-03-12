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

import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class ADFAsymptoticDistribution1Test {

    /**
     * @see Wayne A. Fuller. "Introduction to Statistical Time Series". Table 4.2. Model 3a/8a.
     */
    @Test
    public void test_0010() {
        ProbabilityDistribution dist = new ADFAsymptoticDistribution1(
                3000,
                3000,
                ADFAsymptoticDistribution1.Type.DICKEY_FULLER,
                1234567L);

        assertEquals(-2.58, dist.quantile(0.01), 1e-1);
        assertEquals(-1.95, dist.quantile(0.05), 1e-1);
        assertEquals(-1.62, dist.quantile(0.1), 1e-1);
        assertEquals(0.89, dist.quantile(0.9), 1e-1);
        assertEquals(1.28, dist.quantile(0.95), 1e-1);
        assertEquals(2.00, dist.quantile(0.99), 1e-1);
    }

    /**
     * @see Wayne A. Fuller. "Introduction to Statistical Time Series". Table 4.2. Model 3a/8a.
     */
    @Test
    public void test_0020() {
        ProbabilityDistribution dist = new ADFAsymptoticDistribution1(
                1500,
                1500,
                ADFAsymptoticDistribution1.Type.AUGMENTED_DICKEY_FULLER,
                1234567L);

        assertEquals(-3.96, dist.quantile(0.01), 1e-1);
        assertEquals(-3.41, dist.quantile(0.05), 1e-1);
        assertEquals(-3.12, dist.quantile(0.1), 1e-1);
        assertEquals(-1.25, dist.quantile(0.9), 1e-1);
        assertEquals(-0.94, dist.quantile(0.95), 1e-1);
        assertEquals(-0.33, dist.quantile(0.99), 1e-1);
    }
}
