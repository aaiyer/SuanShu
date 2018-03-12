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
public class JacobianTest {

    /**
     * Test of class Jacobian.
     */
    @Test
    public void test_Jacobian_0010() {

        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector z) {
                double r = z.get(1);
                double theta = z.get(2);
                double phi = z.get(3);

                double[] result = new double[3];
                result[0] = r * sin(theta) * cos(phi);
                result[1] = r * sin(theta) * sin(phi);
                result[2] = r * cos(theta);
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 3;
            }
        };

        Jacobian instance = new Jacobian(f, new DenseVector(0., 0., 0.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {1, 0, 0}
                }),
                instance, 1e-6));
        assertEquals(0, MatrixMeasure.det(instance), 1e-15);

        instance = new Jacobian(f, new DenseVector(1., PI / 2, PI / 2));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, 0, -1},
                    {1, 0, 0},
                    {0, -1, 0}
                }),
                instance, 1e-6));
        assertEquals(1, MatrixMeasure.det(instance), 1e-8);

        instance = new Jacobian(f, new DenseVector(1., PI / 2, PI));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {-1, 0, 0},
                    {0, 0, -1},
                    {0, -1, 0}
                }),
                instance, 1e-6));
        assertEquals(1, MatrixMeasure.det(instance), 1e-8);
    }

    /**
     * Test of class Jacobian.
     */
    @Test
    public void test_Jacobian_0020() {

        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] result = new double[4];
                result[0] = x.get(1);
                result[1] = 5 * x.get(3);
                result[2] = 4 * x.get(2) * x.get(2) - 2 * x.get(3);
                result[3] = x.get(3) * sin(x.get(1));
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 4;
            }
        };

        Jacobian instance = new Jacobian(f, new DenseVector(1., 1., 1.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {1, 0, 0},
                    {0, 0, 5},
                    {0, 8, -2},
                    {cos(1), 0, sin(1)}
                }),
                instance, 1e-6));

        instance = new Jacobian(f, new DenseVector(1., 2., 3.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {1, 0, 0},
                    {0, 0, 5},
                    {0, 16, -2},
                    {3 * cos(1), 0, sin(1)}
                }),
                instance, 1e-6));
    }

    /**
     * Test of class Jacobian.
     */
    @Test
    public void test_Jacobian_0030() {

        RealVectorFunction f = new RealVectorFunction() {

            public Vector evaluate(Vector x) {
                double[] result = new double[3];
                result[0] = 5 * x.get(2);
                result[1] = 4 * x.get(1) * x.get(1) - 2 * sin(x.get(2) * x.get(3));
                result[2] = x.get(2) * x.get(3);
                return new DenseVector(result);
            }

            public int dimensionOfDomain() {
                return 3;
            }

            public int dimensionOfRange() {
                return 3;
            }
        };

        Jacobian instance = new Jacobian(f, new DenseVector(1., 1., 1.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, 5, 0},
                    {8, -2 * cos(1), -2 * cos(1)},
                    {0, 1, 1}
                }),
                instance, 1e-6));
        assertEquals(-40, MatrixMeasure.det(instance), 1e-6);
    }
}
