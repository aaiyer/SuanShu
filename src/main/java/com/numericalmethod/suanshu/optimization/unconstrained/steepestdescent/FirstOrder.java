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
package com.numericalmethod.suanshu.optimization.unconstrained.steepestdescent;

import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.linesearch.Fletcher;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This implements the steepest descent line search using the first order expansion of the Taylor's series.
 * Specifically, we search along the negative gradient direction.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 5.2," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Gradient_descent">Wikipedia: Gradient descent</a>
 * </ul>
 */
public class FirstOrder extends SteepestDescent {

    /**
     * the methods available to do line search
     */
    public static enum Method {

        /**
         * The line search is done using Fletcher's inexact line search method, {@link Fletcher}.
         */
        IN_EXACT_LINE_SEARCH,
        /**
         * The line search is done analytically.
         */
        ANALYTIC
    }

    private class GradientDescent extends SteepestDescentImpl {

        protected Vector gk;//save re-computation

        protected GradientDescent(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        public Vector getDirection(Vector xk) {
            gk = problem.g().evaluate(xk);
            Vector dk = gk.scaled(-1);

            return dk;
        }
    }

    /**
     * This class performs the line search by an analytical formula eq. 5.7.
     */
    private class Analytic extends GradientDescent {

        private double ak = 1;//initial value

        private Analytic(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            Vector xhat = xk.minus(gk.scaled(ak));
            double fhat = problem.f().evaluate(xhat);

            double fk = problem.f().evaluate(xk);

            double gk2 = gk.innerProduct(gk);
            double ak1 = gk2 * ak * ak / 2 / (fhat - fk + ak * gk2);//eq. 5.7

            ak = ak1;//update value
            return ak1;
        }
    }

    private final Method method;

    /**
     * Construct a multivariate minimizer using the First-Order method.
     *
     * @param method        the method to do line search
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public FirstOrder(Method method, double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
        this.method = method;
    }

    /**
     * Construct a multivariate minimizer using the First-Order method.
     * This line search is using Fletcher's inexact line search method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public FirstOrder(double epsilon, int maxIterations) {
        this(Method.IN_EXACT_LINE_SEARCH, epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return method == Method.ANALYTIC
               ? new FirstOrder.Analytic(problem)
               : new FirstOrder.GradientDescent(problem);//default
    }
}
