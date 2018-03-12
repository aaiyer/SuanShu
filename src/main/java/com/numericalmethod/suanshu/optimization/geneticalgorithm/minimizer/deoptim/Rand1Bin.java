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
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The Rand-1-Bin rule is defined by:
 * <ul>
 * <li>mutation by adding a scaled, randomly sampled vector difference to a third vector (differential mutation);
 * <li>crossover by performing a uniform crossover (discrete recombination).
 * </ul>
 *
 * @author Haksun Li
 * @see "Kenneth Price, Rainer M. Storn, Jouni A. Lampinen, "Section 2.1.3 Mutation, Section 2.1.4 Crossover, Section 2.1.8 Notation," Differential Evolution: A Practical Approach to Global Optimization, Springer, 2005."
 */
public class Rand1Bin extends DEOptimCellFactory {

    /**
     * Construct an instance of {@code Rand1Bin}.
     *
     * @param Cr      the crossover probability
     * @param F       the scaling factor
     * @param uniform a uniform random number generator
     */
    public Rand1Bin(double Cr, double F, RandomLongGenerator uniform) {
        super(Cr, F, uniform);
    }

    /**
     * Construct an instance of {@code Rand1Bin}.
     *
     * @param Cr the crossover probability
     * @param F  the scaling factor
     */
    public Rand1Bin(double Cr, double F) {
        this(Cr, F, new UniformRng());
    }

    @Override
    public DeRand1BinCell getSimpleCell(RealScalarFunction f, Vector x) {
        return new DeRand1BinCell(f, x);
    }

    /**
     * This chromosome defines the Rand-1-Bin rule.
     * The two genetic operations of this chromosome are:
     * <ul>
     * <li>mutation by adding a scaled, randomly sampled vector difference to a third vector (differential mutation);
     * <li>crossover by performing a uniform crossover (discrete recombination).
     * </ul>
     */
    public class DeRand1BinCell extends DeOptimCell {

        protected DeRand1BinCell(RealScalarFunction f, Vector x) {
            super(f, x);
        }

        @Override
        public DeRand1BinCell mutate() {
            DeOptimCell r1;
            do {
                r1 = getOne();
            } while (r1 == this); //r0 = this

            DeOptimCell r2;
            do {
                r2 = getOne();
            } while (r2 == r1 || r2 == this);

            Vector v0 = new DenseVector(this.x());
            Vector v1 = new DenseVector(r1.x());
            Vector v2 = new DenseVector(r2.x());
            Vector dv = v1.minus(v2);

            Vector u = v0.add(dv.scaled(F)); //eq. 2.5

            return getSimpleCell(f(), u);
        }

        @Override
        public DeRand1BinCell crossover(Chromosome obj) { //eq. 2.6
            DeOptimCell mutant = (DeOptimCell) obj;
            Vector mx = mutant.x();

            Vector z = new DenseVector(x());

            //force mutation using dna from new mutant
            int jrand = (int) (z.size() * uniform.nextDouble()) + 1; //[1, length]
            z.set(jrand, mx.get(jrand));

            for (int k = 1; k <= f().dimensionOfDomain(); ++k) {
                if (uniform.nextDouble() <= Cr) {
                    z.set(k, mx.get(k));
                }
            }

            return getSimpleCell(f(), z);
        }
    }

}
