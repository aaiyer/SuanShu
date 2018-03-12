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
 * The maximum of a sample is the biggest value in the sample.
 * <p/>
 * The R equivalent function is {@code max}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Maximum">Wikipedia: Maximum</a>
 */
public class Max implements Statistic {

    private long N = 0;
    private double max = Double.NEGATIVE_INFINITY;

    /**
     * Construct an empty {@code Max} calculator.
     */
    public Max() {
    }

    /**
     * Construct a {@code Max} calculator,
     * initialized with a sample.
     *
     * @param data a sample
     */
    public Max(double[] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Max} calculator
     */
    public Max(Max that) {
        this.N = that.N;
        this.max = that.max;
    }

    @Override
    public void addData(double... data) {
        for (double d : data) {
            if (d > max) {
                max = d;
            }
        }

        N += data.length;
    }

    @Override
    public double value() {
        return N > 0 ? max : Double.NaN;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("max: %f; N: %d",
                             max,
                             N);
    }
}
