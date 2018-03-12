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
package com.numericalmethod.suanshu.analysis.uniroot;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BrentTest {

    /**
     * Solve for log(x) = 0.
     */
    @Test
    public void test_Brent_0010() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.log(x);
            }
        };

        Brent instance = new Brent(1e-15, 10);
        double root = instance.solve(f, 0, 2);
        assertEquals(1d, root, 1e-15);
    }

    /**
     * Solve for x^3 - 6x^2 + 11x - 6 = 0.
     * The roots are 1, 2, 3.
     */
    @Test
    public void test_Brent_0020() {
        UnivariateRealFunction f = new Polynomial(new double[]{1, -6, 11, -6});//p(x) = x^3 - 6x^2 + 11x - 6

        Brent instance = new Brent(1e-15, 10);
        double root = instance.solve(f, 0.5, 1.5);
        assertEquals(1d, root, 1e-15);

        root = instance.solve(f, 1.5, 2.5);
        assertEquals(2d, root, 1e-15);

        root = instance.solve(f, 2.5, 3.5);
        assertEquals(3d, root, 1e-14);
    }

    /**
     * Solve for (x+3)*(x-1)^2 = 0.
     */
    @Test
    public void test_Brent_0030() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return (x + 3) * (x - 1) * (x - 1);
            }
        };

        Brent instance = new Brent(1e-15, 10);
        double root = instance.solve(f, -4, 3.0 / 4);
        assertEquals(-3, root, 1e-15);
    }

    /**
     * Solve for (x+3)*(x-1)^2 = 0.
     *
     * The root approximations do not match the example in wiki,
     * because the Brent's additional conditions are not exactly the same in our implementation.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Brent's_method#Example"> Wikipedia: Example</a>
     */
//    @Test
    public void test_Brent_0040() {
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return (x + 3) * (x - 1) * (x - 1);
            }
        };

        Brent instance = new Brent(1e-15, 10);
        double root = instance.solve(f, -4, 4.0 / 3);
        assertEquals(-3, root, 1e-15);
    }

    /**
     * Solve for x^3 - 6x^2 + 11x - 6 = 0.
     * The roots are 1, 2, 3.
     *
     * No bracketing.
     */
    @Test(expected = RuntimeException.class)
    public void test_Brent_0050() {
        UnivariateRealFunction f = new Polynomial(new double[]{1, -6, 11, -6});//p(x) = x^3 - 6x^2 + 11x - 6

        Brent instance = new Brent(1e-15, 10);
        instance.solve(f, 0.5, 0.9);
    }
}
