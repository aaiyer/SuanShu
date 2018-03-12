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

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ScientificNotationTest {

    //<editor-fold defaultstate="collapsed" desc="ctors">
    @Test
    public void test_ScientificNotation_0010() {
        ScientificNotation x = new ScientificNotation(5);
        assertEquals(5, x.significand().doubleValue(), 0);
        assertEquals(0, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0020() {
        ScientificNotation x = new ScientificNotation(723);
        assertEquals(7.23, x.significand().doubleValue(), 0);
        assertEquals(2, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0030() {
        ScientificNotation x = new ScientificNotation(-86);
        assertEquals(-8.6, x.significand().doubleValue(), 0);
        assertEquals(1, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0040() {
        ScientificNotation x = new ScientificNotation(0.5);
        assertEquals(5, x.significand().doubleValue(), 0);
        assertEquals(-1, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0050() {
        ScientificNotation x = new ScientificNotation(-0.5);
        assertEquals(-5, x.significand().doubleValue(), 0);
        assertEquals(-1, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0060() {
        ScientificNotation x = new ScientificNotation(-0.123456789012345678901234567890);
        assertEquals(-1.23456789012345678901234567890, x.significand().doubleValue(), 0);
        assertEquals(-1, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0070() {
        ScientificNotation x = new ScientificNotation(-0.0000000000123456789);
        assertEquals(-1.23456789, x.significand().doubleValue(), 0);
        assertEquals(-11, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0080() {
        ScientificNotation x = new ScientificNotation(new BigInteger("123456789012345678901234567890"));
        assertEquals(1.23456789012345678901234567890, x.significand().doubleValue(), 0);
        assertEquals(29, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0090() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("123456789012345678901234567890"));
        assertEquals(1.23456789012345678901234567890, x.significand().doubleValue(), 0);
        assertEquals(29, x.exponent());
    }

    @Test
    public void test_ScientificNotation_0100() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("123456789012345678901234567890.1234567890"));
        assertEquals(1.234567890123456789012345678901234567890, x.significand().doubleValue(), 0);
        assertEquals(29, x.exponent());
    }

    /**
     * special case for x = 0
     */
    @Test
    public void test_ScientificNotation_0110() {
        ScientificNotation x = new ScientificNotation(0);
        assertEquals(0, x.significand().doubleValue(), 0);
        assertEquals(0, x.exponent());
    }

    /**
     * special case for x = 0
     */
    @Test
    public void test_ScientificNotation_0120() {
        ScientificNotation x = new ScientificNotation(BigInteger.ZERO);
        assertEquals(0, x.significand().doubleValue(), 0);
        assertEquals(0, x.exponent());
    }

    /**
     * special case for x = 0
     */
    @Test
    public void test_ScientificNotation_0130() {
        ScientificNotation x = new ScientificNotation(BigDecimal.ZERO);
        assertEquals(0, x.significand().doubleValue(), 0);
        assertEquals(0, x.exponent());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Test of value method, of class ScientificNotation">
    @Test
    public void test_value_0010() {
        ScientificNotation x = new ScientificNotation(0, 0);
        assertEquals(new BigDecimal(0), x.bigDecimalValue());
    }

    @Test
    public void test_value_0020() {
        ScientificNotation x = new ScientificNotation(1, 0);
        assertEquals(new BigDecimal(1), x.bigDecimalValue());
    }

    @Test
    public void test_value_0030() {
        ScientificNotation x = new ScientificNotation(1, 2);
        assertTrue(new BigDecimal("100").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0040() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("1.23456789"), 2);
        assertTrue(new BigDecimal("123.456789").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0050() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("-1.23456789"), 2);
        assertTrue(new BigDecimal("-123.456789").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0060() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("1.23456789"), -2);
        assertTrue(new BigDecimal("0.0123456789").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0070() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("1.23456789"), 0);
        assertTrue(new BigDecimal("1.23456789").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0080() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("1.23456789"), -1);
        assertTrue(new BigDecimal("0.123456789").compareTo(x.bigDecimalValue()) == 0);
    }

    @Test
    public void test_value_0090() {
        ScientificNotation x = new ScientificNotation(new BigDecimal("1.23456789"), 1);
        assertTrue(new BigDecimal("12.3456789").compareTo(x.bigDecimalValue()) == 0);
    }    //</editor-fold>
}
