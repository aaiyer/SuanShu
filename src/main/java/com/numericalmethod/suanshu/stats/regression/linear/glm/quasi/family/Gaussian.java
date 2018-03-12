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
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Identity;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import static java.lang.Math.*;

/**
 * The quasi Gaussian family of GLM.
 *
 * <p>
 * The R equivalent function is {@code quasi}.
 *
 * @author Haksun Li
 */
public class Gaussian extends com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Gaussian implements QuasiFamily {

    /**
     * Create an instance of {@code Gaussian}.
     * The canonical link is {@link Identity}.
     *
     * @see "pp.32. Section 2.2.4. Measuring the goodness-of-fit. Generalized Linear Models. 2nd ed. P. J. MacCullagh and J. A. Nelder."
     */
    public Gaussian() {
    }

    /**
     * Create an instance of {@code Gaussian} with an overriding link function.
     *
     * @param link the overriding link function
     */
    public Gaussian(LinkFunction link) {
        super(link);
    }

    public double quasiLikelihood(double mu, double y) {
        return -pow(y - mu, 2) / 2;
    }

    public double quasiDeviance(double y, double mu) {
        return pow(y - mu, 2);//both RHSs in eq. 9.4 are the same
    }

    public Family toFamily() {
        return this;
    }
}
