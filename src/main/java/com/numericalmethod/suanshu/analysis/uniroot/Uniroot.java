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
package com.numericalmethod.suanshu.analysis.uniroot;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * A root-finding algorithm is a numerical algorithm for finding a value <i>x</i> such that <i>f(x) = 0</i>, for a given function <i>f</i>.
 * Such an <i>x</i> is called a root of the function <i>f</i>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Root_finding"> Wikipedia: Root-finding algorithm</a>
 */
public interface Uniroot {

    /**
     * Search for a root, <i>x</i>, in the interval <i>[lower, upper]</i> such that <i>f(x) = 0</i>.
     *
     * @param f     a univariate function
     * @param lower the lower bound of the bracketing interval
     * @param upper the upper bound of the bracketing interval
     * @param guess an initial guess of the root within <i>[lower, upper]</i>.
     * Note that {@code guess} is a {@code double[]}.
     * This signature allows multiple initial guesses for certain types of uniroot algorithms, e.g., Brent's algorithm.
     * @return an approximate root
     * @throws NoRootFoundException when the search fails to find a root
     */
    public double solve(UnivariateRealFunction f, double lower, double upper, double... guess) throws NoRootFoundException;
}
