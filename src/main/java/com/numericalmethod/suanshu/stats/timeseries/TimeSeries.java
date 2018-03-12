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
package com.numericalmethod.suanshu.stats.timeseries;

/**
 * A time series is a serially indexed collection of items.
 * Time series data have a natural temporal ordering.
 * This makes time series analysis distinct from other common data analysis problems that have no natural ordering of the observations.
 * A time series model will generally reflect the fact that observations close together in time will be more closely related than observations further apart.
 * In addition, a time series model will often make use of the natural one-way ordering of time so that
 * values for a given period will be expressed as deriving in some way from past values, rather than from future values.
 * <p/>
 * This implementation represents each datum and its timestamp as a pair of value and index often called time.
 * The entries (pairs) are sorted in ascending order by the timestamps, c.f., {@link TimeSeries.Entry}.
 * A {@code TimeSeries} is immutable.
 *
 * @param <T> the timestamp type
 * @param <V> the value type
 * @param <E> TimeSeries.Entry
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Time_series">Wikipedia: Time series</a>
 */
public interface TimeSeries<T extends Comparable, V, E extends TimeSeries.Entry<T, V>> extends Iterable<E> {//TOOD: make T a comparable object?

    /**
     * A time series is composed of a sequence of {@code Entry}s.
     * <p/>
     * An {@code TimeSeries.Entry} is immutable.
     *
     * @param <T> the time type
     * @param <V> the value type
     */
    public static interface Entry<T, V> {

        /**
         * Get the timestamp.
         *
         * @return the timestamp
         */
        public T getTime();

        /**
         * Get the entry value.
         *
         * @return the entry value
         */
        public V getValue();
    }

    /**
     * Get the length of the time series.
     *
     * @return the time series length
     */
    public int size();
}
