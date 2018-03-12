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
package com.numericalmethod.suanshu.stats.random.univariate.beta;

import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.distribution.univariate.BetaDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class Cheng1978Test {

    @Test
    public void test_0010() {
        final int size = 1000000;

        final double alpha = 0.1;
        final double beta = 0.2;

        Cheng1978 rng = new Cheng1978(alpha, beta, new UniformRng());
        rng.seed(1234567890L);

        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            assertTrue(x[i] >= 0);
        }

        Mean mean = new Mean(x);
        Variance var = new Variance(x);
        Skewness skew = new Skewness(x);
        Kurtosis kurtosis = new Kurtosis(x);

        ProbabilityDistribution dist = new BetaDistribution(alpha, beta);
        assertEquals(dist.mean(), mean.value(), 0.0001);
        assertEquals(dist.variance(), var.value(), 0.001);
        assertEquals(dist.skew(), skew.value(), 0.001);
        assertEquals(dist.kurtosis(), kurtosis.value(), 0.001);
    }

    @Test
    public void test_0020() {
        final int size = 1000000;

        final double alpha = 1.1;
        final double beta = 1.2;

        Cheng1978 rng = new Cheng1978(alpha, beta, new UniformRng());
        rng.seed(1234567890L);

        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            assertTrue(x[i] >= 0);
        }

        Mean mean = new Mean(x);
        Variance var = new Variance(x);
        Skewness skew = new Skewness(x);
        Kurtosis kurtosis = new Kurtosis(x);

        ProbabilityDistribution dist = new BetaDistribution(alpha, beta);
        assertEquals(dist.mean(), mean.value(), 0.0003);
        assertEquals(dist.variance(), var.value(), 0.001);
        assertEquals(dist.skew(), skew.value(), 0.001);
        assertEquals(dist.kurtosis(), kurtosis.value(), 0.001);
    }

    @Test
    public void test_0030() {
        final int size = 1000000;

        final double alpha = 5.1;
        final double beta = 9.2;

        Cheng1978 rng = new Cheng1978(alpha, beta, new UniformRng());
        rng.seed(1234567890L);

        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            assertTrue(x[i] >= 0);
        }

        Mean mean = new Mean(x);
        Variance var = new Variance(x);
        Skewness skew = new Skewness(x);
        Kurtosis kurtosis = new Kurtosis(x);

        ProbabilityDistribution dist = new BetaDistribution(alpha, beta);
        assertEquals(dist.mean(), mean.value(), 0.0001);
        assertEquals(dist.variance(), var.value(), 0.001);
        assertEquals(dist.skew(), skew.value(), 0.002);
        assertEquals(dist.kurtosis(), kurtosis.value(), 0.001);
    }

    @Test
    public void test_0040() {
        final int size = 1000000;

        final double alpha = 0.001;
        final double beta = 0.002;

        Cheng1978 rng = new Cheng1978(alpha, beta, new UniformRng());
        rng.seed(1234567890L);

        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            assertTrue(x[i] >= 0);
        }

        Mean mean = new Mean(x);
        Variance var = new Variance(x);
        Skewness skew = new Skewness(x);
        Kurtosis kurtosis = new Kurtosis(x);

        ProbabilityDistribution dist = new BetaDistribution(alpha, beta);
        assertEquals(dist.mean(), mean.value(), 0.0003);
        assertEquals(dist.variance(), var.value(), 0.001);
        assertEquals(dist.skew(), skew.value(), 0.002);
        assertEquals(dist.kurtosis(), kurtosis.value(), 0.003);
    }
}
