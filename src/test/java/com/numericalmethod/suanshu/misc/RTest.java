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
package com.numericalmethod.suanshu.misc;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.misc.R.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class RTest {

    //<editor-fold defaultstate="collapsed" desc="tests for rep">
    @Test
    public void test_rep_0010() {
        double[] expected = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};

        double[] rep1 = rep(1.0, 6);
        assertTrue(equal(expected, rep1, 0));
    }

    @Test
    public void test_rep_0020() {
        double[] expected = {1.0};

        double[] rep1 = rep(1.0, 1);
        assertTrue(equal(expected, rep1, 0));
    }

    @Test
    public void test_rep_0030() {
        double[] expected = new double[0];//an array of length 0 (NOT null)

        double[] rep1 = rep(1.0, 0);
        assertTrue(equal(expected, rep1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_rep_0040() {
        double[] rep1 = rep(1.0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_rep_0050() {
        double[] rep1 = rep(1.0, -10);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for seq">
    @Test
    public void test_seq_0010() {
        double[] expected = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5};

        double[] seq1 = seq(1, 3.5, 0.5);
        assertArrayEquals(expected, seq1, 0);

        double[] seq2 = seq(1, 3.6, 0.5);
        assertArrayEquals(expected, seq2, 0);

        double[] seq3 = seq(1, 3.9, 0.5);
        assertArrayEquals(expected, seq3, 0);

        double[] seq4 = seq(1, 3.999999999, 0.5);
        assertArrayEquals(expected, seq4, 0);

        double[] seq5 = seq(1, 3.49999999999999999999, 0.5);
        assertArrayEquals(expected, seq5, 0);
    }

    @Test
    public void test_seq_0020() {
        double[] expected = {3.5, 3.0, 2.5, 2.0, 1.5, 1};

        double[] seq1 = seq(3.5, 1, -0.5);
        assertArrayEquals(expected, seq1, 0);

        double[] seq2 = seq(3.5, 0.9, -0.5);
        assertArrayEquals(expected, seq2, 0);

        double[] seq3 = seq(3.5, 0.6, -0.5);
        assertArrayEquals(expected, seq3, 0);
    }

    @Test
    public void test_seq_0030() {
        double[] expected = {-3.5, -3.0, -2.5, -2.0, -1.5, -1};

        double[] seq1 = seq(-3.5, -1, 0.5);
        assertArrayEquals(expected, seq1, 0);

        double[] seq2 = seq(-3.5, -0.9, 0.5);
        assertArrayEquals(expected, seq2, 0);
    }

    @Test
    public void test_seq_0040() {
        double[] expected = {-1.0, -1.5, -2.0, -2.5, -3.0, -3.5};

        double[] seq1 = seq(-1.0, -3.5, -0.5);
        assertArrayEquals(expected, seq1, 0);

        double[] seq2 = seq(-1.0, -3.9, -0.5);
        assertArrayEquals(expected, seq2, 0);
    }

    @Test
    public void test_seq_0050() {
        double[] expected = {1.0};

        double[] seq1 = seq(1d, 1d, 1d);
        assertArrayEquals(expected, seq1, 0);

        double[] seq2 = seq(1d, 1.1, 1d);
        assertArrayEquals(expected, seq2, 0);
    }

    @Test
    public void test_seq_0060() {
        double[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        double[] seq1 = seq(1, 10, 1.0);
        assertArrayEquals(expected, seq1, 0);
    }

    @Test
    public void test_seq_0070() {
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int[] seq1 = seq(1, 10, 1);
        assertTrue(Arrays.equals(expected, seq1));

        int[] seq2 = seq(1, 10);
        assertTrue(Arrays.equals(expected, seq2));
    }

    @Test
    public void test_seq_0080() {
        int[] expected = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        int[] seq1 = seq(10, 1, -1);
        assertTrue(Arrays.equals(expected, seq1));

        int[] seq2 = seq(10, 1);
        assertTrue(Arrays.equals(expected, seq2));
    }

    @Test
    public void test_seq_0090() {
        int[] expected = {1};
        int[] seq1 = seq(1, 1, 1);
        assertTrue(Arrays.equals(expected, seq1));
    }

    @Test
    public void test_seq_0100() {
        int[] expected = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1};

        int[] seq1 = seq(-10, -1, 1);
        assertTrue(Arrays.equals(expected, seq1));

        int[] seq2 = seq(-10, -1);
        assertTrue(Arrays.equals(expected, seq2));
    }

    /**
     * The last element does not land on the boundary.
     */
    @Test
    public void test_seq_0110() {
        double[] expected = {10.0, 8.5, 7.0, 5.5, 4.0, 2.5, 1.0};

        double[] seq1 = seq(10, 0, -1.5);
        assertArrayEquals(expected, seq1, 0);
    }

    /**
     * The last element does not land on the boundary.
     */
    @Test
    public void test_seq_0120() {
        double[] expected = {10.0, 8.3, 6.6, 4.9, 3.2, 1.5};

        double[] seq1 = seq(10, 0, -1.7);
        assertArrayEquals(expected, seq1, 0);
    }

    /**
     * The last element does not land on the boundary.
     */
    @Test
    public void test_seq_0130() {
        double[] expected = {0.0, 1.7, 3.4, 5.1, 6.8, 8.5};

        double[] seq1 = seq(0, 10, 1.7);
        assertArrayEquals(expected, seq1, 0);
    }

    /**
     * The last element does not land on the boundary.
     */
    @Test
    public void test_seq_0140() {
        double[] expected = {-10.0, -11.7, -13.4, -15.1, -16.8, -18.5};

        double[] seq1 = seq(-10, -20, -1.7);
        assertArrayEquals(expected, seq1, 0);
    }

    @Test
    public void test_seq_0200() {
        //TODO: 0.0825 + 0.0025*2 = 0.0875 is OK in R but cannot be represented as a double in Java
        double[] expected = {0.0825, 0.0850, 0.0875, 0.0900, 0.0925, 0.0950, 0.0975, 0.1000, 0.1025, 0.1050, 0.1075, 0.1100, 0.1125, 0.1150, 0.1175, 0.1200};

        double[] seq1 = seq(0.0825, 0.1200, 0.0025);
        assertArrayEquals(expected, seq1, 1e-16);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for diff">
    @Test
    public void test_diff_0010() {
        double[] x = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55};

        assertTrue(equal(
                new double[]{2, 3, 4, 5, 6, 7, 8, 9, 10},
                diff(x, 1, 1),
                0));

        assertTrue(equal(
                new double[]{5, 7, 9, 11, 13, 15, 17, 19},
                diff(x, 2, 1),
                0));

        assertTrue(equal(
                new double[]{9, 12, 15, 18, 21, 24, 27},
                diff(x, 3, 1),
                0));

        assertTrue(equal(
                new double[]{54},
                diff(x, 9, 1),
                0));

        assertTrue(equal(
                new double[]{1, 1, 1, 1, 1, 1, 1, 1},
                diff(x, 1, 2),
                0));

        assertTrue(equal(
                new double[]{0, 0, 0, 0, 0, 0, 0},
                diff(x, 1, 3),
                0));

        assertTrue(equal(
                new double[]{0, 0, 0, 0},
                diff(x, 1, 6),
                0));

        assertTrue(equal(
                new double[]{4, 4, 4, 4, 4, 4},
                diff(x, 2, 2),
                0));

        assertTrue(equal(
                new double[]{9, 9, 9, 9},
                diff(x, 3, 2),
                0));

        assertTrue(equal(
                new double[]{0},
                diff(x, 3, 3),
                0));
    }

    @Test
    public void test_diff_0020() {
        double[] x = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55};

        assertTrue(equal(
                new double[]{2, 3, 4, 5, 6, 7, 8, 9, 10},
                diff(x),
                0));
    }

    @Test
    public void test_diff_0030() {
        double[][] x = new double[][]{
            {1, 3, 5, 7, 9},
            {2, 4, 6, 8, 11},
            {5, 15, 25, 35, 99}
        };

        double[][] dx = new double[][]{
            {1, 1, 1, 1, 2},
            {3, 11, 19, 27, 88}
        };

        assertEquals(new DenseMatrix(dx),
                new DenseMatrix(diff(x)));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for cumsum">
    @Test
    public void test_cumsum_0010() {
        double[] x = seq(1.0, 10.0, 1.0);

        assertTrue(equal(
                new double[]{1, 3, 6, 10, 15, 21, 28, 36, 45, 55},
                cumsum(x),
                0));
    }

    @Test
    public void test_cumsum_0020() {
        double[] x = new double[]{1};

        assertTrue(equal(
                new double[]{1},
                cumsum(x),
                0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for which">
    @Test
    public void test_which_0010() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new int[]{0, 1, 2, 3, 4, 9},
                which(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x <= 5);
                    }
                })));
    }

    @Test
    public void test_which_0020() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new int[]{1, 3, 5, 7, 9},
                which(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x % 2 == 0);
                    }
                })));
    }

    @Test
    public void test_which_0030() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new int[]{0, 2, 4, 6, 8},
                which(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x % 2 == 1);
                    }
                })));
    }

    @Test
    public void test_which_0040() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new int[]{0, 2, 4, 6, 8},
                which(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (index % 2 == 0);
                    }
                })));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for select">
    @Test
    public void test_select_0010() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new double[]{1, 2, 3, 4, 5, 0},
                select(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x <= 5);
                    }
                }),
                1e-15));
    }

    @Test
    public void test_select_0020() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new double[]{2, 4, 6, 8, 0},
                select(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x % 2 == 0);
                    }
                }),
                1e-15));
    }

    @Test
    public void test_select_0030() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9},
                select(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (x % 2 == 1);
                    }
                }),
                1e-15));
    }

    @Test
    public void test_select_0040() {
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        assertTrue(equal(
                new double[]{1, 3, 5, 7, 9},
                select(x,
                new which() {

                    public boolean isTrue(double x, int index) {
                        return (index % 2 == 0);
                    }
                }),
                1e-15));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for subarray">
    @Test
    public void test_subarray_0010() {
        double[] x = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertTrue(equal(
                new double[]{0, 1, 2, 3, 4},
                subarray(x, new int[]{0, 1, 2, 3, 4}),
                0));

        assertTrue(equal(
                new double[]{1, 4, 7, 9},
                subarray(x, new int[]{1, 4, 7, 9}),
                0));

        assertTrue(equal(
                new double[]{0},
                subarray(x, new int[]{0}),
                0));

        assertTrue(equal(
                new double[]{6},
                subarray(x, new int[]{6}),
                0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ifelse">
    @Test
    public void test_ifelse_0010() {
        double[] x = new double[]{-5, -4, -3, -2, -1, 0, 1, 4, 9, 16, 25};

        assertTrue(equal(
                new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                    0, 1, 2, 3, 4, 5},
                ifelse(x,
                new ifelse() {

                    public boolean test(double x) {
                        return x >= 0;
                    }

                    public double yes(double x) {
                        return Math.sqrt(x);
                    }

                    public double no(double x) {
                        return Double.NaN;
                    }
                }),
                0));
    }

    @Test
    public void test_ifelse_0020() {
        double[] x = new double[]{-5, -4, -3, -2, -1, 0, 1, 4, 9, 16, 25};

        assertTrue(equal(
                new double[]{5, -2, 3, -1, 1,
                    0, 1, 2, 9, 8, 25},
                ifelse(x,
                new ifelse() {

                    public boolean test(double x) {
                        return x % 2 == 0;
                    }

                    public double yes(double x) {
                        return x / 2;
                    }

                    public double no(double x) {
                        return Math.abs(x);
                    }
                }),
                0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for order">
    @Test
    public void test_order_0010() {
        int[] order = order(new double[]{4, 5, 2, 3, 1});
        assertArrayEquals(new int[]{5, 3, 4, 1, 2}, order);
    }

    @Test
    public void test_order_0020() {
        int[] order = order(new double[]{4, 5, 2, 3, 1}, false);
        assertArrayEquals(new int[]{2, 1, 4, 3, 5}, order);
    }

    @Test
    public void test_order_0030() {
        int[] order = order(new double[]{-4.2, 5.4, 20.5, -3.3, 1});
        assertArrayEquals(new int[]{1, 4, 5, 2, 3}, order);
    }

    @Test
    public void test_order_0040() {
        int[] order = order(new double[]{-4.2, 5.4, 20.5, -3.3, 1}, false);
        assertArrayEquals(new int[]{3, 2, 5, 4, 1}, order);

    }
    //</editor-fold>
}
