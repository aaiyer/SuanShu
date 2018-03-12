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
package com.numericalmethod.suanshu.stats.test.distribution.kolmogorov;

import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovTwoSamplesDistribution.Side;
import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class KolmogorovTwoSamplesDistributionTest {

    //<editor-fold defaultstate="collapsed" desc="tests for Side.EQUAL">
    /**
     * Test of class KolmogorovTwoSamplesDistribution.
     *
     * <p>
     * results from
     * p. 243
     * Section 6.3
     * Nonparametric Statistical Inference
     * 4th edition
     * by
     * Jean Dickinson Gibbons, Subhabrata Chakraborti
     * CRC
     */
    @Test
    public void test_cdf_EQUAL_0010() {
        assertEquals(
                1d - 23d / 35,
                (new KolmogorovTwoSamplesDistribution(
                new double[]{1, 2, 3},
                new double[]{4, 5, 6, 7},
                Side.EQUAL)).cdf(0.5),
                1e-15);
    }

    @Test
    public void test_cdf_EQUAL_0020() {
        assertEquals(
                1d - 23d / 35,
                (new KolmogorovTwoSamplesDistribution(3, 4, Side.EQUAL, 50)).cdf(0.5),
                1e-15);
    }

    @Test
    public void test_cdf_EQUAL_0030() {
        assertEquals(
                0,
                (new KolmogorovTwoSamplesDistribution(249, 79, Side.EQUAL, Integer.MAX_VALUE)).cdf(0),
                1e-15);
    }

    @Test
    public void test_cdf_EQUAL_0040() {
        assertEquals(
                1 - 0.1429,
                (new KolmogorovTwoSamplesDistribution(3, 5, Side.EQUAL, Integer.MAX_VALUE)).cdf(0.8),//from R, ks.test
                1e-4);
    }

    @Test
    public void test_cdf_EQUAL_0050() {
        assertEquals(
                1 - 1.027e-06,
                (new KolmogorovTwoSamplesDistribution(30, 5, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-5);
    }

    @Test
    public void test_cdf_EQUAL_0060() {
        assertEquals(
                1 - 0,
                (new KolmogorovTwoSamplesDistribution(30, 50, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-15);
    }

    @Test
    public void test_cdf_EQUAL_0070() {
        assertEquals(
                1 - 0.0001554,
                (new KolmogorovTwoSamplesDistribution(8, 7, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-3);
    }

    @Test
    public void test_cdf_EQUAL_0080() {
        assertEquals(
                1 - 0.0003232,
                (new KolmogorovTwoSamplesDistribution(12, 4, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-3);
    }

    @Test
    public void test_cdf_EQUAL_0085() {
        assertEquals(
                1 - 0.0003232,
                (new KolmogorovTwoSamplesDistribution(4, 12, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-3);
    }

    @Test
    public void test_cdf_EQUAL_0090() {
        assertEquals(
                1 - 0.05714,
                (new KolmogorovTwoSamplesDistribution(3, 4, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-5);
    }

    @Test
    public void test_cdf_EQUAL_0095() {
        assertEquals(
                1 - 0.05714,
                (new KolmogorovTwoSamplesDistribution(4, 3, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-5);
    }

    /**
     * Our result is different from that of R, because
     * 1. the way we check the boundary condition
     */
    @Test
    public void test_cdf_EQUAL_0100() {
        assertEquals(
                1 - 0.3142857,
                (new KolmogorovTwoSamplesDistribution(3, 4, Side.EQUAL, Integer.MAX_VALUE)).cdf(0.75),//from R, ks.test
                1e-5);
    }

    @Test
    public void test_cdf_EQUAL_0110() {
        assertEquals(
                1 - 0.9714,
                (new KolmogorovTwoSamplesDistribution(3, 4, Side.EQUAL, Integer.MAX_VALUE)).cdf(1d / 3),//from R, ks.test
                1e-4);
    }

    @Test
    public void test_cdf_EQUAL_0120() {
        assertEquals(
                1 - 0.9714,
                (new KolmogorovTwoSamplesDistribution(4, 3, Side.EQUAL, Integer.MAX_VALUE)).cdf(1d / 3),//from R, ks.test
                1e-4);
    }

    @Test
    public void test_cdf_EQUAL_0130() {
        assertEquals(
                1 - 0.2857,
                (new KolmogorovTwoSamplesDistribution(4, 5, Side.EQUAL, Integer.MAX_VALUE)).cdf(0.6),//from R, ks.test
                1e-4);
    }

    @Test
    public void test_cdf_EQUAL_0140() {
        assertEquals(
                1 - 0.001166,
                (new KolmogorovTwoSamplesDistribution(6, 7, Side.EQUAL, Integer.MAX_VALUE)).cdf(1),//from R, ks.test
                1e-6);
    }

    @Test
    public void test_cdf_EQUAL_0150() {
        assertEquals(
                1 - 0.008159,
                (new KolmogorovTwoSamplesDistribution(6, 7, Side.EQUAL, Integer.MAX_VALUE)).cdf(0.8571),//from R, ks.test
                1e-6);
    }

    /**
     * Test of class KolmogorovTwoSamplesDistribution.
     *
     * <p>
     * results from
     * p. 237, 246
     * Example 2.1, Table 3.1
     * Sections 6.2, 6.3
     * Nonparametric Statistical Inference
     * 4th edition
     * by
     * Jean Dickinson Gibbons, Subhabrata Chakraborti
     * CRC
     */
    @Test
    public void test_cdf_EQUAL_0160() {
        assertEquals(
                1d - 0.9801,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.EQUAL, 50)).cdf(0.25),//0.25 = 2/8
                1e-4);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for one-sided">
    /**
     * Test of class KolmogorovTwoSamplesDistribution.
     *
     * <p>
     * results from
     * p. 246
     * Table 3.1
     * Section 6.3
     * Nonparametric Statistical Inference
     * 4th edition
     * by
     * Jean Dickinson Gibbons, Subhabrata Chakraborti
     * CRC
     */
    @Test
    public void test_cdf_OneSided_0010() {
        assertEquals(
                1d - 0.6222,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.LESS, 50)).cdf(0.25),//0.25 = 2/8
                1e-4);
    }

    @Test
    public void test_cdf_OneSided_0020() {
        assertEquals(
                1d - 0.8889,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.GREATER, 50)).cdf(0.125),//0.125 = 1/8
                1e-4);
    }

    @Test
    public void test_cdf_OneSided_0030() {
        assertEquals(
                1d - 0.426,
                (new KolmogorovTwoSamplesDistribution(10, 15,
                Side.GREATER, 0)).cdf(0.2667),
                1e-4);
    }

    @Test
    public void test_cdf_OneSided_0040() {
        assertEquals(
                1d - 0.887,
                (new KolmogorovTwoSamplesDistribution(10, 15,
                Side.LESS, 0)).cdf(0.1),
                1e-4);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for asymptotic distribution">
    /**
     * Test of class KolmogorovTwoSamplesDistribution.
     *
     * <p>
     * results from
     * p. 246
     * Table 3.1
     * Section 6.3
     * Nonparametric Statistical Inference
     * 4th edition
     * by
     * Jean Dickinson Gibbons, Subhabrata Chakraborti
     * CRC
     */
    @Test
    public void test_cdf_Asymptotic_0010() {
        assertEquals(
                1d - 0.9639,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.EQUAL,
                0)).cdf(0.25),//0.25 = 2/8
                1e-4);
    }

    @Test
    public void test_cdf_Asymptotic_0020() {
        assertEquals(
                1d - 0.6065,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.LESS,
                0)).cdf(0.25),//0.25 = 2/8
                1e-4);
    }

    @Test
    public void test_cdf_Asymptotic_0030() {
        assertEquals(
                1d - 0.8825,
                (new KolmogorovTwoSamplesDistribution(8, 8,
                new double[]{1.91, 1.22, 0.96, 0.72, 0.14, 0.82, 1.45, 1.86,//from Normal
                    4.90, 7.25, 8.04, 14.10, 18.30, 21.21, 23.10, 28.12},//from Chi square
                Side.GREATER,
                0)).cdf(0.125),//0.125 = 1/8
                1e-4);
    }

    @Test
    public void test_cdf_Asymptotic_0040() {
        double v1 = (new KolmogorovTwoSamplesDistribution(100, 100, Side.EQUAL, Integer.MAX_VALUE)).cdf(0.25);
        double v2 = (new KolmogorovTwoSamplesDistribution(100, 100, Side.EQUAL, 0)).cdf(0.25);
        double diff = abs(v1 - v2);
        assertTrue(compare(1e-3, diff, 0) > 0);
    }
    //</editor-fold>
}
