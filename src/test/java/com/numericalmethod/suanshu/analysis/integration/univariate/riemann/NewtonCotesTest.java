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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class NewtonCotesTest {

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0010() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x;
                    }
                },
                0, 1);
        assertEquals(0.5, result, 1e-15);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0020() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 20);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.333333333333333333, result, 1e-13);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0030() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 20);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(x);
                    }
                },
                0, 1);
        assertEquals(E - 1, result, 1e-12);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0040() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-5);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0050() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-8);
    }

    /**
     * Test of class NewtonCotes.
     * rate = 3 should require fewer iterations (but not necessarily fewer abscissas
     */
    @Test
    public void testEulerMaclaurin_0060() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-7);
    }

    /**
     * Test of class NewtonCotes.
     * OPEN
     */
    @Test
    public void testEulerMaclaurin_0070() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-3);//very poor approximation
    }

    /**
     * Test of class NewtonCotes.
     * OPEN
     */
    @Test
    public void testEulerMaclaurin_0080() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-4);//very poor approximation
    }

    /**
     * Test of class NewtonCotes.
     * 1 iteration
     */
    @Test
    public void testEulerMaclaurin_0090() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 1);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.5, result, 1e-15);
    }

    /**
     * Test of class NewtonCotes.
     * 1 iteration
     */
    @Test
    public void testEulerMaclaurin_0100() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 1);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.25, result, 1e-15);
    }

    /**
     * Test of class NewtonCotes.
     * rate > 3
     */
    @Test
    public void testEulerMaclaurin_0110() {
        NewtonCotes instance = new NewtonCotes(4, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-10);
    }

    /**
     * Test of class NewtonCotes.
     * rate > 3
     */
    @Test
    public void testEulerMaclaurin_0120() {
        NewtonCotes instance = new NewtonCotes(5, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-10);
    }

    /**
     * Test of class NewtonCotes.
     * rate > 3
     */
    @Test
    public void testEulerMaclaurin_0130() {
        NewtonCotes instance = new NewtonCotes(6, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-9);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0135() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.OPEN, 1e-15, 20);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-5);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0140() {
        NewtonCotes instance = new NewtonCotes(4, NewtonCotes.Type.OPEN, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-5);
    }

    /**
     * Test of class NewtonCotes.
     * rate > 3
     */
    @Test
    public void testEulerMaclaurin_0150() {
        NewtonCotes instance = new NewtonCotes(5, NewtonCotes.Type.OPEN, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-5);
    }

    /**
     * Test of class NewtonCotes.
     * rate > 3
     */
    @Test
    public void testEulerMaclaurin_0155() {
        NewtonCotes instance = new NewtonCotes(6, NewtonCotes.Type.OPEN, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-6);//compared to 1e-9 using NewtonCotes.Type.CLOSED
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0160() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 10);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                1, 3);
        assertEquals(8.666666666666667, result, 1e-8);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0170() {
        NewtonCotes instance = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 20);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.333333333333334, result, 1e-13);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0175() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.333333333333334, result, 1e-9);
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0180() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.333333333333334, result, 1e-6);//much worse than CLOSED
    }

    /**
     * Test of class NewtonCotes.
     */
    @Test
    public void testEulerMaclaurin_0190() {
        NewtonCotes instance = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                1, 3);
        assertEquals(8.666666666666667, result, 1e-6);
    }

    /**
     * Test class ChangeOfVariable.
     *
     * @see "Example 5.2.5, Chapter 5, "Numerical Methods in Scientific Computing, Volume 1" by Germund Dalquist and Åke Björck. The 100-digit challenge, c.f., www.siam.org/books/100digitchallenge."
     */
//    @Test
    public void testEulerMaclaurin_0200() {
        NewtonCotes instance = new NewtonCotes(4, NewtonCotes.Type.OPEN, 1e-15, 15);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return cos(log(x) / x) / x;
                    }
                },
                0, 1);
        assertEquals(0.323367431677779, result, 1e-15);//not even close; can't compute for this one
    }
}
