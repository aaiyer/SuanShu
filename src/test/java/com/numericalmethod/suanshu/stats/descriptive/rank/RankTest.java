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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RankTest {

    @Test
    public void testRank_0010() {
        Rank instance = new Rank(new double[]{5, 3, 4, 2, 1}, 0);
        assertEquals(0, instance.t(), 1e-15);
        assertEquals(0, instance.s(), 1e-15);

        assertArrayEquals(
                new double[]{5, 3, 4, 2, 1},
                instance.ranks(),
                0);
    }

    @Test
    public void testRank_0020() {
        Rank instance = new Rank(new double[]{8.6, 9.6, 7.2, 6.7, 5.3, 3.9, 4.43, 2.001, 1.001}, 0);
        assertEquals(0, instance.t(), 1e-15);
        assertEquals(0, instance.s(), 1e-15);

        assertArrayEquals(
                new double[]{8, 9, 7, 6, 5, 3, 4, 2, 1},
                instance.ranks(),
                0);
    }

    @Test
    public void testRank_0030() {
        Rank instance = new Rank(new double[]{5, 3, 2, 2, 1}, 0);
        assertEquals(6, instance.t(), 1e-15);
        assertEquals(2, instance.s(), 1e-15);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{5, 4, 2.5, 2.5, 1},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    @Test
    public void testRank_0040() {
        Rank instance = new Rank(new double[]{5.5, 6.6, 8.8, 8.8, -9.9, 0, 0, 0, 0, 8.8, 8.8, 3, 2, 1},
                1e-15);
        assertEquals(120, instance.t(), 1e-15);
        assertEquals(24, instance.s(), 1e-15);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{9.0, 10.0, 12.5, 12.5, 1.0, 3.5, 3.5, 3.5, 3.5, 12.5, 12.5, 8.0, 7.0, 6.0},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    @Test
    public void testRank_0050() {
        Rank instance = new Rank(new double[]{5.5, 6.6, 8.8, 8.8, -9.9, 0, 0, 0, 0, 8.8, 8.8, 3, 2, 1});
        assertEquals(120, instance.t(), 1e-15);
        assertEquals(24, instance.s(), 1e-15);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{9.0, 10.0, 12.5, 12.5, 1.0, 3.5, 3.5, 3.5, 3.5, 12.5, 12.5, 8.0, 7.0, 6.0},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    @Test
    public void testRank_0060() {
        Rank instance = new Rank(new double[]{5, 3, 2.5, 2.5, 1}, 0.6);
        assertEquals(24, instance.t(), 1e-15);
        assertEquals(6, instance.s(), 1e-15);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{5, 3, 3, 3, 1},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    /**
     * Check midrank for ties.
     */
    @Test
    public void testRank_0070() {
        Rank instance = new Rank(new double[]{1, 2, 3, 4, 4}, 0);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{1, 2, 3, 4.5, 4.5},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    /**
     * Check midrank for ties.
     */
    @Test
    public void testRank_0080() {
        Rank instance = new Rank(new double[]{1, 2, 3, 3, 3, 4, 4, 5}, 1e-15);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{1, 2, 4, 4, 4, 6.5, 6.5, 8},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    /**
     * Check midrank for ties.
     */
    @Test
    public void testRank_0090() {
        double[] data = new double[]{1, 2, 3, 3, 3, 4, 4, 5};
        Rank instance = new Rank(data, SuanShuUtils.autoEpsilon(data));

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{1, 2, 4, 4, 4, 6.5, 6.5, 8},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }

    /**
     * Check midrank for ties.
     */
    @Test
    public void testRank_0100() {
        double[] data = new double[]{1, 2, 3, 3, 3, 4, 4, 5};
        Rank instance = new Rank(data);

        double[] rank = instance.ranks();
        assertArrayEquals(
                new double[]{1, 2, 4, 4, 4, 6.5, 6.5, 8},
                rank,
                0);

        for (int i = 0; i < 5; ++i) {
            assertEquals(rank[i], instance.rank(i), 1e-15);
        }
    }
}
