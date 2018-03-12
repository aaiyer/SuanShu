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
package com.numericalmethod.suanshu.interval;

import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * This is an interval on the real line.
 * <p/>
 * This class is immutable.
 *
 * @author Haksun Li
 */
public class RealInterval extends Interval<Double> {

    /**
     * Construct an interval on the real line.
     *
     * @param begin the beginning of this interval
     * @param end   the end of this interval
     */
    public RealInterval(Double begin, Double end) {
        super(begin, end);
        SuanShuUtils.assertArgument(begin <= end, "invalid bounds");
    }

    /**
     * Get the lower bound of this interval.
     *
     * @return the lower bound of this interval
     */
    public double lower() {
        return begin();
    }

    /**
     * Get the upper bound of this interval.
     *
     * @return the upper bound of this interval
     */
    public double upper() {
        return end();
    }
}
