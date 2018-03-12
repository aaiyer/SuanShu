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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MatrixUtilsTest {

    //<editor-fold defaultstate="collapsed" desc="sums">
    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertArrayEquals(new int[]{3, 7}, rowSums(A));
        assertArrayEquals(new int[]{4, 6}, colSums(A));
    }

    @Test
    public void test_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {3, 4, 5},
                    {3, 4, 5}
                });

        assertArrayEquals(new int[]{6, 12, 12}, rowSums(A));
        assertArrayEquals(new int[]{7, 10, 13}, colSums(A));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="to Array">
    @Test
    public void test_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {3, 4, 5},
                    {3, 4, 5}
                });

        assertArrayEquals(new double[]{1, 2, 3, 3, 4, 5, 3, 4, 5}, to1DArray(A), 0);
    }

    @Test
    public void test_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {3, 4, 5},
                    {3, 4, 5}
                });

        assertArrayEquals(new double[]{1, 2, 3}, to2DArray(A)[0], 0);
        assertArrayEquals(new double[]{3, 4, 5}, to2DArray(A)[1], 0);
        assertArrayEquals(new double[]{3, 4, 5}, to2DArray(A)[2], 0);
    }

    @Test
    public void test_0050() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });

        assertArrayEquals(new double[]{
                    1, 0, 0, 0, 0,
                    2, 3, 0, 0, 0,
                    4, 5, 6, 0, 0,
                    7, 8, 9, 10, 0,
                    11, 12, 13, 14, 15}, to1DArray(A), 0);
    }

    @Test
    public void test_0060() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10},
                    {11, 12, 13, 14, 15}
                });

        assertArrayEquals(new double[]{1, 0, 0, 0, 0}, to2DArray(A)[0], 0);
        assertArrayEquals(new double[]{2, 3, 0, 0, 0}, to2DArray(A)[1], 0);
        assertArrayEquals(new double[]{4, 5, 6, 0, 0}, to2DArray(A)[2], 0);
        assertArrayEquals(new double[]{7, 8, 9, 10, 0}, to2DArray(A)[3], 0);
        assertArrayEquals(new double[]{11, 12, 13, 14, 15}, to2DArray(A)[4], 0);
    }
    //</editor-fold>
}
