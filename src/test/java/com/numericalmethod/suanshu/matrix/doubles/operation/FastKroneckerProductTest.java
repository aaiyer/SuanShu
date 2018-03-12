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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Ken Yiu
 */
public class FastKroneckerProductTest {

    /**
     * Two 2-by-2 matrices.
     * R code:
     * > a <- matrix(c(1, 2, 3, 4), nrow=2, ncol=2, byrow=TRUE)
     * > b <- matrix(c(5, 6, 7, 8), nrow=2, ncol=2, byrow=TRUE)
     * > kronecker(a,b)
     */
    @Test
    public void test_0010() {
        Matrix a = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });
        Matrix b = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}
                });
        Matrix expResult = new DenseMatrix(new double[][]{
                    {5, 6, 10, 12},
                    {7, 8, 14, 16},
                    {15, 18, 20, 24},
                    {21, 24, 28, 32}
                });

        Matrix result = new FastKroneckerProduct(a, b);

        assertTrue(AreMatrices.equal(expResult, result, 1e-14));
    }

    /**
     * 2-by-2 and 3-by-5.
     * R code:
     * > a <- matrix(c(-1, 2, 3, -4), nrow=2, ncol=2, byrow=TRUE)
     * > b <- matrix(c(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19), nrow=3, ncol=5, byrow=TRUE)
     * > kronecker(a,b)
     */
    @Test
    public void test_0020() {
        Matrix a = new DenseMatrix(new double[][]{
                    {-1, 2},
                    {3, -4}
                });
        Matrix b = new DenseMatrix(new double[][]{
                    {5, 6, 7, 8, 9},
                    {10, 11, 12, 13, 14},
                    {15, 16, 17, 18, 19}
                });
        Matrix expResult = new DenseMatrix(new double[][]{
                    {-5, -6, -7, -8, -9, 10, 12, 14, 16, 18},
                    {-10, -11, -12, -13, -14, 20, 22, 24, 26, 28},
                    {-15, -16, -17, -18, -19, 30, 32, 34, 36, 38},
                    {15, 18, 21, 24, 27, -20, -24, -28, -32, -36},
                    {30, 33, 36, 39, 42, -40, -44, -48, -52, -56},
                    {45, 48, 51, 54, 57, -60, -64, -68, -72, -76}
                });

        Matrix result = new FastKroneckerProduct(a, b);

        assertTrue(AreMatrices.equal(expResult, result, 1e-14));
    }

    /**
     * Same result as column-vector by row-vector.
     */
    @Test
    public void test_0030() {
        Matrix a = new DenseMatrix(new double[][]{
                    {1, 3, 5, 7, 9, 11, 13, 15, 17, 19}
                }).t();
        Matrix b = new DenseMatrix(new double[][]{
                    {2, 4, 6, 8, 10, 12, 14, 16, 18, 20}
                });
        Matrix expResult = a.multiply(b);

        Matrix result = new FastKroneckerProduct(a, b);

        assertTrue(AreMatrices.equal(expResult, result, 1e-14));
    }

    @Test(expected = MatrixAccessException.class)
    public void test_throwExceptionOnSet_0010() {
        Matrix a = new DenseMatrix(2, 2);
        Matrix b = new DenseMatrix(2, 2);

        Matrix result = new FastKroneckerProduct(a, b);

        result.set(1, 1, 0.);
    }
}
