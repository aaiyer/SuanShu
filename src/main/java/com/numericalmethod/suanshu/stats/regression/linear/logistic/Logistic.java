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
package com.numericalmethod.suanshu.stats.regression.linear.logistic;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.unconstrained.MultivariateMaximizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * A logistic regression (sometimes called the logistic model or logit model)
 * is used for prediction of the probability of occurrence of an event by fitting data to a logit function logistic curve.
 * It is a generalized linear model used for binomial regression.
 *
 * <p>
 * This particular implementation works with binary data (y).
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Logistic_regression">Wikipedia: Logistic regression</a>
 * <li>P. J. MacCullagh and J. A. Nelder. "Likelihood functions for binary data," in <i>Generalized Linear Models</i>, 2nd ed. pp.114. Section 4.4."
 * </ul>
 */
public class Logistic {

    /**
     * the logistic regression problem to be solved
     */
    public final LogisticProblem problem;
    /**
     * the β^ statistics
     */
    public final Beta beta;
    /**
     * the residual analysis of this regression
     */
    public final Residuals residuals;
    /**
     * the maximum log-likelihood
     */
    public final double ML;
    /**
     * the AIC
     */
    public final double AIC;

    /**
     * Construct a <tt>Logistic</tt> instance.
     *
     * @param problem the logistic regression problem to be solved
     */
    public Logistic(final com.numericalmethod.suanshu.stats.regression.linear.LMProblem problem) throws Exception {
        this.problem = new LogisticProblem(problem);

        //number of variables (including the intercept, if any)
        final int m = problem.nFactors();

        //the log-likelihood function
        RealScalarFunction L = new LogLikelihood().function(this.problem);

        //fitting a logisitic regression by finding β^ using the maximum likelihood method
        MultivariateMaximizer maximizer = new MultivariateMaximizer(0, 200);
        MultivariateMaximizer.Solution soln = maximizer.solve(new C2OptimProblemImpl(L));
        Vector betaHat = soln.search(new DenseVector(m));//initial values are set to be zero
        ML = L.evaluate(betaHat);
        residuals = new Residuals(this.problem, betaHat);
        beta = new Beta(betaHat, residuals);
        AIC = 2 * m - 2 * ML;
    }
}
