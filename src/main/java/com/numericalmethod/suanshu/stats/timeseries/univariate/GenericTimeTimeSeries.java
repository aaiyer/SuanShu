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

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries.Entry;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This is a univariate time series indexed by some notion of time.
 * This implementation explicitly remembers all data in memory. It is appropriate for a short time series.
 *
 * @param <T> the timestamp type
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Time_series">Wikipedia: Time series</a>
 */
public class GenericTimeTimeSeries<T extends Comparable<? super T>> implements TimeSeries<T, TimeSeries.Entry<T>> {

    private final ArrayList<TimeSeries.Entry<T>> ts = new ArrayList<TimeSeries.Entry<T>>();

    /**
     * Construct a univariate time series from timestamps and values.
     *
     * @param timestamps the timestamps
     * @param values     the values
     */
    public GenericTimeTimeSeries(T[] timestamps, double[] values) {
        SuanShuUtils.assertArgument(timestamps.length == values.length,
                                    "number of values and numbers of timestamps do not match");

        for (int i = 0; i < timestamps.length; ++i) {
            ts.add(new TimeSeries.Entry<T>(timestamps[i], values[i]));
        }
    }

    @Override
    public int size() {
        return ts.size();
    }

    @Override
    public Iterator<Entry<T>> iterator() {
        return new Iterator<Entry<T>>() {

            private final Iterator<TimeSeries.Entry<T>> it = ts.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<T> next() {
                TimeSeries.Entry<T> pair = it.next();
                return new Entry<T>(pair.getTime(), pair.getValue());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }

    @Override
    public double[] toArray() {
        double[] arr = new double[ts.size()];

        int i = 0;
        for (TimeSeries.Entry<T> pair : ts) {
            arr[i++] = pair.getValue();
        }

        return arr;
    }

    /**
     * Get the <i>i</i>-th value.
     *
     * @param i the position index
     * @return the <i>i</i>-th value
     */
    public double get(int i) {
        return ts.get(i - 1).getValue();
    }

    /**
     * Get the value at time {@code t}.
     *
     * @param t a timestamp
     * @return the value at time {@code t}
     */
    public double get(T t) {// TODO: improve efficiency
        for (TimeSeries.Entry<T> entry : ts) {
            if (entry.getTime().compareTo(t) == 0) {
                return entry.getValue().doubleValue();
            }
        }

        throw new RuntimeException(String.format("no value found at time %s", t.toString()));
    }

    /**
     * Get the <i>i</i>-th time.
     *
     * @param index the position index
     * @return the <i>i</i>-th timestamp
     */
    public T time(int index) {
        return ts.get(index - 1).getTime();
    }

    /**
     * Get all the timestamps.
     *
     * @return the timestamps
     */
    public T[] timestamps() {
        int size = ts.size();
        @SuppressWarnings("unchecked")
        T[] times = (T[]) Array.newInstance(ts.get(0).getTime().getClass(), size);

        for (int i = 0; i < size; ++i) {
            times[i] = ts.get(i).getTime();
        }

        return times;
    }

    /**
     * Construct an instance of {@code GenericTimeTimeSeries} by dropping the leading {@code nItems} entries.
     *
     * @param nItems the number of leading entries to be dropped
     * @return a {@code GenericTimeTimeSeries}
     */
    public GenericTimeTimeSeries<T> drop(int nItems) {
        T[] timestamps = this.timestamps();
        double[] values = this.toArray();

        T[] times = Arrays.copyOfRange(timestamps, nItems, this.size());
        double[] data = Arrays.copyOfRange(values, nItems, this.size());

        return new GenericTimeTimeSeries<T>(times, data);
    }

    /**
     * Construct an instance of {@code GenericTimeTimeSeries} by taking the first difference {@code d} times.
     *
     * @param d the number of differences
     * @return {@code diff(x, lag = 1, differences = d)} as in R
     */
    public GenericTimeTimeSeries<T> diff(int d) {//TODO: improve efficiency
        T[] timestamps = this.timestamps();
        double[] values = this.toArray();

        T[] times = Arrays.copyOfRange(timestamps, d, timestamps.length);
        double[] diff = R.diff(values, 1, d);

        return new GenericTimeTimeSeries<T>(times, diff);
    }

    @Override
    public String toString() {
        final int size = size();
        StringBuilder result = new StringBuilder(size);

        result.append("[");

        for (int i = 0; i < size; ++i) {
            TimeSeries.Entry<T> pair = ts.get(i);
            result.append(String.format("%s: %f; ",
                                        pair.getTime().toString(),
                                        pair.getValue()));
        }

        result.append("]");

        return result.toString();
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
        final GenericTimeTimeSeries<T> other = (GenericTimeTimeSeries<T>) obj;
        if (this.ts != other.ts && (this.ts == null || !this.ts.equals(other.ts))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.ts != null ? this.ts.hashCode() : 0);
        return hash;
    }
}
