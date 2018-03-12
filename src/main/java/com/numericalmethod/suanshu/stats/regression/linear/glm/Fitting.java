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

import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface represents a fitting method for estimating <i>β</i> in a Generalized Linear Model (GLM).
 *
 * <p>
 * John Nelder and Robert Wedderburn proposed an iteratively re-weighted least squares method for maximum likelihood estimation of the model parameters, <i>β</i>.
 * Maximum-likelihood estimation remains popular and is the default method on many statistical computing packages.
 * Other approaches, including Bayesian approaches and least squares fits to variance stabilized responses, have been developed.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Generalized_linear_model">Wikipedia: Generalized linear model</a>
 * <li> {@link IWLS}
 * </ul>
 */
public interface Fitting {

    /**
     * Fit a Generalized Linear Model.
     *
     * <p>
     * This method must be called before the three get methods.
     *
     * @param problem the generalized linear regression problem to be solved
     * @param beta0Initial initial guess for betaHat
     */
    public void fit(GLMProblem problem, Vector beta0Initial);

    /**
     * Get <i>μ</i> as in
     * <blockquote><code><pre>
     * E(Y) = μ = g<sup>-1</sup>(Xβ)
     * </pre></code></blockquote>
     *
     * @return μ
     */
    public ImmutableVector mu();

    /**
     * Get the estimates of <i>β</i>, β^, as in
     * <blockquote><code><pre>
     * E(Y) = μ = g<sup>-1</sup>(Xβ)
     * </pre></code></blockquote>
     *
     * @return β^
     */
    public ImmutableVector betaHat();

    /**
     * Get the weights to the observations.
     * 
     * @return the weights
     */
    public ImmutableVector weights();

    public double logLikelihood();//TODO: this is not really the likelihood
}
