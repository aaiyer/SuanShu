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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.stats.test.timeseries.adf.AugmentedDickeyFuller.TrendType;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class computes the finite sample distribution of the augmented Dickey-Fuller (ADF) test statistics.
 *
 * <p>
 * There are three main versions of the test and thus three possible asymptotic distributions:
 * <ol>
 * <li>NO_CONSTANT: test for a unit root without drift or time trend;
 * <li>CONSTANT: test for a unit root with drift;
 * <li>CONSTANT_TIME: test for a unit root with drift and deterministic time trend.
 * </ol>
 *
 * <p>
 * The p-values in R are interpolated using the values from Table 4.2, p. 103 of Banerjee et al. (1993).
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> D. A. Dickey and W. A. Fuller, “Distribution of the Estimators for Autoregressive Time Series with a Unit Root,” J. Amer. Stat. Assoc., vol. 74, pp. 427–431, 1979.
 * <li> E. Said and D. A. Dickey, "Testing for Unit Roots in Autoregressive Moving Average Models of Unknown Order," Biometrika, vol. 71, 599–607, 1984.
 * <li> A. Banerjee et al., "Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data," Oxford, Oxford University Press, 1993, ch. 4, pp. 99-135.
 * </ul>
 */
public class ADFFiniteSampleDistribution extends EmpiricalDistribution {//TODO: cannot seed

    static interface TestStat {
        
        double fromRegression(Vector y, Matrix X);
    }

    /**
     * the (finite) sample size
     */
    public final int sampleSize;
    /**
     * the type of augmented Dickey-Fuller (ADF) test
     */
    public final TrendType trend;
    /**
     * indicate whether the distribution is adjusted for lags
     */
    public final boolean lagAdjust;
    /**
     * the lag order used to calculate the test statistics; lagOrder = 0 yields the Dickey-Fuller distribution
     */
    public final int lagOrder;
    /**
     * the number of truncated values
     */
    public final int truncation;
    /**
     * the number of simulations
     */
    public final int nSim;

    /**
     * Construct the finite sample distribution for the augmented Dickey-Fuller test statistics.
     *
     * @param sampleSize the (finite) sample size
     * @param trend      the type of augmented Dickey-Fuller test
     * @param lagAdjust  indicator of whether the distribution is adjusted for lags
     * @param lagOrder   the lag order; lagOrder = 0 yields the Dickey-Fuller distribution
     * @param truncation the number of truncated values
     * @param nSim       the number of simulations
     */
    public ADFFiniteSampleDistribution(
            int sampleSize, TrendType trend,
            boolean lagAdjust, int lagOrder, int truncation,
            int nSim) {
        super(simulation(sampleSize, trend, lagAdjust, lagOrder, truncation, nSim));
        
        this.sampleSize = sampleSize;
        this.trend = trend;
        this.lagAdjust = lagAdjust;
        this.lagOrder = lagOrder;
        this.truncation = truncation;
        this.nSim = nSim;
    }

    /**
     * Construct the finite sample distribution for the augmented Dickey-Fuller test statistics.
     * The number of truncated values is 50.
     *
     * @param sampleSize the (finite) sample size
     * @param trend      the type of augmented Dickey-Fuller test
     * @param lagAdjust  indicator of whether the distribution is adjusted for lags
     * @param lagOrder   the lag order; lagOrder = 0 yields the Dickey-Fuller distribution
     */
    public ADFFiniteSampleDistribution(int sampleSize, TrendType trend, boolean lagAdjust, int lagOrder) {
        this(sampleSize, trend, lagAdjust, lagOrder, 50, 50000);//TODO: truncaton = 1%? 0.1%?
    }

    /**
     * Construct the finite sample distribution for the augmented Dickey-Fuller test statistics.
     * We do not adjust for the lag.
     *
     * @param sampleSize the (finite) sample size
     * @param trend      the type of augmented Dickey-Fuller test
     */
    public ADFFiniteSampleDistribution(int sampleSize, TrendType trend) {
        this(sampleSize, trend, false, 0);
    }

    /**
     * Construct the finite sample distribution for the augmented Dickey-Fuller test statistics.
     * We test for a unit root with drift and deterministic time trend.
     *
     * @param sampleSize the (finite) sample size
     */
    public ADFFiniteSampleDistribution(int sampleSize) {
        this(sampleSize, TrendType.CONSTANT);
    }
    
    private static double[] simulation(int sampleSize, TrendType trend, boolean lagAdjust, int lagOrder, int truncations, int nSim) {
        assertArgument(lagOrder >= 0 && truncations >= 0, "lagOrder >= 0; truncations >= 0");
        assertArgument((lagAdjust && lagOrder > 0) || (!lagAdjust && lagOrder == 0),
                       "if !lagAdjust, then lagOrder == 0; if lagAdjust, then lagOrder > 0");
        
        double[] stats = new double[nSim];
        int simSize = sampleSize + truncations;
        
        for (int i = 0; i < nSim; ++i) {
            StandardNormalRng rnorm = new StandardNormalRng();//TODO: how to seed?
            double[] wn = new double[simSize];//white noise
            for (int j = 0; j < simSize; ++j) {
                wn[j] = rnorm.nextDouble();
            }
            double[] s = R.cumsum(wn);//random walk
            s = Arrays.copyOfRange(s, truncations, simSize);
            double[] ds = Arrays.copyOfRange(wn, truncations + 1, simSize);//R.diff(series);

            int length = sampleSize - lagOrder - 1;
            double[] response = Arrays.copyOfRange(ds, lagOrder, lagOrder + length);
            DenseVector y = new DenseVector(response);
            
            DenseVector sLagged = new DenseVector(Arrays.copyOfRange(s, 0, length));
            DenseMatrix X = new DenseMatrix(length, lagOrder + 1);
            X.setColumn(1, sLagged);
            if (lagOrder > 0) {
                for (int lag = 1; lag <= lagOrder; ++lag) {
                    double[] dsLagged = Arrays.copyOfRange(ds, lagOrder - lag, lagOrder + length - lag);
                    X.setColumn(lag + 1, new DenseVector(dsLagged));
                }
            }

            //perform the OLS regression, depending on the assumptions
            stats[i] = trend.getSupport().fromRegression(y, X);
        }
        
        return stats;
    }
    
    static double testStat(LMProblem problem) {
        OLSRegression instance = new OLSRegression(problem);
        double numerator = instance.beta.betaHat.get(1);
        double denominator = instance.beta.stderr.get(1);
        double testStat = numerator / denominator;
        return testStat;
    }
}
