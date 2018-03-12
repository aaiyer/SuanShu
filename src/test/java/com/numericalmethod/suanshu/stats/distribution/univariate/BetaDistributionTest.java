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
public class BetaDistributionTest {

    /**
     * Test of class BetaDistribution.
     */
    @Test
    public void testBeta_0010() {
        BetaDistribution dist = new BetaDistribution(0.5, 1.5);

        assertEquals(0.25, dist.mean(), 1e-16);
        assertEquals(0.0625, dist.variance(), 1e-16);
        assertEquals(1, dist.skew(), 1e-16);
        assertEquals(0, dist.kurtosis(), 1e-16);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.00402633025778728, dist.cdf(0.00001), 1e-16);//from R
        assertEquals(0.01273218323757763, dist.cdf(0.0001), 1e-16);//from R
        assertEquals(0.04025665811503178, dist.cdf(0.001), 1e-16);//from R
        assertEquals(0.1271114284304618, dist.cdf(0.01), 1e-16);//from R
        assertEquals(0.395818696409408, dist.cdf(0.1), 1e-15);//from R
        assertEquals(0.549815144247899, dist.cdf(0.2), 1e-15);//from R
        assertEquals(0.660745949143546, dist.cdf(0.3), 1e-15);//from R
        assertEquals(0.747784503644496, dist.cdf(0.4), 1e-15);//from R
        assertEquals(0.818309886183791, dist.cdf(0.5), 1e-15);//from R
        assertEquals(0.875972937342445, dist.cdf(0.6), 1e-15);//from R
        assertEquals(0.922725710012454, dist.cdf(0.7), 1e-15);//from R
        assertEquals(0.959480673646166, dist.cdf(0.8), 1e-15);//from R
        assertEquals(0.986153167011141, dist.cdf(0.9), 1e-15);//from R
        assertEquals(0.999999999575587, dist.cdf(0.999999), 1e-15);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0), 1e-16);//from R
        assertEquals(2.774961253210154, dist.density(0.05), 1e-14);//from R
        assertEquals(1.909859317102744, dist.density(0.1), 1e-14);//from R
        assertEquals(1.273239544735163, dist.density(0.2), 1e-14);//from R
        assertEquals(0.972452765259999, dist.density(0.3), 1e-14);//from R
        assertEquals(0.779696801233676, dist.density(0.4), 1e-14);//from R
        assertEquals(0.636619772367581, dist.density(0.5), 1e-14);//from R
        assertEquals(0.5197978674891174, dist.density(0.6), 1e-14);//from R
        assertEquals(0.416765470825714, dist.density(0.7), 1e-14);//from R
        assertEquals(0.3183098861837906, dist.density(0.8), 1e-14);//from R
        assertEquals(0.2122065907891937, dist.density(0.9), 1e-14);//from R
        assertEquals(0.0006366200906868593, dist.density(0.999999), 1e-18);//from R
    }

    /**
     * Test of class BetaDistribution.
     */
    @Test
    public void testBeta_0020() {
        BetaDistribution dist = new BetaDistribution(10.5, 99.5);

        assertEquals(0.09545454545454546, dist.mean(), 1e-16);
        assertEquals(7.778646415010052E-4, dist.variance(), 1e-16);
        assertEquals(0.5180328244786744, dist.skew(), 1e-16);
        assertEquals(0.3458773912909318, dist.kurtosis(), 1e-16);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(4.092331091278184e-39, dist.cdf(0.00001), 1e-51);//from R
        assertEquals(1.283676177641933e-28, dist.cdf(0.0001), 1e-40);//from R
        assertEquals(3.743679371319493e-18, dist.cdf(0.001), 1e-30);//from R
        assertEquals(5.26489993691123e-08, dist.cdf(0.01), 1e-20);//from R
        assertEquals(0.597585033690877, dist.cdf(0.1), 1e-13);//from R
        assertEquals(0.9988926739763537, dist.cdf(0.2), 1e-13);//from R
        assertEquals(0.99999992752212, dist.cdf(0.3), 1e-13);//from R
        assertEquals(0.999999999999778, dist.cdf(0.4), 1e-13);//from R
        assertEquals(1, dist.cdf(0.5), 1e-15);//from R
        assertEquals(1, dist.cdf(0.6), 1e-15);//from R
        assertEquals(1, dist.cdf(0.7), 1e-15);//from R
        assertEquals(1, dist.cdf(0.8), 1e-15);//from R
        assertEquals(1, dist.cdf(0.9), 1e-15);//from R
        assertEquals(1, dist.cdf(0.999999), 1e-15);//from R

        assertEquals(0, dist.density(0), 1e-16);//from R
        assertEquals(3.79787622444234, dist.density(0.05), 1e-12);//from R
        assertEquals(13.37943654913825, dist.density(0.1), 1e-11);//from R
        assertEquals(0.08865420140180569, dist.density(0.2), 1e-12);//from R
        assertEquals(8.097659400592775e-06, dist.density(0.3), 1e-17);//from R
        assertEquals(3.16970412416646e-11, dist.density(0.4), 1e-22);//from R
        assertEquals(4.19093681110904e-18, dist.density(0.5), 1e-29);//from R
        assertEquals(6.743675573430544e-27, dist.density(0.6), 1e-38);//from R
        assertEquals(1.440209311710196e-38, dist.density(0.7), 1e-49);//from R
        assertEquals(2.3139826260561e-55, dist.density(0.8), 1e-67);//from R
        assertEquals(1.580718311035701e-84, dist.density(0.9), 1e-95);//from R
        assertEquals(0, dist.density(0.999999), 1e-18);//from R
    }

    /**
     * Test of class BetaDistribution.
     */
    @Test
    public void testBeta_0030() {
        BetaDistribution dist = new BetaDistribution(3.5, 1.623);

        double x, y;
        for (double u = 0.001; u < 1d; u += 0.001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }
}
