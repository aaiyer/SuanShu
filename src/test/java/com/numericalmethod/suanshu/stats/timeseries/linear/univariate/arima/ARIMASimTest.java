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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima;

import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARMAModel;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.MAModel;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARModel;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.MersenneTwister;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ARIMASimTest {

    @Test
    public void test_0010() {
        assertTrue(true);
    }
//    @Test
//    public void test_0010() {
//        ARIMASim instance = new ARIMASim(10,
//                new ARIMAModel(new double[]{0.3, -0.02}, 1, new double[]{0.6}));
//        assertEquals(10, instance.size());
//    }
//
//    @Test
//    public void test_0020() {
//        ARIMASim instance = new ARIMASim(100,
//                new ARMAModel(new double[]{0.3, -0.02}, new double[]{0.6}));
//        assertEquals(100, instance.size());
//    }
//
//    @Test
//    public void test_0030() {
//        ARIMASim instance = new ARIMASim(1000,
//                new ARModel(new double[]{0.3, -0.02}));
//        assertEquals(1000, instance.size());
//    }
//
//    @Test
//    public void test_0040() {
//        ARIMASim instance = new ARIMASim(10000,
//                new MAModel(new double[]{0.6}));
//        assertEquals(10000, instance.size());
//    }
//
//    @Test
//    public void test_0050() {
//        ARIMASim instance = new ARIMASim(100000,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//        assertEquals(100000, instance.size());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void test_0060() {
//        ARIMASim instance = new ARIMASim(2,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//    }
//
//    @Test
//    public void test_0070() {
//        ARIMASim instance = new ARIMASim(10,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//        assertEquals(10, instance.size());
//    }
//
//    @Test
//    public void test_0080() {
//        ARIMASim instance = new ARIMASim(100,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//        assertEquals(100, instance.size());
//    }
//
//    @Test
//    public void test_0090() {
//        ARIMASim instance = new ARIMASim(10,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//        assertEquals(10, instance.size());
//    }
//
//    @Test
//    public void test_00100() {
//        ARIMASim instance = new ARIMASim(100,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}));
//        assertEquals(100, instance.size());
//    }
//
//    @Test
//    public void test_00110() {
//        RandomLongGenerator uniform = new MersenneTwister();
//
//        uniform.seed(10000000L);
//        ARIMASim instance1 = new ARIMASim(100,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}),
//                ARIMASim.getWhiteNoise(150));
//
//        uniform.seed(10000000L);
//        ARIMASim instance2 = new ARIMASim(100,
//                new ARIMAModel(new double[]{0.3, -0.02}, 0, new double[]{0.6}),
//                ARIMASim.getWhiteNoise(150));
//        assertEquals(instance1, instance2);
//    }
//
//    @Test
//    public void test_0120() {
//        final int n = 1000000;
//        double[] Z = ARIMASim.getWhiteNoise(n + 100);
//
//        ARIMASim arimaSim = new ARIMASim(n,
//                new ARIMAModel(0.05, new double[]{0.99}, 0, new double[]{0.6}),
//                Z);
//
//        double mu = new Mean(arimaSim.toArray()).value();
//
//        assertEquals(5, mu, 3e-2);//mean = 0.05 / (1 - 0.99)
//    }
}
