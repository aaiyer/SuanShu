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
package com.numericalmethod.suanshu.stats.descriptive;

/**
 * A statistic (singular) is a single measure of some attribute of a sample (e.g., its arithmetic mean value).
 * It is calculated by applying a function (statistical algorithm) to a sample, i.e., a set of data.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Statistic">Wikipedia: Statistic</a>
 */
public interface Statistic {

    /**
     * Recompute the statistic with more data, incrementally if possible.
     *
     * @param data an array of new items
     */
    public void addData(double... data);

    /**
     * Get the value of the statistic.
     *
     * @return the statistic
     */
    public double value();

    /**
     * Get the size of the sample.
     *
     * @return the sample size
     */
    public long N();
}
