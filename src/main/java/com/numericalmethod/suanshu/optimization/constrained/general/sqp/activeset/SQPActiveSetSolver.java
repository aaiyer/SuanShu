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

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Gradient;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Jacobian;
import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.activeset.QPPrimalActiveSetSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem.QPProblem;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Sequential quadratic programming (SQP) is an iterative method for nonlinear optimization.
 * SQP methods are used on problems for which the objective function and the constraints are twice continuously differentiable.
 * SQP methods solve a sequence of optimization subproblems,
 * each which optimizes a quadratic model of the objective subject to a linearization of the constraints.
 * If the problem is unconstrained, then the method reduces to Newton's method for finding a point where the gradient of the objective vanishes.
 * If the problem has only equality constraints, then the method is equivalent to applying Newton's method to the first-order optimality conditions,
 * or Karush–Kuhn–Tucker conditions, of the problem.
 * SQP methods are the state of the art in nonlinear programming methods.
 * Schittkowski has shown that SQP can outperform every other tested method in terms of efficiency, accuracy, and percentage of successful solutions,
 * over a large number of test problems.
 * The general minimization problem takes this form.
 * \[
 * \min_x f(x) \textrm{ s.t.,} \\
 * a_i(x) = 0, i = 1, 2, ..., p\\
 * c_j(x) \geq 0, j = 1, 2, ..., q
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Sequential_quadratic_programming">Wikipedia: Sequential quadratic programming</a>
 * <li><a href="http://en.wikipedia.org/wiki/Active_set">Wikipedia: Active set</a>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 15.4, SQP algorithm for nonlinear problems with equality and inequality constraints," Practical Optimization: Algorithms and Engineering Applications."
 * </ul>
 */
public class SQPActiveSetSolver implements ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>> {

    protected final double epsilon;
    protected final int maxIterations;
    private final VariationFactory variant;

    /**
     * This factory constructs a new instance of {@code SQPASVariation} for each SQP problem.
     */
    public static interface VariationFactory {

        /**
         * Construct a new instance of {@code SQPASVariation} for an SQP problem.
         *
         * @param f       the objective function to be minimized
         * @param equal   the equality constraints
         * @param greater the greater-than-or-equal-to constraints
         * @return a new instance of {@code SQPASVariation}
         */
        public SQPASVariation newVariation(RealScalarFunction f, EqualityConstraints equal, GreaterThanConstraints greater);
    }

    /**
     * This is the solution to a general minimization with only inequality constraints using the SQP Active Set algorithm.
     */
    public class Solution implements IterativeMinimizer<Vector> {

        private Vector x0;//the best minimizer found so far
        private Vector v0;//the Lagrange multipliers for equality constraints (lambda)
        private Vector u0;//the Lagrange multipliers for greater-than constraints (mu)
        private Matrix Ae0;//the Jacobian for equality constraints
        private Matrix Ai0;//the Jacobian for inequality constraints
        private Vector g0;//the gradient
        private Matrix Z0;//the Hessian approximation
        private Vector d;//the increment
        private final RealScalarFunction f;
        private final EqualityConstraints equal;
        private final List<RealScalarFunction> a;
        private final int p;
        private final GreaterThanConstraints greater;
        private final List<RealScalarFunction> c;
        private final int q;
        private final SQPASVariation impl;

        protected Solution(RealScalarFunction f, EqualityConstraints equal, GreaterThanConstraints greater) {
            this.f = f;
            this.equal = equal;
            this.a = equal != null ? equal.getConstraints() : new ArrayList<RealScalarFunction>(0);
            this.p = a.size();
            this.greater = greater;
            this.c = greater.getConstraints();
            this.q = c.size();
            this.impl = variant.newVariation(f, equal, greater);
        }

        @Override
        public void setInitials(Vector... initials) {
            // step 1
            x0 = initials[0];
            v0 = initials[1];// the Lagrange multipliers for equality constraints (lambda)
            u0 = initials[2];// the Lagrange multipliers for greater-than constraints (mu)
            Z0 = impl.getInitialHessian(x0, v0, u0);

            SuanShuUtils.assertArgument(DoubleArrayMath.min(u0.toArray()) >= 0, "mu[0] must be >= 0");
        }

        @Override
        public Object step() throws Exception {
            // step 2
            // eq. 15.32g
            g0 = new Gradient(f, x0);
            // eq. 15.32h
            Ae0 = equal != null ? new Jacobian(a, x0) : new DenseMatrix(0, 0);
            // eq. 15.32i
            Ai0 = new Jacobian(c, x0);
            // eq.15.4e
            Vector ax0 = new DenseVector(p);
            for (int i = 0; i < p; ++i) {
                ax0.set(i + 1, a.get(i).evaluate(x0));
            }
            // eq.15.16
            Vector cx0 = new DenseVector(q);
            for (int i = 0; i < q; ++i) {
                cx0.set(i + 1, c.get(i).evaluate(x0));
            }

            // step 3
            // eq. 15.33
            try {
                QuadraticFunction qf = new QuadraticFunction(Z0, g0);
                LinearEqualityConstraints equal2 = equal != null ? new LinearEqualityConstraints(Ae0, ax0.scaled(-1.)) : null;
                LinearGreaterThanConstraints greater2 = new LinearGreaterThanConstraints(Ai0, cx0.scaled(-1.));
                QPProblem problem2 = new QPProblem(qf, equal2, greater2);
                QPPrimalActiveSetSolver solver2 = new QPPrimalActiveSetSolver(epsilon, maxIterations);
                IterativeMinimizer<QPSolution> soln = solver2.solve(problem2);
                QPSolution soln2 = soln.search();
                d = soln2.minimizer();
            } catch (QPInfeasible ex) {
                throw new RuntimeException(String.format("unable to solve a sub quadratic programming problem: ", ex.getMessage()));
            }

            // identify the active greater-than constraints in the sub-problem
            ArrayList<Integer> active = new ArrayList<Integer>();
            for (int i = 1; i <= Ai0.nRows(); ++i) {
                Vector Aii = Ai0.getRow(i);
                double Aiid = Aii.innerProduct(d);
                if (Math.abs(Aiid + cx0.get(i)) < epsilon) {
                    active.add(i);
                }
            }

            if (!active.isEmpty()) {
                int[] rows = DoubleUtils.collection2IntArray(active);
                int[] cols = R.seq(1, Ai0.nCols());
                Matrix Aai = CreateMatrix.subMatrix(Ai0, rows, cols);

                // eq. 15.34
                Matrix Aa = CreateMatrix.rbind(Ae0, Aai);
                Matrix vu1 = Aa.multiply(Aa.t());
                vu1 = new Inverse(vu1);
                vu1 = vu1.multiply(Aa);
                Vector vu2 = Z0.multiply(d);
                vu2 = vu2.add(g0);
                Vector vu3 = vu1.multiply(vu2);

                Vector v1 = CreateVector.subVector(vu3, 1, p);
                Vector u1Hat = CreateVector.subVector(vu3, p + 1, vu3.size());// 1-1 correspondence between rows and u1Hat
                Vector u1 = new DenseVector(q);// initialized to 0s
                for (int i = 0; i < rows.length; ++i) {
                    int j = rows[i];// the vector index
                    u1.set(j, u1Hat.get(i + 1));
                }

                v0 = v1;
                u0 = u1;
            }

            // step 4
            // eq. 15.37, eq. 15.39, eq. 15.40; scale increment by alpha
            double alpha = impl.alpha(x0, d, v0, u0);

            // steps 5, 6
            d = d.scaled(alpha);
            x0 = x0.add(d);

            return true;
        }

        @Override
        public Vector search(Vector... initials) throws Exception {
            switch (initials.length) {
                case 1:
                    return search(initials[0]);
                case 3:
                    return search(initials[0], initials[1], initials[2]);
                default:
                    throw new IllegalArgumentException("wrong number of paramters");
            }
        }

        /**
         * Search for a solution that minimizes the objective function from the
         * given starting point.
         *
         * @param initial an initial guess
         * @return an (approximate) optimizer
         * @throws Exception when an error occurs during the search
         */
        public Vector search(Vector initial) throws Exception {
            return search(
                    initial,
                    equal != null ? new DenseVector(equal.getConstraints().size(), -1.) : new DenseVector(0),
                    new DenseVector(greater.getConstraints().size(), 1));
        }

        /**
         * Search for a solution that minimizes the objective function from the
         * given starting point.
         * Note that <i>f(x<sub>0</sub>) &ge; 0</i> and <i>μ<sub>0</sub> &ge; 0</i>
         *
         * @param x0      <i>x<sub>0</sub></i>
         * @param lambda0 <i>λ<sub>0</sub></i>, the Lagrange multipliers for equality constraints (lambda)
         * @param mu0     <i>μ<sub>0</sub></i>, the Lagrange multipliers for inequality constraints (mu)
         * @return an (approximate) minimizer
         * @throws Exception when an error occurs during the search
         */
        public Vector search(Vector x0, Vector lambda0, Vector mu0) throws Exception {
            setInitials(new Vector[]{x0, lambda0, mu0});

            for (int k = 0; k < maxIterations; ++k) {
                step();

                if (d.norm() < epsilon) {
                    break;
                }

                // step 7
                Matrix Z1 = impl.updateHessian(this.x0, v0, u0, d, g0, Ae0, Ai0, Z0);
                Z0 = Z1;
            }

            return minimizer();
        }

        @Override
        public double minimum() {
            return f.evaluate(x0);
        }

        @Override
        public Vector minimizer() {
            return new ImmutableVector(x0);
        }
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with inequality constraints.
     *
     * @param variant       a factory that constructs a new instance of {@code SQPASVariation} for each problem
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolver(VariationFactory variant, double epsilon, int maxIterations) {
        this.variant = variant;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with inequality constraints.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolver(double epsilon, int maxIterations) {
        this(
                new VariationFactory() {

                    @Override
                    public SQPASVariation newVariation(RealScalarFunction f, EqualityConstraints equal, GreaterThanConstraints greater) {
                        SQPASVariation1 impl = new SQPASVariation1(Math.sqrt(Constant.EPSILON));
                        impl.set(f, equal, greater);
                        return impl;
                    }
                },
                epsilon, maxIterations);
    }

    @Override
    public Solution solve(ConstrainedOptimProblem problem) throws Exception {
        return new Solution(problem.f(), problem.getEqualityConstraints(), problem.getLessThanConstraints().toGreaterThanConstraints());
    }
}
