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

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A multivariate realization is a multivariate time series indexed by real numbers, e.g., real time.
 * The entries are the pairs: <i>{(t, vector)}</i>.
 * This time series is often seen in the studies of stochastic process, where the set of times is a continuum.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Stochastic_process">Wikipedia: Stochastic process</a>
 */
public interface MultiVariateRealization extends com.numericalmethod.suanshu.stats.timeseries.multivariate.MultiVariateTimeSeries<Double, MultiVariateRealization.Entry> {

    /**
     * This is the {@code TimeSeries.Entry} for a real number -indexed multivariate time series.
     */
    public static class Entry extends com.numericalmethod.suanshu.stats.timeseries.multivariate.MultiVariateTimeSeries.Entry<Double> {

        /**
         * Construct an instance of {@code TimeSeries.Entry}.
         *
         * @param time  the timestamp
         * @param value the entry value
         */
        public Entry(double time, Vector value) {
            super(time, value);
        }
    }

}
