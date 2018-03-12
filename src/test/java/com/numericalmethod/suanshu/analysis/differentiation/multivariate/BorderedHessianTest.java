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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BorderedHessianTest {

    @Test
    public void test_BorderedHessian_0010() {
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

        RealScalarFunction g = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return 6 - x.get(1) - x.get(2);//f = 6 - x - y
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BorderedHessian instance = new BorderedHessian(f, g, new DenseVector(3., 3.));
        assertTrue(AreMatrices.equal(
                new DenseMatrix(
                new double[][]{
                    {0, -1, -1},
                    {-1, 0, 1},
                    {-1, 1, 0}
                }),
                instance, 1e-6));
        assertEquals(2, MatrixMeasure.det(instance), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_BorderedHessian_0020() {
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

        RealScalarFunction g = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector x) {
                return 6 - x.get(1) - x.get(2) - x.get(3);//f = 6 - x - y - z
            }

            public int dimensionOfDomain() {
                return 3;//domain does not match that of f
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BorderedHessian instance = new BorderedHessian(f, g, new DenseVector(3., 3.));
    }
}
