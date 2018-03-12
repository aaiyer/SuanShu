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

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * The Best-1-Bin rule is the same as the {@linkplain  Rand1Bin Rand-1-Bin} rule,
 * except that it always pick the best candidate in the population to be the base.
 *
 * @author Haksun Li
 * @see "Kenneth Price, Rainer M. Storn, Jouni A. Lampinen, "Section 2.1.3 Mutation, Section 2.1.4 Crossover, Section 2.1.8 Notation," Differential Evolution: A Practical Approach to Global Optimization, Springer, 2005."
 */
public class Best1Bin extends Rand1Bin {

    /**
     * Construct an instance of {@code Best1Bin}.
     *
     * @param Cr      the crossover probability
     * @param F       the scaling factor
     * @param uniform a uniform random number generator
     */
    public Best1Bin(double Cr, double F, RandomLongGenerator uniform) {
        super(Cr, F, uniform);
    }

    /**
     * Construct an instance of {@code Best1Bin}.
     *
     * @param Cr the crossover probability
     * @param F  the scaling factor
     */
    public Best1Bin(double Cr, double F) {
        this(Cr, F, new UniformRng());
    }

    @Override
    public DeOptimCell getBase() {
        return (DeOptimCell) getPopulation().get(0);
    }
}
