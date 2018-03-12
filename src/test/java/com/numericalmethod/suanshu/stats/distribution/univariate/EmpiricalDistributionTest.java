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
package com.numericalmethod.suanshu.stats.distribution.univariate;

import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class EmpiricalDistributionTest {

    @Test
    public void testEmpirical_0010() {
        EmpiricalDistribution dist = new EmpiricalDistribution(new double[]{
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 99
                });

        assertEquals(13.5, dist.mean(), 1e-16);
        assertEquals(909.1666666666666667, dist.variance(), 1e-16);
        assertEquals(2.2456049365721427, dist.skew(), 1e-16);
        assertEquals(3.47801757904226, dist.kurtosis(), 1e-14);

        assertEquals(0.1, dist.cdf(0), 1e-16);
        assertEquals(0.1, dist.cdf(0.00001), 1e-16);
        assertEquals(0.1, dist.cdf(0.0001), 1e-16);
        assertEquals(0.1, dist.cdf(0.001), 1e-16);
        assertEquals(0.1, dist.cdf(0.01), 1e-16);
        assertEquals(0.2, dist.cdf(1), 1e-15);
        assertEquals(0.3, dist.cdf(2), 1e-15);
        assertEquals(0.4, dist.cdf(3), 1e-15);
        assertEquals(0.5, dist.cdf(4), 1e-15);
        assertEquals(0.6, dist.cdf(5), 1e-15);
        assertEquals(0.7, dist.cdf(6), 1e-15);
        assertEquals(0.8, dist.cdf(7), 1e-15);
        assertEquals(0.9, dist.cdf(8), 1e-15);
        assertEquals(0.9, dist.cdf(9), 1e-15);
        assertEquals(1, dist.cdf(999999), 1e-15);

        assertEquals(0.1, dist.density(0), 1e-16);
        assertEquals(0, dist.density(0.5), 1e-14);
        assertEquals(0.1, dist.density(1), 1e-14);
        assertEquals(0.1, dist.density(2), 1e-14);
        assertEquals(0.1, dist.density(3), 1e-14);
        assertEquals(0.1, dist.density(4), 1e-14);
        assertEquals(0.1, dist.density(5), 1e-14);
        assertEquals(0.1, dist.density(6), 1e-14);
        assertEquals(0.1, dist.density(7), 1e-14);
        assertEquals(0.1, dist.density(8), 1e-14);
        assertEquals(0, dist.density(8.01), 1e-18);
        assertEquals(0.0, dist.density(9), 1e-14);

    }

    @Test
    public void testEmpirical_0020() {
        EmpiricalDistribution dist = new EmpiricalDistribution(new double[]{
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 99
                });

        double x, y;
        for (double u = 0.1; u < 1d; u += 0.1) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }

    @Test
    public void testEmpirical_0030() {
        EmpiricalDistribution dist = new EmpiricalDistribution(new double[]{
                    0, 1, 2, 3, 3, 3, 6, 7, 8, 9
                },
                Quantile.QuantileType.INVERSE_OF_EMPIRICAL_CDF);

        assertEquals(0.1, dist.cdf(0), 1e-14);
        assertEquals(0.2, dist.cdf(1), 1e-14);
        assertEquals(0.3, dist.cdf(2), 1e-14);
        assertEquals(0.6, dist.cdf(3), 1e-14);//discontinuity
        assertEquals(0.7, dist.cdf(6), 1e-14);
        assertEquals(0.8, dist.cdf(7), 1e-14);
        assertEquals(0.9, dist.cdf(8), 1e-14);
        assertEquals(1.0, dist.cdf(9), 1e-14);

        assertEquals(0.1, dist.density(0), 1e-16);
        assertEquals(0.1, dist.density(1d), 1e-16);
        assertEquals(0.1, dist.density(2d), 1e-16);
        assertEquals(0.3, dist.density(3d), 1e-16);
        assertEquals(0, dist.density(4d), 1e-16);
        assertEquals(0, dist.density(5d), 1e-16);
        assertEquals(0.1, dist.density(6d), 1e-16);
        assertEquals(0.1, dist.density(7d), 1e-16);
        assertEquals(0.1, dist.density(8d), 1e-16);
        assertEquals(0.1, dist.density(9d), 1e-16);

//        double x, y;
//        for (double u = 0.1; u < 1d; u += 0.1) {
//            x = dist.quantile(u);
//            y = dist.cdf(x);
//            assertEquals(u, y, 1e-14);
//        }
    }

    @Test
    public void test_quantileAtZeroAndOne_0010() {
        for (Quantile.QuantileType type : Quantile.QuantileType.values()) { // this should be true for any quantile type
            EmpiricalDistribution dist = new EmpiricalDistribution(new double[]{
                        2, 0, 8, 9, 1, 3, 6, 5, 7, 4
                    }, type);

            assertEquals("quantile(0) = min", 0., dist.quantile(0), 0.);
            assertEquals("quantile(1) = max", 9., dist.quantile(1), 0.);
        }
    }
}
