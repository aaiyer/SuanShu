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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class FiniteDifferenceTest {

    /**
     * Test of class FiniteDifference.
     */
    @Test
    public void testFiniteDifference_0010() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2);//f = xy
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1, 2});
        assertEquals(1, instance.evaluate(new DenseVector(1., 1.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(2., 2.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(-3., -3.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(1.5, -21.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(-0.1, 19.6)), 1e-6);

        //continuous function allows switching the order of differentiation
        instance = new FiniteDifference(f, new int[]{2, 1});
        assertEquals(1, instance.evaluate(new DenseVector(1., 1.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(2., 2.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(-3., -3.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(1.5, -21.)), 1e-6);
        assertEquals(1, instance.evaluate(new DenseVector(-0.1, 19.6)), 1e-6);
    }

    /**
     * Test of class FiniteDifference.
     */
    @Test
    public void testFiniteDifference_0020() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2) + 2 * x.get(1) * x.get(2) * x.get(3);//f = xy + 2xyz
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1, 2});//1 + 2z
        assertEquals(3, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-5);
        assertEquals(-2, instance.evaluate(new DenseVector(-100., 0., -1.5)), 1e-5);

        //continuous function allows switching the order of differentiation
        instance = new FiniteDifference(f, new int[]{2, 1});//1 + 2z
        assertEquals(3, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-5);
        assertEquals(-2, instance.evaluate(new DenseVector(-100., 0., -1.5)), 1e-5);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = x^y + xyz
     * fx = y*x^(y-1) + yz
     * fxy = x^(y-1) + y*x^(y-1)*log(x) + z
     */
    @Test
    public void testFiniteDifference_0030() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return pow(x.get(1), x.get(2)) + x.get(1) * x.get(2) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1, 2});
        assertEquals(2, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-5);
        assertEquals(1.0 + log(1) + 1, instance.evaluate(new DenseVector(1., 2., 1.)), 1e-5);

        instance = new FiniteDifference(f, new int[]{2, 1});
        assertEquals(2, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-5);
        assertEquals(1.0 + log(1) + 1, instance.evaluate(new DenseVector(1., 2., 1.)), 1e-5);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = exp(x * y)
     * fx = y * exp(x * y)
     * fxy = exp(x * y) + y * x * exp(x * y)
     */
    @Test
    public void testFiniteDifference_0040() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return exp(x.get(1) * x.get(2));
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1, 2});
        assertEquals(exp(1) + exp(1), instance.evaluate(new DenseVector(1., 1.)), 1e-5);
        assertEquals(exp(-1.5 * 0.5) - 1.5 * 0.5 * exp(-1.5 * 0.5), instance.evaluate(new DenseVector(0.5, -1.5)), 1e-5);

        //continuous function allows switching the order of differentiation
        instance = new FiniteDifference(f, new int[]{2, 1});
        assertEquals(exp(1) + exp(1), instance.evaluate(new DenseVector(1., 1.)), 1e-5);
        assertEquals(exp(-1.5 * 0.5) - 1.5 * 0.5 * exp(-1.5 * 0.5), instance.evaluate(new DenseVector(0.5, -1.5)), 1e-5);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = [x ^ exp(y)] * log(z) + x * y * z
     * fz = [x ^ exp(y)] / z + x * y
     * fzy = [x ^ exp(y)] * log(x) * exp(y) / z + x
     * fzyx = [x ^ (exp(y) - 1)] * exp(y) / z * (1 + exp(y) * log(x)) + 1
     */
    @Test
    public void testFiniteDifference_0050() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return pow(x.get(1), exp(x.get(2))) * log(x.get(3)) + x.get(1) * x.get(2) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealScalarFunction fz = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return pow(x.get(1), exp(x.get(2))) / x.get(3) + x.get(1) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealScalarFunction fzy = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return pow(x.get(1), exp(x.get(2))) * log(x.get(1)) * exp(x.get(2)) / x.get(3) + x.get(1);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealScalarFunction fzyx = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return pow(x.get(1), exp(x.get(2)) - 1) * exp(x.get(2)) / x.get(3) * (1 + exp(x.get(2)) * log(x.get(1))) + 1;
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference dfz = new FiniteDifference(f, new int[]{3});
        assertEquals(fz.evaluate(new DenseVector(1., 1., 1.)), dfz.evaluate(new DenseVector(1., 1., 1.)), 1e-9);
        assertEquals(fz.evaluate(new DenseVector(1.5, -2.1, 91.)), dfz.evaluate(new DenseVector(1.5, -2.1, 91.)), 1e-7);

        FiniteDifference dfzy = new FiniteDifference(f, new int[]{3, 2});
        assertEquals(fzy.evaluate(new DenseVector(1., 1., 1.)), dfzy.evaluate(new DenseVector(1., 1., 1.)), 1e-6);
        assertEquals(fzy.evaluate(new DenseVector(-21.1, 19.78, 7.1)), dfzy.evaluate(new DenseVector(-21.1, 19.78, 7.1)), 1e-9);

        FiniteDifference dfzyx = new FiniteDifference(f, new int[]{3, 2, 1});
        assertEquals(fzyx.evaluate(new DenseVector(1., 1., 1.)), dfzyx.evaluate(new DenseVector(1., 1., 1.)), 1e-5);
        assertEquals(fzyx.evaluate(new DenseVector(-21.1, 19.78, 7.1)), dfzyx.evaluate(new DenseVector(-21.1, 19.78, 7.1)), 1e-9);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = xyz
     * fxyz = 1
     */
    @Test
    public void testFiniteDifference_0060() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1, 2, 3});
        assertEquals(1, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-5);
        assertEquals(1, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-5);

        instance = new FiniteDifference(f, new int[]{1, 2, 3, 1});
        assertEquals(0, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-4);
        assertEquals(0, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-5);

        instance = new FiniteDifference(f, new int[]{1, 2, 3, 1, 3, 2});
        assertEquals(0, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-4);
        assertEquals(0, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-5);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = xyz
     * fxyz = 1
     *
     * Test for near x = 0 values.
     */
    @Test
    public void testFiniteDifference_0070() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);

        instance = new FiniteDifference(f, new int[]{1, 2});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);

        instance = new FiniteDifference(f, new int[]{1, 2, 3});
        assertEquals(1, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);

        instance = new FiniteDifference(f, new int[]{1, 2, 3, 3});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);
    }

    /**
     * Test of class FiniteDifference.
     *
     * f = xyz
     * fxyz = 1
     *
     * Test for near x = 0 values.
     */
    @Test
    public void testFiniteDifference_0080() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return x.get(1) * x.get(2) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        FiniteDifference instance = new FiniteDifference(f, new int[]{1});
        assertEquals(0, instance.evaluate(new DenseVector(0.0001, 0., 0.)), 1e-15);

        instance = new FiniteDifference(f, new int[]{1, 2});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0.0001, 0.)), 1e-15);

        instance = new FiniteDifference(f, new int[]{1, 2, 3});
        assertEquals(1, instance.evaluate(new DenseVector(0.0001, 0.0001, 0.0001)), 1e-6);

        instance = new FiniteDifference(f, new int[]{1, 2, 3, 3});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);
    }
}
