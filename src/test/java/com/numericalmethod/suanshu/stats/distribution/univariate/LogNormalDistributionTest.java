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

import com.numericalmethod.suanshu.stats.distribution.univariate.LogNormalDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LogNormalDistributionTest {

    @Test
    public void test_0010() {
        LogNormalDistribution logN = new LogNormalDistribution(1, 2);

        assertEquals(20.085, logN.mean(), 1e-3);// from R
        assertEquals(22757., logN.variance(), 22757);// from debugger

        assertEquals(0, logN.cdf(-99999999), 0);
        assertEquals(0, logN.cdf(-9999), 0);
        assertEquals(0, logN.cdf(-1000), 0);
        assertEquals(0, logN.cdf(-100), 0);
        assertEquals(0, logN.cdf(-14.5), 0);
        assertEquals(0, logN.cdf(-3), 0);
        assertEquals(0, logN.cdf(-2), 0);
        assertEquals(0, logN.cdf(-1), 0);
        assertEquals(0, logN.cdf(-0.1), 0);
        assertEquals(0, logN.cdf(-0.00001), 0);
        assertEquals(0, logN.cdf(0), 0);
        assertEquals(1.96903577229064e-10, logN.cdf(0.00001), 1e-25);// from R
        assertEquals(1.652484704450418e-07, logN.cdf(0.0001), 1e-22);// from R
        assertEquals(3.84473721175183e-05, logN.cdf(0.001), 1e-20);// from R
        assertEquals(0.00253474205159659, logN.cdf(0.01), 1e-15);// from R
        assertEquals(0.0493394267528022, logN.cdf(0.1), 1e-15);// from R
        assertEquals(0.308537538725987, logN.cdf(1), 1e-15);// from R
        assertEquals(0.3255102636292202, logN.cdf(1.1), 1e-15);// from R
        assertEquals(0.3413288272347352, logN.cdf(1.2), 1e-15);// from R
        assertEquals(0.3561317428011628, logN.cdf(1.3), 1e-15);// from R
        assertEquals(0.370033779619676, logN.cdf(1.4), 1e-15);// from R
        assertEquals(0.383131166616305, logN.cdf(1.5), 1e-15);// from R
        assertEquals(0.4072262760592488, logN.cdf(1.7), 1e-15);// from R
        assertEquals(0.428940168747573, logN.cdf(1.9), 1e-15);// from R
        assertEquals(0.4390310097476894, logN.cdf(2.0), 1e-15);// from R
        assertEquals(0.4833072907274, logN.cdf(2.5), 1e-15);// from R
        assertEquals(0.519662338497517, logN.cdf(3.0), 1e-15);// from R
        assertEquals(0.742571170554942, logN.cdf(10), 1e-15);// from R
        assertEquals(0.7987246042843795, logN.cdf(14.5), 1e-15);// from R
        assertEquals(0.964273300056689, logN.cdf(100), 1e-15);// from R
        assertEquals(0.998430957989599, logN.cdf(1000), 1e-15);// from R
        assertEquals(0.999979794763356, logN.cdf(9999), 1e-15);// from R
        assertEquals(1, logN.cdf(99999999), 0);// from R

        assertEquals(0, logN.density(-3), 0);
        assertEquals(0, logN.density(-2.5), 0);
        assertEquals(0, logN.density(-2), 0);
        assertEquals(0, logN.density(-1.5), 0);
        assertEquals(0, logN.density(-1), 0);
        assertEquals(0, logN.density(-0.9), 0);
        assertEquals(0, logN.density(-0.8), 0);
        assertEquals(0, logN.density(-0.7), 0);
        assertEquals(0, logN.density(-0.6), 0);
        assertEquals(0, logN.density(-0.5), 0);
        assertEquals(0, logN.density(-0.4), 0);
        assertEquals(0, logN.density(-0.3), 0);
        assertEquals(0, logN.density(-0.2), 0);
        assertEquals(0, logN.density(-0.1), 0);
        assertEquals(0, logN.density(-0.05), 0);
        assertEquals(0, logN.density(0d), 1e-15);// from R
        assertEquals(0.5422175412490545, logN.density(0.05), 1e-15);// from R
        assertEquals(0.510234855730895, logN.density(0.1), 1e-15);// from R
        assertEquals(0.4257965714024518, logN.density(0.2), 1e-15);// from R
        assertEquals(0.3622937526854036, logN.density(0.3), 1e-15);// from R
        assertEquals(0.3151154330921068, logN.density(0.4), 1e-15);// from R
        assertEquals(0.2787940462927309, logN.density(0.5), 1e-15);// from R
        assertEquals(0.2499275137462398, logN.density(0.6), 1e-15);// from R
        assertEquals(0.22639327599457, logN.density(0.7), 1e-15);// from R
        assertEquals(0.2068105378673808, logN.density(0.8), 1e-15);// from R
        assertEquals(0.1902430275483389, logN.density(0.9), 1e-15);// from R
        assertEquals(0.1760326633821498, logN.density(1), 1e-15);// from R
        assertEquals(0.127233055814411, logN.density(1.5), 1e-15);// from R
        assertEquals(0.0985685803440131, logN.density(2), 1e-15);// from R
        assertEquals(0.07971859955531624, logN.density(2.5), 1e-15);// from R
        assertEquals(0.0664096069245068, logN.density(3), 1e-15);// from R
    }

    @Test
    public void test_0020() {
        LogNormalDistribution logN = new LogNormalDistribution(1, 2);

        double x, y;
        for (double u = 0.000001; u < 1d; u += 0.000001) {
            x = logN.quantile(u);
            y = logN.cdf(x);
            assertEquals(u, y, 1e-9);
        }
    }
}
