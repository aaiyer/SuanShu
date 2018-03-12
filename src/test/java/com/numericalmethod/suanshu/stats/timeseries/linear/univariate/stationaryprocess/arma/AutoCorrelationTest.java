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
public class AutoCorrelationTest {

    /**
     * ARMAacf(ar = c(0.5), ma = c(-0.8), lag.max = 10, pacf = FALSE)
     */
    @Test
    public void test_0010() {
        AutoCorrelation instance = new AutoCorrelation(
                new ARIMAModel(
                new double[]{0.5},
                0,
                new double[]{-0.8}),
                1, 10);

        assertEquals(1, instance.evaluate(0), 1e-5);
        assertEquals(-0.2142857143, instance.evaluate(1), 1e-5);
        assertEquals(-0.1071428571, instance.evaluate(2), 1e-5);
        assertEquals(-0.0535714286, instance.evaluate(3), 1e-5);
        assertEquals(-0.0267857143, instance.evaluate(4), 1e-5);
        assertEquals(-0.0133928571, instance.evaluate(5), 1e-5);
        assertEquals(-0.0066964286, instance.evaluate(6), 1e-5);
        assertEquals(-0.0033482143, instance.evaluate(7), 1e-5);
        assertEquals(-0.0016741071, instance.evaluate(8), 1e-5);
        assertEquals(-0.0008370536, instance.evaluate(9), 1e-5);
        assertEquals(-0.0004185268, instance.evaluate(10), 1e-5);
    }

    /**
     * ARMAacf(ar = c(0.3, -0.2, 0.05, 0.04), ma = c(0.2, 0.5), lag.max = 10, pacf = FALSE)
     */
    @Test
    public void test_0020() {
        AutoCorrelation instance = new AutoCorrelation(
                new ARIMAModel(new double[]{0.3, -0.2, 0.05, 0.04}, 0, new double[]{0.2, 0.5}),
                1, 10);

        assertEquals(1, instance.evaluate(0), 1e-5);
        assertEquals(0.5229935806, instance.evaluate(1), 1e-5);
        assertEquals(0.3387499282, instance.evaluate(2), 1e-5);
        assertEquals(0.0679460056, instance.evaluate(3), 1e-5);
        assertEquals(0.0187834951, instance.evaluate(4), 1e-5);
        assertEquals(0.0299030870, instance.evaluate(5), 1e-5);
        assertEquals(0.0221615245, instance.evaluate(6), 1e-5);
        assertEquals(0.0043248549, instance.evaluate(7), 1e-5);
        assertEquals(-0.0008883543, instance.evaluate(8), 1e-5);
        assertEquals(0.0011727224, instance.evaluate(9), 1e-5);
        assertEquals(0.0016321913, instance.evaluate(10), 1e-5);
    }
}
