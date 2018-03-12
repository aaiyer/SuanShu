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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.constrained;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.Chromosome;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.DEOptimCellFactory;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.List;

/**
 * This defines a Differential Evolution operator that takes in account constraints.
 *
 * @author Haksun Li
 */
public abstract class ConstrainedCellFactory extends DEOptimCellFactory {

    /**
     * a factory that defines the unconstrained Differential Evolution operators
     */
    protected final DEOptimCellFactory unconstrainedFactory;

    /**
     * Construct an instance of a {@code ConstrainedCellFactory} that define the constrained Differential Evolution operators.
     *
     * @param unconstrainedFactory a factory that defines the unconstrained Differential Evolution operators
     */
    protected ConstrainedCellFactory(DEOptimCellFactory unconstrainedFactory) {
        super(unconstrainedFactory);
        this.unconstrainedFactory = unconstrainedFactory;
    }

    @Override
    public void setPopulation(List<Chromosome> population) {
        unconstrainedFactory.setPopulation(population);
        super.setPopulation(population);
    }

    /**
     * Override this method to put in whatever constraints in the minimization problem.
     *
     * @param f the original objective function
     * @param x the original, unprocessed, unconstrained candidate solution
     * @return a constrained cell/chromosome
     */
    @Override
    public abstract ConstrainedCell getSimpleCell(RealScalarFunction f, Vector x);

    /**
     * A {@code ConstrainedCell} is a chromosome for a constrained optimization problem.
     * It encodes the real valued objective function, a candidate solution, as well as the constraints.
     */
    public class ConstrainedCell extends DeOptimCell {

        private final SimpleCell cell;

        protected ConstrainedCell(RealScalarFunction f, Vector x) {
            super(f, x);
            this.cell = unconstrainedFactory.getSimpleCell(f, x);
        }

        @Override
        public Chromosome mutate() {
            SimpleCell mutant = (SimpleCell) cell.mutate();
            return getSimpleCell(mutant.f(), mutant.x());
        }

        @Override
        public Chromosome crossover(Chromosome obj) {
            SimpleCell hybrid = (SimpleCell) cell.crossover(obj);
            return getSimpleCell(hybrid.f(), hybrid.x());
        }
    }

}
