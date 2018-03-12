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
package com.numericalmethod.suanshu.parallel;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a wrapper for an iterator to allow performing both element
 * availability check and element retrieval in one synchronized method call.
 * {@code Iterator} interface splits these two functionalities into two separate
 * calls, namely, {@code hasNext()} and {@code next()}. Therefore, these two
 * methods have to be called within a single synchronized block. This class
 * wrapper provides a synchronized method {@link #next() next()} to make the
 * iterator callable within a single for-loop construct.
 *
 * @author Ken Yiu
 */
class SynchronizedIterator<T> {

    /**
     * Contains both element existence and the element itself. If the element
     * does not exist, calling {@code get()} will throw
     * {@link NoSuchElementException}.
     *
     * @param <T> the type of the wrapped element
     */
    static class Element<T> {

        private final boolean exists;
        private final T element;

        private Element(boolean exists, T element) {
            this.exists = exists;
            this.element = element;
        }

        boolean exists() {
            return exists;
        }

        T get() {
            if (!exists) {
                throw new NoSuchElementException();
            }
            return element;
        }
    }
    private final Iterator<T> iterator;

    /**
     * Creates an instance that wraps the given iterator.
     *
     * @param iterator the iterator to be wrapped
     */
    SynchronizedIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * Provides an atomic method call for checking the next element's
     * availability and returning the element.
     *
     * @return the next element
     */
    synchronized Element<T> next() {
        if (iterator.hasNext()) {
            return new Element<T>(true, iterator.next());
        }
        return new Element<T>(false, null);
    }
}
