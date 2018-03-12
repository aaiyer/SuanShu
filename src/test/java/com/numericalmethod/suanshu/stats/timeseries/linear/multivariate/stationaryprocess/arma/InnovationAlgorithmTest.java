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

import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class InnovationAlgorithmTest {

    /**
     * P. J. Brockwell and R. A. Davis, "Example 5.2.1. Chapter 5. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006.
     */
    @Test
    public void test_0010() {
        Matrix PHI = new DenseMatrix(new double[][]{
                    {0.7, 0},
                    {0, 0.6}
                });

        Matrix THETA = new DenseMatrix(new double[][]{
                    {0.5, 0.6},
                    {-0.7, 0.8}
                });

        Matrix SIGMA = new DenseMatrix(new double[][]{
                    {1, 0.71},
                    {0.71, 2}
                });

        MultiVariateTimeSeries Xt = new SimpleMultiVariateTimeSeries(new double[][]{
                    {-1.875, 1.693},
                    {-2.518, -0.03},
                    {-3.002, -1.057},
                    {-2.454, -1.038},
                    {-1.119, -1.086},
                    {-0.72, -0.455},
                    {-2.738, 0.962},
                    {-2.565, 1.992},
                    {-4.603, 2.434},
                    {-2.689, 2.118}
                });

        InnovationAlgorithm instance = new InnovationAlgorithm(Xt,
                new ARMAModel(new Matrix[]{PHI}, new Matrix[]{THETA}, SIGMA));

        Matrix V = instance.covariance(0);
        assertEquals(7.24, V.get(1, 1), 1e-3);
        assertEquals(3.701, V.get(1, 2), 1e-3);
        assertEquals(3.701, V.get(2, 1), 1e-3);
        assertEquals(6.716, V.get(2, 2), 1e-3);

        V = instance.covariance(1);
        assertEquals(2.035, V.get(1, 1), 1e-3);
        assertEquals(1.060, V.get(1, 2), 1e-3);
        assertEquals(1.060, V.get(2, 1), 1e-3);
        assertEquals(2.688, V.get(2, 2), 1e-3);

        V = instance.covariance(2);
        assertEquals(1.436, V.get(1, 1), 1e-3);
        assertEquals(0.777, V.get(1, 2), 1e-3);
        assertEquals(0.777, V.get(2, 1), 1e-3);
        assertEquals(2.323, V.get(2, 2), 1e-3);

        V = instance.covariance(3);
        assertEquals(1.215, V.get(1, 1), 1e-3);
        assertEquals(0.740, V.get(1, 2), 1e-3);
        assertEquals(0.740, V.get(2, 1), 1e-3);
        assertEquals(2.238, V.get(2, 2), 1e-3);

        V = instance.covariance(4);
        assertEquals(1.141, V.get(1, 1), 1e-3);
        assertEquals(0.750, V.get(1, 2), 1e-3);
        assertEquals(0.750, V.get(2, 1), 1e-3);
        assertEquals(2.177, V.get(2, 2), 1e-3);

        V = instance.covariance(5);
        assertEquals(1.113, V.get(1, 1), 1e-3);
        assertEquals(0.744, V.get(1, 2), 1e-3);
        assertEquals(0.744, V.get(2, 1), 1e-3);
        assertEquals(2.119, V.get(2, 2), 1e-3);

        V = instance.covariance(6);
        assertEquals(1.085, V.get(1, 1), 1e-3);
        assertEquals(0.728, V.get(1, 2), 1e-3);
        assertEquals(0.728, V.get(2, 1), 1e-3);
        assertEquals(2.084, V.get(2, 2), 1e-3);

        V = instance.covariance(7);
        assertEquals(1.059, V.get(1, 1), 1e-3);
        assertEquals(0.721, V.get(1, 2), 1e-3);
        assertEquals(0.721, V.get(2, 1), 1e-3);
        assertEquals(2.069, V.get(2, 2), 1e-3);

        V = instance.covariance(8);
        assertEquals(1.045, V.get(1, 1), 1e-3);
        assertEquals(0.722, V.get(1, 2), 1e-3);
        assertEquals(0.722, V.get(2, 1), 1e-3);
        assertEquals(2.057, V.get(2, 2), 1e-3);

        V = instance.covariance(9);
        assertEquals(1.038, V.get(1, 1), 1e-3);
        assertEquals(0.721, V.get(1, 2), 1e-3);
        assertEquals(0.721, V.get(2, 1), 1e-3);
        assertEquals(2.042, V.get(2, 2), 1e-3);

        V = instance.covariance(10);
        assertEquals(1.030, V.get(1, 1), 1e-3);
        assertEquals(0.717, V.get(1, 2), 1e-3);
        assertEquals(0.717, V.get(2, 1), 1e-3);
        assertEquals(2.032, V.get(2, 2), 1e-3);

        Matrix T = instance.theta(1, 1);
        assertEquals(0.013, T.get(1, 1), 1e-3);
        assertEquals(0.224, T.get(1, 2), 1e-3);
        assertEquals(-0.142, T.get(2, 1), 1e-3);
        assertEquals(0.243, T.get(2, 2), 1e-3);

        T = instance.theta(2, 1);
        assertEquals(0.193, T.get(1, 1), 1e-3);
        assertEquals(0.502, T.get(1, 2), 1e-3);
        assertEquals(-0.351, T.get(2, 1), 1e-3);
        assertEquals(0.549, T.get(2, 2), 1e-3);

        T = instance.theta(3, 1);
        assertEquals(0.345, T.get(1, 1), 1e-3);
        assertEquals(0.554, T.get(1, 2), 1e-3);
        assertEquals(-0.426, T.get(2, 1), 1e-3);
        assertEquals(0.617, T.get(2, 2), 1e-3);

        T = instance.theta(4, 1);
        assertEquals(0.424, T.get(1, 1), 1e-3);
        assertEquals(0.555, T.get(1, 2), 1e-3);
        assertEquals(-0.512, T.get(2, 1), 1e-3);
        assertEquals(0.662, T.get(2, 2), 1e-3);

        T = instance.theta(5, 1);
        assertEquals(0.442, T.get(1, 1), 1e-3);
        assertEquals(0.562, T.get(1, 2), 1e-3);
        assertEquals(-0.580, T.get(2, 1), 1e-3);
        assertEquals(0.707, T.get(2, 2), 1e-3);

        T = instance.theta(6, 1);
        assertEquals(0.446, T.get(1, 1), 1e-3);
        assertEquals(0.577, T.get(1, 2), 1e-3);
        assertEquals(-0.610, T.get(2, 1), 1e-3);
        assertEquals(0.735, T.get(2, 2), 1e-3);

        T = instance.theta(7, 1);
        assertEquals(0.461, T.get(1, 1), 1e-3);
        assertEquals(0.585, T.get(1, 2), 1e-3);
        assertEquals(-0.623, T.get(2, 1), 1e-3);
        assertEquals(0.747, T.get(2, 2), 1e-3);

        T = instance.theta(8, 1);
        assertEquals(0.475, T.get(1, 1), 1e-3);
        assertEquals(0.586, T.get(1, 2), 1e-3);
        assertEquals(-0.639, T.get(2, 1), 1e-3);
        assertEquals(0.756, T.get(2, 2), 1e-3);

        T = instance.theta(9, 1);
        assertEquals(0.480, T.get(1, 1), 1e-3);
        assertEquals(0.587, T.get(1, 2), 1e-3);
        assertEquals(-0.657, T.get(2, 1), 1e-3);
        assertEquals(0.767, T.get(2, 2), 1e-3);

        T = instance.theta(10, 1);
        assertEquals(0.481, T.get(1, 1), 1e-3);
        assertEquals(0.591, T.get(1, 2), 1e-3);
        assertEquals(-0.666, T.get(2, 1), 1e-3);
        assertEquals(0.775, T.get(2, 2), 1e-3);

        assertArrayEquals(new double[]{0, 0}, instance.XtHat(1).toArray(), 1e-15);
        assertArrayEquals(new double[]{-0.958, 1.693}, instance.XtHat(2).toArray(), 1e-3);
        assertArrayEquals(new double[]{-2.93, -0.417}, instance.XtHat(3).toArray(), 1e-3);
        assertArrayEquals(new double[]{-2.481, -1}, instance.XtHat(4).toArray(), 1e-2);
        assertArrayEquals(new double[]{-1.728, -0.662}, instance.XtHat(5).toArray(), 1e-3);
        assertArrayEquals(new double[]{0.001, 0.331}, instance.XtHat(7).toArray(), 1e-3);
        assertArrayEquals(new double[]{-2.809, 2.754}, instance.XtHat(8).toArray(), 1e-3);
        assertArrayEquals(new double[]{-2.126, 0.463}, instance.XtHat(9).toArray(), 1e-3);
        assertArrayEquals(new double[]{-3.254, 4.598}, instance.XtHat(10).toArray(), 1e-3);
        assertArrayEquals(new double[]{-3.077, -1.029}, instance.XtHat(11).toArray(), 1e-3);
    }
}
