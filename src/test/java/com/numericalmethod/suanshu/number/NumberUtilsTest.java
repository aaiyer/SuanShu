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

import com.numericalmethod.suanshu.number.complex.Complex;
import static com.numericalmethod.suanshu.number.NumberUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class NumberUtilsTest {

    //<editor-fold defaultstate="collapsed" desc="tests for parse of NumberUtils">
    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0010() {
        Number number = NumberUtils.parse("5");
        assertEquals(new Double(5d), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0015() {
        Number number = NumberUtils.parse("0");
        assertEquals(new Double(0), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0016() {
        Number number = NumberUtils.parse("+0");
        assertEquals(new Double(0), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0017() {
        Number number = NumberUtils.parse("-0");
//        assertEquals(new Double(-0), number);//TODO: how to make this pass?
        assertTrue(NumberUtils.equal(0, number, 0));
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0018() {
        Number number = NumberUtils.parse("-0.0");
//        assertEquals(new Double(0), number);//TODO: how to make this pass?
        assertTrue(NumberUtils.equal(0, number, 0));
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0019() {
        Number number = NumberUtils.parse("-0.0000");
//        assertEquals(new Double(0), number);//TODO: how to make this pass?
        assertTrue(NumberUtils.equal(0, number, 0));
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0020() {
        Number number = NumberUtils.parse("5.");
        assertEquals(new Double(5d), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0030() {
        Number number = NumberUtils.parse("5d");
        assertEquals(new Double(5d), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0040() {
        Number number = NumberUtils.parse("5.123");
        assertEquals(new Double(5.123), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0050() {
        Number number = NumberUtils.parse("5e1");
        assertEquals(new Double(50), number);
    }

    /**
     * Test of class NumberUtils.parse
     * Test for trailing 0s.
     */
    @Test
    public void test_parse_0060() {
        Number number = NumberUtils.parse("5.123     ");
        assertEquals(new Double(5.123), number);
    }

    /**
     * Test of class NumberUtils.parse
     * Test for leading 0s.
     */
    @Test
    public void test_parse_0070() {
        Number number = NumberUtils.parse("     5.123");
        assertEquals(new Double(5.123), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0080() {
        Number number = NumberUtils.parse("i");
        assertEquals(new Complex(0, 1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0085() {
        Number number = NumberUtils.parse("+i");
        assertEquals(new Complex(0, 1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0087() {
        Number number = NumberUtils.parse("+  i  ");
        assertEquals(new Complex(0, 1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0090() {
        Number number = NumberUtils.parse("-i");
        assertEquals(new Complex(0, -1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0095() {
        Number number = NumberUtils.parse("-  i  ");
        assertEquals(new Complex(0, -1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0100() {
        Number number = NumberUtils.parse("1 +2i");
        assertEquals(new Complex(1, 2), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0110() {
        Number number = NumberUtils.parse("1 -2i");
        assertEquals(new Complex(1, -2), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0120() {
        Number number = NumberUtils.parse("1 + 2i");
        assertEquals(new Complex(1, 2), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0130() {
        Number number = NumberUtils.parse("1 - 2i");
        assertEquals(new Complex(1, -2), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0140() {
        Number number = NumberUtils.parse("1 - i");
        assertEquals(new Complex(1, -1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0150() {
        Number number = NumberUtils.parse("1.234 - i");
        assertEquals(new Complex(1.234, -1), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0160() {
        Number number = NumberUtils.parse("1.234 - 2.456i");
        assertEquals(new Complex(1.234, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0170() {
        Number number = NumberUtils.parse("1.234 -2.456i");
        assertEquals(new Complex(1.234, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0175() {
        Number number = NumberUtils.parse("1.234 -  2.456  i");
        assertEquals(new Complex(1.234, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0177() {
        Number number = NumberUtils.parse("  1.234 -  2.456  i");
        assertEquals(new Complex(1.234, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0180() {
        Number number = NumberUtils.parse("-2.456i");
        assertEquals(new Complex(0, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0190() {
        Number number = NumberUtils.parse("-  2.456i");
        assertEquals(new Complex(0, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0200() {
        Number number = NumberUtils.parse("+  2.456i");
        assertEquals(new Complex(0, 2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0210() {
        Number number = NumberUtils.parse("+  2.456  i");
        assertEquals(new Complex(0, 2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0220() {
        Number number = NumberUtils.parse("1.234 -  2.456  i");
        assertEquals(new Complex(1.234, -2.456), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0230() {
        Number number = NumberUtils.parse("1.234e2 -  2.456e3 i");
        assertEquals(new Complex(1.234e2, -2.456e3), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0240() {
        Number number = NumberUtils.parse("-12.33499998353 + 16.10039362555i");
        assertEquals(new Complex(-12.33499998353, 16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0250() {
        Number number = NumberUtils.parse("-12.33499998353 - 16.10039362555i");
        assertEquals(new Complex(-12.33499998353, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0260() {
        Number number = NumberUtils.parse("12.33499998353 - 16.10039362555i");
        assertEquals(new Complex(12.33499998353, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0270() {
        Number number = NumberUtils.parse("12.33499998353e3 - 16.10039362555i");
        assertEquals(new Complex(12.33499998353e3, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0280() {
        Number number = NumberUtils.parse("12.33499998353e03 - 16.10039362555i");
        assertEquals(new Complex(12.33499998353e3, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0290() {
        Number number = NumberUtils.parse("12.33499998353e-03 - 16.10039362555i");
        assertEquals(new Complex(12.33499998353e-3, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0300() {
        Number number = NumberUtils.parse("12.33499998353e+03 - 16.10039362555i");
        assertEquals(new Complex(12.33499998353e+3, -16.10039362555), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0310() {
        Number number = NumberUtils.parse("12.33499998353e+03 - 16.10039362555e7i");
        assertEquals(new Complex(12.33499998353e+3, -16.10039362555e7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0320() {
        Number number = NumberUtils.parse("12.33499998353e+03 - 16.10039362555e07i");
        assertEquals(new Complex(12.33499998353e+3, -16.10039362555e7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0330() {
        Number number = NumberUtils.parse("12.33499998353e+03 - 16.10039362555e-7i");
        assertEquals(new Complex(12.33499998353e+3, -16.10039362555e-7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0340() {
        Number number = NumberUtils.parse("12.33499998353e+03 - 16.10039362555e+7i");
        assertEquals(new Complex(12.33499998353e+3, -16.10039362555e+7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0350() {
        Number number = NumberUtils.parse("- 16.10039362555e+7i");
        assertEquals(new Complex(0, -16.10039362555e+7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0360() {
        Number number = NumberUtils.parse("+ 16.10039362555e+7i");
        assertEquals(new Complex(0, 16.10039362555e+7), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test
    public void test_parse_0370() {
        Number number = NumberUtils.parse("12.33499998353e+03");
        assertEquals(new Double(12.33499998353e+3), number);
    }

    /**
     * Test of class NumberUtils.parse
     */
    @Test(expected = NumberFormatException.class)
    public void test_parse_0500() {
        Number number = NumberUtils.parse("+ 12.33499998353 - 16.10039362555i");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for equal of NumberUtils">
    @Test
    public void test_equal_0010() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1, 0);
        assertTrue(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0011() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1 + 1e-15, 0);
        assertFalse(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0012() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1 + 1e-16, 0);
        assertTrue(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0020() {
        Number num1 = new Complex(1, 0);
        Number num2 = new Complex(1, 0);
        assertTrue(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0021() {
        Number num1 = new Complex(1, 2e-15);
        Number num2 = new Complex(1, 0);
        assertFalse(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0022() {
        Number num1 = new Complex(1, 1e-16);
        Number num2 = new Complex(1, 0);
        assertTrue(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0030() {
        Number num1 = new Double(1);
        Number num2 = new Double(1);
        assertTrue(equal(num1, num2, 1e-15));
    }

    @Test
    public void test_equal_0040() {
        assertTrue(equal(1.1, 1.1, 1e-15));
    }

    @Test
    public void test_equal_0050() {
        Number num1 = new Double(1.1);
        assertTrue(equal(num1, 1.1, 1e-15));
    }

    @Test
    public void test_equal_0060() {
        Number num1 = new Complex(1.1, 0);
        assertTrue(equal(num1, 1.1, 1e-15));
    }

    /**
     * test for Complex vs. int
     */
    @Test
    public void test_equal_0070() {
        Number num1 = new Complex(1, 0);
        assertTrue(equal(num1, 1, 1e-15));
    }

    /**
     * test for int
     */
    @Test
    public void test_equal_0080() {
        assertTrue(equal(1, 1, 1e-15));
    }

    @Test
    public void test_equal_0090() {
        Number num1 = new Complex(1.1, 1e-100);
        assertTrue(equal(num1, 1.1, 1e-15));
    }

    @Test
    public void test_equal_0100() {
        Number num1 = new Complex(1.1, 1e-100);
        assertTrue(equal(1.1, num1, 1e-15));
    }

    /**
     * test for Complex vs. Complex
     */
    @Test
    public void test_equal_0110() {
        Number num1 = new Complex(1.1, -9.9);
        Number num2 = new Complex(1.1, -9.9);

        assertTrue(equal(num1, num2, 1e-15));
    }

    /**
     * test for Complex vs. Double
     */
    @Test
    public void test_equal_0120() {
        Number num1 = new Double(1.1);
        Number num2 = new Complex(1.1, -9.9);

        assertFalse(equal(num1, num2, 1e-15));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for compare of NumberUtils">
    @Test
    public void test_compare_0010() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1, 0);
        assertEquals(0, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0011() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1 + 1e-15, 0);
        assertEquals(-1, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0012() {
        Number num1 = new Double(1);
        Number num2 = new Complex(1 + 1e-16, 0);
        assertEquals(0, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0020() {
        Number num1 = new Complex(1, 0);
        Number num2 = new Complex(1, 0);
        assertEquals(0, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0021() {
        Number num1 = new Complex(1, 2e-15);
        Number num2 = new Complex(1, 0);
        assertEquals(1, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0022() {
        Number num1 = new Complex(1, 1e-16);
        Number num2 = new Complex(1, 0);
        assertEquals(0, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0030() {
        Number num1 = new Double(1);
        Number num2 = new Double(1);
        assertEquals(0, compare(num1, num2, 1e-15));
    }

    @Test
    public void test_compare_0040() {
        assertEquals(0, compare(1.1, 1.1, 1e-15));
    }

    @Test
    public void test_compare_0050() {
        Number num1 = new Double(1.1);
        assertEquals(0, compare(num1, 1.1, 1e-15));
    }

    @Test
    public void test_compare_0060() {
        Number num1 = new Complex(1.1, 0);
        assertEquals(0, compare(num1, 1.1, 1e-15));
    }

    /**
     * test for Complex vs. int
     */
    @Test
    public void test_compare_0070() {
        Number num1 = new Complex(1, 0);
        assertEquals(0, compare(num1, 1, 1e-15));
    }

    /**
     * test for int
     */
    @Test
    public void test_compare_0080() {
        assertEquals(0, compare(1, 1, 1e-15));
    }

    @Test
    public void test_compare_0090() {
        Number num1 = new Complex(1.1, 1e-100);
        assertEquals(0, compare(num1, 1.1, 1e-15));
    }

    @Test
    public void test_compare_0100() {
        Number num1 = new Complex(1.1, 1e-100);
        assertEquals(0, compare(1.1, num1, 1e-15));
    }

    /**
     * test for Complex vs. Complex
     */
    @Test
    public void test_compare_0110() {
        Number num1 = new Complex(1.1, -9.9);
        Number num2 = new Complex(1.1, -9.9);

        assertEquals(0, compare(num1, num2, 1e-15));
    }

    /**
     * test for Complex vs. Double
     */
    @Test
    public void test_compare_0120() {
        Number num1 = new Double(1.1);
        Number num2 = new Complex(1.1, -9.9);

        assertEquals(-1, compare(num1, num2, 1e-15));
    }
    //</editor-fold>
}
