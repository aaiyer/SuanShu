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

import com.numericalmethod.suanshu.stats.distribution.univariate.BinomialDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BinomialDistributionTest {

    @Test
    public void test_0010() {
        BinomialDistribution Bi = new BinomialDistribution(20, 0.7);

        assertEquals(14, Bi.mean(), 1e-3);
        assertEquals(4.2, Bi.variance(), 1e-15);

        assertEquals(0, Bi.cdf(-99999999), 0);
        assertEquals(0, Bi.cdf(-9999), 0);
        assertEquals(0, Bi.cdf(-1000), 0);
        assertEquals(0, Bi.cdf(-100), 0);
        assertEquals(0, Bi.cdf(-14.5), 0);
        assertEquals(0, Bi.cdf(-3), 0);
        assertEquals(0, Bi.cdf(-2), 0);
        assertEquals(0, Bi.cdf(-1), 0);
        assertEquals(0, Bi.cdf(-0.1), 0);
        assertEquals(0, Bi.cdf(-0.00001), 0);
        assertEquals(0, Bi.cdf(0), 0);
        assertEquals(0, Bi.cdf(0.00001), 0);
        assertEquals(0, Bi.cdf(0.1), 0);
        assertEquals(1.662034e-09, Bi.cdf(1), 1e-15);// from R
        assertEquals(1.662034e-09, Bi.cdf(1.1), 1e-15);// from R
        assertEquals(1.662034e-09, Bi.cdf(1.9), 1e-15);// from R
        assertEquals(3.773088e-08, Bi.cdf(2.0), 1e-14);// from R
        assertEquals(5.426947e-07, Bi.cdf(3.0), 1e-13);// from R
        assertEquals(5.550253e-06, Bi.cdf(4.0), 1e-13);// from R
        assertEquals(4.294002e-05, Bi.cdf(5.0), 1e-11);// from R
        assertEquals(0.0479618973313435, Bi.cdf(10), 1e-14);// from R
        assertEquals(0.762492221122398, Bi.cdf(15), 1e-14);// from R
        assertEquals(1, Bi.cdf(20), 0);
        assertEquals(1, Bi.cdf(21), 0);
        assertEquals(1, Bi.cdf(9999), 0);
        assertEquals(1, Bi.cdf(99999999), 0);

        assertEquals(0, Bi.density(-3), 0);
        assertEquals(0, Bi.density(-2), 0);
        assertEquals(0, Bi.density(-1), 0);
        assertEquals(0, Bi.density(-0.5), 0);
        assertEquals(0, Bi.density(-0.05), 0);
        assertEquals(0, Bi.density(0), 0);
        assertEquals(0, Bi.density(0.5), 0);
        assertEquals(1.627166053800003e-09, Bi.density(1), 1e-23);// from R
        assertEquals(0, Bi.density(1.5), 0);
        assertEquals(3.60688475259001e-08, Bi.density(2), 1e-22);// from R
        assertEquals(5.049638653626004e-07, Bi.density(3), 1e-15);// from R
        assertEquals(5.007558331512471e-06, Bi.density(4), 1e-15);// from R
        assertEquals(3.738976887529305e-05, Bi.density(5), 1e-15);// from R
        assertEquals(0.03081708090008503, Bi.density(10), 1e-15);// from R
        assertEquals(0.00683933711122387, Bi.density(19), 1e-15);// from R
        assertEquals(0.000797922662976119, Bi.density(20), 1e-15);// from R
        assertEquals(0, Bi.density(21), 0);// from R
    }

    @Test
    public void test_0020() {
        BinomialDistribution Bi = new BinomialDistribution(20, 0.7);

        assertEquals(4, (int) Bi.quantile(0.000001));
        assertEquals(9, (int) Bi.quantile(0.01));
        assertEquals(11, (int) Bi.quantile(0.1));
        assertEquals(12, (int) Bi.quantile(0.2));
        assertEquals(14, (int) Bi.quantile(0.5));
        assertEquals(15, (int) Bi.quantile(0.6));
        assertEquals(17, (int) Bi.quantile(0.9));
        assertEquals(20, (int) Bi.quantile(1));
    }
}
