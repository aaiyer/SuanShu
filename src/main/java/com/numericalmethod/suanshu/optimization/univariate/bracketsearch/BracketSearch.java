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
package com.numericalmethod.suanshu.optimization.univariate.bracketsearch;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.univariate.UnivariateMinimizer;
import static java.lang.Math.sqrt;

/**
 * This class provides implementation support for those univariate optimization algorithms that are based on bracketing.
 * Examples are Bisection method, Fibonacci search, and Golden-section search.
 * Starting with a 3-point bracketing interval of a minimum, i.e., <i>x<sup>l</sup>, x<sup>a</sup>, x<sup>u</sup>,</i>
 * the search iteratively reduce the length of the interval.
 * It computes a 4th point, <i>x<sup>b</sup></i>, according to an interval dividing schedule, to form two overlapping sub-intervals, e.g.,
 * <i>[x<sup>l</sup>, x<sup>a</sup>]</i>, and
 * <i>[x<sup>b</sup>, x<sup>u</sup>]</i>.
 * The sub-interval that contains the new minimum is chosen. Repeat the procedure until convergence.
 * This algorithm is most effective for a uni-modal function in interval
 * <i>[x<sup>l</sup>, x<sup>u</sup>]</i>.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou and Wu-Sheng Lu, "Chapter 4," Practical Optimization: Algorithms and Engineering Applications. Springer."
 */
public abstract class BracketSearch implements UnivariateMinimizer {

    protected abstract class Solution implements UnivariateMinimizer.Solution {

        protected UnivariateRealFunction f;

        protected Solution(UnivariateRealFunction f) {
            this.f = f;
        }
        /**
         * the best minimum found so far
         */
        protected double fmin;
        /**
         * the next guess of the minimum
         */
        protected double fnext;
        /**
         * the lower bound of the bracketing interval
         */
        protected double xl;
        /**
         * the upper bound of the bracketing interval
         */
        protected double xu;
        /**
         * the best minimizer found so far
         */
        protected double xmin;
        /**
         * the next best guess of the minimizer
         */
        protected double xnext;
        /**
         * the current iteration count
         */
        protected int iter;

        /**
         * the convergence criterion
         *
         * @return {@code true} if the current min value is considered to have converged within a threshold
         */
        protected abstract boolean isMinFound();

        /**
         * Compute the next best estimate within the bracketing interval.
         * A particular univariate minimization algorithm implements the logic in this method.
         *
         * @return the next best guess of the minimizer
         */
        protected abstract double xnext();

        @Override
        public double search(double lower, double initial, double upper) {
            assertArgument(lower < initial && initial < upper, "invalid bracket interval");
            assertArgument(isBracketing(lower, initial, upper), "the interval specified may not bracket a minimum");

            xl = lower;
            xu = upper;
            xmin = initial;
            fmin = f.evaluate(xmin);

            iter = 1;
            for (; iter <= maxIterations; ++iter) {
                //stopping criterion
                if (isMinFound()) {
                    break;
                }

                //compute the next best guess
                xnext = xnext();
                if (xnext <= xl || xu <= xnext) {//next guess lies outside the bracketing interval
                    break;
                }
                fnext = f.evaluate(xnext);

                updateStates();
            }

            double xmid = 0.5 * (xl + xu);
            return xmid;
        }

        /**
         * Update the bracketing interval and the best min found so far.
         */
        protected void updateStates() {
            if (fnext < fmin) {//a new min found
                if (xnext < xmin) {
                    xu = xmin;
                } else {
                    xl = xmin;
                }
                xmin = xnext;
                fmin = fnext;
            } else { //if (fmin < fnext) {//min stays the same
                if (xnext < xmin) {
                    xl = xnext;
                } else {
                    xu = xnext;
                }
            }
        }

        /**
         * Check whether <i>[xl, xu]</i> is bracketing <i>x</i>.
         *
         * @param xl the lower bound of the bracketing interval
         * @param x  a guess of the minimizer
         * @param xu the upper bound of the bracketing interval
         * @return {@code true} if <i>fx &lt; fl && fx &lt; fu</i>
         */
        protected boolean isBracketing(double xl, double x, double xu) {
            double fl = f.evaluate(xl);
            double fx = f.evaluate(x);
            double fu = f.evaluate(xu);

            boolean result = false;
            if (fx < fl && fx < fu) {
                result = true;
            }

            return result;
        }

        @Override
        public double minimum() {
            return fmin;
        }

        @Override
        public Double minimizer() {
            return xmin;
        }
    }

    /**
     * the convergence tolerance
     */
    protected final double epsilon;
    /**
     * the maximum number of iterations
     */
    protected final int maxIterations;

    /**
     * Construct a univariate minimizer using a bracket search method.
     *
     * @param epsilon       the convergence tolerance; It should be no less than the square root of the machine precision.
     * @param maxIterations the maximum number of iterations
     */
    protected BracketSearch(double epsilon, int maxIterations) {
        double maxeps = sqrt(EPSILON);
        this.epsilon = epsilon < maxeps ? maxeps : epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Minimize a univariate function.
     *
     * @param f the objective function
     * @return the minimizer
     * @throws Exception
     */
    public UnivariateMinimizer.Solution solve(UnivariateRealFunction f) throws Exception {
        return solve(new C2OptimProblemImpl(f));
    }
}
