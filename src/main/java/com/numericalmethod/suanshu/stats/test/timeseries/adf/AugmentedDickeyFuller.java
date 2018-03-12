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
package com.numericalmethod.suanshu.stats.test.timeseries.adf;

import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile.QuantileType;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.cbind;
import static com.numericalmethod.suanshu.stats.timeseries.univariate.UnivariateTimeSeriesUtils.*;
import static java.lang.Math.*;

/**
 * The Augmented Dickey Fuller test tests whether a one-time differencing (d = 1) will make the time series stationary.
 * That is, whether the series has a unit root.
 *
 * <p>
 * Cheung and Lai (1995) pointed out that the lag order does have some effect on the critical values, esp. when the sample size is small.
 *
 * <p>
 * The R equivalent function is {@code adf.test} in package {@code tseries}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"S. E. Said and D. A. Dickey, "Testing for Unit Roots in Autoregressive-Moving Average Models of Unknown Order," <i>Biometrika</i>, vol. 71, no. 3, pp.599–607, 1984."
 * <li>"Yin-Wong Cheung, Kon S. Lai, "ESTIMATING FINITE SAMPLE CRITICAL VALUES FOR UNIT ROOT TESTS USING PURE RANDOM WALK PROCESSES," Journal of Time Series Analysis, vol. 16, issue 5, pp.493-498, 1995."
 * </ul>
 */
public class AugmentedDickeyFuller extends HypothesisTest {

    /**
     * the three versions of augmented Dickey-Fuller (ADF) test
     */
    public static enum TrendType {

        /**
         * test for a unit root without drift or time trend
         */
        NO_CONSTANT(new ADFDistributionSupport_NO_CONSTANT_lag0()),
        /**
         * test for a unit root with drift
         */
        CONSTANT(new ADFDistributionSupport_CONSTANT_lag0()),
        /**
         * test for a unit root with drift and deterministic time trend
         */
        CONSTANT_TIME(new ADFDistributionSupport_CONSTANT_TIME_lag0());
        private final ADFDistributionSupport support;

        TrendType(ADFDistributionSupport support) {
            this.support = support;
        }

        ADFDistribution getDistributionInstance(int sampleSize) {
            return new ADFDistribution(support.getData(sampleSize), QuantileType.INVERSE_OF_EMPIRICAL_CDF) {
            };
        }

        ADFDistributionSupport getSupport() {
            return support;
        }
    }
    /**
     * the trend type
     */
    public final TrendType type;
    /**
     * the lag order
     */
    public final int lagOrder;

    @Override
    public String getNullHypothesis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAlternativeHypothesis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Perform the Augmented Dickey-Fuller test statistics to test for the existence of uniroot.
     *
     * @param xt a time series
     * @param type the trend type
     * @param lagOrder the lags; when {@code lagOrder} == 0, we perform the standard Dickey-Fuller test.
     * @param dist the ADF distribution to use; in general, the correct ADF distribution to use depends on the trend type, as well as lag order;
     * to improve accuracy, the user may generate and use a customized ADF distribution;
     * {@code null} if to use the default
     */
    public AugmentedDickeyFuller(double[] xt, TrendType type, int lagOrder, ADFDistribution dist) {
        this(new SimpleTimeSeries(xt), type, lagOrder, dist);
    }

    /**
     * Perform the Augmented Dickey-Fuller test statistics to test for the existence of uniroot.
     *
     * <p>
     * Lag order is automatically selected as in R.
     *
     * <blockquote><code>
     * nLag = (int) Math.pow((series.length - 1, 1.0 / 3.0));
     * </code></blockquote>
     *
     * which corresponds to the suggested upper bound on the rate.
     *
     * @param xt a time series
     */
    public AugmentedDickeyFuller(double[] xt) {
        this(xt, TrendType.CONSTANT_TIME, (int) pow(xt.length - 1, 1. / 3.), null);
    }

    private AugmentedDickeyFuller(SimpleTimeSeries xt, TrendType type, int lagOrder, ADFDistribution dist) {
        assertArgument(0 <= lagOrder && lagOrder < xt.size(), "0 <= lagOrder && lagOrder < sample size");

        this.type = type;
        this.lagOrder = lagOrder;
        final int n = xt.size();//the number of observations

        SimpleTimeSeries Y;
        Matrix A;
        if (lagOrder == 0) {//the standard Dickey-Fuller test
            //Y_t = a + b_t + β * Y_{t-1} + e_t
            Y = xt.drop(1);
            A = cbind(
                    new DenseMatrix(R.seq(2, n, 1.0), n - 2 + 1, 1),
                    toMatrix(xt.lag(1)));
        } else {//the Augmented Dickey Fuller test
            //Y_t = a + b_t + β * Y_{t-1} + c_1 * d(y_{t-1}) + ... + c_p * d(Y_{t-p}) + e
            int length = n - lagOrder - 2 + 1;

            Y = xt.drop(lagOrder + 1);
            A = cbind(
                    new DenseMatrix(R.seq(lagOrder + 2, n, 1.0), length, 1),
                    toMatrix(xt.lag(1, length)));

            Matrix[] dy_lags = new Matrix[lagOrder + 1];
            for (int i = 1; i <= lagOrder; ++i) {
                dy_lags[i] = toMatrix(xt.lag(i, length + 1).diff(1));
            }

            dy_lags[0] = A;
            A = cbind(dy_lags);
        }

        LMProblem problem = new LMProblem(toVector(Y), A, true);
        OLSRegression instance = new OLSRegression(problem);

        double beta = instance.beta.betaHat.get(2);
        double stderr = instance.beta.stderr.get(2);

        //the D.F. test statistics is (β-1) / s.e(β)
        testStatistics = (beta - 1) / stderr;

        ADFDistribution dist1 = dist;
        if (dist1 == null) {
            dist1 = ADFDistribution.getDistribution(type, lagOrder, xt.size());
        }
        pValue = dist1.cdf(testStatistics);//TODO: why not 1 - cdf???
    }
}
