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

import com.numericalmethod.suanshu.stats.timeseries.multivariate.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries.Entry;
import java.util.Iterator;

/**
 * This class constructs a univariate realization from a multivariate realization by taking one of its dimension (coordinate).
 *
 * @param <T> the timestamp type
 * @author Haksun Li
 */
public class OneDimensionTimeSeries<T extends Comparable> implements TimeSeries<T, TimeSeries.Entry<T>> {

    private final MultiVariateTimeSeries<T, ? extends MultiVariateTimeSeries.Entry<T>> mts;
    private final int dim;

    /**
     * Construct a univariate realization from a multivariate realization by taking one of its dimension (coordinate).
     *
     * @param mts a multivariate realization
     * @param dim the dimension/coordinate to extract
     */
    public OneDimensionTimeSeries(MultiVariateTimeSeries<T, ? extends MultiVariateTimeSeries.Entry<T>> mts, int dim) {
        this.mts = mts;
        this.dim = dim;
    }

    @Override
    public int size() {
        return mts.size();
    }

    @Override
    public Iterator<Entry<T>> iterator() {

        final Iterator<? extends MultiVariateTimeSeries.Entry<T>> it = mts.iterator();

        return new Iterator<TimeSeries.Entry<T>>() {

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<T> next() {
                MultiVariateTimeSeries.Entry<T> entry = it.next();
                return new TimeSeries.Entry<T>(entry.getTime(), entry.getValue().get(dim));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("time series is immutable");
            }
        };
    }

    @Override
    public double[] toArray() {
        int size = size();
        double[] result = new double[size];

        Iterator<Entry<T>> it = iterator();
        for (int i = 0; i < size; ++i) {
            result[i] = it.next().getValue();
        }

        return result;
    }
}
