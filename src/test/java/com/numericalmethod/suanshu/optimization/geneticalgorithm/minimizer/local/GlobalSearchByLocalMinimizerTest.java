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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.local;

import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.optimization.unconstrained.MultivariateMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GlobalSearchByLocalMinimizerTest {

    @Test
    public void test_0010() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        GlobalSearchByLocalMinimizer instance = new GlobalSearchByLocalMinimizer(false, uniform, 1e-8, 20);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));//much fewer iterations
        Vector xmin = soln.search(new Vector[]{//[-10, 10], [-10, 10]
                    new DenseVector(-10.0, 10.0),
                    new DenseVector(10.0, -10.0),
                    new DenseVector(10.0, 10.0),
                    new DenseVector(-10.0, -10.0)
                });

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-76);//much higher accuracy
    }

    @Test
    public void test_0020() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        GlobalSearchByLocalMinimizer instance = new GlobalSearchByLocalMinimizer(false, uniform, 1e-8, 20);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return x * x + y * y;
            }
        };

        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));//much fewer iterations
        Vector xmin = soln.search(new Vector[]{//[-10, 3], [-2, 10]
                    new DenseVector(-1.0, 10.0),
                    new DenseVector(-10.0, -2.0),
                    new DenseVector(3.0, 5.0),
                    new DenseVector(-5.0, -2.0)
                });

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-76);
    }

    @Test
    public void test_0030() throws Exception {
        RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        GlobalSearchByLocalMinimizer instance = new GlobalSearchByLocalMinimizer(
                new LocalSearchCellFactory.MinimizerFactory() {

                    public MultivariateMinimizer newInstance() {
                        return new BFGS(false, 1e-8, 100);
                    }
                },
                false, uniform, 1e-8, 100, 10);

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

        assertArrayEquals(new double[]{0.0, 0.0}, xmin.toArray(), 1e-27);
    }
}
