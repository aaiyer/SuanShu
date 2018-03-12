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

import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class OneDimensionTimeSeriesTest {

    @Test
    public void test_0010() {
        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4}
                });

        OneDimensionTimeSeries<Integer> t1_1 = new OneDimensionTimeSeries<Integer>(t1, 1);
        assertEquals(4, t1_1.size());

        Iterator<com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries.Entry<Integer>> it = t1_1.iterator();
        assertEquals(1, it.next().getValue(), 0);
        assertEquals(2, it.next().getValue(), 0);
        assertEquals(3, it.next().getValue(), 0);
        assertEquals(4, it.next().getValue(), 0);
        assertFalse(it.hasNext());

        OneDimensionTimeSeries<Integer> t1_2 = new OneDimensionTimeSeries<Integer>(t1, 2);
        assertEquals(4, t1_2.size());

        it = t1_2.iterator();
        assertEquals(1.1, it.next().getValue(), 0);
        assertEquals(2.2, it.next().getValue(), 0);
        assertEquals(3.3, it.next().getValue(), 0);
        assertEquals(4.4, it.next().getValue(), 0);
        assertFalse(it.hasNext());
    }
}
