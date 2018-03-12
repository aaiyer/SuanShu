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
package com.numericalmethod.suanshu.stats.descriptive.rank;

import com.numericalmethod.suanshu.stats.descriptive.Statistic;

/**
 * The minimum of a sample is the smallest value in the sample.
 * <p/>
 * The R equivalent function is {@code min}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Minimum">Wikipedia: Minimum</a>
 */
public class Min implements Statistic {

    private long N = 0;
    private double min = Double.POSITIVE_INFINITY;

    /**
     * Construct an empty {@code Min} calculator.
     */
    public Min() {
    }

    /**
     * Construct a {@code Min} calculator,
     * initialized with a sample.
     *
     * @param data a sample
     */
    public Min(double[] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Min} calculator
     */
    public Min(Min that) {
        this.N = that.N;
        this.min = that.min;
    }

    @Override
    public void addData(double... data) {
        for (double d : data) {
            if (d < min) {
                min = d;
            }
        }

        N += data.length;
    }

    @Override
    public double value() {
        return N > 0 ? min : Double.NaN;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("min: %f; N: %d",
                             min,
                             N);
    }
}
