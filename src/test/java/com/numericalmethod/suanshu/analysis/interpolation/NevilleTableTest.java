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
package com.numericalmethod.suanshu.analysis.interpolation;

import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 * TODO: add test cases to evaluate at interpolated points, not just points from (x, y)'s
 *
 * @author Haksun Li
 */
public class NevilleTableTest {

    @Test
    public void testNevilleTable_0010() {
        double[] x = new double[]{1, 3, 5, 7, 9};
        double[] y = new double[]{2, 4, 6, 8, 10};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        assertEquals(5, instance.N());

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9},
                instance.x(),
                0));

        assertTrue(equal(
                new double[][]{
                    {2, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 6, 0, 0},
                    {0, 0, 0, 8, 0},
                    {0, 0, 0, 0, 10}
                },
                instance.getTable(),
                0));

        instance.addData(new BinaryRelation(
                new double[]{11, 13, 15, 17, 19},
                new double[]{12, 14, 16, 18, 20}));

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19},
                instance.x(),
                0));

        assertTrue(equal(
                new double[][]{
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 6, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 10, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 12, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 14, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 16, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 18, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 20}
                },
                instance.getTable(),
                0));
    }

    @Test
    public void testNevilleTable_0020() {
        double[] x = new double[]{1, 3, 5, 7, 9};
        double[] y = new double[]{2, 4, 6, 8, 10};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        assertEquals(5, instance.N());

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9},
                instance.x(),
                0));

        assertTrue(equal(
                new double[][]{
                    {2, 0, 0, 0, 0},
                    {0, 4, 0, 0, 0},
                    {0, 0, 6, 0, 0},
                    {0, 0, 0, 8, 0},
                    {0, 0, 0, 0, 10}
                },
                instance.getTable(),
                0));

        double px = instance.evaluate(10);
        assertEquals(11d, px, 0);

        assertTrue(equal(
                new double[][]{
                    {2, 11, 11, 11, 11},
                    {0, 4, 11, 11, 11},
                    {0, 0, 6, 11, 11},
                    {0, 0, 0, 8, 11},
                    {0, 0, 0, 0, 10}
                },
                instance.getTable(),
                0));

        instance.addData(new BinaryRelation(
                new double[]{11, 13, 15, 17, 19},
                new double[]{12, 14, 16, 18, 20}));

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19},
                instance.x(),
                0));

        assertTrue(equal(
                new double[][]{
                    {2, 11, 11, 11, 11, 0, 0, 0, 0, 0},
                    {0, 4, 11, 11, 11, 0, 0, 0, 0, 0},
                    {0, 0, 6, 11, 11, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8, 11, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 10, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 12, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 14, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 16, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 18, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 20}
                },
                instance.getTable(),
                0));

        px = instance.evaluate(10);
        assertEquals(11d, px, 0);

        assertTrue(equal(
                new double[][]{
                    {2, 11, 11, 11, 11, 11, 11, 11, 11, 11},
                    {0, 4, 11, 11, 11, 11, 11, 11, 11, 11},
                    {0, 0, 6, 11, 11, 11, 11, 11, 11, 11},
                    {0, 0, 0, 8, 11, 11, 11, 11, 11, 11},
                    {0, 0, 0, 0, 10, 11, 11, 11, 11, 11},
                    {0, 0, 0, 0, 0, 12, 11, 11, 11, 11},
                    {0, 0, 0, 0, 0, 0, 14, 11, 11, 11},
                    {0, 0, 0, 0, 0, 0, 0, 16, 11, 11},
                    {0, 0, 0, 0, 0, 0, 0, 0, 18, 11},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 20}
                },
                instance.getTable(),
                0));

        px = instance.evaluate(25);
        assertEquals(26d, px, 0);

        assertTrue(equal(
                new double[][]{
                    {2, 26, 26, 26, 26, 26, 26, 26, 26, 26},
                    {0, 4, 26, 26, 26, 26, 26, 26, 26, 26},
                    {0, 0, 6, 26, 26, 26, 26, 26, 26, 26},
                    {0, 0, 0, 8, 26, 26, 26, 26, 26, 26},
                    {0, 0, 0, 0, 10, 26, 26, 26, 26, 26},
                    {0, 0, 0, 0, 0, 12, 26, 26, 26, 26},
                    {0, 0, 0, 0, 0, 0, 14, 26, 26, 26},
                    {0, 0, 0, 0, 0, 0, 0, 16, 26, 26},
                    {0, 0, 0, 0, 0, 0, 0, 0, 18, 26},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 20}
                },
                instance.getTable(),
                0));

        assertEquals(10, instance.N());
        assertEquals(26, instance.get(1, 3), 0);
        assertEquals(18, instance.get(8, 8), 0);
    }

    @Test
    public void testNevilleTable_0030() {
        double[] x = new double[]{1940, 1950, 1960, 1970, 1980, 1990};
        double[] y = new double[]{132165, 151326, 179323, 203302, 226542, 249633};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        double px = instance.evaluate(1940);
        assertEquals(132165, px, 0);
    }

    @Test
    public void testNevilleTable_0040() {
        double[] x = new double[]{-1, 0, 1};
        double[] y = new double[]{3, 4, 7};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        double px = instance.evaluate(-1);
        assertEquals(3, px, 0);
        px = instance.evaluate(0);
        assertEquals(4, px, 0);
        px = instance.evaluate(1);
        assertEquals(7, px, 0);
    }

    @Test
    public void testNevilleTable_0050() {
        double[] x = new double[]{-1, 0, 1};
        double[] y = new double[]{3, 4, 7};
        NevilleTable instance = new NevilleTable();
        instance.addData(new BinaryRelation(x, y));

        double px = instance.evaluate(-1);
        assertEquals(3, px, 0);
        px = instance.evaluate(0);
        assertEquals(4, px, 0);
        px = instance.evaluate(1);
        assertEquals(7, px, 0);
    }

    @Test(expected = DuplicatedAbscissae.class)
    public void testNevilleTable_0090() {
        double[] x = new double[]{-1, 0, -1};
        double[] y = new double[]{3, 4, 7};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        double px = instance.evaluate(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNevilleTable_0100() {
        double[] x = new double[]{1, 3, 5, 7, 9};
        double[] y = new double[]{2, 4, 6, 8, 10};
        NevilleTable instance = new NevilleTable(new BinaryRelation(x, y));
        instance.get(3, 1);
    }
}
