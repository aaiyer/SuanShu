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
package com.numericalmethod.suanshu.analysis.differentiation;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
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
public class RiddersTest {

    //<editor-fold defaultstate="collapsed" desc="tests for UnivariateRealFunction">
    @Test
    public void test_UnivariateRealFunction_0010() {

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.log(x);
            }
        };
        Ridders instance = new Ridders(f, 1);
        assertEquals(1, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());
        assertEquals(2, instance.evaluate(0.5), 1e-15);

        instance = new Ridders(f, 2);
        assertEquals(-4, instance.evaluate(0.5), 1e-6);

        instance = new Ridders(f, 3);
        assertEquals(16, instance.evaluate(0.5), 5e-5);//1e-6

        instance = new Ridders(f, 4);
        assertEquals(-96, instance.evaluate(0.5), 1e-3);
    }

    @Test
    public void test_UnivariateRealFunction_0020() {

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return (new Polynomial(new double[]{1, 2, 3, 4})).evaluate(x);//p(x) = x^3 + 2x^2 + 3x^1 + 4
            }
        };

        Ridders instance = new Ridders(f, 1);//p(x) = 3x^2 + 4x + 3
        assertEquals(1, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());
        assertEquals(10, instance.evaluate(1), 1e-14);

        instance = new Ridders(f, 2);//p(x) = 6x + 4
        assertEquals(10, instance.evaluate(1), 1e-5);

        instance = new Ridders(f, 3);//p(x) = 6
        assertEquals(6, instance.evaluate(1), 1e-5);

        instance = new Ridders(f, 4);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-5);

        instance = new Ridders(f, 5);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-5);

        instance = new Ridders(f, 6);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-6);

        instance = new Ridders(f, 6);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-6);

        instance = new Ridders(f, 7);//p(x) = 0
        assertEquals(0, instance.evaluate(1), 1e-6);
    }

    /**
     * Special value: x = 0
     */
    @Test
    public void test_UnivariateRealFunction_0030() {

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return (new Polynomial(new double[]{1, 2, 3, 4})).evaluate(x);//p(x) = x^3 + 2x^2 + 3x^1 + 4
            }
        };

        Ridders instance = new Ridders(f, 1);//p(x) = 3x^2 + 4x + 3
        assertEquals(1, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(3, instance.evaluate(0), 1e-3);

        instance = new Ridders(f, 2);//p(x) = 6x + 4
        assertEquals(4, instance.evaluate(0), 1e-1);//TODO: very poor approximation
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for RealScalarFunction">
    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0010() {

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

        Ridders instance = new Ridders(f, new int[]{1, 2});
        assertEquals(2, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(1, instance.evaluate(new DenseVector(1., 1.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(2., 2.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(-3., -3.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(1.5, -21.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(-0.1, 19.6)), 1e-8);

        //continuous function allows switching the order of differentiation
        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(1, instance.evaluate(new DenseVector(1., 1.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(2., 2.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(-3., -3.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(1.5, -21.)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(-0.1, 19.6)), 1e-8);
    }

    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0020() {

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

        Ridders instance = new Ridders(f, new int[]{1, 2});
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(3, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-7);
        assertEquals(-2, instance.evaluate(new DenseVector(-100., 0., -1.5)), 1e-8);

        //continuous function allows switching the order of differentiation
        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(3, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-7);
        assertEquals(-2, instance.evaluate(new DenseVector(-100., 0., -1.5)), 1e-8);
    }

    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0030() {

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

        Ridders instance = new Ridders(f, new int[]{1, 2});
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(2, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-7);
        assertEquals(1.0 + log(1) + 1, instance.evaluate(new DenseVector(1., 2., 1.)), 1e-6);

        //continuous function allows switching the order of differentiation
        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(2, instance.evaluate(new DenseVector(1., 1., 1.)), 1e-7);
        assertEquals(1.0 + log(1) + 1, instance.evaluate(new DenseVector(1., 2., 1.)), 1e-6);
    }

    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0040() {

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

        Ridders instance = new Ridders(f, new int[]{1, 2});
        assertEquals(2, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(exp(1) + exp(1), instance.evaluate(new DenseVector(1., 1.)), 1e-6);
        assertEquals(exp(-1.5 * 0.5) - 1.5 * 0.5 * exp(-1.5 * 0.5), instance.evaluate(new DenseVector(0.5, -1.5)), 1e-7);

        //continuous function allows switching the order of differentiation
        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(exp(1) + exp(1), instance.evaluate(new DenseVector(1., 1.)), 1e-6);
        assertEquals(exp(-1.5 * 0.5) - 1.5 * 0.5 * exp(-1.5 * 0.5), instance.evaluate(new DenseVector(0.5, -1.5)), 1e-7);
    }

    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0050() {

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

        Ridders dfz = new Ridders(f, new int[]{3});
        assertEquals(3, dfz.dimensionOfDomain());
        assertEquals(1, dfz.dimensionOfRange());

        assertEquals(fz.evaluate(new DenseVector(1., 1., 1.)), dfz.evaluate(new DenseVector(1., 1., 1.)), 1e-9);
        assertEquals(fz.evaluate(new DenseVector(1.5, -2.1, 91.)), dfz.evaluate(new DenseVector(1.5, -2.1, 91.)), 1e-8);

        Ridders dfzy = new Ridders(f, new int[]{3, 2});
        assertEquals(fzy.evaluate(new DenseVector(1., 1., 1.)), dfzy.evaluate(new DenseVector(1., 1., 1.)), 1e-8);
        assertEquals(fzy.evaluate(new DenseVector(-21.1, 19.78, 7.1)), dfzy.evaluate(new DenseVector(-21.1, 19.78, 7.1)), 1e-15);

        Ridders dfzyx = new Ridders(f, new int[]{3, 2, 1});
        assertEquals(fzyx.evaluate(new DenseVector(1., 1., 1.)), dfzyx.evaluate(new DenseVector(1., 1., 1.)), 1e-6);
        assertEquals(fzyx.evaluate(new DenseVector(-21.1, 19.78, 7.1)), dfzyx.evaluate(new DenseVector(-21.1, 19.78, 7.1)), 1e-15);
    }

    /**
     * Expect to have better accuracy than those in
     * com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifferenceTest.
     */
    @Test
    public void test_RealFunction_0060() {

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

        Ridders instance = new Ridders(f, new int[]{1, 2, 3});
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(1, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-8);
        assertEquals(1, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-9);

        instance = new Ridders(f, new int[]{1, 2, 3, 1});
        assertEquals(0, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-9);
        assertEquals(0, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-11);

        instance = new Ridders(f, new int[]{1, 2, 3, 1, 3, 2});
        assertEquals(0, instance.evaluate(new DenseVector(1.2, 1.3, 1.45678)), 1e-10);
        assertEquals(0, instance.evaluate(new DenseVector(-21.2, 21.3, -1.45678)), 1e-12);
    }

    /**
     * Test for near x = 0 values.
     */
    @Test
    public void test_RealFunction_0070() {

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

        Ridders instance = new Ridders(f, new int[]{1});
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);

        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 1e-15);
    }

    /**
     * Test for near x = 0 values.
     */
    @Test
    public void test_RealFunction_0080() {

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

        Ridders instance = new Ridders(f, new int[]{1});
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(0.0000001, instance.evaluate(new DenseVector(0., 0.0000001, 1.)), 1e-15);

        instance = new Ridders(f, new int[]{1, 2});
        assertEquals(0.0000001, instance.evaluate(new DenseVector(0., 0., 0.0000001)), 1e-15);
    }
    //</editor-fold>
}
