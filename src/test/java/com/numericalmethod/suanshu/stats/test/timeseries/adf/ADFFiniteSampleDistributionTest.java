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

import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.test.timeseries.adf.AugmentedDickeyFuller.TrendType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The expected results are taken from
 * "Wayne A. Fuller. "Table 4.2. Introduction to Statistical Time Series"."
 *
 * @author Kevin Sun
 */
public class ADFFiniteSampleDistributionTest {

    @Test
    public void test_0010() {
        ADFFiniteSampleDistribution dist =
                new ADFFiniteSampleDistribution(100, TrendType.NO_CONSTANT,
                false, 0, 50, 1000);

        assertEquals(-2.60, dist.quantile(0.01), 4e-1);//why not 3e-1?
        assertEquals(-2.24, dist.quantile(0.025), 4e-1);//why not 3e-1?
        assertEquals(-1.95, dist.quantile(0.05), 4e-1);//why not 3e-1?
        assertEquals(-1.61, dist.quantile(0.1), 4e-1);//why not 3e-1?
        assertEquals(0.90, dist.quantile(0.9), 4e-1);//why not 3e-1?
        assertEquals(1.29, dist.quantile(0.95), 4e-1);//why not 3e-1?
        assertEquals(1.64, dist.quantile(0.975), 4e-1);//why not 3e-1?
        assertEquals(2.03, dist.quantile(0.99), 4e-1);//why not 3e-1?
    }

    @Test
    public void test_0020() {
        ADFFiniteSampleDistribution dist =
                new ADFFiniteSampleDistribution(100, TrendType.CONSTANT,
                false, 0, 50, 1000);

        assertEquals(-3.51, dist.quantile(0.01), 3e-1);
        assertEquals(-3.17, dist.quantile(0.025), 3e-1);
        assertEquals(-2.89, dist.quantile(0.05), 3e-1);
        assertEquals(-2.58, dist.quantile(0.1), 3e-1);
        assertEquals(-0.42, dist.quantile(0.9), 3e-1);
        assertEquals(-0.05, dist.quantile(0.95), 3e-1);
        assertEquals(0.26, dist.quantile(0.975), 3e-1);
//        assertEquals(0.63, dist.quantile(0.99), 3e-1);//TODO: fail occassionally
    }

    @Test
    public void test_0030() {
        ADFFiniteSampleDistribution dist =
                new ADFFiniteSampleDistribution(100, TrendType.CONSTANT_TIME,
                false, 0, 50, 1000);

        assertEquals(-4.04, dist.quantile(0.01), 3e-1);
        assertEquals(-3.73, dist.quantile(0.025), 3e-1);
        assertEquals(-3.45, dist.quantile(0.05), 3e-1);
        assertEquals(-3.15, dist.quantile(0.1), 3e-1);
        assertEquals(-1.22, dist.quantile(0.9), 3e-1);
        assertEquals(-0.90, dist.quantile(0.95), 3e-1);
        assertEquals(-0.62, dist.quantile(0.975), 3e-1);
        assertEquals(-0.28, dist.quantile(0.99), 3e-1);
    }

//    @Test
    public void generate_DF_table() {
        final int firstSampleLenght = 10;
        final int next = 1;
        final int lastSampleLenght = 100;
        final double step = 0.01;

        int nRows = 1 + (lastSampleLenght - firstSampleLenght) / next;
        int nCols = 1 + (int) Math.round(1.0 / step);//TODO: get rid of (int) cast
        double[][] table = new double[nRows][nCols];

        for (int sampleSize = firstSampleLenght, i = 0; sampleSize <= lastSampleLenght; sampleSize += next, ++i) {
            System.out.println("sampleSize = " + sampleSize);
            ADFFiniteSampleDistribution dist =
                    new ADFFiniteSampleDistribution(sampleSize, TrendType.NO_CONSTANT,
                    false, 0, Math.min(sampleSize / 10, 50),//0 lag
                    10000);//number of simulations

            int j = 0;
            table[i][j++] = sampleSize;
            for (double x = step; x < 1.0000000001; x += step, ++j) {
                if (x > 1) {
                    x = 1;
                }
                table[i][j] = dist.quantile(x);
            }
        }

        String str = DoubleUtils.toString(table);
        System.out.println(str);
    }
}
