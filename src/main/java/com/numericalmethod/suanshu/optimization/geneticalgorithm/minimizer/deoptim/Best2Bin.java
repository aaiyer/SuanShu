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
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The Best-1-Bin rule always picks the best chromosome as the base.
 * The two genetic operations are:
 * <ul>
 * <li>mutation by adding TWO scaled, randomly sampled vector difference to a third vector (differential mutation);
 * <li>crossover by performing a uniform crossover (discrete recombination).
 * </ul>
 *
 * @author Haksun Li
 */
public class Best2Bin extends Best1Bin {

    /**
     * Construct an instance of {@code Best2Bin}.
     *
     * @param Cr      the crossover probability
     * @param F       the scaling factor
     * @param uniform a uniform random number generator
     */
    public Best2Bin(double Cr, double F, RandomLongGenerator uniform) {
        super(Cr, F, uniform);
    }

    /**
     * Construct an instance of {@code Best2Bin}.
     *
     * @param Cr the crossover probability
     * @param F  the scaling factor
     */
    public Best2Bin(double Cr, double F) {
        this(Cr, F, new UniformRng());
    }

    @Override
    public DeBest2BinCell getSimpleCell(RealScalarFunction f, Vector x) {
        return new DeBest2BinCell(f, x);
    }

    public class DeBest2BinCell extends DeRand1BinCell {

        protected DeBest2BinCell(RealScalarFunction f, Vector x) {
            super(f, x);
        }

        @Override
        public DeBest2BinCell mutate() {
            DeOptimCell r1;
            do {
                r1 = getOne();
            } while (r1 == this); //r0 = this

            DeOptimCell r2;
            do {
                r2 = getOne();
            } while (r2 == r1 || r2 == this);

            DeOptimCell r3;
            do {
                r3 = getOne();
            } while (r3 == r2 || r3 == r1 || r3 == this);

            DeOptimCell r4;
            do {
                r4 = getOne();
            } while (r4 == r3 || r4 == r2 || r4 == r1 || r4 == this);

            Vector v0 = new DenseVector(this.x());

            Vector v1 = new DenseVector(r1.x());
            Vector v2 = new DenseVector(r2.x());
            Vector dv1 = v1.minus(v2);

            Vector v3 = new DenseVector(r3.x());
            Vector v4 = new DenseVector(r4.x());
            Vector dv2 = v3.minus(v4);

            Vector u = v0.add(dv1.scaled(F)).add(dv2.scaled(F));

            return getSimpleCell(f(), u);
        }
    }

}
