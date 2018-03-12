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
package com.numericalmethod.suanshu.optimization.unconstrained.conjugatedirection;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.steepestdescent.SteepestDescent;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A conjugate direction optimization method is performed by using
 * sequential line search along directions that bear a strict mathematical relationship to one another.
 * In particular, this conjugate-gradient method generates a new search direction
 * by adding a vector <i>β<sub>k</sub>d<sub>k</sub></i> to the negative gradient <i>-g<sub>k+1</sub></i>.
 * The algorithm was originally developed for quadratic problems.
 * For convex quadratic problems, the algorithm converges in <i>n</i> iterations,
 * where <i>n</i> is the number of variables.
 * It still has good convergence properties when applied to non-quadratic problems.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Theorem 6.4, Algorithm 6.2," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Conjugate_gradient">Wikipedia: Conjugate gradient method</a>
 * </ul>
 */
public class ConjugateGradient extends SteepestDescent {

    /**
     * Hestenes and Stiefel proposes this way to compute the new search direction:
     * A new search direction is created by adding a vector <i>β<sub>k</sub>d<sub>k</sub></i>
     * to the negative gradient <i>-g<sub>k+1</sub></i>.
     *
     * @see "M. R. Hestenes and E. L. Stiefel, "Methods of conjugate gradients for solving linear systems," J. Res. Natl, Bureau Standards, vol. 49, p. 409–436, 1952."
     */
    class HestenesStiefel extends SteepestDescentImpl {

        protected HestenesStiefel(C2OptimProblem problem) throws Exception {
            super(problem);
        }
        /**
         * the gradient at the k-th iteration
         */
        protected Vector gk = null;
        /**
         * the line search direction at the k-th iteration
         */
        protected Vector dk = null;

        @Override
        public Vector getDirection(Vector xk) {
            Vector gk1 = problem.g().evaluate(xk);//g<sub>k+1</sub>
            Vector dk1 = gk1.scaled(-1);//d<sub>k+1</sub>

            if (dk != null) {//second iteration onward
                double beta = gk1.innerProduct(gk1);
                beta /= gk.innerProduct(gk);

                Vector betadk = dk.scaled(beta);
                dk1 = dk1.add(betadk);//dk1 = -gk1 + beta * dk
            }

            gk = gk1;//update the gradient
            dk = dk1;

            return dk;
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            double g2 = gk.innerProduct(gk);
            Matrix Hk = problem.H().evaluate(xk);

            DenseMatrix d = new DenseMatrix(dk);
            Matrix dtHd = new CongruentMatrix(d, Hk);
            double dtHd11 = dtHd.get(1, 1);

            double ak1 = g2 / dtHd11;
            return ak1;
        }
    }

    /**
     * Construct a multivariate minimizer using the Conjugate-Gradient method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public ConjugateGradient(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new HestenesStiefel(problem);
    }
}
