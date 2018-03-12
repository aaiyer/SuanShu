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
package com.numericalmethod.suanshu.stats.test.distribution.normality;

import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import com.numericalmethod.suanshu.stats.random.multivariate.IID;

/**
 * Jarque–Bera distribution is the distribution of the Jarque–Bera statistics, which measures the departure from normality.
 *
 * <p>
 * The statistics is
 *
 * <blockquote><code><pre>
 *       n
 * JB = --- (S<sup>2</sup>) + 0.25 * K<sup>2</sup>))
 *       6
 * </pre></code></blockquote>
 *
 * S is the skewness; K is the kurtosis.
 * 
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Jarque%E2%80%93Bera_test">Wikipedia: Jarque–Bera test</a>
 */
public class JarqueBeraDistribution extends EmpiricalDistribution {

    /**
     * the number of observations in a sample
     */
    public final int N;
    /**
     * the number of Monte Carlo simulation paths
     */
    public final int nSim;

    /**
     * Construct a Jarque–Bera distribution using Monte Carlo simulation.
     * 
     * @param N number of observations in a sample
     * @param nSim number of simulations
     * @param rnorm a Gaussian random number generator
     */
    public JarqueBeraDistribution(int N, int nSim, StandardNormalRng rnorm) {//recommend nSim to be >= 70000 for cdf, and >= 1000000 for quantile
        super(simulation(N, nSim, rnorm));

        this.N = N;
        this.nSim = nSim;
    }

    /**
     * Construct a Jarque–Bera distribution using Monte Carlo simulation.
     *
     * @param N number of observations in a sample
     * @param nSim number of simulations
     */
    public JarqueBeraDistribution(int N, int nSim) {
        this(N, nSim, new StandardNormalRng());
    }

    /**
     * Simulate the statistics.
     * 
     * @param N number of observations in a sample
     * @param nSim number of simulations
     * @param rnorm a Gaussian random number generator
     * @return a set of statistics
     */
    private static double[] simulation(int N, int nSim, StandardNormalRng rnorm) {
        double[] stats = new double[nSim];

        //simulate the test statistics
        for (int i = 0; i < nSim; ++i) {
            double[] sample = new IID(rnorm, N).nextVector();

            double s = new Skewness(sample).sample();
            double k = new Kurtosis(sample).sample();

            stats[i] = (s * s + k * k / 4.) * N / 6.;
        }

        return stats;
    }
}
