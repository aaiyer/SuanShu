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

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAModel;
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
                new ARIMAModel(new double[]{0.5}, 0, null),
                1, 10);

        assertEquals(1.333333, instance.evaluate(0), 1e-5);
        assertEquals(0.666667, instance.evaluate(1), 1e-5);
        assertEquals(0.333333, instance.evaluate(2), 1e-5);
        assertEquals(0.166667, instance.evaluate(3), 1e-5);
        assertEquals(0.083333, instance.evaluate(4), 1e-5);
        assertEquals(0.041667, instance.evaluate(5), 1e-5);
        assertEquals(0.020833, instance.evaluate(6), 1e-5);
        assertEquals(0.010417, instance.evaluate(7), 1e-5);
        assertEquals(0.005208, instance.evaluate(8), 1e-5);
        assertEquals(0.002604, instance.evaluate(9), 1e-5);
        assertEquals(0.001302, instance.evaluate(10), 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void test_0020() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(new double[]{0.5}, 0, null),
                1, 10);

        instance.evaluate(11);
    }

    /**
     * ARMAacf(ar = c(0.5), ma = c(-0.8), lag.max = 10, pacf = FALSE) * 1.12
     */
    @Test
    public void test_0030() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(
                new double[]{0.5},
                0,
                new double[]{-0.8}),
                1, 10);

        assertEquals(1.120000, instance.evaluate(0), 1e-5);
        assertEquals(-0.240000, instance.evaluate(1), 1e-5);
        assertEquals(-0.120000, instance.evaluate(2), 1e-5);
        assertEquals(-0.060000, instance.evaluate(3), 1e-5);
        assertEquals(-0.030000, instance.evaluate(4), 1e-5);
        assertEquals(-0.015000, instance.evaluate(5), 1e-5);
        assertEquals(-0.007500, instance.evaluate(6), 1e-5);
        assertEquals(-0.003750, instance.evaluate(7), 1e-5);
        assertEquals(-0.00187500, instance.evaluate(8), 1e-5);
        assertEquals(-0.00093750, instance.evaluate(9), 1e-5);
        assertEquals(-0.00046875, instance.evaluate(10), 1e-5);
    }

    /**
     * ARMAacf(ar = c(0.3, -0.2, 0.05, 0.04), ma = c(0.2, 0.5), lag.max = 10, pacf = FALSE) * 1.461338
     */
    @Test
    public void test_0040() {
        AutoCovariance instance = new AutoCovariance(
                new ARIMAModel(new double[]{0.3, -0.2, 0.05, 0.04}, 0, new double[]{0.2, 0.5}),
                1, 10);

        assertEquals(1.461338, instance.evaluate(0), 1e-5);
        assertEquals(0.764270, instance.evaluate(1), 1e-5);
        assertEquals(0.495028, instance.evaluate(2), 1e-5);
        assertEquals(0.099292, instance.evaluate(3), 1e-5);
        assertEquals(0.027449, instance.evaluate(4), 1e-5);
        assertEquals(0.043699, instance.evaluate(5), 1e-5);
        assertEquals(0.032385, instance.evaluate(6), 1e-5);
        assertEquals(0.006320, instance.evaluate(7), 1e-5);
        assertEquals(-0.001298186, instance.evaluate(8), 1e-5);
        assertEquals(0.001713744, instance.evaluate(9), 1e-5);
        assertEquals(0.002385183, instance.evaluate(10), 1e-5);
    }
}
