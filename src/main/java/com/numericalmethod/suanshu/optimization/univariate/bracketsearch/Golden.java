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

import static com.numericalmethod.suanshu.Constant.GOLDEN_RATIO;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;

/**
 * This is the golden section univariate minimization algorithm.
 * On each iteration, it compares the distances to the lower and upper ends of the bracketing interval.
 * The bigger sub-interval is divided by the golden section (about 0.3189660...) at the new {@code next} point.
 * The value of the function at this new point is calculated and compared to the current minimum.
 * A new bracketing interval is then chosen in the usual manner as in {@link BracketSearch}.
 * Choosing the golden section as the bisection ratio gives the fastest convergence among the algorithms that converge linearly.
 * This implementation guarantees that the next guess lies inside the bracketing interval.
 *
 * @author Haksun Li
 */
public class Golden extends BracketSearch {

    /**
     * This is the solution to a Golden section univariate optimization.
     */
    public class Solution extends BracketSearch.Solution {

        private Solution(UnivariateRealFunction f) {
            super(f);
        }

        @Override
        public double search(double lower, double upper) {
            double initial = lower + golden * (upper - lower);
            return super.search(lower, initial, upper);
        }

        @Override
        protected boolean isMinFound() {
            return ((xu - xl) < epsilon);
        }

        @Override
        protected double xnext() {
            double w_upper = xu - xmin;
            double w_lower = xmin - xl;
            double result = xmin;
            result += golden * ((w_upper > w_lower) ? w_upper : -w_lower);//from GSL
//        result += golden * (((xmin < 0.5 * (xl + xu)) ? xu : xl) - xmin);//equivalent implementation from Numerical Recipes
            return result;
        }
    }

    private final double golden = 1.0 - 1 / GOLDEN_RATIO;

    /**
     * Construct a univariate minimizer using the Golden method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public Golden(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public Golden.Solution solve(C2OptimProblem problem) {
        return new Golden.Solution((UnivariateRealFunction) problem.f());
    }
}
