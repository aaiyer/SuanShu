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

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The Generalized Linear Model (GLM) is a flexible generalization of ordinary least squares regression.
 * The GLM generalizes linear regression by allowing the linear model to be related to the response variable via a link function and
 * by allowing the magnitude of the variance of each measurement to be a function of its predicted value.
 *
 * <p>
 * In a GLM, each outcome of the dependent variables, <i>Y</i>, is assumed to be generated from a particular distribution in the exponential family,
 * a large range of probability distributions that includes the normal, binomial and Poisson distributions, among others.
 * The mean, <i>μ</i>, of the distribution depends on the independent variables, <i>X</i>, through
 * <blockquote><code><pre>
 * E(Y) = μ = g<sup>-1</sup>(Xβ)
 * </pre></code></blockquote>
 *
 * where <i>E(Y)</i> is the expected value of <i>Y</i>;
 * <i>Xβ</i> is the linear predictor, a linear combination of unknown parameters, <i>β</i>;
 * <i>g</i> is the link function.
 * 
 * <p>
 * The R equivalent function is {@code glm}.
 *
 * @author Chun Yip Yau, Ken Yiu, Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Generalized_linear_model">Wikipedia: Generalized linear model</a>
 * <li>"P. J. MacCullagh and J. A. Nelder. An algorithm for fitting generalized linear models," in <i>Generalized Linear Models</i>, 2nd ed. pp.40. Section 2.5."
 * </ul>
 */
public class GeneralizedLinearModel {

    /**
     * the generalized linear regression problem to be solved
     */
    public final GLMProblem problem;
    /**
     * the GLM coefficients β^ statistics
     */
    public final Beta beta;
    /**
     * the residual analysis of this GLM regression
     */
    public final Residuals residuals;
    /*
     * Akaike information criterion (AIC)
     */
    public final double AIC;

    /**
     * Construct a <tt>GeneralizedLinearModel</tt> instance.
     *
     * @param problem the generalized linear regression problem to be solved
     * @param fitting the fitting method, c.f., {@link Fitting}
     */
    public GeneralizedLinearModel(GLMProblem problem, Fitting fitting) {
        this.problem = problem;

        fitting.fit(problem, new DenseVector(problem.nFactors(), 0.0001));

        residuals = new Residuals(problem, fitting.mu());

        beta = new Beta(fitting, residuals);

        AIC = this.problem.family.AIC(this.problem.y, fitting.mu(), fitting.weights(), fitting.logLikelihood(), residuals.deviance, problem.nFactors());
    }

    /**
     * Solve a generalized linear problem using the Iterative Re-weighted Least Squares algorithm.
     *
     * @param problem the generalized linear regression problem to be solved
     */
    public GeneralizedLinearModel(GLMProblem problem) {
        this(problem, new IWLS(1e-10, 100));
    }
}
