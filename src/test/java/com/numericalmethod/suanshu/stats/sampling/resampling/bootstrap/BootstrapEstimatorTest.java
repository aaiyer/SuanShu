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

import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.stats.descriptive.Statistic;
import com.numericalmethod.suanshu.stats.descriptive.StatisticFactory;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class BootstrapEstimatorTest {

    @Test
    public void test_0010() throws MultipleExecutionException {
        double[] sample = new double[]{1., 2., 3., 4., 5.,};
        NonParametricBootstrap bootstrap = new NonParametricBootstrap(sample);
        bootstrap.seed(1234567890L);
        int B = 100;

        BootstrapEstimator instance = new BootstrapEstimator(bootstrap,
                new StatisticFactory() {

                    public Statistic getStatistic() {
                        return new Mean();
                    }
                },
                B);

        assertEquals(new Mean(sample).value(), instance.value(), 1e-1);
        assertEquals(0.4, instance.variance(), 1e-1);
    }

    @Test
    public void test_improveEstimatorByBiggerB_0010() throws MultipleExecutionException {
        double[] sample = new double[]{1., 2., 3., 4., 5.,};
        NonParametricBootstrap bootstrap = new NonParametricBootstrap(sample);
        bootstrap.seed(1234567890L);
        int B = 10000000;

        BootstrapEstimator instance = new BootstrapEstimator(bootstrap,
                new StatisticFactory() {

                    public Statistic getStatistic() {
                        return new Mean();
                    }
                },
                B,
                true);// multi thread flag

        assertEquals(new Mean(sample).value(), instance.value(), 1e-2);
        assertEquals(0.4, instance.variance(), 1e-2);
    }
}
