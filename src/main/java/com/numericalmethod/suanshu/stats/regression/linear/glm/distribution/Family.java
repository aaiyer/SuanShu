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
package com.numericalmethod.suanshu.stats.regression.linear.glm.distribution;

import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;

/**
 * <tt>Family</tt> is a description of the error distribution and link function to be used in the GLM model.
 *
 * <p>
 * The R equivalent function is {@code family}.
 *
 * @author Haksun Li
 */
public abstract class Family implements ExponentialDistribution {

    /**
     * the link function of this distribution
     */
    private final LinkFunction link;

    /**
     * Construct an instance of <tt>Family</tt>.
     * 
     * @param link the link function of this distribution
     */
    public Family(LinkFunction link) {
        this.link = link;
    }

    /**
     * Get the link function of this distribution.
     *
     * @return the link function of this distribution
     */
    public LinkFunction link() {
        return link;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The definition above is the default implementation of this function, a
     * subclass of {@link ExponentialDistribution} may override this
     * function to implement a simplified expression for efficiency or handle
     * special values.
     *
     * @param y the observed value
     * @param mu the <em>estimated</em> mean, μ^
     * @return the deviance
     *
     * @see
     * <ul>
     * <li>P. J. MacCullagh and J. A. Nelder, "Measuring the goodness-of-fit," <i>Generalized Linear Models,<i> 2nd ed. Section 2.3. pp.34.
     * <li><a href="http://en.wikipedia.org/wiki/Deviance_%28statistics%29">Wikipedia: Deviance<a>
     * </ul>
     */
    public double deviance(double y, double mu) {
        double thetaY = theta(y);
        double thetaMu = theta(mu);
        return 2 * ((y * thetaY - cumulant(thetaY)) - (y * thetaMu - cumulant(thetaMu)));
    }
}
