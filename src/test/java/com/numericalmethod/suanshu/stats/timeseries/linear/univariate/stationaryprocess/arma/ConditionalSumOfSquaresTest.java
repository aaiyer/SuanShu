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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAModel;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMASim;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ConditionalSumOfSquaresTest {

    @Test
    public void test_0010() {
        assertTrue(true);
    }
//    @Test
//    public void test_0010() {
//        final int n = 500;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 1, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(new double[]{0.3}, 0, new double[]{0.6}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        assertArrayEquals(new double[]{0.3}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.6}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(1.0, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0012() {
//        final int n = 500;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 0.01, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(new double[]{0.3}, 0, new double[]{0.6}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        assertArrayEquals(new double[]{0.3}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.6}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(0.0001, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0015() {
//        final int n = 100000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 1, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(0.05, new double[]{0.3}, 0, new double[]{0.6}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        assertEquals(0.05, instance.getFittedARMA().mu(), 1e-2);
//        assertArrayEquals(new double[]{0.3}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.6}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(1.0, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0020() {
//        final int n = 800;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 0.5, 1234567);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARMAModel(new double[]{-0.3}, new double[]{0.8}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        Mean mu = new Mean(arimaSim.toArray());
//        double muv = mu.value();//TODO: how to check
//        Variance var = new Variance(arimaSim.toArray());
//        double varv = var.value();//TODO: how to check
//
//        assertArrayEquals(new double[]{-0.3}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.8}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(0.5, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0030() {
//        final int n = 2000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 0.5, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARMAModel(new double[]{-0.3, 0.1}, new double[]{0.2, 0.4}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                2, 0, 2);
//
//        assertArrayEquals(new double[]{-0.3, 0.1}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.2, 0.4}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(0.5, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0035() {
//        final int n = 50000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 0.5, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARMAModel(-0.1, new double[]{-0.3, 0.1}, new double[]{0.2, 0.4}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                2, 0, 2);
//
//        assertEquals(-0.1, instance.getFittedARMA().mu(), 1e-2);
//        assertArrayEquals(new double[]{-0.3, 0.1}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.2, 0.4}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(0.5, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0040() {
//        final int n = 3000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 0.5, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(new double[]{-0.3, 0.1}, 1, new double[]{0.2, 0.4}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                2, 1, 2);
//
//        assertArrayEquals(new double[]{-0.3, 0.1}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{0.2, 0.4}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(0.5, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0050() {
//        final int n = 1000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 1, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARModel(new double[]{0.3}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 0);
//
//        assertArrayEquals(new double[]{0.3}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{}, instance.getFittedARMA().MA(), 0);//TODO: this is better than {@code null}, right?
//        assertEquals(1.0, instance.var(), 1e-1);
//    }
//
//    @Test
//    public void test_0100() {
//        final int n = 500;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 1, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(new double[]{0.3}, 0, new double[]{0.6}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        Matrix cov = instance.covariance();//TODO: how to check the correctness
//
//        Vector stderr = instance.stderr();//TODO: how to check the correctness
//        for (int i = 1; i < stderr.size(); ++i) {
//            assertTrue(stderr.get(i) > 0);
//        }
//    }
//
//    @Test
//    public void test_0110() {
//        final int n = 1000;
//        WhiteNoise Z = new WhiteNoise(n + 100, 0, 1, 123456);
//
//        ARIMASim arimaSim = new ARIMASim(
//                n,
//                new ARIMAModel(new double[]{0.06}, 0, new double[]{-1.0}),
//                Z);
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(
//                arimaSim,
//                1, 0, 1);
//
//        assertArrayEquals(new double[]{0.06}, instance.getFittedARMA().AR(), 1e-1);
//        assertArrayEquals(new double[]{-1.0}, instance.getFittedARMA().MA(), 1e-1);
//        assertEquals(1.0, instance.var(), 1e-1);
//    }
//
//    /**
//     * Apr 5, 2011
//     * a question from aldeida aleti <aldeida.aleti@gmail.com>
//     *
//     * We found that the fitting is not the same as R.
//     * c.f., test_0110
//     */
////    @Test//TODO: explain the differences
//    public void test_0300() {
//        SimpleTimeSeries ts = new SimpleTimeSeries(new double[]{2.2, 2.4, 2.6, 3.6, 1.2, 2.6, 3.4, 3.2});
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(ts, 1, 0, 1);
//        ARMAModel model = instance.getFittedARMA();
//
//        assertArrayEquals(new double[]{0.0638822881663969}, model.AR(), 1e-1);//from R
//        assertArrayEquals(new double[]{-0.9999654276170717}, model.MA(), 1e-1);//from R
//        assertEquals(0.2811226604384638, instance.var(), 1e-1);
//    }
//
//    /**
//     * May 13, 2011
//     * a question from aldeida aleti <aldeida.aleti@gmail.com>
//     *
//     * We found that the fitting is not the same as R.
//     */
//    @Test
//    public void test_0410() {
//        SimpleTimeSeries ts = new SimpleTimeSeries(new double[]{0.25, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.375, 0.375, 0.25, 0.5, 0.375, 0.375, 0.375, 0.375, 0.5, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.5, 0.5, 0.5, 0.625, 0.625, 0.5, 0.625, 0.5, 0.75, 0.625, 0.75, 0.75, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.75, 0.875, 0.75, 0.875, 0.75});
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(ts, 1, 0, 0);
//        ARMAModel model = instance.getFittedARMA();
//
//        assertArrayEquals(new double[]{0.8718}, model.AR(), 1e-1);//from R
//        assertArrayEquals(new double[]{}, model.MA(), 1e-1);//from R
//        assertEquals(0.009635, instance.var(), 1e-2);
//    }
//
//    @Test
//    public void test_0420() {
//        DenseVector v = new DenseVector(new double[]{0.25, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.375, 0.375, 0.25, 0.5, 0.375, 0.375, 0.375, 0.375, 0.5, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.5, 0.5, 0.5, 0.625, 0.625, 0.5, 0.625, 0.5, 0.75, 0.625, 0.75, 0.75, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.75, 0.875, 0.75, 0.875, 0.75});
//        v = v.scaled(10);
//
//        SimpleTimeSeries ts = new SimpleTimeSeries(v.toArray());
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(ts, 1, 0, 0);
//        ARMAModel model = instance.getFittedARMA();
//
//        assertArrayEquals(new double[]{0.8718}, model.AR(), 1e-1);//from R
//        assertArrayEquals(new double[]{}, model.MA(), 1e-1);//from R
//        assertEquals(0.9635, instance.var(), 3e-1);
//    }
//
//    @Test
//    public void test_0430() {
//        DenseVector v = new DenseVector(new double[]{0.25, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.375, 0.375, 0.25, 0.5, 0.375, 0.375, 0.375, 0.375, 0.5, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.5, 0.5, 0.5, 0.625, 0.625, 0.5, 0.625, 0.5, 0.75, 0.625, 0.75, 0.75, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.75, 0.875, 0.75, 0.875, 0.75});
//        v = v.scaled(100);
//
//        SimpleTimeSeries ts = new SimpleTimeSeries(v.toArray());
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(ts, 1, 0, 0);
//        ARMAModel model = instance.getFittedARMA();
//
//        assertArrayEquals(new double[]{0.8718}, model.AR(), 1e-1);//from R
//        assertArrayEquals(new double[]{}, model.MA(), 1e-1);//from R
//        assertEquals(96.35, instance.var(), 3e1);
//    }
//
//    @Test
//    public void test_0440() {
//        DenseVector v = new DenseVector(new double[]{0.25, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.375, 0.375, 0.25, 0.5, 0.375, 0.375, 0.375, 0.375, 0.5, 0.5, 0.625, 0.625, 0.625, 0.5, 0.5, 0.5, 0.5, 0.5, 0.625, 0.625, 0.5, 0.625, 0.5, 0.75, 0.625, 0.75, 0.75, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.875, 0.75, 0.875, 0.75, 0.875, 0.75});
//        v = v.scaled(0.01);
//
//        SimpleTimeSeries ts = new SimpleTimeSeries(v.toArray());
//        ConditionalSumOfSquares instance = new ConditionalSumOfSquares(ts, 1, 0, 0);
//        ARMAModel model = instance.getFittedARMA();
//
//        assertArrayEquals(new double[]{0.8718}, model.AR(), 1e-1);//from R
//        assertArrayEquals(new double[]{}, model.MA(), 1e-1);//from R
//        assertEquals(9.635e-07, instance.var(), 3e-7);
//    }
}
