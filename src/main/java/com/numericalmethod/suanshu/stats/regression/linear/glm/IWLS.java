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
package com.numericalmethod.suanshu.stats.regression.linear.glm;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.foreach;

/**
 * We estimate parameters ß in a GLM model using the Iteratively Re-weighted Least Squares algorithm.
 * The idea is that, at each iteration, we regress the adjusted, weighted, dependent variables on the same design matrix.
 *
 * <p>
 * The R equivalent function is {@code glm.fit}.
 *
 * @author Chun Yip Yau, Ken Yiu, Haksun Li
 *
 * @see
 * <ul>
 * <li>"P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models. Generalized Linear Models. 2nd ed. pp.40. Section 2.5."
 * <li><a href="http://en.wikipedia.org/wiki/Generalized_linear_model#Maximum_likelihood">Wikipedia: Maximum likelihood</a>
 * </ul>
 */
public class IWLS implements Fitting {

    /**
     * the convergence threshold
     */
    public final double threshold;
    /**
     * the maximum number of iterations
     */
    public final int maxIterations;
    private Run run;

    /**
     * Construct an instance to run the Iteratively Re-weighted Least Squares algorithm.
     *
     * @param threshold     the convergence threshold
     * @param maxIterations maximum number of iterations
     */
    public IWLS(double threshold, int maxIterations) {
        this.threshold = threshold;
        this.maxIterations = maxIterations;
    }

    public void fit(GLMProblem probelm, Vector beta0Initial) {
        run = new Run(probelm, beta0Initial);
    }

    public ImmutableVector mu() {
        return new ImmutableVector(run.mu);
    }

    public ImmutableVector betaHat() {
        return new ImmutableVector(run.beta0);
    }

    public ImmutableVector weights() {
        return new ImmutableVector(run.weights);
    }

    public double logLikelihood() {
        return run.logLikelihood0;
    }

    /**
     * the implementation of the IWLS algorithm
     */
    private class Run {

        //the generalized linear regression problem to be solved
        private final GLMProblem problem;
        //the link function for {@code family}
        private final LinkFunction link;
        //the following are fitting results
        private Vector beta0, mu, weights;
        private double logLikelihood0 = Double.POSITIVE_INFINITY;

        private Run(GLMProblem problem, Vector beta0Initial) {
            this.problem = problem;
            this.link = problem.family.link();

            this.beta0 = beta0Initial;
            solve();
        }

        private void solve() {
            final int nObs = problem.nObs();//number of observations hence equations

            Vector eta, beta1 = null;
            double logLikelihood1 = 0;

            boolean converged = false;
            for (int iter = 0; !converged && iter < maxIterations; iter++, logLikelihood0 = logLikelihood1, beta0 = beta1) {
                eta = iter == 0 ? new DenseVector(nObs, 0.05)//initial estimate of η; avoid 0 values
                      : problem.A.multiply(beta0);//the linear prediction, η = Xβ

                try {
                    mu = mu(eta);//the non-linear prediction μ
                    Vector dg = dg(mu);
                    weights = weights(mu, dg);
                    beta1 = beta1(eta, mu, weights, dg);
                    /*
                     * compute the log-likelihood (only the part related to theta)
                     *
                     * @see P. J. MacCullagh and J. A. Nelder. Generalized Linear Models. Second edition. Eq. 2.4. pp 28.
                     */
                    logLikelihood1 = 0;
                    for (int i = 1; i <= nObs; ++i) {
                        double theta = problem.family.theta(mu.get(i));
                        logLikelihood1 += problem.y.get(i) * theta - problem.family.cumulant(theta);
                    }

                    converged = beta0 != null ? beta1.minus(beta0).norm() < threshold : false;
                    converged |= Math.abs(logLikelihood1 - logLikelihood0) < threshold;
                } catch (ArithmeticException ex) {//TODO: is converged?
                    //TODO: use the values from the last iteration
                    break;
                }
            }

//            if (converged) {
//                return;
//            }
//
//            throw new RuntimeException("could not converge after maximal number of iterations");
        }

        /**
         * Compute μ from the inverse of the link function g^{-1}
         * μ = g^{-1}(Xβ), because g(E(y)) = g(μ) = Xβ = η.
         *
         * @param eta η
         * @return μ
         */
        private Vector mu(Vector eta) {
            return foreach(eta, new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    return link.inverse(x);
                }
            });
        }

        /**
         * Compute a vector value of dη/dμ at {@code mu}, element-wise.
         *
         * @param mu μ
         * @return vector [dg] evaluated at μ
         */
        private Vector dg(Vector mu) {
            return foreach(mu, new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    return link.derivative(x);
                }
            });
        }

        /**
         * Weighted linear regression fitting for GLM.
         *
         * @param eta     η
         * @param mu      μ
         * @param weights weights
         * @param dg      d(link)
         * @return a new estimate of ß
         *
         * @see "P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models. Generalized Linear Models. 2nd ed. pp.40. Section 2.5."
         */
        private Vector beta1(Vector eta, Vector mu, Vector weights, Vector dg) {
            //z = (η + (y - μ^) * dη/dμ)
            Vector z = eta.add(problem.y.minus(mu).multiply(dg));
            com.numericalmethod.suanshu.stats.regression.linear.LMProblem wols =
                    new com.numericalmethod.suanshu.stats.regression.linear.LMProblem(z, problem.A, false, weights);
            return (new OLSRegression(wols).beta.betaHat);
        }

        /**
         * <blockquote><i><pre>
         * W = 1 / [V(μ) * (dη/dμ)^2]
         *   = 1 / V(μ) / (dη/dμ)^2
         * </pre></i></blockquote>
         *
         * @see "P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models. Generalized Linear Models. 2nd ed. pp.40. Eq. 2.12. Section 2.5."
         *
         * @param mu μ
         * @param dg d(link) = dη/dμ
         * @return the weights
         */
        private Vector weights(Vector mu, Vector dg) {
            Vector oneOverVariance = foreach(mu, new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    if (DoubleUtils.isZero(problem.family.variance(x), 0)) {
                        throw new ArithmeticException("division by 0");
                    }

                    return 1. / problem.family.variance(x);
                }
            });

            return oneOverVariance.divide(dg.multiply(dg));
        }
    }

}
