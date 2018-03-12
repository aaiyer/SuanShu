/*
 * Copyright (c)
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
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author Haksun Li
 */
public class HessianTest {

    @Test
    public void test_Hessian_0010() {

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

        Hessian instance = new Hessian(f, new DenseVector(1., 1.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, 1},
                    {1, 0}
                }),
                instance, 1e-6));
        assertEquals(-1, MatrixMeasure.det(instance), 1e-6);

        instance = new Hessian(f, new DenseVector(0., 0.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, 1},
                    {1, 0}
                }),
                instance, 1e-6));
        assertEquals(-1, MatrixMeasure.det(instance), 1e-15);
    }

    @Test
    public void test_Hessian_0020() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return sin(x.get(1)) + cos(x.get(2));//f = sin(x) + cos(y)
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction h = new RealVectorFunction() {

            public Vector evaluate(Vector z) {
                double x = z.get(1);
                double y = z.get(2);

                double[] result = new double[4];
                result[0] = -sin(x);
                result[1] = 0;
                result[2] = 0;
                result[3] = -cos(y);

                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        Hessian instance = new Hessian(f, new DenseVector(1., 1.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(1., 1.)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(0.5, PI / 2.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(0.5, PI / 2.)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(-100.1, 99.9));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(-100.1, 99.9)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(0., 0.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(0., 0.)).toArray(),
                2, 2),
                instance, 1e-4));
    }

    @Test
    public void test_Hessian_0030() {

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return sin(x.get(1)) * cos(x.get(2));//f = sin(x) * cos(y)
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        RealVectorFunction h = new RealVectorFunction() {

            public DenseVector evaluate(Vector z) {
                double x = z.get(1);
                double y = z.get(2);

                double[] result = new double[4];
                result[0] = -sin(x) * cos(y);
                result[1] = -cos(x) * sin(y);
                result[2] = -cos(x) * sin(y);
                result[3] = -sin(x) * cos(y);

                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        Hessian instance = new Hessian(f, new DenseVector(1., 1.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(1., 1.)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(0., 0.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(0., 0.)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(PI, -PI / 3));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(PI, -PI / 3)).toArray(),
                2, 2),
                instance, 1e-6));

        instance = new Hessian(f, new DenseVector(-99.01, 100.99));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                h.evaluate(new DenseVector(-99.01, 100.99)).toArray(),
                2, 2),
                instance, 1e-6));
    }
}
