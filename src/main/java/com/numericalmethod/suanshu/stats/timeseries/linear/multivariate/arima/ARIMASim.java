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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.multivariate.NormalRvg;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class generates simulations of a multivariate ARIMA model.
 *
 * @author Kevin Sun
 */
public class ARIMASim extends SimpleMultiVariateTimeSeries {

    /**
     * Simulate a multivariate ARIMA process.
     *
     * @param n     the length of the multivariate time series to generate
     * @param arima a multivariate ARIMA model
     * @param seed  a seed for the random number generation
     */
    public ARIMASim(int n, ARIMAModel arima, long seed) {
        super(sim(n, arima, seed).t());
    }

    /**
     * Simulate a multivariate ARIMA process.
     *
     * @param n     the length of the multivariate time series to generate
     * @param arima a multivariate ARIMA model
     */
    public ARIMASim(int n, ARIMAModel arima) {
        this(n, arima, new UniformRng().nextLong());
    }

    /**
     * an implementation of the simulation algorithm
     *
     * @param n     the length of the time series to generate
     * @param arima an ARIMA model
     * @param seed  a seed for the random number generation
     *
     * @return a multivariate ARIMA time series
     */
    private static DenseMatrix sim(int n, ARIMAModel arima, long seed) {
        int maxPQ = arima.maxPQ();
        assertArgument(n > maxPQ, "time series length > max(p, q)");

        /*
         * The number of columns of innovations is at least
         * <blockquote><code>
         * n + max(p, q) + d
         * </code></blockquote>
         */
        int length = n + maxPQ + arima.d();
        DenseMatrix Z = new DenseMatrix(arima.dimension(), length);//innovations
        NormalRvg innovations = new NormalRvg(new DenseVector(R.rep(0., arima.dimension())), arima.sigma());
        innovations.seed(seed);
        for (int i = 1; i <= length; ++i) {
            Z.setColumn(i, new DenseVector(innovations.nextVector()));
        }

        //arma
        DenseMatrix series0 = new DenseMatrix(new DenseMatrix(arima.dimension(), length).ZERO());
        for (int t = maxPQ + 1; t <= length; ++t) {
            DenseMatrix xLags = null;
            if (arima.p() > 0) {
                xLags = new DenseMatrix(new DenseMatrix(arima.dimension(), arima.p()).ZERO());//autoregressive
                for (int i = 1; i <= arima.p(); ++i) {
                    xLags.setColumn(i, series0.getColumn(t - i));
                }
            }
            DenseMatrix zLags = null;
            if (arima.q() > 0) {
                zLags = new DenseMatrix(new DenseMatrix(arima.dimension(), arima.q()).ZERO());//moving average
                for (int j = 1; j <= arima.q(); ++j) {
                    zLags.setColumn(j, Z.getColumn(t - j));
                }
            }
            series0.setColumn(t, arima.getArma().armaMean(xLags, zLags).getColumn(1).add(new DenseVector(Z.getColumn(t))));
        }

        //integration
        DenseMatrix series1 = new DenseMatrix(arima.dimension(), length - arima.d());
        for (int r = 1; r <= arima.dimension(); ++r) {
            double[] cumsum = series0.getRow(r).toArray();
            for (int i = 1; i <= arima.d(); ++i) {
                cumsum = R.cumsum(cumsum);
            }
            series1.setRow(r, new DenseVector(cumsum));
        }

        //extract the last n items (drop the heads)
        DenseMatrix series2 = new DenseMatrix(arima.dimension(), n);
        for (int r = 1; r <= arima.dimension(); ++r) {
            series2.setRow(r, new DenseVector(Arrays.copyOfRange(series1.getRow(r).toArray(), series1.nCols() - n, series1.nCols())));
        }

        return series2;
    }
}
