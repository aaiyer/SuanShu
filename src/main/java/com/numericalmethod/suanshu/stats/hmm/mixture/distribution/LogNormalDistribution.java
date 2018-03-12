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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.LogNormalRng;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import static java.lang.Math.*;
import java.util.Arrays;

/**
 * The HMM states use the Log-Normal distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.LogNormalDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Log-normal_distribution">Wikipedia: Log-normal distribution</a>
 */
public class LogNormalDistribution implements HMMDistribution {

    /**
     * the Log-Normal distribution parameters
     */
    public static class Lambda {

        /** the log-mean μ ∈ R */
        public final double logMu;
        /** the log-standard deviation; shape */
        public final double logSigma;

        /**
         * Construct a Log-Normal distribution.
         *
         * @param mu    the log-mean
         * @param sigma the log-standard deviation
         */
        public Lambda(double mu, double sigma) {
            this.logMu = mu;
            this.logSigma = sigma;
        }
    }

    private final Lambda[] lambda;
    private final boolean isMuEstimated;
    private final boolean isSigmaEstimated;

    /**
     * Construct a Log-Normal distribution for each state in the HMM model.
     *
     * @param lambda           the distribution parameters
     * @param isMuEstimated    indicate whether parameter {@code mu} is to be estimated
     * @param isSigmaEstimated indicate whether parameter {@code sigma} is to be estimated
     */
    public LogNormalDistribution(Lambda[] lambda, boolean isMuEstimated, boolean isSigmaEstimated) {
        this.lambda = Arrays.copyOf(lambda, lambda.length);
        this.isMuEstimated = isMuEstimated;
        this.isSigmaEstimated = isSigmaEstimated;
    }

    /**
     * Construct a Log-Normal distribution for each state in the HMM model.
     *
     * @param lambda the distribution parameters
     */
    public LogNormalDistribution(Lambda[] lambda) {
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
            double logMu = lambda[i].logMu;
            double logSigma = lambda[i].logSigma;
            rng[i] = new LogNormalRng(logMu, logSigma);
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double logMu = lambda[i].logMu;
            double logSigma = lambda[i].logSigma;
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.LogNormalDistribution(logMu, logSigma);
        }

        return dist;
    }

    @Override
    public Lambda[] getMStepParams(double[] observations, Matrix u, Object[] param0) {
        final Lambda[] lambda0 = Arrays.copyOf((Lambda[]) param0, param0.length);
        final int n = u.nRows();
        final int m = u.nCols();

        final double[] logx = DoubleUtils.foreach(observations, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return log(x);
            }
        });

        Lambda[] lambda1 = new Lambda[lambda0.length];
        for (int j = 1; j <= m; ++j) {
            double logmu = lambda[j - 1].logMu;
            if (isMuEstimated) {
                double numerator = 0.;
                double denominator = 0.;
                for (int i = 1; i <= n; ++i) {
                    double u_ij = u.get(i, j);
                    numerator += u_ij * logx[i - 1];
                    denominator += u_ij;
                }
                logmu = numerator / denominator;
            }

            double logsigma = lambda[j - 1].logSigma;
            if (isSigmaEstimated) {
                double numerator = 0.;
                double denominator = 0.;
                for (int i = 1; i <= n; ++i) {
                    double u_ij = u.get(i, j);
                    numerator += u_ij * pow(logx[i - 1] - logmu, 2);
                    denominator += u_ij;
                }
                logsigma = sqrt(numerator / denominator);
            }

            lambda1[j - 1] = new Lambda(logmu, logsigma);
        }

        return lambda1;
    }

    @Override
    public HMMDistribution newEMDistribution(Object[] param) {
        Lambda[] lambda1 = Arrays.copyOf((Lambda[]) param, param.length);
        return new LogNormalDistribution(lambda1, isMuEstimated, isSigmaEstimated);
    }
}
