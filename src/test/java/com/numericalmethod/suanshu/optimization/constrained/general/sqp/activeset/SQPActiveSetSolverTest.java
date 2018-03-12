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
 * FITNESS FOR Jacobian PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS Jacobian RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset;

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.NonNegativityConstraintOptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.*;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SQPActiveSetSolverTest {

    /**
     * example 15.4
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double fx = x1 * x1 + x2;
                return fx;
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        EqualityConstraints equal = new GeneralEqualityConstraints(
                new RealScalarFunction() {

                    public Double evaluate(Vector x) {
                        double x1 = x.get(1);
                        double x2 = x.get(2);

                        double fx = x1 * x1 + x2 * x2 - 9.;
                        return fx;
                    }

                    public int dimensionOfDomain() {
                        return 2;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                });

        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                }),
                new DenseVector(1., -5., 2., -4.));

        SQPActiveSetSolver instance = new SQPActiveSetSolver(1e-6, 20);
        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f, equal, greater.toLessThanConstraints());

        Vector x;
        double fx;

        IterativeMinimizer<Vector> soln = instance.solve(problem);
        x = soln.search(new DenseVector(5., 4.), new DenseVector(-1.), new DenseVector(1., 1., 1., 1.));
        fx = f.evaluate(x);
//        System.out.println("x = " + x);
//        System.out.println("fx = " + f.evaluate(x.toArray()));
        assertArrayEquals(new double[]{1., 2.8284}, x.toArray(), 1e-1);
        assertEquals(3.8284, fx, 1e-1);
    }

    @Test
    public void test_0020() throws Exception {
        BivariateRealFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x1, double x2) {
                double fx = (x1 - 5) * (x1 - 5) + (x2 - 3) * (x2 - 3);
                return fx;
            }
        };

        SQPActiveSetSolver optim = new SQPActiveSetSolver(1e-6, 20);
        NonNegativityConstraintOptimProblem problem = new NonNegativityConstraintOptimProblem(f);

        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector x = soln.search(new DenseVector(10., -10.));
        double fx = f.evaluate(x);
//        System.out.println(String.format("f(%s) = %f", x.toString(), fx));
        assertArrayEquals(new double[]{5., 3.}, x.toArray(), 1e-10);
        assertEquals(0, fx, 1e-10);
    }

    @Test
    public void test_0030() throws Exception {
        final Gamma gamma = new GammaLanczosQuick();

        BivariateRealFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double a, double b) {
                double gamma_ab = gamma.evaluate(a + b);
                double gamma_a = gamma.evaluate(a);
                double gamma_b = gamma.evaluate(b);
                double fx = log(gamma_ab) - log(gamma_a) - log(gamma_b) - 2 * a - b; //to be maximized
                fx = -fx; //minimize
                return fx;
            }
        };

        SQPActiveSetSolver optim = new SQPActiveSetSolver(1e-15, 200);
        NonNegativityConstraintOptimProblem problem = new NonNegativityConstraintOptimProblem(f);

        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector x = soln.search(new DenseVector(50., 50.0));
        double fx = f.evaluate(x);
//        System.out.println(String.format("f(%s) = %f", x.toString(), fx));
        assertEquals(2.594345, fx, 1e-5);//from R, using optim
    }
}
