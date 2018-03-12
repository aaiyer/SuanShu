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
package com.numericalmethod.suanshu.stats.dlm.univariate;

import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ConditionalSumOfSquares;
import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DLMSeriesTest {

    @Test
    public void test_0010() {
        NormalRng rnorm = new NormalRng(0, 1);
        rnorm.seed(1234567890L);

        double ar1 = 0.7;
        double sigma1 = 0.2;
        StateEquation states = new StateEquation(ar1, 0, sigma1, rnorm);//x_t = ar1 * x_{t-1} + sigma1 * w_t

        double sigma2 = 0.1;
        ObservationEquation obs = new ObservationEquation(1.0, sigma2, rnorm);//yt - xt ~ N(0, sigma2)

        final int T = 200000;
        DLM model = new DLM(100, 0.00001, obs, states);//x0 ~ N(100, 0.00001)

        DLMSeries instance = new DLMSeries(T, model, R.rep(0.0, T), rnorm);
        double[] Xt = instance.getStates();
        double[] Yt = instance.getObservations();

        assertEquals(T, Xt.length);
        assertEquals(T, Yt.length);

        //fit an AR(1) model for the states
        ConditionalSumOfSquares arFit = new ConditionalSumOfSquares(
                new SimpleTimeSeries(Xt),
                1, 0, 0);
        assertArrayEquals(new double[]{ar1}, arFit.getFittedARMA().AR(), 1e-1);
        assertEquals(sigma1, arFit.var(), 1e-1);

        double[] diff = new double[T];
        for (int i = 0; i < T; ++i) {
            diff[i] = Yt[i] - Xt[i];
        }

        Variance var = new Variance(diff);
        assertEquals(sigma2, var.value(), 1e-3);
    }
}
