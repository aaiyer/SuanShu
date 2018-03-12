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

import java.util.ArrayList;
import java.util.List;

/**
 * A generic wrapper that makes an underlying item generator thread-safe by
 * caching generated items in a concurrently-accessible list. This is more
 * efficient than plainly synchronizing all accessors of the generator.
 *
 * @param <T> the type of the item
 * @author Johannes Lehmann
 */
public class ConcurrentCachedGenerator<T> {

    /**
     * Defines a generic generator of type {@code T}.
     *
     * @author Johannes Lehmann
     * @param <T> the type of the generated items
     */
    public static interface Generator<T> {

        /**
         * Returns the next value in the underlying generated sequence.
         *
         * @return the next generated value
         */
        T next();
    }
    /*
     * Note: cache is volatile because updates to the reference must be seen by
     * all threads to ensure thread-safety.
     */
    private final int cacheSize;
    private volatile AtomicIndexedList<T> cache;
    private final Generator<T> generator;
    private final Object lock = new Object();

    /**
     * Creates a new instance which wraps the given item generator and uses a
     * cache of the specified size. A larger cache will make the concurrency
     * slightly faster at the expense of increased memory usage, but may lead to
     * the computation of unnecessary values at the tail.
     *
     * @param generator the underlying generator
     * @param cacheSize the size of the cache
     */
    public ConcurrentCachedGenerator(Generator<T> generator, int cacheSize) {
        this.generator = generator;
        this.cacheSize = cacheSize;
        replaceCache();
    }

    /**
     * Returns the next value in the generated sequence.
     *
     * @return the next generated value
     */
    public T next() {
        AtomicIndexedList<T> localCache = cache;
        int localIndex = localCache.getNewIndex();

        if (localIndex < localCache.size()) {
            return localCache.get(localIndex);
        } else if (localIndex == localCache.size()) {
            replaceCache(); // generate a new cache after the current one is exhausted
            return next();
        } else {
            synchronized (lock) {
                while (cache == localCache) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ex) {
                        // Don't care if we're interrupted
                    }
                }
            }
            return next();
        }
    }

    /*
     * Note: this method is synchronized to avoid multiple threads accessing the
     * underlying RNG.
     */
    private synchronized void replaceCache() {
        List<T> list = new ArrayList<T>(cacheSize);
        for (int i = 0; i < cacheSize; i++) {
            list.add(generator.next());
        }
        AtomicIndexedList<T> newCache = new AtomicIndexedList<T>(list);

        synchronized (lock) {
            this.cache = newCache;
            lock.notifyAll();
        }
    }
}
