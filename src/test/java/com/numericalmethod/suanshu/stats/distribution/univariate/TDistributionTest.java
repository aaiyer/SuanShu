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
public class TDistributionTest {

    /**
     * Test of class TDistribution.
     */
    @Test
    public void testBeta_0010() {
        TDistribution dist = new TDistribution(4.5);

        assertEquals(0, dist.mean(), 1e-16);
        assertEquals(1.8, dist.variance(), 1e-16);
        assertEquals(0, dist.skew(), 1e-16);
        assertEquals(12, dist.kurtosis(), 1e-16);


        assertEquals(0.0171904339443798, dist.cdf(-3), 1e-15);//from R
        assertEquals(0.02995284325110026, dist.cdf(-2.5), 1e-15);//from R
        assertEquals(0.0541289535905625, dist.cdf(-2), 1e-15);//from R
        assertEquals(0.10010954282807667, dist.cdf(-1.5), 1e-15);//from R
        assertEquals(0.2068435923611011, dist.cdf(-0.9), 1e-15);//from R
        assertEquals(0.2319131437422617, dist.cdf(-0.8), 1e-15);//from R
        assertEquals(0.259219801155809, dist.cdf(-0.7), 1e-15);//from R
        assertEquals(0.288713035824106, dist.cdf(-0.6), 1e-15);//from R
        assertEquals(0.3202747510370236, dist.cdf(-0.5), 1e-15);//from R
        assertEquals(0.3537140940655575, dist.cdf(-0.4), 1e-15);//from R
        assertEquals(0.3887667735197156, dist.cdf(-0.3), 1e-15);//from R
        assertEquals(0.425099805947531, dist.cdf(-0.2), 1e-15);//from R
        assertEquals(0.462322033815343, dist.cdf(-0.1), 1e-15);//from R
        assertEquals(0.4962246086974444, dist.cdf(-0.01), 1e-15);//from R
        assertEquals(0.499622453256076, dist.cdf(-0.001), 1e-15);//from R
        assertEquals(0.4999622453179937, dist.cdf(-0.0001), 1e-15);//from R
        assertEquals(0.499996224531792, dist.cdf(-0.00001), 1e-15);//from R
        assertEquals(0.5, dist.cdf(0), 1e-15);
        assertEquals(0.500003775468208, dist.cdf(0.00001), 1e-15);//from R
        assertEquals(0.5000377546820063, dist.cdf(0.0001), 1e-15);//from R
        assertEquals(0.500377546743924, dist.cdf(0.001), 1e-15);//from R
        assertEquals(0.5037753913025556, dist.cdf(0.01), 1e-15);//from R
        assertEquals(0.537677966184657, dist.cdf(0.1), 1e-15);//from R
        assertEquals(0.574900194052469, dist.cdf(0.2), 1e-15);//from R
        assertEquals(0.6112332264802844, dist.cdf(0.3), 1e-15);//from R
        assertEquals(0.6462859059344426, dist.cdf(0.4), 1e-15);//from R
        assertEquals(0.679725248962976, dist.cdf(0.5), 1e-15);//from R
        assertEquals(0.711286964175894, dist.cdf(0.6), 1e-15);//from R
        assertEquals(0.7407801988441910, dist.cdf(0.7), 1e-15);//from R
        assertEquals(0.768086856257738, dist.cdf(0.8), 1e-15);//from R
        assertEquals(0.793156407638899, dist.cdf(0.9), 1e-15);//from R
        assertEquals(0.899890457171923, dist.cdf(1.5), 1e-15);//from R
        assertEquals(0.945871046409438, dist.cdf(2), 1e-15);//from R
        assertEquals(0.9700471567489, dist.cdf(2.5), 1e-15);//from R
        assertEquals(0.98280956605562, dist.cdf(3), 1e-15);//from R

        assertEquals(0.01840294665072774, dist.density(-3), 1e-15);//from R
        assertEquals(0.0344295981491077, dist.density(-2.5), 1e-15);//from R
        assertEquals(0.06567546929159986, dist.density(-2), 1e-15);//from R
        assertEquals(0.1237997749496185, dist.density(-1.5), 1e-15);//from R
        assertEquals(0.2394943639194139, dist.density(-0.9), 1e-15);//from R
        assertEquals(0.261912757663598, dist.density(-0.8), 1e-15);//from R
        assertEquals(0.284137614522088, dist.density(-0.7), 1e-15);//from R
        assertEquals(0.305531146688152, dist.density(-0.6), 1e-15);//from R
        assertEquals(0.3253852064852857, dist.density(-0.5), 1e-15);//from R
        assertEquals(0.34296038622757, dist.density(-0.4), 1e-15);//from R
        assertEquals(0.3575364677512182, dist.density(-0.3), 1e-15);//from R
        assertEquals(0.3684695764406623, dist.density(-0.2), 1e-15);//from R
        assertEquals(0.3752491700034207, dist.density(-0.1), 1e-15);//from R
        assertEquals(0.3775237494874604, dist.density(-0.01), 1e-15);//from R
        assertEquals(0, dist.density(0d), 1e-16);//from R
        assertEquals(0.3769706135021267, dist.density(0.05), 1e-15);//from R
        assertEquals(0.3752491700034207, dist.density(0.1), 1e-15);//from R
        assertEquals(0.3684695764406623, dist.density(0.2), 1e-15);//from R
        assertEquals(0.3575364677512182, dist.density(0.3), 1e-15);//from R
        assertEquals(0.34296038622757, dist.density(0.4), 1e-15);//from R
        assertEquals(0.3253852064852857, dist.density(0.5), 1e-15);//from R
        assertEquals(0.305531146688152, dist.density(0.6), 1e-15);//from R
        assertEquals(0.284137614522088, dist.density(0.7), 1e-15);//from R
        assertEquals(0.261912757663598, dist.density(0.8), 1e-15);//from R
        assertEquals(0.2394943639194139, dist.density(0.9), 1e-15);//from R
        assertEquals(0.1237997749496185, dist.density(1.5), 1e-15);//from R
        assertEquals(0.06567546929159986, dist.density(2), 1e-15);//from R
        assertEquals(0.034429598149107, dist.density(2.5), 1e-15);//from R
        assertEquals(0.01840294665072774, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class TDistribution.
     */
    @Test
    public void testBeta_0020() {
        TDistribution dist = new TDistribution(0.1);

        assertEquals(0.3738470769863423, dist.cdf(-3), 1e-15);//from R
        assertEquals(0.389177113901638, dist.cdf(-2), 1e-15);//from R
        assertEquals(0.4204997337897933, dist.cdf(-0.9), 1e-15);//from R
        assertEquals(0.4432601133229935, dist.cdf(-0.5), 1e-15);//from R
        assertEquals(0.485450382255038, dist.cdf(-0.1), 1e-15);//from R
        assertEquals(0.4985193501389607, dist.cdf(-0.01), 1e-15);//from R
        assertEquals(0.499998519078763, dist.cdf(-0.00001), 1e-15);//from R
        assertEquals(0.5, dist.cdf(0), 1e-15);
        assertEquals(0.500001480921237, dist.cdf(0.00001), 1e-15);//from R
        assertEquals(0.514549617744962, dist.cdf(0.1), 1e-15);//from R
        assertEquals(0.5567398866770065, dist.cdf(0.5), 1e-15);//from R
        assertEquals(0.579500266210207, dist.cdf(0.9), 1e-15);//from R
        assertEquals(0.610822886098362, dist.cdf(2), 1e-15);//from R
        assertEquals(0.626152923013658, dist.cdf(3), 1e-15);//from R

        assertEquals(0.01238965448970154, dist.density(-3), 1e-15);//from R
        assertEquals(0.01920882676867347, dist.density(-2), 1e-15);//from R
        assertEquals(0.0439601530092399, dist.density(-0.9), 1e-15);//from R
        assertEquals(0.0743523221380823, dist.density(-0.5), 1e-15);//from R
        assertEquals(0.1405290092120206, dist.density(-0.1), 1e-15);//from R
        assertEquals(0, dist.density(0d), 1e-16);//from R
        assertEquals(0.1460944894942613, dist.density(0.05), 1e-15);//from R
        assertEquals(0.0743523221380823, dist.density(0.5), 1e-15);//from R
        assertEquals(0.0439601530092399, dist.density(0.9), 1e-15);//from R
        assertEquals(0.01510102450253927, dist.density(2.5), 1e-15);//from R
        assertEquals(0.01238965448970154, dist.density(3), 1e-15);//from R
    }

    /**
     * Test of class TDistribution.
     */
    @Test
    public void testF_0030() {
        TDistribution dist = new TDistribution(5.1234);

        double x, y;
        for (double u = 0.001; u < 1d; u += 0.001) {
            x = dist.quantile(u);
            y = dist.cdf(x);
            assertEquals(u, y, 1e-14);
        }
    }
}
