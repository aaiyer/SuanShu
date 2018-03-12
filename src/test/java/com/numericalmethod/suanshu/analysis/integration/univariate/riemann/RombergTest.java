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
public class RombergTest {

    /**
     * Test of class Romberg.
     * extrapolation to give a much higher precision
     */
    @Test
    public void testRomberg_0010() {
        NewtonCotes integrator = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 10);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-15);
    }

    /**
     * Test of class Romberg.
     * OPEN with extrapolation
     */
    @Test
    public void testRomberg_0020() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 10);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-11);
    }

    /**
     * Test of class Romberg.
     * OPEN with extrapolation
     */
    @Test
    public void testRomberg_0030() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-9);//worse convergence?
    }

    /**
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0040() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 10);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                1, 3);
        assertEquals(8.666666666666667, result, 1e-11);
    }

    /**
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0050() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 15);
        Romberg instance = new Romberg(integrator);
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
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0060() {
        NewtonCotes integrator = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 25);//25 iterations!
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return sqrt(x);
                    }
                },
                0, 1);
        assertEquals(2 / 3d, result, 1e-10);
    }

    /**
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0070() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 15);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return sqrt(x);
                    }
                },
                0, 1);
        assertEquals(2 / 3d, result, 1e-11);
    }

    /**
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0080() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 10);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return sin(x) / x;
                    }
                },
                0, 0.8);
        assertEquals(0.7720957855, result, 1e-10);
    }

    /**
     * Test of class Romberg.
     */
    @Test
    public void testRomberg_0090() {
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 10);
        Romberg instance = new Romberg(integrator);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 1 / (1 + x * x);
                    }
                },
                -4, 4);
        assertEquals(2.651635327336065, result, 1e-13);
    }
}
