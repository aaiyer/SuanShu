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
package com.numericalmethod.suanshu.stats.sampling.resampling.bootstrap;

import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class NonParametricBootstrapTest {

    @Test
    public void test_0010() {
        double[] sample = new double[]{1., 2., 3., 4., 5.}; // sample from true population
        NonParametricBootstrap instance = new NonParametricBootstrap(sample);
        instance.seed(1234567890L);

        int B = 100;
        double[] means = new double[B];
        for (int i = 0; i < B; ++i) {
            double[] resample = instance.getResample();
            means[i] = new Mean(resample).value();
        }

        double mean = new Mean(means).value();// estimator of population mean
        assertEquals(new Mean(sample).value(), mean, 1e-1);

        double var = new Variance(means).value();// variance of estimator; limited by sample size (regardless of how big B is)
        assertEquals(0.4, var, 5e-2);
    }

    /**
     * Increasing the sample size significantly improve the estimator accuracy,
     * for the same B.
     * Note that estimator variance is greatly reduced.
     */
    @Test
    public void test_0020() {
        double[] sample = new double[]{
            1., 2., 3., 4., 5.,
            1., 2., 3., 4., 5.}; // sample from true population
        NonParametricBootstrap instance = new NonParametricBootstrap(sample);
        instance.seed(1234567890L);

        int B = 100;
        double[] means = new double[B];
        for (int i = 0; i < B; ++i) {
            double[] resample = instance.getResample();
            means[i] = new Mean(resample).value();
        }

        double mean = new Mean(means).value();// estimator of population mean
        assertEquals(new Mean(sample).value(), mean, 3e-2);

        double var = new Variance(means).value();// variance of estimator; limited by sample size (regardless of how big B is)
        assertEquals(0.15, var, 1e-2);
    }

    /**
     * Increasing B improves the estimator mean, but NOT the accuracy.
     * In fact, the accuracy/variance converges to 0.4.
     * The bootstrap method is limited by sample size.
     */
    @Test
    public void test_0030() {
        double[] sample = new double[]{1., 2., 3., 4., 5.}; // sample from true population
        NonParametricBootstrap instance = new NonParametricBootstrap(sample);
        instance.seed(1234567890L);

        int B = 10000;
        double[] means = new double[B];
        for (int i = 0; i < B; ++i) {
            double[] resample = instance.getResample();
            means[i] = new Mean(resample).value();
        }

        double mean = new Mean(means).value();// estimator of population mean
        assertEquals(new Mean(sample).value(), mean, 3e-2);

        double var = new Variance(means).value();// variance of estimator; limited by sample size (regardless of how big B is)
        assertEquals(0.4, var, 1e-2);
    }
}
