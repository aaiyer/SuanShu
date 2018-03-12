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
 * FITNESS FOR Jacobian PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS Jacobian RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset;

import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Gradient;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Jacobian;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.PositiveDefiniteMatrixByPositiveDiagonal;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.optimization.univariate.GridSearch;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.List;

/**
 * This implementation is a modified version of Algorithm 15.4 in the reference to solve a general constrained minimization problem
 * using Sequential Quadratic Programming.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 15.4, SQP algorithm for nonlinear problems with equality and inequality constraints," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SQPASVariation1 implements SQPASVariation {//TODO: not thread safe

    private RealScalarFunction f;
    private List<RealScalarFunction> ae;
    private List<RealScalarFunction> ag;
    private final double epsilon;

    /**
     * Construct a variation.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public SQPASVariation1(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Associate this variation to a particular general constrained minimization problem.
     *
     * @param f       the objective function to be minimized
     * @param equal   the equality constraints
     * @param greater the greater-than-or-equal-to constraints
     */
    public void set(RealScalarFunction f, EqualityConstraints equal, GreaterThanConstraints greater) {
        this.f = f;
        this.ae = equal != null ? equal.getConstraints() : new ArrayList<RealScalarFunction>(0);
        this.ag = greater.getConstraints();
    }

    @Override
    public Matrix getInitialHessian(Vector x0, Vector v0, Vector u0) {
        return new DenseMatrix(f.dimensionOfDomain(), f.dimensionOfDomain()).ONE();// I
    }

    /**
     * {@inheritDoc}
     *
     * @see "eqs. 15.41 - 15.44"
     */
    @Override
    public Matrix updateHessian(Vector x1, Vector v1, Vector u1, Vector d, Vector g0, Matrix Ae0, Matrix Ai0, Matrix W0) {//Z0 = W0
        Vector g1 = new Gradient(f, x1);
        Vector dg = g1.minus(g0);

        Vector dAetv = new DenseVector(dg.size(), 0);// zeros
        if (!ae.isEmpty()) {
            Matrix Ae1 = new Jacobian(ae, x1);
            Matrix dAe = Ae1.minus(Ae0);
            dAetv = dAe.t().multiply(v1);
        }

        Matrix Ai1 = new Jacobian(ag, x1);
        Matrix dAi = Ai1.minus(Ai0);
        Vector dAitu = dAi.t().multiply(u1);

        // eq. 15.43
        Vector gamma = dg.minus(dAetv).minus(dAitu);

        // eq. 15.41
        Matrix Z1 = BFGS.dampedBFGSHessianUpdate(W0, gamma, d);
        Z1 = new PositiveDefiniteMatrixByPositiveDiagonal(Z1, epsilon, epsilon);// TODO: W1 may become numerically non positive definite due to numerical error?

        return Z1;
    }

    /**
     * {@inheritDoc}
     *
     * @see "eq. 15.36"
     */
    @Override
    public double alpha(final Vector x, final Vector d, final Vector v, final Vector u) {
        final int p = ae.size();
        final int q = ag.size();
        final double beta = 100;

        UnivariateRealFunction phi = new UnivariateRealFunction() {

            @Override
            public double evaluate(double alpha) {
                Vector x1 = x.add(d.scaled(alpha));

                double phi_x1 = f.evaluate(x1);

                for (int i = 0; i < p; ++i) {
                    double a_x1 = ae.get(i).evaluate(x1);
                    phi_x1 += beta * a_x1 * a_x1;
                }

                for (int i = 0; i < q; ++i) {
                    double c_x1 = ag.get(i).evaluate(x1);
                    phi_x1 -= u.get(i + 1) * c_x1;
                }

                return phi_x1;
            }
        };

        GridSearch minimizer = new GridSearch(epsilon, 50);
        double alpha = minimizer.solve(phi).search(0.001, 1.);

        return alpha;
    }
}
