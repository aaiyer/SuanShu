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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess;

import java.util.Arrays;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.InverseTransformSampling;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class MADecompositionTest {

    /**
     * from R: ?decompose
     * example taken from Kendall/Stuart
     * @see M. Kendall and A. Stuart (1983) The Advanced Theory of Statistics, Vol.3, Griffin, 410–414.
     *
    x <- c(-50, 175, 149, 214, 247, 237, 225, 329, 729, 809,
    530, 489, 540, 457, 195, 176, 337, 239, 128, 102, 232, 429, 3,
    98, 43, -141, -77, -13, 125, 361, -45, 184)
    x <- ts(x, start = c(1951, 1), end = c(1958, 4), frequency = 4)
    m <- decompose(x)
    m
     *
     */
    @Test
    public void test_0010() {
        double[] Xt = new double[]{
            -50, 175, 149, 214, 247, 237, 225, 329, 729, 809,
            530, 489, 540, 457, 195, 176, 337, 239, 128, 102, 232, 429, 3,
            98, 43, -141, -77, -13, 125, 361, -45, 184};

        MADecomposition instance = new MADecomposition(new SimpleTimeSeries(Xt), 4);

        assertArrayEquals(instance.trend.toArray(),
                new double[]{
                    Double.NaN, Double.NaN, 159.125, 204.000,
                    221.250, 245.125, 319.750, 451.500,
                    561.125, 619.250, 615.625, 548.000,
                    462.125, 381.125, 316.625, 264.000,
                    228.375, 210.750, 188.375, 199.000,
                    207.125, 191.000, 166.875, 72.000,
                    -9.250, -33.125, -36.750, 36.250,
                    103.000, 131.625, Double.NaN, Double.NaN
                },
                1e-3);

        double[] seasonal = Arrays.copyOf(instance.seasonal.toArray(), 4);
        assertArrayEquals(seasonal,
                new double[]{
                    62.45982142857142, 86.17410714285714, -88.37946428571428, -60.25446428571428
                },
                1e-14);

        assertArrayEquals(instance.random.toArray(),
                new double[]{
                    Double.NaN, Double.NaN, 78.254464285714278, 70.254464285714278,
                    -36.709821428571416, -94.299107142857139, -6.370535714285722, -62.245535714285722,
                    105.415178571428555, 103.575892857142890, 2.754464285714221, 1.254464285714334,
                    15.415178571428555, -10.299107142857110, -33.245535714285722, -27.745535714285722,
                    46.165178571428555, -57.924107142857139, 28.004464285714278, -36.745535714285722,
                    -37.584821428571416, 151.825892857142890, -75.495535714285722, 86.254464285714278,
                    -10.209821428571423, -194.049107142857139, 48.129464285714278, 11.004464285714285,
                    -40.459821428571423, 143.200892857142890, Double.NaN, Double.NaN
                },
                1e-13);

    }

    @Test
    public void test_0020() {
        final int n = 100;
        double[] trend = R.seq(1.0, n, 1);
        double[] seasonal = new double[]{4, 8, -8, -4};
        InverseTransformSampling rnorm = new InverseTransformSampling(new NormalDistribution(0, 1));
        rnorm.seed(1000000);

        SimpleTimeSeries Xt = new AdditiveModel(trend, seasonal, rnorm);
        MADecomposition instance = new MADecomposition(Xt, seasonal.length);

        double[] trendArr = instance.trend.toArray();
        for (int i = 0; i < n; ++i) {
            if (!Double.isNaN(trendArr[i])) {
                assertEquals(trend[i], trendArr[i], 1.5);
            }
        }

        double[] seasonalArr = instance.seasonal.toArray();
        assertEquals(seasonalArr[0], seasonal[0], 1);
        assertEquals(seasonalArr[1], seasonal[1], 1);
        assertEquals(seasonalArr[2], seasonal[2], 1);
        assertEquals(seasonalArr[3], seasonal[3], 1);
    }

    @Test
    public void test_0030() {
        final int n = 100;
        double[] trend = R.seq(1.0, n, 1);
        InverseTransformSampling rnorm = new InverseTransformSampling(new NormalDistribution(0, 1));
        rnorm.seed(2000000);

        SimpleTimeSeries Xt = new AdditiveModel(trend, null, rnorm);
        MADecomposition instance = new MADecomposition(Xt, 2, 1);

        double[] trendArr = instance.trend.toArray();
        for (int i = 0; i < n; ++i) {
            if (!Double.isNaN(trendArr[i])) {
                assertEquals(trend[i], trendArr[i], 1.5);
            }
        }

        double[] seasonalArr = instance.seasonal.toArray();
        assertArrayEquals(new double[seasonalArr.length], seasonalArr, 0);
    }

    /**
     * no decomposition, no seasonality, no random (b/c mt = Xt when no MA smoothing)
     */
    @Test
    public void test_0100() {
        final int n = 100;
        double[] trend = R.seq(1.0, n, 1);
        InverseTransformSampling rnorm = new InverseTransformSampling(new NormalDistribution(0, 1));

        SimpleTimeSeries Xt = new AdditiveModel(trend, null, rnorm);
        MADecomposition instance = new MADecomposition(Xt, 1);

        assertArrayEquals(Xt.toArray(), instance.trend.toArray(), 1e-15);

        double[] seasonalArr = instance.seasonal.toArray();
        assertArrayEquals(new double[seasonalArr.length], seasonalArr, 0);
    }
}
