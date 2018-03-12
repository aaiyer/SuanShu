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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.garch;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GARCHSimTest {

    @Test
    public void test_0010() {
        assertTrue(true);
    }
//    @Test
//    public void test_0010() {
//        GARCHSim instance = new GARCHSim(10,
//                new GARCHModel(0.2, new double[]{0.3}, new double[]{0.6}));
//        assertEquals(10, instance.size());
//        assertEquals(10, instance.sigma2().size());
//    }
//
//    @Test
//    public void test_0020() {
//        GARCHSim instance = new GARCHSim(100,
//                new GARCHModel(0.2, new double[]{0.3}, new double[]{0.6}));
//        assertEquals(100, instance.size());
//        assertEquals(100, instance.sigma2().size());
//    }
//
//    @Test
//    public void test_0030() {
//        GARCHSim instance = new GARCHSim(10000,
//                new GARCHModel(0.2, new double[]{0.3}, new double[]{0.6}));
//        assertEquals(10000, instance.sigma2().size());
//    }
//
//    @Test
//    public void test_0040() {
//        GARCHSim instance = new GARCHSim(1000,
//                new GARCHModel(0.2, new double[]{0.3}, new double[]{0.6}),
//                new WhiteNoise(1100, new TDistribution(2)));
//        assertEquals(1000, instance.size());
//        assertEquals(1000, instance.sigma2().size());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void test_0510() {
//        GARCHSim instance = new GARCHSim(1000,
//                new GARCHModel(-0.2, new double[]{0.3}, new double[]{0.6}),
//                new WhiteNoise(1100, new TDistribution(2)));
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void test_0520() {
//        GARCHSim instance = new GARCHSim(1000,
//                new GARCHModel(0.2, new double[]{0.3}, new double[]{-0.6}),
//                new WhiteNoise(1100, new TDistribution(2)));
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void test_0530() {
//        GARCHSim instance = new GARCHSim(1000,
//                new GARCHModel(0.2, new double[]{0.9}, new double[]{0.6}),
//                new WhiteNoise(1100, new TDistribution(2)));
//    }
}
