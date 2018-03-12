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
package com.numericalmethod.suanshu.stats.random.univariate.uniform.linear;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.MWC8222;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.MersenneTwister;

/**
 * A linear congruential generator (LCG) produces a sequence of pseudo-random numbers
 * based on a linear recurrence relation.
 * An LCG is simple to understand and implement,
 * but it should <em>not</em> be used for applications where high-quality randomness is critical.
 * The higher quality pseudo-random generators available are, for instances, {@link MersenneTwister} and {@link MWC8222}.
 * If, however, only a small number of random numbers are needed, e.g,. a few thousands,
 * then an LCG should be sufficient.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Linear_congruential_generator">Wikipedia: Linear congruential generator</a>
 */
public interface LinearCongruentialGenerator extends RandomLongGenerator {

    /**
     * Get the order of recursion.
     *
     * @return the order of recursion
     */
    public int order();

    /**
     * Get the modulus of this linear congruential generator.
     *
     * @return the modulus
     */
    public long modulus();
}
