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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.ChangeOfVariable;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.NewtonCotes;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class DoubleExponential4HalfRealLineTest {

    /**
     * Test class ChangeOfVariable.
     * compared to using the Exponential substitution
     */
    @Test
    public void testChangeOfVariable_0010() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x / (exp(x) - 1);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new DoubleExponential4HalfRealLine(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(PI * PI / 6, result, 1e-5);
    }

    /**
     * Test class ChangeOfVariable.
     * compared to testChangeOfVariable_0010
     */
    @Test
    public void testChangeOfVariable_0020() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x / (exp(x) - 1);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new DoubleExponential4HalfRealLine(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);

        assertEquals(PI * PI / 6, result, 1e-11);//much better; Exponential substitution cannot use CLOSED formula
    }

    /**
     * Test class ChangeOfVariable.
     */
    @Test
    public void testChangeOfVariable_0030() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 1 / sqrt(x) / (1 + x);
            }
        };

        NewtonCotes integrator = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 6);//only 6 iteration! about 64 function evaluations
        ChangeOfVariable instance = new ChangeOfVariable(
                new DoubleExponential4HalfRealLine(f, a, b, PI / 2) {

                    @Override
                    public double ta() {
                        return -4;
                    }

                    @Override
                    public double tb() {
                        return 4;
                    }
                },
                integrator);

        double result = instance.integrate(f, a, b);
        assertEquals(PI, result, 1e-15);//machine precision
    }

    /**
     * Test class ChangeOfVariable.
     */
    @Test
    public void testChangeOfVariable_0040() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 1 / sqrt(x) / (1 + x);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new DoubleExponential4HalfRealLine(f, a, b, PI / 2), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(PI, result, 1e-5);//much worse than using the CLOSED formula
    }

    /**
     * Test class ChangeOfVariable.
     * never converge using Exponential substitution
     */
//    @Test
    public void testChangeOfVariable_0050() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new Exponential(a), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 1 / sqrt(x) / (1 + x);
                    }
                },
                a, b);

        assertEquals(PI, result, 1e-15);//very off
    }
}
