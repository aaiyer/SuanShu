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
public class WilcoxonSignedRankDistributionTest {

    /**
     *
    psignrank(1,10)
    dsignrank(1,10)
     */
    @Test
    public void test_0010() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.001953125, dist.cdf(1), 1e-15);
        assertEquals(0.0009765625, dist.density(1), 1e-15);
    }

    /**
     *
    psignrank(2,10)
    dsignrank(2,10)
     */
    @Test
    public void test_0020() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.0029296875, dist.cdf(2), 1e-15);
        assertEquals(0.0009765625, dist.density(2), 1e-15);
    }

    /**
     *
    psignrank(4,10)
    dsignrank(4,10)
     */
    @Test
    public void test_0030() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.0068359375, dist.cdf(4), 1e-15);
        assertEquals(0.001953125, dist.density(4), 1e-15);
    }

    /**
     *
    psignrank(5,10)
    dsignrank(5,10)
     */
    @Test
    public void test_0040() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.009765625, dist.cdf(5), 1e-15);
        assertEquals(0.0029296875, dist.density(5), 1e-15);
    }

    /**
     *
    psignrank(7,10)
    dsignrank(7,10)
     */
    @Test
    public void test_0050() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.0185546875, dist.cdf(7), 1e-15);
        assertEquals(0.0048828125, dist.density(7), 1e-15);
    }

    /**
     *
    psignrank(10,10)
    dsignrank(10,10)
     */
    @Test
    public void test_0060() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.0419921875, dist.cdf(10), 1e-15);
        assertEquals(0.009765625, dist.density(10), 1e-15);
    }

    /**
     *
    psignrank(25,10)
    dsignrank(25,10)
     */
    @Test
    public void test_0070() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.4228515625, dist.cdf(25), 1e-15);
        assertEquals(0.0380859375, dist.density(25), 1e-15);
    }

    /**
     *
    psignrank(35,10)
    dsignrank(35,10)
     */
    @Test
    public void test_0080() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.7841796875, dist.cdf(35), 1e-15);
        assertEquals(0.0302734375, dist.density(35), 1e-15);
    }

    /**
     *
    psignrank(48,10)
    dsignrank(48,10)
     */
    @Test
    public void test_0090() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.986328125, dist.cdf(48), 1e-15);
        assertEquals(0.0048828125, dist.density(48), 1e-15);
    }

    /**
     *
    psignrank(50,10)
    dsignrank(50,10)
     */
    @Test
    public void test_0100() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0.9931640625, dist.cdf(50), 1e-15);
        assertEquals(0.0029296875, dist.density(50), 1e-15);
    }

    /**
     *
    qsignrank(0,10)
    qsignrank(0.01,10)
    qsignrank(0.1,10)
    qsignrank(0.2,10)
    qsignrank(0.5,10)
    qsignrank(0.8,10)
    qsignrank(0.9,10)
    qsignrank(0.99,10)
    qsignrank(1,10)
     */
    @Test
    public void test_0200() {
        WilcoxonSignedRankDistribution dist = new WilcoxonSignedRankDistribution(10);
        assertEquals(0, dist.quantile(0), 1e-15);
        assertEquals(6, dist.quantile(0.01), 1e-15);
        assertEquals(15, dist.quantile(0.1), 1e-15);
        assertEquals(19, dist.quantile(0.2), 1e-15);
        assertEquals(27, dist.quantile(0.5), 1e-15);
        assertEquals(36, dist.quantile(0.8), 1e-15);
        assertEquals(40, dist.quantile(0.9), 1e-15);
        assertEquals(49, dist.quantile(0.99), 1e-15);
        assertEquals(55, dist.quantile(1), 1e-15);
    }
}
