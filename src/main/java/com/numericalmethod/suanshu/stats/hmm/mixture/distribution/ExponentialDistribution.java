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
package com.numericalmethod.suanshu.stats.hmm.mixture.distribution;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.exp.Ziggurat2000Exp;
import java.util.Arrays;

/**
 * The HMM states use the Exponential distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.ExponentialDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Exponential_distribution">Wikipedia: ExponentialDistribution distribution</a>
 */
public class ExponentialDistribution implements HMMDistribution {

    private final Double[] rates;

    /**
     * Construct an Exponential distribution for each state in the HMM model.
     *
     * @param rates the rates
     */
    public ExponentialDistribution(Double[] rates) {
        this.rates = Arrays.copyOf(rates, rates.length);
    }

    @Override
    public Double[] getParams() {
        Double[] rates0 = Arrays.copyOf(this.rates, this.rates.length);
        return rates0;
    }

    @Override
    public RandomNumberGenerator[] getRandomNumberGenerators() {
        RandomNumberGenerator[] rng = new RandomNumberGenerator[rates.length];
        for (int i = 0; i < rates.length; ++i) {
            final double rate = rates[i];
            rng[i] = new RandomNumberGenerator() {

                Ziggurat2000Exp rng = new Ziggurat2000Exp();

                @Override
                public void seed(long... seeds) {
                    rng.seed(seeds);
                }

                @Override
                public double nextDouble() {
                    return rng.nextDouble() / rate;
                }
            };
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[rates.length];
        for (int i = 0; i < rates.length; ++i) {
            double rate = rates[i];
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.ExponentialDistribution(rate);
        }

        return dist;
    }

    @Override
    public Double[] getMStepParams(double[] observations, Matrix u, Object[] param0) {
        final int n = u.nRows();
        final int m = u.nCols();

        Double[] param1 = new Double[param0.length];
        for (int j = 1; j <= m; ++j) {
            double numerator = 0.;
            double denominator = 0.;
            for (int i = 1; i <= n; ++i) {
                double u_ij = u.get(i, j);
                numerator += u_ij;
                denominator += u_ij * observations[i - 1];
            }

            param1[j - 1] = numerator / denominator;
        }

        return param1;
    }

    @Override
    public HMMDistribution newEMDistribution(Object[] param) {
        Double[] lambda = Arrays.copyOf((Double[]) param, param.length);
        return new ExponentialDistribution(lambda);
    }
}
