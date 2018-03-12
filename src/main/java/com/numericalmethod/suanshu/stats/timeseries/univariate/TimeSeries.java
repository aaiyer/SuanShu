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
package com.numericalmethod.suanshu.stats.timeseries.univariate;

/**
 * This is a univariate time series indexed by some notion of time.
 * The entries are the pair: <i>{(timestamp, double)}</i>.
 *
 * @param <T> the timestamp type
 * @param <E> TimeSeries.Entry
 * @author Haksun Li
 */
public interface TimeSeries<T extends Comparable, E extends TimeSeries.Entry<T>> extends com.numericalmethod.suanshu.stats.timeseries.TimeSeries<T, Double, E> {

    /**
     * This is the {@code TimeSeries.Entry} for a univariate time series.
     *
     * @param <T> the timestamp type
     */
    public static class Entry<T> implements com.numericalmethod.suanshu.stats.timeseries.TimeSeries.Entry<T, Double> {

        private final T time;
        private final double value;

        /**
         * Construct an instance of {@code Entry}.
         *
         * @param time  the timestamp
         * @param value the entry value
         */
        public Entry(T time, double value) {
            this.time = time;
            this.value = value;
        }

        @Override
        public Double getValue() {
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
            if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + (this.time != null ? this.time.hashCode() : 0);
            hash = 17 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
            return hash;
        }
    }

    /**
     * Convert this time series into an array, discarding the timestamps.
     *
     * @return the array representation of the time series
     */
    public abstract double[] toArray();
}
