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

import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Trigamma;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.SQPActiveSetSolver;
import com.numericalmethod.suanshu.optimization.constrained.problem.NonNegativityConstraintOptimProblem;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.beta.Cheng1978;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.log;
import java.util.Arrays;

/**
 * The HMM states use the Beta distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.BetaDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Beta_distribution">Wikipedia: Beta distribution</a>
 */
public class BetaDistribution implements HMMDistribution {

    /**
     * the Beta distribution parameters
     */
    public static class Lambda {

        /** α: the shape parameter */
        public final double alpha;
        /** β: the shape parameter */
        public final double beta;

        /**
         * Store the Beta distribution parameters.
         *
         * @param alpha α: the shape parameter
         * @param beta  β: the shape parameter
         */
        public Lambda(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }
    }

    private final boolean isAlphaEstimated;
    private final boolean isBetaEstimated;
    private final Lambda[] lambda;
    private final double epsilon;
    private final int maxIterations;
    private final Gamma gamma = new GammaLanczosQuick();//TODO: make parameter?
    private final Digamma digamma = new Digamma();
    private final Trigamma trigamma = new Trigamma();

    /**
     * Construct a Beta distribution for each state in the HMM model.
     *
     * @param lambda           the distribution parameters
     * @param isAlphaEstimated indicate whether parameter {@code alpha} is to be estimated
     * @param isBetaEstimated  indicate whether parameter {@code beta} is to be estimated
     * @param epsilon          a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations    the maximum number of iterations in each M-step
     */
    public BetaDistribution(Lambda[] lambda, boolean isAlphaEstimated, boolean isBetaEstimated, double epsilon, int maxIterations) {
        this.lambda = Arrays.copyOf(lambda, lambda.length);
        this.isAlphaEstimated = isAlphaEstimated;
        this.isBetaEstimated = isBetaEstimated;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a Beta distribution for each state in the HMM model.
     *
     * @param lambda        the distribution parameters
     * @param maxIterations the maximum number of iterations in each M-step
     */
    public BetaDistribution(Lambda[] lambda, int maxIterations) {
        this(lambda, true, true, 1e-5, maxIterations);
    }

    @Override
    public Lambda[] getParams() {
        Lambda[] lambda1 = Arrays.copyOf(lambda, lambda.length);
        return lambda1;
    }

    @Override
    public RandomNumberGenerator[] getRandomNumberGenerators() {
        RandomNumberGenerator[] rng = new RandomNumberGenerator[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double alpha = lambda[i].alpha;
            double beta = lambda[i].beta;
            rng[i] = new Cheng1978(alpha, beta);
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double alpha = lambda[i].alpha;
            double beta = lambda[i].beta;
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.BetaDistribution(alpha, beta);
        }

        return dist;
    }

    @Override
    public Lambda[] getMStepParams(double[] observations, Matrix u, Object[] param0) {
        final Lambda[] lambda0 = Arrays.copyOf((Lambda[]) param0, param0.length);
        final int n = u.nRows();
        final int m = u.nCols();

        double[] x1 = new double[n];
        double[] x2 = new double[n];
        for (int i = 0; i < n; ++i) {
            x1[i] = log(observations[i]);
            x2[i] = log(1. - observations[i]);
        }

        double[] log_x1_bar = new double[m];
        double[] log_x2_bar = new double[m];
        for (int j = 1; j <= m; ++j) {
            double numerator_x1 = 0.;
            double numerator_x2 = 0.;
            double denominator = 0.;
            for (int i = 1; i <= n; ++i) {
                double u_ij = u.get(i, j);
                numerator_x1 += u_ij * x1[i - 1];
                numerator_x2 += u_ij * x2[i - 1];
                denominator += u_ij;
            }
            log_x1_bar[j - 1] = numerator_x1 / denominator;
            log_x2_bar[j - 1] = numerator_x2 / denominator;
        }

        Lambda[] lambda1 = new Lambda[lambda0.length];
        for (int j = 1; j <= m; ++j) {
            double a0 = lambda[j - 1].alpha;
            double b0 = lambda[j - 1].beta;
            final double a0_temp = a0;
            final double b0_temp = b0;
            final double y_1j = log_x1_bar[j - 1];
            final double y_2j = log_x2_bar[j - 1];

            try {//TODO: how to get rid of the if-statements
                if (isAlphaEstimated && isBetaEstimated) {
                    BivariateRealFunction f = new BivariateRealFunction() {

                        @Override
                        public double evaluate(double a, double b) {
                            double gamma_ab = gamma.evaluate(a + b);
                            double gamma_a = gamma.evaluate(a);
                            double gamma_b = gamma.evaluate(b);
                            double fx = log(gamma_ab) - log(gamma_a) - log(gamma_b)
                                        + (a - 1) * y_1j + (b - 1) * y_2j; //to be maximized
                            fx = -fx; //minimize
                            return fx;
                        }
                    };
                    Vector estimator = estimate(f, new DenseVector(a0, b0));
                    double F_old = f.evaluate(a0, b0);
                    double F_new = f.evaluate(estimator);
                    if (F_new < F_old) {
                        a0 = estimator.get(1);
                        b0 = estimator.get(2);
                    }
                }

                if (isAlphaEstimated && !isBetaEstimated) {
                    UnivariateRealFunction f = new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double a) {
                            double gamma_ab = gamma.evaluate(a + b0_temp);
                            double gamma_a = gamma.evaluate(a);
                            double fx = log(gamma_ab) - log(gamma_a) + (a - 1) * y_1j; //to be maximized
                            fx = -fx; //minimize
                            return fx;
                        }
                    };
                    Vector estimator = estimate(f, new DenseVector(a0));
                    double F_old = f.evaluate(a0);
                    double F_new = f.evaluate(estimator);
                    if (F_new < F_old) {
                        a0 = estimator.get(1);
                    }
                }

                if (!isAlphaEstimated && isBetaEstimated) {
                    UnivariateRealFunction f = new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double b) {
                            double gamma_ab = gamma.evaluate(a0_temp + b);
                            double gamma_b = gamma.evaluate(b);
                            double fx = log(gamma_ab) - log(gamma_b) + (b - 1) * y_2j; //to be maximized
                            fx = -fx; //minimize
                            return fx;
                        }
                    };
                    Vector estimator = estimate(f, new DenseVector(b0));
                    double F_old = f.evaluate(b0);
                    double F_new = f.evaluate(estimator);
                    if (F_new < F_old) {
                        b0 = estimator.get(1);
                    }
                }
            } catch (Exception ex) {
                //do nothing
            }
            lambda1[j - 1] = new Lambda(a0, b0);
        }

        return lambda1;
    }

    private Vector estimate(RealScalarFunction f, Vector v0) throws Exception {
        SQPActiveSetSolver optim = new SQPActiveSetSolver(epsilon, maxIterations);
        NonNegativityConstraintOptimProblem problem = new NonNegativityConstraintOptimProblem(f);
        SQPActiveSetSolver.Solution soln = optim.solve(problem);
        Vector estimator = soln.search(v0);
        return estimator;
    }

    @Override
    public HMMDistribution newEMDistribution(Object[] param) {
        Lambda[] lambda1 = Arrays.copyOf((Lambda[]) param, param.length);
        return new BetaDistribution(lambda1, isAlphaEstimated, isBetaEstimated, epsilon, maxIterations);
    }

    /** the gradient of the log-likelihood */
    private RealVectorFunction dF(final double log_x1_bar, final double log_x2_bar) {
        return new RealVectorFunction() {

            @Override
            public Vector evaluate(Vector x) {
                Vector dF = new DenseVector(2);
                double phi_ab = digamma.evaluate(x.get(1) + x.get(2));
                double phi_a = digamma.evaluate(x.get(1));
                double phi_b = digamma.evaluate(x.get(2));
                dF.set(1, phi_ab - phi_a + log_x1_bar);
                dF.set(2, phi_ab - phi_b + log_x2_bar);
                return dF;
            }

            @Override
            public int dimensionOfDomain() {
                return 2;
            }

            @Override
            public int dimensionOfRange() {
                return 2;
            }
        };
    }

    /** the Hessian of the log-likelihood */
    private RntoMatrix d2F() {
        return new RntoMatrix() {

            @Override
            public Matrix evaluate(Vector x) {
                Matrix H = new DenseMatrix(2, 2);//Hessian
                double phi_1_ab = trigamma.evaluate(x.get(1) + x.get(2));
                double phi_1_a = trigamma.evaluate(x.get(1));
                double phi_1_b = trigamma.evaluate(x.get(2));
                H.set(1, 1, phi_1_ab - phi_1_a);
                H.set(1, 2, phi_1_ab);
                H.set(2, 1, phi_1_ab);
                H.set(2, 2, phi_1_ab - phi_1_b);
                return H;
            }

            @Override
            public int dimensionOfDomain() {
                return 2;
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };
    }
}
