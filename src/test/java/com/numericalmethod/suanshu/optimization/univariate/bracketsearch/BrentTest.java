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
package com.numericalmethod.suanshu.optimization.univariate.bracketsearch;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.optimization.univariate.UnivariateMinimizer;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class BrentTest {

    @Test
    public void test_0010() throws Exception {
        Brent instance = new Brent(1e-15, 4);

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 4 * x * x - 4 * x + 1;
            }
        };

        UnivariateMinimizer.Solution soln = instance.solve(f);
        double minimizer = soln.search(0, 1);

        assertEquals(0.5, minimizer, 1e-15);
        assertEquals(f.evaluate(minimizer), soln.minimum(), 1e-15);
    }

    @Test
    public void test_0020() throws Exception {
        Brent instance = new Brent(1e-15, 33);

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 4 * x * x - 4 * x + 1;
            }
        };

        UnivariateMinimizer.Solution soln = instance.solve(f);
        double minimizer = soln.search(0, 1);

        assertEquals(0.5, minimizer, 1e-15);
        assertEquals(f.evaluate(minimizer), soln.minimum(), 1e-15);
    }

    @Test
    public void test_0030() throws Exception {
        Brent instance = new Brent(1e-15, 4);

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x * x - 2 * x + 1;
            }
        };

        UnivariateMinimizer.Solution soln = instance.solve(f);
        double minimizer = soln.search(0, 2);

        assertEquals(1, minimizer, 1e-15);
        assertEquals(f.evaluate(minimizer), soln.minimum(), 1e-15);
    }

    /**
     * For the 45-th iteration, the next guess lies outside the bracketing interval.
     */
    @Test
    public void test_0040() throws Exception {
        Brent instance = new Brent(0, 4);

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return sin(x);
            }
        };

        UnivariateMinimizer.Solution soln = instance.solve(f);
        double minimizer = soln.search(0, 1.1 * PI, 1.9 * PI);

        assertEquals(1.5 * PI, minimizer, 1e-8);
        assertEquals(f.evaluate(minimizer), soln.minimum(), 1e-8);
    }

    /**
     * A werid function (from R).
     * TODO: Hard to get this right... fall on a local minimum?
     */
//    @Test
    public void test_0050() throws Exception {
        Brent instance = new Brent(0, 100);

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 10 * sin(0.3 * x) * sin(1.3 * x * x) + 0.00001 * x * x * x * x + 0.2 * x + 80;
            }
        };

        UnivariateMinimizer.Solution soln = instance.solve(f);
        double minimizer = soln.search(-100, 100);

        assertEquals(-15.81515, minimizer, 1e-8);
        assertEquals(f.evaluate(minimizer), soln.minimum(), 1e-8);
    }
}
