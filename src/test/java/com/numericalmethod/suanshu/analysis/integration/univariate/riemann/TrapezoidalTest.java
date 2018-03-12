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
public class TrapezoidalTest {

    /**
     * Test of class Trapezoidal.
     */
    @Test
    public void testTrapezoidal_0010() {
        Trapezoidal instance = new Trapezoidal(1e-15, 10);
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
     * Test of class Trapezoidal.
     */
    @Test
    public void testTrapezoidal_0020() {
        Trapezoidal instance = new Trapezoidal(1e-15, 20);
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
     * Test of class Trapezoidal.
     */
    @Test
    public void testTrapezoidal_0030() {
        Trapezoidal instance = new Trapezoidal(1e-15, 20);
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
     * Test of class Trapezoidal.
     */
    @Test
    public void testTrapezoidal_0040() {
        Trapezoidal instance = new Trapezoidal(1e-15, 10);
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
}
