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
package com.numericalmethod.suanshu.stats.descriptive.rank;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class QuantileTest {

    @Test
    public void testQuantile_0010() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.INVERSE_OF_EMPIRICAL_CDF);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.1), 0);
        assertEquals(2, stat.value(0.15), 0);
        assertEquals(2, stat.value(0.2), 0);
        assertEquals(3, stat.value(0.25), 0);
        assertEquals(3, stat.value(0.3), 0);
        assertEquals(4, stat.value(0.35), 0);
        assertEquals(4, stat.value(0.4), 0);
        assertEquals(5, stat.value(0.45), 0);
        assertEquals(5, stat.value(0.5), 0);
        assertEquals(6, stat.value(0.55), 0);
        assertEquals(6, stat.value(0.6), 0);
        assertEquals(7, stat.value(0.65), 0);
        assertEquals(7, stat.value(0.7), 0);
        assertEquals(8, stat.value(0.75), 0);
        assertEquals(8, stat.value(0.8), 0);
        assertEquals(9, stat.value(0.85), 0);
        assertEquals(9, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0020() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.INVERSE_OF_EMPIRICAL_CDF_WITH_AVERAGING_AT_DISCONTINUITIES);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1.5, stat.value(0.1), 0);
        assertEquals(2, stat.value(0.15), 0);
        assertEquals(2.5, stat.value(0.2), 0);
        assertEquals(3, stat.value(0.25), 0);
        assertEquals(3.5, stat.value(0.3), 0);
        assertEquals(4, stat.value(0.35), 0);
        assertEquals(4.5, stat.value(0.4), 0);
        assertEquals(5, stat.value(0.45), 0);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(6, stat.value(0.55), 0);
        assertEquals(6.5, stat.value(0.6), 0);
        assertEquals(7, stat.value(0.65), 0);
        assertEquals(7.5, stat.value(0.7), 0);
        assertEquals(8, stat.value(0.75), 0);
        assertEquals(8.5, stat.value(0.8), 0);
        assertEquals(9, stat.value(0.85), 0);
        assertEquals(9.5, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0030() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.NEAREST_EVEN_ORDER_STATISTICS);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1, stat.value(0.1), 0);
        assertEquals(2, stat.value(0.15), 0);
        assertEquals(2, stat.value(0.2), 0);
        assertEquals(2, stat.value(0.25), 0);
        assertEquals(3, stat.value(0.3), 0);
        assertEquals(4, stat.value(0.35), 0);
        assertEquals(4, stat.value(0.4), 0);
        assertEquals(4, stat.value(0.45), 0);
        assertEquals(5, stat.value(0.5), 0);
        assertEquals(6, stat.value(0.55), 0);
        assertEquals(6, stat.value(0.6), 0);
        assertEquals(6, stat.value(0.65), 0);
        assertEquals(7, stat.value(0.7), 0);
        assertEquals(8, stat.value(0.75), 0);
        assertEquals(8, stat.value(0.8), 0);
        assertEquals(8, stat.value(0.85), 0);
        assertEquals(9, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0040() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.LINEAR_INTERPOLATION_OF_EMPIRICAL_CDF);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1, stat.value(0.1), 0);
        assertEquals(1.5, stat.value(0.15), 0);
        assertEquals(2, stat.value(0.2), 0);
        assertEquals(2.5, stat.value(0.25), 0);
        assertEquals(3, stat.value(0.3), 0);
        assertEquals(3.5, stat.value(0.35), 0);
        assertEquals(4, stat.value(0.4), 0);
        assertEquals(4.5, stat.value(0.45), 0);
        assertEquals(5, stat.value(0.5), 0);
        assertEquals(5.5, stat.value(0.55), 0);
        assertEquals(6, stat.value(0.6), 0);
        assertEquals(6.5, stat.value(0.65), 0);
        assertEquals(7, stat.value(0.7), 0);
        assertEquals(7.5, stat.value(0.75), 0);
        assertEquals(8, stat.value(0.8), 0);
        assertEquals(8.5, stat.value(0.85), 0);
        assertEquals(8.8, stat.value(0.88), 0);
        assertEquals(9, stat.value(0.9), 0);
        assertEquals(9.5, stat.value(0.95), 0);
        assertEquals(9.9, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0050() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.MIDWAY_THROUGH_STEPS_OF_EMPIRICAL_CDF);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1.5, stat.value(0.1), 0);
        assertEquals(2, stat.value(0.15), 0);
        assertEquals(2.5, stat.value(0.2), 0);
        assertEquals(3, stat.value(0.25), 0);
        assertEquals(3.5, stat.value(0.3), 0);
        assertEquals(4, stat.value(0.35), 0);
        assertEquals(4.5, stat.value(0.4), 0);
        assertEquals(5, stat.value(0.45), 0);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(6, stat.value(0.55), 0);
        assertEquals(6.5, stat.value(0.6), 0);
        assertEquals(7, stat.value(0.65), 0);
        assertEquals(7.5, stat.value(0.7), 0);
        assertEquals(8, stat.value(0.75), 0);
        assertEquals(8.2, stat.value(0.77), 0);
        assertEquals(8.5, stat.value(0.8), 0);
        assertEquals(9, stat.value(0.85), 0);
        assertEquals(9.3, stat.value(0.88), 0);
        assertEquals(9.5, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0060() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.MINITAB_SPSS);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1.1, stat.value(0.1), 0);
        assertEquals(1.65, stat.value(0.15), 0);
        assertEquals(2.2, stat.value(0.2), 0);
        assertEquals(2.75, stat.value(0.25), 0);
        assertEquals(3.3, stat.value(0.3), 0);
        assertEquals(3.85, stat.value(0.35), 0);
        assertEquals(4.4, stat.value(0.4), 0);
        assertEquals(4.95, stat.value(0.45), 0);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(6.05, stat.value(0.55), 0);
        assertEquals(6.6, stat.value(0.6), 0);
        assertEquals(7.15, stat.value(0.65), 0);
        assertEquals(7.7, stat.value(0.7), 0);
        assertEquals(8.25, stat.value(0.75), 0);
        assertEquals(8.47, stat.value(0.77), 0);
        assertEquals(8.8, stat.value(0.8), 0);
        assertEquals(9.35, stat.value(0.85), 0);
        assertEquals(9.68, stat.value(0.88), 1e-14);
        assertEquals(9.9, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0070() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.S);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 1e-14);
        assertEquals(1.00009, stat.value(0.00001), 1e-14);
        assertEquals(1.018, stat.value(0.002), 0);
        assertEquals(1.9, stat.value(0.1), 0);
        assertEquals(2.35, stat.value(0.15), 0);
        assertEquals(2.8, stat.value(0.2), 0);
        assertEquals(3.25, stat.value(0.25), 0);
        assertEquals(3.7, stat.value(0.3), 0);
        assertEquals(4.15, stat.value(0.35), 0);
        assertEquals(4.6, stat.value(0.4), 0);
        assertEquals(5.05, stat.value(0.45), 0);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(5.95, stat.value(0.55), 0);
        assertEquals(6.4, stat.value(0.6), 0);
        assertEquals(6.85, stat.value(0.65), 0);
        assertEquals(7.3, stat.value(0.7), 0);
        assertEquals(7.75, stat.value(0.75), 0);
        assertEquals(7.93, stat.value(0.77), 0);
        assertEquals(8.2, stat.value(0.8), 0);
        assertEquals(8.65, stat.value(0.85), 0);
        assertEquals(8.92, stat.value(0.88), 0);
        assertEquals(9.1, stat.value(0.9), 0);
        assertEquals(9.55, stat.value(0.95), 0);
        assertEquals(9.91, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0080() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.APPROXIMATELY_UNBIASED_IF_DATA_IS_NORMAL);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1.4, stat.value(0.1), 0);
        assertEquals(1.9125, stat.value(0.15), 0);
        assertEquals(2.425, stat.value(0.2), 0);
        assertEquals(2.9375, stat.value(0.25), 0);
        assertEquals(3.45, stat.value(0.3), 0);
        assertEquals(3.9625, stat.value(0.35), 0);
        assertEquals(4.475, stat.value(0.4), 0);
        assertEquals(4.9875, stat.value(0.45), 0);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(6.0125, stat.value(0.55), 0);
        assertEquals(6.525, stat.value(0.6), 0);
        assertEquals(7.0375, stat.value(0.65), 0);
        assertEquals(7.55, stat.value(0.7), 0);
        assertEquals(8.0625, stat.value(0.75), 0);
        assertEquals(8.2675, stat.value(0.77), 0);
        assertEquals(8.5750, stat.value(0.8), 0);
        assertEquals(9.0875, stat.value(0.85), 0);
        assertEquals(9.395, stat.value(0.88), 1e-14);
        assertEquals(9.6, stat.value(0.9), 0);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    @Test
    public void testQuantile_0090() {
        Quantile stat = new Quantile(new double[]{5, 4, 3, 2, 1},
                Quantile.QuantileType.APPROXIMATELY_MEDIAN_UNBIASED);
        stat.addData(new double[]{10, 9, 8, 7, 6});
        assertEquals(10, stat.N());

        assertEquals(1, stat.value(0.0000000000000001), 0);
        assertEquals(1, stat.value(0.00001), 0);
        assertEquals(1, stat.value(0.002), 0);
        assertEquals(1.366666666666666, stat.value(0.1), 1e-14);
        assertEquals(1.883333333333333, stat.value(0.15), 1e-14);
        assertEquals(2.4, stat.value(0.2), 0);
        assertEquals(2.916666666666667, stat.value(0.25), 1e-14);
        assertEquals(3.433333333333334, stat.value(0.3), 1e-14);
        assertEquals(3.95, stat.value(0.35), 0);
        assertEquals(4.466666666666666, stat.value(0.4), 1e-14);
        assertEquals(4.983333333333333, stat.value(0.45), 1e-14);
        assertEquals(5.5, stat.value(0.5), 0);
        assertEquals(6.016666666666667, stat.value(0.55), 1e-14);
        assertEquals(6.533333333333332, stat.value(0.6), 1e-14);
        assertEquals(7.05, stat.value(0.65), 0);
        assertEquals(7.566666666666666, stat.value(0.7), 1e-14);
        assertEquals(8.083333333333332, stat.value(0.75), 1e-14);
        assertEquals(8.29, stat.value(0.77), 1e-14);
        assertEquals(8.6, stat.value(0.8), 0);
        assertEquals(9.116666666666665, stat.value(0.85), 1e-14);
        assertEquals(9.426666666666667, stat.value(0.88), 1e-14);
        assertEquals(9.633333333333333, stat.value(0.9), 1e-14);
        assertEquals(10, stat.value(0.95), 0);
        assertEquals(10, stat.value(0.99), 0);
        assertEquals(10, stat.value(1), 0);
    }

    /**
     * with repeated observations
     */
    @Test
    public void testQuantile_0100() {
        Quantile stat = new Quantile(
                new double[]{0, 1, 2, 3, 3, 3, 6, 7, 8, 9},
                Quantile.QuantileType.APPROXIMATELY_MEDIAN_UNBIASED);
        assertEquals(10, stat.N());

        assertEquals(0, stat.value(0.0000000000000001), 0);
        assertEquals(0.3666666666666665, stat.value(0.1), 1e-15);
        assertEquals(0.883333333333333, stat.value(0.15), 1e-14);
        assertEquals(1.4, stat.value(0.2), 1e-14);
        assertEquals(2.433333333333333, stat.value(0.3), 1e-14);
        assertEquals(3, stat.value(0.4), 1e-14);
        assertEquals(3, stat.value(0.5), 1e-14);
        assertEquals(4.6, stat.value(0.6), 1e-14);
        assertEquals(6.566666666666665, stat.value(0.7), 1e-14);
        assertEquals(7.6, stat.value(0.8), 1e-14);
        assertEquals(8.63333333333333, stat.value(0.9), 1e-14);
        assertEquals(9, stat.value(0.95), 1e-14);
        assertEquals(9, stat.value(1.0), 1e-14);
    }
}
