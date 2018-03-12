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

/**
 *
 * @author Haksun Li
 */
public class NoChangeOfVariableTest {

    /**
     * Test class ChangeOfVariable.
     */
    @Test
    public void testChangeOfVariable_0010() {
        double a = 0, b = 1;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 10);
        ChangeOfVariable instance = new ChangeOfVariable(new NoChangeOfVariable(a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x;
                    }
                },
                a, b);

        assertEquals(0.5, result, 1e-15);
    }

    /**
     * Test class ChangeOfVariable.
     */
    @Test
    public void testChangeOfVariable_0020() {
        double a = 0, b = 1;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.CLOSED, 1e-15, 10);
        ChangeOfVariable instance = new ChangeOfVariable(new NoChangeOfVariable(a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x;
                    }
                },
                a, b);

        assertEquals(0.5, result, 1e-15);
    }
}
