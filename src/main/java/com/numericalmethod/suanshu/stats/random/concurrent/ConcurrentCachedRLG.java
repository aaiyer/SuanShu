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
package com.numericalmethod.suanshu.stats.random.concurrent;

import com.numericalmethod.suanshu.stats.random.concurrent.ConcurrentCachedGenerator.Generator;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;

/**
 * This is a fast thread-safe wrapper for random long generators. This class
 * is backed by {@link ConcurrentCachedGenerator}.
 *
 * @author Johannes Lehmann
 */
public class ConcurrentCachedRLG implements RandomLongGenerator {

    private final ConcurrentCachedGenerator<Long> concurrentGenerator;
    private final RandomNumberGenerator rlg;

    /**
     * Constructs a new instance which wraps the given random long generator and
     * uses a cache of the specified size. A larger cache will make the
     * simulation slightly faster at the expense of increased memory usage, but
     * can lead to unnecessary computation of values at the tail.
     *
     * @param rlg       the underlying generator
     * @param cacheSize the cache size
     * @see ConcurrentCachedGenerator#ConcurrentCachedGenerator(com.numericalmethod.suanshu.stats.random.concurrent.ConcurrentCachedGenerator.Generator, int)
     */
    public ConcurrentCachedRLG(final RandomLongGenerator rlg, int cacheSize) {
        this.rlg = rlg;
        this.concurrentGenerator = new ConcurrentCachedGenerator<Long>(
                new Generator<Long>() {

                    @Override
                    public Long next() {
                        return rlg.nextLong();
                    }
                },
                cacheSize);
    }

    /**
     * Construct a new instance which wraps the given random long generator and
     * uses a cache which has 8 entries per available core.
     *
     * @param rlg the underlying generator
     */
    public ConcurrentCachedRLG(RandomLongGenerator rlg) {
        this(rlg, Runtime.getRuntime().availableProcessors() * 8);
    }

    @Override
    public long nextLong() {
        return concurrentGenerator.next();
    }

    /**
     * Delegate to the underlying random long generator.
     * Note: <em>this method is NOT thread-safe.</em>
     *
     * @param seeds the seeds
     */
    @Override
    public void seed(long... seeds) {
        rlg.seed(seeds);
    }

    @Override
    public double nextDouble() {
        return (double) concurrentGenerator.next();
    }
}
