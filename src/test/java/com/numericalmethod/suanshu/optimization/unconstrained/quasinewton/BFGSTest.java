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
package com.numericalmethod.suanshu.optimization.unconstrained.quasinewton;

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
public class BFGSTest {

    /**
     * The Himmelblau function:
     * f(x) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 - 7)^2
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double result = pow(x1 * x1 + x2 - 11, 2);
                result += pow(x1 + x2 * x2 - 7, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double w1 = x1 * x1 + x2 - 11;
                double w2 = x1 + x2 * x2 - 7;

                double[] result = new double[2];
                result[0] = 4 * w1 * x1 + 2 * w2;
                result[1] = 2 * w1 + 4 * w2 * x2;
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        BFGS optim = new BFGS(false, 1e-6, 13);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{6, 6}));
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{3.00000000003421, 2.00000000051810});//from Matlab
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-10);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-15);
    }

    /**
     * Use numerical gradient.
     *
     * The Himmelblau function:
     * f(x) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 - 7)^2
     */
    @Test
    public void test_0020() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double result = pow(x1 * x1 + x2 - 11, 2);
                result += pow(x1 + x2 * x2 - 7, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BFGS optim = new BFGS(false, 1e-6, 13);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new DenseVector(new double[]{6, 6}));
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{3.00000000003421, 2.00000000051810});//from Matlab
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-10);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-15);
    }

    /**
     * The Himmelblau function:
     * f(x) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 - 7)^2
     *
     * BFGS with possible switching to DFP seems to produce less accurate results.
     *
     * TODO:
     * Fletcher-switching does not seem to work for this case.
     * It will work if we modify the switching criteria to:
     * ltdvalue - ltSk1lvalue > 1e-20
     *
     * Or, change the parameter 'threshold' to 1e-7
     *
     * Is this a numerical problem?
     */
    @Test
    public void test_BFGS_0030() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double result = pow(x1 * x1 + x2 - 11, 2);
                result += pow(x1 + x2 * x2 - 7, 2);

                return result;
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double w1 = x1 * x1 + x2 - 11;
                double w2 = x1 + x2 * x2 - 7;

                double[] result = new double[2];
                result[0] = 4 * w1 * x1 + 2 * w2;
                result[1] = 2 * w1 + 4 * w2 * x2;
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        BFGS optim = new BFGS(true, 1e-7, 170);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f, g));//was 1e-6
        Vector xmin = soln.search(new DenseVector(new double[]{6, 6}));
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{2.99999999929922, 2.00000000071777});//from Matlab
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-8);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-15);
    }

    /**
     * The global minimizer is at x = [0,0,0,0].
     */
    @Test
    public void test_BFGS_0040() throws Exception {
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

//        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});
//        System.out.println(ans);
//        System.out.println(f.evaluate(ans));

        BFGS optim1 = new BFGS(false, 1e-15, 20);
        IterativeMinimizer<Vector> soln = optim1.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));
//        System.out.println(xmin);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-6);

        BFGS optim2 = new BFGS(false, 1e-15, 40);
        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-8);

        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2.0001, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-8);

        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-8);
    }

    /**
     * The global minimizer is at x = [0,0,0,0].
     *
     * BFGS with possible switching to DFP seems to produce less accurate results.
     */
    @Test
    public void test_BFGS_0050() throws Exception {
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

        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});
//        System.out.println(ans);
//        System.out.println(f.evaluate(ans));

        BFGS optim1 = new BFGS(true, 1e-15, 45);
        IterativeMinimizer<Vector> soln = optim1.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));
//        System.out.println(xmin);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-6);

        BFGS optim2 = new BFGS(true, 1e-15, 60);
        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-8);

        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2.0001, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-7);

        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-4);
    }
}
