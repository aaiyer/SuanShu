/*
 * Copyright (c)
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
public class FDistributionTest {

    /**
     * Test of class FDistribution.
     */
    @Test
    public void testF_0010() {
        FDistribution dist = new FDistribution(9.5, 12.5);

        assertEquals(1.190476190476190, dist.mean(), 1e-15);
        assertEquals(0.7020352000449303, dist.variance(), 1e-16);
        assertEquals(2.715103035027001, dist.skew(), 1e-15);
        assertEquals(15.638866396761134, dist.kurtosis(), 1e-16);
    }

    /**
     * Test of class FDistribution.
     */
    @Test
    public void testF_0020() {
        FDistribution dist = new FDistribution(1, 1);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.002013161773658131, dist.cdf(0.00001), 1e-17);//from R
        assertEquals(0.00636598552981651, dist.cdf(0.0001), 1e-16);//from R
        assertEquals(0.02012497830364413, dist.cdf(0.001), 1e-16);//from R
        assertEquals(0.0634510348611071, dist.cdf(0.01), 1e-16);//from R
        assertEquals(0.1949822290421367, dist.cdf(0.1), 1e-15);//from R
        assertEquals(0.4999999999999997, dist.cdf(1), 1e-15);//from R
        assertEquals(0.515163347982104, dist.cdf(1.1), 1e-15);//from R
        assertEquals(0.5289772698358565, dist.cdf(1.2), 1e-15);//from R
        assertEquals(0.5416373191884818, dist.cdf(1.3), 1e-15);//from R
        assertEquals(0.5533003790381138, dist.cdf(1.4), 1e-15);//from R
        assertEquals(0.564094216848975, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.583478409772886, dist.cdf(1.7), 1e-15);//from R
        assertEquals(0.60044451458014, dist.cdf(1.9), 1e-15);//from R
        assertEquals(0.608173447969393, dist.cdf(2.0), 1e-15);//from R
        assertEquals(0.640982964028624, dist.cdf(2.5), 1e-15);//from R
        assertEquals(0.666666666666667, dist.cdf(3.0), 1e-15);//from R
        assertEquals(0.8050177709578633, dist.cdf(10), 1e-15);//from R
        assertEquals(0.836507182350739, dist.cdf(14.5), 1e-15);//from R
        assertEquals(0.936548965138893, dist.cdf(100), 1e-15);//from R
        assertEquals(0.979875021696356, dist.cdf(1000), 1e-15);//from R
        assertEquals(0.993633696168254, dist.cdf(9999), 1e-15);//from R
        assertEquals(0.999936338022657, dist.cdf(99999999), 1e-11);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0d), 1e-16);//from R
        assertEquals(1.35573817793748, dist.density(0.05), 1e-15);//from R
        assertEquals(0.915076583717946, dist.density(0.1), 1e-15);//from R
        assertEquals(0.5931354528476476, dist.density(0.2), 1e-15);//from R
        assertEquals(0.4470397562558056, dist.density(0.3), 1e-15);//from R
        assertEquals(0.3594943721749074, dist.density(0.4), 1e-15);//from R
        assertEquals(0.3001054387190354, dist.density(0.5), 1e-15);//from R
        assertEquals(0.256835185025625, dist.density(0.6), 1e-15);//from R
        assertEquals(0.2237959309416713, dist.density(0.7), 1e-15);//from R
        assertEquals(0.1977118176158825, dist.density(0.8), 1e-15);//from R
        assertEquals(0.1765937266824107, dist.density(0.9), 1e-15);//from R
        assertEquals(0.1591549430918953, dist.density(1), 1e-15);//from R
        assertEquals(0.1039595734978235, dist.density(1.5), 1e-15);//from R
        assertEquals(0.07502635967975885, dist.density(2), 1e-15);//from R
        assertEquals(0.0575190995479852, dist.density(2.5), 1e-15);//from R
        assertEquals(0.04594407461848267, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class FDistribution.
     */
    @Test
    public void testF_0030() {
        FDistribution dist = new FDistribution(100.5, 1.5);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(9.57936482888353e-161, dist.cdf(0.00001), 1e-171);//from R
        assertEquals(1.223261814147995e-61, dist.cdf(0.001), 1e-71);//from R
        assertEquals(4.061243663127156e-21, dist.cdf(0.01), 1e-31);//from R
        assertEquals(0.000455803163519936, dist.cdf(0.1), 1e-15);//from R
        assertEquals(0.351482092056715, dist.cdf(1), 1e-13);//from R
        assertEquals(0.3808402150823796, dist.cdf(1.1), 1e-13);//from R
        assertEquals(0.4074839308003405, dist.cdf(1.2), 1e-13);//from R
        assertEquals(0.4317460121966527, dist.cdf(1.3), 1e-13);//from R
        assertEquals(0.4539173480628874, dist.cdf(1.4), 1e-13);//from R
        assertEquals(0.4742492841762894, dist.cdf(1.5), 1e-13);//from R
        assertEquals(0.5102291137878144, dist.cdf(1.7), 1e-13);//from R
        assertEquals(0.5410756867573907, dist.cdf(1.9), 1e-13);//from R
        assertEquals(0.5549079642338917, dist.cdf(2.0), 1e-13);//from R
        assertEquals(0.6119472124059623, dist.cdf(2.5), 1e-13);//from R
        assertEquals(0.65455399376332, dist.cdf(3.0), 1e-13);//from R
        assertEquals(0.849307177252634, dist.cdf(10), 1e-13);//from R
        assertEquals(0.88481353519825, dist.cdf(14.5), 1e-13);//from R
        assertEquals(0.9724116074274877, dist.cdf(100), 1e-13);//from R
        assertEquals(0.995079600959545, dist.cdf(1000), 1e-13);//from R
        assertEquals(0.999124693011295, dist.cdf(9999), 1e-13);//from R
        assertEquals(0.999999124730101, dist.cdf(99999999), 1e-11);//from R

        assertEquals(0, dist.density(0d), 1e-16);//from R
        assertEquals(0.000203394311000109, dist.density(0.05), 1e-15);//from R
        assertEquals(0.03061901452218088, dist.density(0.1), 1e-14);//from R
        assertEquals(0.2794205750766580, dist.density(0.2), 1e-14);//from R
        assertEquals(0.453768941814001, dist.density(0.3), 1e-13);//from R
        assertEquals(0.50371881571631, dist.density(0.4), 1e-13);//from R
        assertEquals(0.492625691346621, dist.density(0.5), 1e-13);//from R
        assertEquals(0.458362686558962, dist.density(0.6), 1e-13);//from R
        assertEquals(0.4178163964742959, dist.density(0.7), 1e-13);//from R
        assertEquals(0.3778985921413494, dist.density(0.8), 1e-13);//from R
        assertEquals(0.3411752999152102, dist.density(0.9), 1e-13);//from R
        assertEquals(0.308363981513069, dist.density(1), 1e-13);//from R
        assertEquals(0.1948698030966134, dist.density(1.5), 1e-13);//from R
        assertEquals(0.1335744165137994, dist.density(2), 1e-13);//from R
        assertEquals(0.0974918819783367, dist.density(2.5), 1e-13);//from R
        assertEquals(0.07452811649695643, dist.density(3), 1e-13);//from R
    }

    /**
     * Test of class FDistribution.
     */
    @Test
    public void testF_0040() {
        FDistribution dist = new FDistribution(3.5, 1.623);
        double u = 0.5;
        double x = dist.quantile(u);
        double y = dist.cdf(x);
        assertEquals(u, y, 1e-14);
    }

    /**
     * Test of class FDistribution.
     */
    @Test
    public void testF_0050() {
        FDistribution dist = new FDistribution(3.5, 1.623);

        double x, y;
        for (double u = 0.001; u < 1d; u += 0.001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }
}
