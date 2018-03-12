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
package com.numericalmethod.suanshu.optimization.constrained.integer.bruteforce;

import com.numericalmethod.suanshu.algorithm.Combination;
import com.numericalmethod.suanshu.analysis.function.SubFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarSubFunction;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.constraint.ConstraintsUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod.PenaltyMethodMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.parallel.IterationBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This implementation solves an integral constrained minimization problem by brute force search for all possible integer combinations.
 * For each combination, it fixes the integral variables to the integers. The problem then becomes a real valued minimization problem,
 * which can be solved by another constrained minimization algorithm, e.g., Nelder-Mead or BFGS.
 * The drawbacks are that: (1) the integral domains must be enumerable, hence bounded and known; (2) the performance is very slow.
 *
 * @author Haksun Li
 */
public class BruteForceIPMinimizer implements IPMinimizer<BruteForceIPProblem, BruteForceIPMinimizer.Solution> {

    /**
     * This factory constructs a new instance of {@code ConstrainedMinimizer} to solve a real valued minimization problem.
     *
     * @param <U> a subclass of {@code ConstrainedMinimizer}
     */
    public static interface ConstrainedMinimizerFactory<U extends ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>>> {

        /**
         * Construct a new instance of {@code ConstrainedMinimizer} to solve a real valued minimization problem.
         *
         * @return a new instance of a constrained minimizer
         */
        public U newInstance();
    }

    // This structure stores the results computed for each combination of integers.
    private static class Result {

        private final double f;
        private final Vector solnReal;
        private final Map<Integer, Double> solnInt;

        private Result(double f, Vector solnReal, Map<Integer, Double> solnInt) {
            this.f = f;
            this.solnReal = solnReal;
            this.solnInt = new HashMap<Integer, Double>(solnInt);
        }
    }

    /**
     * This is the solution to an integral constrained minimization using the brute-force search.
     */
    public class Solution implements MinimizationSolution<Vector> {

        private BruteForceIPProblem problem;
        private ImmutableVector minimizer;
        private double fmin = Double.POSITIVE_INFINITY;

        private Solution(BruteForceIPProblem problem) {
            this.problem = problem;
        }

        /**
         * Search for a solution that minimizes the objective function from the
         * given starting points.
         *
         * @param initials the initial guesses of the non fixed double variables
         * @return an (approximate) minimizer
         * @throws Exception when an error occurs during the search
         */
        public Vector search(final Vector... initials) throws Exception {
            //enumeration of all possible combinations
            final int[] indices = problem.getIntegerIndices();
            Integer[][] sets = new Integer[indices.length][];
            for (int i = 0; i < indices.length; ++i) {
                sets[i] = DoubleUtils.intArray2List(problem.getIntegralConstraint(indices[i]).domain).toArray(new Integer[0]);
            }
            Combination<Integer> domains = indices.length > 0 ? new Combination<Integer>(sets) ://all possible values for the fixed variables
                    new Combination<Integer>(new Integer[]{null});//for the case where there is no integral constraint

            final ConcurrentLinkedQueue<Result> results = new ConcurrentLinkedQueue<Result>();
            try {
                new ParallelExecutor().forEach(
                        domains,
                        new IterationBody<List<Integer>>() {

                            @Override
                            public void run(List<Integer> values) {//TODO: what if fixed is empty
                                try {
                                    //with the index variables held fixed
                                    final Map<Integer, Double> fixed = new HashMap<Integer, Double>();
                                    for (int i = 0; i < indices.length; ++i) {
                                        fixed.put(indices[i], values.get(i).doubleValue());
                                    }

                                    //construct another minimization problem over the Real
                                    RealScalarSubFunction subf = new RealScalarSubFunction(problem.f(), fixed);
                                    int subfDimension = subf.dimensionOfDomain();

                                    //<editor-fold defaultstate="collapsed" desc="equality constraints">
                                    EqualityConstraints equal = null;
                                    if (problem.getEqualityConstraints() != null) {
                                        equal = new EqualityConstraints() {

                                            @Override
                                            public ArrayList<RealScalarFunction> getConstraints() {
                                                ArrayList<RealScalarFunction> subf = new ArrayList<RealScalarFunction>();
                                                for (RealScalarFunction f : problem.getEqualityConstraints().getConstraints()) {
                                                    subf.add(new RealScalarSubFunction(f, fixed));
                                                }
                                                return subf;
                                            }

                                            @Override
                                            public int dimension() {
                                                return problem.getEqualityConstraints().dimension() - fixed.size();
                                            }

                                            @Override
                                            public int size() {
                                                return problem.getEqualityConstraints().size();
                                            }
                                        };

                                        if (equal.getConstraints().get(0).dimensionOfDomain() == 0) {
                                            if (!ConstraintsUtils.isSatisfied(equal, new DenseVector(0))) {//empty vector
                                                return;//constraint violated
                                            }
                                        }
                                    }
                                    //</editor-fold>

                                    //<editor-fold defaultstate="collapsed" desc="less-than-or-equal-to constraints">
                                    LessThanConstraints less = null;
                                    if (problem.getLessThanConstraints() != null) {
                                        less = new LessThanConstraints() {

                                            @Override
                                            public ArrayList<RealScalarFunction> getConstraints() {
                                                ArrayList<RealScalarFunction> subh = new ArrayList<RealScalarFunction>();
                                                for (RealScalarFunction h : problem.getLessThanConstraints().getConstraints()) {
                                                    subh.add(new RealScalarSubFunction(h, fixed));
                                                }
                                                return subh;
                                            }

                                            @Override
                                            public int dimension() {
                                                return problem.getLessThanConstraints().dimension() - fixed.size();
                                            }

                                            @Override
                                            public int size() {
                                                return problem.getLessThanConstraints().size();
                                            }

                                            @Override
                                            public GreaterThanConstraints toGreaterThanConstraints() {
                                                throw new UnsupportedOperationException("Not supported yet.");
                                            }
                                        };

                                        if (less.getConstraints().get(0).dimensionOfDomain() == 0) {
                                            if (!ConstraintsUtils.isSatisfied(less, new DenseVector(0))) {//empty vector
                                                return;//constraint violated
                                            }
                                        }
                                    }
                                    //</editor-fold>

                                    ConstrainedOptimProblem subProblem = new ConstrainedOptimProblemImpl1(subf, equal, less);

                                    // find the solution for the Real part of the minimization problem
                                    Vector solnReal = new DenseVector(0);
                                    ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>> solver = factory.newInstance();
                                    if (subfDimension > 0) {
                                        //not a constant function
                                        IterativeMinimizer<Vector> soln = solver.solve(subProblem);
                                        solnReal = soln.search(new DenseVector(subf.getVariablePart(initials[0].toArray())));
                                    }

                                    double f = subf.evaluate(solnReal);
                                    // store result for final comparison
                                    results.add(new Result(f, solnReal, fixed));
                                } catch (Exception ex) {
                                    //TODO: what to do?
                                }
                            }
                        });
            } catch (MultipleExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            }

            // find the minimum among all results
            Vector realMinimizer = null;
            Map<Integer, Double> intMinimizer = null;
            for (Result result : results) {
                if (result.f < fmin) {
                    //found a better solution
                    fmin = result.f;
                    realMinimizer = result.solnReal;
                    intMinimizer = result.solnInt;
                }
            }
            //expand the minimizer to include the integral variable values
            Vector z = SubFunction.getAllParts(realMinimizer, intMinimizer);
            minimizer = new ImmutableVector(z);

            return minimizer();
        }

        @Override
        public double minimum() {
            return fmin;
        }

        @Override
        public ImmutableVector minimizer() {
            return minimizer;
        }
    }

    private final ConstrainedMinimizerFactory<? extends ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>>> factory;

    /**
     * Construct a brute force minimizer to solve integral constrained minimization problems.
     *
     * @param factory a factory that constructs a new instance of {@code ConstrainedMinimizerFactory} to solve a real valued minimization problem
     */
    public BruteForceIPMinimizer(ConstrainedMinimizerFactory<? extends ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>>> factory) {
        this.factory = factory;
    }

    /**
     * Construct a brute force minimizer to solve integral constrained minimization problems.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public BruteForceIPMinimizer(final double epsilon, final int maxIterations) {
        this(new ConstrainedMinimizerFactory<ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>>>() {

            @Override
            public ConstrainedMinimizer<ConstrainedOptimProblem, IterativeMinimizer<Vector>> newInstance() {
                return new PenaltyMethodMinimizer(
                        PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                        1e30,
                        new NelderMead(epsilon, maxIterations));
            }
        });
    }

    @Override
    public Solution solve(BruteForceIPProblem problem) throws Exception {
        return new Solution(problem);
    }
}
