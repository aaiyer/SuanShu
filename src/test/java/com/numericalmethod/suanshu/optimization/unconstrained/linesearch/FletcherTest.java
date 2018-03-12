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
package com.numericalmethod.suanshu.optimization.unconstrained.linesearch;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class FletcherTest {

    /**
     * Test of class Fletcher.
     */
    @Test
    public void testSearch_0010() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector z) {
                double x = z.get(1);
                return x * x + 2 * x + 1;
            }

            public int dimensionOfDomain() {
                return 1;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector z) {
                double x = z.get(1);
                double[] result = new double[1];
                result[0] = 2 * x + 2;
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 1;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Fletcher instance = new Fletcher(0.1, 0.1, 0.1, 9, 1e-15, 100);//TODO: why 9 as suggested in the reference? should be < 1
        LineSearch.Solution soln = instance.solve(new C2OptimProblemImpl(f, g));

        Vector x = new DenseVector(new double[]{-5});
        Vector d = new DenseVector(new double[]{1});
        double a = soln.linesearch(x, d);
        Vector xmin = x.add(d.scaled(a));//xmin = x + a * d
        assertEquals(-1.0, xmin.get(1), 1e-14);

        x = new DenseVector(new double[]{10});
        d = new DenseVector(new double[]{-1});
        a = soln.linesearch(x, d);
        xmin = x.add(d.scaled(a));//xmin = x + a * d
        assertEquals(-1.0, xmin.get(1), 1e-14);

        x = new DenseVector(new double[]{9.7865});
        d = new DenseVector(new double[]{-0.434});
        a = soln.linesearch(x, d);
        xmin = x.add(d.scaled(a));//xmin = x + a * d
        assertEquals(-1.0, xmin.get(1), 1e-13);
    }

    /**
     * Test of class Fletcher.
     *
     * Exercise 4.11.
     */
    @Test
    public void testSearch_0020() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double fx = 0.7 * pow(x.get(1), 4) - 8 * pow(x.get(1), 2) + 6 * pow(x.get(2), 2) + cos(x.get(1) * x.get(2)) - 8 * x.get(1);
                return fx;
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
                double[] result = new double[2];
                result[0] = 2.8 * pow(x.get(1), 3) - 16 * x.get(1) - x.get(2) * sin(x.get(1) * x.get(2)) - 8;
                result[1] = 12 * x.get(2) - x.get(1) * sin(x.get(1) * x.get(2));
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        Fletcher instance = new Fletcher(0.1, 0.1, 0.1, 0.75, 1e-15, 100);
        LineSearch.Solution soln = instance.solve(new C2OptimProblemImpl(f, g));

        Vector x = new DenseVector(new double[]{-PI, PI});
        Vector d = new DenseVector(new double[]{1, -1.1});
        double a = soln.linesearch(x, d);
        assertEquals(4.595944872927603, a, 1e-15);//from matlab
//        Vector xmin = x.add(d.scaled(a));//xmin = x + a * d
//        System.out.println(a);
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin.toArray()));
    }

    /**
     * Test of class Fletcher.
     *
     * Sample Solution, SA.5.
     */
    @Test
    public void testSearch_0030() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double x1 = x.get(1);
                double x2 = x.get(2);

                double fx = 0.6 * pow(x2, 4) + 5 * pow(x1, 2) - 7 * pow(x2, 2) + sin(x1 * x2) - 5 * x2;
                return fx;
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

                double[] result = new double[2];
                result[0] = 10 * x1 + x2 * cos(x1 * x2);
                result[1] = 2.4 * pow(x2, 3) - 14 * x2 + x1 * cos(x1 * x2) - 5.0;//there was a typo error in progSA5c2.m; the sign in front of cos was wrong
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        Fletcher instance = new Fletcher(0.1, 0.1, 0.1, 0.75, 1e-10, 400);
        LineSearch.Solution soln = instance.solve(new C2OptimProblemImpl(f, g));

        Vector x = new DenseVector(new double[]{-PI, -PI});
        Vector d = new DenseVector(new double[]{1.0, 1.01});
        double a = soln.linesearch(x, d);
        assertEquals(5.012310046346364, a, 1e-15);//from matlab
//        Vector xmin = x.add(d.scaled(a));//xmin = x + a * d
//        System.out.println(a);
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin.toArray()));

        d = new DenseVector(new double[]{1.0, 0.85});
        a = soln.linesearch(x, d);
        assertEquals(2.928589840962318, a, 1e-15);//from matlab
//        xmin = x.add(d.scaled(a));//xmin = x + a * d
//        System.out.println(a);
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin.toArray()));
    }

    /**
     * Test of class Fletcher.
     *
     * Himmelblau funtion.
     */
    @Test
    public void testSearch_0040() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                double fx = pow((pow(x.get(1), 2) + x.get(2) - 11), 2) + pow((x.get(1) + pow(x.get(2), 2) - 7), 2);
                return fx;
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
                double[] result = new double[2];
                double w1 = (pow(x.get(1), 2) + x.get(2) - 11);
                double w2 = (x.get(1) + pow(x.get(2), 2) - 7);

                result[0] = 4 * w1 * x.get(1) + 2 * w2;
                result[1] = 2 * w1 + 4 * w2 * x.get(2);
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        Fletcher instance = new Fletcher(0.1, 0.1, 0.1, 0.75, 1e-10, 400);
        LineSearch.Solution soln = instance.solve(new C2OptimProblemImpl(f, g));

        Vector x = new DenseVector(new double[]{6, 6});
        Vector d = new DenseVector(new double[]{-1, -1});
        double a = soln.linesearch(x, d);
        assertEquals(3.458462682256765, a, 1e-15);//from matlab
//        Vector xmin = x.add(d.scaled(a));//xmin = x + a * d
//        System.out.println(a);
//        System.out.println(xmin);
//        System.out.println(f.evaluate(xmin.toArray()));
    }
}
