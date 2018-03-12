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

import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Gradient;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Jacobian;
import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSimpleSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSolution;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.List;

/**
 * This implementation is a modified version of Algorithm 15.1 in the reference to solve a general constrained optimization problem with only equality constraints.
 * \[
 * \min_x f(x) \textrm{ s.t.,} \\
 * a_i(x) = 0, i = 1, 2, ..., p\\
 * \]
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu. "Algorithm 15.1, SQP Problems with equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SQPActiveSetSolverForOnlyEqualityConstraint1 implements ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>> {

    private final VariationFactory variant;
    private final double epsilon;
    private final int maxIterations;

    /**
     * This factory constructs a new instance of {@code SQPASEVariation} for each SQP problem.
     */
    public static interface VariationFactory {

        /**
         * Construct a new instance of {@code SQPASEVariation} for an SQP problem.
         *
         * @param f     the objective function to be minimized
         * @param equal the equality constraints
         * @return a new instance of {@code SQPASEVariation}
         */
        SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal);
    };

    /**
     * This is the solution to a general minimization with only equality constraints using the SQP Active Set algorithm.
     */
    private class Solution implements IterativeMinimizer<Vector> {

        private Vector x0;//the best minimizer found so far
        private Vector v0;//the Lagrange multipliers (lambda)
        private Matrix A0;//the Jacobian
        private Vector g0;//the gradient
        private Matrix W0;//the Hessian approximation
        private Vector d;//the increment
        private final RealScalarFunction f;
        private final EqualityConstraints equal;
        private final List<RealScalarFunction> a;
        private final int p;
        private final SQPASEVariation impl;

        private Solution(RealScalarFunction f, EqualityConstraints equal) {
            this.f = f;
            this.equal = equal;
            this.a = equal.getConstraints();
            this.p = a.size();
            this.impl = variant.newVariation(f, equal);
        }

        @Override
        public void setInitials(Vector... initials) {
            x0 = initials[0];
            v0 = initials[1];
            W0 = impl.getInitialHessian(x0, v0);
        }

        @Override
        public Object step() throws Exception {
            // eq. 15.4d
            g0 = new Gradient(f, x0);
            // eq. 15.4c
            A0 = new Jacobian(a, x0);
            // eq.15.4e
            Vector ax0 = new DenseVector(p);//a(x0)
            for (int i = 0; i < p; ++i) {
                ax0.set(i + 1, this.a.get(i).evaluate(x0));
            }

            // eq. 15.6
            try {
                QuadraticFunction qf = new QuadraticFunction(W0, g0);
                LinearEqualityConstraints equal2 = new LinearEqualityConstraints(A0, ax0.scaled(-1.));
                QPSolution soln = QPSimpleSolver.solve(qf, equal2, 0);
                d = soln.minimizer();
            } catch (QPInfeasible ex) {
                throw new RuntimeException(String.format("unable to solve a sub quadratic programming problem: ", ex.getMessage()));
            }

            Vector v1 = v1(W0, g0, A0, d);

            // eq. 15.19; scale increment by alpha
            double alpha = impl.alpha(x0, d, v1);
            d = d.scaled(alpha);
            Vector x1 = x0.add(d);

            // recompute mu_{k+1}
            v1 = v1(W0, g0, A0, d);

            // update state 
            x0 = x1;
            v0 = v1;

            return true;
        }

        /**
         * {@inheritDoc}
         *
         * @param initials initials[0]: <i>x<sub>0</sub></i>, initials[1] <i>λ<sub>0</sub></i>
         * @return an (approximate) minimizer
         */
        @Override
        public Vector search(Vector... initials) throws Exception {
            setInitials(initials);

            for (int k = 0; k < maxIterations; ++k) {
                step();

                if (d.norm() < epsilon) {
                    break;
                }

                Matrix W1 = impl.updateHessian(x0, v0, d, g0, A0, W0);//TODO: move to step()
                W0 = W1;
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
     * Construct an SQP Active Set minimizer to solve general minimization problems with equality constraints.
     *
     * @param variant       specify an implementation to use
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyEqualityConstraint1(VariationFactory variant, double epsilon, int maxIterations) {
        this.variant = variant;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with equality constraints.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyEqualityConstraint1(double epsilon, int maxIterations) {
        this(
                new VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation1 impl = new SQPASEVariation1();
                        impl.set(f, equal);
                        return impl;
                    }
                },
                epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(ConstrainedOptimProblem problem) throws Exception {
        EqualityConstraints equal = problem.getEqualityConstraints();
        assertArgument(equal != null, "there must be equality constraints");

        LessThanConstraints less = problem.getLessThanConstraints();
        assertArgument(less == null, "there must be only equality constraints");

        return solve(problem.f(), equal);
    }

    /**
     * Minimize a function subject to only equality constraints.
     *
     * @param f     the objective function to be minimized
     * @param equal the equality constraints
     * @return a solution to the minimization problem
     * @throws Exception when there is an error solving the problem
     */
    public IterativeMinimizer<Vector> solve(RealScalarFunction f, EqualityConstraints equal) throws Exception {
        return new Solution(f, equal);
    }

    /**
     * eq. 15.7
     *
     * @param W Hessian
     * @param g gradient
     * @param A Jacobian
     * @param d line direction for improvement
     * @return updated Lagrangian
     */
    private static Vector v1(Matrix W, Vector g, Matrix A, Vector d) {
        Vector v1 = W.multiply(d).add(g);
        v1 = A.multiply(v1);
        Matrix AAt = A.multiply(A.t());
        v1 = new Inverse(AAt, 0).multiply(v1);
        return v1;
    }
}
