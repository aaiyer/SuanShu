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
package com.numericalmethod.suanshu.optimization.univariate;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import static java.lang.Math.sqrt;

/**
 * This performs a grid search to find the minimum of a univariate function.
 * This procedure may be appropriate when the bracketing interval is difficult to determine.
 *
 * @author Haksun Li
 */
public class GridSearch implements UnivariateMinimizer {

    /**
     * This is the solution to the {@code GridSearch}.
     */
    public class Solution implements UnivariateMinimizer.Solution {

        private final UnivariateRealFunction f;
        private double fmin;//the best minimum found so far
        private double xmin;//the best {@code xmin} found so far

        private Solution(UnivariateRealFunction f) {
            this.f = f;
        }

        /**
         * {@inheritDoc}
         *
         * @param lower   the lower bound
         * @param initial <em>not used</em>
         * @param upper   the upper bound
         * @return the minimizer found
         */
        @Override
        public double search(double lower, double initial, double upper) {
            assertArgument(lower < upper, "invalid bracket interval");

            fmin = Double.POSITIVE_INFINITY;
            double step = (upper - lower) / maxIterations;
            final double[] x = R.seq(lower, upper, step);
            final double[] fx = new double[x.length];

            try {// try multi-thread
                new ParallelExecutor().forLoop(
                        0,
                        x.length,
                        new LoopBody() {

                            @Override
                            public void run(int i) throws Exception {
                                double fx_i = f.evaluate(x[i]);
                                fx[i] = fx_i;
                            }
                        });

                fmin = DoubleArrayMath.min(fx);
                int i = DoubleUtils.minIndex(fx);
                xmin = x[i];
            } catch (Exception ex) {// resort to single thread
                for (double x_i = lower; x_i <= upper; x_i += step) {
                    double fx_i = f.evaluate(x_i);
                    if (fx_i < fmin) {
                        fmin = fx_i;
                        xmin = x_i;
                    }
                }
            }

            return xmin;
        }

        @Override
        public double search(double lower, double upper) {
            return search(lower, Double.NaN, upper);
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
     * a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    protected final double epsilon;
    /**
     * the maximum number of iterations
     */
    protected final int maxIterations;

    /**
     * Construct a univariate minimizer using the grid search method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public GridSearch(double epsilon, int maxIterations) {
        double maxeps = sqrt(EPSILON);
        this.epsilon = epsilon < maxeps ? maxeps : epsilon;
        this.maxIterations = maxIterations;
    }

    @Override
    public Solution solve(C2OptimProblem problem) {
        return solve((UnivariateRealFunction) problem.f());
    }

    /**
     * Minimize a univariate function.
     *
     * @param f the objective function
     * @return the minimizer
     */
    public Solution solve(UnivariateRealFunction f) {
        return new Solution(f);
    }
}
