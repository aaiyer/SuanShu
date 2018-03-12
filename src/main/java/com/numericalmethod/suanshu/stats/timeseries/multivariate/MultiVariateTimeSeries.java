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
package com.numericalmethod.suanshu.stats.timeseries.multivariate;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A multivariate time series is a sequence of vectors indexed by some notion of time.
 * The entries are the pair: <i>{(timestamp, vector)}</i>.
 *
 * @param <T> the timestamp type
 * @param <E> TimeSeries.Entry
 * @author Haksun Li
 */
public interface MultiVariateTimeSeries<T extends Comparable, E extends MultiVariateTimeSeries.Entry<T>> extends com.numericalmethod.suanshu.stats.timeseries.TimeSeries<T, Vector, E> {

    /**
     * This is the {@code TimeSeries.Entry} for a multivariate time series.
     *
     * @param <T> the timestamp type
     */
    public static class Entry<T> implements com.numericalmethod.suanshu.stats.timeseries.TimeSeries.Entry<T, Vector> {

        private final T time;
        private final ImmutableVector value;

        /**
         * Construct an instance of {@code TimeSeries.Entry}.
         *
         * @param time  the timestamp
         * @param value the entry value
         */
        public Entry(T time, Vector value) {
            this.time = time;
            this.value = new ImmutableVector(value);
        }

        @Override
        public Vector getValue() {
            return value;
        }

        @Override
        public T getTime() {
            return time;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final Entry<T> other = (Entry<T>) obj;
            if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
                return false;
            }
            if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.time != null ? this.time.hashCode() : 0);
            hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Convert this multivariate time series into an <i>m x n</i> matrix, where
     * <i>m</i> is the dimension, and <i>n</i> the length.
     *
     * @return the matrix representation of this time series
     */
    public abstract Matrix toMatrix();

    /**
     * Get the dimension of the multivariate time series.
     *
     * @return the multivariate time series dimension
     */
    public abstract int dimension();
}
