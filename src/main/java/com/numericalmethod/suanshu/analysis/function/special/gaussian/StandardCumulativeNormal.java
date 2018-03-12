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
package com.numericalmethod.suanshu.analysis.function.special.gaussian;

/**
 * The cumulative Normal distribution function describes the probability of a Normal random variable falling in the interval \((-\infty, x]\).
 * It is defined as:
 * /[
 * F(x;\,\mu,\sigma^2)
 * = \Phi\left(\frac{x-\mu}{\sigma}\right)
 * = \frac12\left[\, 1 + \operatorname{erf}\left(\frac{x-\mu}{\sigma\sqrt{2}}\right)\,\right],\quad x\in\mathbb{R}
 * /]
 * <p/>
 * The R equivalent function is {@code pnorm}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Cumulative_normal#Cumulative_distribution_function">Wikipedia: Cumulative distribution function</a>
 */
public interface StandardCumulativeNormal {

    /**
     * Evaluate \(F(x;\,\mu,\sigma^2)\).
     *
     * @param x <i>x</i>
     * @return \(F(x;\,1,1)\)
     */
    public double evaluate(double x);
}
