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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset;

import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralGreaterThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SQPActiveSetSolverForOnlyInequalityConstraintTest {

    /**
     * example 15.2
     *
     * */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double fx = (x1 - x3) * (x1 - x3);
                fx += (x2 - x4) * (x2 - x4);
                fx /= 2;

                return fx;
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        GreaterThanConstraints greater = new GeneralGreaterThanConstraints(
                new RealScalarFunction() {

                    public Double evaluate(Vector x) {
                        double x1 = x.get(1);
                        double x2 = x.get(2);
                        double x3 = x.get(3);
                        double x4 = x.get(4);

                        Matrix v = new DenseMatrix(new double[]{x1, x2}, 2, 1);

                        Matrix A = new DenseMatrix(new double[][]{
                                    {0.25, 0},
                                    {0, 1}
                                });
                        Matrix B = new DenseMatrix(new double[]{0.5, 0}, 2, 1);

                        Matrix FX = v.t().multiply(A).multiply(v);
                        FX = FX.scaled(-1);
                        FX = FX.add(v.t().multiply(B));

                        double fx = FX.get(1, 1);
                        fx += 0.75;

                        return fx;
                    }

                    public int dimensionOfDomain() {
                        return 4;
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
                        double x4 = x.get(4);

                        Matrix v = new DenseMatrix(new double[]{x3, x4}, 2, 1);

                        Matrix A = new DenseMatrix(new double[][]{
                                    {5, 3},
                                    {3, 5}
                                });
                        Matrix B = new DenseMatrix(new double[]{11. / 2, 13. / 2}, 2, 1);

                        Matrix FX = v.t().multiply(A).multiply(v);
                        FX = FX.scaled(-1. / 8);
                        FX = FX.add(v.t().multiply(B));

                        double fx = FX.get(1, 1);
                        fx += -35. / 2;

                        return fx;
                    }

                    public int dimensionOfDomain() {
                        return 4;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                });

        SQPActiveSetSolverForOnlyInequalityConstraint instance = new SQPActiveSetSolverForOnlyInequalityConstraint(1e-7, 300);
        IterativeMinimizer<Vector> soln = instance.solve(f, greater);
        Vector x = soln.search(new DenseVector(1., 0.5, 2., 3.), new DenseVector(1., 1.));
        assertArrayEquals(new double[]{2.044750, 0.852716, 2.544913, 2.485633}, x.toArray(), 1e-6);
        double fx = f.evaluate(x);
        assertEquals(1.458290, fx, 1e-5);
    }
}
