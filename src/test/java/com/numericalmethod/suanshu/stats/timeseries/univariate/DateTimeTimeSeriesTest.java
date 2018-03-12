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

import com.numericalmethod.suanshu.time.JodaTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DateTimeTimeSeriesTest {

    @Test
    public void test_0010() {
        DateTime[] times = new DateTime[]{
            JodaTimeUtils.getDate(2011, 11, 27, DateTimeZone.UTC),
            JodaTimeUtils.getDate(2011, 11, 28, DateTimeZone.UTC)
        };
        double[] values = new double[]{27., 28.};

        DateTimeTimeSeries instance = new DateTimeTimeSeries(times, values);
        assertEquals(2, instance.size());
        assertEquals(27., instance.get(1), 0);
        assertEquals(28., instance.get(2), 0);
        assertEquals(27., instance.get(JodaTimeUtils.getDate(2011, 11, 27, DateTimeZone.UTC)), 0);
        assertEquals(28., instance.get(JodaTimeUtils.getDate(2011, 11, 28, DateTimeZone.UTC)), 0);
    }
}
