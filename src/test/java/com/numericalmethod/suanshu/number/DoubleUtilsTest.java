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
package com.numericalmethod.suanshu.number;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class DoubleUtilsTest {

    //<editor-fold defaultstate="collapsed" desc="tests for compare of DoubleUtils">
    @Test
    public void test_compare_0010() {
        assertTrue(compare(1, 1.0, 0) == 0);
        assertTrue(compare(9.2, 1.0, 0) > 0);
        assertTrue(compare(-9.2, 1.0, 0) < 0);
    }

    /**
     * It is important that 0 == 0.
     */
    @Test
    public void test_compare_0020() {
        assertTrue(compare(0.0, 0, 0) == 0);
    }

    /**
     * 9.2 cannot be represented in finite binary representation.
     */
    @Test
    public void test_compare_0030() {
//        System.out.println(String.format("This is supposed to be 0 but 9.2 cannot be represented, %1.15f", 9.2 * 100 - 920));
        assertTrue(compare(9.2 * 100 - 920, 0, 1e-12) == 0);
        assertTrue(compare(0, 9.2 * 100 - 920, 1e-12) == 0);
    }

    @Test
    public void test_compare_0040() {
        assertTrue(compare(Double.NaN, Double.NaN, 0) == 0);
        assertTrue(compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0) == 0);
        assertTrue(compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0) == 0);
    }

    @Test
    public void test_compare_0050() {
        assertTrue(compare(Double.NaN, Double.POSITIVE_INFINITY, 0) > 0);
        assertTrue(compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0) > 0);
    }

    @Test
    public void test_compare_0060() {
        assertTrue(compare(Double.NaN, Double.MAX_VALUE, 0) > 0);
        assertTrue(compare(Double.POSITIVE_INFINITY, Double.MAX_VALUE, 0) > 0);
        assertTrue(compare(0, Double.MIN_VALUE, 0) < 0);
        assertTrue(compare(Double.MIN_VALUE, Double.MIN_NORMAL, 0) < 0);
    }

    @Test
    public void test_compare_0070() {
        assertTrue(compare(0, Double.MIN_VALUE / 2, 0) == 0);
    }

    @Test
    public void test_compare_0080() {
        assertTrue(compare(0, 0, 0) == 0);
    }

    @Test
    public void test_compare_0090() {
        assertTrue(compare(0.0, -0.0, 0) == 0);
    }

    @Test
    public void test_compare_0100() {
        assertTrue(compare(1, 0, 0) == 1);
    }

    @Test
    public void test_compare_0110() {
        assertTrue(compare(-1, 0, 0) == -1);
    }

    @Test
    public void test_compare_0120() {
        assertTrue(compare(Double.NaN, Double.NaN, 0) == 0);
    }

    @Test
    public void test_compare_0130() {
        assertTrue(compare(Double.NaN, Double.POSITIVE_INFINITY, 0) == 1);
    }

    @Test
    public void test_compare_0140() {
        assertTrue(compare(Double.MIN_VALUE, Double.NEGATIVE_INFINITY, 0) == 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for equal of DoubleUtils">
    @Test
    public void test_equals_0010() {
        double[][] dd1 = {{1, 2, 3}, {4, 5, 6}};
        double[][] dd2 = {{1, 2, 3}, {4, 5, 6}};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_equals_0020() {
        double[][] dd1 = {null, {4, 5, 6}};
        double[][] dd2 = {null, {4, 5, 6}};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_equals_0030() {
        double[][] dd1 = {null, null};
        double[][] dd2 = {null, null};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_equals_0040() {
        double[] dd1 = {1, 2, 3, 4, 5};
        double[] dd2 = {1, 2, 3, 4, 5};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_equals_0050() {
        double[] dd1 = {1, 2, 3, 4, 5.00000000001};
        double[] dd2 = {1, 2, 3, 4, 5};

        assertTrue(equal(dd1, dd2, 1e-10));
    }

    @Test
    public void test_equals_0060() {
        int[] dd1 = {1, 2, 3, 4, 5};
        int[] dd2 = {1, 2, 3, 4, 5};

        assertTrue(equal(dd1, dd2));
    }

    @Test
    public void test_equals_0070() {
        assertTrue(equal(0, 0, 0));
    }

    @Test
    public void test_equals_0080() {
        assertTrue(equal(9.2 * 100 - 920, 0, 1e-12));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for equal for NaN of DoubleUtils">
    @Test
    public void test_equalsNaN_0010() {
        assertTrue(equal(Double.NaN, Double.NaN, 0));
    }

    @Test
    public void test_equalsNaN_0020() {
        assertFalse(equal(Double.NaN, 2, 0));
    }

    @Test
    public void test_equalsNaN_0030() {
        assertFalse(equal(Double.NaN, -0, 0));
    }

    @Test
    public void test_equalsNaN_0040() {
        double[][] dd1 = {{1, 2, Double.NaN}, {4, 5, 6}};
        double[][] dd2 = {{1, 2, Double.NaN}, {4, 5, 6}};

        assertTrue(equal(dd1, dd2, 0));
    }

    @Test
    public void test_equalsNaN_0050() {
        double[][] dd1 = {{1, 2, Double.NaN}, {4, Double.NaN, 6}};
        double[][] dd2 = {{1, 2, Double.NaN}, {4, Double.NaN, 6}};

        assertTrue(equal(dd1, dd2, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for power of 2">
    @Test
    public void test_isPow2_0010() {
        assertTrue(isPow2(0));
        assertTrue(isPow2(1));
        assertTrue(isPow2(2));
        assertTrue(isPow2(4));
        assertTrue(isPow2(8));
        assertTrue(isPow2(16));
        assertTrue(isPow2(32));
    }

    @Test
    public void test_isPow2_0020() {
        assertFalse(isPow2(3));
        assertFalse(isPow2(5));
        assertFalse(isPow2(7));
        assertFalse(isPow2(9));
        assertFalse(isPow2(11));
        assertFalse(isPow2(13));
        assertFalse(isPow2(723));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for min/max index">
    @Test
    public void test_maxIndex_001() {
        double[] data = new double[]{1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0};
        int result = maxIndex(false, 0, data.length, data);
        assertEquals(9, result);
    }

    @Test
    public void test_maxIndex_002() {
        double[] data = new double[]{-20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0, 100.0};
        int result = maxIndex(false, 0, data.length, data);
        assertEquals(15, result);
    }

    @Test
    public void test_maxIndex_003() {
        double[] data = new double[]{-20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0, 100.0};
        int result = maxIndex(false, 0, data.length, data);
        assertEquals(15, result);
    }

    @Test
    public void test_maxIndex_004() {
        double[] data = new double[]{-20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0, 100.0};
        int result = maxIndex(false, 0, data.length - 2, data);
        assertEquals(14, result);
    }

    @Test
    public void test_minIndex_001() {
        double[] data = new double[]{1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0};
        int result = minIndex(false, 0, data.length, data);
        assertEquals(0, result);
    }

    @Test
    public void test_minIndex_002() {
        double[] data = new double[]{-20, -20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0};
        int result = minIndex(false, 0, data.length, data);
        assertEquals(0, result);
    }

    @Test
    public void test_minIndex_003() {
        double[] data = new double[]{-20, -20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0};
        int result = minIndex(false, 0, data.length, data);
        assertEquals(0, result);
    }

    @Test
    public void test_minIndex_004() {
        double[] data = new double[]{-20, -20, -15, -10, -5, -1, 0, 1.1, 1.2, 1.3, 1.4, 5, 6, 7, 8, 9, 100.0};
        int result = minIndex(false, 2, data.length, data);
        assertEquals(2, result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for foreach">
    @Test
    public void test_foreach_0010() {
        double[] inputs = new double[]{1, 2, 3, 4, 5, 6, 7};
        double[] outputs = foreach(inputs, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x * x;
            }
        });

        assertArrayEquals(new double[]{1, 4, 9, 16, 25, 36, 49}, outputs, 1e-15);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for round">
    @Test
    public void test_round_0010() {
        double number = 1.1234567890123456789;

        double value = round(number, 0);
        assertEquals(1, value, 1e-15);

        value = round(number, 1);
        assertEquals(1.1, value, 1e-15);

        value = round(number, 2);
        assertEquals(1.12, value, 1e-15);

        value = round(number, 3);
        assertEquals(1.123, value, 1e-15);

        value = round(number, 4);
        assertEquals(1.1235, value, 1e-15);

        value = round(number, 5);
        assertEquals(1.12346, value, 1e-15);

        value = round(number, 6);
        assertEquals(1.123457, value, 1e-15);

        value = round(number, 7);
        assertEquals(1.1234568, value, 1e-15);
    }

    @Test
    public void test_round_0020() {
        double number = 12345.123456789;

        double value = round(number, 0);
        assertEquals(12345, value, 1e-15);

        value = round(number, 1);
        assertEquals(12345.1, value, 1e-15);

        value = round(number, 2);
        assertEquals(12345.12, value, 1e-15);

        value = round(number, 3);
        assertEquals(12345.123, value, 1e-15);

        value = round(number, 4);
        assertEquals(12345.1235, value, 1e-15);

        value = round(number, 5);
        assertEquals(12345.12346, value, 1e-15);

        value = round(number, 6);
        assertEquals(12345.123457, value, 1e-15);

        value = round(number, 7);
        assertEquals(12345.1234568, value, 1e-15);
    }

    @Test
    public void test_round_0030() {
        double number = -12345.123456789;

        double value = round(number, 0);
        assertEquals(-12345, value, 1e-15);

        value = round(number, 1);
        assertEquals(-12345.1, value, 1e-15);

        value = round(number, 2);
        assertEquals(-12345.12, value, 1e-15);

        value = round(number, 3);
        assertEquals(-12345.123, value, 1e-15);

        value = round(number, 4);
        assertEquals(-12345.1235, value, 1e-15);

        value = round(number, 5);
        assertEquals(-12345.12346, value, 1e-15);

        value = round(number, 6);
        assertEquals(-12345.123457, value, 1e-15);

        value = round(number, 7);
        assertEquals(-12345.1234568, value, 1e-15);
    }

    @Test
    public void test_round_0040() {
        double number = 0;

        double value = round(number, 0);
        assertEquals(0, value, 1e-15);

        value = round(number, 1);
        assertEquals(0, value, 1e-15);

        value = round(number, 2);
        assertEquals(0, value, 1e-15);

        value = round(number, 3);
        assertEquals(0, value, 1e-15);

        value = round(number, 4);
        assertEquals(0, value, 1e-15);

        value = round(number, 5);
        assertEquals(0, value, 1e-15);

        value = round(number, 6);
        assertEquals(0, value, 1e-15);

        value = round(number, 7);
        assertEquals(0, value, 1e-15);
    }

    @Test
    public void test_round_0050() {
        assertTrue(Double.isNaN(round(Double.NaN, 0)));
        assertTrue(Double.isNaN(round(Double.NaN, 1)));

        assertTrue(Double.isInfinite(round(Double.POSITIVE_INFINITY, 0)));
        assertTrue(Double.isInfinite(round(Double.POSITIVE_INFINITY, 1)));

        assertTrue(Double.isInfinite(round(Double.NEGATIVE_INFINITY, 0)));
        assertTrue(Double.isInfinite(round(Double.NEGATIVE_INFINITY, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_round_0060() {
        assertTrue(Double.isNaN(round(Double.NaN, -1)));
    }

    @Test
    public void test_round_0070() {
        assertEquals(1, round(0.9, 0), 1e-15);
        assertEquals(-1, round(-0.9, 0), 1e-15);

        assertEquals(0, round(0.1, 0), 1e-15);
        assertEquals(0, round(-0.1, 0), 1e-15);

        assertEquals(0, round(0.0, 0), 1e-15);
        assertEquals(0, round(-0.0, 0), 1e-15);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for paste method for double[]">
    /**
     * Test of paste method for double[].
     */
    @Test
    public void testJoin4Doubles_001() {
        double[] d1 = {1, 2, 3, 4, 5};
        double[] d2 = {6, 7, 8, 9, 10};
        double[] d3 = {11, 12, 13, 14, 15};
        double[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        double[][] d = {d1, d2, d3};
        double[] instance = concat(d);

        assertTrue(equal(expected, instance, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for hasDuplicate method">
    /**
     * Test of hasDuplicate method.
     */
    @Test
    public void test_hasDuplicate_0010() {
        double[] d1 = {1, 2, 3, 4, 5};
        assertFalse(hasDuplicate(d1, 0.));

        double[] d2 = {6, 7, 8, 9, 10};
        assertFalse(hasDuplicate(d2, 0.));

        double[] d3 = {11, 12, 13, 14, 11};//11
        assertTrue(hasDuplicate(d3, 0.));

        double[] d4 = {1, 2, 3, 4, 5, 6, 15, 8, 9, 10, 11, 12, 13, 14, 15};//15
        assertTrue(hasDuplicate(d4, 0.));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for reverse">
    @Test
    public void test_reverse_0010() {
        double[] arr1 = {1.0, 2, 3, 4, 5};
        reverse(arr1);

        double[] expected = {5, 4, 3, 2, 1};
        assertTrue(equal(expected, arr1, 0));
    }

    @Test
    public void test_reverse_0020() {
        double[] arr1 = {1.0, 2, 3, 4, 5, 6.0};
        reverse(arr1);

        double[] expected = {6, 5, 4, 3, 2, 1};
        assertTrue(equal(expected, arr1, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for concat">
    /**
     * Test of concat method, of class Util.
     */
    @Test
    public void test_merge_0010() {
        double[] expected = R.seq(1.0, 15.0, 1.0);

        double[] arr1 = {1.0, 2, 3, 4, 5};
        double[] arr2 = {6, 7, 8, 9, 10};
        double[] arr3 = {11, 12, 13, 14, 15};

        double[] arr = concat(arr1, arr2, arr3);
        assertTrue(equal(expected, arr, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for shellsort">
    @Test
    public void test_shellsort_0010() {
        double[] arr = new double[]{4, 5, 2, 3, 1};
        int[] order = shellsort(arr);
        assertArrayEquals(new int[]{5, 3, 4, 1, 2}, order);
        assertTrue(equal(new double[]{1, 2, 3, 4, 5}, arr, 0));
    }

    @Test
    public void test_shellsort_0020() {
        double[] arr = new double[]{-4.2, 5.4, 20.5, -3.3, 1};
        int[] order = shellsort(arr);
        assertArrayEquals(new int[]{1, 4, 5, 2, 3}, order);
        assertTrue(equal(new double[]{-4.2, -3.3, 1, 5.4, 20.5}, arr, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for toString">
    @Test
    public void test_toString_0010() {
        double[][] arr = new double[][]{
            {1, 2},
            {3, 4}
        };

        String str = DoubleUtils.toString(arr);
        assertEquals("{\n{1.0, 2.0},\n{3.0, 4.0}\n};\n", str);
    }
    //</editor-fold>
}
