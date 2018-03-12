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
 * The variance of a sample is the average squared deviations from the sample mean.
 * It measures the amount of variation the sample values have. That is,
 * <blockquote><i>
 * Var(X, Y) = E[(X - E(X))<sup>2</sup>]
 * </i></blockquote>
 * This implementation uses Chan's update formula to incrementally compute the new statistic.
 * <p/>
 * The R equivalent function is {@code var}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Variance">Wikipedia: Variance</a>
 * <li><a href="http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance">Wikipedia: Algorithms for calculating variance</a>
 * <li>"Tony F. Chan, Gene H. Golub, Randall J. LeVeque, "Updating Formulae and a Pairwise Algorithm for Computing Sample Variances," Technical Report STAN-CS-79-773, Department of Computer Science, Stanford University, 1979."
 * </ul>
 */
public class Variance implements Statistic {

    /** indicate whether the variance calculation is unbiased or not; {@code true} if unbiased */
    private final boolean unbiased;
    private long N = 0;
    private double m2 = 0;//the sum of squares of differences from the mean
    private Mean mean = new Mean();

    /**
     * Construct an empty {@code Variance} calculator.
     */
    public Variance() {
        this.unbiased = true;
    }

    /**
     * Construct a {@code Variance} calculator,
     * initialized with a sample.
     *
     * @param data     a sample
     * @param unbiased {@code true} if the variance calculation uses the unbiased formula
     */
    public Variance(double[] data, boolean unbiased) {
        this.unbiased = unbiased;
        addData(data);
    }

    /**
     * Construct an unbiased {@code Variance} calculator.
     *
     * @param data a sample
     */
    public Variance(double[] data) {
        this.unbiased = true;
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Variance} calculator
     */
    public Variance(Variance that) {
        this.unbiased = that.unbiased;
        this.N = that.N;
        this.m2 = that.m2;
        this.mean = new Mean(that.mean);
    }

    /**
     * Get the standard deviation of the sample,
     * which is the square root of the variance.
     *
     * @return the standard deviation
     */
    public double standardDeviation() {
        return Math.sqrt(value());
    }

    @Override
    public void addData(double... data) {
        //base case
        if (N == 0) {
            N = data.length;
            mean.addData(data);
            m2 = Moments.sumsOfPowersOfDifferences(2, mean.value(), data);//sum of squares
            return;
        }

        /*
         * Chan's update formula.
         * <pre>
         * Chan, Tony F.; Golub, Gene H.; LeVeque, Randall J. (1979),
         * "Updating Formulae and a Pairwise Algorithm for Computing Sample Variances.",
         * Technical Report STAN-CS-79-773,
         * Department of Computer Science, Stanford University.
         * </pre>
         */
        Variance that = new Variance(data, unbiased);
        double delta = that.mean.value() - this.mean.value();//mean difference
        long Nboth = this.N + that.N;
        m2 += that.m2 + delta * delta * ((double) this.N / Nboth) * that.N;

        //update states
        mean.addData(data);
        N = Nboth;
    }

    @Override
    public double value() {
        return N >= 2 ? m2 / (unbiased ? N - 1 : N) : 0;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("var: %f, stdev: %f, N: %d",
                             value(),
                             standardDeviation(),
                             N);
    }
}
