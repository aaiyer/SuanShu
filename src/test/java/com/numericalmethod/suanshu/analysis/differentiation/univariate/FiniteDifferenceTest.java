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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class FiniteDifferenceTest {

    @Test
    public void testFiniteDifference_0010() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.log(x);
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);
        assertEquals(2, instance.evaluate(0.5), 1e-15);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);
        assertEquals(-4, instance.evaluate(0.5), 1e-5);

        instance = new FiniteDifference(f, 3, FiniteDifference.Type.CENTRAL);
        assertEquals(16, instance.evaluate(0.5), 1e-3);

        instance = new FiniteDifference(f, 4, FiniteDifference.Type.CENTRAL);
        assertEquals(-96, instance.evaluate(0.5), 1e-1);
    }

    @Test
    public void testFiniteDifference_0020() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.log(x);
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.FORWARD);
        assertEquals(2, instance.evaluate(0.5), 1e-7);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.FORWARD);
        assertEquals(-4, instance.evaluate(0.5), 1e-4);

        instance = new FiniteDifference(f, 3, FiniteDifference.Type.CENTRAL);
        assertEquals(16, instance.evaluate(0.5), 1e-3);

        instance = new FiniteDifference(f, 4, FiniteDifference.Type.FORWARD);
        assertEquals(-96, instance.evaluate(0.5), 1e0);
    }

    @Test
    public void testFiniteDifference_0030() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.log(x);
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.BACKWARD);
        assertEquals(2, instance.evaluate(0.5), 1e-7);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.BACKWARD);
        assertEquals(-4, instance.evaluate(0.5), 1e-4);

        instance = new FiniteDifference(f, 3, FiniteDifference.Type.BACKWARD);
        assertEquals(16, instance.evaluate(0.5), 1e-2);

        instance = new FiniteDifference(f, 4, FiniteDifference.Type.BACKWARD);
        assertEquals(-96, instance.evaluate(0.5), 1e-0);
    }

    @Test
    public void testFiniteDifference_0040() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.sin(x);
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);
        assertEquals(0.877582561890373, instance.evaluate(0.5), 1e-9);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);
        assertEquals(-0.479425538604203, instance.evaluate(0.5), 1e-5);

        instance = new FiniteDifference(f, 3, FiniteDifference.Type.CENTRAL);
        assertEquals(-0.877582561890373, instance.evaluate(0.5), 1e-2);

        instance = new FiniteDifference(f, 4, FiniteDifference.Type.CENTRAL);
        assertEquals(0.479425538604203, instance.evaluate(0.5), 1e-3);
    }

    @Test
    public void testFiniteDifference_0050() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return (new Polynomial(new double[]{1, 2, 3, 4})).evaluate(x);//p(x) = x^3 + 2x^2 + 3x^1 + 4
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//p(x) = 3x^2 + 4x + 3
        assertEquals(10, instance.evaluate(1), 1e-15);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);//p(x) = 6x + 4
        assertEquals(10, instance.evaluate(1), 1e-4);

        instance = new FiniteDifference(f, 3, FiniteDifference.Type.CENTRAL);//p(x) = 6
        assertEquals(6, instance.evaluate(1), 1e-15);

        instance = new FiniteDifference(f, 4, FiniteDifference.Type.CENTRAL);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-1);

        instance = new FiniteDifference(f, 5, FiniteDifference.Type.FORWARD);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e0);

        instance = new FiniteDifference(f, 6, FiniteDifference.Type.FORWARD);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e0);

        instance = new FiniteDifference(f, 6, FiniteDifference.Type.BACKWARD);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e1);//very poor approximation

        instance = new FiniteDifference(f, 7, FiniteDifference.Type.BACKWARD);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e0);
    }

    @Test
    public void testFiniteDifference_0060() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x * x;//x^2
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(200, instance.evaluate(100), 1e-15);

        instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(-200, instance.evaluate(-100), 1e-15);

        instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(-20000, instance.evaluate(-10000), 1e-15);

        instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(-20000000, instance.evaluate(-10000000), 1e-1);
    }

    /**
     * Test for near x = 0 values.
     */
    @Test
    public void testFiniteDifference_0070() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x * x;//x^2
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(0, instance.evaluate(0), 1e-15);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);//2
        assertEquals(2, instance.evaluate(0), 1e-15);
    }

    /**
     * Test for near x = 0 values.
     */
    @Test
    public void testFiniteDifference_0080() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x * x;//x^2
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);//2x
        assertEquals(0.0000002, instance.evaluate(0.0000001), 1e-9);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);//2
        assertEquals(2, instance.evaluate(0.0000001), 1e-9);
    }

    /**
     * Test for near x = 0 values.
     */
    @Test
    public void testFiniteDifference_0090() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return cos(x);
            }
        };

        FiniteDifference instance = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);
        assertEquals(0, instance.evaluate(0), 1e-9);

        instance = new FiniteDifference(f, 2, FiniteDifference.Type.CENTRAL);
        assertEquals(-1, instance.evaluate(0), 1e-4);
    }
}
