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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;

/**
 * Covariance is a measure of how much two variables change together.
 * This implementation uses the Pearson method. That is,
 * <blockquote><i>
 * Cov(X, Y) = E[(X - E(X)) * (Y - E(Y))]
 * </i></blockquote>
 * Note that this implementation uses <i>N - 1</i> as the denominator to give an unbiased estimator of the covariance for i.i.d. observations.
 * This implementation uses Pébay's update formula to incrementally compute the new statistic.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Covariance">Wikipedia: Covariance</a>
 * <li><a href="http://en.wikipedia.org/wiki/Correlation">Wikipedia: Correlation</a>
 * </ul>
 */
public class Covariance implements Statistic {

    private long N = 0;
    private Mean mean1 = new Mean();
    private Variance var1 = new Variance();
    private Mean mean2 = new Mean();
    private Variance var2 = new Variance();
    private double sum = 0;

    /**
     * Construct an empty {@code Covariance} calculator.
     */
    public Covariance() {
    }

    /**
     * Construct a {@code Covariance} calculator, initialized with two samples.
     * The size of the two samples must be equal.
     *
     * @param data the two samples
     */
    public Covariance(double[][] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Covariance} instance
     */
    public Covariance(Covariance that) {
        this.N = that.N;
        this.mean1 = that.mean1;
        this.var1 = that.var1;
        this.mean2 = that.mean2;
        this.var2 = that.var2;
        this.sum = that.sum;
    }

    /**
     * Get the correlation.
     *
     * @return the correlation
     */
    public double correlation() {
        return value() / var1.standardDeviation() / var2.standardDeviation();
    }

    /**
     * Update the covariance statistic with more data.
     * Since this signature takes only a single array {@code double[]},
     * we concatenate the two arrays into one.
     * <p/>
     * For example, suppose we want to do
     * <blockquote><code>
     *        addData(new double[][]{
     *              {1, 2, 3},
     *              {4, 5, 6}
     *          });
     * </code></blockquote>
     * We can also write
     * <blockquote><code>
     *        addData(new double[]{
     *              {1, 2, 3, 4, 5, 6}
     *          });
     * </code></blockquote>
     * In the latter case, there must be an even number of data points.
     *
     * @param data a data array concatenating two samples
     */
    @Override
    public void addData(double... data) {
        SuanShuUtils.assertArgument(data.length % 2 == 0, "there must be an even number of data points");

        double[][] dataArr = new double[2][data.length / 2];
        int i = 0;
        for (int j = 0; i < data.length / 2; ++j, ++i) {
            dataArr[0][j] = data[i];
        }

        for (int j = 0; i < data.length; ++j, ++i) {
            dataArr[1][j] = data[i];
        }

        addData(dataArr);
    }

    /**
     * Update the covariance statistic with more data.
     *
     * @param data two new samples
     * @see "Pébay, Philippe, "Formulas for Robust, One-Pass Parallel Computation of Covariances and Arbitrary-Order Statistical Moments," Technical Report SAND2008-6212, Sandia National Laboratories, 2008."
     */
    public void addData(double[][] data) {
        SuanShuUtils.assertArgument(data.length == 2, "there must be a pair of data");
        SuanShuUtils.assertArgument(data[0].length == data[1].length, "there must be a pair of data of the same length");

        //base case
        if (N == 0) {
            N = data[0].length;
            mean1.addData(data[0]);
            var1.addData(data[0]);
            mean2.addData(data[1]);
            var2.addData(data[1]);

            double mu1 = mean1.value();
            double mu2 = mean2.value();
            sum = 0;
            for (int i = 0; i < N; ++i) {
                sum += (data[0][i] - mu1) * (data[1][i] - mu2);
            }

            return;
        }

        //Pébay's update formula.
        Covariance that = new Covariance(data);
        long Nboth = this.N + that.N;
        double dMean1 = that.mean1.value() - this.mean1.value();
        double dMean2 = that.mean2.value() - this.mean2.value();
        double correction = ((double) this.N / Nboth) * that.N;
        correction *= dMean1 * dMean2;
        this.sum += that.sum + correction;

        mean1.addData(data[0]);
        mean2.addData(data[1]);
        this.N = Nboth;
    }

    @Override
    public double value() {
        return N > 1 ? sum / (double) (N - 1) : Double.NaN;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("covariance: %f; correlation: %f, N: %d",
                             value(),
                             correlation(),
                             N);
    }
}
