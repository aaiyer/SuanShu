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

import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.stats.descriptive.Statistic;
import com.numericalmethod.suanshu.stats.descriptive.StatisticFactory;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.sampling.resampling.Resampling;

/**
 * This class estimates the statistic for a sample using a bootstrap method.
 *
 * @author Haksun Li
 */
public class BootstrapEstimator {

    private final Resampling bootstrap;
    private final StatisticFactory factory;
    private final int B;
    private final double[] stats;
    private final boolean isParallel;

    /**
     * Constructs a bootstrap estimator.
     *
     * @param bootstrap the bootstrap method and the sample
     * @param factory   the statistic
     * @param B         the number of bootstrap replicas
     */
    public BootstrapEstimator(Resampling bootstrap, StatisticFactory factory, int B) {
        this(bootstrap, factory, B, false);
    }

    /**
     * Constructs a bootstrap estimator.
     *
     * @param bootstrap  the bootstrap method and the sample
     * @param factory    the statistic
     * @param B          the number of bootstrap replicas
     * @param isParallel {@code true} if to run in parallel cores
     */
    public BootstrapEstimator(Resampling bootstrap, StatisticFactory factory, int B, boolean isParallel) {
        this.bootstrap = bootstrap;
        this.factory = factory;
        this.B = B;
        this.stats = new double[B];
        this.isParallel = isParallel;

        try {
            sim();
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException("failed to do bootstrapping in parallel", ex);
        }
    }

    /**
     * The estimator value.
     *
     * @return the estimator value
     */
    public double value() {
        double theta = new Mean(stats).value();
        return theta;
    }

    /**
     * The estimator variance, of which the convergence limit is decided by
     * sample size, not B.
     *
     * @return the estimator variance
     */
    public double variance() {
        double var = new Variance(stats).value();
        return var;
    }

    private void sim() throws MultipleExecutionException {
        final Object lock = new Object();
        new ParallelExecutor().conditionalForLoop(isParallel, 0, B,
                                                  new LoopBody() {

            public void run(int i) throws Exception {
                double[] resample;
                Statistic stat;
                synchronized (lock) { // synchronized on shared objects
                    resample = bootstrap.getResample();
                    stat = factory.getStatistic();
                }
                stat.addData(resample);// TODO: this is an unnecessary copying
                stats[i] = stat.value();
            }
        });
    }
}
