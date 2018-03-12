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
package com.numericalmethod.suanshu.optimization.initialization;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.interval.RealInterval;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.abs;

/**
 *
 * @author Haksun Li
 */
public class UniformDistributionOverBox2Test {

    @Test
    public void test_0010() {
        UniformDistributionOverBox2 instance = new UniformDistributionOverBox2(
                0,
                new RealInterval[]{
                    new RealInterval(1.0, 4.0),//[1, 2, 3, 4]
                    new RealInterval(10.0, 14.0)//[10, 11, 12, 13, 14]
                },
                new int[]{3, 4});

        Vector[] grid = instance.getInitials();
        assertArrayEquals(new double[]{2.0, 11.0}, grid[0].toArray(), 0);
        assertArrayEquals(new double[]{2.0, 12.0}, grid[1].toArray(), 0);
        assertArrayEquals(new double[]{2.0, 13.0}, grid[2].toArray(), 0);
        assertArrayEquals(new double[]{3.0, 11.0}, grid[3].toArray(), 0);
        assertArrayEquals(new double[]{3.0, 12.0}, grid[4].toArray(), 0);
        assertArrayEquals(new double[]{3.0, 13.0}, grid[5].toArray(), 0);
    }

    @Test
    public void test_0020() {
        UniformDistributionOverBox2 instance = new UniformDistributionOverBox2(
                0,
                new RealInterval[]{
                    new RealInterval(0.0, 9.0),//3, 6
                    new RealInterval(10.0, 14.0)//12
                },
                new int[]{3, 2});

        Vector[] grid = instance.getInitials();
        assertArrayEquals(new double[]{3.0, 12.0}, grid[0].toArray(), 0);
        assertArrayEquals(new double[]{6.0, 12.0}, grid[1].toArray(), 0);
    }

    @Test
    public void test_0030() {
        double pct = 0.1;//10%

        UniformDistributionOverBox2 instance = new UniformDistributionOverBox2(
                pct,
                new RealInterval[]{
                    new RealInterval(0.0, 9.0),//3, 6
                    new RealInterval(10.0, 14.0)//12
                },
                new int[]{3, 2});

        Vector[] grid = instance.getInitials();

        assertTrue(abs(grid[0].get(1) - 3.0) / 3.0 < pct);
        assertTrue(abs(grid[0].get(2) - 12.0) / 12.0 < pct);
        assertTrue(abs(grid[1].get(1) - 6.0) / 6.0 < pct);
        assertTrue(abs(grid[1].get(2) - 12.0) / 12.0 < pct);
    }
}
