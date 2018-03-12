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

import com.numericalmethod.suanshu.analysis.differentiation.multivariate.JacobianFunction;
import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.MatthewsDavies;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The Gauss-Newton method is a steepest descent method to minimize a real vector function in the form:
 * /[
 * f(x) = [f_1(x), f_2(x), ..., f_m(x)]'
 * /]
 * The objective function is
 * /[
 * F(x) = f' %*% f
 * ]/
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 5.5," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Gauss%E2%80%93Newton_algorithm">Wikipedia: Gauss–Newton algorithm</a>
 * </ul>
 */
public class GaussNewton {

    protected static class MySteepestDescent extends SteepestDescent {

        /**
         * an implementation of the Gauss-Newton algorithm.
         */
        protected class GaussNewtonImpl extends MySteepestDescent.SteepestDescentImpl {

            private final RntoMatrix J;

            protected GaussNewtonImpl(C2OptimProblem problem, RntoMatrix J) throws Exception {
                super(problem);
                this.J = J;
            }

            @Override
            public Vector getDirection(Vector xk) {
                Matrix Jk = J.evaluate(xk);//TODO: save computation; Jk is computed also in g
                Vector gk = problem.g().evaluate(xk);
                Matrix Hk = Jk.t().multiply(Jk).scaled(2);//eq. 5.25

                MatthewsDavies Hhat = new MatthewsDavies(Hk);
                Vector dk = new Inverse(Hhat).multiply(gk).scaled(-1.0);

                return dk;
            }
        }

        protected MySteepestDescent(double epsilon, int maxIterations) {
            super(epsilon, maxIterations);
        }

        @Override
        public IterativeMinimizer<Vector> solve(C2OptimProblem problem) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    private final MySteepestDescent minimizer;

    /**
     * Construct a multivariate minimizer using the Gauss-Newton method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public GaussNewton(double epsilon, int maxIterations) {
        minimizer = new MySteepestDescent(epsilon, maxIterations);
    }

//    @Override
//    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) {
//        return new GaussNewton.GaussNewtonImpl(problem);
//    }
    /**
     * Solve the minimization problem to minimize <i>F = vf' * vf</i>.
     *
     * @param vf a real vector function to be minimized
     * @param J  a function that computes the Jacobian of <i>f</i> for a given <i>x</i>
     * @return a minimizer
     * @throws Exception
     */
    public IterativeMinimizer<Vector> solve(RealVectorFunction vf, RntoMatrix J) throws Exception {
        return minimizer.new GaussNewtonImpl(new C2OptimProblemImpl(f2(vf), g(vf, J)), J);
    }

    /**
     * Solve the minimization problem to minimize <i>F = vf' * vf</i>.
     *
     * @param vf a real vector function to be minimized
     * @return a minimizer
     * @throws Exception
     */
    public IterativeMinimizer<Vector> solve(RealVectorFunction vf) throws Exception {
        return solve(vf, new JacobianFunction(vf));
    }

    /**
     * Compute the gradient function of <i>F = vf' * vf</i>.
     *
     * @param vf a real vector function
     * @param J  the Jacobian function
     * @return the gradient of <i>F = vf' * vf</i>
     */
    private static RealVectorFunction g(final RealVectorFunction vf, final RntoMatrix J) {
        RealVectorFunction gf = new RealVectorFunction() {

            @Override
            public Vector evaluate(Vector x) {
                Matrix Jxt = J.evaluate(x).t();
                Matrix fx = new DenseMatrix(vf.evaluate(x));
                Matrix G = Jxt.multiply(fx).scaled(2);//eq. 5.24
                Vector g = new DenseVector(MatrixUtils.to1DArray(G));
                return g;
            }

            @Override
            public int dimensionOfDomain() {
                return vf.dimensionOfDomain();
            }

            @Override
            public int dimensionOfRange() {
                return vf.dimensionOfDomain();
            }
        };

        return gf;
    }

    /**
     * <i>F = vf' * vf</i>
     *
     * @param vf a real vector function
     * @return <i>F = vf' * vf</i>
     */
    private static RealScalarFunction f2(final RealVectorFunction vf) {
        RealScalarFunction F = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                Vector fx = vf.evaluate(x);
                double Fx = fx.innerProduct(fx);
                return Fx;
            }

            @Override
            public int dimensionOfDomain() {
                return vf.dimensionOfDomain();
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };

        return F;
    }
}
