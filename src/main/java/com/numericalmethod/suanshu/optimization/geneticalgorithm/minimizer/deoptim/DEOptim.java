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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.Chromosome;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid.SimpleGridMinimizer;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.problem.OptimProblem;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Differential Evolution (DE) is a global optimization method.
 * It is a genetic algorithm that iteratively tries to improve a candidate solution with regard to a given measure of quality.
 * DE is used for multidimensional real-valued functions but does not use the gradient of the problem being optimized,
 * which means DE does not require for the optimization problem to be differentiable as is required by classic optimization methods
 * such as gradient descent and quasi-newton methods.
 * DE can therefore also be used on optimization problems that are not even continuous, are noisy, change over time, etc.
 * Comparing to other genetic algorithm optimization methods, the breakthrough of DE is the idea of using vector differences for perturbing the vector population.
 *
 * <p/>
 * The R equivalent function is {@code DEoptim} in package {@code DEoptim}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Differential_evolution">Wikipedia: Differential evolution</a>
 * <li><a href="http://www.icsi.berkeley.edu/~storn/code.html">Differential Evolution (DE)</a>
 * <li>"Kenneth Price, Rainer M. Storn, Jouni A. Lampinen. Differential Evolution: A Practical Approach to Global Optimization. Springer. 2005."
 * </ul>
 */
public class DEOptim extends SimpleGridMinimizer {

    /**
     * This factory constructs a new {@code DEOptimCellFactory} for each minimization problem.
     */
    public static interface NewCellFactory extends SimpleGridMinimizer.NewCellFactoryCtor {

        /**
         * Construct a new instance of {@code DEOptimCellFactory} for a minimization problem.
         *
         * @return a new instance of {@code DEOptimCellFactory}
         */
        @Override
        public DEOptimCellFactory newCellFactory();//override the return type
    }

    /**
     * This is the solution to a minimization problem using {@code DEOptim}.
     */
    protected class Solution extends SimpleGridMinimizer.Solution {

        private Solution(RealScalarFunction f) {
            super(f);
            ((DEOptimCellFactory) factory).setPopulation(population);
        }

        @Override
        public List<Chromosome> getNextGeneration(List<Chromosome> parents, List<Chromosome> children) {
            ArrayList<Chromosome> selected = new ArrayList<Chromosome>();

            for (int i = 0; i < nPopulation(); ++i) {
                if (children.get(i).fitness() <= parents.get(i).fitness()) {
                    selected.add(children.get(i));
                } else {
                    selected.add(parents.get(i));
                }
            }

            Collections.sort(selected);
            return selected;
        }

        @Override
        public Chromosome getChild(int i) {
            Chromosome r0 = ((DEOptimCellFactory) factory).getBase();

            //apply the Differential Evolution operators
            Chromosome cell2 = r0.mutate();
            Chromosome cell3 = getBest(i).crossover(cell2);

            return cell3;
        }
    }

    /**
     * Construct a {@code DEOptim} to solve unconstrained minimization problems.
     *
     * @param factoryCtor       a factory that constructs a new instance of {@code SimpleCellFactory} for each problem
     * @param parallel          {@code true} if the algorithm is to run in parallel (multi-core)
     * @param uniform           a uniform random number generator
     * @param epsilon           a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations     the maximum number of iterations
     * @param nStableIterations The solution is considered converged if the minimum does not change over this many iterations.
     */
    public DEOptim(NewCellFactory factoryCtor, boolean parallel, RandomLongGenerator uniform, double epsilon, int maxIterations, int nStableIterations) {
        super(factoryCtor, parallel, uniform, epsilon, maxIterations, nStableIterations);
    }

    /**
     * Construct a {@code DEOptim} to solve unconstrained minimization problems.
     *
     * @param Cr                the crossover probability
     * @param F                 the scaling factor
     * @param parallel          {@code true} if the algorithm is to run in parallel (multi-core)
     * @param epsilon           a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations     the maximum number of iterations
     * @param nStableIterations The solution is considered converged if the minimum does not change over this many iterations.
     * @see "Kenneth Price, Rainer M. Storn, Jouni A. Lampinen, "Section 2.1.8 Notation," Differential Evolution: A Practical Approach to Global Optimization, Springer, 2005."
     */
    public DEOptim(final double Cr, final double F, boolean parallel, double epsilon, int maxIterations, int nStableIterations) {
        this(
                new NewCellFactory() {

                    @Override
                    public DEOptimCellFactory newCellFactory() {
                        return new Rand1Bin(Cr, F);
                    }
                },
                parallel, new UniformRng(), epsilon, maxIterations, nStableIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(OptimProblem problem) throws Exception {
        return new Solution(problem.f());
    }
}
