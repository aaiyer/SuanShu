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
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.ChangeOfVariable;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.NewtonCotes;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class DoubleExponential4RealLineTest {

    @Test
    public void test_DoubleExponential4RealLine_0010() {
        double a = Double.NEGATIVE_INFINITY, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return exp(-x * x);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 6);//only 6 iterations!
        ChangeOfVariable instance = new ChangeOfVariable(new DoubleExponential4RealLine(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(sqrt(PI), result, 1e-15);//machine precision
    }

    /**
     * Student's t distribution.
     */
    @Test
    public void test_DoubleExponential4RealLine_0020() {
        Gamma gamma = new GammaLanczosQuick();

        final double v = 1;
        double studentT = sqrt(v * PI) * gamma.evaluate(v / 2) / gamma.evaluate((v + 1) / 2);

        double a = Double.NEGATIVE_INFINITY, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return pow(1 + x * x / v, -(v + 1) / 2);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3,
                                                 NewtonCotes.Type.CLOSED, 1e-15, 5);//only 5 iterations!
        ChangeOfVariable instance =
                new ChangeOfVariable(new DoubleExponential4RealLine(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(studentT, result, 1e-15);//machine precision
    }

    /**
     * Student's t distribution.
     */
    @Test
    public void test_DoubleExponential4RealLine_0030() {
        Gamma gamma = new GammaLanczosQuick();

        final double v = 10.5;
        double studentT = sqrt(v * PI) * gamma.evaluate(v / 2) / gamma.evaluate((v + 1) / 2);

        double a = Double.NEGATIVE_INFINITY, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return pow(1 + x * x / v, -(v + 1) / 2);
            }
        };

        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 7);//only 7 iterations!
        ChangeOfVariable instance = new ChangeOfVariable(new DoubleExponential4RealLine(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(studentT, result, 1e-15);//machine precision
    }
}
