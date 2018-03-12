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

import com.numericalmethod.suanshu.mathstructure.Field.InverseNonExistent;
import com.numericalmethod.suanshu.number.NumberUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ComplexTest {

    /**
     * Test of creation and identification of different {@link Number}
     */
    @Test
    public void testNumber_001() {
        Number c1 = new Complex(1, 0);
        Number d1 = new Double(1);

        assertTrue(c1 instanceof Complex);
        assertTrue(d1 instanceof Double);
    }

    /**
     * Test of creation and identification of different {@link Number}
     */
    @Test
    public void testNumber_002() {
        Complex c1 = new Complex(1, -1);

        assertEquals(1, c1.real(), 0);
        assertEquals(-1, c1.imaginary(), 0);
        assertEquals(new Complex(1, 1), c1.conjugate());
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0010() {
        Complex c1 = new Complex(1.5, -2.5);
        Complex c2 = new Complex(-7.23, 8.6);

        Number add = new Complex(-5.73, 6.1);
        assertEquals(add, c1.add(c2));

        Number minus = new Complex(8.73, -11.1);
        assertEquals(minus, c1.minus(c2));

        Number product = new Complex(10.655, 30.975);
        assertEquals(product, c1.multiply(c2));

        Number divide = new Complex(-0.256232725383002, 0.040995651688268);
        assertTrue(NumberUtils.equal(divide, c1.divide(c2), 1e-14));

        assertEquals(c1.ZERO(), c1.multiply(c1.ZERO()));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0020() {
        Complex c1 = new Complex(1.5, -2.5);
        Complex c2 = new Complex(0, 8.6);

        Number add = new Complex(1.5, 6.1);
        assertEquals(add, c1.add(c2));

        Number minus = new Complex(1.5, -11.1);
        assertEquals(minus, c1.minus(c2));

        Number product = new Complex(21.5, 12.9);
        assertEquals(product, c1.multiply(c2));

        Number divide = new Complex(-0.290697674418605, -0.174418604651163);
        assertTrue(NumberUtils.equal(divide, c1.divide(c2), 1e-14));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0030() {
        Complex c1 = new Complex(1.5, -2.5);
        Complex c2 = new Complex(-7.23, 0);

        Number add = new Complex(-5.73, -2.5);
        assertEquals(add, c1.add(c2));

        Number minus = new Complex(8.73, -2.5);
        assertEquals(minus, c1.minus(c2));

        Number product = new Complex(-10.845, +18.075);
        assertTrue(NumberUtils.equal(product, c1.multiply(c2), 1e-14));

        Number divide = new Complex(-0.20746887966805, 0.345781466113416);
        assertTrue(NumberUtils.equal(divide, c1.divide(c2), 1e-14));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0040() {
        Complex c1 = new Complex(0, -2.5);
        Complex c2 = new Complex(-7.23, 8.6);

        Number add = new Complex(-7.23, 6.1);
        assertEquals(add, c1.add(c2));

        Number minus = new Complex(7.23, -11.1);
        assertEquals(minus, c1.minus(c2));

        Number product = new Complex(21.5, 18.075);
        assertTrue(NumberUtils.equal(product, c1.multiply(c2), 1e-14));

        Number divide = new Complex(-0.170320098801501, 0.143187710969169);
        assertTrue(NumberUtils.equal(divide, c1.divide(c2), 1e-14));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0050() {
        Complex c1 = new Complex(1.5, 0);
        Complex c2 = new Complex(-7.23, 8.6);

        Number add = new Complex(-5.73, 8.6);
        assertEquals(add, c1.add(c2));

        Number minus = new Complex(8.73, -8.6);
        assertEquals(minus, c1.minus(c2));

        Number product = new Complex(-10.845, 12.9);
        assertTrue(NumberUtils.equal(product, c1.multiply(c2), 1e-14));

        Number divide = new Complex(-0.085912626581501, -0.102192059280901);
        assertTrue(NumberUtils.equal(divide, c1.divide(c2), 1e-14));

        assertEquals(c1.ZERO(), c1.multiply(c1.ZERO()));
    }

    /**
     * Test of divide, of class Complex.
     * Test for division by 0
     */
    @Test(expected = ArithmeticException.class)
    public void test_0060() {
        Complex c1 = new Complex(1.5, -2.5);
        Complex c2 = new Complex(0, 0);

        c1.divide(c2);
    }

    /**
     * Test of divide, of class Complex.
     * Test for division by 0
     */
    @Test(expected = ArithmeticException.class)
    public void test_0070() {
        Complex c1 = new Complex(1.5, -2.5);
        c1.divide(c1.ZERO());
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0080() {
        Complex c1 = new Complex(1.5, Double.NaN);
        Complex c2 = new Complex(-7.23, 8.6);

        assertEquals(Complex.NaN, c1.add(c2));
        assertEquals(Complex.NaN, c1.minus(c2));
        assertEquals(Complex.NaN, c1.multiply(c2));
        assertEquals(Complex.NaN, c1.divide(c2));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test
    public void test_0090() {
        Complex inf1 = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Complex inf2 = new Complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Complex inf3 = new Complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Complex inf4 = new Complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Complex c2 = new Complex(1, 0);

        assertEquals(c2.ZERO(), c2.divide(inf1));
        assertEquals(c2.ZERO(), c2.divide(inf2));
        assertEquals(c2.ZERO(), c2.divide(inf3));
        assertEquals(c2.ZERO(), c2.divide(inf4));
    }

    /**
     * Test of methods, of class Complex.
     */
    @Test(expected = InverseNonExistent.class)
    public void test_0100() throws InverseNonExistent {
        new Complex(0, 0).inverse();
    }
}
