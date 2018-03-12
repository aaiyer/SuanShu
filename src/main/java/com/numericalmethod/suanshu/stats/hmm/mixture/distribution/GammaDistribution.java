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
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Trigamma;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.SQPActiveSetSolver;
import com.numericalmethod.suanshu.optimization.constrained.problem.NonNegativityConstraintOptimProblem;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.gamma.MarsagliaTsang2000;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import java.util.Arrays;

/**
 * The HMM states use the Gamma distribution to model the observations.
 *
 * @author Haksun Li
 * @see com.numericalmethod.suanshu.stats.distribution.univariate.GammaDistribution
 * @see <a href="http://en.wikipedia.org/wiki/Gamma_distribution">Wikipedia: Gamma distribution distribution</a>
 */
public class GammaDistribution implements HMMDistribution {

    /**
     * the Gamma distribution parameters
     */
    public static class Lambda {

        /** the shape parameter */
        public final double k;
        /** the scale parameter */
        public final double theta;

        /**
         * Store the Gamma distribution parameters.
         *
         * @param k     the shape parameter
         * @param theta the scale parameter
         */
        public Lambda(double k, double theta) {
            this.k = k;
            this.theta = theta;
        }
    }

    private final boolean isShapeEstimated;
    private final boolean isScaleEstimated;
    private final Lambda[] lambda;
    private final double epsilon;
    private final int maxIterations;
    private final Digamma digamma = new Digamma();
    private final Trigamma trigamma = new Trigamma();

    /**
     * Construct a Gamma distribution for each state in the HMM model.
     *
     * @param lambda           the distribution parameters
     * @param isShapeEstimated indicate whether the shape parameter {@code k} is to be estimated
     * @param isScaleEstimated indicate whether the scale parameter {@code theta} is to be estimated
     * @param epsilon          a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations    the maximum number of iterations in each M-step
     */
    public GammaDistribution(Lambda[] lambda, boolean isShapeEstimated, boolean isScaleEstimated, double epsilon, int maxIterations) {
        this.lambda = Arrays.copyOf(lambda, lambda.length);
        this.isShapeEstimated = isShapeEstimated;
        this.isScaleEstimated = isScaleEstimated;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a Gamma distribution for each state in the HMM model.
     *
     * @param lambda        the distribution parameters
     * @param maxIterations the maximum number of iterations in each M-step
     */
    public GammaDistribution(Lambda[] lambda, int maxIterations) {
        this(lambda, true, true, 1e-5, maxIterations);
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
            double k = lambda[i].k;
            double theta = lambda[i].theta;
            rng[i] = new MarsagliaTsang2000(k, theta);
        }

        return rng;
    }

    @Override
    public ProbabilityDistribution[] getDistributions() {
        ProbabilityDistribution[] dist = new ProbabilityDistribution[lambda.length];
        for (int i = 0; i < lambda.length; ++i) {
            double k = lambda[i].k;
            double theta = lambda[i].theta;
            dist[i] = new com.numericalmethod.suanshu.stats.distribution.univariate.GammaDistribution(k, theta);
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

        double[] x_bar = new double[m];
        double[] log_x_bar = new double[m];
        for (int j = 1; j <= m; ++j) {
            double numerator_x = 0.;
            double numerator_logx = 0.;
            double denominator = 0.;
            for (int i = 1; i <= n; ++i) {
                double u_ij = u.get(i, j);
                numerator_x += u_ij * observations[i - 1];
                numerator_logx += u_ij * logx[i - 1];
                denominator += u_ij;
            }

            x_bar[j - 1] = numerator_x / denominator;
            log_x_bar[j - 1] = numerator_logx / denominator;
        }

        Lambda[] lambda1 = new Lambda[lambda0.length];
        for (int j = 1; j <= m; ++j) {
            double k0 = lambda[j - 1].k;
            double theta0 = lambda[j - 1].theta;
            final double theta0_temp = theta0;
            final double y_1j = x_bar[j - 1];
            final double y_2j = log_x_bar[j - 1];

            try {
                if (isShapeEstimated && isScaleEstimated) {
                    BivariateRealFunction f = new BivariateRealFunction() {

                        @Override
                        public double evaluate(double k, double theta) {
                            double gamma_k = new GammaLanczosQuick().evaluate(k);
                            double fx = k * log(1. / theta) - log(gamma_k)
                                        + (k - 1) * y_2j - (1. / theta) * y_1j; //to be maximized
                            fx = -fx; //minimize
                            return fx;
                        }
                    };
                    Vector estimator = estimate(f, new DenseVector(k0, theta0));
                    double F_old = f.evaluate(k0, theta0);
                    double F_new = f.evaluate(estimator);
                    if (F_new < F_old) {
                        k0 = estimator.get(1);
                        theta0 = estimator.get(2);
                    }
                }

                if (isShapeEstimated && !isScaleEstimated) {
                    UnivariateRealFunction f = new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double k) {
                            double gamma_k = new GammaLanczosQuick().evaluate(k);//TODO: make parameter?
                            double fx = k * log(1. / theta0_temp) - log(gamma_k) + (k - 1) * y_2j; //to be maximized
                            fx = -fx; //minimize
                            return fx;
                        }
                    };
                    Vector estimator = estimate(f, new DenseVector(k0));
                    double F_old = f.evaluate(k0);
                    double F_new = f.evaluate(estimator);
                    if (F_new < F_old) {
                        k0 = estimator.get(1);
                    }
                }

                if (!isShapeEstimated && isScaleEstimated) {
                    double theta1 = x_bar[j - 1] / k0;
                    if (theta1 <= 0) {
                        break;
                    }
                    double dtheta = theta1 - theta0;
                    theta0 = theta1;

                    if (abs(dtheta) < epsilon) {
                        break;
                    }
                }
            } catch (Exception ex) {
                //do nothing
            }

            lambda1[j - 1] = new Lambda(k0, theta0);
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
        return new GammaDistribution(lambda1, isShapeEstimated, isScaleEstimated, epsilon, maxIterations);
    }

    /** the gradient of the log-likelihood */
    private RealVectorFunction dF(final double x_bar, final double log_x_bar) {
        return new RealVectorFunction() {

            @Override
            public Vector evaluate(Vector x) {
                double theta = x.get(1);
                double k = x.get(2);

                Vector dF = new DenseVector(2);
                double phi_k = digamma.evaluate(k);
                dF.set(1, k * theta - x_bar);
                dF.set(2, log(1. / theta) - phi_k + log_x_bar);
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
                double theta = x.get(1);
                double k = x.get(2);

                Matrix H = new DenseMatrix(2, 2);//Hessian
                double phi_1_k = trigamma.evaluate(k);
                H.set(1, 1, -k * theta * theta);
                H.set(1, 2, theta);
                H.set(2, 1, theta);
                H.set(2, 2, -phi_1_k);

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
