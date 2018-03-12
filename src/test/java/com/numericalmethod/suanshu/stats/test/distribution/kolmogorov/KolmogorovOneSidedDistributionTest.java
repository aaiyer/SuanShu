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
package com.numericalmethod.suanshu.stats.test.distribution.kolmogorov;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class KolmogorovOneSidedDistributionTest {

    /**
     * Test of class KolmogorovOneSidedDistribution.
     * n = 5
     */
    @Test
    public void test_0010() {
        assertEquals(0.929435998960737, (new KolmogorovOneSidedDistribution(5)).cdf(0.4799), 1e-15);
        assertEquals(0.327821329325613, (new KolmogorovOneSidedDistribution(5)).cdf(0.1731), 1e-15);
        assertEquals(0.4506873779950269, (new KolmogorovOneSidedDistribution(5)).cdf(0.2146), 1e-15);
        assertEquals(0.569225403376663, (new KolmogorovOneSidedDistribution(5)).cdf(0.2628), 1e-15);
        assertEquals(1 - 0.100, (new KolmogorovOneSidedDistribution(5)).cdf(0.4799), 1e-1);
        assertEquals(1 - 0.050, (new KolmogorovOneSidedDistribution(5)).cdf(0.5473), 1e-1);
    }

    /**
     * Test of class KolmogorovOneSidedDistribution.
     * n = 20
     */
    @Test
    public void test_0020() {
        assertEquals(0.915242458110528, (new KolmogorovOneSidedDistribution(20)).cdf(0.2399), 1e-15);
        assertEquals(0.959216189863297, (new KolmogorovOneSidedDistribution(20)).cdf(0.2737), 1e-15);
        assertEquals(1 - 0.0100, (new KolmogorovOneSidedDistribution(20)).cdf(0.3393), 1e-2);
        assertEquals(1 - 0.0010, (new KolmogorovOneSidedDistribution(20)).cdf(0.4156), 1e-2);
    }

    /**
     * Test of class KolmogorovOneSidedDistribution.
     * n >= 50
     * From Table 2 in Z. W. Birnbaum and Fred H. Tingey (1951).
     */
    @Test
    public void test_0030() {
        assertEquals(1 - 0.100, (new KolmogorovOneSidedDistribution(50)).cdf(0.1517), 1e-2);//1-alpha
        assertEquals(1 - 0.050, (new KolmogorovOneSidedDistribution(50)).cdf(0.1731), 1e-2);
        assertEquals(1 - 0.010, (new KolmogorovOneSidedDistribution(50)).cdf(0.2146), 1e-2);
        assertEquals(1 - 0.001, (new KolmogorovOneSidedDistribution(50)).cdf(0.2628), 1e-2);
    }

    /**
     * Test of class KolmogorovOneSidedDistribution.
     * n >= 50
     */
    @Test
    public void test_0040() {
        KolmogorovOneSidedDistribution dist = new KolmogorovOneSidedDistribution(100);

        for (double q = 0.01; q <= 1; q += 0.01) {
            assertEquals(q, dist.cdf(dist.quantile(q)), 1e-15);
        }
    }

    /**
     * Test of class KolmogorovOneSidedDistribution.
     * n < 50
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_0050() {
        KolmogorovOneSidedDistribution dist = new KolmogorovOneSidedDistribution(10);

        assertEquals(0.1, dist.cdf(dist.quantile(0.1)), 1e-15);
    }
}
