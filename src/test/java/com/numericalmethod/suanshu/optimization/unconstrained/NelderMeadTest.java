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
package com.numericalmethod.suanshu.optimization.unconstrained;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class NelderMeadTest {

    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x - 4 * x + y * y - y - x * y;
            }
        };

        NelderMead instance = new NelderMead(1e-8, 1);
        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{1.2, 0}),
                    new DenseVector(new double[]{0, 0.8})
                });

        assertArrayEquals(new double[]{1.8, 1.2}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 2);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{1.2, 0}),
                    new DenseVector(new double[]{0, 0.8})
                });

        assertArrayEquals(new double[]{1.8, 1.2}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 3);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{1.2, 0}),
                    new DenseVector(new double[]{0, 0.8})
                });

        assertArrayEquals(new double[]{3.6, 1.6}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 4);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{1.2, 0}),
                    new DenseVector(new double[]{0, 0.8})
                });

        assertArrayEquals(new double[]{3.6, 1.6}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 60);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{1.2, 0}),
                    new DenseVector(new double[]{0, 0.8})
                });

        double fxmin = f.evaluate(xmin);
        assertEquals(-7, fxmin, 1e-12);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-12);
    }

    @Test
    public void test_0020() {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x - 4 * x + y * y - y - x * y;
            }
        };

        NelderMead instance = new NelderMead(1e-8, 1);
        NelderMead.Solution soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(
                new Vector[]{
                    new DenseVector(0., 0.),
                    new DenseVector(1.2, 0.),
                    new DenseVector(0., 0.8)
                });

        assertArrayEquals(new double[]{1.8, 1.2}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 2);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new Vector[]{
                    new DenseVector(0., 0.),
                    new DenseVector(1.2, 0.),
                    new DenseVector(0., 0.8)
                });

        assertArrayEquals(new double[]{1.8, 1.2}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 3);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new Vector[]{
                    new DenseVector(0., 0.),
                    new DenseVector(1.2, 0.),
                    new DenseVector(0., 0.8)
                });

        assertArrayEquals(new double[]{3.6, 1.6}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 4);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new Vector[]{
                    new DenseVector(0., 0.),
                    new DenseVector(1.2, 0.),
                    new DenseVector(0., 0.8)
                });

        assertArrayEquals(new double[]{3.6, 1.6}, xmin.toArray(), 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);

        instance = new NelderMead(1e-8, 60);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new Vector[]{
                    new DenseVector(0., 0.),
                    new DenseVector(1.2, 0.),
                    new DenseVector(0., 0.8)
                });

        double fxmin = f.evaluate(xmin);
        assertEquals(-7, fxmin, 1e-12);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-12);
    }

    /**
     * Rosenbrock Banana function (from R).
     */
    @Test
    public void test_0030() throws Exception {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return 100 * (y - x * x) * (y - x * x) + (1 - x) * (1 - x);
            }
        };

        NelderMead instance = new NelderMead(0, 88);
        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{0, 0}),
                    new DenseVector(new double[]{-1.2, 1}),
                    new DenseVector(new double[]{2, 2})
                });

        double fxmin = f.evaluate(xmin);
        assertEquals(0, fxmin, 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);
    }

    /**
     * A weird function (from R).
     */
    @Test
    public void test_0040() throws Exception {
        RealScalarFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return 10 * sin(0.3 * x) * sin(1.3 * x * x) + 0.00001 * x * x * x * x + 0.2 * x + 80;
            }
        };

        NelderMead instance = new NelderMead(1e-8, 15);
        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{-100}),
                    new DenseVector(new double[]{100})
                });

        double fxmin = f.evaluate(xmin);
        assertEquals(67.4703656098372, fxmin, 3);//Nelder-Mead does not work well with univariate functon

        instance = new NelderMead(1e-8, 20000);
        soln = instance.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(
                new DenseVector[]{
                    new DenseVector(new double[]{-100}),
                    new DenseVector(new double[]{100})
                });

        fxmin = f.evaluate(xmin);
        assertEquals(67.4703656098372, fxmin, 3);//Nelder-Mead does not work well with univariate functon
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);
    }

    /**
     * Rosenbrock Banana function (from R).
     */
    @Test
    public void test_0050() {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return 100 * (y - x * x) * (y - x * x) + (1 - x) * (1 - x);
            }
        };

        NelderMead instance = new NelderMead(0, 500);
        NelderMead.Solution soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new DenseVector(0., 0.));
        double fxmin = f.evaluate(xmin);
        assertEquals(0, fxmin, 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);
    }

    @Test
    public void test_0060() {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return sin(x) * cos(y);
            }
        };

        NelderMead instance = new NelderMead(0, 500);
        NelderMead.Solution soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new DenseVector(0.5, 0.5));
        double fxmin = f.evaluate(xmin);
        assertEquals(-1, fxmin, 1e-15);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);
    }

    /**
     * It should be OK to pass in ImmutableVector as an initial.
     */
    @Test
    public void test_0070() {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        NelderMead instance = new NelderMead(1e-8, 200);
        NelderMead.Solution soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new ImmutableVector(new DenseVector(4.865377630657838, 0.15538121099941904)));
        double fxmin = f.evaluate(xmin);
        assertEquals(0, fxmin, 1e-12);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-15);
    }
}
