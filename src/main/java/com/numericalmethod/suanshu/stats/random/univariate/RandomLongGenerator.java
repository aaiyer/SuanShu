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
package com.numericalmethod.suanshu.stats.random.univariate;

/**
 * A (pseudo) random number generator that generates a sequence of {@code long}s that lack any pattern and are uniformly distributed.
 * <p/>
 * By default, an implementation of {@code RandomLongGenerator} is
 * <em>not thread-safe</em>, and thus should not be shared among multiple threads.
 * If a {@code RandomLongGenerator} instance is used in a multi-threaded
 * program, for example, use
 * <blockquote><code>
 * RandomLongGenerator rng = RandomNumberGenerators.synchronizedRLG(new MersenneTwister());
 * </code></blockquote>
 *
 * @author Haksun Li
 */
public interface RandomLongGenerator extends RandomNumberGenerator {

    /**
     * Get the next random {@code long}.
     *
     * @return the next random {@code long}
     */
    public long nextLong();
}
