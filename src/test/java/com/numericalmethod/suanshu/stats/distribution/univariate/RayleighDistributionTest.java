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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RayleighDistributionTest {

    /**
     * Test of class RayleighDistribution.
     */
    @Test
    public void test_Rayleigh_0010() {
        double root2 = Math.sqrt(2);

        RayleighDistribution dist = new RayleighDistribution(2);

        assertEquals(0, dist.cdf(0), 0);//from R
        assertEquals(2.49999999996875e-11, dist.cdf(root2 * 0.00001), 1e-11);//from R
        assertEquals(2.499999996875e-09, dist.cdf(root2 * 0.0001), 1e-16);//from R
        assertEquals(2.499999687500026e-07, dist.cdf(root2 * 0.001), 1e-13);//from R
        assertEquals(2.499968750260415e-05, dist.cdf(root2 * 0.01), 1e-13);//from R
        assertEquals(0.002496877602539876, dist.cdf(root2 * 0.1), 1e-15);//from R
        assertEquals(0.221199216928595, dist.cdf(root2 * 1), 1e-15);//from R
        assertEquals(0.2610315117410558, dist.cdf(root2 * 1.1), 1e-16);//from R
        assertEquals(0.302323673928969, dist.cdf(root2 * 1.2), 1e-16);//from R
        assertEquals(0.3445937456731595, dist.cdf(root2 * 1.3), 1e-17);//from R
        assertEquals(0.387373605815584, dist.cdf(root2 * 1.4), 1e-14);//from R
        assertEquals(0.430217175269077, dist.cdf(root2 * 1.5), 1e-14);//from R
        assertEquals(0.5144631048459205, dist.cdf(root2 * 1.7), 1e-15);//from R
        assertEquals(0.5944454949366794, dist.cdf(root2 * 1.9), 1e-15);//from R
        assertEquals(0.632120558828558, dist.cdf(root2 * 2.0), 1e-13);//from R
        assertEquals(0.790388612848902, dist.cdf(root2 * 2.5), 1e-13);//from R
        assertEquals(0.894600775438136, dist.cdf(root2 * 3.0), 1e-13);//from R
        assertEquals(0.999999999986112, dist.cdf(root2 * 10), 1e-13);//from R
        assertEquals(1, dist.cdf(root2 * 14.5), 0);//from R
        assertEquals(1, dist.cdf(root2 * 100), 0);//from R
        assertEquals(1, dist.cdf(root2 * 1000), 0);//from R
        assertEquals(1, dist.cdf(root2 * 9999), 0);//from R
        assertEquals(1, dist.cdf(root2 * 99999999), 0);//from R

        assertEquals(0, dist.density(0d), 0);//from R
        assertEquals(0.0249843798817954 / root2, dist.density(root2 * 0.05), 1e-15);//from R
        assertEquals(0.049875156119873 / root2, dist.density(root2 * 0.1), 1e-14);//from R
        assertEquals(0.09900498337491681 / root2, dist.density(root2 * 0.2), 1e-16);//from R
        assertEquals(0.1466626855790004 / root2, dist.density(root2 * 0.3), 1e-15);//from R
        assertEquals(0.1921578878304646 / root2, dist.density(root2 * 0.4), 1e-15);//from R
        assertEquals(0.234853265703369 / root2, dist.density(root2 * 0.5), 1e-15);//from R
        assertEquals(0.2741793555813685 / root2, dist.density(root2 * 0.6), 1e-16);//from R
        assertEquals(0.3096470667302192 / root2, dist.density(root2 * 0.7), 1e-16);//from R
        assertEquals(0.3408575155864845 / root2, dist.density(root2 * 0.8), 1e-16);//from R
        assertEquals(0.36750891716915 / root2, dist.density(root2 * 0.9), 1e-14);//from R
        assertEquals(0.3894003915357024 / root2, dist.density(root2), 1e-16);//from R
        assertEquals(0.427337118548192 / root2, dist.density(root2 * 1.5), 1e-15);//from R
        assertEquals(0.3678794411714423 / root2, dist.density(root2 * 2), 1e-16);//from R
        assertEquals(0.2620142339388723 / root2, dist.density(root2 * 2.5), 1e-16);//from R
        assertEquals(0.1580988368427965 / root2, dist.density(root2 * 3), 1e-16);//from R
    }

    /**
     * Test of class RayleighDistribution.
     */
    @Test
    public void testRayleigh_0020() {
        RayleighDistribution dist = new RayleighDistribution(3.1415);

        double x, y;
        for (double u = 0.000001; u < 1d; u += 0.000001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-15);
        }
    }
}
