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
import com.numericalmethod.suanshu.optimization.unconstrained.MultivariateMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.linesearch.Fletcher;
import com.numericalmethod.suanshu.optimization.unconstrained.linesearch.LineSearch;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A steepest descent algorithm finds the minimum by moving along the negative of the steepest gradient direction.
 * In each iteration, we compute a direction, e.g., <i>d<sub>k</sub> = −g</i> as in the first order approximation.
 * We search along this direction for an improved minimizer by analytically computing
 * <i>α<sub>k</sub></i> that minimizes <i>f(x<sub>k</sub> + α<sub>k</sub> * d<sub>k</sub>)</i>.
 * The minimizer <i>x<sub>k+1</sub> = x<sub>k</sub> + α<sub>k</sub> * d<sub>k</sub></i> is updated.
 * This procedure is repeated until the increment <i>|α<sub>k</sub> * d<sub>k</sub>|</i> becomes sufficiently small, hence convergence.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou and Wu-Sheng Lu, "Algorithms 5.2, 5.3, 5.5," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Gradient_descent">Wikipedia: Gradient descent</a>
 * </ul>
 */
public abstract class SteepestDescent implements MultivariateMinimizer<IterativeMinimizer<Vector>> {

    /** This is an implementation of the steepest descent method. */
    protected abstract class SteepestDescentImpl implements IterativeMinimizer<Vector> {

        private Vector xmin = null;//the current minimizer
        private Vector dx = null;
        protected final C2OptimProblem problem;
        protected final LineSearch.Solution linesearch;

        /**
         * Get the next search direction.
         *
         * @param xk the current minimizer
         * @return the next search direction
         */
        protected abstract Vector getDirection(Vector xk);

        protected SteepestDescentImpl(C2OptimProblem problem) throws Exception {
            this.problem = problem;
            this.linesearch = linesearch0.solve(problem);
        }

        /**
         * Get the increment fraction, <i>α<sub>k</sub></i>.
         *
         * @param xk the current minimizer
         * @param dk the search direction
         * @return <i>α<sub>k</sub></i>
         */
        protected double getIncrement(Vector xk, Vector dk) {
            double ak = linesearch.linesearch(xk, dk);
            return ak;
        }

        @Override
        public double minimum() {
            return problem.f().evaluate(xmin);//TODO: could be very expensive; can we save this from somewhere?
        }

        @Override
        public ImmutableVector minimizer() {
            return new ImmutableVector(xmin);
        }

        @Override
        public Vector search(Vector... initials) throws Exception {
            setInitials(initials);

            for (int iter = 1; iter <= maxIterations; ++iter) {
                Vector xk1 = step();
                xmin = xk1;

                //check convergence
                if (dx.norm() <= epsilon) {//TODO: we cannot use relative epsilon. because computing minimum() can be very time consuming
                    break;
                }
            }

            return minimizer();
        }

        @Override
        public void setInitials(Vector... initials) {
            xmin = initials[0].deepCopy();
        }

        @Override
        public Vector step() {
            Vector xk = xmin;

            Vector dk = getDirection(xk);
            double ak = getIncrement(xk, dk);

            dx = dk.scaled(ak);//increment = ak * dk
            Vector xk1 = xk.add(dx);//x(k+1) = x(k) + dx = x(k) + ak * dk

            return xk1;
        }
    }

    /** a precision parameter: when a number |x| ≤ ε, it is considered 0 */
    protected final double epsilon;
    /** the maximum number of iterations */
    protected final double maxIterations;
    private final LineSearch linesearch0;

    /**
     * Construct a multivariate minimizer using a steepest descent method.
     *
     * @param linesearch    the line search method used in each iteration
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SteepestDescent(LineSearch linesearch, double epsilon, int maxIterations) {
        this.linesearch0 = linesearch;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a multivariate minimizer using a steepest descent method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SteepestDescent(double epsilon, int maxIterations) {
        this(new Fletcher(), epsilon, maxIterations);
    }

    /**
     * Solve a minimization problem with a C<sup>2</sup> objective function.
     *
     * @param problem a minimization problem with a C<sup>2</sup> objective function
     * @return a minimizer
     */
    @Override
    public abstract IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception;
}
