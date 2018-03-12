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
package com.numericalmethod.suanshu.stats.cointegration;

import com.numericalmethod.suanshu.stats.cointegration.JohansenAsymptoticDistribution.Test;
import com.numericalmethod.suanshu.stats.cointegration.JohansenAsymptoticDistribution.TrendType;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import static org.junit.Assert.*;

/**
 * The expected results are taken from Johansen (1995) and MacKinnon (1998).
 *
 * Care should be taken to choose the specification of the deterministic terms in the VECM used to construct Johansen cointegration tests.
 * This is particularly the case when the VECM includes UNRESTRICTED intercepts and/or trends.
 * See Turner (2009): Testing for cointegration using the Johansen approach: are we using the correct critical values?
 * Turner (2009) argues that
 * "the tendency of the Johansen test to reject a null of no cointegration may be due to the use of inappropriate critical values" and
 * suggests to "MacKinnon et al.'s (1999) estimates of the Pesaran et al.'s (2000) classification".
 *
 * @author Kevin Sun
 */
public class JohansenAsymptoticDistributionTest {

    /**
     * no constant case: Johansen (1988) case 0; Pesaran et al. (2000) Case I
     */
    @org.junit.Test
    public void test_0010() {
        int nSim = 500;
        int nT = 500;
        int drMax = 12;

        double[] quantiles = new double[]{0.50, 0.75, 0.80, 0.85, 0.90, 0.95, 0.975, 0.99};
        DenseMatrix JohansenTable = new DenseMatrix(
                new double[][]{
                    {0.60, 1.56, 1.89, 2.32, 2.98, 4.14, 5.30, 7.02},
                    {5.47, 7.77, 8.41, 9.22, 10.35, 12.21, 13.94, 16.16},
                    {14.31, 17.85, 18.83, 20.00, 21.58, 24.08, 26.42, 29.19},
                    {27.14, 31.86, 33.10, 34.62, 36.58, 39.71, 42.59, 46.00},
                    {43.75, 49.71, 51.25, 53.11, 55.54, 59.24, 62.68, 66.71},
                    {64.37, 71.44, 73.27, 75.48, 78.30, 82.61, 86.36, 91.12},
                    {88.73, 97.02, 99.12, 101.67, 104.93, 109.93, 114.24, 119.58},
                    {116.81, 126.20, 128.61, 131.49, 135.16, 140.74, 145.80, 151.70},
                    {148.76, 159.27, 161.98, 165.22, 169.30, 175.47, 181.20, 187.82},
                    {184.44, 196.13, 199.11, 202.65, 207.21, 214.07, 220.00, 226.95},
                    {223.98, 236.82, 240.09, 243.88, 248.77, 256.23, 262.69, 270.47},
                    {266.92, 280.71, 284.36, 288.47, 293.83, 301.95, 309.08, 318.14}
                });

        DenseMatrix criticalValues = new DenseMatrix(drMax, quantiles.length);
        DenseMatrix ratio = new DenseMatrix(drMax, quantiles.length);
        for (int dr = 1; dr <= drMax; ++dr) {//rows
            JohansenAsymptoticDistribution dist = new JohansenAsymptoticDistribution(
                    Test.TRACE, TrendType.NO_CONSTANT, dr,
                    nSim, nT,
                    12345678L);

            for (int i = 1; i <= quantiles.length; ++i) {//columns
                criticalValues.set(dr, i, dist.quantile(quantiles[i - 1]));
                ratio.set(dr, i, criticalValues.get(dr, i) / JohansenTable.get(dr, i));//ratio of our results to Johansen results
            }
        }

        //We check whether the ratio of our results to Johansen results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * quantiles.length), drMax, quantiles.length),
                2e-1));
    }

    /**
     * restricted constant: Johansen (1988) case 1*; Pesaran et al. (2000) Case II
     */
    @org.junit.Test
    public void test_0020() {
        int nSim = 500;
        int nT = 500;
        int drMax = 12;

        double[] quantiles = new double[]{0.50, 0.75, 0.80, 0.85, 0.90, 0.95, 0.975, 0.99};
        DenseMatrix JohansenTable = new DenseMatrix(
                new double[][]{
                    {3.43, 5.28, 5.85, 6.54, 7.50, 9.13, 10.73, 12.73},
                    {11.34, 14.45, 15.31, 16.39, 17.79, 19.99, 22.07, 24.74},
                    {23.15, 27.50, 28.65, 30.05, 31.88, 34.80, 37.56, 40.84},
                    {38.90, 44.39, 45.86, 47.60, 49.92, 53.42, 56.57, 60.42},
                    {58.47, 65.19, 66.92, 69.01, 71.66, 75.74, 79.60, 83.93},
                    {81.85, 89.71, 91.75, 94.13, 97.17, 101.84, 106.07, 111.38},
                    {109.11, 118.12, 120.52, 123.26, 126.71, 132.00, 136.69, 142.34},
                    {140.01, 150.10, 152.75, 155.86, 159.74, 165.73, 171.18, 177.42},
                    {174.65, 185.99, 188.92, 192.27, 196.66, 203.34, 209.28, 216.08},
                    {213.14, 225.67, 228.81, 232.63, 237.35, 244.56, 250.86, 258.31},
                    {255.33, 269.01, 272.44, 276.46, 281.63, 289.71, 296.70, 304.89},
                    {315.69, 319.50, 323.90, 329.51, 333.26, 338.10, 345.77, 354.32}
                });

        DenseMatrix criticalValues = new DenseMatrix(drMax, quantiles.length);
        DenseMatrix ratio = new DenseMatrix(drMax, quantiles.length);
        for (int dr = 1; dr <= drMax; ++dr) {//rows
            JohansenAsymptoticDistribution dist = new JohansenAsymptoticDistribution(
                    Test.TRACE, TrendType.RESTRICTED_CONSTANT, dr,
                    nSim, nT,
                    123456);

            for (int i = 1; i <= quantiles.length; ++i) {//columns
                criticalValues.set(dr, i, dist.quantile(quantiles[i - 1]));
                ratio.set(dr, i, criticalValues.get(dr, i) / JohansenTable.get(dr, i));//ratio of our results to Johansen results
            }
        }

        //We check whether the ratio of our results to Johansen results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * quantiles.length), drMax, quantiles.length),
                2e-1));
    }

    /**
     * constant with restricted linear trend: Johansen (1988) case 2*; Pesaran et al. (2000) Case IV
     */
    @org.junit.Test
    public void test_0030() {
        int nSim = 500;
        int nT = 500;
        int drMax = 12;

        double[] quantiles = new double[]{0.50, 0.75, 0.80, 0.85, 0.90, 0.95, 0.975, 0.99};
        DenseMatrix JohansenTable = new DenseMatrix(
                new double[][]{
                    {5.62, 7.92, 8.59, 9.43, 10.56, 12.39, 14.13, 16.39},
                    {15.68, 19.24, 20.22, 21.40, 22.95, 25.47, 27.87, 30.65},
                    {29.56, 34.30, 35.58, 37.11, 39.08, 42.20, 45.04, 48.59},
                    {47.19, 53.16, 54.73, 56.56, 58.96, 62.61, 66.04, 70.22},
                    {68.70, 75.87, 77.65, 79.85, 82.68, 86.96, 90.87, 95.38},
                    {93.88, 102.16, 104.29, 106.79, 110.00, 114.96, 119.43, 124.61},
                    {122.99, 132.35, 134.77, 137.55, 141.31, 146.75, 151.70, 157.53},
                    {155.60, 166.08, 168.85, 172.10, 176.13, 182.45, 187.80, 194.12},
                    {192.11, 203.70, 206.64, 210.21, 214.72, 221.56, 227.61, 234.65},
                    {232.31, 245.06, 248.24, 252.18, 257.08, 264.23, 270.90, 278.80},
                    {290.06, 293.60, 297.73, 302.88, 306.47, 311.13, 318.03, 326.73},
                    {338.46, 342.33, 346.88, 352.61, 356.39, 361.07, 368.75, 377.54}
                });

        DenseMatrix criticalValues = new DenseMatrix(drMax, quantiles.length);
        DenseMatrix ratio = new DenseMatrix(drMax, quantiles.length);
        for (int dr = 1; dr <= drMax; ++dr) {//rows
            JohansenAsymptoticDistribution dist = new JohansenAsymptoticDistribution(
                    Test.TRACE, TrendType.CONSTANT_RESTRICTED_TIME, dr,
                    nSim, nT,
                    123456);

            for (int i = 1; i <= quantiles.length; ++i) {//columns
                criticalValues.set(dr, i, dist.quantile(quantiles[i - 1]));
                ratio.set(dr, i, criticalValues.get(dr, i) / JohansenTable.get(dr, i));//ratio of our results to Johansen results
            }
        }

        //We check whether the ratio of our results to Johansen results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * quantiles.length), drMax, quantiles.length),
                2e-1));
    }

    /**
     * Pesaran et al. (2000) Case I; values are obtained from MacKinnon (1998)
     */
    @org.junit.Test
    public void test_0040() {
        int nSim = 250;
        int nT = 250;
        int drMax = 12;
        double quantile = 0.95;

        DenseMatrix Table = new DenseMatrix(
                new double[][]{
                    {4.13, 11.23, 17.80, 24.16, 30.42, 36.61, 42.76, 48.87, 54.97, 61.04, 67.06, 73.10},
                    {4.13, 12.32, 24.28, 40.17, 60.06, 83.94, 111.79, 143.64, 179.48, 219.38, 263.25, 311.09}
                });
        DenseMatrix criticalValues = new DenseMatrix(2, drMax);
        DenseMatrix ratio = new DenseMatrix(2, drMax);
        for (int dr = 1; dr <= drMax; ++dr) {
            TrendType trend_type = TrendType.NO_CONSTANT;
            JohansenAsymptoticDistribution dist_1 = new JohansenAsymptoticDistribution(
                    Test.EIGEN, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            JohansenAsymptoticDistribution dist_2 = new JohansenAsymptoticDistribution(
                    Test.TRACE, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            criticalValues.set(1, dr, dist_1.quantile(quantile));
            criticalValues.set(2, dr, dist_2.quantile(quantile));
            ratio.set(1, dr, criticalValues.get(1, dr) / Table.get(1, dr));//ratio of our results to MacKinnon's results
            ratio.set(2, dr, criticalValues.get(2, dr) / Table.get(2, dr));
        }

        //We check whether the ratio of our results to MacKinnon's results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * 2), 2, drMax),
                2e-1));
    }

    /**
     * Pesaran et al. (2000) Case II; values are obtained from MacKinnon (1998)
     */
    @org.junit.Test
    public void test_0050() {
        int nSim = 250;
        int nT = 250;
        int drMax = 12;
        double quantile = 0.95;

        DenseMatrix Table = new DenseMatrix(
                new double[][]{
                    {9.17, 15.88, 22.30, 28.58, 34.80, 40.95, 47.06, 53.15, 59.26, 65.30, 71.33, 77.35},
                    {9.17, 20.25, 35.19, 54.09, 76.96, 103.84, 134.70, 169.54, 208.41, 251.31, 298.16, 348.98}
                });
        DenseMatrix criticalValues = new DenseMatrix(2, drMax);
        DenseMatrix ratio = new DenseMatrix(2, drMax);
        for (int dr = 1; dr <= drMax; ++dr) {
            TrendType trend_type = TrendType.RESTRICTED_CONSTANT;
            JohansenAsymptoticDistribution dist_1 = new JohansenAsymptoticDistribution(
                    Test.EIGEN, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            JohansenAsymptoticDistribution dist_2 = new JohansenAsymptoticDistribution(
                    Test.TRACE, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            criticalValues.set(1, dr, dist_1.quantile(quantile));
            criticalValues.set(2, dr, dist_2.quantile(quantile));
            ratio.set(1, dr, criticalValues.get(1, dr) / Table.get(1, dr));//ratio of our results to MacKinnon's results
            ratio.set(2, dr, criticalValues.get(2, dr) / Table.get(2, dr));
        }

        //We check whether the ratio of our results to MacKinnon's results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * 2), 2, drMax),
                2e-1));
    }

    /**
     * Pesaran et al. (2000) Case III; values are obtained from MacKinnon (1998)
     */
    @org.junit.Test
    public void test_0060() {
        int nSim = 250;
        int nT = 250;
        int drMax = 12;
        double quantile = 0.95;

        DenseMatrix Table = new DenseMatrix(
                new double[][]{
                    {8.19, 15.02, 21.49, 27.80, 34.03, 40.19, 46.31, 52.41, 58.51, 64.56, 70.59, 76.61},
                    {8.19, 18.11, 31.88, 49.64, 71.44, 97.26, 127.05, 160.87, 198.72, 240.58, 286.39, 336.22}
                });
        DenseMatrix criticalValues = new DenseMatrix(2, drMax);
        DenseMatrix ratio = new DenseMatrix(2, drMax);
        for (int dr = 1; dr <= drMax; ++dr) {
            TrendType trend_type = TrendType.CONSTANT;
            JohansenAsymptoticDistribution dist_1 = new JohansenAsymptoticDistribution(
                    Test.EIGEN, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            JohansenAsymptoticDistribution dist_2 = new JohansenAsymptoticDistribution(
                    Test.TRACE, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            criticalValues.set(1, dr, dist_1.quantile(quantile));
            criticalValues.set(2, dr, dist_2.quantile(quantile));
            ratio.set(1, dr, criticalValues.get(1, dr) / Table.get(1, dr));//ratio of our results to MacKinnon's results
            ratio.set(2, dr, criticalValues.get(2, dr) / Table.get(2, dr));
        }

        //We check whether the ratio of our results to MacKinnon's results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * 2), 2, drMax),
                2e-1));
    }

    /**
     * Pesaran et al. (2000) Case IV; values are obtained from MacKinnon (1998)
     */
    @org.junit.Test
    public void test_0070() {
        int nSim = 250;
        int nT = 250;
        int drMax = 12;
        double quantile = 0.95;

        DenseMatrix Table = new DenseMatrix(
                new double[][]{
                    {12.52, 19.38, 25.83, 32.12, 38.32, 44.47, 50.58, 56.68, 62.75, 68.81, 74.83, 80.84},
                    {12.52, 25.86, 42.92, 63.87, 88.79, 117.69, 150.55, 187.44, 228.32, 273.20, 322.03, 374.84}
                });
        DenseMatrix criticalValues = new DenseMatrix(2, drMax);
        DenseMatrix ratio = new DenseMatrix(2, drMax);
        for (int dr = 1; dr <= drMax; ++dr) {
            TrendType trend_type = TrendType.CONSTANT_RESTRICTED_TIME;
            JohansenAsymptoticDistribution dist_1 = new JohansenAsymptoticDistribution(
                    Test.EIGEN, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            JohansenAsymptoticDistribution dist_2 = new JohansenAsymptoticDistribution(
                    Test.TRACE, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            criticalValues.set(1, dr, dist_1.quantile(quantile));
            criticalValues.set(2, dr, dist_2.quantile(quantile));
            ratio.set(1, dr, criticalValues.get(1, dr) / Table.get(1, dr));//ratio of our results to MacKinnon's results
            ratio.set(2, dr, criticalValues.get(2, dr) / Table.get(2, dr));
        }

        //We check whether the ratio of our results to MacKinnon's results are more or less the same (close to 1).
        assertTrue(AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * 2), 2, drMax),
                2e-1));
    }

    /**
     * Pesaran et al. (2000) Case V; values are obtained from MacKinnon (1998)
     */
    @org.junit.Test
    public void test_0080() {
        int nSim = 250;
        int nT = 250;
        int drMax = 12;
        double quantile = 0.95;

        DenseMatrix Table = new DenseMatrix(
                new double[][]{
                    {11.64, 18.55, 25.03, 31.34, 37.55, 43.71, 49.83, 55.92, 62.01, 68.07, 74.10, 80.11},
                    {11.64, 23.94, 39.92, 59.79, 83.63, 111.45, 143.29, 179.08, 218.91, 262.76, 310.55, 362.35}
                });
        DenseMatrix criticalValues = new DenseMatrix(2, drMax);
        DenseMatrix ratio = new DenseMatrix(2, drMax);
        for (int dr = 1; dr <= drMax; ++dr) {
            TrendType trend_type = TrendType.CONSTANT_TIME;
            JohansenAsymptoticDistribution dist_1 = new JohansenAsymptoticDistribution(
                    Test.EIGEN, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            JohansenAsymptoticDistribution dist_2 = new JohansenAsymptoticDistribution(
                    Test.TRACE, trend_type, dr,
                    dr <= 2 ? Math.max(5000, nSim) : nSim, nT,
                    123456);
            criticalValues.set(1, dr, dist_1.quantile(quantile));
            criticalValues.set(2, dr, dist_2.quantile(quantile));
            ratio.set(1, dr, criticalValues.get(1, dr) / Table.get(1, dr));//ratio of our results to MacKinnon's results
            ratio.set(2, dr, criticalValues.get(2, dr) / Table.get(2, dr));
        }

        //We check whether the ratio of our results to MacKinnon's results are more or less the same (close to 1).
        assertTrue(
                AreMatrices.equal(
                ratio,
                new DenseMatrix(R.rep(1., drMax * 2), 2, drMax),
                2e-1));
    }
////    @org.junit.Test
//    public void test_0100() {
//        final int nSim = 1000;
//        final int nT = 1000;
//        final int drMax = 12;
//        final double[] quantiles = new double[]{0.50, 0.75, 0.80, 0.85, 0.90, 0.95, 0.975, 0.99};
//        final Test spec = Test.TRACE;
//
//        //change the TrendType here for different tables
//        //        TrendType type = TrendType.NO_CONSTANT;
//        TrendType type = TrendType.RESTRICTED_CONSTANT;
//        //        TrendType type = TrendType.CONSTANT;
//        //        TrendType type = TrendType.CONSTANT_RESTRICTED_TIME;
//        //        TrendType type = TrendType.CONSTANT_TIME;
//
//        DenseMatrix criticalValues = new DenseMatrix(drMax, quantiles.length);
//        JohansenAsymptoticDistribution dist;
//        for (int dr = 1; dr <= drMax; ++dr) {
//            if (dr <= 2) {
//                dist = new JohansenAsymptoticDistribution(
//                        spec, type, dr,
//                        Math.max(5000, nSim), nT); //change the TrendType for tables
//            } else {
//                dist = new JohansenAsymptoticDistribution(
//                        spec, type, dr,
//                        nSim, nT); //change the TrendType for tables
//            }
//
//            for (int i = 1; i <= criticalValues.nCols(); ++i) {
//                criticalValues.set(dr, i, dist.quantile(quantiles[i - 1]));
//            }
//        }
//
//        System.out.println(criticalValues.toString());
//    }
}
