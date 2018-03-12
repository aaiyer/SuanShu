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
import static com.numericalmethod.suanshu.Constant.GOLDEN_RATIO;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import static java.lang.Math.abs;

/**
 * Brent's algorithm is the preferred method for finding the minimum of a univariate function.
 * It is able to recognize behavior of the function to switch between the golden ratio and parabolic approach.
 *
 * @author Haksun Li
 */
public class Brent extends BracketSearch {

    /**
     * This is the solution to a Brent's univariate optimization.
     */
    public class Solution extends BracketSearch.Solution {

        /**
         * the abscissae for the 2nd least value
         */
        private double w;
        /**
         * the abscissae for the 3rd least value; most of the time it is the old value of w
         */
        private double v;
        /**
         * <i>f(w)</i>
         */
        private double fw;
        /**
         * <i>f(v)</i>
         */
        private double fv;
        /**
         * the increment between two successive guesses
         */
        private double incLast = 0;

        private Solution(UnivariateRealFunction f) {
            super(f);
        }

        @Override
        public double search(double lower, double initial, double upper) {
            w = v = initial;
            fw = fv = f.evaluate(initial);
            super.search(lower, initial, upper);
            return xmin;//override the default return
        }

        @Override
        public double search(double lower, double upper) {
            double initial = lower + golden * (upper - lower);
            this.search(lower, initial, upper);
            return xmin;//override the default return
        }

        /**
         * {@inheritDoc}
         *
         * The search stops when
         * <ul>
         * \((x_u - x_l) < 2 \left | x \right | \varepsilon \)
         * \(\left | \rm xmin - \rm mid \right | < \left | x \right | \varepsilon\)
         * </ul>
         * <p/>
         * \((x_u - x_l) / 2 + \left | \rm xmin - \rm mid \right | < 2 \left | x \right | \varepsilon \approx \rm tol_2\)
         *
         * @return {@code true} if the minimum is found
         * @see "Chapter 10.3, Numerical Recipes."
         */
        @Override
        protected boolean isMinFound() {
            boolean result = false;
            double mid = 0.5 * (xu - xl);
            double tol1 = epsilon * abs(xmin) + EPSILON;//the machine epsilon is to protect against when the minimum happens to be at exactly 0, c.f., Chapter 10.3, Numerical Recipes
            double tol2 = 2 * tol1;
            if (0.5 * (xu - xl) + abs(xmin - mid) <= tol2) {
                result = true;
            }

            return result;
        }

        @Override
        protected double xnext() {
            double inc = 0;
            double tol1 = epsilon * abs(xmin) + EPSILON;//the machine epsilon is to protect against when the minimum happens to be at exactly 0, c.f., Chapter 10.3, Numerical Recipes

            double xmid = 0.5 * (xl + xu);
            inc = golden * (((xmin < xmid) ? xu : xl) - xmin);//default to a golden section division

            if (abs(incLast) > tol1) {//attempt a parabolic fit
                double x = xmin;
                double fx = fmin;

                /*
                 * the increment part in
                 * eq. 10.3.1 in Numerical Recipes (with signs +/- flipped and different symbols)
                 */
                double r = (x - w) * (fx - fv);
                double q = (x - v) * (fx - fw);
                double p = (x - v) * q - (x - w) * r;//the numerator
                q = 2 * (q - r);//the denominator

                if (q > 0) {
                    p = -p;
                } else {
                    q = -q;
                }

                /*
                 * There are two conditions.
                 * <ul>
                 * <li>increment, p/q, small enough, i.e., less than half the movement of the last step; in addition, q != 0
                 * <li>increment, p/q, in bounds; hence, new guess must fall within the current bracket interval
                 * </ul>
                 */
                if (abs(p) < abs(0.5 * q * incLast)
                    && (p > q * (xl - x)) && (p < q * (xu - x))) {
                    inc = p / q;//here, q != 0
                    double u = xmin + inc;

                    double t2 = 2 * tol1;
                    //f must not be evaluated too close to either bound of the bracket interval
                    if (((u - xl) < t2) || ((xu - u) < t2)) {
                        inc = (x < xmid) ? tol1 : -tol1;
                    }
                }
            }

            incLast = inc;

            return xmin + inc;
        }

        @Override
        protected void updateStates() {
            if (fnext < fmin) {//a new min is found
                v = w;//v = old value of w
                fv = fw;
                w = xmin;
                fw = fmin;
            } else {//fmin <= fnext; min stays the same
                if ((fnext <= fw) || (w == xmin)) {//find the 2nd least value
                    v = w;//v = old value of w
                    fv = fw;
                    w = xnext;
                    fw = fnext;
                } else if ((fnext <= fv) || (v == xmin) || (v == w)) {//fw < fnext; w remains the 2nd min
                    v = xnext;//v becomes the abscissae for the 3rd least value
                    fv = fnext;
                }
            }

            super.updateStates();
        }
    }

    private final double golden = 1.0 - 1 / GOLDEN_RATIO;

    /**
     * Construct a univariate minimizer using Brent's algorithm.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public Brent(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public Brent.Solution solve(C2OptimProblem problem) {
        return new Brent.Solution((UnivariateRealFunction) problem.f());
    }
}
