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
public class MixedRuleTest {

    @Test
    public void testChangeOfVariable_0010() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return pow(x, -1.5) * sin(x / 2) * exp(-x);
            }
        };

        NewtonCotes integrator = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 7);//only 7 iteration
        ChangeOfVariable instance = new ChangeOfVariable(new MixedRule(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(sqrt(PI * (sqrt(5) - 2)), result, 1e-15);//machine precision
    }

    @Test
    public void testChangeOfVariable_0020() {
        double a = 0, b = Double.POSITIVE_INFINITY;
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return pow(x, -2d / 7) * exp(-x * x);
            }
        };

        NewtonCotes integrator = new NewtonCotes(2, NewtonCotes.Type.CLOSED, 1e-15, 8);//only 8 iteration
        ChangeOfVariable instance = new ChangeOfVariable(new MixedRule(f, a, b, 1), integrator);
        double result = instance.integrate(f, a, b);
        assertEquals(1.246631334954062, result, 1e-15);//machine precision
    }
}
