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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;

/**
 * The Fibonacci search is a dichotomous search where a bracketing interval is sub-divided by the Fibonacci ratio.
 * This implementation runs the risk that the next guess may lie outside the bracketing interval.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 4.1," Practical Optimization: Algorithms and Engineering Applications. Springer."
 */
public class Fibonacci extends BracketSearch {

    /**
     * This is the solution to a Fibonacci's univariate optimization.
     */
    public class Solution extends BracketSearch.Solution {

        private double[] fibonacci;
        private double Ik;
        private double xlLast, xuLast;

        private Solution(UnivariateRealFunction f) {
            super(f);
        }

        @Override
        public double search(double lower, double initial, double upper) {
            initialize(lower, upper);
            return super.search(lower, initial, upper);
        }

        @Override
        public double search(double lower, double upper) {
            initialize(lower, upper);
            double initial = upper - Ik;
            return super.search(lower, initial, upper);
        }

        /**
         * This algorithm stops only after a pre-specified number of iterations.
         * This is to guarantee the search interval is reduced to certain size, or a fraction of the input interval.
         *
         * @return false
         */
        @Override
        protected boolean isMinFound() {
            return false;
        }

        @Override
        protected double xnext() {
            if (iter == 1) {
                return xlLast + Ik;
            }

            int offset = fibonacci.length - iter;
            Ik *= fibonacci[offset - 1] / fibonacci[offset];//eq. 4.6

            if (compare(xuLast, xu, 0) != 0) {//upper bound moved
                xuLast = xu;
                return xu - Ik;
            } else if (compare(xlLast, xl, 0) != 0) {//lower bound moved
                xlLast = xl;
                return xl + Ik;
            } else {
                throw new RuntimeException("cannot identify the moving bound");
            }
        }

        private void initialize(double lower, double upper) {
            fibonacci = new com.numericalmethod.suanshu.analysis.sequence.Fibonacci(maxIterations + 2).getAll();

            xlLast = lower;
            xuLast = upper;

            double I1 = upper - lower;
            Ik = I1 * fibonacci[fibonacci.length - 2] / fibonacci[fibonacci.length - 1];//eq. 4.6
        }
    }

    /**
     * Construct a univariate minimizer using the Fibonacci method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public Fibonacci(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public Fibonacci.Solution solve(C2OptimProblem problem) {
        return new Fibonacci.Solution((UnivariateRealFunction) problem.f());
    }
}
