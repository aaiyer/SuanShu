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
package com.numericalmethod.suanshu.number.big;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BigDecimalUtilsTest {

    static boolean equals(String bigDecimal, BigDecimal n2, int precision) {
        BigDecimal n1 = new BigDecimal(bigDecimal);
        return BigDecimalUtils.equals(n1, n2, precision);
    }

    //<editor-fold defaultstate="collapsed" desc="get parts">
    /**
     * Test of getWhole method, of class BigDecimalUtils.
     */
    @Test
    public void test_getWhole_0010() {
        assertEquals(0, new BigDecimal("1").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("1.234567890123456789"))));
        assertEquals(0, new BigDecimal("0").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("0.234567890123456789"))));
        assertEquals(0, new BigDecimal("-1").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("-1.234567890123456789"))));
        assertEquals(0, new BigDecimal("0").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("-0.234567890123456789"))));
        assertEquals(0, new BigDecimal("-123456").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("-123456.234567890123456789"))));
        assertEquals(0, new BigDecimal("24680").compareTo(
                BigDecimalUtils.getWhole(new BigDecimal("24680.234567890123456789"))));
    }

    /**
     * Test of getFractional method, of class BigDecimalUtils.
     */
    @Test
    public void test_getFractional_0010() {
        assertEquals(0, new BigDecimal("0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("1.234567890123456789"))));
        assertEquals(0, new BigDecimal("0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("0.234567890123456789"))));
        assertEquals(0, new BigDecimal("-0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("-1.234567890123456789"))));
        assertEquals(0, new BigDecimal("-0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("-0.234567890123456789"))));
        assertEquals(0, new BigDecimal("-0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("-123456.234567890123456789"))));
        assertEquals(0, new BigDecimal("0.234567890123456789").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("24680.234567890123456789"))));
        assertEquals(0, new BigDecimal("0").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("24680.0"))));
        assertEquals(0, new BigDecimal("0").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("24680.0000000000"))));
        assertEquals(0, new BigDecimal("0.00000000001").compareTo(
                BigDecimalUtils.getFractional(new BigDecimal("24680.00000000001"))));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pow">
    /**
     * Test of pow method, of class BigDecimalUtils.
     */
    @Test
    public void test_pow_0010() {
        assertEquals(0, new BigDecimal("64").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(4d), 3)));
        assertEquals(0, new BigDecimal("915.0625").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(5.5d), 4)));
        assertEquals(0, new BigDecimal("0.00002999373436917227").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(0.12456789), 5, 20)));
//        assertEquals(0, new BigDecimal("266635235464391229440").compareTo(
//                BigDecimalUtils.pow(BigDecimal.valueOf(23), 15)));//R is not able to give correct answer here? the answer won't end with 0 anyway; the last digit can only be 1,3,7,9
        assertEquals(0, new BigDecimal("41426511213649").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(23), 10)));
        assertEquals(0, new BigDecimal("23465261.9918447").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(3.1d), 15).setScale(7, RoundingMode.HALF_EVEN)));
    }

    /**
     * Test of pow method, of class BigDecimalUtils.
     */
    @Test
    public void test_pow_0020() {
        assertEquals(0, new BigDecimal("0.015625").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(4d), -3)));
        assertEquals(0, new BigDecimal("0.001092821528584113").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(5.5d), -4, 18)));
        assertEquals(0, new BigDecimal("33340.2965996727").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(0.12456789), -5, 20).setScale(10, RoundingMode.HALF_EVEN)));
//        assertEquals(0, new BigDecimal("3.750442053385509e-21").compareTo(
//                BigDecimalUtils.pow(BigDecimal.valueOf(23), -15)));
        assertEquals(0, new BigDecimal("0.000000000293700812439916").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(23), -7, 25)));
        assertEquals(0, new BigDecimal("0.003492943259127733").compareTo(
                BigDecimalUtils.pow(BigDecimal.valueOf(3.1d), -5, 18)));
    }

    /**
     * Test of pow method, of class BigDecimalUtils.
     */
    @Test
    public void test_pow_0030() {
        assertTrue(equals("193.3053163068724",
                BigDecimalUtils.pow(BigDecimal.valueOf(4.5), BigDecimal.valueOf(3.5)),
                13));
        assertTrue(equals("44644681.42172585",
                BigDecimalUtils.pow(BigDecimal.valueOf(31.123456), BigDecimal.valueOf(5.123456)),
                7));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="log">
    /**
     * Test of logByNewton method, of class BigDecimalUtils.
     * for small x
     */
    @Test
    public void test_log_0010() {
        assertTrue(equals("0.693147180559945",
                BigDecimalUtils.log(BigDecimal.valueOf(2), 15), 10));
        assertTrue(equals("3.145444546782318",
                BigDecimalUtils.log(BigDecimal.valueOf(23.23), 15), 10));
    }

    /**
     * Test of logByNewton method, of class BigDecimalUtils.
     * for very small x
     */
    @Test
    public void test_log_0020() {
        assertTrue(equals("-8.51719319141624",
                BigDecimalUtils.log(BigDecimal.valueOf(0.0002), 15), 14));
        assertTrue(equals("-15.0194833622902",
                BigDecimalUtils.log(BigDecimal.valueOf(0.0000003), 15), 13));
        assertTrue(equals("-63.37377031516517",
                BigDecimalUtils.log(BigDecimal.valueOf(0.0000000000000000000000000003), 30), 14));
    }

    /**
     * Test of logByNewton method, of class BigDecimalUtils.
     * for big x
     */
    @Test
    public void test_log_0030() {
        assertTrue(equals("4.612077745577306",
                BigDecimalUtils.log(BigDecimal.valueOf(100.693147180559945)), 15));
        assertTrue(equals("6.90687835138251",
                BigDecimalUtils.log(BigDecimal.valueOf(999.1234567890123456789)), 14));
        assertTrue(equals("18.30289769968487",
                BigDecimalUtils.log(BigDecimal.valueOf(88888888.1234567890123456789)), 14));
        assertTrue(equals("18.30289770829598",
                BigDecimalUtils.log(BigDecimal.valueOf(88888888.88888888)), 14));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="exp">
    /**
     * Test of exp method, of class BigDecimalUtils.
     */
    @Test
    public void test_exp_0010() {
        assertTrue(equals("1",
                BigDecimalUtils.exp(0),
                22));
        assertTrue(equals("1.105170918075647",
                BigDecimalUtils.exp(0.1),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("1.22140275816017",
                BigDecimalUtils.exp(0.2),
                14));//R has up to only 14 digits after the decimal point
        assertTrue(equals("1.648721270700128",
                BigDecimalUtils.exp(0.5),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("2.718281828459045",
                BigDecimalUtils.exp(1.0),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("3.004166023946433",
                BigDecimalUtils.exp(1.1),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("4.481689070338065",
                BigDecimalUtils.exp(1.5),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("7.38905609893065",
                BigDecimalUtils.exp(2.0),
                14));//R has up to only 14 digits after the decimal point
        assertTrue(equals("12.18249396070347",
                BigDecimalUtils.exp(2.5),
                14));//R has up to only 14 digits after the decimal point
        assertTrue(equals("20.08553692318767",
                BigDecimalUtils.exp(3.0),
                14));//R has up to only 14 digits after the decimal point
        assertTrue(equals("22.72479886047115",
                BigDecimalUtils.exp(3.123456789),
                14));//R has up to only 14 digits after the decimal point
        assertTrue(equals("22026.46579480672",
                BigDecimalUtils.exp(10),
                11));//R has up to only 11 digits after the decimal point
    }

    /**
     * Test of exp method, of class BigDecimalUtils.
     */
    @Test
    public void test_exp_0020() {
        assertTrue(equals("0.904837418035960",
                BigDecimalUtils.exp(-0.1),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.818730753077982",
                BigDecimalUtils.exp(-0.2),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.606530659712633",
                BigDecimalUtils.exp(-0.5),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.3678794411714423",
                BigDecimalUtils.exp(-1.0),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.3328710836980796",
                BigDecimalUtils.exp(-1.1),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.2231301601484298",
                BigDecimalUtils.exp(-1.5),
                15));//R has up to only 15 digits after the decimal point
        assertTrue(equals("0.1353352832366127",
                BigDecimalUtils.exp(-2.0),
                15));//R has up to only 14 digits after the decimal point
        assertTrue(equals("0.0820849986238988",
                BigDecimalUtils.exp(-2.5),
                15));//R has up to only 14 digits after the decimal point
        assertTrue(equals("0.04978706836786394",
                BigDecimalUtils.exp(-3.0),
                16));//R has up to only 16 digits after the decimal point
        assertTrue(equals("0.04400478992751214",
                BigDecimalUtils.exp(-3.123456789, 18),
                16));//need more than the default precision
        assertTrue(equals("0.00004539992976248485",
                BigDecimalUtils.exp(-10, 20),
                19));//need more than the default precision
    }

    /**
     * Test of exp method, of class BigDecimalUtils.
     */
    @Test
    public void test_exp_0030() {
        assertTrue(equals("67710.73408331936",
                BigDecimalUtils.exp(11.123),
                10));
        assertTrue(equals("0.0000147687071117776",
                BigDecimalUtils.exp(-11.123),
                10));
        assertTrue(equals("0.02127973643837717",
                BigDecimalUtils.exp(-3.85, 17),
                10));
//        assertTrue(equals("39506622896066568192",
//                BigDecimalUtils.exp(45.123),
//                10));//is R correct?
    }
    //</editor-fold>
}
