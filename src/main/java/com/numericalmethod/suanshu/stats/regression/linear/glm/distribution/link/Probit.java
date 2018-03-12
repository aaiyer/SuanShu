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
package com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link;

import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GeneralizedLinearModel;

/**
 * This class represents the link function:
 *
 * Inverse of cumulative distribution function of a NormalDistribution distribution <i>N(0, 1)</i>.
 *
 * @author Ken Yiu
 *
 * @see GeneralizedLinearModel
 */
public class Probit implements LinkFunction {

    private ProbabilityDistribution normal = new NormalDistribution(0, 1);

    @Override
    public double inverse(double x) {
        return normal.cdf(x);
    }

    @Override
    public double derivative(double x) {
        return 1. / normal.density(normal.quantile(x));
    }
}
