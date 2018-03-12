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
public class GammaDistributionTest {

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0010() {
        GammaDistribution dist = new GammaDistribution(1, 2);

        assertEquals(2d, dist.mean(), 1e-16);
        assertEquals(4d, dist.variance(), 1e-16);
        assertEquals(2d, dist.skew(), 1e-16);
        assertEquals(6d, dist.kurtosis(), 1e-16);
//        assertEquals(1d, dist.entropy(), 1e-16);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(4.99998750002083e-06, dist.cdf(0.00001), 1e-16);//from R
        assertEquals(4.999875002083308e-05, dist.cdf(0.0001), 1e-16);//from R
        assertEquals(0.0004998750208307295, dist.cdf(0.001), 1e-16);//from R
        assertEquals(0.004987520807317687, dist.cdf(0.01), 1e-16);//from R
        assertEquals(0.048770575499286, dist.cdf(0.1), 1e-16);//from R
        assertEquals(0.3934693402873666, dist.cdf(1), 1e-16);//from R
        assertEquals(0.4230501896195134, dist.cdf(1.1), 1e-15);//from R
        assertEquals(0.4511883639059736, dist.cdf(1.2), 1e-15);//from R
        assertEquals(0.477954223238984, dist.cdf(1.3), 1e-15);//from R
        assertEquals(0.5034146962085905, dist.cdf(1.4), 1e-15);//from R
        assertEquals(0.527633447258985, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.5725850680512734, dist.cdf(1.7), 1e-15);//from R
        assertEquals(0.6132589765454987, dist.cdf(1.9), 1e-15);//from R
        assertEquals(0.632120558828558, dist.cdf(2.0), 1e-15);//from R
        assertEquals(0.71349520313981, dist.cdf(2.5), 1e-15);//from R
        assertEquals(0.77686983985157, dist.cdf(3.0), 1e-15);//from R
        assertEquals(0.993262053000915, dist.cdf(10), 1e-15);//from R
        assertEquals(0.9992898256111574, dist.cdf(14.5), 1e-15);//from R
        assertEquals(1, dist.cdf(100), 1e-16);//from R
        assertEquals(1, dist.cdf(1000), 1e-16);//from R
        assertEquals(1, dist.cdf(9999), 1e-16);//from R
        assertEquals(1, dist.cdf(99999999), 1e-16);//from R

        assertEquals(0.5, dist.density(0d), 1e-16);//from R
        assertEquals(0.4876549560141663, dist.density(0.05), 1e-16);//from R
        assertEquals(0.475614712250357, dist.density(0.1), 1e-16);//from R
        assertEquals(0.4524187090179798, dist.density(0.2), 1e-16);//from R
        assertEquals(0.430353988212529, dist.density(0.3), 1e-15);//from R
        assertEquals(0.409365376538991, dist.density(0.4), 1e-15);//from R
        assertEquals(0.3894003915357024, dist.density(0.5), 1e-15);//from R
        assertEquals(0.370409110340859, dist.density(0.6), 1e-15);//from R
        assertEquals(0.3523440448593567, dist.density(0.7), 1e-15);//from R
        assertEquals(0.3351600230178197, dist.density(0.8), 1e-15);//from R
        assertEquals(0.3188140758108867, dist.density(0.9), 1e-15);//from R
        assertEquals(0.3032653298563167, dist.density(1), 1e-15);//from R
        assertEquals(0.2361832763705073, dist.density(1.5), 1e-15);//from R
        assertEquals(0.1839397205857212, dist.density(2), 1e-15);//from R
        assertEquals(0.1432523984300950, dist.density(2.5), 1e-15);//from R
        assertEquals(0.1115650800742149, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0020() {
        GammaDistribution dist = new GammaDistribution(7.123456, 2.987654);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(1.533855009644069e-43, dist.cdf(0.00001), 1e-42);//from R
        assertEquals(2.038115711532956e-36, dist.cdf(0.0001), 1e-35);//from R
        assertEquals(2.707510286344577e-29, dist.cdf(0.001), 1e-28);//from R
        assertEquals(3.58821885948026e-22, dist.cdf(0.01), 1e-21);//from R
        assertEquals(4.64371530280992e-15, dist.cdf(0.1), 1e-16);//from R
        assertEquals(4.741212904445357e-08, dist.cdf(1), 1e-16);//from R
        assertEquals(9.07953499367997e-08, dist.cdf(1.1), 1e-15);//from R
        assertEquals(1.638966274140315e-07, dist.cdf(1.2), 1e-15);//from R
        assertEquals(2.815293794280488e-07, dist.cdf(1.3), 1e-15);//from R
        assertEquals(4.635786361862873e-07, dist.cdf(1.4), 1e-15);//from R
        assertEquals(0.002193367746728515, dist.cdf(5.4), 1e-15);//from R
        assertEquals(0.00704734913677686, dist.cdf(6.7), 1e-15);//from R
        assertEquals(0.04645010754499305, dist.cdf(9.9), 1e-15);//from R
        assertEquals(0.1028147768252625, dist.cdf(12.0), 1e-15);//from R
        assertEquals(0.2497095859232607, dist.cdf(15.5), 1e-15);//from R
        assertEquals(0.862893322181293, dist.cdf(30.0), 1e-15);//from R
        assertEquals(0.94147509424181, dist.cdf(35), 1e-15);//from R
        assertEquals(0.9793683222856, dist.cdf(40.5), 1e-15);//from R
        assertEquals(0.999999991563173, dist.cdf(100), 1e-15);//from R
        assertEquals(1, dist.cdf(1000), 1e-16);//from R
        assertEquals(1, dist.cdf(9999), 1e-16);//from R
        assertEquals(1, dist.cdf(99999999), 1e-16);//from R

        assertEquals(0, dist.density(0d), 1e-16);//from R
        assertEquals(0.001691401702215769, dist.density(5.05), 1e-16);//from R
        assertEquals(0.02175240701228963, dist.density(10.1), 1e-16);//from R
        assertEquals(0.0482175634175375, dist.density(15.2), 1e-15);//from R
        assertEquals(0.05143763366848643, dist.density(20.3), 1e-15);//from R
        assertEquals(0.02075061220787939, dist.density(30.4), 1e-15);//from R
        assertEquals(0.0097302355425827, dist.density(35.5), 1e-15);//from R
        assertEquals(0.001871517849642277, dist.density(44.6), 1e-15);//from R
        assertEquals(0.000646753122424468, dist.density(49.78746532), 1e-15);//from R
        assertEquals(0.0001737545163104003, dist.density(55.8), 1e-15);//from R
        assertEquals(5.384834709102528e-05, dist.density(60.9), 1e-19);//from R
        assertEquals(1.598426275716021e-05, dist.density(66), 1e-19);//from R
        assertEquals(9.05254228335044e-09, dist.density(95), 1e-22);//from R
        assertEquals(2.324683651743346e-09, dist.density(100), 1e-22);//from R
        assertEquals(4.714149228159156e-22, dist.density(200), 1e-33);//from R
        assertEquals(6.394137579557377e-134, dist.density(999), 1e-147);//from R
    }

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0030() {
        GammaDistribution dist = new GammaDistribution(0.0001, 2.987654);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.998797694963191, dist.cdf(0.00001), 1e-12);//from R
        assertEquals(0.99986290009917, dist.cdf(0.5), 1e-12);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0d), 1e-16);//from R
        assertEquals(0.000966810207685967, dist.density(0.1), 1e-15);//from R
    }

    /**
     * Test of class GammaDistribution.
     * Check x = 0.
     */
    @Test
    public void testGamma_0050() {
        assertEquals(Double.POSITIVE_INFINITY, (new GammaDistribution(0.000001, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(Double.POSITIVE_INFINITY, (new GammaDistribution(0.5, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(Double.POSITIVE_INFINITY, (new GammaDistribution(0.99999999999, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0.5, (new GammaDistribution(1.0, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0.5, (new GammaDistribution(1, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0, (new GammaDistribution(1.0000000001, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0, (new GammaDistribution(1.5, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0, (new GammaDistribution(2, 1 / 0.5)).density(0d), 1e-16);
        assertEquals(0, (new GammaDistribution(3.123, 1 / 0.5)).density(0d), 1e-16);
    }

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0060() {
        GammaDistribution dist = new GammaDistribution(3.5, 1.623);

        double x, y;
        for (double u = 0.001; u < 1d; u += 0.001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0070() {
        GammaDistribution dist = new GammaDistribution(0.5, 2.2);

        assertEquals(1.1, dist.mean(), 1e-16);
        assertEquals(2.42, dist.variance(), 1e-15);
        assertEquals(2.82842712474619, dist.skew(), 1e-15);
        assertEquals(12, dist.kurtosis(), 1e-16);
    }

    /**
     * Test of class GammaDistribution.
     */
    @Test
    public void testGamma_0080() {
        GammaDistribution dist = new GammaDistribution(1.5, 2.2);

        assertEquals(3.3, dist.mean(), 1e-15);
        assertEquals(7.26, dist.variance(), 1e-14);
        assertEquals(1.632993161855452, dist.skew(), 1e-15);
        assertEquals(4, dist.kurtosis(), 1e-16);
    }
}
