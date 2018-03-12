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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family;

import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.ExponentialDistribution;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Family;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;

/**
 * This interface represents the quasi family of GLM.
 *
 * @author Haksun Li
 */
public interface QuasiFamily extends ExponentialDistribution {

    public Family toFamily();

    /**
     *
     * @return the link function of this distribution
     */
    public LinkFunction link();

    /**
     * the quasi-likelihood function corresponding to a single observation <i>Q(μ; y)</i>
     * 
     * @param mu <i>μ</i>
     * @param y <i>y</i>
     * @return <i>Q(μ; y)</i>
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 9. Table 9.1. p.326."
     */
    public double quasiLikelihood(double mu, double y);

    /**
     * the quasi-deviance function corresponding to a single observation
     *
     * @param y <i>y</i>
     * @param mu <i>μ</i>
     * @return <i>D(y; μ;)</i>
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 9. Eq. 9.4., the integral form, p.327."
     */
    public double quasiDeviance(double y, double mu);
}
