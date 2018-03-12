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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi;

import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.Fitting;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Family;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Newton-Raphson method is an iterative algorithm to estimate the β of the quasi GLM regression.
 * It is a maximum likelihood method.
 *
 * @author Chun Yip Yau, Haksun Li
 *
 * @see
 * <ul>
 * "P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models," in <i>Generalized Linear Models</i>, 2nd ed. pp.327."
 * <a href="http://en.wikipedia.org/wiki/Generalized_linear_model#Fitting">Wikipedia: Fitting</a>
 * </ul>
 *
 */
class NewtonRaphson implements Fitting {

    /**
     * the convergence threshold
     */
    final double threshold;
    /**
     * the maximum number of iterations
     */
    final int maxIterations;
    private Run run;

    /**
     * Construct an instance to run the Newton-Raphson method iteratively.
     *
     * @param threshold the convergence threshold
     * @param maxIterations maximum number of iterations
     */
    NewtonRaphson(double threshold, int maxIterations) {
        this.threshold = threshold;
        this.maxIterations = maxIterations;
    }

    @Override
    public void fit(com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem problem, Vector beta0Initial) {
        if (!(problem instanceof QuasiGlmProblem)) {//TODO: bad coding style
            throw new RuntimeException("the problem is not setup for quasi GLM");
        }

        run = new Run((QuasiGlmProblem) problem, beta0Initial);
    }

    @Override
    public ImmutableVector mu() {
        return new ImmutableVector(run.mu);
    }

    @Override
    public ImmutableVector betaHat() {
        return new ImmutableVector(run.beta0);
    }

    @Override
    public double logLikelihood() {
        return run.logLikelihood0;
    }

    @Override
    public ImmutableVector weights() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Compute D.
     *
     * <blockquote><code><pre>
     * D = d(μ)/d(β) = (dμ/dη) * (dη/dβ)
     * </pre></code></blockquote>
     *
     * @return D
     */
    public ImmutableMatrix D() {
        return new ImmutableMatrix(run.D);
    }

    /**
     * Compute D / V(μ).
     *
     * @return D / V(μ)
     */
    public ImmutableMatrix DVInv() {
        return new ImmutableMatrix(run.DVInv);
    }

    /**
     * an implementation of the Newton-Raphson algorithm
     */
    private class Run {

        //the generalized linear regression problem to be solved
        private final QuasiGlmProblem problem;
        //the following are fitting results
        private Vector beta0, mu;
        private double logLikelihood0 = Double.POSITIVE_INFINITY;
        private Matrix D, DVInv;

        private Run(QuasiGlmProblem problem, Vector beta0Initial) {
            this.problem = problem;

            this.beta0 = beta0Initial;
            solve();
        }

        /**
         * an implementation of the Newton-Raphson algorithm
         * 
         * @see "P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models," in <i>Generalized Linear Models</i>, 2nd ed. pp.327."
         */
        private void solve() {
            final int n = problem.nObs();//number of observations hence equations
            final int m = beta0.size();
            final Family family = problem.quasiFamily.toFamily();

            mu = new DenseVector(n);
            D = new DenseMatrix(n, m);

            Vector beta1 = new DenseVector(m);
            double logLikelihood1;

            boolean converged = false;
            for (int iter = 0; !converged && iter < maxIterations; iter++, logLikelihood0 = logLikelihood1, beta0 = beta1) {
                Vector eta = this.problem.A.multiply(beta0);

                for (int i = 1; i <= n; ++i) {
                    //μ = g^{-1}(η), where η = g(μ) is the link function
                    double mu_i = family.link().inverse(eta.get(i));
                    mu.set(i, mu_i);

                    for (int j = 1; j <= m; j++) {
                        //D = d(μ)/d(β) = (dμ/dη) * (dη/dβ)
                        D.set(i, j, this.problem.A.get(i, j) / family.link().derivative(mu_i));
                    }
                }

                //a diagonal matrix with elements 1/V(μ)
                Matrix VInv = new DiagonalMatrix(n);
                for (int i = 1; i <= n; ++i) {
                    VInv.set(i, i, 1 / family.variance(mu.get(i)));
                }

                DVInv = D.t().multiply(VInv);

                Vector residuals = new ImmutableVector(this.problem.y.minus(mu));
                Vector tmp = new Inverse(DVInv.multiply(D)).multiply(DVInv).multiply(residuals);//the formula at the bottom of pp.327
                beta1 = beta0.add(tmp);

                /*
                 * compute the log-likelihood
                 *
                 * @see P. J. MacCullagh and J. A. Nelder. Generalized Linear Models. 2nd edition. Table 9.1.
                 */
                logLikelihood1 = 0;
                for (int i = 1; i <= n; i++) {
                    logLikelihood1 += problem.quasiFamily.quasiLikelihood(mu.get(i), problem.y.get(i));
                }

                converged = beta0 != null ? beta1.minus(beta0).norm() < threshold : false;
                converged |= Math.abs(logLikelihood1 - logLikelihood0) < threshold;
            }
        }
    }
}
