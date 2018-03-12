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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.local;

import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid.SimpleCellFactory;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid.SimpleGridMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.MultivariateMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * This minimizer is a global optimization method.
 * It puts a mesh over the feasible region and then locally searches (optimizes) the neighborhood around each mesh point.
 * The algorithm tries to escape the local minimums by crossing over other local minimums using a genetic algorithm.
 * The local search (mutation) is performed by a typical local minimization method, such as Nelder-Mead, BFGS.
 *
 * @author Haksun Li
 */
public class GlobalSearchByLocalMinimizer extends SimpleGridMinimizer {

    private static final int defaultMaxIterations = 200;

    /**
     * Construct a {@code GlobalSearchByLocalMinimizer} to solve unconstrained minimization problems.
     *
     * @param factory           a factory that constructs a new instance of {@code Minimizer} for each problem
     * @param parallel          {@code true} if the algorithm is to run in parallel (multi-core)
     * @param uniform           a uniform random number generator
     * @param epsilon           a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations     the maximum number of iterations
     * @param nStableIterations The solution is considered converged if the minimum does not change over this many iterations.
     */
    public GlobalSearchByLocalMinimizer(
            final LocalSearchCellFactory.MinimizerFactory factory,
            boolean parallel,
            final RandomLongGenerator uniform,
            double epsilon,
            int maxIterations,
            int nStableIterations) {
        super(
                new NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new LocalSearchCellFactory(factory, uniform);
                    }
                },
                parallel, uniform, epsilon, maxIterations, nStableIterations);
    }

    /**
     * Construct a {@code GlobalSearchByLocalMinimizer} to solve unconstrained minimization problems.
     *
     * @param parallel      {@code true} if the algorithm is to run in parallel (multi-core)
     * @param uniform       a uniform random number generator
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public GlobalSearchByLocalMinimizer(boolean parallel, RandomLongGenerator uniform, final double epsilon, int maxIterations) {
        this(
                new LocalSearchCellFactory.MinimizerFactory() {

                    @Override
                    public MultivariateMinimizer newInstance() {
                        return new NelderMead(epsilon, defaultMaxIterations);
                    }
                },
                parallel, uniform, epsilon, maxIterations, 10);
    }

    /**
     * Construct a {@code GlobalSearchByLocalMinimizer} to solve unconstrained minimization problems.
     *
     * @param parallel {@code true} if the algorithm is to run in parallel (multi-core)
     */
    public GlobalSearchByLocalMinimizer(boolean parallel) {
        this(parallel, new UniformRng(), 1e-8, defaultMaxIterations);
    }
}
