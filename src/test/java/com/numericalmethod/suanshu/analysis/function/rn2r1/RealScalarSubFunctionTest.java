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
package com.numericalmethod.suanshu.analysis.function.rn2r1;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RealScalarSubFunctionTest {

    @Test
    public void test_0010() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(2, 1.);
        fixed.put(3, 2.);

        RealScalarSubFunction instance = new RealScalarSubFunction(f, fixed);
        assertEquals(1, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(5, instance.evaluate(0), 0);
        assertEquals(6, instance.evaluate(-1), 0);
        assertEquals(105, instance.evaluate(10), 0);
    }

    @Test
    public void test_0020() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(2, 1.);

        RealScalarSubFunction instance = new RealScalarSubFunction(f, fixed);
        assertEquals(2, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(5, instance.evaluate(new DenseVector(0., 2.)), 0);
        assertEquals(6, instance.evaluate(new DenseVector(-1., 2.)), 0);
        assertEquals(105, instance.evaluate(new DenseVector(10., 2.)), 0);

        assertEquals(11, instance.evaluate(new DenseVector(-1., 3.)), 0);
    }

    /**
     * result is a constant function
     */
    @Test
    public void test_0030() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(1, 0.);
        fixed.put(2, 1.);
        fixed.put(3, 2.);

        RealScalarSubFunction instance = new RealScalarSubFunction(f, fixed);
        assertEquals(0, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(5, instance.evaluate(0), 0);
        assertEquals(5, instance.evaluate(-1), 0);
        assertEquals(5, instance.evaluate(10), 0);
    }

    @Test(expected = RuntimeException.class)
    public void test_0040() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(0, 0.);
        RealScalarSubFunction instance = new RealScalarSubFunction(f, fixed);
    }

    @Test(expected = RuntimeException.class)
    public void test_0050() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(4, 3.);//invalid
        RealScalarSubFunction instance = new RealScalarSubFunction(f, fixed);
    }

    /**
     * no integral variable
     */
    @Test
    public void test_0060() {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2) + x.get(3) * x.get(3);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealScalarSubFunction instance = new RealScalarSubFunction(f, new HashMap<Integer, Double>());
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());

        assertEquals(0, instance.evaluate(new DenseVector(0., 0., 0.)), 0);
        assertEquals(5, instance.evaluate(new DenseVector(0., 1., 2.)), 0);
    }
}
