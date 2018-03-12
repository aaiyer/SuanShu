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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import com.numericalmethod.suanshu.stats.descriptive.Statistic;
import java.util.Arrays;

/**
 * Quantiles are points taken at regular intervals from the cumulative distribution function (CDF) of a random variable.
 * Dividing ordered data into <i>q</i> essentially equal-sized data subsets is the motivation for <i>q</i>-quantiles;
 * the quantiles are the data values marking the boundaries between consecutive subsets.
 * Put another way, the <i>k</i>-th <i>q</i>-quantile for a random variable is the value <i>x</i> such that
 * the probability that the random variable will be less than <i>x</i> is at most <i>k/q</i> and
 * the probability that the random variable will be more than <i>x</i> is at most <i>(q - k)/q</i>.
 * There are <i>q-1</i> <i>q</i>-quantiles, with <i>k</i> an integer satisfying <i>0 &lt; k &lt; q</i>.
 * The smallest observation corresponds to probability 0 and the largest probability 1.
 * This class implements the 9 different quantile definitions in Hyndman 1996.
 * <p/>
 * The R equivalent function is {@code quantile}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"R. J. Hyndman, and Y. Fan, "Sample quantiles in statistical packages," American Statistician, 50, 361–365, 1996."
 * <li><a href="http://en.wikipedia.org/wiki/Quantile">Wikipedia: Quantile</a>
 * </ul>
 */
public class Quantile implements Statistic {

    /**
     * the quantile definitions available
     *
     * @see "R. J. Hyndman, and Y. Fan, "Sample quantiles in statistical packages," American Statistician, 50, 361–365, 1996."
     */
    public static enum QuantileType {

        /**
         * the inverse of empirical distribution function
         */
        INVERSE_OF_EMPIRICAL_CDF,
        /**
         * the inverse of empirical distribution function with averaging at discontinuities
         */
        INVERSE_OF_EMPIRICAL_CDF_WITH_AVERAGING_AT_DISCONTINUITIES,
        /**
         * the nearest even order statistic as in SAS
         */
        NEAREST_EVEN_ORDER_STATISTICS,
        /**
         * the linear interpolation of the empirical cdf
         */
        LINEAR_INTERPOLATION_OF_EMPIRICAL_CDF,
        /**
         * a piecewise linear function where
         * the knots are the values midway through the steps of the empirical cdf
         */
        MIDWAY_THROUGH_STEPS_OF_EMPIRICAL_CDF,
        /**
         * the definition in Minitab and SPSS
         */
        MINITAB_SPSS,
        /**
         * the definition in S
         */
        S,
        /**
         * default: the resulting quantile estimates are approximately median-unbiased
         * regardless of the distribution of the sample
         */
        APPROXIMATELY_MEDIAN_UNBIASED,
        /**
         * the resulting quantile estimates are approximately unbiased
         * for the expected order statistics if the sample is normally distributed
         */
        APPROXIMATELY_UNBIASED_IF_DATA_IS_NORMAL
    };

    /** the quantile definition */
    private final QuantileType type;
    private double[] sortedData;

    /**
     * Construct a {@code Quantile} calculator.
     *
     * @param data the data
     * @param type the algorithm to compute <i>Q(q)</i>
     */
    public Quantile(double[] data, QuantileType type) {
        this.type = type;
        addData(data);
    }

    /**
     * Construct a {@code Quantile} calculator using the default type:
     * {@link QuantileType#APPROXIMATELY_MEDIAN_UNBIASED}.
     *
     * @param data the data
     */
    public Quantile(double[] data) {
        this(data, QuantileType.APPROXIMATELY_MEDIAN_UNBIASED);//this.q is not used
    }

    /**
     * Compute the sample value corresponding to a quantile.
     *
     * @param q a quantile
     * @return the value for the {@code q} quantile
     */
    public double value(double q) {
        SuanShuUtils.assertArgument(q >= 0 && q <= 1, "0 <= q <= 1; input = %f", q);

        int j = 0;
        double m = 0, g = 0, gamma = 0, Q = 0;
        switch (type) {
            case INVERSE_OF_EMPIRICAL_CDF:
                m = 0;
                j = j(q, m);
                g = g(q, m, j);
                gamma = isZero(g, 0) ? 0 : 1;
                break;
            case INVERSE_OF_EMPIRICAL_CDF_WITH_AVERAGING_AT_DISCONTINUITIES:
                m = 0;
                j = j(q, m);
                g = g(q, m, j);
                gamma = isZero(g, 0) ? 0.5 : 1;
                break;
            case NEAREST_EVEN_ORDER_STATISTICS:
                m = -0.5;
                j = j(q, m);
                g = g(q, m, j);
                gamma = isZero(g, 0) ? j % 2 == 0 ? 0 : 1 : 1;//check whether j is even
                break;
            case LINEAR_INTERPOLATION_OF_EMPIRICAL_CDF:
                m = 0;//m(q, 0, 1);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
            case MIDWAY_THROUGH_STEPS_OF_EMPIRICAL_CDF:
                m = 0.5;//m(q, 0.5, 0.5);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
            case MINITAB_SPSS:
                m = q;//m(q, 0, 0);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
            case S:
                m = 1 - q;//m(q, 1, 1);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
            case APPROXIMATELY_UNBIASED_IF_DATA_IS_NORMAL:
                m = m(q, 3 / 8d, 3 / 8d);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
            case APPROXIMATELY_MEDIAN_UNBIASED:
            default:
                m = m(q, 1 / 3d, 1 / 3d);
                j = j(q, m);
                g = g(q, m, j);
                gamma = g;
                break;
        }

        Q = Q(gamma, j);
        return Q;
    }

    private double m(double q, double a, double b) {
        double m = a + q * (1 - a - b);
        return m;
    }

    private int j(double q, double m) {
        int N = sortedData.length;
        int j = (int) Math.floor(q * N + m);//the integral part
        j = j > 0 ? j : 0;
        j = j <= N ? j : N;
        return j;
    }

    private double g(double q, double m, double j) {
        int N = sortedData.length;
        double g = q * N + m - j;//the fractional part
        return g;
    }

    private double Q(double gamma, int j) {
        double stat = (1 - gamma) * sortedData[j > 0 ? j - 1 : 0];
        stat += gamma * sortedData[j < sortedData.length ? j : sortedData.length - 1];
        return stat;
    }

    @Override
    public void addData(double... data) {
        int oldLenght = sortedData == null ? 0 : sortedData.length;
        sortedData = sortedData == null ? new double[data.length]
                     : Arrays.copyOf(sortedData, sortedData.length + data.length);

        for (int i = 0; i < data.length; ++i) {
            sortedData[oldLenght + i] = data[i];
        }

        Arrays.sort(sortedData);
    }

    @Override
    public double value() {
        throw new UnsupportedOperationException("you must supply a quantile value q");
    }

    @Override
    public long N() {
        return sortedData.length;
    }
}
