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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.MultiVariateTimeSeries.Entry;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This is a multivariate time series indexed by some notion of time.
 * This implementation explicitly remembers all data in memory. It is appropriate for a short time series.
 *
 * @param <T> the timestamp type
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Time_series">Wikipedia: Time series</a>
 */
public class GenericTimeTimeSeries<T extends Comparable<? super T>> implements MultiVariateTimeSeries<T, MultiVariateTimeSeries.Entry<T>> {

    private final ArrayList<MultiVariateTimeSeries.Entry<T>> ts = new ArrayList<MultiVariateTimeSeries.Entry<T>>();

    /**
     * Construct a multivariate time series from timestamps and vectors.
     *
     * @param timestamps the timestamps
     * @param values     the vector values
     */
    public GenericTimeTimeSeries(T[] timestamps, Vector[] values) {
        SuanShuUtils.assertArgument(timestamps.length == values.length,
                                    "number of values and numbers of timestamps do not match");

        for (int i = 0; i < timestamps.length; ++i) {
            ts.add(new MultiVariateTimeSeries.Entry<T>(timestamps[i], values[i]));
        }
    }

    /**
     * Construct a multivariate time series from timestamps and vectors.
     *
     * @param timestamps the timestamps
     * @param values     the vector values
     */
    public GenericTimeTimeSeries(T[] timestamps, double[][] values) {
        SuanShuUtils.assertArgument(timestamps.length == values.length,
                                    "number of values and numbers of timestamps do not match");

        for (int i = 0; i < timestamps.length; ++i) {
            ts.add(new MultiVariateTimeSeries.Entry<T>(timestamps[i], new DenseVector(values[i])));
        }
    }

    /**
     * Construct a multivariate time series from timestamps and vectors.
     *
     * @param timestamps the timestamps
     * @param values     the vector values
     */
    public GenericTimeTimeSeries(T[] timestamps, Matrix values) {
        final int nRows = values.nRows();
        SuanShuUtils.assertArgument(timestamps.length == nRows,
                                    "number of values and numbers of timestamps do not match");

        for (int i = 0; i < timestamps.length; ++i) {
            ts.add(new MultiVariateTimeSeries.Entry<T>(timestamps[i], values.getRow(i + 1)));
        }
    }

    @Override
    public int dimension() {
        return ts.get(0).getValue().size();
    }

    @Override
    public int size() {
        return ts.size();
    }

    @Override
    public Iterator<Entry<T>> iterator() {
        return new Iterator<Entry<T>>() {

            private final Iterator<MultiVariateTimeSeries.Entry<T>> it = ts.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<T> next() {
                Entry<T> pair = it.next();
                return new Entry<T>(pair.getTime(), pair.getValue());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }

    @Override
    public Matrix toMatrix() {
        Vector[] values = this.toArray();
        DenseMatrix A = CreateMatrix.rbind(values);
        return A;
    }

    /**
     * Convert this multivariate time series into an array of vectors.
     *
     * @return the array representation of this time series
     */
    public Vector[] toArray() {
        int size = ts.size();

        Vector[] result = new Vector[size];
        for (int i = 0; i < size; ++i) {
            result[i] = new ImmutableVector(ts.get(i).getValue());
        }

        return result;
    }

    /**
     * Get the <i>i</i>-th value.
     *
     * @param i the position index
     * @return the <i>i</i>-th value
     */
    public Vector get(int i) {
        return new ImmutableVector(ts.get(i - 1).getValue());
    }

    /**
     * Get the <i>i</i>-th timestamp.
     *
     * @param i the position index
     * @return the <i>i</i>-th timestamp
     */
    public T time(int i) {
        return ts.get(i - 1).getTime();
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
     * Construct an instance of {@code GenericTimeTimeSeries} by dropping the leading {@code nItems} entries,
     * those most backward in time entries.
     *
     * @param nItems the number of leading entries to be dropped
     * @return an instance of {@code GenericTimeTimeSeries}
     */
    public GenericTimeTimeSeries<T> drop(int nItems) {
        T[] timestamps = this.timestamps();
        Vector[] values = this.toArray();

        Vector[] vectors = Arrays.copyOfRange(values, nItems, values.length);
        T[] times = Arrays.copyOfRange(timestamps, nItems, timestamps.length);

        return new GenericTimeTimeSeries<T>(times, vectors);
    }

    /**
     * Construct an instance of {@code GenericTimeTimeSeries} by taking the first difference {@code d} times.
     *
     * @param d the number of differences
     * @return {@code diff(x, lag = 1, differences = d)} as in R
     */
    public GenericTimeTimeSeries<T> diff(int d) {//TODO: improve efficiency
        T[] timestamps = this.timestamps();
        Vector[] values = this.toArray();

        DenseMatrix A1 = CreateMatrix.rbind(values);
        double[][] A2 = MatrixUtils.to2DArray(A1);

        double[][] diff = R.diff(A2, 1, d);
        T[] times = Arrays.copyOfRange(timestamps, d, timestamps.length);

        return new GenericTimeTimeSeries<T>(times, diff);
    }

    @Override
    public String toString() {
        final int size = size();
        StringBuilder result = new StringBuilder(size);

        result.append("{");

        for (int i = 0; i < size; ++i) {
            result.append(String.format("%s: %s;\n",
                                        time(i + 1),
                                        get(i + 1)));
        }

        result.setCharAt(result.length() - 2, '}');

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
        int hash = 3;
        hash = 13 * hash + (this.ts != null ? this.ts.hashCode() : 0);
        return hash;
    }
}
