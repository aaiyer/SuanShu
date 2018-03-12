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
package com.numericalmethod.suanshu.stats.distribution.univariate;

import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile;
import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile.QuantileType;
import java.util.Arrays;

/**
 * An empirical cumulative probability distribution function
 * is a cumulative probability distribution function that
 * assigns probability <i>1/n</i> at each of the <i>n</i> numbers in a sample.
 * <p/>
 * The R equivalent function is {@code ecdf}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Empirical_distribution_function">Wikipedia: EmpiricalDistribution distribution function</a>
 */
public class EmpiricalDistribution implements ProbabilityDistribution {

    /** a sorted copy of the inputs */
    private final double[] sortedData;
    private final Quantile quantile;

    /**
     * Construct an empirical distribution from a sample.
     *
     * @param data         a sample
     * @param quantileType specify how the quantile function is computed
     */
    public EmpiricalDistribution(double[] data, Quantile.QuantileType quantileType) {
        sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);
        this.quantile = new Quantile(sortedData, quantileType);
    }

    /**
     * Construct an empirical distribution from a sample using the default
     * quantile type {@link QuantileType#APPROXIMATELY_MEDIAN_UNBIASED}.
     *
     * @param data a sample
     */
    public EmpiricalDistribution(double[] data) {
        this(data, Quantile.QuantileType.APPROXIMATELY_MEDIAN_UNBIASED);
    }

    /**
     * Get the number of samples in the empirical distribution.
     *
     * @return the number of samples
     */
    public int nSamples() {
        return sortedData.length;
    }

    /**
     * Get the sorted sample.
     *
     * @return the sorted sample
     */
    public double[] toArray() {
        double[] copy = Arrays.copyOf(sortedData, sortedData.length);
        return copy;
    }

    @Override
    public double mean() {
        Mean mean = new Mean(sortedData);
        return mean.value();
    }

    @Override
    public double median() {
        return quantile.value(0.5);
    }

    @Override
    public double variance() {
        Variance var = new Variance(sortedData, true);
        return var.value();
    }

    @Override
    public double skew() {
        Skewness skew = new Skewness(sortedData);
        return skew.value();
    }

    @Override
    public double kurtosis() {
        Kurtosis kurtosis = new Kurtosis(sortedData);
        return kurtosis.value();
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double entropy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double cdf(double x) {
        int count = 0;
        for (int i = 0; i < sortedData.length; ++i) {
            if (x < sortedData[i]) {//F(x) = Pr(X ≤ x), hence including x
                break;
            }
            ++count;
        }

        return (double) count / sortedData.length;
    }

    /**
     * {@inheritDoc }
     * <p/>
     * For an empirical distribution, this implementation assumes the following.
     * <blockquote><pre><i>
     * F<sup>-1</sup>(0) = the minimum x value
     * F<sup>-1</sup>(1) = the maximum x value
     * </i></pre></blockquote>
     */
    @Override
    public double quantile(double u) {
        return quantile.value(u);
    }

    /**
     * This is the probability mass function for the discrete sample.
     *
     * @param x an observation
     * @return {@code pmf(x)}
     */
    @Override
    public double density(double x) {
        int location = Arrays.binarySearch(sortedData, 0, sortedData.length, x);

        int count = 0;
        if (location >= 0) {
            for (int i = location; i < sortedData.length; ++i) {
                if (sortedData[i] != x) {
                    break;
                }
                count++;
            }

            for (int i = location - 1; i > 0; --i) {
                if (sortedData[i] != x) {
                    break;
                }
                count++;
            }
        }

        return (double) count / sortedData.length;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double moment(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
