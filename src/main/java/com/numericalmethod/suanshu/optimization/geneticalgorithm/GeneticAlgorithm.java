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

import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.stats.random.RngUtils;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A genetic algorithm (GA) is a search heuristic that mimics the process of natural evolution.
 * A population of strings (called chromosomes or the genotype of the genome),
 * which encode candidate solutions to an optimization problem, evolves toward better solutions.
 * The evolution usually starts from a population of randomly generated individuals and happens in generations.
 * In each generation, the fitness of every individual in the population is evaluated,
 * multiple individuals are stochastically selected from the current population (based on their fitness),
 * and modified (recombined and possibly randomly mutated) to form a new population.
 * The new population is then used in the next iteration of the algorithm.
 * Commonly, the algorithm terminates when either a maximum number of generations has been produced,
 * or a satisfactory fitness level has been reached for the population.
 * If the algorithm has terminated due to a maximum number of generations, a satisfactory solution may or may not have been reached.
 * <p/>
 * This implementation is the meta evolutionary algorithm in the reference.
 * It provides a framework for implementing multiple classes of evolutionary algorithms, e.g., Genetic Algorithm, Differential Evolution.
 * All methods are {@code protected} so any can be overridden to allow customization.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Differential_evolution">Wikipedia: Differential evolution</a>
 * <li>"Kenneth Price, Rainer M. Storn, Jouni A. Lampinen, "Fig. 1.15, Meta-algorithm for evolution strategies (ESs)," Differential Evolution: A Practical Approach to Global Optimization, 2005."
 * </ul>
 */
public abstract class GeneticAlgorithm {

    /**
     * Initialize the first population.
     *
     * @return the first population
     */
    protected abstract ArrayList<Chromosome> initialization();

    /**
     * This is the convergence criterion.
     *
     * @return {@code true} if the search has converged
     */
    protected abstract boolean isConverged();
    /**
     * This indicate if the algorithm is to run in parallel (multi-core).
     */
    protected final ParallelExecutor parallel;
    /**
     * This is a uniform random number generator.
     */
    protected final RandomLongGenerator uniform;
    /**
     * This is the (current) population pool.
     * <p/>
     * An implementation must guarantee that this is sorted in descending order.
     */
    protected final ArrayList<Chromosome> population = new ArrayList<Chromosome>();//{@code final} ensures that the address does not change.

    /**
     * Construct an instance of this implementation of genetic algorithm.
     *
     * @param parallel {@code true} if the algorithm is to run in parallel
     * @param uniform  a uniform random number generator
     */
    public GeneticAlgorithm(boolean parallel, RandomLongGenerator uniform) {
        this.parallel = parallel ? new ParallelExecutor() : null;
        this.uniform = parallel ? RngUtils.synchronizedRLG(uniform) : uniform;
    }

    /**
     * Run the genetic algorithm.
     */
    public void run() {
        population.addAll(initialization());

        for (; !isConverged();) {
            step();
        }
    }

    /**
     * Run a step in genetic algorithm: produce the next generation of chromosome pool.
     *
     * @return true
     */
    protected Object step() {
        int nChildren = nChildren();
        final ArrayList<Chromosome> children = GeneticAlgorithm.getNewPool(nChildren);

        if (parallel != null) {
            //multiple threads
            try {
                parallel.forLoop(0, children.size(),
                                 new LoopBody() {

                    @Override
                    public void run(int i) throws Exception {
                        Chromosome child = getChild(i);
                        child.fitness();//force objective function evaluation in the parallel loop
                        children.set(i, child);
                    }
                });
            } catch (Exception ex) {
                throw new RuntimeException("failed to generate children", ex);
            }
        } else {
            //single thread
            for (int i = 0; i < children.size(); ++i) {
                children.set(i, getChild(i));
            }
        }

        List<Chromosome> newPopulation = getNextGeneration(population, children);
        population.clear();
        population.addAll(newPopulation);

        return true;
    }

    /**
     * Produce a child chromosome.
     * <p/>
     * This implementation first applies the crossover and then the mutation operators.
     *
     * @param i an index that ranges from 0 to (population size - 1)
     * @return a child chromosome
     */
    protected Chromosome getChild(int i) {
        Chromosome cell1 = getOne();
        Chromosome cell2 = getOne();

        //apply the evolutionary operators
        Chromosome cell3 = cell1.crossover(cell2);
        Chromosome cell4 = cell3.mutate();

        return cell4;
    }

    /**
     * Pick a chromosome for mutation/crossover.
     * <p/>
     * This implementation uniformly and randomly chooses from the population.
     *
     * @return a chromosome
     */
    protected Chromosome getOne() {
        int i = (int) (nPopulation() * uniform.nextDouble());//[0, nPopulation()]
        return getBest(i);
    }

    /**
     * Populate the next generation using the parent and children chromosome pools.
     * <p/>
     * This implementation chooses the best chromosomes among the parents and children.
     *
     * @param parents  the parent chromosome pool
     * @param children the children chromosome pool
     * @return the next generation population
     */
    protected List<Chromosome> getNextGeneration(List<Chromosome> parents, List<Chromosome> children) {
        int size = parents.size() + children.size();
        ArrayList<Chromosome> concat = new ArrayList<Chromosome>(size);

        for (int i = 0; i < parents.size(); ++i) {
            concat.add(parents.get(i));
        }
        for (int i = 0; i < children.size(); ++i) {
            concat.add(children.get(i));
        }

        Collections.sort(concat);

        List<Chromosome> selected = concat.subList(0, nChildren());
        return selected;
    }

    /**
     * Get the size of the population pool, that is the number of chromosomes.
     *
     * @return the population size
     */
    protected int nPopulation() {
        return population.size();
    }

    /**
     * Get the number of children before populating the next generation.
     * <p/>
     * In this implementation, it is the same as the population size (same as the number of parents).
     *
     * @return the number of children
     */
    protected int nChildren() {
        return nPopulation();
    }

    /**
     * Get the <i>i</i>-th best chromosome.
     *
     * @param i an index
     * @return the <i>i</i>-th best chromosome
     */
    protected Chromosome getBest(int i) {
        return population.get(i);
    }

    /**
     * Allocate space for a population pool.
     *
     * @param size the population size
     * @return a population pool
     */
    protected static ArrayList<Chromosome> getNewPool(int size) {
        ArrayList<Chromosome> pool = new ArrayList<Chromosome>(
                Collections.<Chromosome>nCopies(size, null));

        return pool;
    }
}
