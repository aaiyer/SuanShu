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
package com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.MultivariateMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The penalty method is an algorithm for solving a constrained minimization problem with general constraints.
 * It replaces a constrained optimization problem by a series of unconstrained problems
 * whose solutions ideally converge to the solution of the original constrained problem.
 * The unconstrained problems are formed by adding a term to the objective function
 * that consists of a penalty parameter and a measure of violation of the constraints.
 * The measure of violation is zero in the feasible region and is positive when the constraints are violated.
 * <p/>
 * In this implementation, we use only one iteration.
 * In this iteration, we multiply the penalties by ɣ, which is a very big number, to penalize a solution outside the feasible region.
 * This is to mimic when ɣ approaches infinity.
 * A classical unconstrained minimization algorithm is then applied to the modified problem.
 * It can be shown that if the penalty is large enough, the original and modified problems have the same solution.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>@see <a href="http://en.wikipedia.org/wiki/Penalty_method">Wikipedia: Penalty method</a>
 * <li>"R. Fletcher, "Section 22.4, Penalty Methods, Chapter 22, Non-differentiable Optimization," Practical Methods of Optimization. 2nd ed. Wiley. May 2000."
 * </ul>
 */
public class PenaltyMethodMinimizer implements ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>> {

    /**
     * For each constrained optimization problem, the solver creates a new penalty function for it.
     */
    public static interface PenaltyFunctionFactory {

        /**
         * Get an instance of the penalty function.
         *
         * @param problem a constrained optimization problem
         * @return the penalty function
         */
        public PenaltyFunction getPenaltyFunction(ConstrainedOptimProblem problem);
    }

    /**
     * the default penalty function factory
     */
    public static final PenaltyFunctionFactory DEFAULT_PENALTY_FUNCTION_FACTORY =
            new PenaltyFunctionFactory() {

                @Override
                public PenaltyFunction getPenaltyFunction(ConstrainedOptimProblem problem) {
                    if (problem.getEqualityConstraints() == null && problem.getLessThanConstraints() == null) {//no constraint
                        return new ZERO(problem.dimension());
                    }

                    PenaltyFunction penaltyFunction =
                            new SumOfPenalties(
                            problem.getEqualityConstraints() != null ? new Courant(problem.getEqualityConstraints()) : new ZERO(problem.dimension()),
                            problem.getLessThanConstraints() != null ? new Fletcher(problem.getLessThanConstraints()) : new ZERO(problem.dimension()));

                    return penaltyFunction;
                }
            };
    private final double gamma;
    private final PenaltyFunctionFactory penaltyFunctionFactory;
    private final MultivariateMinimizer<? extends IterativeMinimizer<Vector>> minimizer;

    /**
     * Construct a constrained minimizer using the penalty method.
     *
     * @param <T>                    a multivariate, iterative minimizer type
     * @param penaltyFunctionFactory a factory to construct a penalty function from a constrained optimization problem
     * @param gamma                  the ɣ as in R. Fletcher
     * @param minimizer              an unconstrained minimization solver
     */
    public <T extends MultivariateMinimizer<? extends IterativeMinimizer<Vector>>> PenaltyMethodMinimizer(
            PenaltyFunctionFactory penaltyFunctionFactory,
            double gamma,
            T minimizer) {
        this.penaltyFunctionFactory = penaltyFunctionFactory;
        this.gamma = gamma;
        this.minimizer = minimizer;
    }

    /**
     * Construct a constrained minimizer using the penalty method.
     * ɣ should be big enough to reflect the penalty, but it cannot be too big to overflow the computations (to produce NaNs).
     * It is recommended to experiment different ɣ's to get better results.
     *
     * @param gamma the ɣ as in R. Fletcher
     */
    public PenaltyMethodMinimizer(double gamma) {
        this(DEFAULT_PENALTY_FUNCTION_FACTORY, gamma, new NelderMead(Constant.EPSILON, 200));
    }

    /**
     * Construct a constrained minimizer using the penalty method.
     */
    public PenaltyMethodMinimizer() {
        this(1e30);
    }

    @Override
    public IterativeMinimizer<Vector> solve(final ConstrainedOptimProblem problem) throws Exception {
        return new IterativeMinimizer<Vector>() {

            private final IterativeMinimizer<Vector> soln0 = minimizer.solve(new C2OptimProblemImpl(getFP()));

            @Override
            public double minimum() {
                return soln0.minimum();
            }

            @Override
            public ImmutableVector minimizer() {
                return new ImmutableVector(soln0.minimizer());
            }

            @Override
            public Vector search(Vector... initials) throws Exception {
                return soln0.search(initials);
            }

            @Override
            public void setInitials(Vector... initials) {
                soln0.setInitials(initials);
            }

            @Override
            public Object step() throws Exception {
                return soln0.step();
            }

            /** Construct the penalized objective function to be solved as an unconstrained optimization problem. */
            private RealScalarFunction getFP() {
                final PenaltyFunction penalty = penaltyFunctionFactory.getPenaltyFunction(problem);
                RealScalarFunction fp = new RealScalarFunction() {

                    @Override
                    public Double evaluate(Vector x) {
                        double f = problem.f().evaluate(x);
                        double p = penalty.evaluate(x);
                        double result = f + gamma * p;

                        return result;
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return problem.f().dimensionOfDomain();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return problem.f().dimensionOfRange();
                    }
                };

                return fp;
            }
        };
    }
}
