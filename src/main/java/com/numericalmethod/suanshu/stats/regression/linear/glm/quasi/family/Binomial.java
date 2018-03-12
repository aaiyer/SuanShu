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

import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Family;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Logit;
import static java.lang.Math.log;

/**
 * The quasi Binomial family of GLM.
 *
 * <p>
 * The R equivalent function is {@code quasibinomial}.
 *
 * @author Haksun Li
 */
public class Binomial extends com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Binomial implements QuasiFamily {

    /**
     * Construct an instance of {@code Binomial}.
     * The canonical link is {@link Logit}.
     *
     * @see "pp.32. Section 2.2.4. Measuring the goodness-of-fit. Generalized Linear Models. 2nd ed. P. J. MacCullagh and J. A. Nelder."
     */
    public Binomial() {
    }

    /**
     * Construct an instance of {@code Binomial} with an overriding link function.
     *
     * @param link the overriding link function
     */
    public Binomial(LinkFunction link) {
        super(link);
    }

    public double quasiLikelihood(double mu, double y) {
        return y * log(mu / (1 - mu)) + log(1 - mu);
    }

    public double quasiDeviance(double y, double mu) {
        /*
         * This is derived using the integral form in Eq. 9.4.
         * As this is a binomial model, y is either 1 or 0. Some log terms vanish.
         * Also, the invalid log terms, e.g., log(0) and log(-1), are taken to be 0.
         */
        return 2 * (-y * log(mu / (1 - mu)) - log(1 - mu));
    }

    public Family toFamily() {
        return this;
    }
}
