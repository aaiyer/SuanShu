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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid;

import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SimpleGridMinimizerTest {

    @Test
    public void test_0010() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                false, uniform, 0, 500, 500);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{//[-10, 10], [-10, 10]
                    new DenseVector(-10.0, 10.0),
                    new DenseVector(10.0, -10.0),
                    new DenseVector(10.0, 10.0),
                    new DenseVector(-10.0, -10.0)
                });

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-7);
    }

    @Test
    public void test_0020() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                false, uniform, 0, 500, 500);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{//[-10, 3], [-2, 10]
                    new DenseVector(-1.0, 10.0),
                    new DenseVector(-10.0, -2.0),
                    new DenseVector(3.0, 5.0),
                    new DenseVector(-5.0, -2.0)
                });

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 2e-6);// build server seem to produce different random number sequences
    }

    @Test
    public void test_0030() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                false, uniform, 0, 500, 500);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{
                    new DenseVector(-10.0, 10.0),});

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 2e-5);//worse accuracy; build server seem to produce different random number sequences
    }

    @Test
    public void test_0050() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                false, uniform, 0, 500, 500);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{
                    new DenseVector(-1.0, 1.0),});//close to the minimum

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-5);//better accuracy
    }

    /**
     * for a unimodal problem, it does not matter where we start......
     */
    @Test
    public void test_0060() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                false, uniform, 0, 1000, 1000);//more iterations

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{
                    new DenseVector(-100.0, 100.0),});//very far away from the minimum

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-7);//still good
    }

    @Test
    public void test_speed_0010() throws Exception {
        test_speed(false);//no parallelization
    }

    @Test
    public void test_speed_0020() throws Exception {
        test_speed(true);//with parallelization
    }

    private void test_speed(boolean parallel) throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        SimpleGridMinimizer instance = new SimpleGridMinimizer(
                new SimpleGridMinimizer.NewCellFactoryCtor() {

                    @Override
                    public SimpleCellFactory newCellFactory() {
                        return new SimpleCellFactory(0.1, new UniformRng());
                    }
                },
                parallel, uniform, 0, 100, 100);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    // ignored
                }
                return x * x + y * y;
            }
        };

        long start = System.currentTimeMillis();
        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{//[-10, 10], [-10, 10]
                    new DenseVector(-10.0, 10.0),
                    new DenseVector(10.0, -10.0),
                    new DenseVector(10.0, 10.0),
                    new DenseVector(-10.0, -10.0)
                });
        System.out.println((parallel ? "parallel" : "single")
                           + " thread: time elapsed: " + (System.currentTimeMillis() - start));

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-2);
    }
}
