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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A wrapper for a list with an atomic counter (for the current index) into one
 * instance, such that both can be swapped atomically.
 *
 * <p>
 * Note: this class does not implement {@code List} interface to minimize the
 * access to the underlying list instance.
 *
 * @author Johannes Lehmann
 * @param <T> type of the list items
 */
class AtomicIndexedList<T> {

    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final List<T> values;

    /**
     * Creates a new instance that wraps the given list.
     *
     * @param list the wrapped list
     */
    AtomicIndexedList(List<T> list) {
        this.values = list;
    }

    public int size() {
        return values.size();
    }

    public T get(int index) {
        return values.get(index);
    }

    /**
     * Returns a unique index to the internal list. If the index is in the range
     * {@code [0, size)}, the caller can thread-safely use the value returned by
     * {@code get(index)}.
     *
     * @return the unique index
     */
    public int getNewIndex() {
        return currentIndex.getAndIncrement();
    }
}
