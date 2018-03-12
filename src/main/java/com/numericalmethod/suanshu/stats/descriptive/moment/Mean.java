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
package com.numericalmethod.suanshu.stats.descriptive.moment;

import com.numericalmethod.suanshu.stats.descriptive.Statistic;

/**
 * The mean of a sample is the sum of all numbers in the sample,
 * divided by the sample size. That is,
 * <blockquote><i>
 * E(X) = Σ (xi) / N
 * </i></blockquote>
 * This implementation supports incremental update of the statistic.
 * <p/>
 * The R equivalent function is {@code mean}.
 *
 * @author Haksun Li
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Arithmetic_mean">Wikipedia: Arithmetic mean</a>
 * <li><a href="http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance">Wikipedia: Algorithms for calculating variance</a>
 * </ul>
 */
public class Mean implements Statistic {

    private long N = 0;
    private double mean = 0;

    /**
     * Construct an empty {@code Mean} calculator.
     */
    public Mean() {
    }

    /**
     * Construct a {@code Mean} calculator,
     * initialized with a sample.
     *
     * @param data a sample
     */
    public Mean(double[] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Mean} calculator
     */
    public Mean(Mean that) {
        this.N = that.N;
        this.mean = that.mean;
    }

    @Override
    public void addData(double... data) {
        double sum = mean * N;//previous sum
        for (double d : data) {
            sum += d;
        }

        N += data.length;
        mean = sum / N;
    }

    @Override
    public double value() {
        return N >= 1 ? mean : Double.NaN;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("mean: %f; N: %d",
                             mean,
                             N);
    }
}
