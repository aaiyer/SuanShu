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
import com.numericalmethod.suanshu.stats.random.univariate.BinomialRng;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import java.util.Arrays;

/**
 * The HMM states use the Binomial distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.BinomialDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>
 */
public class BinomialDistribution implements HMMDistribution {

    /**
     * the Binomial distribution parameters
     */
    public static class Lambda {

        /** the size */
        public final int size;
        /** the success probability in each trial */
        public final double p;

        /**
         * Store the Binomial distribution parameters.
         *
         * @param size the size
         * @param p    the success probability in each trial
         */
        public Lambda(int size, double p) {
            this.size = size;
            this.p = p;
        }
    }

    private final Lambda[] lambda;

    /**
     * Construct a Binomial distribution for each state in the HMM model.
     *
     * @param lambda the distribution parameters
     */
    public BinomialDistribution(Lambda[] lambda) {
        this.lambda = Arrays.copyOf(lambda, lambda.length);
    }

    @Override
    public Lambda[] getParams() {
        Lambda[] lambda1 = Arrays.copyOf(this.lambda, this.lambda.length);
        return lambda1;
    }

    @Override
    public RandomNumberGenerator[] getRandomNumberGenerators() {
        RandomNumberGenerator[] rng = new RandomNumberGenerator[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            int n = lambda[i].size;
            double p = lambda[i].p;
            rng[i] = new BinomialRng(n, p);
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            int n = lambda[i].size;
            double p = lambda[i].p;
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.BinomialDistribution(n, p);
        }

        return dist;
    }

    @Override
    public Lambda[] getMStepParams(double[] observations, Matrix u, Object[] param0) {
        final Lambda[] lambda0 = Arrays.copyOf((Lambda[]) param0, param0.length);
        final int n = u.nRows();
        final int m = u.nCols();

        Lambda[] lambda1 = new Lambda[lambda0.length];
        for (int j = 0; j < m; ++j) {
            //To avoid identifiability problems, for binomial distribution, the parameter size should always be specified (not estimated).
            int size = lambda0[j].size;

            double numerator = 0.;
            double denominator = 0.;
            for (int i = 0; i < n; ++i) {
                double u_ij = u.get(i + 1, j + 1);
                numerator += u_ij * observations[i];
                denominator += u_ij * size;
            }
            double p = numerator / denominator;

            lambda1[j] = new Lambda(size, p);
        }

        return lambda1;
    }

    @Override
    public HMMDistribution newEMDistribution(Object[] param) {
        Lambda[] lambda1 = Arrays.copyOf((Lambda[]) param, param.length);
        return new BinomialDistribution(lambda1);
    }
}
