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
package com.numericalmethod.suanshu.number.complex;

import com.numericalmethod.suanshu.test.NumberAssert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ElementaryFunctionTest {

    //<editor-fold defaultstate="collapsed" desc="tests for sqrt">
    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.87677190747824, 1.51270380203776);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.87677190747824, -1.51270380203776);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(1.51270380203776, 1.87677190747824);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(1.51270380203776, -1.87677190747824);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(1.11085552615991, 0);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(1.68493323309857, 1.68493323309857);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0070() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0080() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0090() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(complex, result.multiply(result), 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0100() {
        Complex complex = new Complex(0, -0);
        Complex result = ElementaryFunction.sqrt(complex);
        assertEquals(complex, result.multiply(result));
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0110() {
        Complex complex = new Complex(-123.456789, -999.888777);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(complex, result.multiply(result), 1e-12);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0120() {
        Complex complex = new Complex(-123456.789, 987654.321);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(complex.ZERO(), complex.minus(result.multiply(result)), 1e-9);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sqrt_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.sqrt(complex);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sqrt_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.sqrt(complex);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sqrt_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.sqrt(complex);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sqrt_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sqrt(complex);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sqrt_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sqrt(complex);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0180() {
        Complex complex = new Complex(-1, 0);
        Complex result = ElementaryFunction.sqrt(complex);
        assertEquals(complex, result.multiply(result));
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0190() {
        Complex complex = new Complex(-5, 0);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(complex, result.multiply(result), 1e-14);
    }

    /**
     * Test of sqrt method, of class ElementaryFunction.
     */
    @Test
    public void test_sqrt_0200() {
        Complex complex = Complex.I;
        Complex expResult = new Complex(0.707106781186548, 0.707106781186548);
        Complex result = ElementaryFunction.sqrt(complex);
        NumberAssert.assertEquals(complex, result.multiply(result), 1e-14);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for log">
    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.75967447099878, 1.35679413815657);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.75967447099878, -1.35679413815657);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(1.75967447099878, 1.78479851543322);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(1.75967447099878, -1.78479851543322);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0.210260925483196, 0);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(1.73659905805078, 1.5707963267949);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0070() {
        Complex complex = new Complex(0, 0);
        Complex expResult = Complex.NEGATIVE_INFINITY;
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0080() {
        Complex complex = new Complex(0, -0);
        Complex expResult = Complex.NEGATIVE_INFINITY;
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0090() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = Complex.NEGATIVE_INFINITY;
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = Complex.NEGATIVE_INFINITY;
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0110() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(2.48393031456536, 1.56049809532671);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0120() {
        Complex complex = new Complex(-123.456789, -999.888777);
        Complex expResult = new Complex(6.91520901623403, -1.69364509113879);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0130() {
        Complex complex = new Complex(-123456.789, 987654.321);
        Complex expResult = new Complex(13.810840131106, 1.6951513202201);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_log_0140() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.log(complex);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_log_0150() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.log(complex);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_log_0160() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.log(complex);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_log_0170() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.log(complex);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_log_0180() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.log(complex);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0190() {
        Complex complex = new Complex(Math.E, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0200() {
        Complex complex = new Complex(1, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of log method, of class ElementaryFunction.
     */
    @Test
    public void test_log_0210() {
        Complex complex = Complex.I;
        Complex expResult = new Complex(0, 1.5707963267949);
        Complex result = ElementaryFunction.log(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for exp">
    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(2.82488480915939, -1.95418816956349);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(2.82488480915939, 1.95418816956349);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(0.239420846448682, -0.165625651056588);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(0.239420846448682, 0.165625651056588);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(3.43494186080076, 0);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0.82239668781493, -0.568914482036654);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0070() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0080() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0090() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0110() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(0.947168991328382, -0.618819330531157);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0120() {
        Complex complex = new Complex(-123.456789, -999.888777);
        Complex expResult = new Complex(1.57313883910376e-54, -1.83586027270222e-54);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0130() {
        Complex complex = new Complex(-123456.789, 987654.321);
        Complex expResult = complex.ZERO();   // close to zero
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_exp_0140() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.exp(complex);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_exp_0150() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.exp(complex);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_exp_0160() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.exp(complex);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_exp_0170() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.exp(complex);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_exp_0180() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.exp(complex);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0190() {
        Complex complex = new Complex(1, 0);
        Complex expResult = new Complex(Math.E, 0);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0200() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of exp method, of class ElementaryFunction.
     */
    @Test
    public void test_exp_0210() {
        Complex complex = Complex.I;
        Complex expResult = new Complex(0.54030230586814, 0.841470984807897);
        Complex result = ElementaryFunction.exp(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for pow">
    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0010() {
        Complex complex1 = new Complex(1.234, 5.678);
        Complex complex2 = new Complex(2.345, 6.789);
        Complex expResult = new Complex(-0.00517811289492325, 0.00339143380661985);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0020() {
        Complex complex1 = new Complex(1.234, 5.678);
        Complex complex2 = new Complex(-2.345, -6.789);
        Complex expResult = new Complex(-135.146982282522, -88.515266831082);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-12);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0030() {
        Complex complex1 = new Complex(-0.1234, -0.5678);
        Complex complex2 = new Complex(2.345, 6.789);
        Complex expResult = new Complex(-880.472624721709, -51211.73267081133);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0040() {
        Complex complex1 = new Complex(0.1234, -0.5678);
        Complex complex2 = new Complex(2.345, 6.789);
        Complex expResult = new Complex(2337.260449515937, -1545.761855065327);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-11);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0050() {
        Complex complex1 = new Complex(0.1234, 0.5678);
        Complex complex2 = new Complex(2.345, -6.789);
        Complex expResult = new Complex(2337.260449515937, +1545.761855065327);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-11);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0060() {
        Complex complex1 = new Complex(0.1234, 0);
        Complex complex2 = new Complex(2.345, 6.789);
        Complex expResult = new Complex(-0.0004999078402352673, -0.00738142718546403);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0070() {
        Complex complex1 = new Complex(0.1234, 0.5678);
        Complex complex2 = new Complex(2.345, 0);
        Complex expResult = new Complex(-0.279730339815568, -0.01122028917695776);
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0080() {
        Complex complex1 = new Complex(0.1234, 0.5678);
        Complex complex2 = new Complex(0, 0);
        Complex expResult = complex1.ONE();
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0090() {
        Complex complex1 = new Complex(0, 0);
        Complex complex2 = new Complex(2.345, 6.789);
        Complex expResult = complex1.ZERO();
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0100() {
        Complex complex1 = new Complex(0, 0);
        Complex complex2 = new Complex(0, 0);
        Complex expResult = complex1.ZERO();
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0110() {
        Complex complex1 = new Complex(0, 0);
        Complex complex2 = complex1.ONE();
        Complex expResult = complex1.ZERO();
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0120() {
        Complex complex1 = new Complex(0, 1);
        Complex complex2 = complex1.ONE();
        Complex expResult = Complex.I;
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test
    public void test_pow_0130() {
        Complex complex1 = new Complex(1, 0);
        Complex complex2 = Complex.I;
        Complex expResult = complex1.ONE();
        Complex result = ElementaryFunction.pow(complex1, complex2);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0140() {
        Complex complex1 = new Complex(Double.NaN, Double.NaN);
        Complex complex2 = new Complex(1, 1);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0150() {
        Complex complex1 = new Complex(1, 1);
        Complex complex2 = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0160() {
        Complex complex1 = new Complex(Double.POSITIVE_INFINITY, 1);
        Complex complex2 = new Complex(1, 1);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0170() {
        Complex complex1 = new Complex(1, 1);
        Complex complex2 = new Complex(Double.POSITIVE_INFINITY, 1);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0180() {
        Complex complex1 = new Complex(Double.NEGATIVE_INFINITY, 1);
        Complex complex2 = new Complex(1, 1);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }

    /**
     * Test of pow method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_pow_0190() {
        Complex complex1 = new Complex(1, 1);
        Complex complex2 = new Complex(Double.NEGATIVE_INFINITY, 1);
        Complex result = ElementaryFunction.pow(complex1, complex2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for sin">
    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(137.9709026081064, 48.3075045293216);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(137.9709026081064, -48.3075045293216);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-137.9709026081064, 48.3075045293216);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-137.9709026081064, -48.3075045293216);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0.943818209374634, 0);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0, 146.1803480893846);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(9898.06770064497, 79767.12172439139);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(-0.334754725112355, 1.128176301977986);
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test
    public void test_sin_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sin_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.sin(complex);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sin_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.sin(complex);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sin_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.sin(complex);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sin_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sin(complex);
    }

    /**
     * Test of sin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sin_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sin(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for asin">
    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(0.2109896732051443, 2.45950487589494);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(0.2109896732051443, -2.45950487589494);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-0.2109896732051443, 2.45950487589494);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-0.2109896732051443, -2.45950487589494);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(1.570796326794897, -0.671422229481383);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0, 2.43741198386183);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(0.01026259219905693, 3.17881211312171);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(1.490705483041202, 3.20802384060582);
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test
    public void test_asin_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.asin(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_asin_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.asin(complex);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_asin_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.asin(complex);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_asin_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.asin(complex);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_asin_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.asin(complex);
    }

    /**
     * Test of asin method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_asin_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.asin(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for sinh">
    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.292731981355355, -1.059906910310037);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.292731981355355, 1.059906910310037);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-1.292731981355355, -1.059906910310037);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-1.292731981355355, 1.059906910310037);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(1.571908059102337, 0);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0, -0.568914482036654);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(0.1036158184642408, -0.5511234156505894);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(63309.85809807696, 95972.1755148712);
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test
    public void test_sinh_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.sinh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sinh_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.sinh(complex);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sinh_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.sinh(complex);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sinh_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.sinh(complex);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sinh_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sinh(complex);
    }

    /**
     * Test of sinh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_sinh_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.sinh(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for cos">
    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(48.30863484966304, -137.9676743794837);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(48.30863484966304, 137.9676743794837);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(48.30863484966304, 137.9676743794837);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(48.30863484966304, -137.9676743794837);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0.33046510807173, 0);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(146.1837684817766, 0);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(79767.12173056458, -9898.06769987896);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(1.491586010839885, 0.253195152745003);
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test
    public void test_cos_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cos_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.cos(complex);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cos_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.cos(complex);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cos_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.cos(complex);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cos_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.cos(complex);
    }

    /**
     * Test of cos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cos_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.cos(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for acos">
    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.359806653589752, -2.45950487589494);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.359806653589752, 2.45950487589494);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(1.78178600000004, -2.45950487589494);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(1.78178600000004, 2.45950487589494);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0, 0.671422229481383);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(1.570796326794897, -2.43741198386183);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(1.56053373459584, -3.17881211312171);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(0.0800908437536947, -3.20802384060582);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = new Complex(1.570796326794897, 0);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = new Complex(1.570796326794897, 0);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = new Complex(1.570796326794897, 0);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = new Complex(1.570796326794897, 0);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test
    public void test_acos_0130() {
        Complex complex = new Complex(1.0, 0.0);
        Complex expResult = new Complex(0, 0);
        Complex result = ElementaryFunction.acos(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_acos_0140() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.acos(complex);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_acos_0150() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.acos(complex);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_acos_0160() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.acos(complex);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_acos_0170() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.acos(complex);
    }

    /**
     * Test of acos method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_acos_0180() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.acos(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for cosh">
    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.532152827804037, -0.894281259253449);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.532152827804037, 0.894281259253449);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(1.532152827804037, 0.894281259253449);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(1.532152827804037, -0.894281259253449);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(1.863033801698422, 0);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0.8223966878149286, 0);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(0.843553172864141, -0.0676959148805678);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(63309.85810047165, 95972.1755112411);
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-10);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test
    public void test_cosh_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ONE();
        Complex result = ElementaryFunction.cosh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cosh_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.cosh(complex);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cosh_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.cosh(complex);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cosh_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.cosh(complex);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cosh_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.cosh(complex);
    }

    /**
     * Test of cosh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_cosh_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.cosh(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for tan">
    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(0.00001459599684135905, 1.000018287732633);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(0.00001459599684135905, -1.000018287732633);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-0.00001459599684135905, 1.000018287732633);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-0.00001459599684135905, -1.000018287732633);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(2.85602983891955, 0);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0, 0.999976602105504);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(1.89150019936e-11, 9.99999999924957e-01);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(-0.0933475801444403, 0.772205859012093);
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test
    public void test_tan_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tan_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.tan(complex);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tan_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.tan(complex);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tan_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.tan(complex);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tan_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.tan(complex);
    }

    /**
     * Test of tan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tan_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.tan(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for atan">
    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(1.533202248049476, 0.1695504179623492);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(1.533202248049476, -0.1695504179623493);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-1.533202248049476, 0.1695504179623492);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-1.533202248049476, -0.1695504179623493);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0.889762448959189, 0);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0060() {
        Complex complex = new Complex(1e-15, 5.678);
        Complex expResult = new Complex(1.570796326794897, 0.1779739366797378);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(1.569931299719718, 0.08360449654862497);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(1.490480746851707, 0.006397515229342295);
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test
    public void test_atan_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.atan(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_atan_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.atan(complex);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_atan_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.atan(complex);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_atan_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.atan(complex);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_atan_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.atan(complex);
    }

    /**
     * Test of atan method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_atan_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.atan(complex);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for tanh">
    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0010() {
        Complex complex = new Complex(1.234, 5.678);
        Complex expResult = new Complex(0.9305060883129296, -0.1486619022444423);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0020() {
        Complex complex = new Complex(1.234, -5.678);
        Complex expResult = new Complex(0.9305060883129296, 0.1486619022444423);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0030() {
        Complex complex = new Complex(-1.234, 5.678);
        Complex expResult = new Complex(-0.9305060883129296, -0.1486619022444423);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0040() {
        Complex complex = new Complex(-1.234, -5.678);
        Complex expResult = new Complex(-0.9305060883129296, 0.1486619022444423);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0050() {
        Complex complex = new Complex(1.234, 0);
        Complex expResult = new Complex(0.84373566258933, 0);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0060() {
        Complex complex = new Complex(0, 5.678);
        Complex expResult = new Complex(0, -0.691776232159002);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0070() {
        Complex complex = new Complex(0.123456, 11.987654);
        Complex expResult = new Complex(0.174141866518699, -0.639360671060459);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0080() {
        Complex complex = new Complex(12.3456, 0.987654);
        Complex expResult = new Complex(1.0000000000148868, 3.4772253669e-11);
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-13);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0090() {
        Complex complex = new Complex(0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0100() {
        Complex complex = new Complex(-0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0110() {
        Complex complex = new Complex(0, -0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test
    public void test_tanh_0120() {
        Complex complex = new Complex(-0, 0);
        Complex expResult = complex.ZERO();
        Complex result = ElementaryFunction.tanh(complex);
        NumberAssert.assertEquals(expResult, result, 1e-14);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tanh_0130() {
        Complex complex = new Complex(Double.NaN, Double.NaN);
        Complex result = ElementaryFunction.tanh(complex);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tanh_0140() {
        Complex complex = new Complex(-12345678, Double.NaN);
        Complex result = ElementaryFunction.tanh(complex);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tanh_0150() {
        Complex complex = new Complex(Double.NaN, 9876543);
        Complex result = ElementaryFunction.tanh(complex);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tanh_0160() {
        Complex complex = new Complex(Double.POSITIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.tanh(complex);
    }

    /**
     * Test of tanh method, of class ElementaryFunction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_tanh_0170() {
        Complex complex = new Complex(Double.NEGATIVE_INFINITY, 9876543);
        Complex result = ElementaryFunction.tanh(complex);
    }
    //</editor-fold>
}
