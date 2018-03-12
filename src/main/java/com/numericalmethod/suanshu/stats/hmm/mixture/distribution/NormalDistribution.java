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
import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 * The HMM states use the Normal distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Normal_distribution">Wikipedia: Normal distribution</a>
 */
public class NormalDistribution implements HMMDistribution {

    /**
     * the Normal distribution parameters
     */
    public static class Lambda {

        /** the mean */
        public final double mu;
        /** the standard deviation */
        public final double sigma;

        /**
         * Construct a Normal distribution.
         *
         * @param mu    the mean
         * @param sigma the standard deviation
         */
        public Lambda(double mu, double sigma) {
            this.mu = mu;
            this.sigma = sigma;
        }
    }

    private final Lambda[] lambda;
    private final boolean isMuEstimated;
    private final boolean isSigmaEstimated;

    /**
     * Construct a Normal distribution for each state in the HMM model.
     *
     * @param lambda           the distribution parameters
     * @param isMuEstimated    indicate whether parameter {@code mu} is to be estimated
     * @param isSigmaEstimated indicate whether parameter {@code sigma} is to be estimated
     */
    public NormalDistribution(Lambda[] lambda, boolean isMuEstimated, boolean isSigmaEstimated) {
        this.lambda = Arrays.copyOf(lambda, lambda.length);
        this.isMuEstimated = isMuEstimated;
        this.isSigmaEstimated = isSigmaEstimated;
    }

    /**
     * Construct a Normal distribution for each state in the HMM model.
     *
     * @param lambda the distribution parameters
     */
    public NormalDistribution(Lambda[] lambda) {
        this(lambda, true, true);
    }

    @Override
    public Lambda[] getParams() {
        Lambda[] lambda0 = Arrays.copyOf(this.lambda, this.lambda.length);
        return lambda0;
    }

    @Override
    public RandomNumberGenerator[] getRandomNumberGenerators() {
        RandomNumberGenerator[] rng = new RandomNumberGenerator[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double mu = lambda[i].mu;
            double sigma = lambda[i].sigma;
            rng[i] = new NormalRng(mu, sigma);
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double mu = lambda[i].mu;
            double sigma = lambda[i].sigma;
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution(mu, sigma);
        }

        return dist;
    }

    @Override
    public Lambda[] getMStepParams(double[] observations, Matrix u, Object[] param0) {
        final Lambda[] lambda0 = Arrays.copyOf((Lambda[]) param0, param0.length);
        final int n = u.nRows();
        final int m = u.nCols();

        Lambda[] lambda1 = new Lambda[lambda0.length];
        for (int j = 1; j <= m; ++j) {
            double mu = lambda[j - 1].mu;
            if (isMuEstimated) {
                double numerator = 0.;
                double denominator = 0.;
                for (int i = 1; i <= n; ++i) {
                    double u_ij = u.get(i, j);
                    numerator += u_ij * observations[i - 1];
                    denominator += u_ij;
                }
                mu = numerator / denominator;
            }

            double sigma = lambda[j - 1].sigma;
            if (isSigmaEstimated) {
                double numerator = 0.;
                double denominator = 0.;
                for (int i = 1; i <= n; ++i) {
                    double u_ij = u.get(i, j);
                    numerator += u_ij * pow(observations[i - 1] - mu, 2);
                    denominator += u_ij;
                }
                sigma = sqrt(numerator / denominator);
            }

            lambda1[j - 1] = new Lambda(mu, sigma);
        }

        return lambda1;
    }

    @Override
    public HMMDistribution newEMDistribution(Object[] param) {
        Lambda[] lambda1 = Arrays.copyOf((Lambda[]) param, param.length);
        return new NormalDistribution(lambda1, isMuEstimated, isSigmaEstimated);
    }
}
