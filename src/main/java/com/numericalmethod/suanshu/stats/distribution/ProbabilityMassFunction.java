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
package com.numericalmethod.suanshu.stats.distribution;

/**
 * A probability mass function (pmf) is a function that gives the probability that a discrete random variable is exactly equal to some value.
 * Suppose that <i>X: S → R</i> is a discrete random variable defined on a sample space <i>S</i>.
 * The probability mass function <i>f<sub>X</sub>: R → [0, 1]</i> for <i>X</i> is defined as
 * <blockquote><i>
 * f<sub>X</sub>(x) = Pr(X = x) = Pr({s ∈ S : X(s) = x})
 * </i></blockquote>
 *
 * @param <X> the discrete random variable (domain) type
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Probability_mass_function">Wikipedia: Probability mass function</a>
 */
public interface ProbabilityMassFunction<X> {

    /**
     * Compute the probability mass for a discrete realization <i>x</i>.
     *
     * @param x a realization
     * @return {@code pmf(x)}
     */
    public double evaluate(X x);
}
