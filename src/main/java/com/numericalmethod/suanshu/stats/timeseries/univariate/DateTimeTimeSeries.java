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

import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 * This is a time series has its {@code double} values indexed by {@code DateTime}.
 *
 * @author Haksun Li
 */
public class DateTimeTimeSeries extends GenericTimeTimeSeries<DateTime> {

    /**
     * Construct a time series from {@code DateTime} and {@code double}.
     *
     * @param timestamps the timestamps
     * @param values     the entry values
     */
    public DateTimeTimeSeries(DateTime[] timestamps, double[] values) {
        super(timestamps, values);
    }

    /**
     * Construct a time series from {@code DateTime} and {@code double}.
     *
     * @param timestamps the timestamps
     * @param values     the entry values
     */
    public DateTimeTimeSeries(ArrayList<DateTime> timestamps, ArrayList<Double> values) {
        super(timestamps.toArray(new DateTime[0]),
              DoubleUtils.collection2DoubleArray(values));
    }
}
