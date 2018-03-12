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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.GoldfeldQuandtTrotter;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Newton-Raphson method is a second order steepest descent method that is based on
 * the quadratic approximation of the Taylor series.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Newton's_method_in_optimization">Wikipedia: Newton's method in optimization</a>
 */
public class NewtonRaphson extends SteepestDescent {

    protected class NewtonRaphsonImpl extends SteepestDescentImpl {

        protected NewtonRaphsonImpl(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        public Vector getDirection(Vector xk) {
            Vector gk = problem.g().evaluate(xk);
            Matrix Hk = problem.H().evaluate(xk);

            Matrix Hinv;
            try {
                Hinv = new Inverse(Hk);//Is.positiveDefinite(Hk)
            } catch (Exception ex1) {//any reason why Hk is not invertible
                try {
                    Matrix Hk2 = new GoldfeldQuandtTrotter(Hk, Double.POSITIVE_INFINITY);
                    Hinv = new Inverse(Hk2);
                } catch (Exception ex2) {
                    Hinv = Hk.ONE();//this is essentially using Goldfeld-Quandt-Trotter with β → ∞ in eq. 5.13, hence a first order steepest-descent method
                }
            }

            //TODO: allow solving dk using other methods, e.g., solving a system of linear equation
            //e.g., Hk %*% dk = gk, solve dk
            Vector dk = Hinv.multiply(gk);
            dk = dk.scaled(-1);

            return dk;
        }
    }

    /**
     * Construct a multivariate minimizer using the Newton-Raphson method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public NewtonRaphson(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new NewtonRaphsonImpl(problem);
    }
}
