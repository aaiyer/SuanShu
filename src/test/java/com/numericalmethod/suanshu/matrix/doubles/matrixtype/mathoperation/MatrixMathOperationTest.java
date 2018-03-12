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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Generic testcases for testing implementation of MatrixMathOperation.
 *
 * @author Ken Yiu
 */
@Ignore("This contains generic testcases for MatrixMathOperation")
public abstract class MatrixMathOperationTest {

    public abstract MatrixMathOperation newInstance();

    @Test
    public void test_add_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}}); //2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}}); //2x2

        Matrix expected = new DenseMatrix(new double[][]{
                    {6, 8},
                    {10, 12}});

        Matrix result = newInstance().add(A1, A2);
        assertEquals(expected, result);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_add_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix result = newInstance().add(A1, A2);
    }

    @Test
    public void test_minus_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}});//2x2

        Matrix expected = new DenseMatrix(new double[][]{
                    {-4, -4},
                    {-4, -4}});

        Matrix result = newInstance().minus(A1, A2);
        assertEquals(expected, result);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_minus_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix result = newInstance().minus(A1, A2);
    }

    @Test
    public void test_multiply_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}});//2x2

        Matrix expected = new DenseMatrix(new double[][]{
                    {19, 22},
                    {43, 50}});

        Matrix result = newInstance().multiply(A1, A2);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiply_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}}); //2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix expected = new DenseMatrix(new double[][]{
                    {21, 24, 27},
                    {47, 54, 61}});

        Matrix result = newInstance().multiply(A1, A2);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiply_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{{10}});//1x1
        Matrix A2 = new DenseMatrix(new double[][]{{10},});//1x1
        Matrix expected = new DenseMatrix(new double[][]{{100}});

        Matrix result = newInstance().multiply(A1, A2);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiplyVector_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Vector v1 = new DenseVector(new double[]{1, 2, 3});
        Vector expected = new DenseVector(new double[]{14, 32, 50});

        Vector result = newInstance().multiply(A1, v1);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiplyVector_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 1.5},
                    {4, 5, 6, 6.3},
                    {7, 8, 9, 8.9}
                });
        Vector v1 = new DenseVector(new double[]{1, 2, 3.5, 1.234});
        Vector expected = new DenseVector(new double[]{17.3510, 42.7742, 65.4826});

        Vector result = newInstance().multiply(A1, v1);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiplyVector_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        Vector v1 = new DenseVector(new double[]{0, 0, 0, 0});
        Vector expected = new DenseVector(new double[]{0, 0, 0});

        Vector result = newInstance().multiply(A1, v1);
        assertEquals(expected, result);
    }

    @Test
    public void test_multiplyVector_0040() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {0}
                });
        DenseVector v1 = new DenseVector(new double[]{0});
        DenseVector expected = new DenseVector(new double[]{0});

        Vector result = newInstance().multiply(A1, v1);
        assertEquals(expected, result);
    }

    @Test
    public void test_scaled_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}});

        Matrix expected = new DenseMatrix(new double[][]{
                    {-1, -2, -3},
                    {-4, -5, -6},
                    {-7, -8, -9}});

        Matrix result = newInstance().scaled(A1, -1);
        assertEquals(expected, result);
    }

    @Test
    public void test_scaled_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}});

        Matrix expected = new DenseMatrix(new double[][]{
                    {1.5, 3., 4.5},
                    {6., 7.5, 9.},
                    {10.5, 12., 13.5}});

        Matrix result = newInstance().scaled(A1, 1.5);
        assertEquals(result, expected);
    }

    @Test
    public void test_transpose_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 1, 2},
                    {3, 4, 5}});

        Matrix expected = new DenseMatrix(new double[][]{
                    {0, 3},
                    {1, 4},
                    {2, 5}
                });

        Matrix result = newInstance().transpose(A1);

        assertEquals(expected, result);
    }

    @Test
    public void test_transpose_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{{100},});
        Matrix result = newInstance().transpose(A1);
        assertEquals(A1, result);
    }

    @Test
    public void test_transpose_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{{0, 1, 2},});
        Matrix expected = new DenseMatrix(new double[][]{{0}, {1}, {2},});
        Matrix result = newInstance().transpose(A1);
        assertEquals(expected, result);
    }
}
