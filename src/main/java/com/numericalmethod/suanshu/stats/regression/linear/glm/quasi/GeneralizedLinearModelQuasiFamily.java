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

import com.numericalmethod.suanshu.stats.regression.linear.glm.IWLS;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * GLM for the quasi-families.
 *
 * <p>
 * In order to construct a likelihood function it is usually necessary to posit a probabilistic mechanism specifying, for a range of parameter values,
 * the probabilities of all relevant samples that might possibly have been observed.
 * Such a specification implies the knowledge of the mechanism by which the data were generated or substantial experience of similar data from previous experiments.
 * Often, this knowledge is not available.
 *
 * <p>
 * We may, however, be able to specify the range of possible response values and past experience with similar data is usually sufficient to specify,
 * in a qualitative fashion, a few additional characteristic features of the data.
 * From these characteristics, we may construct a quasi-likelihood function.
 *
 * <p>
 * Note that AIC is not computed for the quasi-GLM because there is no 'real' likelihood function.
 *
 * @author Chun Yip Yau
 *
 * @see "P. J. MacCullagh and J. A. Nelder, "An algorithm for fitting generalized linear models," in <i>Generalized Linear Models</i>, 2nd ed. Chapter 9."
 */
public class GeneralizedLinearModelQuasiFamily {

    /**
     * the quasi- generalized linear regression problem to be solved
     */
    public final QuasiGlmProblem problem;
    /**
     * the GLM coefficients β^ statistics
     */
    public final Beta beta;
    /**
     * the residual analysis of this quasi GLM regression
     */
    public final Residuals residuals;

    /**
     * Construct a <tt>GeneralizedLinearModelQuasiFamily</tt> instance.
     *
     * @param problem the quasi generalized linear regression problem to be solved
     */
    public GeneralizedLinearModelQuasiFamily(final QuasiGlmProblem problem) {
        this.problem = problem;

        /*
         * The initial values of quasi-GLM need to be close to the true values, (c.f., p.327).
         * We use the weighted linear regression fitting for GLM to get the initial values for the quasi-GLM.
         */
        IWLS irls = new IWLS(0, 1);//the precision does not matter
        irls.fit(problem, new DenseVector(problem.nFactors(), 0.0001));
        Vector betahat0 = irls.betaHat();

        NewtonRaphson fitting = new NewtonRaphson(1e-9, 100);
        fitting.fit(problem, betahat0);

        residuals = new Residuals(problem, fitting.mu());

        beta = new Beta(fitting, residuals);
    }
}
