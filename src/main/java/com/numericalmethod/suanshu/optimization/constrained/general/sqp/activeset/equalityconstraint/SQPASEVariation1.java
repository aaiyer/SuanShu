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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.equalityconstraint;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Gradient;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Hessian;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Jacobian;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.PositiveDefiniteMatrixByPositiveDiagonal;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.optimization.univariate.GridSearch;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.sqrt;
import java.util.List;

/**
 * This implementation is a modified version of the algorithm in the reference to solve a general constrained minimization problem
 * using Sequential Quadratic Programming.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 15.3.2, Algorithm 15.4b, SQP algorithm for nonlinear problems with equality and inequality constraints," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SQPASEVariation1 implements SQPASEVariation {

    protected RealScalarFunction f;
    protected List<RealScalarFunction> a;
    protected int p;
    protected final double r;
    protected final double lower;
    protected final int discretization;
    protected boolean foundPositiveDefiniteHessian = false;
    protected final double epsilon = sqrt(Constant.EPSILON);

    /**
     * Construct a variation.
     *
     * @param r              Han's exact penalty function coefficient, the bigger the better, e.g., eq. 15.20
     * @param lower          the lower bound of alpha; the smaller the better but cannot be zero
     * @param discretization the number of points between <i>[lower, 1]</i> to search for alpha; the bigger the better
     */
    public SQPASEVariation1(double r, double lower, int discretization) {
        this.r = r;
        this.lower = lower;
        this.discretization = discretization;
    }

    /**
     * Construct a variation.
     */
    public SQPASEVariation1() {
        this(100., 0.01, 50);
    }

    /**
     * Associate this variation to a particular general constrained minimization problem with only equality constraints.
     *
     * @param f     the objective function to be minimized
     * @param equal the equality constraints
     */
    public void set(RealScalarFunction f, EqualityConstraints equal) {//TODO: move to ctor
        this.f = f;
        this.a = equal.getConstraints();
        this.p = this.a.size();
    }

    @Override
    public Matrix getInitialHessian(Vector x0, Vector v0) {
        Matrix W0 = W(x0, v0);
        if (IsMatrix.positiveDefinite(W0)) {
            foundPositiveDefiniteHessian = true;
            return W0;
        }

        Eigen eigen = new Eigen(W0);
        double[] ev = eigen.getRealEigenvalues();
        double max = DoubleArrayMath.max(ev);
        if (DoubleUtils.isNegative(max, 0)) {// all eigenvalues are negative
            return W0.ONE();
        }

        W0 = new PositiveDefiniteMatrixByPositiveDiagonal(W0, epsilon, Constant.EPSILON);//TODO: how to determine epsilons?
        return W0;
    }

    /**
     * Compute <i>W</i>.
     *
     * @param x the current minimizer
     * @param u the Lagrange multipliers for inequality constraints (mu)
     * @return <i>W</i>
     * @see "15.4b"
     */
    protected Matrix W(Vector x, Vector u) {
        Matrix W = new Hessian(f, x);
        for (int i = 0; i < p; ++i) {
            double ui = u.get(i + 1);
            RealScalarFunction ai = a.get(i);
            Matrix Ha = new Hessian(ai, x);
            W = W.minus(Ha.scaled(ui));
        }

        return W;
    }

    @Override
    public Matrix updateHessian(Vector x1, Vector v1, Vector d, Vector g0, Matrix A0, Matrix W0) {
        // section 15.3.2
        Vector g1 = new Gradient(f, x1);
        Vector dg = g1.minus(g0);

        Matrix A1 = new Jacobian(a, x1);
        Matrix dA = A1.minus(A0);

        Vector dAtv = dA.t().multiply(v1);
        Vector gamma = dg.minus(dAtv);

        Matrix W1 = BFGS.dampedBFGSHessianUpdate(W0, gamma, d);
        W1 = new PositiveDefiniteMatrixByPositiveDiagonal(W1, epsilon, Constant.EPSILON);// TODO: W1 may become numerically non positive definite due to numerical error?

        return W1;
    }

    @Override
    public double alpha(final Vector x, final Vector d, final Vector v) {
        UnivariateRealFunction phi = new UnivariateRealFunction() {

            @Override
            public double evaluate(double alpha) {
                Vector x1 = x.add(d.scaled(alpha));

                double phi_x1 = f.evaluate(x1);
                for (int i = 0; i < p; ++i) {
                    double a_x1 = a.get(i).evaluate(x1);
                    phi_x1 += Math.abs(r * a_x1);// similiar to Han's exact penalty function, eq. 15.20
                }

                return phi_x1;
            }
        };

        GridSearch solver = new GridSearch(epsilon, discretization);
        GridSearch.Solution soln = solver.solve(phi);
        double alpha = soln.search(lower, 1.);

        return alpha;
    }
}
