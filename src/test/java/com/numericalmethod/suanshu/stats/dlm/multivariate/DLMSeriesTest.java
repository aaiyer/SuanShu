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
package com.numericalmethod.suanshu.stats.dlm.multivariate;

import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.random.multivariate.NormalRvg;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ConditionalSumOfSquares;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class DLMSeriesTest {

    @Test
    public void test_0010() {
        int T = 1000;
        int p = 3;//state dimension
        int d = 2;//observation dimension

        DenseVector m0 = new DenseVector(R.rep(0., p));
        DenseMatrix C0 = new DenseMatrix(p, p).ONE();

        DenseMatrix F = new DenseMatrix(new double[][]{
                    {0.5, 0.2, 0.3},
                    {0.1, 0.8, 0.1}
                });
        DenseMatrix V = new DenseMatrix(d, d).ONE().scaled(0.5);

        DenseMatrix G = new DenseMatrix(p, p).ONE();
        DenseMatrix W = new DenseMatrix(p, p).ONE().scaled(0.2);

        DLM model = new DLM(m0, C0,
                new ObservationEquation(F, V),
                new StateEquation(G, W));

        DLMSeries instance = new DLMSeries(T, model);

        MultiVariateTimeSeries Xt = instance.getStates();
        Matrix states = Xt.toMatrix();
        assertEquals(T, states.nRows(), 1e-15);
        assertEquals(p, states.nCols(), 1e-15);

        MultiVariateTimeSeries Yt = instance.getObservations();
        Matrix observations = Yt.toMatrix();
        assertEquals(T, observations.nRows(), 1e-15);
        assertEquals(d, observations.nCols(), 1e-15);
    }

    @Test
    public void test_0020() {
        NormalRvg rnorm = new NormalRvg(1);
        rnorm.seed(1234567890L);

        double ar1 = 0.7;
        double sigma1 = 0.5;
        StateEquation states = new StateEquation(//x_t = ar1 * x_{t-1} + sqrt(sigma1) * w_t
                new DenseMatrix(new double[][]{{ar1}}),
                null,
                new DenseMatrix(new double[][]{{sigma1}}),
                rnorm);

        double sigma2 = 0.1;
        ObservationEquation obs = new ObservationEquation(//yt - xt ~ N(0, sqrt(sigma2))
                new DenseMatrix(new double[][]{{1.0}}),
                new DenseMatrix(new double[][]{{sigma2}}),
                rnorm);

        final int T = 100000;
        DLM model = new DLM(
                new DenseVector(new double[]{100}),
                new DenseMatrix(new double[][]{{100}}),
                obs,
                states);//x0 ~ N(100, 0.00001)

        DLMSeries instance = new DLMSeries(T, model, null, rnorm);
        SimpleMultiVariateTimeSeries Xt = instance.getStates();
        SimpleMultiVariateTimeSeries Yt = instance.getObservations();

        assertEquals(T, Xt.size());
        assertEquals(T, Yt.size());

        //fit an AR(1) model for the states
        ConditionalSumOfSquares arFit = new ConditionalSumOfSquares(
                new SimpleTimeSeries(Xt.toMatrix().getColumn(1).toArray()),
                1, 0, 0);
        assertArrayEquals(new double[]{ar1}, arFit.getFittedARMA().AR(), 1e-1);
        assertEquals(sigma1, arFit.var(), 1e-1);

        Matrix YM = Yt.toMatrix();
        Matrix XM = Xt.toMatrix();
        double[] diff = new double[T];
        for (int i = 1; i <= T; ++i) {
            diff[i - 1] = YM.get(1, i) - XM.get(1, i);
        }

        Variance var = new Variance(diff);
        assertEquals(sigma2, var.value(), 1e-3);
    }
}
