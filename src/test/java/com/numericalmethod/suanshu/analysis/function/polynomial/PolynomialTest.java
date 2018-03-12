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
package com.numericalmethod.suanshu.analysis.function.polynomial;

import com.numericalmethod.suanshu.test.NumberAssert;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PolynomialTest {

    @Test
    public void test_coefficient_0010() {
        double[] coefficients = new double[]{5, 4, 3, 2, 1, 0};
        double epsilon = SuanShuUtils.autoEpsilon(coefficients);
        Polynomial poly = new Polynomial(coefficients);
        for (int i = 0; i < coefficients.length; ++i) {
            assertEquals(coefficients[i], poly.getCoefficient(i), epsilon);
        }
    }

    @Test
    public void test_times_0010() {
        double scalar = 9.8765;
        double[] coefficients = new double[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        double[] inputs = new double[coefficients.length + 1];
        inputs[0] = scalar;
        System.arraycopy(coefficients, 0, inputs, 1, coefficients.length);

        double epsilon = SuanShuUtils.autoEpsilon(inputs);
        Polynomial poly = new Polynomial(coefficients).scaled(scalar);
        for (int i = 0; i < coefficients.length; ++i) {
            assertEquals(scalar * coefficients[i], poly.getCoefficient(i), epsilon);
        }
    }

    @Test
    public void test_times_0020() {
        double[] coeff1 = new double[]{1, -2, 3, -4, 5};
        double[] coeff2 = new double[]{2, 3, 7};
        double[] inputs = Arrays.copyOf(coeff1, coeff1.length + coeff2.length);
        System.arraycopy(coeff2, 0, inputs, coeff1.length, coeff2.length);
        double epsilon = SuanShuUtils.autoEpsilon(inputs);
        double[] expCoeff = new double[]{2, -1, 7, -13, 19, -13, 35};

        Polynomial poly1 = new Polynomial(coeff1);
        Polynomial poly2 = new Polynomial(coeff2);
        Polynomial result = poly1.multiply(poly2);
        assertArrayEquals(expCoeff, result.getCoefficients(), epsilon);

    }

    //<editor-fold defaultstate="collapsed" desc="tests for evaluate">
    @Test(expected = Polynomial.EvaluationException.class)
    public void test_evaluate_0010() {
        Polynomial instance = new Polynomial(new double[]{1});//p(x) = 1
        assertEquals(0, instance.degree());
        double result = instance.evaluate(new DenseVector(1., 2.));//supply two arguments instead of 1
        assertEquals(1., result, 1e-15);
    }

    @Test
    public void test_evaluate_0020() {
        Polynomial instance = new Polynomial(new double[]{1});//p(x) = 1
        assertEquals(0, instance.degree());
        double result = instance.evaluate(1.);
        assertEquals(1., result, 1e-15);
    }

    @Test
    public void test_evaluate_0030() {
        Polynomial instance = new Polynomial(new double[]{1});//p(x) = 1
        assertEquals(0, instance.degree());
        double result = instance.evaluate(123.);
        assertEquals(1., result, 1e-15);
    }

    @Test
    public void test_evaluate_0040() {
        Polynomial instance = new Polynomial(new double[]{1, 1});//p(x) = 1 + x
        assertEquals(1, instance.degree());
        double result = instance.evaluate(10.);
        assertEquals(11., result, 1e-15);
    }

    @Test
    public void test_evaluate_0050() {
        Polynomial instance = new Polynomial(new double[]{1, -1, 1});//p(x) = x^2 - x + 1
        assertEquals(2, instance.degree());
        double result = instance.evaluate(10.);
        assertEquals(91., result, 1e-15);
    }

    @Test
    public void test_evaluate_0060() {
        Polynomial instance = new Polynomial(new double[]{4, 1, -1, 1});//p(x) = 4*x^3 + x^2 - x + 1
        assertEquals(3, instance.degree());
        double result = instance.evaluate(5.);
        assertEquals(521., result, 1e-15);
    }

    @Test
    public void test_evaluate_0070() {
        Polynomial instance = new Polynomial(new double[]{2, 0, 0, 0, 4, 1, -1, 1});//p(x) = 2*x^7 + 4*x^3 + x^2 - x + 1
        assertEquals(7, instance.degree());
        double result = instance.evaluate(5.);
        assertEquals(156771., result, 1e-15);
    }

    @Test
    public void test_evaluate_0080() {
        Polynomial instance = new Polynomial(new double[]{2, 0, 0, 0, 4, 1, -1, 1});//p(x) = 2*x^7 + 4*x^3 + x^2 - x + 1
        assertEquals(7, instance.degree());
        double result = instance.evaluate(5.123);
        assertEquals(185785.314055786, result, 1e-9);
    }

    @Test
    public void test_evaluate_0090() {
        Polynomial instance = new Polynomial(new double[]{2, 0, 0, 0, 4, 1, -1, 1});//p(x) = 2*x^7 + 4*x^3 + x^2 - x + 1
        assertEquals(7, instance.degree());
        double result = instance.evaluate(0.);
        assertEquals(1., result, 1e-15);
    }

    /**
     * Leading coefficients are 0s.
     */
    @Test
    public void test_evaluate_0100() {
        Polynomial instance = new Polynomial(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 4, 1, -1, 1});//p(x) = 4*x^3 + x^2 - x + 1
        assertEquals(3, instance.degree());
        double result = instance.evaluate(0.);
        assertEquals(1., result, 1e-15);
        result = instance.evaluate(1.5);
        assertEquals(15.25, result, 1e-15);
    }

    /**
     * Evaluate for Complex number.
     */
    @Test
    public void test_evaluate_0110() {
        Polynomial instance = new Polynomial(new double[]{1, 0, 1});//p(x) = x^2 + 1
        assertEquals(2, instance.degree());
        double result1 = instance.evaluate(1.1);
        assertEquals(2.21, result1, 1e-15);
        Complex result2 = instance.evaluate(new Complex(0, 1));
        assertTrue(NumberUtils.equal(0, result2, 1e-15));
    }

    /**
     * Evaluate for Complex number.
     */
    @Test
    public void test_evaluate_0120() {
        Polynomial instance = new Polynomial(new double[]{1, 0, -1, 0, 1});//p(x) = x^4 - x^2 + 1
        assertEquals(4, instance.degree());
        double result1 = instance.evaluate(1.1);
        assertEquals(1.2541, result1, 1e-15);
        Complex result2 = instance.evaluate(new Complex(0, 1));
        assertTrue(NumberUtils.equal(3, result2, 1e-15));
    }

    /**
     * Evaluate for Complex number.
     */
    @Test
    public void test_evaluate_0130() {
        Polynomial instance = new Polynomial(new double[]{1, 0, -1, 0, 1});//p(x) = x^4 - x^2 + 1
        assertEquals(4, instance.degree());
        Number result = instance.evaluate(new Complex(2, 1));
        assertTrue(NumberUtils.equal(new Complex(-9, 20), result, 1e-14));
        result = instance.evaluate(new Complex(2.15, -1.312));
        assertTrue(NumberUtils.equal(new Complex(-25.312100423664, -27.09272337920), result, 1e-14));
    }

    @Test
    public void test_evaluate_0140() {
        Polynomial poly = new Polynomial(1, 0, -1, 0, 1);//p(x) = x^4 - x^2 + 1
        Double x = new Double(2.1);
        double[] inputs = Arrays.copyOf(poly.getCoefficients(), poly.degree() + 2);
        inputs[poly.degree() + 1] = x;

        Number result = poly.evaluate(new Double(2.1));
        Number expResult = new Double(x * x * x * x - x * x + 1);

        NumberAssert.assertEquals(expResult, result, SuanShuUtils.autoEpsilon(inputs));
    }
    // </editor-fold>

    /**
     * Test of getNormalization method, of class Polynomial.
     */
    @Test
    public void test_normalize_0010() {
        Polynomial instance = new Polynomial(new double[]{2, 1, -1, 1});//p(x) = 2*x^3 + x^2 - x + 1
        assertEquals(3, instance.degree());
        Polynomial expResult = new Polynomial(new double[]{1, 0.5, -0.5, 0.5});//p(x) = x^3 + 0.5x^2 - 0.5x + 0.5
        assertEquals(expResult, instance.getNormalization());
    }

    /**
     * Test of ZERO, of class Polynomial.
     */
    @Test
    public void test_ZERO_0010() {
        Polynomial instance = new Polynomial(new double[]{0});//p(x) = 0
        assertEquals(instance.ZERO(), instance);
        assertEquals(0, instance.degree());
        double result = instance.evaluate(100.);
        assertEquals(0., result, 1e-15);
    }

    /**
     * Test of ZERO method, of class Polynomial.
     */
    @Test
    public void test_ZERO_0020() {
        Polynomial instance = new Polynomial(new double[]{0, 0, 0, 0, 0});//p(x) = 0
        assertEquals(instance.ZERO(), instance);
        assertEquals(0, instance.degree());
        double result = instance.evaluate(100.);
        assertEquals(0., result, 1e-15);
    }

    /**
     * Test of ONE method, of class Polynomial.
     */
    @Test
    public void test_ONE_0010() {
        Polynomial instance = new Polynomial(new double[]{1});//p(x) = 1
        assertEquals(instance.ONE(), instance);
        assertEquals(0, instance.degree());
        double result = instance.evaluate(100.);
        assertEquals(1., result, 1e-15);
    }

    /**
     * Test of opposite method, of class Polynomial.
     */
    @Test
    public void test_opposite_0010() {
        Polynomial instance = new Polynomial(new double[]{2, 1, -1, 1});//p(x) = 2*x^3 + x^2 - x + 1
        assertEquals(3, instance.degree());
        Polynomial expResult = new Polynomial(new double[]{-2, -1, 1, -1});//p(x) = -2*x^3 - x^2 + x - 1
        assertEquals(expResult, instance.opposite());
        assertEquals(3, instance.opposite().degree());
    }

    @Test
    public void test_add_0010() {
        Polynomial polynomial1 = new Polynomial(1, -2, 3, -4);
        Polynomial polynomial2 = polynomial1.opposite();
        Polynomial expResult = Polynomial.ZERO;
        Polynomial result = polynomial1.add(polynomial2);
        assertEquals(expResult, result);
    }

    @Test
    public void test_add_0020() {
        Polynomial polynomial1 = new Polynomial(1, -2, 3, -4);
        Polynomial polynomial2 = new Polynomial(-1, 2, -3, 5);
        Polynomial expResult = Polynomial.ONE;
        Polynomial result = polynomial1.add(polynomial2);
        assertEquals(expResult, result);
    }

    @Test
    public void test_minus_0010() {
        Polynomial polynomial1 = new Polynomial(1, -2, 3, -4);
        Polynomial expResult = Polynomial.ZERO;
        Polynomial result = polynomial1.minus(polynomial1);
        assertEquals(expResult, result);
    }

    @Test
    public void test_minus_0020() {
        Polynomial polynomial1 = new Polynomial(1, -2, 3, -4);
        Polynomial polynomial2 = new Polynomial(1, -2, 3, -5);
        Polynomial expResult = Polynomial.ONE;
        Polynomial result = polynomial1.minus(polynomial2);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Polynomial.
     */
    @Test
    public void test_toString_0010() {
        Polynomial instance = new Polynomial(new double[]{2, 1, -1, 1});//p(x) = 2*x^3 + x^2 - x + 1
        assertTrue(instance.toString().equals("2.00(x^3)+1.00(x^2)-1.00(x^1)+1.00"));
        Polynomial instance1 = new Polynomial(new double[]{1, 0.5, -0.5, 0.5});//p(x) = x^3 + 0.5x^2 - 0.5x + 0.5
        assertTrue(instance1.toString().equals("1.00(x^3)+0.50(x^2)-0.50(x^1)+0.50"));
        Polynomial instance2 = new Polynomial(new double[]{7, 0, 0, 0, -1, 0.5, -0.5, 0.5});//p(x) = 7x^7 - x^3 + 0.5x^2 - 0.5x + 0.5
        assertTrue(instance2.toString().equals("7.00(x^7)-1.00(x^3)+0.50(x^2)-0.50(x^1)+0.50"));
    }
}
