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

import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.brownian.RandomWalk;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.Filtration;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.FiltrationFunction;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.IntegralDB;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.IntegralDt;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.Construction;
import com.numericalmethod.suanshu.stats.test.timeseries.adf.AugmentedDickeyFuller.TrendType;

/**
 * This class computes the asymptotic distribution of the augmented Dickey-Fuller (ADF) test statistics.
 *
 * <p>
 * There are three main versions of the test and thus three possible asymptotic distributions:
 * <ol>
 * <li>(NO_CONSTANT) test for a unit root without drift or time trend;
 * <li>(CONSTANT) test for a unit root with drift;
 * <li>(CONSTANT_TIME) test for a unit root with drift and deterministic time trend.
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
public class ADFAsymptoticDistribution extends EmpiricalDistribution {

    static interface TestStat {

        double fromFiltration(Filtration FT);
    }
    /**
     * the type of augmented Dickey-Fuller (ADF) test
     */
    public final TrendType trend;
    /**
     * the number of simulations
     */
    public final int nSim;
    /**
     * the number of grid points in interval [0, 1]
     *
     * <p>
     * The bigger {@code nT} is, the finer the time discretization is, the smaller the discretization error is, and the more accurate the results are.
     */
    public final int nT;

    /**
     * Construct the asymptotic distribution for the augmented Dickey-Fuller test statistics.
     *
     * @param trend the type of augmented Dickey-Fuller test
     * @param nSim the number of simulations
     * @param nT the number of grid points in interval [0, 1]
     * @param seed the seed
     */
    public ADFAsymptoticDistribution(TrendType trend, int nSim, int nT, long seed) {
        super(simulation(trend, nSim, nT, seed));

        this.trend = trend;
        this.nSim = nSim;
        this.nT = nT;
    }

    /**
     * Construct the asymptotic distribution for the augmented Dickey-Fuller test statistics.
     *
     * @param trend the type of augmented Dickey-Fuller test
     */
    public ADFAsymptoticDistribution(TrendType trend) {
        this(trend, 10000, 10000, new UniformRng().nextLong());//TODO: are 10,000's sufficient?
    }

    /**
     * Generate a large sample of test statistics for the computation of empirical distribution.
     *
     * @param trend the type of augmented Dickey-Fuller test
     * @param nSim the number of simulations
     * @param nT the number of grid points in interval [0, 1]
     * @return a set of statistics
     */
    private static double[] simulation(TrendType trend, int nSim, int nT, long seed) {
        double[] stats = new double[nSim];
        TimeGrid T = new EvenlySpacedGrid(0, 1, nT);
        Construction B = new RandomWalk(T);
        B.seed(seed);

        for (int i = 0; i < nSim; ++i) {//generate a test statistics for each filtration
            Filtration FT = new Filtration(B.nextRealization(0));
            stats[i] = trend.getSupport().fromFiltration(FT);
        }

        return stats;
    }

    static double ratio(FiltrationFunction numeratorFunction, FiltrationFunction denominatorFunction, Filtration FT) {
        double numerator = new IntegralDB(numeratorFunction).integral(FT);
        double denominator = new IntegralDt(denominatorFunction).integral(FT);
        denominator = Math.sqrt(denominator);

        double testStat = numerator / denominator;
        return testStat;
    }
}
