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
package com.numericalmethod.suanshu.analysis.function.rn2rm;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RealVectorSubFunctionTest {

    /**
     * both variables held fixed
     */
    @Test
    public void test_0010() {
        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] value = new double[2];
                value[0] = x.get(1) * x.get(1) + x.get(2) * x.get(2);
                value[1] = x.get(1) * x.get(1) - x.get(2) * x.get(2);

                return new DenseVector(value);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(1, 1.);
        fixed.put(2, 2.);

        RealVectorSubFunction instance = new RealVectorSubFunction(f, fixed);
        assertEquals(0, instance.dimensionOfDomain());
        assertEquals(2, instance.dimensionOfRange());

        assertArrayEquals(new double[]{5, -3}, instance.evaluate(new DenseVector(0., 0.)).toArray(), 0);
        assertArrayEquals(new double[]{5, -3}, instance.evaluate(new DenseVector(-1., 0.)).toArray(), 0);
        assertArrayEquals(new double[]{5, -3}, instance.evaluate(new DenseVector(10., 0.)).toArray(), 0);
    }

    /**
     * one variable held fixed
     */
    @Test
    public void test_0020() {
        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] value = new double[2];
                value[0] = x.get(1) * x.get(1) + x.get(2) * x.get(2);
                value[1] = x.get(1) * x.get(1) - x.get(2) * x.get(2);

                return new DenseVector(value);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        Map<Integer, Double> fixed = new HashMap<Integer, Double>();
        fixed.put(1, 0.);

        RealVectorSubFunction instance = new RealVectorSubFunction(f, fixed);
        assertEquals(1, instance.dimensionOfDomain());
        assertEquals(2, instance.dimensionOfRange());

        assertArrayEquals(new double[]{0, 0}, instance.evaluate(new DenseVector(0.)).toArray(), 0);
        assertArrayEquals(new double[]{1, -1}, instance.evaluate(new DenseVector(-1.)).toArray(), 0);
        assertArrayEquals(new double[]{100, -100}, instance.evaluate(new DenseVector(10.)).toArray(), 0);
    }

    /**
     * no variable held fixed
     */
    @Test
    public void test_0030() {
        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] value = new double[2];
                value[0] = x.get(1) * x.get(1) + x.get(2) * x.get(2);
                value[1] = x.get(1) * x.get(1) - x.get(2) * x.get(2);

                return new DenseVector(value);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 2;
            }
        };

        RealVectorSubFunction instance = new RealVectorSubFunction(f, new HashMap<Integer, Double>());
        assertEquals(2, instance.dimensionOfDomain());
        assertEquals(2, instance.dimensionOfRange());

        assertArrayEquals(new double[]{0, 0}, instance.evaluate(new DenseVector(0., 0.)).toArray(), 0);
        assertArrayEquals(new double[]{1, 1}, instance.evaluate(new DenseVector(-1., 0.)).toArray(), 0);
        assertArrayEquals(new double[]{100, 100}, instance.evaluate(new DenseVector(10., 0.)).toArray(), 0);
    }
}
