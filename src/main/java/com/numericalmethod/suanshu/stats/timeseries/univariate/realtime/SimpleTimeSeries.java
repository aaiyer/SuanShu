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
package com.numericalmethod.suanshu.stats.timeseries.univariate.realtime;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This simple univariate time series has its {@code double} values indexed by integers.
 *
 * @author Haksun Li
 */
public class SimpleTimeSeries implements TimeSeries {

    private final ArrayList<Double> ts = new ArrayList<Double>();

    /**
     * Construct an instance of {@code SimpleTimeSeries}.
     *
     * @param values an array of values
     */
    public SimpleTimeSeries(double[] values) {
        for (double d : values) {
            ts.add(d);
        }
    }

    @Override
    public int size() {
        return ts.size();
    }

    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<Entry>() {

            private final Iterator<Double> it = ts.iterator();
            private int time = 1;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry next() {
                Double d = it.next();
                return new TimeSeries.Entry(time++, d);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }

    @Override
    public double[] toArray() {
        return DoubleUtils.collection2DoubleArray(ts);
    }

    @Override
    public double get(int t) {
        return ts.get(t - 1);
    }

    /**
     * Construct an instance of {@code SimpleTimeSeries} by dropping the leading {@code nItems} entries.
     *
     * @param nItems the number of leading entries to be dropped
     * @return a {@code SimpleTimeSeries}
     */
    public SimpleTimeSeries drop(int nItems) {
        return lag(0, size() - nItems);
    }

    /**
     * Construct an instance of {@code SimpleTimeSeries} by taking the first difference {@code d} times.
     *
     * @param d the number of differences
     * @return {@code diff(x, lag = 1, differences = d)} as in R
     */
    public SimpleTimeSeries diff(int d) {
        double[] values = toArray();
        return new SimpleTimeSeries(R.diff(values, 1, d));
    }

    /**
     * /**
     * Construct an instance of {@code SimpleTimeSeries} by lagging the time series.
     * This operation makes sense only for equi-distant data points.
     *
     * @param nLags  the number of lags
     * @param length the length of the lagged time series
     * @return a lagged time series
     */
    public SimpleTimeSeries lag(int nLags, int length) {
        SuanShuUtils.assertArgument(nLags >= 0, "nLags >= 0");

        int begin = size() - nLags - length + 1;
        SuanShuUtils.assertArgument(begin >= 1, "lagged series length is too long");

        double[] lagged = new double[length];
        for (int i = begin; i <= size() - nLags; ++i) {
            lagged[i - begin] = ts.get(i - 1);
        }

        return new SimpleTimeSeries(lagged);
    }

    /**
     * Construct an instance of {@code SimpleTimeSeries} by lagging the time series.
     * This operation makes sense only for equi-distant data points.
     *
     * @param nLags the number of lags
     * @return a lagged time series
     */
    public SimpleTimeSeries lag(int nLags) {
        return lag(nLags, size() - nLags);
    }

    @Override
    public String toString() {
        final int lineBreak = 20;

        StringBuilder result = new StringBuilder();
        result.append(String.format("length = %d\n", ts.size()));

        for (int i = 1; i <= ts.size(); ++i) {
            result.append(String.format("[%d,] ", i));
            result.append(String.format("%f, ", ts.get(i - 1)));

            if (i % lineBreak == 0) {
                result.append("\n");
            }
        }

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
        final SimpleTimeSeries other = (SimpleTimeSeries) obj;
        if (this.ts != other.ts && (this.ts == null || !this.ts.equals(other.ts))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.ts != null ? this.ts.hashCode() : 0);
        return hash;
    }
}
