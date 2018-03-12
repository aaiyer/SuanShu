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
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid.SimpleCellFactory;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.Collections;
import java.util.List;

/**
 * A {@code DEOptimCellFactory} produces {@code DeOptimCell}s.
 * A {@code DeOptimCell} is a chromosome for a real valued function (an optimization problem) and a candidate solution.
 * They together define the two genetic operations of an Differential Evolution implementation.
 * <ul>
 * <li>Mutation by perturbing the vector population by vector differences;
 * <li>Crossover by performing a uniform crossover (discrete recombination).
 * </ul>
 *
 * @author Haksun Li
 */
public abstract class DEOptimCellFactory extends SimpleCellFactory {

    /**
     * the crossover probability
     */
    protected final double Cr;
    /**
     * the scaling factor
     */
    protected final double F;
    /**
     * an unmodifiable view of the parents population
     */
    private List<Chromosome> population;

    /**
     * Compute the <i>F</i> critical value.
     *
     * @param Cr          the crossover probability
     * @param nPopulation the population size
     * @return the minimum value for <i>F</i>
     * @see "eq. 2.32"
     */
    public static double Fmin(double Cr, int nPopulation) {
        double Fmin = 1. - Cr / 2.;
        Fmin /= nPopulation;//TOOD: won't know population size until after initialization...
        Fmin = Math.sqrt(Fmin);
        return Fmin;
    }

    /**
     * Construct an instance of a {@code DEOptimCellFactory}.
     *
     * @param Cr      the crossover probability
     * @param F       the scaling factor
     * @param uniform a uniform random number generator
     */
    protected DEOptimCellFactory(double Cr, double F, RandomLongGenerator uniform) {
        super(F, uniform);

        this.Cr = Cr;
        this.F = F;
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code DEOptimCellFactory}
     */
    protected DEOptimCellFactory(DEOptimCellFactory that) {
        this(that.Cr, that.F, that.uniform);
    }

    /**
     * Set the current generation.
     *
     * @param population the current population pool
     */
    public void setPopulation(List<Chromosome> population) {
        this.population = Collections.unmodifiableList(population);
    }

    /**
     * Get the current generation.
     *
     * @return the current generation
     */
    protected List<Chromosome> getPopulation() {
        return population;//unmodifiable
    }

    /**
     * Pick a base chromosome from the population.
     *
     * @return the base chromosome
     */
    public DeOptimCell getBase() {
        return getOne();
    }

    /**
     * Pick a random chromosome from the population.
     *
     * @return a random chromosome
     */
    public DeOptimCell getOne() {
        int i = (int) (getPopulation().size() * uniform.nextDouble()); //[0, nPopulation()]
        return (DeOptimCell) getPopulation().get(i);
    }

    /**
     * A {@code DeOptimCell} is a chromosome for a real valued function (an optimization problem) and a candidate solution.
     * It is an {@code abstract} class so the subclasses must implement the two genetic operations.
     * The specific Differential Evolution rules are implemented by these two genetic operations.
     */
    public abstract class DeOptimCell extends SimpleCell {

        protected DeOptimCell(RealScalarFunction f, Vector x) {
            super(f, x);
        }

        @Override
        public abstract Chromosome mutate();//override to force implementation (no default implementation)

        @Override
        public abstract Chromosome crossover(Chromosome obj);//override to force implementation (no default implementation)
    }

}
