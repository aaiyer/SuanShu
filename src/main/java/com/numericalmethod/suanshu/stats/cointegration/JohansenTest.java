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
package com.numericalmethod.suanshu.stats.cointegration;

import com.numericalmethod.suanshu.stats.cointegration.JohansenAsymptoticDistribution.Test;
import com.numericalmethod.suanshu.stats.cointegration.JohansenAsymptoticDistribution.TrendType;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.log;

/**
 * The maximum number of cointegrating relations among a multivariate time series is the rank of the <i>Π</i> matrix.
 * To determine the (most likely) number of cointegrating relations <i>r</i>,
 * we need to check the likelihood of hypothesis of <i>r</i> being from 1 to the rank.
 * Johansen provides two types of such hypothesis {@linkplain JohansenAsymptoticDistribution.Test test} statistics.
 *
 * @author Haksun Li
 * @see JohansenAsymptoticDistribution.Test
 */
public class JohansenTest {

    private JohansenAsymptoticDistribution[] dist = null;
    private final Test test;
    private final TrendType trend;
    private final int dim;
    private final int nSim;
    private final int nT;

    /**
     * Construct an instance of {@code JohansenTest}.
     *
     * @param test  the type of Johansen cointegration test
     * @param trend the trend type
     * @param dim   the number of (real) eigenvalues
     * @param nSim  the number of simulations
     * @param nT    the number of discretization levels
     */
    public JohansenTest(Test test, TrendType trend, int dim, int nSim, int nT) {
        this.test = test;
        this.trend = trend;
        this.dim = dim;
        this.nSim = nSim;
        this.nT = nT;
    }

    /**
     * Construct an instance of {@code JohansenTest}.
     *
     * @param test  the type of Johansen cointegration test
     * @param trend the trend type
     * @param dim   the number of (real) eigenvalues
     */
    public JohansenTest(Test test, TrendType trend, int dim) {
        this(test, trend, dim, 500, 500);
    }

    /**
     * Get the set of likelihood ratio test statistics for testing <i>H(r)</i> in <i>H(r+1)</i>.
     *
     * @param coint the results of Johansen cointegration
     * @return the likelihood ratio test statistics
     */
    public ImmutableVector getStats(CointegrationMLE coint) {

        double[] eigenvalues = coint.getEigenvalues().toArray();
        int rank = eigenvalues.length;
        int n = coint.n();

        double[] stats = new double[rank];
        for (int i = 0; i < rank; ++i) {
            if (test == JohansenAsymptoticDistribution.Test.EIGEN) {//eigen statistics
                stats[i] = -n * log(1. - eigenvalues[i]);
            } else {//trace statistics
                //filling backward for efficiency
                int j = rank - i - 1;
                if (i == 0) {
                    stats[j] = -n * log(1. - eigenvalues[j]);
                } else {
                    stats[j] = log(1. - eigenvalues[j]);
                    stats[j] *= -n;
                    stats[j] += stats[rank - i];//cumulative sum
                }
            }
        }

        return new ImmutableVector(new DenseVector(stats));
    }

    /**
     * Get the (most likely) order of cointegration.
     *
     * @param coint the results of Johansen cointegration
     * @param level the confidence level, i.e., 1 - quantile
     * @return the order of cointegration
     */
    public int r(CointegrationMLE coint, double level) {
        //lazy construction of the distributions
        if (dist == null) {
            dist = new JohansenAsymptoticDistribution[dim];
            for (int r = 0; r < dim; ++r) {
                dist[r] = new JohansenAsymptoticDistribution(
                        test, trend, dim - r,
                        dim - r <= 2 ? Math.max(5000, nSim) : nSim, nT,
                        new UniformRng().nextLong());
            }
        }

        double quantile = 1 - level;
        double[] stats = getStats(coint).toArray();

        int r = 0;
        for (; r < stats.length; ++r) {
            if (stats[r] < dist[r].quantile(quantile)) {
                break;
            }
        }

        return r;
    }
}
