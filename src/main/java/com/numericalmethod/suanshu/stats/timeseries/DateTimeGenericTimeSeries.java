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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.joda.time.DateTime;

/**
 * This is a generic time series where time is indexed by {@code DateTime} and value can be any type.
 *
 * @param <V> the data type
 * @author Haksun Li
 */
public class DateTimeGenericTimeSeries<V> implements TimeSeries<DateTime, V, DateTimeGenericTimeSeries.Entry<V>> {

    private final Map<DateTime, V> map = new TreeMap<DateTime, V>();

    /**
     * Construct a time series.
     *
     * @param timestamps the timestamps
     * @param values     the data values
     */
    public DateTimeGenericTimeSeries(DateTime[] timestamps, V[] values) {
        SuanShuUtils.assertArgument(timestamps.length == values.length,
                                    "number of values and numbers of timestamps do not match");

        for (int i = 0; i < timestamps.length; ++i) {
            this.map.put(timestamps[i], values[i]);
        }
    }

    /**
     * This is the {@code TimeSeries.Entry} for a {@code DateTime} -indexed time series.
     *
     * @param <V> the data type
     */
    public static class Entry<V> implements TimeSeries.Entry<DateTime, V> {

        private final DateTime time;
        private final V value;

        private Entry(DateTime time, V value) {
            this.time = time;
            this.value = value;
        }

        @Override
        public DateTime getTime() {
            return time;
        }

        @Override
        public V getValue() {
            return value;
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
            final Entry<V> other = (Entry<V>) obj;
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
            hash = 67 * hash + (this.time != null ? this.time.hashCode() : 0);
            hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Iterator<Entry<V>> iterator() {
        return new Iterator<Entry<V>>() {

            private final Iterator<Map.Entry<DateTime, V>> it = map.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<V> next() {
                Map.Entry<DateTime, V> pair = it.next();
                return new Entry<V>(pair.getKey(), pair.getValue());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }
}
