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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class AutoCovarianceTest {

    /**
     * TacvfAR(c(0.5),10)
     */
    @Test
    public void test_0010() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(
                new Matrix[]{new DenseMatrix(new double[][]{{0.5}})},//phi, AR
                0,
                null,
                new DenseMatrix(new double[][]{{1}})),//sigma
                10);

        assertEquals(1.333333, instance.evaluate(0).get(1, 1), 1e-5);
        assertEquals(0.666667, instance.evaluate(1).get(1, 1), 1e-5);
        assertEquals(0.333333, instance.evaluate(2).get(1, 1), 1e-5);
        assertEquals(0.166667, instance.evaluate(3).get(1, 1), 1e-5);
        assertEquals(0.083333, instance.evaluate(4).get(1, 1), 1e-5);
        assertEquals(0.041667, instance.evaluate(5).get(1, 1), 1e-5);
        assertEquals(0.020833, instance.evaluate(6).get(1, 1), 1e-5);
        assertEquals(0.010417, instance.evaluate(7).get(1, 1), 1e-5);
        assertEquals(0.005208, instance.evaluate(8).get(1, 1), 1e-5);
        assertEquals(0.002604, instance.evaluate(9).get(1, 1), 1e-5);
        assertEquals(0.001302, instance.evaluate(10).get(1, 1), 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void test_0020() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(
                new Matrix[]{new DenseMatrix(new double[][]{{0.5}})},//phi
                0,
                null,
                new DenseMatrix(new double[][]{{1}})),//sigma
                10);

        instance.evaluate(11);
    }

    /**
     * ARMAacf(ar = c(0.5), ma = c(-0.8), lag.max = 10, pacf = FALSE) * 1.12
     */
    @Test
    public void test_0030() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(
                new Matrix[]{new DenseMatrix(new double[][]{{0.5}})},//phi
                0,
                new Matrix[]{new DenseMatrix(new double[][]{{-0.8}})},//theta
                new DenseMatrix(new double[][]{{1}})),//sigma
                10);

        assertEquals(1.120000, instance.evaluate(0).get(1, 1), 1e-5);
        assertEquals(-0.240000, instance.evaluate(1).get(1, 1), 1e-5);
        assertEquals(-0.120000, instance.evaluate(2).get(1, 1), 1e-5);
        assertEquals(-0.060000, instance.evaluate(3).get(1, 1), 1e-5);
        assertEquals(-0.030000, instance.evaluate(4).get(1, 1), 1e-5);
        assertEquals(-0.015000, instance.evaluate(5).get(1, 1), 1e-5);
        assertEquals(-0.007500, instance.evaluate(6).get(1, 1), 1e-5);
        assertEquals(-0.003750, instance.evaluate(7).get(1, 1), 1e-5);
        assertEquals(-0.00187500, instance.evaluate(8).get(1, 1), 1e-5);
        assertEquals(-0.00093750, instance.evaluate(9).get(1, 1), 1e-5);
        assertEquals(-0.00046875, instance.evaluate(10).get(1, 1), 1e-5);
    }

    /**
     * ARMAacf(ar = c(0.3, -0.2, 0.05, 0.04), ma = c(0.2, 0.5), lag.max = 10, pacf = FALSE) * 1.461338
     */
    @Test
    public void test_0040() {
        Matrix[] AR = new Matrix[4];
        AR[0] = new DenseMatrix(new DenseVector(new double[]{0.3}));
        AR[1] = new DenseMatrix(new DenseVector(new double[]{-0.2}));
        AR[2] = new DenseMatrix(new DenseVector(new double[]{0.05}));
        AR[3] = new DenseMatrix(new DenseVector(new double[]{0.04}));

        Matrix[] MA = new Matrix[2];
        MA[0] = new DenseMatrix(new DenseVector(new double[]{0.2}));
        MA[1] = new DenseMatrix(new DenseVector(new double[]{0.5}));

        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(AR, 0, MA, new DenseMatrix(new double[][]{{1}})),
                10);

        assertEquals(1.461338, instance.evaluate(0).get(1, 1), 1e-5);
        assertEquals(0.764270, instance.evaluate(1).get(1, 1), 1e-5);
        assertEquals(0.495028, instance.evaluate(2).get(1, 1), 1e-5);
        assertEquals(0.099292, instance.evaluate(3).get(1, 1), 1e-5);
        assertEquals(0.027449, instance.evaluate(4).get(1, 1), 1e-5);
        assertEquals(0.043699, instance.evaluate(5).get(1, 1), 1e-5);
        assertEquals(0.032385, instance.evaluate(6).get(1, 1), 1e-5);
        assertEquals(0.006320, instance.evaluate(7).get(1, 1), 1e-5);
        assertEquals(-0.001298186, instance.evaluate(8).get(1, 1), 1e-5);
        assertEquals(0.001713744, instance.evaluate(9).get(1, 1), 1e-5);
        assertEquals(0.002385183, instance.evaluate(10).get(1, 1), 1e-5);
    }

//    @Test
    public void test_0050() {
        Matrix[] AR = new Matrix[1];
        AR[0] = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0, 0.5}});

        Matrix[] MA = new Matrix[1];
        MA[0] = AR[0].t();

        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(AR, 0, MA,
                new DenseMatrix(new double[][]{//sigma
                    {1, 0.2},
                    {0.2, 1}
                })),
                10);

        //TODO: the numbers below are wrong; how do we get the expected answers to compare?
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {4.044444, 3.155556},
                    {1.866667, 2.933333}}),
                instance.evaluate(0), 1e-5));
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {3.455556, 3.144444},
                    {1.533333, 2.066667}}),
                instance.evaluate(1), 1e-5));
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {2.494444, 2.605556},
                    {0.766667, 1.033333}}),
                instance.evaluate(2), 1e-5));
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {1.630556, 1.819444},
                    {0.383333, 0.516667}}),
                instance.evaluate(3), 1e-5));
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {1.006944, 1.168056},
                    {0.191667, 0.258333}}),
                instance.evaluate(4), 1e-5));
        assertTrue(AreMatrices.equal(new DenseMatrix(new double[][]{
                    {0.599306, 0.713194},
                    {0.095833, 0.129167}}),
                instance.evaluate(5), 1e-5));
    }
}
