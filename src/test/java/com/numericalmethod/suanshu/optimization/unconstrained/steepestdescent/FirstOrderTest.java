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
package com.numericalmethod.suanshu.optimization.unconstrained.steepestdescent;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class FirstOrderTest {

    /**
     * Use the "Analytical" method.
     *
     * The global minimizer is at x = [0,0,0,0].
     *
     * Sample Solution SA.8.
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double result = pow(x1 - 4 * x2, 4);
                result += 12 * pow(x3 - x4, 4);
                result += 3 * pow(x2 - 10 * x3, 2);
                result += 55 * pow(x1 - 2 * x4, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double[] result = new double[4];
                result[0] = 4 * pow(x1 - 4 * x2, 3) + 110 * (x1 - 2 * x4);
                result[1] = -16 * pow(x1 - 4 * x2, 3) + 6 * (x2 - 10 * x3);
                result[2] = 48 * pow(x3 - x4, 3) - 60 * (x2 - 10 * x3);
                result[3] = -48 * pow(x3 - x4, 3) - 220 * (x1 - 2 * x4);
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        FirstOrder optim = new FirstOrder(FirstOrder.Method.ANALYTIC, 1e-6, 40000);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));

        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});//from sample solution
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-4);
        double fxmin = f.evaluate(xmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-6);
//        System.out.println(xmin);
//        System.out.println(fxmin);

        soln = optim.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));
//        assertEquals(0.0, xmin.minus(ans).norm(), 1e-4);//f has a too big intermediate values to compute; matlab has the same problem
        fxmin = f.evaluate(xmin);
//        assertEquals(0.0, abs(fans - fxmin), 1e-6);
//        System.out.println(xmin);
//        System.out.println(fxmin);
    }

    /**
     * The global minimizer is at x = [0,0,0,0].
     */
    @Test
    public void test_0020() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double result = pow(x1 - 4 * x2, 4);
                result += 12 * pow(x3 - x4, 4);
                result += 3 * pow(x2 - 10 * x3, 2);
                result += 55 * pow(x1 - 2 * x4, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double[] result = new double[4];
                result[0] = 4 * pow(x1 - 4 * x2, 3) + 110 * (x1 - 2 * x4);
                result[1] = -16 * pow(x1 - 4 * x2, 3) + 6 * (x2 - 10 * x3);
                result[2] = 48 * pow(x3 - x4, 3) - 60 * (x2 - 10 * x3);
                result[3] = -48 * pow(x3 - x4, 3) - 220 * (x1 - 2 * x4);
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        FirstOrder optim = new FirstOrder(FirstOrder.Method.IN_EXACT_LINE_SEARCH, 1e-6, 40000);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});//from sample solution
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-8);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-12);

        optim.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));
//        System.out.println(xmin);

        ans = new DenseVector(new double[]{0.04813224188615, 0.01694710245881, 0.00169595248698, 0.02406511597832});//from matlab

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-13);//unlike test 0010, no problem of handling f
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-7);
    }

    /**
     * Use the numerical gradient.
     *
     * The global minimizer is at x = [0,0,0,0].
     */
    @Test
    public void test_0030() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double result = pow(x1 - 4 * x2, 4);
                result += 12 * pow(x3 - x4, 4);
                result += 3 * pow(x2 - 10 * x3, 2);
                result += 55 * pow(x1 - 2 * x4, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FirstOrder optim = new FirstOrder(1e-6, 40000);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});//from sample solution
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-5);//less precision
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-9);//less precision

        soln = optim.solve(new C2OptimProblemImpl(f));
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));
//        System.out.println(xmin);

        ans = new DenseVector(new double[]{0.04813224188615, 0.01694710245881, 0.00169595248698, 0.02406511597832});//from matlab

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-6);//unlike test 0010, no problem of handling f; less precision
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-7);
    }
}
