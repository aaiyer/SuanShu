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
package com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles;

import com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles.MovingAverage.Side;
import com.numericalmethod.suanshu.misc.R;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MovingAverageTest {

    /**
    x <- 1:10
    filter(x, rep(1, 3))
     */
    @Test
    public void test_0010() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1, 1, 1};//rep(1,3)

        MovingAverage instance = new MovingAverage(MAFilter, Side.SYMMETRIC_WINDOW);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, 6, 9, 12, 15, 18, 21, 24, 27, Double.NaN};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    x <- 1:10
    filter(x, rep(1, 3), side=1)
     */
    @Test
    public void test_0020() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1, 1, 1};//rep(1,3)

        MovingAverage instance = new MovingAverage(MAFilter, Side.PAST);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, 6, 9, 12, 15, 18, 21, 24, 27};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    x <- 1:10
    filter(x, rep(1, 4))
     */
    @Test
    public void test_0030() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1, 1, 1, 1};//rep(1,4)

        MovingAverage instance = new MovingAverage(MAFilter, Side.SYMMETRIC_WINDOW);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, 10, 14, 18, 22, 26, 30, 34, Double.NaN, Double.NaN};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    x <- 1:10
    filter(x, rep(1, 4), side=1)
     */
    @Test
    public void test_0040() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1, 1, 1, 1};//rep(1,4)

        MovingAverage instance = new MovingAverage(MAFilter, Side.PAST);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, Double.NaN, 10, 14, 18, 22, 26, 30, 34};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    x <- 1:10
    filter(x, rep(1, 1), side=1)
     */
    @Test
    public void test_0050() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1};//rep(1,1)

        MovingAverage instance = new MovingAverage(MAFilter, Side.PAST);
        double[] result = instance.transform(Xt);

        assertArrayEquals(Xt, result, 1e-15);
    }

    /**
    x <- 1:10
    filter(x, rep(1, 1))
     */
    @Test
    public void test_0060() {
        double[] Xt = R.seq(1.0, 10, 1);
        double[] MAFilter = new double[]{1};//rep(1,1)

        MovingAverage instance = new MovingAverage(MAFilter, Side.SYMMETRIC_WINDOW);
        double[] result = instance.transform(Xt);

        assertArrayEquals(Xt, result, 1e-15);
    }

    /**
    filter(c(10,9,8,7,6,5,4,3,2,1), c(5,4,3,2,1), method="convolution", sides = 1)
     */
    @Test
    public void test_0070() {
        double[] Xt = new double[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        double[] MAFilter = new double[]{5, 4, 3, 2, 1};

        MovingAverage instance = new MovingAverage(MAFilter, Side.PAST);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN, 110, 95, 80, 65, 50, 35};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    filter(c(11,10,9,8,7,6,5,4,3,2,1), c(5,4,3,2,1), method="convolution", sides = 1)
     */
    @Test
    public void test_0080() {
        double[] Xt = new double[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        double[] MAFilter = new double[]{5, 4, 3, 2, 1};

        MovingAverage instance = new MovingAverage(MAFilter, Side.PAST);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN, 125, 110, 95, 80, 65, 50, 35};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    filter(c(10,9,8,7,6,5,4,3,2,1), c(5,4,3,2,1), method="convolution", sides = 2)
     */
    @Test
    public void test_0090() {
        double[] Xt = new double[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        double[] MAFilter = new double[]{5, 4, 3, 2, 1};

        MovingAverage instance = new MovingAverage(MAFilter, Side.SYMMETRIC_WINDOW);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, 110, 95, 80, 65, 50, 35, Double.NaN, Double.NaN};
        assertArrayEquals(expResult, result, 1e-15);
    }

    /**
    filter(c(11,10,9,8,7,6,5,4,3,2,1), c(5,4,3,2,1), method="convolution", sides = 2)
     */
    @Test
    public void test_0100() {
        double[] Xt = new double[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        double[] MAFilter = new double[]{5, 4, 3, 2, 1};

        MovingAverage instance = new MovingAverage(MAFilter, Side.SYMMETRIC_WINDOW);
        double[] result = instance.transform(Xt);

        double[] expResult = new double[]{Double.NaN, Double.NaN, 125, 110, 95, 80, 65, 50, 35, Double.NaN, Double.NaN};
        assertArrayEquals(expResult, result, 1e-15);
    }
}
