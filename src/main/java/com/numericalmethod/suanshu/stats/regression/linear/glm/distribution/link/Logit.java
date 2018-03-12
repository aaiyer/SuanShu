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

import com.numericalmethod.suanshu.stats.regression.linear.glm.GeneralizedLinearModel;
import static java.lang.Math.*;

/**
 * This class represents the link function:
 *
 * <blockquote><pre>
 *               mu
 * g(x) = log(--------)
 *             1 - mu
 * </pre></blockquote>
 *
 * @author Ken Yiu
 *
 * @see GeneralizedLinearModel
 */
public class Logit implements LinkFunction {

    /**
     * {@inheritDoc}
     *
     * <blockquote><pre>
     *               exp(x)
     * g_inv(x) = ------------
     *             1 + exp(x)
     * </pre></blockquote>
     *
     * @param x <i>x</i>
     * @return g<sup>-1</sup>(x)
     */
    @Override
    public double inverse(double x) {
        return exp(x) / (1 + exp(x));
    }

    /**
     * {@inheritDoc}
     * 
     * <blockquote><pre>
     *              1
     * g'(x) = -----------
     *          x (1 - x)
     * </pre></blockquote>
     *
     * @param x <i>x</i>
     * @return g'(x)
     */
    @Override
    public double derivative(double x) {
        return 1. / (x * (1. - x));
    }
}
