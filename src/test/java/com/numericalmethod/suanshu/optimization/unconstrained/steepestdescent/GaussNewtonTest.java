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

import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class GaussNewtonTest {

    /**
     * For the same objective function,
     * Gauss-Newton is much more effective than Newton-Raphson.
     *
     * The numbers of iterations are 13 vs. 36686.
     *
     * @see "Solution for SA.8 (c). Practical Optimization: Algorithms and Engineering Applications. Andreas Antoniou and Wu-Sheng Lu."
     *
     * f <- function(x)
     * {
     * x1 = x.get(2)
     * x2 = x.get(3)
     * x3 = x.get(4)
     * x4 = x[4]
     *
     * v1 = (x1 - 4*x2)^2
     * v2 = sqrt(12) * (x3 - x4)^2
     * v3 = sqrt(3) * (x2 - 10*x3)
     * v4 = sqrt(55) * (x1 - 2*x4)
     *
     * m = matrix(c(v1,v2,v3,v4),4,1)
     *
     * return(m)
     * }
     *
     *
     * F <- function(x)
     * {
     * x1 = x.get(2)
     * x2 = x.get(3)
     * x3 = x.get(4)
     * x4 = x[4]
     *
     * v = (x1 - 4*x2)^4
     * v = v + 12 * (x3 - x4)^4
     * v = v + 3 * (x2 - 10*x3)^2
     * v = v + 55 * (x1 - 2*x4)^2
     *
     * return(v)
     * }
     *
     *
     * g <- function(x)
     * {
     * x1 = x.get(2)
     * x2 = x.get(3)
     * x3 = x.get(4)
     * x4 = x[4]
     *
     * v1 = 4 * (x1 - 4*x2)^3 + 110*(x1 - 2*x4)
     * v2 = -16*(x1 - 4*x2)^3 + 6*(x2 - 10*x3)
     * v3 = 48 * (x3 - x4)^3 - 60*(x2 - 10*x3)
     * v4 = -48*(x3 - x4)^3 - 220*(x1 - 2*x4)
     *
     * v = c(v1,v2,v3,v4)
     *
     * return(v)
     * }
     *
     *
     * J <- function(x)
     * {
     * x1 = x.get(2)
     * x2 = x.get(3)
     * x3 = x.get(4)
     * x4 = x[4]
     *
     * j11 = 2*(x1 - 4*x2)
     * j12 = -8*(x1 - 4*x2)
     *
     * j23 = 2*sqrt(12)*(x3 - x4)
     * j44 = -j23
     *
     * j32 = sqrt(3)
     * j33 = -10 * sqrt(3)
     *
     * j41 = sqrt(55)
     * j44 = -2 * sqrt(55)
     *
     * m = matrix(rep(0,16),4,4)
     * m[1,1] = j11
     * m[1,2] = j12
     * m[2,3] = j23
     * m[2,4] = j24
     * m[3,2] = j32
     * m[3,3] = j33
     * m[4,1] = j41
     * m[4,4] = j44
     *
     * return(m)
     * }
     *
     */
    @Test
    public void test_GaussNewton_0010() throws Exception {
        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                double[] fx = new double[4];
                fx[0] = pow(x1 - 4 * x2, 2);
                fx[1] = sqrt(12) * pow(x3 - x4, 2);
                fx[2] = sqrt(3) * (x2 - 10 * x3);
                fx[3] = sqrt(55) * (x1 - 2 * x4);

                return new DenseVector(fx);
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        RntoMatrix J = new RntoMatrix() {

            public Matrix evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);
                double x3 = x.get(3);
                double x4 = x.get(4);

                Matrix Jx = new DenseMatrix(4, 4);

                double value = 2 * (x1 - 4 * x2);
                Jx.set(1, 1, value);

                value = -8 * (x1 - 4 * x2);
                Jx.set(1, 2, value);

                value = 2 * sqrt(12) * (x3 - x4);
                Jx.set(2, 3, value);
                Jx.set(2, 4, -value);

                Jx.set(3, 2, sqrt(3));
                Jx.set(3, 3, -10 * sqrt(3));

                Jx.set(4, 1, sqrt(55));
                Jx.set(4, 4, -2 * sqrt(55));

                return Jx;
            }

            public int dimensionOfDomain() {
                return 4;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        GaussNewton optim1 = new GaussNewton(1e-6, 30);

        IterativeMinimizer<Vector> soln = optim1.solve(f, J);//analytical gradient
        Vector initial = new DenseVector(new double[]{1, -1, -1, 1});
        Vector xmin = soln.search(initial);
        double fxmin = f.evaluate(xmin).norm();
        assertEquals(0.0, fxmin, 1e-11);
//        System.out.println("xmin");
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin));

        GaussNewton optim2 = new GaussNewton(1e-6, 30);
        soln = optim2.solve(f);//numerical gradient
        xmin = soln.search(initial);
        fxmin = f.evaluate(xmin).norm();
        assertEquals(0.0, fxmin, 1e-11);
//        System.out.println("xmin");
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin));

        soln = optim1.solve(f);//numerical gradient
        initial = new DenseVector(new double[]{2, 10, -15, 17});
        xmin = soln.search(initial);//TODO: algorithm OK; but need to fix the precision problem
        fxmin = f.evaluate(xmin).norm();
        assertEquals(0.0, fxmin, 1e-11);
//        System.out.println("xmin");
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin));

        GaussNewton optim3 = new GaussNewton(1e-6, 17);
        soln = optim3.solve(f);//numerical gradient
        xmin = soln.search(initial);
        fxmin = f.evaluate(xmin).norm();
        assertEquals(0.0, fxmin, 1e-11);
//        System.out.println("xmin");
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin));
    }
}
