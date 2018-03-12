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
public class GoldenTest {

    @Test
    public void testGolden_0002() throws Exception {
        Golden instance = new Golden(1e-15, 1);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.3090169943749474, soln.search(0, 1), 1e-15);
    }

    @Test
    public void testGolden_0004() throws Exception {
        Golden instance = new Golden(1e-15, 2);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.42705098312484224, soln.search(0, 1), 1e-15);
    }

    @Test
    public void testGolden_0006() throws Exception {
        Golden instance = new Golden(1e-15, 3);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.5, soln.search(0, 1), 1e-15);
    }

    @Test
    public void testGolden_0008() throws Exception {
        Golden instance = new Golden(1e-15, 4);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.4549150281252629, soln.search(0, 1), 1e-15);
    }

    @Test
    public void testGolden_0010() throws Exception {
        Golden instance = new Golden(1e-15, 5);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.5, soln.search(0, 1), 1e-1);
    }

    @Test
    public void testGolden_0020() throws Exception {
        Golden instance = new Golden(1e-15, 30);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.5, soln.search(0, 1), 1e-15);
    }

    @Test
    public void testGolden_0030() throws Exception {
        Golden instance = new Golden(1e-15, 60);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.5, soln.search(0, 1), 1e-8);
    }

    /**
     * Increasing the number of iterations does not increase precision.
     */
    @Test
    public void testGolden_0060() throws Exception {
        Golden instance = new Golden(1e-15, 120);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 4 * x * x - 4 * x + 1;
                    }
                });

        assertEquals(0.5, soln.search(0, 1), 1e-8);
    }

    @Test
    public void testGolden_0040() throws Exception {
        Golden instance = new Golden(1e-15, 30);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x - 2 * x + 1;
                    }
                });

        assertEquals(1, soln.search(0, 2), 1e-15);
    }

    /**
     * Increasing the number of iterations won't help here,
     * because the 'next' guess gets out of the bracketing interval very quickly.
     */
    @Test
    public void testGolden_0050() throws Exception {
        Golden instance = new Golden(0, 35);

        UnivariateMinimizer.Solution soln = instance.solve(
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return sin(x);
                    }
                });

        assertEquals(1.5 * PI, soln.search(0, 1.1 * PI, 1.9 * PI), 1e-8);
    }
}
