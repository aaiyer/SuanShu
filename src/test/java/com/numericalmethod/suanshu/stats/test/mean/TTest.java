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
package com.numericalmethod.suanshu.stats.test.mean;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class TTest {

    /**
     *
    x=c(1,3,5,2,3,5)
    y=c(2,5,6,4,9,8)
    t.test(x,y)
    t.test(x,y,"greater")
    t.test(x,y,"less")
     */
    @Test
    public void test_0010() {
        T instance = new T(
                new double[]{1, 3, 5, 2, 3, 5},
                new double[]{2, 5, 6, 4, 9, 8});

        assertEquals(-2.0153, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);
        assertEquals(5.666666666666667, instance.mean2, 1e-15);

        assertEquals(0.07713, instance.pValue(), 1e-5);
        assertEquals(0.9614, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.03857, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{-5.339736444764696, 0.339736444764695}, instance.confidenceInterval(0.975), 1e-15);
        assertEquals(-0.2057690276716359, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(-4.794230972328365, instance.rightConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    x=c(1,3,5,2,3,5)
    t.test(x)
    t.test(x,alternative="greater")
    t.test(x,alternative="less")
     */
    @Test
    public void test_0020() {
        T instance = new T(new double[]{1, 3, 5, 2, 3, 5}, 0);

        assertEquals(4.8416, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);

        assertEquals(0.004708, instance.pValue(), 1e-6);
        assertEquals(0.002354, instance.pValue1SidedGreater, 1e-6);
        assertEquals(0.9976, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{1.485384740121133, 4.847948593212200}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(4.4846034715695, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(1.848729861763831, instance.rightConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    x=c(1,3,5,2,3,5)
    t.test(x,mu=4)
    t.test(x,mu=4,alternative="greater")
    t.test(x,mu=4,alternative="less")
     */
    @Test
    public void test_0030() {
        T instance = new T(new double[]{1, 3, 5, 2, 3, 5}, 4);

        assertEquals(-1.2741, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);

        assertEquals(0.2586, instance.pValue(), 1e-4);
        assertEquals(0.8707, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.1293, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{1.485384740121133, 4.847948593212200}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(1.848729861763831, instance.rightConfidenceInterval(0.95), 1e-14);
        assertEquals(4.4846034715695, instance.leftConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    x=c(1,3,5,2,3,5)
    y=c(2,5,4,6,9,8)
    t.test(x,y,var.equal=T)
    t.test(x,y,var.equal=T,"greater")
    t.test(x,y,var.equal=T,"less")
     */
    @Test
    public void test_0040() {
        T instance = new T(
                new double[]{1, 3, 5, 2, 3, 5},
                new double[]{2, 5, 6, 4, 9, 8},
                true, 0);

        assertEquals(-2.0153, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);
        assertEquals(5.666666666666667, instance.mean2, 1e-15);

        assertEquals(0.07154, instance.pValue(), 1e-5);
        assertEquals(0.9642, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.03577, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{-5.264049927203647, 0.264049927203646}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(-0.2516064449479323, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(-4.74839355505207, instance.rightConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    options(digits=22)
    x=c(1,3,5,2,3,5)
    y=c(2,5,4,6,9,8)
    t.test(x,y,var.equal=F)
    t.test(x,y,var.equal=F,"greater")
    t.test(x,y,var.equal=F,"less")
     */
    @Test
    public void test_0050() {
        T instance = new T(
                new double[]{1, 3, 5, 2, 3, 5},
                new double[]{2, 5, 6, 4, 9, 8},
                false, 0);

        assertEquals(-2.0153, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);
        assertEquals(5.666666666666667, instance.mean2, 1e-15);

        assertEquals(0.07713, instance.pValue(), 1e-5);
        assertEquals(0.9614, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.03857, instance.pValue1SidedLess, 1e-4);

        assertArrayEquals(new double[]{-5.339736444764696, 0.339736444764695}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(-0.2057690276716359, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(-4.794230972328365, instance.rightConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    x=c(1,3,5,2,3,5)
    y=c(2,5,4,6,9,8)
    t.test(x,y,mu=4,var.equal=T)
    t.test(x,y,mu=4,var.equal=T,"greater")
    t.test(x,y,mu=4,var.equal=T,"less")
     */
    @Test
    public void test_0060() {
        T instance = new T(
                new double[]{1, 3, 5, 2, 3, 5},
                new double[]{2, 5, 6, 4, 9, 8},
                true, 4);

        assertEquals(-5.2397, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);
        assertEquals(5.666666666666667, instance.mean2, 1e-15);

        assertEquals(0.0003789, instance.pValue(), 1e-7);
        assertEquals(0.9998, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.0001895, instance.pValue1SidedLess, 1e-7);

        assertArrayEquals(new double[]{-5.264049927203647, 0.264049927203646}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(-0.2516064449479316, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(-4.74839355505207, instance.rightConfidenceInterval(0.95), 1e-14);
    }

    /**
     *
    x=c(1,3,5,2,3,5)
    y=c(2,5,4,6,9,8)
    t.test(x,y,mu=4,var.equal=F)
    t.test(x,y,mu=4,var.equal=F,"greater")
    t.test(x,y,mu=4,var.equal=F,"less")
     */
    @Test
    public void test_0070() {
        T instance = new T(
                new double[]{1, 3, 5, 2, 3, 5},
                new double[]{2, 5, 6, 4, 9, 8},
                false, 4);

        assertEquals(-5.2397, instance.statistics(), 1e-4);
        assertEquals(3.166666666666667, instance.mean1, 1e-15);
        assertEquals(5.666666666666667, instance.mean2, 1e-15);

        assertEquals(0.0006816, instance.pValue(), 1e-7);
        assertEquals(0.9997, instance.pValue1SidedGreater, 1e-4);
        assertEquals(0.0003408, instance.pValue1SidedLess, 1e-7);

        assertArrayEquals(new double[]{-5.339736444764696, 0.339736444764695}, instance.confidenceInterval(0.975), 1e-14);
        assertEquals(-0.2057690276716357, instance.leftConfidenceInterval(0.95), 1e-14);
        assertEquals(-4.794230972328364, instance.rightConfidenceInterval(0.95), 1e-14);
    }
}
