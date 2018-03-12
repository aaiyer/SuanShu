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
 * The Newton–Raphson method is as follows:
 * one starts with an initial guess which is reasonably close to the true root,
 * then the function is approximated by its tangent line (which can be computed using the tools of calculus),
 * and one computes the x-intercept of this tangent line (which is easily done with elementary algebra).
 * This x-intercept will typically be a better approximation to the function's root than the original guess, and the method can be iterated.
 * It has the following properties.
 * <ul>
 * <li>The function to be solved is assumed to be continuous and smooth (1st derivative exists).
 * <li>The solution converges quadratically, when the multiplicity of the root is 1; otherwise, it is linear.
 * <li>The solution may fail to converge when the derivative is or is close to 0.
 * <li>The solution may fail to converge if the initial guess is far away from the true value.
 * </ul>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Newton_method"> Wikipedia: Newton's method</a>
 */
public class Newton implements Uniroot {

    private final double tol;
    private final int maxIterations;

    /**
     * Construct an instance of Newton's root finding algorithm.
     *
     * @param tol           the convergence tolerance
     * @param maxIterations the maximum number of iterations
     */
    public Newton(double tol, int maxIterations) {
        this.tol = tol;
        this.maxIterations = maxIterations;
    }

    @Override
    public double solve(UnivariateRealFunction f, double lower, double upper, double... guess) throws NoRootFoundException {
        return solve(f, guess[0]);
    }

    public double solve(UnivariateRealFunction f, double guess) throws NoRootFoundException {
        return solve(f, null, guess);
    }

    /**
     * Search for a root, <i>x</i>, in the interval <i>[lower, upper]</i> such that <i>f(x) = 0</i>.
     *
     * @param f     a univariate function
     * @param df    the first order derivative
     * @param guess an initial guess of the root within <i>[lower, upper]</i>
     * @return an approximate root
     * @throws NoRootFoundException when the search fails to find a root
     */
    public double solve(UnivariateRealFunction f, UnivariateRealFunction df, double guess) throws NoRootFoundException {
        UnivariateRealFunction df_ = df == null ? new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL) : df;

        double xn = guess;//x<sub>n<sub>
        double xn1 = xn;//x<sub>n+1<sub>
        double fx = f.evaluate(xn1);

        for (int count = 0; count < maxIterations && Math.abs(fx) > tol; ++count) {
            xn1 = xn - f.evaluate(xn) / df_.evaluate(xn);//the updating rule
            fx = f.evaluate(xn1);
            xn = xn1;
        }

        return xn1;
    }
}
