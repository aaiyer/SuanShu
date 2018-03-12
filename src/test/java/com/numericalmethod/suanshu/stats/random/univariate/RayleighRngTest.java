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
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RayleighRngTest {

    /**
     * Test of class RayleighDistribution.
     */
    @Test
    public void testRayleigh_0010() {
        int size = 1000000;

        double sigma = 25;
        RayleighRng rng = new RayleighRng(sigma);
        rng.seed(1234567890L);
        double[] x = new double[size];
        for (int i = 1; i < size; ++i) {
            x[i] = rng.nextDouble();

        }

        Mean mean = new Mean(x);
        Variance var = new Variance(x);
        Skewness skew = new Skewness(x);
        Kurtosis kurtosis = new Kurtosis(x);

        assertEquals(new com.numericalmethod.suanshu.stats.distribution.univariate.RayleighDistribution(sigma).mean(),
                mean.value(), 0.1);
        assertEquals(new com.numericalmethod.suanshu.stats.distribution.univariate.RayleighDistribution(sigma).variance(),
                var.value(), 1);
        assertEquals(new com.numericalmethod.suanshu.stats.distribution.univariate.RayleighDistribution(sigma).skew(),
                skew.value(), 0.1);
        assertEquals(new com.numericalmethod.suanshu.stats.distribution.univariate.RayleighDistribution(sigma).kurtosis(),
                kurtosis.value(), 0.1);
    }
}
