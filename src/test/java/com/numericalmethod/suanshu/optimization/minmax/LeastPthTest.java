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
package com.numericalmethod.suanshu.optimization.minmax;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.number.DoubleUtils.doubleArray2List;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LeastPthTest {

    /**
     * Example 8.1
     * Practical Optimization: Algorithms and Engineering Applications
     * by
     * Andreas Antoniou, Wu-Sheng Lu
     */
    @Test
    public void test_LeastPth_0010() throws Exception {
        final Matrix A = new DenseMatrix(new double[][]{
                    {3, -4, 2, -1},
                    {-2, 3, 6, -2},
                    {1, 2, 5, 1},
                    {-3, 1, -2, 2},
                    {7, -2, 4, 3},
                    {10, -1, 8, 5}
                });
        final DenseMatrix b = new DenseMatrix(new double[][]{
                    {-17.4},
                    {-1.2},
                    {7.35},
                    {9.41},
                    {4.1},
                    {12.3}
                });

        //the problem setup
        MinMaxProblem<Double> problem = new MinMaxProblem<Double>() {

            @Override
            public RealScalarFunction error(Double omega) {
                final int row = omega.intValue();

                RealScalarFunction f = new RealScalarFunction() {

                    public Double evaluate(Vector x) {
                        Matrix X = new DenseMatrix(x);
                        Matrix AX = A.multiply(X);//6x1
                        double diff = AX.get(row, 1);
                        diff -= b.get(row, 1);
                        return diff;
                    }

                    public int dimensionOfDomain() {
                        return 4;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                };

                return f;
            }

            @Override
            public RealVectorFunction gradient(Double omega) {
                final int row = omega.intValue();

                RealVectorFunction g = new RealVectorFunction() {

                    public Vector evaluate(Vector x) {
                        Vector gradient = A.getRow(row);

                        if (gradient.innerProduct(new DenseVector(x)) - b.get(row, 1) < 0) {
                            gradient = gradient.scaled(-1);
                        }

                        return gradient;
                    }

                    public int dimensionOfDomain() {
                        return 4;
                    }

                    public int dimensionOfRange() {
                        return 4;
                    }
                };

                return g;
            }

            @Override
            public List<Double> getOmega() {
                return doubleArray2List(new double[]{1, 2, 3, 4, 5, 6});
            }
        };

        LeastPth<Double> instance = new LeastPth<Double>(1e-6, 15);
        IterativeMinimizer<Vector> soln = instance.solve(problem);
        Vector xmin = soln.search(new DenseVector(new double[]{0.6902, 3.6824, -0.7793, 3.1150}));
//        System.out.println(xmin);
//        Vector diff = A.multiply(xmin).minus(new DenseVector(to1DArray(b)));
//        System.out.println(diff);
        Vector expected = new DenseVector(new double[]{0.7592, 3.6780, -0.8187, 3.0439});
        assertEquals(0.0, expected.minus(xmin).norm(), 1e-4);
    }

    /**
     * Test of using numerical gradient.
     *
     * Example 8.1
     * Practical Optimization: Algorithms and Engineering Applications
     * by
     * Andreas Antoniou, Wu-Sheng Lu
     */
    @Test
    public void test_LeastPth_0020() throws Exception {
        final Matrix A = new DenseMatrix(new double[][]{
                    {3, -4, 2, -1},
                    {-2, 3, 6, -2},
                    {1, 2, 5, 1},
                    {-3, 1, -2, 2},
                    {7, -2, 4, 3},
                    {10, -1, 8, 5}
                });
        final DenseMatrix b = new DenseMatrix(new double[][]{
                    {-17.4},
                    {-1.2},
                    {7.35},
                    {9.41},
                    {4.1},
                    {12.3}
                });

        //the problem setup
        MinMaxProblem<Double> problem = new MinMaxProblem<Double>() {

            @Override
            public RealScalarFunction error(Double omega) {
                final int row = omega.intValue();

                RealScalarFunction f = new RealScalarFunction() {

                    public Double evaluate(Vector x) {
                        Matrix X = new DenseMatrix(x);
                        Matrix AX = A.multiply(X);//6x1
                        double diff = AX.get(row, 1);
                        diff -= b.get(row, 1);
                        return diff;
                    }

                    public int dimensionOfDomain() {
                        return 4;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }
                };

                return f;
            }

            @Override
            public RealVectorFunction gradient(Double omega) {
                return null;//use numerical gradient
            }

            @Override
            public List<Double> getOmega() {
                return doubleArray2List(new double[]{1, 2, 3, 4, 5, 6});
            }
        };

        LeastPth<Double> instance = new LeastPth<Double>(1e-6, 15);
        IterativeMinimizer<Vector> soln = instance.solve(problem);
        Vector xmin = soln.search(new DenseVector(new double[]{0.6902, 3.6824, -0.7793, 3.1150}));
//        System.out.println(xmin);
//        Vector diff = A.multiply(xmin).minus(new DenseVector(to1DArray(b)));
//        System.out.println(diff);
        Vector expected = new DenseVector(new double[]{0.7592, 3.6780, -0.8187, 3.0439});
        assertEquals(0.0, expected.minus(xmin).norm(), 1e-4);
    }
}
