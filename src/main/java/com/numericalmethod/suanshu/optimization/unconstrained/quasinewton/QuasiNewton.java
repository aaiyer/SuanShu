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
package com.numericalmethod.suanshu.optimization.unconstrained.quasinewton;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.unconstrained.linesearch.Fletcher;
import com.numericalmethod.suanshu.optimization.unconstrained.steepestdescent.SteepestDescent;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Quasi-Newton methods in optimization are for finding local maxima and minima of functions.
 * The Quasi-Newton methods are based on Newton's method to find the stationary point of a function,
 * where the gradient is 0.
 * Newton's method assumes that the function can be locally approximated as quadratic in the region around the optimum.
 * It uses the first and second derivatives (gradient and Hessian) to find the stationary point.
 * In the Quasi-Newton methods the Hessian matrix of the objective function needs not to be computed.
 * The Hessian is updated by analyzing successive gradient vectors instead.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 7.2, 7.3," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Quasi-Newton_method">Wikipedia: Quasi-Newton method</a>
 * </ul>
 */
public abstract class QuasiNewton extends SteepestDescent {

    /**
     * This is an implementation of the Quasi-Newton algorithm.
     * A sub-class supplies the Hessian updating rule.
     */
    protected abstract class QuasiNewtonImpl extends SteepestDescentImpl {

        /**
         * This is the approximate inverse of the Hessian matrix.
         * An implementation of {@link #updateSk(com.numericalmethod.suanshu.vector.doubles.Vector)}
         * modifies this incrementally.
         */
        protected Matrix Sk;
        /**
         * the gradient at the k-th iteration
         */
        protected Vector gk = null;
        /**
         * the line search direction at the k-th iteration
         */
        protected Vector dk = null;
        /**
         * the increment in the search direction
         */
        protected double ak;
        private double dgNorm = Double.POSITIVE_INFINITY;

        QuasiNewtonImpl(C2OptimProblem problem) throws Exception {
            super(problem);
            int n = problem.f().dimensionOfDomain();//number of variables
            Sk = new DenseMatrix(n, n).ONE();
        }

        @Override
        public Vector getDirection(Vector xk) {
            Vector g0 = gk;
            gk = problem.g().evaluate(xk);//update the gradient

            //update the quasi Hessian inverse
            if (dk != null) {
                Vector dg = gk.minus(g0);//TODO: what if gamma = 0
                dgNorm = dg.norm();
                if (dgNorm <= epsilon) {
                    return dk;//no change
                }

                updateSk(dg);
            }

            dk = Sk.multiply(gk).scaled(-1);

            return dk;
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            if (dgNorm <= epsilon) {
                return 1e-5;//TODO: a small random increment like in Fletcher (precision is not so important), or ak = 0
            }

            ak = super.getIncrement(xk, dk);
            return ak;
        }

        /**
         * The Hessian inverse is updated incrementally to improve approximation.
         * The internal member {@code Sk} is modified by this method.
         *
         * @param dg difference between the new and the last gradient
         */
        abstract void updateSk(Vector dg);
    }

    /**
     * Construct a multivariate minimizer using a Quasi-Newton method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public QuasiNewton(double epsilon, int maxIterations) {
        super(new Fletcher(0.1, 0.7, 0.1, 0.75, 1e-10, 600),//these settings are from Algorithm 7.3
              epsilon, maxIterations);
    }
}
