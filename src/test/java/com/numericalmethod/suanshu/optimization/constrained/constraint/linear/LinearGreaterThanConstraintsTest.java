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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.constraint.ConstraintsUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearGreaterThanConstraintsTest {

    @Test
    public void test_getFeasibleInitialPoint_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {-1, -2, 0, 0},
                    {0, 0, 0, 1},
                    {0, 0, 1, 1},
                    {0, 0, -1, -2}
                });
        Vector b = new DenseVector(new double[]{0, 0, -2, 2, 3, -6});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);

        Vector x = instance.getFeasibleInitialPoint();
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(DoubleArrayMath.min(A.multiply(x0).minus(b).toArray()) >= 0);// Ax0 > b
        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }

    @Test
    public void test_getFeasibleInitialPoint_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });
        Vector b = new DenseVector(new double[]{0, 0, 0});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);// x >= 0

        Matrix Aeq = new DenseMatrix(new double[][]{{1, 1, 1}});
        Vector beq = new DenseVector(3.0);
        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 + x3 = 3
                Aeq, beq);

        Vector x = instance.getFeasibleInitialPoint(equal);
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertTrue(ConstraintsUtils.isSatisfied(equal, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }

    @Test
    public void test_getFeasibleInitialPoint_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -2},
                    {-1, -2},
                    {-1, 2},
                    {1, 0},
                    {0, 1}
                });
        Vector b = new DenseVector(new double[]{-2, -6, -2, 0, 0});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);

        Vector x = instance.getFeasibleInitialPoint();
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(DoubleArrayMath.min(A.multiply(x0).minus(b).toArray()) >= 0);// Ax0 > b
        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }

    @Test
    public void test_getFeasibleInitialPoint_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                });
        Vector b = new DenseVector(new double[]{-0.8, -3.2, -2., 0});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);

        Matrix Aeq = new DenseMatrix(new double[][]{{3.6, 8.}});
        Vector beq = new DenseVector(-10.24);
        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 = -10.24
                Aeq, beq);

        Vector x = instance.getFeasibleInitialPoint(equal);
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(DoubleArrayMath.min(A.multiply(x0).minus(b).toArray()) >= 0);// Ax0 > b
        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }

    @Test
    public void test_getFeasibleInitialPoint_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
                });
        Vector b = new DenseVector(new double[]{-4, 0, -2, 0});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);

        Matrix Aeq = new DenseMatrix(new double[][]{{10, 8}});
        Vector beq = new DenseVector(-32.0);
        LinearEqualityConstraints equal = new LinearEqualityConstraints(// x1 + x2 = -32
                Aeq, beq);

        Vector x = instance.getFeasibleInitialPoint(equal);
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(DoubleArrayMath.min(A.multiply(x0).minus(b).toArray()) >= 0);// Ax0 > b
        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }

    @Test
    public void test_getFeasibleInitialPoint_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1., -2., 0., 0.},
                    {0., 0., 1., 2.}
                });
        Vector b = new DenseVector(new double[]{0.3, 0.03});
        LinearGreaterThanConstraints instance = new LinearGreaterThanConstraints(A, b);

        Vector x = instance.getFeasibleInitialPoint();
        Vector x0 = CreateVector.subVector(x, 1, x.size() - 1);

        assertTrue(DoubleArrayMath.min(A.multiply(x0).minus(b).toArray()) >= 0);// Ax0 > b
        assertTrue(ConstraintsUtils.isSatisfied(instance, x0));
        assertEquals(x.get(x.size()), 0, 0);
    }
}
