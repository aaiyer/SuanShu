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
public class PowerLawSingularityTest {

    @Test
    public void testChangeOfVariable_0010() {
        double a = 1, b = 2;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new PowerLawSingularity(PowerLawSingularity.PowerLawSingularityType.LOWER, 0.5, a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 1 / sqrt(x - 1);
                    }
                },
                a, b);

        assertEquals(2, result, 1e-15);
    }

    @Test
    public void testChangeOfVariable_0020() {
        double a = 1, b = 2;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new PowerLawSingularity(PowerLawSingularity.PowerLawSingularityType.LOWER, 0.5, a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x / sqrt(x * x - 1);
                    }
                },
                a, b);

        assertEquals(sqrt(3), result, 1e-7);
    }

    @Test
    public void testChangeOfVariable_0030() {
        double a = 1, b = 2;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new PowerLawSingularity(PowerLawSingularity.PowerLawSingularityType.UPPER, 0.5, a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 1 / sqrt(2 - x);
                    }
                },
                a, b);

        assertEquals(2, result, 1e-15);
    }

    @Test
    public void testChangeOfVariable_0040() {
        double a = 1, b = 2;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);
        ChangeOfVariable instance = new ChangeOfVariable(new PowerLawSingularity(PowerLawSingularity.PowerLawSingularityType.UPPER, 0.5, a, b), integrator);

        double result = instance.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x / sqrt(4 - x * x);
                    }
                },
                a, b);

        assertEquals(sqrt(3), result, 1e-7);
    }

    /**
     * compared to testChangeOfVariable_0110 w/o substitution
     */
    @Test
    public void testChangeOfVariable_0050() {
        double a = 1, b = 2;
        NewtonCotes integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, 1e-15, 15);

        double result = integrator.integrate(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x / sqrt(4 - x * x);
                    }
                },
                a, b);

        assertEquals(sqrt(3), result, 1e-2);//much poorer result
    }
}
