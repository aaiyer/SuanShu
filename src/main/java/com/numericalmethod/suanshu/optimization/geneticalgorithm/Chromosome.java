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
package com.numericalmethod.suanshu.optimization.geneticalgorithm;

/**
 * A chromosome is a representation of a solution to an optimization problem.
 * A genetic algorithm creates new chromosomes from existing ones by taking parts and combine them in new ways.
 * As a genetic algorithm usually runs in a multi-core environment for performance,
 * it is important to ensure that an implementation of the chromosome operations are thread-safe and can run in parallel.
 *
 * @author Haksun Li
 */
public interface Chromosome extends Comparable<Chromosome> {

    /**
     * This is the fitness to determine how good this chromosome is.
     *
     * @return the fitness
     */
    public double fitness();

    /**
     * Construct a {@code Chromosome} by mutation.
     *
     * @return a mutated chromosome
     */
    public Chromosome mutate();

    /**
     * Construct a {@code Chromosome} by crossing over a pair of chromosomes.
     *
     * @param that another chromosome
     * @return a crossed over chromosome
     */
    public Chromosome crossover(Chromosome that);
}
