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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.equalityconstraint;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralEqualityConstraints;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import static java.lang.Math.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SQPActiveSetSolverForOnlyEqualityConstraint1Test {

    /**
     * example 15.1
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);

                double fx = -pow(x1, 4.);
                fx -= 2. * pow(x2, 4.);
                fx -= pow(x3, 4.);
                fx -= pow(x1 * x2, 2.);
                fx -= pow(x1 * x3, 2.);

                return fx;
            }

            public int dimensionOfDomain() {
                return 3;
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
                        double x3 = x.get(3);

                        double fx = pow(x1, 4.);
                        fx += pow(x2, 4.);
                        fx += pow(x3, 4.);
                        fx -= 25.;

                        return fx;
                    }

                    public int dimensionOfDomain() {
                        return 3;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                },
                new RealScalarFunction() {

                    public Double evaluate(Vector x) {
                        double x1 = x.get(1);
                        double x2 = x.get(2);
                        double x3 = x.get(3);

                        double fx = 8. * pow(x1, 2.);
                        fx += 14. * pow(x2, 2.);
                        fx += 7. * pow(x3, 2.);
                        fx -= 56.;

                        return fx;
                    }

                    public int dimensionOfDomain() {
                        return 3;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                });

        SQPActiveSetSolverForOnlyEqualityConstraint1 instance;
        Vector x;
        double fx;

        instance = new SQPActiveSetSolverForOnlyEqualityConstraint1(
                new SQPActiveSetSolverForOnlyEqualityConstraint1.VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(100., 0.01, 10);
                        impl.set(f, equal);
                        return impl;
                    }
                }, 1e-7, 300);
        IterativeMinimizer<Vector> soln = instance.solve(f, equal);
        x = soln.search(new DenseVector(3., 1.5, 3.), new DenseVector(-1., -1.));
        fx = f.evaluate(x);
//        System.out.println("x = " + x);
//        System.out.println("fx = " + f.evaluate(x.toArray()));
        assertEquals(-38.384828, fx, 2e-1);
        assertArrayEquals(new double[]{1.874065, -0.465820, 1.884720}, x.toArray(), 1e-2);

        instance = new SQPActiveSetSolverForOnlyEqualityConstraint1(
                new SQPActiveSetSolverForOnlyEqualityConstraint1.VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(100., 0.001, 50);
                        impl.set(f, equal);
                        return impl;
                    }
                }, 1e-7, 600);
        soln = instance.solve(f, equal);
        x = soln.search(new DenseVector(0.003, 0.015, 0.3), new DenseVector(-1., -1.));
        fx = f.evaluate(x);
        assertEquals(-38.28479, fx, 1e-3);
        assertArrayEquals(new double[]{-1.874065, -0.465820, -1.884720}, x.toArray(), 1e-5);

        instance = new SQPActiveSetSolverForOnlyEqualityConstraint1(
                new SQPActiveSetSolverForOnlyEqualityConstraint1.VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(100., 0.01, 50);
                        impl.set(f, equal);
                        return impl;
                    }
                }, 1e-7, 70);
        soln = instance.solve(f, equal);
        x = soln.search(new DenseVector(-3.5, 2.2, 6.8), new DenseVector(2.2, -20.2));
        fx = f.evaluate(x);
        assertEquals(-38.28479, fx, 1e-3);
        assertArrayEquals(new double[]{1.874065, -0.465820, 1.884720}, x.toArray(), 1e-6);

        // need a better precision for alpha and a lot more iterations
        instance = new SQPActiveSetSolverForOnlyEqualityConstraint1(
                new SQPActiveSetSolverForOnlyEqualityConstraint1.VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(100., 0.001, 100);
                        impl.set(f, equal);
                        return impl;
                    }
                }, 1e-10, 2000);// poor convergence property
        soln = instance.solve(f, equal);
        x = soln.search(new DenseVector(100.6, 37.3, -23.95), new DenseVector(-100., -1.));
        fx = f.evaluate(x);
        assertEquals(-38.28479, fx, 1e-4);
        assertArrayEquals(new double[]{1.874065, 0.465820, 1.884720}, x.toArray(), 1e-6);

        // need a lot fewer iterations for the same initials
        instance = new SQPActiveSetSolverForOnlyEqualityConstraint1(
                new SQPActiveSetSolverForOnlyEqualityConstraint1.VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(100., 0.01, 10);
                        impl.set(f, equal);
                        return impl;
                    }
                }, 1e-10, 200);
        soln = instance.solve(f, equal);
        x = soln.search(new DenseVector(100.6, 37.3, -23.95), new DenseVector(-100., -1.));
        fx = f.evaluate(x);
        assertEquals(-38.28479, fx, 1e-4);
        assertArrayEquals(new double[]{1.874065, -0.465820, 1.884720}, x.toArray(), 1e-5);
    }
}
