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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

/**
 * The Gamma function is an extension of the factorial function to real and complex numbers, with its argument shifted down by 1.
 * For real numbers, it is defined as:
 * \[
 * \Gamma(z) = \int_0^\infty e^{-t} t^{z-1} dt
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Gamma_function">Wikipedia: Gamma function</a>
 * <li><a href="http://en.wikipedia.org/wiki/Lanczos_approximation">Wikipedia: Lanczos approximation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Stirling%27s_approximation">Wikipedia: Stirling's approximation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Spouge%27s_approximation">Wikipedia: Spouge's approximation</a>
 * </ul>
 */
public interface Gamma {

    /**
     * Evaluate \(\Gamma(z) = \int_0^\infty e^{-t} t^{z-1} dt\).
     *
     * @param x <i>x</i>
     * @return \(\Gamma(z)\)
     */
    public double evaluate(double x);
}
