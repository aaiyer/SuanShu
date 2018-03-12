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
package com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.rbind;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This simple multivariate time series has its vectored values indexed by integers.
 *
 * @author Haksun Li
 */
public class SimpleMultiVariateTimeSeries implements MultiVariateTimeSeries {

    private final ArrayList<Vector> ts = new ArrayList<Vector>();//by rows

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries}.
     *
     * @param values a matrix representation of the time series
     */
    public SimpleMultiVariateTimeSeries(Matrix values) {
        for (int i = 1; i <= values.nRows(); ++i) {
            ts.add(values.getRow(i));
        }
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries}.
     *
     * @param values a double array representation of the time series
     */
    public SimpleMultiVariateTimeSeries(double[]... values) {
        this(new DenseMatrix(values));
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries}.
     *
     * @param values a vector representation of the time series
     */
    public SimpleMultiVariateTimeSeries(Vector... values) {
        this(new DenseMatrix(rbind(values)));
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries} from a univariate time series.
     *
     * @param ts a univariate time series
     */
    public SimpleMultiVariateTimeSeries(TimeSeries ts) {
        this(new DenseMatrix(ts.toArray(), ts.size(), 1));
    }

    @Override
    public Matrix toMatrix() {
        Vector[] rows = new Vector[ts.size()];

        int i = 0;
        for (Vector v : ts) {
            rows[i++] = v;
        }

        return CreateMatrix.rbind(rows);
    }

    @Override
    public int size() {
        return ts.size();
    }

    @Override
    public int dimension() {
        return ts.get(0).size();
    }

    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<Entry>() {

            private final Iterator<Vector> it = ts.iterator();
            private int time = 1;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry next() {
                Vector d = it.next();
                return new MultiVariateTimeSeries.Entry(time++, d);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }

    @Override
    public Vector get(int t) {
        return new ImmutableVector(ts.get(t - 1));
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries} by dropping the leading {@code nItems} entries.
     *
     * @param nItems the number of leading entries to be dropped
     * @return a {@code SimpleMultiVariateTimeSeries}
     */
    public SimpleMultiVariateTimeSeries drop(int nItems) {
        return lag(0, size() - nItems);
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries} by taking the first difference {@code d} times.
     *
     * @param d the number of differences
     * @return {@code diff(x, lag = 1, differences = d)} as in R
     */
    public SimpleMultiVariateTimeSeries diff(int d) {
        return new SimpleMultiVariateTimeSeries(R.diff(MatrixUtils.to2DArray(toMatrix()), 1, d));
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries} by lagging the time series.
     * This operation makes sense only for equi-distant data points.
     *
     * @param nLags  the number of lags
     * @param length the length of the lagged time series
     * @return a lagged time series
     */
    public SimpleMultiVariateTimeSeries lag(int nLags, int length) {
        SuanShuUtils.assertArgument(nLags >= 0, "nLags >= 0");

        int begin = size() - nLags - length + 1;
        SuanShuUtils.assertArgument(begin >= 1, "lagged series length is too long");

        Vector[] lagged = new Vector[length];
        for (int i = begin; i <= size() - nLags; ++i) {
            lagged[i - begin] = ts.get(i - 1);
        }

        return new SimpleMultiVariateTimeSeries(rbind(lagged));
    }

    /**
     * Construct an instance of {@code SimpleMultiVariateTimeSeries} by lagging the time series.
     * This operation makes sense only for equi-distant data points.
     *
     * @param nLags the number of lags
     * @return a lagged time series
     */
    public SimpleMultiVariateTimeSeries lag(int nLags) {
        return lag(nLags, size() - nLags);
    }

    @Override
    public String toString() {
        return ts.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleMultiVariateTimeSeries other = (SimpleMultiVariateTimeSeries) obj;
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
