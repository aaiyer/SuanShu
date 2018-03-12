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
package com.numericalmethod.suanshu.time;

import com.numericalmethod.suanshu.interval.Interval;
import com.numericalmethod.suanshu.interval.Intervals;
import org.joda.time.DateTime;

/**
 * This is a collection of time intervals {@link TimeInterval}.
 *
 * @author Haksun Li
 * @see TimeInterval
 */
public class TimeIntervals extends Intervals<DateTime> {

    /**
     * Construct an empty collection of time interval.
     */
    public TimeIntervals() {
    }

    /**
     * Construct a collection consisting of one time interval.
     *
     * @param begin the beginning time
     * @param end   the ending time
     */
    public TimeIntervals(DateTime begin, DateTime end) {
        super(begin, end);
    }

    /**
     * Construct a collection consisting of one time interval.
     *
     * @param interval a time interval
     */
    public TimeIntervals(Interval<DateTime> interval) {
        super(interval);
    }

    /**
     * Construct a collection of time intervals.
     *
     * @param intervals time intervals
     */
    public TimeIntervals(Interval<DateTime>... intervals) {
        super(intervals);
    }

    /**
     * Copy constructor.
     *
     * @param that a collection of time intervals
     */
    public TimeIntervals(Intervals<DateTime> that) {
        super(that);
    }
}
