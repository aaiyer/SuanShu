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
package com.numericalmethod.suanshu.stats.test.rank.wilcoxon;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class WilcoxonRankSumDistributionTest {

    /**
     *
    pwilcox(1,10,5)
    dwilcox(1,10,5)
     */
    @Test
    public void test_0010() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(0.000666000666000666, dist.cdf(1), 1e-15);
        assertEquals(0.000333000333000333, dist.density(1), 1e-15);
    }

    /**
     *
    pwilcox(10,10,5)
    dwilcox(10,10,5)
     */
    @Test
    public void test_0020() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(0.03762903762903763, dist.cdf(10), 1e-15);
        assertEquals(0.00999000999000999, dist.density(10), 1e-15);
    }

    /**
     *
    pwilcox(30,10,5)
    dwilcox(30,10,5)
     */
    @Test
    public void test_0030() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(0.743256743256743, dist.cdf(30), 1e-15);
        assertEquals(0.04029304029304030, dist.density(30), 1e-15);
    }

    /**
     *
    pwilcox(48,10,5)
    dwilcox(48,10,5)
     */
    @Test
    public void test_0040() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(0.999333999333999, dist.cdf(48), 1e-15);
        assertEquals(0.000666000666000666, dist.density(48), 1e-15);
    }

    /**
     *
    pwilcox(50,10,5)
    dwilcox(50,10,5)
     */
    @Test
    public void test_0050() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(1, dist.cdf(50), 1e-15);
        assertEquals(0.000333000333000333, dist.density(50), 1e-15);
    }

    /**
     *
    qwilcox(0.5,10,5)
    qwilcox(0.45,10,5)
    qwilcox(0.4,10,5)
     */
    @Test
    public void test_0200() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(25, dist.quantile(0.5), 1e-15);
        assertEquals(24, dist.quantile(0.45), 1e-15);
        assertEquals(23, dist.quantile(0.4), 1e-15);
    }

    /**
     *
    qwilcox(0,10,5)
    qwilcox(0.001,10,5)
    qwilcox(0.01,10,5)
    qwilcox(0.1,10,5)
    qwilcox(0.2,10,5)
    qwilcox(0.5,10,5)
    qwilcox(0.7,10,5)
    qwilcox(0.9,10,5)
    qwilcox(0.999,10,5)
    qwilcox(1,10,5)
     */
    @Test
    public void test_0210() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(10, 5);
        assertEquals(0, dist.quantile(0.0), 1e-15);
        assertEquals(2, dist.quantile(0.001), 1e-15);
        assertEquals(7, dist.quantile(0.01), 1e-15);
        assertEquals(14, dist.quantile(0.1), 1e-15);
        assertEquals(18, dist.quantile(0.2), 1e-15);
        assertEquals(25, dist.quantile(0.5), 1e-15);
        assertEquals(29, dist.quantile(0.7), 1e-15);
        assertEquals(36, dist.quantile(0.9), 1e-15);
        assertEquals(48, dist.quantile(0.999), 1e-15);
    }

    @Test
    public void test_0070() {
        WilcoxonRankSumDistribution dist = new WilcoxonRankSumDistribution(5, 6);
        assertEquals(0.9588744588744589, dist.pValue1SidedGreater(6), 1e-15);
        assertEquals(0.12554112554112554, dist.pValue(6), 1e-15);
    }
}
