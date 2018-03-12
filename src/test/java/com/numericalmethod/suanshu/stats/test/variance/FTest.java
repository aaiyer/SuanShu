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
package com.numericalmethod.suanshu.stats.test.variance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class FTest {

    /**
     *
    x=c(1,3,5,2,3,5)
    y=c(2,5,6,4,9,8)
    test=var.test(x,y)
    var.test(x,y,alternative="greater")
    var.test(x,y,alternative="less")
    test$statistic
    test$p.value
    test$estimate
    test$conf.int
     */
    @Test
    public void test_0010() {
        double[] sample1 = new double[]{1, 3, 5, 2, 3, 5};
        double[] sample2 = new double[]{2, 5, 6, 4, 9, 8};

        F instance = new F(sample1, sample2);
        assertEquals(0.385, instance.statistics(), 1e-15);
        assertEquals(0.3182339203093393, instance.pValue(), 1e-15);

        assertEquals(0.8409, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.1591, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{0.05387341583849666, 2.75135700406213957}, instance.confidenceInterval(0.975), 1e-15);
        assertEquals(1.944376687188568, instance.leftConfidenceInterval(0.95), 1e-15);
    }

    /**
     * 
    x=c(1,8,3,4,9,9,7)
    y=c(2,1,3,4)
    test=var.test(x,y)
    var.test(x,y,alternative="greater")
    var.test(x,y,alternative="less")
    test$statistic
    test$p.value
    test$estimate
    test$conf.int
     */
    @Test
    public void test_0020() {
        double[] sample1 = new double[]{1, 8, 3, 4, 9, 9, 7};
        double[] sample2 = new double[]{2, 1, 3, 4};

        F instance = new F(sample1, sample2);
        assertEquals(6.085714285714285, instance.statistics(), 1e-12);
        assertEquals(0.166820963939726, instance.pValue(), 1e-15);

        assertEquals(0.08341, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.9166, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{0.4130187028432706, 40.1584024336208003}, instance.confidenceInterval(0.975), 1e-13);
        assertEquals(28.9501242068013, instance.leftConfidenceInterval(0.95), 1e-14);
    }

    /**
     * 
    x=c(-0.88516864, -0.88303844, -0.09166664, 0.16713331, -0.43235759, 1.37763060, -0.23668177, -0.38665639, 0.11952296, -1.46997598)
    y=c(3.2625111, 2.0730973, 1.8930992, -0.6001412, 2.0386785, 0.9333079, 0.4217831, 2.1588248, 1.4501559, 0.9206389)
    test=var.test(x,y,2)
    var.test(x,y,ratio=2,alternative="greater")
    var.test(x,y,ratio=2,alternative="less")
    test$statistic
    test$p.value
    test$estimate
    test$conf.int
     */
    @Test
    public void test_0030() {
        double[] sample1 = new double[]{
            -0.88516864, -0.88303844, -0.09166664, 0.16713331, -0.43235759,
            1.37763060, -0.23668177, -0.38665639, 0.11952296, -1.46997598};
        double[] sample2 = new double[]{
            3.2625111, 2.0730973, 1.8930992, -0.6001412, 2.0386785,
            0.9333079, 0.4217831, 2.1588248, 1.4501559, 0.9206389
        };

        F instance = new F(sample1, sample2, 2);
        assertEquals(0.252922852134327, instance.statistics(), 1e-14);
        assertEquals(0.05284810953665684, instance.pValue(), 1e-14);

        assertEquals(0.9736, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.02642, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{0.1256449175982881, 2.0365318503781404}, instance.confidenceInterval(0.975), 1e-13);
        assertEquals(1.608029421219462, instance.leftConfidenceInterval(0.95), 1e-14);
    }

    /**
     * 
    x=c(0,0,0,0,0,0,0,0,0,0)
    y=c(3.2625111, 2.0730973, 1.8930992, -0.6001412, 2.0386785, 0.9333079, 0.4217831, 2.1588248, 1.4501559, 0.9206389)
    test=var.test(x,y)
    var.test(x,y,alternative="greater")
    var.test(x,y,alternative="less")
    test$statistic
    test$p.value
    test$estimate
    test$conf.int
     */
    @Test
    public void test_0040() {
        double[] sample1 = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] sample2 = new double[]{
            3.2625111, 2.0730973, 1.8930992, -0.6001412, 2.0386785,
            0.9333079, 0.4217831, 2.1588248, 1.4501559, 0.9206389
        };

        F instance = new F(sample1, sample2);
        assertEquals(0, instance.statistics(), 1e-15);
        assertEquals(0, instance.pValue(), 1e-15);

        assertEquals(1, instance.pValue1SidedGreater, 1e-4);
        assertEquals(2.2e-16, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{0, 0}, instance.confidenceInterval(0.975), 1e-15);
        assertEquals(0, instance.leftConfidenceInterval(0.95), 1e-15);
    }
}
