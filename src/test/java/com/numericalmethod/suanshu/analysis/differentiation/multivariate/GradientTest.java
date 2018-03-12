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
package com.numericalmethod.suanshu.analysis.differentiation.multivariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class GradientTest {

    /**
     * Test of class Gradient.
     */
    @Test
    public void testGradient_0010() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2) * x.get(3);//f = xyz
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Gradient instance = new Gradient(f, new DenseVector(1., 1., 1.));
        assertArrayEquals(new double[]{1., 1., 1.}, instance.toArray(), 1e-9);
    }

    /**
     * Test of class Gradient.
     */
    @Test
    public void testGradient_0020() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return sin(x.get(1));//sin(x)
            }

            public int dimensionOfDomain() {
                return 1;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Gradient instance = new Gradient(f, new DenseVector(PI));
        assertArrayEquals(new double[]{-1.}, instance.toArray(), 1e-9);
    }

    /**
     * Test of class Gradient.
     *
     * From wiki.
     */
    @Test
    public void testGradient_0030() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return 2 * x.get(1) + 3 * x.get(2) * x.get(2) - sin(x.get(3));//2x + 3y^2 - sin(z)
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] result = new double[3];
                result[0] = 2;
                result[1] = 6 * x.get(2);
                result[2] = -cos(x.get(3));

                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 3;
            }
        };

        Vector x = new DenseVector(1., 2., 3.);
        Gradient instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(1.5, -2.901, 3.33);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(0., 0., 0.);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(-0.000001, 990., 0.0000001);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(100., 990., -0.0000001);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-4);
    }

    /**
     * Test of class Gradient.
     *
     * From R.
     */
    @Test
    public void testGradient_0040() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return sin(10 * x.get(1)) - exp(-x.get(1));
            }

            public int dimensionOfDomain() {
                return 1;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction g = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] result = new double[1];
                result[0] = 10 * cos(10 * x.get(1)) + exp(-x.get(1));

                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 1;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Vector x = new DenseVector(1., 2., 3.);
        Gradient instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(1.5, -2.901, 3.33);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(-0.000001, 990., 0.0000001);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(100., 990., -0.0000001);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);

        x = new DenseVector(0., 0., 0.);
        instance = new Gradient(f, x);
        assertArrayEquals(g.evaluate(x).toArray(), instance.toArray(), 1e-6);
    }
}
