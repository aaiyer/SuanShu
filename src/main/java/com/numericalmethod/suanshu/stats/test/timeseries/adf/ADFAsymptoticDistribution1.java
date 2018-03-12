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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.MultiVariateRealization;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.integration.sde.Construction;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.integration.sde.Euler;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.Ft;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.FtWt;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.SDE;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.coefficients.ConstantSigma1;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.coefficients.Drift;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.pow;

/**
 * This is the asymptotic distribution of the Augmented Dickey-Fuller test statistics, for the CONSTANT_TIME case.
 *
 * <p>
 * The p-values in R are interpolated using the values from Table 4.2, p. 103 of Banerjee et al. (1993).
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"Wayne A. Fuller. "Introduction to Statistical Time Series". Chapter 10. pp.553, 554, 561, 568."
 * <li>"A. Banerjee, J. J. Dolado, J. W. Galbraith, and D. F. Hendry. "Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data", 1993. Oxford University Press, Oxford."
 * </ul>
 * 
 * @deprecated use instead {@link ADFAsymptoticDistribution}
 */
@Deprecated
public class ADFAsymptoticDistribution1 extends EmpiricalDistribution {//TODO: 1 out the 3 possible forms only?

    /**
     * the number of grid point in interval [0, 1]
     *
     * <p>
     * The bigger {@code nT} is, the finer the time discretization is, the smaller the discretization error is, the more accurate the results are.
     */
    public final int nT;
    /**
     * the number of simulations
     */
    public final int nSim;
    /**
     * the seed (for randomly generating the simulations)
     */
    public final long seed;

    /**
     * the types of Dickey-Fuller tests available
     */
    public static enum Type {

        DICKEY_FULLER,
        AUGMENTED_DICKEY_FULLER,}

    /**
     * Construct the asymptotic distribution for the Augmented Dickey Fuller test statistics.
     * 
     * @param type the type of Dickey-Fuller test
     */
    public ADFAsymptoticDistribution1(Type type) {
        this(30000, 30000, type, new UniformRng().nextLong());
    }

    /**
     * Construct the asymptotic distribution for the Augmented Dickey Fuller test statistics.
     * 
     * @param nT the number of grid point in interval [0, 1]
     * @param nSim the number of simulations
     * @param type the types of Dickey-Fuller tests available
     * @param seed the seed
     */
    public ADFAsymptoticDistribution1(int nT, int nSim, Type type, long seed) {
        super(simulation(nT, nSim, type, seed));

        this.nT = nT;
        this.nSim = nSim;
        this.seed = seed;
    }

    /**
     * This is an implementation of "Wayne A. Fuller. "Introduction to Statistical Time Series". Chapter 10. pp.553, 554, 561, 568."
     * It computes the distribution by Monte Carlo simulation.
     *
     * @param nT the number of grid point in interval [0, 1]
     * @param nSim the number of simulations
     * @param type the types of Dickey-Fuller tests available
     * @param seed the seed
     * @return a set of statistics
     */
    private static double[] simulation(int nT, int nSim, Type type, long seed) {
        TimeGrid t = new EvenlySpacedGrid(0, 1, nT);

        SDE sde = new SDE(
                new Drift() {

                    public Vector evaluate(Ft ft) {
                        FtWt ftwt = (FtWt) ft;

                        double t = ftwt.t();
                        double Wt = ftwt.Wt().get(1);

                        double dG = Wt * Wt;//Fuller, p.553, eq. 10.1.14
                        double dT = 0;//Fuller, p.553, eq. 10.1.14
                        double dH = Wt;//Fuller, p.561, Theorem 10.1.3
                        double dK = 2 * t * Wt - dH;//Fuller, p.568, Theorem 10.1.6

                        return new DenseVector(new double[]{dG, dT, dH, dK});
                    }
                },
                new ConstantSigma1(new DenseMatrix(new double[][]{
                    {0},
                    {1},//dT; Fuller, p.553, eq. 10.1.14
                    {0},
                    {0}
                })),
                1) {//1 driving Brownian motion

            @Override
            public Ft getFt() {
                return new FtWt();
            }
        };

        Construction Xt = new Euler(sde, t);
        Xt.seed(seed);

        double[] stats = new double[nSim];

        //G, T, H, K are the intermediate variables in computing the asymptotic distribution of ADF test statistics.
        double[] G = new double[nSim];//Fuller, p.553, eq. 10.1.14
        double[] T = new double[nSim];//Fuller, p.553, eq. 10.1.14
        double[] H = new double[nSim];//Fuller, p.561, Theorem 10.1.3
        double[] K = new double[nSim];//Fuller, p.568, Theorem 10.1.6

        for (int j = 0; j < nSim; ++j) {
            MultiVariateRealization xt = Xt.nextRealization(new DenseVector(4, 0.0));//x0 = [0, 0, 0, 0]
            Vector GTHK = xt.lastValue();
            G[j] = GTHK.get(1);
            T[j] = GTHK.get(2);
            H[j] = GTHK.get(3);
            K[j] = GTHK.get(4);

            switch (type) {
                /*
                 * Fuller, p.554, Corollary 10.1.1.2
                 * DF -> (T^2 - 1) / 2 / sqrt(G)
                 */
                case DICKEY_FULLER:
                    stats[j] = 0.5 * (pow(T[j], 2) - 1) / pow(G[j], 0.5);
                    break;
                /*
                 * Fuller, p.568, Theorem 10.1.6
                 * ADF -> 0.5 * ((T-2H) * (T-6K) - 1) / sqrt(G - H^2 - 3K^2)
                 */
                case AUGMENTED_DICKEY_FULLER:
                    stats[j] = 0.5 * ((T[j] - 2 * H[j]) * (T[j] - 6 * K[j]) - 1) / pow(G[j] - pow(H[j], 2) - 3 * pow(K[j], 2), 0.5);
                    break;
                default:
                    throw new RuntimeException("unreachable");
            }
        }

        return stats;
    }
}
