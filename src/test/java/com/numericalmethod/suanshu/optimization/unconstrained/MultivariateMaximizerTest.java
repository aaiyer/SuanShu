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

import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MultivariateMaximizerTest {

    @Test
    public void test_0010() throws Exception {
        BivariateRealFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x1, double x2) {
                double x = x1;
                double y = x2;

                return -1. * ((x - 1) * (x - 1) + (y - 1) * (y - 1));//optimal at (1, 1)
            }
        };

        MultivariateMaximizer maximizer = new MultivariateMaximizer(1e-15, 100);
        MultivariateMaximizer.Solution soln = maximizer.solve(new C2OptimProblemImpl(f));
        Vector xmax = soln.search(new DenseVector(0.5, 0.5));

        assertArrayEquals(new double[]{1, 1}, xmax.toArray(), 1e-7);
    }

    @Test
    public void test_0020() throws Exception {
        BivariateRealFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x1, double x2) {
                double x = x1;
                double y = x2;

                return -1. * ((x - 1) * (x - 1) + (y - 1) * (y - 1));//optimal at (1, 1)
            }
        };

        MultivariateMaximizer maximizer = new MultivariateMaximizer(new BFGS(false, 1e-8, 100));
        MultivariateMaximizer.Solution soln = maximizer.solve(new C2OptimProblemImpl(f));
        Vector xmax = soln.search(new DenseVector(0.5, 0.5));

        assertArrayEquals(new double[]{1, 1}, xmax.toArray(), 1e-8);
    }
}
