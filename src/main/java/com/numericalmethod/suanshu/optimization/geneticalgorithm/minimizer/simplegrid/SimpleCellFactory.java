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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.Chromosome;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * A {@code SimpleCellFactory} produces {@code SimpleCell}s.
 * A {@code SimpleCell} is a chromosome for a real valued function (an optimization problem) and a candidate solution.
 *
 * @author Haksun Li
 */
public class SimpleCellFactory {

    /**
     * the uniform random number generator
     */
    protected final RandomLongGenerator uniform;
    /**
     * the convergence rate
     */
    private final double rate;

    /**
     * Construct an instance of a {@code SimpleCellFactory}.
     *
     * @param rate    the convergence rate
     * @param uniform a uniform random number generator
     */
    public SimpleCellFactory(double rate, RandomLongGenerator uniform) {
        this.rate = rate;
        this.uniform = uniform;
    }

    /**
     * Construct an instance of a {@code SimpleCell}.
     *
     * @param f a real-valued function
     * @param x a candidate solution
     * @return a {@code SimpleCell}
     */
    public SimpleCell getSimpleCell(RealScalarFunction f, Vector x) {
        return new SimpleCell(f, x);
    }

    /**
     * A {@code SimpleCell} implements the two genetic operations.
     * <ul>
     * <li>Mutation by disturbing (scaling) the fitness by a percentage;
     * <li>Crossover by taking the midpoint (average) of two cells.
     * </ul>
     */
    public class SimpleCell extends RealScalarFunctionChromosome {

        protected SimpleCell(RealScalarFunction f, Vector x) {
            super(f, x);
        }

        /**
         * Mutate by random disturbs in a neighborhood.
         *
         * @return a mutant chromosome
         */
        @Override
        public Chromosome mutate() {
            final double offset = 1 - rate;
            final double width = 2 * rate;

            Vector z = new DenseVector(x());
            for (int i = 1; i <= z.size(); ++i) {
                double u = offset + uniform.nextDouble() * width;
                z.set(i, z.get(i) * u);
            }

            return getSimpleCell(f(), z);
        }

        /**
         * Crossover by taking the midpoint.
         *
         * @param other another chromosome
         * @return a hybrid chromosome
         */
        @Override
        public Chromosome crossover(Chromosome other) {
            SimpleCell that = (SimpleCell) other;
            Vector z = this.x().add(that.x()).scaled(0.5);
            return getSimpleCell(f(), z);
        }
    }

}
