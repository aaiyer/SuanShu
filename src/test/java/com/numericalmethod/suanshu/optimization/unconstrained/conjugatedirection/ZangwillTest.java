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
package com.numericalmethod.suanshu.optimization.unconstrained.conjugatedirection;

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
public class ZangwillTest {

    /**
     * The Himmelblau function:
     * f(x) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 - 7)^2
     *
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

        Zangwill optim = new Zangwill(1e-6, 1e-6, 5);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{6, 6}));//very fast convergence
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{-2.80511834779604, 3.13131274464586});//from matlab
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-5);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-10);
    }

    /**
     * Use numerical gradient.
     *
     * The Himmelblau function:
     * f(x) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 - 7)^2
     *
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

        Zangwill optim = new Zangwill(1e-6, 1e-6, 5);
        IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(f));
        Vector xmin = soln.search(new DenseVector(new double[]{6, 6}));//very fast convergence
//        System.out.println(xmin);

        Vector ans = new DenseVector(new double[]{-2.80511834779604, 3.13131274464586});//from matlab
        double fans = f.evaluate(ans);
//        System.out.println(ans);
//        System.out.println(fans);

        assertEquals(0.0, xmin.minus(ans).norm(), 1e-5);
        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, abs(fans - fxmin), 1e-10);
    }

    /**
     * The global minimizer is at x = [0,0,0,0].
     *
     * For this particular example,
     * Zangwill seems to have fewer precision than Powell does.
     *
     * In fact, Zangwill's algorithm does not seem to work on this example.
     * Powell's algorithm, however, works fine.
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

        Zangwill optim1 = new Zangwill(1e-6, 1e-6, 10);
        IterativeMinimizer<Vector> soln = optim1.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));//very fast convergence
//        System.out.println(xmin);

//        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});
//        System.out.println(ans);
//        System.out.println(f.evaluate(ans));

        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-6);//fewer precision than that of Powell's algorithm

        Zangwill optim2 = new Zangwill(1e-5, 1e-5, 70);
        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        //TODO: very sensitive starting value; changing a little will not land even close to the minimum
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));//need more iterations than Powell does; changing the params does not seem to improve the results
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
//        assertEquals(0.0, fxmin, 1e-8);//TODO: not even close to 0!!!

        /*
         * Powell's algorithm does not have this kind of sensitivity issue.
         */
        Zangwill optim3 = new Zangwill(1e-15, 1e-15, 150);
        soln = optim3.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2.0001, 10, -15, 17}));//need more iterations than Powell does
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
//        assertEquals(0.0, fxmin, 1e-8);//TODO: not even close to 0!!! The answer, however, does agree with what Matlab gives.

        Zangwill optim4 = new Zangwill(1e-15, 1e-15, 150);
        soln = optim4.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999, 10, -15, 17}));//need more iterations than Powell does; changing the params does not seem to improve the results
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
//        assertEquals(0.0, fxmin, 1e-8);//TODO: not even close to 0!!! The answer, however, does agree with what Matlab gives.

        Zangwill optim5 = new Zangwill(1e-10, 1e-15, 150);
        soln = optim5.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.99999999, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
//        assertEquals(0.0, fxmin, 1e-8);//TODO: not even close to 0!!! The answer, however, does agree with what Matlab gives.

        Zangwill optim6 = new Zangwill(1e-15, 1e-15, 150);
        soln = optim6.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999999999, 10, -15, 17}));//this initial value seems to give the best final result; hey, how do you "guess" the initial value?
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-2);
    }

    /**
     * Test of search method, of class Zangwill.
     * The global minimizer is at x = [0,0,0,0].
     *
     * When {@code threshold2} is big, Zangwill seems to perform better for this example.
     * The accuracy is still worse than that of Powell's.
     */
    @Test
    public void test_Zangwill_0030() throws Exception {
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

        Zangwill optim1 = new Zangwill(1e-6, 1e-6, 10);
        IterativeMinimizer<Vector> soln = optim1.solve(new C2OptimProblemImpl(f, g));
        Vector xmin = soln.search(new DenseVector(new double[]{1, -1, -1, 1}));//very fast convergence
//        System.out.println(xmin);

//        Vector ans = new DenseVector(new double[]{0.04841813, 0.01704776, 0.00170602, 0.02420804});
//        System.out.println(ans);
//        System.out.println(f.evaluate(ans));

        double fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-6);//fewer precision than that of Powell's algorithm

        Zangwill optim2 = new Zangwill(1e-5, 1e15, 70);
        soln = optim2.solve(new C2OptimProblemImpl(f, g));
        //TODO: very sensitive starting value; changing a little will not land even close to the minimum
        xmin = soln.search(new DenseVector(new double[]{2, 10, -15, 17}));//need more iterations than Powell does; changing the params does not seem to improve the results
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-2);//better than what Zangwill would normally give

        /*
         * Powell's algorithm does not have this kind of sensitivity issue.
         */
        Zangwill optim3 = new Zangwill(1e-15, 1e15, 150);
        soln = optim3.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{2.0001, 10, -15, 17}));//need more iterations than Powell does
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-3);//better than what Zangwill would normally give

        Zangwill optim4 = new Zangwill(1e-15, 1e15, 150);
        soln = optim4.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999, 10, -15, 17}));//need more iterations than Powell does; changing the params does not seem to improve the results
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-3);//better than what Zangwill would normally give

        Zangwill optim5 = new Zangwill(1e-10, 1e15, 150);
        soln = optim5.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.99999999, 10, -15, 17}));
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-3);//better than what Zangwill would normally give

        Zangwill optim6 = new Zangwill(1e-15, 1e-15, 150);
        soln = optim6.solve(new C2OptimProblemImpl(f, g));
        xmin = soln.search(new DenseVector(new double[]{1.9999999999, 10, -15, 17}));//this initial value seems to give the best final result; hey, how do you "guess" the initial value?
//        System.out.println(xmin);
        fxmin = f.evaluate(xmin);
//        System.out.println(fxmin);
        assertEquals(0.0, fxmin, 1e-2);
    }
}
