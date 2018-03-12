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
package com.numericalmethod.suanshu.stats.random.univariate;

import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.distribution.univariate.BinomialDistribution;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BinomialRngTest {

    @Test
    public void test_0010() {
        final int n = 20;
        final double p = 0.7;

        BinomialRng instance = new BinomialRng(n, p);
        instance.seed(1234567890123L);

        Mean mean = new Mean();
        Variance var = new Variance();
        Skewness skew = new Skewness();
        Kurtosis kurtosis = new Kurtosis();
        for (int i = 0; i < 100000; ++i) {
            double value = instance.nextDouble();
            mean.addData(value);
            var.addData(value);
            skew.addData(value);
            kurtosis.addData(value);
        }

        BinomialDistribution dist = new BinomialDistribution(n, p);
        assertEquals(mean.value(), dist.mean(), 1e-2);
        assertEquals(1., var.value() / dist.variance(), 1e-2);
        assertEquals(1., skew.value() / dist.skew(), 5e-2);
        assertEquals(1., kurtosis.value() / dist.kurtosis(), 6e-2);
    }
}
