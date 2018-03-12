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

import com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * Halley's method is an iterative root finding method for a univariate function
 * with a continuous second derivative, i.e., a C<sup>2</sup> function.
 * It has the following properties.
 * <ul>
 * <li>The function to be solved is assumed to be continuous and smooth (1st derivative exists).
 * <li>The 1st derivative is assumed to be continuous and smooth (2nd derivative exists).
 * <li>The rate of convergence for solution is cubic.
 * </ul>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Halley_method"> Wikipedia: Halley's method</a>
 */
public class Halley implements Uniroot {

    private final double tol;
    private final int maxIterations;

    /**
     * Construct an instance of Halley's root finding algorithm.
     *
     * @param tol           the convergence tolerance
     * @param maxIterations the maximum number of iterations
     */
    public Halley(double tol, int maxIterations) {
        this.tol = tol;
        this.maxIterations = maxIterations;
    }

    @Override
    public double solve(UnivariateRealFunction f, double lower, double upper, double... guess) throws NoRootFoundException {
        return solve(f, guess[0]);
    }

    public double solve(UnivariateRealFunction f, double guess) throws NoRootFoundException {
        return solve(f, null, null, guess);
    }

    /**
     * Search for a root, <i>x</i>, in the interval <i>[lower, upper]</i> such that <i>f(x) = 0</i>.
     *
     * @param f     a univariate function
     * @param df    the first order derivative
     * @param d2f   the second order derivative
     * @param guess an initial guess of the root within <i>[lower, upper]</i>
     * @return an approximate root
     * @throws NoRootFoundException when the search fails to find a root
     */
    public double solve(UnivariateRealFunction f, UnivariateRealFunction df, UnivariateRealFunction d2f, double guess) throws NoRootFoundException {
        UnivariateRealFunction df_ = df == null ? new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL) : df;
        UnivariateRealFunction d2f_ = d2f == null ? new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL) : d2f;

        double xn = guess;//x<sub>n<sub>
        double xn1 = xn;//x<sub>n+1<sub>
        double fx = f.evaluate(xn1);

        for (int count = 0; count < maxIterations && Math.abs(fx) > tol; ++count) {
            double dfx = df_.evaluate(xn);

            //the updating rule
            xn1 = 2 * fx * dfx;
            xn1 /= 2 * dfx * dfx - fx * d2f_.evaluate(xn);
            xn1 = xn - xn1;

            fx = f.evaluate(xn1);
            xn = xn1;
        }

        return xn1;
    }
}
