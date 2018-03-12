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
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution.DoubleExponential4HalfRealLine;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution.InvertingVariable;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution.SubstitutionRule;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class RiemannTest {

    @Test
    public void test_0010() {
        Riemann instance = new Riemann();
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

    @Test
    public void test_0020() {
        Riemann instance = new Riemann();
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                0, 1);
        assertEquals(0.333333333333333333, result, 1e-15);
    }

    @Test
    public void test_0030() {
        Riemann instance = new Riemann(1e-15, 20);
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(x);
                    }
                },
                0, 1);
        assertEquals(E - 1, result, 1e-14);//actually worse precision than the Simpson's rule; why?
    }

    @Test
    public void test_0040() {
        Riemann instance = new Riemann();
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-12);
    }

    @Test
    public void test_0050() {
        Riemann instance = new Riemann();
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return exp(2 * x);
                    }
                },
                0, 1);
        assertEquals(0.5 * E * E - 0.5, result, 1e-13);
    }

    @Test
    public void test_0060() {
        Riemann instance = new Riemann();
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                },
                1, 3);
        assertEquals(8.666666666666667, result, 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_0070() {
        Riemann instance = new Riemann();
        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x / (exp(x) - 1);
                    }
                },
                0, Double.POSITIVE_INFINITY);
        assertEquals(PI * PI / 6, result, 1e-5);
    }

    @Test
    public void test_0080() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x / (exp(x) - 1);
            }
        };

        SubstitutionRule substitute = new DoubleExponential4HalfRealLine(f, a, b, 1);
        Riemann instance = new Riemann();
        double result = instance.integrate(
                f, a, b, substitute);
        assertEquals(PI * PI / 6, result, 1e-11);
    }

    @Test
    public void test_0090() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 1 / sqrt(x) / (1 + x);
            }
        };

        SubstitutionRule substitute = new DoubleExponential4HalfRealLine(f, a, b, PI / 2) {

            @Override
            public double ta() {
                return -4;
            }

            @Override
            public double tb() {
                return 4;
            }
        };
        Riemann instance = new Riemann(1e-15, 15);
        double result = instance.integrate(
                f, a, b, substitute);
        assertEquals(PI, result, 1e-13);//slower & worse precision (due to extrapolation?)
    }

    /**
     * OPEN formula
     */
    @Test
    public void test_0100() {
        double a = 1, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 1 / x / x;
            }
        };

        SubstitutionRule substitute = new InvertingVariable(a, b);
        Riemann instance = new Riemann();
        double result = instance.integrate(
                f, a, b, substitute);
        assertEquals(1, result, 1e-15);
    }
}
