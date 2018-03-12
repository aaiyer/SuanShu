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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.activeset;

import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LinearSystemSolver;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSimpleSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem.QPProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation solves a Quadratic Programming problem using the Primal Active Set algorithm.
 * In quadratic programming, as the solution is not necessarily on one of the edges of the bounding polygon,
 * an estimation of the active set gives us a subset of inequalities to watch while searching the solution,
 * which reduces the complexity of the search.
 * A bottleneck is that only one constraint can be added or removed from the working set per iteration.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 13.1, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
 * <li>"Jorge Nocedal, Stephen Wright, "Algorithm 16.3," Numerical Optimization."
 * <li><a href="http://en.wikipedia.org/wiki/Active_set">Wikipedia: Active set</a>
 * </ul>
 */
public class QPPrimalActiveSetSolver implements ConstrainedMinimizer<QPProblem, IterativeMinimizer<QPSolution>> {

    private final double epsilon;
    private final int maxIterations;

    /**
     * This is the solution to a Quadratic Programming problem using the Primal Active Set algorithm.
     */
    public class Solution implements IterativeMinimizer<QPSolution> {

        private QPSolution minimizer;
        private Vector x;
        private int k = 0;
        private WorkingActiveSet Js;
        private final QPProblem problem;
        private final ImmutableMatrix A;
        private final ImmutableVector b;
        private final ImmutableMatrix Aeq;
//    private final ImmutableVector beq;
        private final Matrix H;
        private final Vector p;

        private Solution(QPProblem problem) {
            this.problem = problem;

            this.A = problem.A();
            this.b = problem.b();
            this.Aeq = problem.getEqualityConstraints() != null ? problem.Aeq() : null;
//        this.beq = problem.getEqualityConstraints() != null ? problem.beq() : null;
            this.H = problem.f().Hessian();
            this.p = problem.f().p();

            this.x = null;
        }

        @Override
        public void setInitials(QPSolution... initials) {
            // step 1
            x = initials[0].minimizer();
            Js = new WorkingActiveSet(A);
            Js.addAll(problem.getGreaterThanConstraints().getActiveRows(x, epsilon));
        }

        @Override
        public Object step() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Search for a minimizer for the quadratic programming problem.
         *
         * @return a quadratic programming solution
         * @throws QPInfeasible if there is no solution to the quadratic programming problem
         * @see "Algorithms 16.3 and 13.1"
         */
        public QPSolution search() throws QPInfeasible {
            LinearGreaterThanConstraints greater = problem.getGreaterThanConstraints();
            Vector x = greater.getFeasibleInitialPoint(problem.getEqualityConstraints());
            if (x.get(x.size()) > epsilon) {
                throw new QPInfeasible();
            }

            return search(CreateVector.subVector(x, 1, x.size() - 1));
        }

        /**
         * Search for a minimizer for the quadratic programming problem.
         *
         * @param initial a <em>strictly</em> feasible initial point that satisfies the constraints
         * @return a quadratic programming solution
         * @throws QPInfeasible if there is no solution to the quadratic programming problem
         * @see "Algorithms 16.3 and 13.1"
         */
        public QPSolution search(final Vector initial) throws QPInfeasible {//TODO: this algorithm may fail if the initial is not strictly feasible
            return search(
                    new QPSolution() {

                        @Override
                        public boolean isUnique() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public ImmutableVector minimizer() {
                            return new ImmutableVector(initial);//initial guess
                        }
                    });
        }

        /**
         * Search for a minimizer for the quadratic programming problem from the
         * given starting points.
         *
         * @param initial an initial guess
         * @return an (approximate) optimizer
         * @throws QPInfeasible when the quadratic programming problem is infeasible
         */
        public QPSolution search(QPSolution initial) throws QPInfeasible {
            setInitials(initial);

            for (k = 0; k < maxIterations; ++k) {
                // step 2
                Vector g = H.multiply(x).add(p);// eq. 13.16

                // step 4
                QuadraticFunction f1 = new QuadraticFunction(H, g);// eq. 13.17a
                Matrix allActiveConstraints = Aeq != null ? CreateMatrix.rbind(Js.Aa(), Aeq) : Js.Aa();

                Vector ZERO = new DenseVector(allActiveConstraints.nRows());// zero
                Vector d = ZERO;
                if (allActiveConstraints.nRows() < allActiveConstraints.nCols()) {// TODO: handle the case where rows are linearly dependent
                    LinearEqualityConstraints equal = new LinearEqualityConstraints(allActiveConstraints, ZERO);// eq. 13.17b
                    QPSolution subProblem = equal.size() > 0 ? QPSimpleSolver.solve(f1, equal, epsilon) : QPSimpleSolver.solve(f1, epsilon);
                    d = subProblem.minimizer();
                }

                // step 3
                if (d.norm(Integer.MAX_VALUE) < epsilon) {
                    if (Js.size() == 0) {
                        return getSolution(x);//break
                    }

                    // the eq. below eq. 13.19
                    LinearSystemSolver solver = new LinearSystemSolver(epsilon);// consider only active inequality constraints
                    Vector mu = solver.solve(allActiveConstraints.t()).getParticularSolution(g);// the Lagrange multipliers for the inequality constraints

                    int nInequalities = Aeq != null ? mu.size() - Aeq.nRows() : mu.size();
                    if (!isPositive(mu, nInequalities)) {
                        int j = getMostNegativeLagrangeMultiplier(mu, nInequalities);
                        Js.removeByIndex(j);
                    } else {// solution found
                        return getSolution(x);//break
                    }
                } else {// d > 0
                    // step 5; 
                    double alphaMin = 1;
                    int j = 0;// the index to the new constraint
                    Map<Integer, Double> alphas = new HashMap<Integer, Double>();
                    for (int i = 1; i <= A.nRows(); ++i) {
                        if (Js.contains(i)) {
                            continue;
                        }

                        Vector ai = A.getRow(i);
                        if (ai.innerProduct(d) >= 0) {
                            continue;
                        }

                        double alpha = ai.innerProduct(x) - b.get(i);
                        alpha /= -ai.innerProduct(d);

                        alphas.put(i, alpha);

                        if (alpha < alphaMin) {
                            alphaMin = alpha;
                            j = i;
                        }
                    }

                    if (DoubleUtils.isZero(alphaMin, 0)) {
                        break;// TODO: anti-cycling
                    }

                    x = x.add(d.scaled(alphaMin));

                    // step 6
                    // add the new Aa constraint, namely, the blocking constraint
                    if (j > 0) {// alphaMin < 1
                        Js.add(j);
                    }
                }
            }

            minimizer = getSolution(x);
            return minimizer();
        }

        @Override
        public QPSolution search(QPSolution... initials) throws QPInfeasible {//TODO: this algorithm may fail if the initial is not strictly feasible
            switch (initials.length) {
                case 0:
                    return search();
                case 1:
                    return search(initials[0]);
                default:
                    throw new IllegalArgumentException("wrong number of parameters");
            }
        }

        @Override
        public QPSolution minimizer() {
            return minimizer;
        }

        @Override
        public double minimum() {
            return problem.f().evaluate(x);//TODO: cache result
        }

//    // check rank condition, eq. 13.21
//    private boolean isRankConditionMet(Matrix Aa, Vector g) {
//        Matrix Aat = Aa.t();
//        Matrix Aatg = CreateMatrix.cbind(Aat, new DenseMatrix(g));
//        int rAatg = rank(Aatg, epsilon);
//        int rAat = rank(Aat, epsilon);
//
//        return rAatg == rAat;
//    }
        private boolean isPositive(Vector mu, int size) {
            for (int i = 1; i <= size; ++i) {
                if (DoubleUtils.isNegative(mu.get(i), 0)) {
                    return false;
                }
            }

            return true;
        }

        /** Bland's anti-cycling rule. */
        private int getMostNegativeLagrangeMultiplier(Vector mu, int size) {
            int index = 0;
            double min = Double.POSITIVE_INFINITY;

            for (int i = 1; i <= size; ++i) {
                if (mu.get(i) < min) {
                    min = mu.get(i);
                    index = i;
                }
            }

            return index;
        }

        private QPSolution getSolution(final Vector x) {
            return new QPSolution() {

                @Override
                public boolean isUnique() {
                    throw new UnsupportedOperationException("Not supported yet.");// TODO
                }

                @Override
                public ImmutableVector minimizer() {
                    return new ImmutableVector(x);
                }
            };
        }
    }

    /**
     * Construct a Primal Active Set minimizer to solve quadratic programming problems.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations, e.g., {@code Integer.MAX_VALUE}
     */
    public QPPrimalActiveSetSolver(double epsilon, int maxIterations) {
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    @Override
    public Solution solve(QPProblem problem) throws Exception {
        Matrix H = problem.f().Hessian();
        SuanShuUtils.assertArgument(IsMatrix.positiveDefinite(H), "the Hessian must be positive definite");
        return new Solution(problem);
    }
}
