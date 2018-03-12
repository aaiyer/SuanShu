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
package com.numericalmethod.suanshu.analysis.function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class FunctionOpsTest {

    //<editor-fold defaultstate="collapsed" desc="tests for combination of FunctionOps">
    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0010() {
        double instance = FunctionOps.combination(5, 3);
        assertEquals(10, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0020() {
        double instance = FunctionOps.combination(5, 2);
        assertEquals(10, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0030() {
        double instance = FunctionOps.combination(52, 5);
        assertEquals(2598960, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0040() {
        double instance = FunctionOps.combination(79, 0);
        assertEquals(1, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0050() {
        double instance = FunctionOps.combination(79, 79);
        assertEquals(1, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0060() {
        double instance = FunctionOps.combination(23, 1);
        assertEquals(23, instance, 0);
    }

    /**
     * Test of combination method, in class FunctionOps.
     */
    @Test
    public void test_combination_0070() {
        double instance = FunctionOps.combination(23, 23);
        assertEquals(1, instance, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for permutation of FunctionOps">
    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0010() {
        double instance = FunctionOps.permutation(5, 3);
        assertEquals(60, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0020() {
        double instance = FunctionOps.permutation(5, 2);
        assertEquals(20, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0030() {
        double instance = FunctionOps.permutation(10, 3);
        assertEquals(720, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0040() {
        double instance = FunctionOps.permutation(79, 0);
        assertEquals(1, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0050() {
        double instance = FunctionOps.permutation(79, 79);
        assertEquals(1, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0060() {
        double instance = FunctionOps.permutation(23, 1);
        assertEquals(23, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0070() {
        double instance = FunctionOps.permutation(23, 23);
        assertEquals(1, instance, 0);
    }

    /**
     * Test of permutation method, in class FunctionOps.
     */
    @Test
    public void test_permutation_0080() {
        double instance = FunctionOps.permutation(40, 2);
        assertEquals(1560, instance, 1e-12);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for modpow of FunctionOps">
    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0010() {
        long b = 5;
        long e = 3;
        long m = 3;

        long expected = (long) Math.pow(b, e) % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0020() {
        long b = 5;
        long e = 10;
        long m = 7;

        long expected = (long) Math.pow(b, e) % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0030() {
        long b = 50;
        long e = 4;
        long m = 13;

        long expected = (long) Math.pow(b, e) % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0040() {
        long b = 50;
        long e = 1;
        long m = 13;

        long expected = (long) Math.pow(b, e) % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0050() {
        long b = 71;
        long e = 9;
        long m = 23;

        long be = b;
        for (long i = 1; i < e; ++i) {
            be *= b;
        }

        long expected = be % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test
    public void test_modpow_0060() {
        long b = 71;
        long e = 10;
        long m = 23;

        long be = b;
        for (long i = 1; i < e; ++i) {
            be *= b;
        }

        long expected = be % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }

    /**
     * Test of modpow method, in class FunctionOps.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_modpow_0070() {
        long b = 71;
        long e = 0;
        long m = 23;

        long be = b;
        for (long i = 1; i < e; ++i) {
            be *= b;
        }

        long expected = be % m;
        long result = FunctionOps.modpow(b, e, m);
        assertEquals(expected, result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for linearInterpolate of FunctionOps">
    @Test
    public void test_linearInterpolate_0010() {
        assertEquals(0.5, FunctionOps.linearInterpolate(0.5, 0, 0, 1, 1), 0);
    }

    @Test
    public void test_linearInterpolate_0020() {
        assertEquals(2., FunctionOps.linearInterpolate(2, 0, 0, 3, 3), 0);
    }
    //</editor-fold>
}
