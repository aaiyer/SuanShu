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
public class ChiSquareDistributionTest {

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0010() {
        ChiSquareDistribution dist = new ChiSquareDistribution(2);

        assertEquals(2d, dist.mean(), 1e-16);
        assertEquals(4d, dist.variance(), 1e-16);
        assertEquals(2d, dist.skew(), 1e-16);
        assertEquals(6d, dist.kurtosis(), 1e-16);

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

        assertEquals(0.5, dist.density(0), 1e-16);//from R
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
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0020() {
        ChiSquareDistribution dist = new ChiSquareDistribution(1.5);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.000115048646502205, dist.cdf(0.00001), 1e-16);//from R
        assertEquals(0.651592519499689, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.8045754113339455, dist.cdf(2.5), 1e-15);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0), 1e-16);//from R
        assertEquals(1.000792045039388, dist.density(0.05), 1e-15);//from R
        assertEquals(0.449394327493127, dist.density(0.5), 1e-15);//from R
        assertEquals(0.2071095054251791, dist.density(1.5), 1e-15);//from R
        assertEquals(0.0822662443601042, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0030() {
        ChiSquareDistribution dist = new ChiSquareDistribution(1.0);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.002523128316805598, dist.cdf(0.00001), 1e-16);//from R
        assertEquals(0.779328638080153, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.886153701993342, dist.cdf(2.5), 1e-15);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0), 1e-16);//from R
        assertEquals(1.740073934772586, dist.density(0.05), 1e-15);//from R
        assertEquals(0.4393912894677224, dist.density(0.5), 1e-15);//from R
        assertEquals(0.1538663228054553, dist.density(1.5), 1e-15);//from R
        assertEquals(0.0513934432679231, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0040() {
        ChiSquareDistribution dist = new ChiSquareDistribution(0.5);

        assertEquals(0, dist.cdf(0), 1e-16);
        assertEquals(0.05217001758066663, dist.cdf(0.00001), 1e-16);//from R
        assertEquals(0.899936513284498, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.952753298856091, dist.cdf(2.5), 1e-15);//from R

        assertEquals(Double.POSITIVE_INFINITY, dist.density(0), 1e-16);//from R
        assertEquals(2.13932398726506, dist.density(0.05), 1e-15);//from R
        assertEquals(0.303780786595025, dist.density(0.5), 1e-15);//from R
        assertEquals(0.08082991466920407, dist.density(1.5), 1e-15);//from R
        assertEquals(0.02270276544056991, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class ChiSquareDistribution.
     * Check x = 0.
     */
    @Test
    public void testChiSquare_0050() {
        assertEquals(Double.POSITIVE_INFINITY, (new ChiSquareDistribution(0.000001)).density(0), 1e-16);
        assertEquals(Double.POSITIVE_INFINITY, (new ChiSquareDistribution(0.5)).density(0), 1e-16);
        assertEquals(Double.POSITIVE_INFINITY, (new ChiSquareDistribution(1.0)).density(0), 1e-16);
        assertEquals(Double.POSITIVE_INFINITY, (new ChiSquareDistribution(1.5)).density(0), 1e-16);
        assertEquals(0.5, (new ChiSquareDistribution(2)).density(0), 1e-16);
        assertEquals(0, (new ChiSquareDistribution(2.0000000001)).density(0), 1e-16);
        assertEquals(0, (new ChiSquareDistribution(2.5)).density(0), 1e-16);
        assertEquals(0, (new ChiSquareDistribution(3.123)).density(0), 1e-16);
    }

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0060() {
        ChiSquareDistribution dist = new ChiSquareDistribution(0.5);

        assertEquals(0.5, dist.mean(), 1e-16);
        assertEquals(1, dist.variance(), 1e-16);
        assertEquals(4d, dist.skew(), 1e-16);
        assertEquals(24, dist.kurtosis(), 1e-16);
    }

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0070() {
        ChiSquareDistribution dist = new ChiSquareDistribution(1);

        assertEquals(1, dist.mean(), 1e-16);
        assertEquals(2, dist.variance(), 1e-16);
        assertEquals(2.82842712474619, dist.skew(), 1e-14);
        assertEquals(12, dist.kurtosis(), 1e-16);
    }

    /**
     * Test of class ChiSquareDistribution.
     */
    @Test
    public void testChiSquare_0080() {
        ChiSquareDistribution dist = new ChiSquareDistribution(3.5);

        double x, y;
        for (double u = 0.001; u < 1d; u += 0.001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }
}
