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
public class NormalDistributionTest {

    /**
     * Test of class NormalDistribution.
     */
    @Test
    public void testNormal_0010() {
        NormalDistribution N = new NormalDistribution();

        assertEquals(0d, N.mean(), 0);
        assertEquals(1d, N.variance(), 0);
        assertEquals(0d, N.skew(), 0);
        assertEquals(0d, N.kurtosis(), 0);
        assertEquals(Math.log(2 * Math.PI * Math.E), N.entropy(), 1e-15);

        assertEquals(0, N.cdf(-99999999), 0);
        assertEquals(0, N.cdf(-9999), 0);
        assertEquals(0, N.cdf(-1000), 0);
        assertEquals(0, N.cdf(-100), 0);
        assertEquals(6.05749476441522e-48, N.cdf(-14.5), 1e-60);//from R
        assertEquals(7.61985302416053e-24, N.cdf(-10), 1e-37);//from R
        assertEquals(1.12858840595384e-19, N.cdf(-9), 1e-32);//from R
        assertEquals(6.22096057427178e-16, N.cdf(-8), 1e-29);//from R
        assertEquals(1.27981254388584e-12, N.cdf(-7), 1e-25);//from R
        assertEquals(9.86587645037698e-10, N.cdf(-6), 1e-23);//from R
        assertEquals(2.86651571879194e-07, N.cdf(-5), 1e-20);//from R
        assertEquals(3.16712418331199e-05, N.cdf(-4), 1e-18);//from R
        assertEquals(0.00134989803163009, N.cdf(-3), 1e-17);//from R
        assertEquals(0.00620966532577613, N.cdf(-2.5), 1e-17);//from R
        assertEquals(0.0227501319481792, N.cdf(-2), 1e-17);//from R
        assertEquals(0.0287165598160018, N.cdf(-1.9), 1e-17);//from R
        assertEquals(0.0445654627585430, N.cdf(-1.7), 1e-16);//from R
        assertEquals(0.066807201268858, N.cdf(-1.5), 1e-16);//from R
        assertEquals(0.080756659233771, N.cdf(-1.4), 1e-16);//from R
        assertEquals(0.0968004845856103, N.cdf(-1.3), 1e-16);//from R
        assertEquals(0.115069670221708, N.cdf(-1.2), 1e-15);//from R
        assertEquals(0.135666060946383, N.cdf(-1.1), 1e-15);//from R
        assertEquals(0.158655253931457, N.cdf(-1), 1e-15);//from R
        assertEquals(0.460172162722971, N.cdf(-0.1), 1e-15);//from R
        assertEquals(0.496010643685368, N.cdf(-0.01), 1e-15);//from R
        assertEquals(0.499601057786089, N.cdf(-0.001), 1e-15);//from R
        assertEquals(0.499960105772026, N.cdf(-0.0001), 1e-15);//from R
        assertEquals(0.499996010577196, N.cdf(-0.00001), 1e-15);//from R
        assertEquals(0.5, N.cdf(0), 1e-15);//from R
        assertEquals(0.500003989422804, N.cdf(0.00001), 1e-15);//from R
        assertEquals(0.500039894227974, N.cdf(0.0001), 1e-15);//from R
        assertEquals(0.500398942213911, N.cdf(0.001), 1e-15);//from R
        assertEquals(0.503989356314632, N.cdf(0.01), 1e-15);//from R
        assertEquals(0.539827837277029, N.cdf(0.1), 1e-15);//from R
        assertEquals(0.841344746068543, N.cdf(1), 1e-15);//from R
        assertEquals(0.864333939053617, N.cdf(1.1), 1e-15);//from R
        assertEquals(0.884930329778292, N.cdf(1.2), 1e-15);//from R
        assertEquals(0.90319951541439, N.cdf(1.3), 1e-15);//from R
        assertEquals(0.919243340766229, N.cdf(1.4), 1e-15);//from R
        assertEquals(0.933192798731142, N.cdf(1.5), 1e-15);//from R
        assertEquals(0.955434537241457, N.cdf(1.7), 1e-15);//from R
        assertEquals(0.971283440183998, N.cdf(1.9), 1e-15);//from R
        assertEquals(0.977249868051821, N.cdf(2.0), 1e-15);//from R
        assertEquals(0.993790334674224, N.cdf(2.5), 1e-15);//from R
        assertEquals(0.99865010196837, N.cdf(3.0), 1e-15);//from R
        assertEquals(1, N.cdf(10), 0);//from R
        assertEquals(1, N.cdf(14.5), 0);//from R
        assertEquals(1, N.cdf(100), 0);//from R
        assertEquals(1, N.cdf(1000), 0);//from R
        assertEquals(1, N.cdf(9999), 0);//from R
        assertEquals(1, N.cdf(99999999), 0);//from R

        assertEquals(0.00443184841193801, N.density(-3), 1e-15);//from R
        assertEquals(0.0175283004935685, N.density(-2.5), 1e-15);//from R
        assertEquals(0.053990966513188, N.density(-2), 1e-15);//from R
        assertEquals(0.129517595665892, N.density(-1.5), 1e-15);//from R
        assertEquals(0.241970724519143, N.density(-1), 1e-15);//from R
        assertEquals(0.266085249898755, N.density(-0.9), 1e-15);//from R
        assertEquals(0.289691552761483, N.density(-0.8), 1e-15);//from R
        assertEquals(0.312253933366761, N.density(-0.7), 1e-15);//from R
        assertEquals(0.333224602891800, N.density(-0.6), 1e-15);//from R
        assertEquals(0.352065326764300, N.density(-0.5), 1e-15);//from R
        assertEquals(0.368270140303323, N.density(-0.4), 1e-15);//from R
        assertEquals(0.381387815460524, N.density(-0.3), 1e-15);//from R
        assertEquals(0.391042693975456, N.density(-0.2), 1e-15);//from R
        assertEquals(0.396952547477012, N.density(-0.1), 1e-15);//from R
        assertEquals(0.398443914094764, N.density(-0.05), 1e-15);//from R
        assertEquals(0.398942280401433, N.density(0d), 1e-15);//from R
        assertEquals(0.398443914094764, N.density(0.05), 1e-15);//from R
        assertEquals(0.396952547477012, N.density(0.1), 1e-15);//from R
        assertEquals(0.391042693975456, N.density(0.2), 1e-15);//from R
        assertEquals(0.381387815460524, N.density(0.3), 1e-15);//from R
        assertEquals(0.368270140303323, N.density(0.4), 1e-15);//from R
        assertEquals(0.352065326764300, N.density(0.5), 1e-15);//from R
        assertEquals(0.333224602891800, N.density(0.6), 1e-15);//from R
        assertEquals(0.312253933366761, N.density(0.7), 1e-15);//from R
        assertEquals(0.289691552761483, N.density(0.8), 1e-15);//from R
        assertEquals(0.266085249898755, N.density(0.9), 1e-15);//from R
        assertEquals(0.241970724519143, N.density(1), 1e-15);//from R
        assertEquals(0.129517595665892, N.density(1.5), 1e-15);//from R
        assertEquals(0.053990966513188, N.density(2), 1e-15);//from R
        assertEquals(0.0175283004935685, N.density(2.5), 1e-15);//from R
        assertEquals(0.00443184841193801, N.density(3), 1e-15);//from R
    }

    /**
     * Test of class NormalDistribution.
     */
    @Test
    public void testNormal_0020() {
        NormalDistribution N = new NormalDistribution();

        double x, y;
        for (double u = 0.000001; u < 1d; u += 0.000001) {
            x = N.quantile(u);
            y = N.cdf(x);
            assertEquals(u, y, 1e-9);
        }
    }

    /**
     * Test of class NormalDistribution.
     */
    @Test
    public void testNormal_0030() {
        //TODO: test for moment()
        assertTrue(true);
    }

    /**
     * Test of class NormalDistribution.
     */
    @Test
    public void testNormal_0040() {
        //TODO: test for non-standard NormalDistribution with mu and sigma
        assertTrue(true);
    }
}
