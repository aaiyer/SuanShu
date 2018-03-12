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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.constrained;

import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.DEOptimCellFactory;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.Rand1Bin;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPProblemImpl1;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.DEOptim;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class IntegralConstrainedCellFactoryTest {

    @Test
    public void test_0010() throws Exception {
        final RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        DEOptim instance = new DEOptim(
                new DEOptim.NewCellFactory() {

                    @Override
                    public DEOptimCellFactory newCellFactory() {
                        return new IntegralConstrainedCellFactory(new Rand1Bin(0.5, 0.5, uniform), new IntegralConstrainedCellFactory.AllIntegers());
                    }
                },
                false, uniform, 0, 100, 20);

        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return (x - 1) * (x - 1) + (y - 2) * (y - 2);
            }
        };
        IterativeMinimizer<Vector> soln = instance.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new Vector[]{//[-10, 10], [-10, 10]
                    new DenseVector(-10.0, 10.0),
                    new DenseVector(10.0, -10.0),
                    new DenseVector(10.0, 10.0),
                    new DenseVector(-10.0, -10.0)
                });

        assertArrayEquals(new double[]{1.0, 2.0}, xmin.toArray(), 0);//precise due to integral constraints
    }

    @Test
    public void test_0020() throws Exception {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {
                return (x - 1) * (x - 1) + (y - 2) * (y - 2);
            }
        };

        final IPProblemImpl1 problem = new IPProblemImpl1(f, null, null, new int[]{1, 2}, 0);

        final RandomLongGenerator uniform = new UniformRng();
        uniform.seed(123456798L);

        DEOptim instance = new DEOptim(
                new DEOptim.NewCellFactory() {

                    @Override
                    public DEOptimCellFactory newCellFactory() {
                        return new IntegralConstrainedCellFactory(new Rand1Bin(0.5, 0.5, uniform), new IntegralConstrainedCellFactory.SomeIntegers(problem));
                    }
                },
                false, uniform, 0, 100, 20);

        IterativeMinimizer<Vector> soln = instance.solve(problem);
        Vector xmin = soln.search(new Vector[]{//[-10, 10], [-10, 10]
                    new DenseVector(-10.0, 10.0),
                    new DenseVector(10.0, -10.0),
                    new DenseVector(10.0, 10.0),
                    new DenseVector(-10.0, -10.0)
                });

        assertArrayEquals(new double[]{1.0, 2.0}, xmin.toArray(), 0);//precise due to integral constraints
    }
}
