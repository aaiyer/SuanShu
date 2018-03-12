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
package com.numericalmethod.suanshu.number.doublearray;

import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DoubleArrayMathTest {

    //<editor-fold defaultstate="collapsed" desc="tests for min/max">
    @Test
    public void testMax_001() {
        double result = max(1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0);
        assertEquals(100., result, 0);
    }

    @Test
    public void testMax_002() {
        double result = max(-20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0);
        assertEquals(100., result, 0);
    }

    @Test
    public void testMin_001() {
        double result = min(1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0);
        assertEquals(1.1, result, 0);
    }

    @Test
    public void testMin_002() {
        double result = min(-20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0);
        assertEquals(-20, result, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for abs">
    @Test
    public void test_abs_001() {
        double[] dd1 = abs(new double[]{-1, -2, -3, -4, -5});
        double[] dd2 = {1, 2, 3, 4, 5};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_abs_002() {
        double[] dd1 = abs(new double[]{0, -1, -2, -3, -4, -5});
        double[] dd2 = {0, 1, 2, 3, 4, 5};

        assertTrue(equal(dd1, dd2, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for sum">
    @Test
    public void test_sum_0010() {
        double[] dd1 = {1, 2, 3, 4, 5, 6};

        assertEquals(21d, sum(dd1), 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for add">
    @Test
    public void test_add_0010() {
        double[] dd1 = {1, 2, 3, 4, 5, 6};

        assertArrayEquals(
                new double[]{2, 3, 4, 5, 6, 7},
                add(dd1, 1), 0);
    }
    //</editor-fold>
}
