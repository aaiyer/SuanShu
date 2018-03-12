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
package com.numericalmethod.suanshu.matrix.doubles;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.matrix.doubles.AreMatrices.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class AreTest {

    //<editor-fold defaultstate="collapsed" desc="tests for equal of vectors">
    @Test
    public void test_equal_vectors_0010() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4});
        DenseVector v2 = new DenseVector(new double[]{1, 2, 3, 4});

        assertArrayEquals(v1.toArray(), v2.toArray(), 0);
    }

    @Test
    public void test_equal_vectors_0020() {
        DenseVector v1 = new DenseVector(new double[]{Double.NaN, 2, 3, 4});
        DenseVector v2 = new DenseVector(new double[]{Double.NaN, 2, 3, 4});

        assertArrayEquals(v1.toArray(), v2.toArray(), 0);
    }

    @Test
    public void test_equal_vectors_0030() {
        DenseVector v1 = new DenseVector(new double[]{Double.NaN, 2, 3, Double.NaN});
        DenseVector v2 = new DenseVector(new double[]{Double.NaN, 2, 3, Double.NaN});

        assertArrayEquals(v1.toArray(), v2.toArray(), 0);
    }

    @Test
    public void test_equal_vectors_0040() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, Double.NaN});
        DenseVector v2 = new DenseVector(new double[]{Double.NaN, 2, 3, Double.NaN});

        assertFalse(equal(v1, v2, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for equal of matrices">
    @Test
    public void test_equal_matrices_0010() {
        Matrix m1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                });

        Matrix m2 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                });

        assertTrue(equal(m1, m2, 0));
    }

    @Test
    public void test_equal_matrices_0020() {
        Matrix m1 = new DenseMatrix(new double[][]{
                    {Double.NaN, 2, 3},
                    {4, 5, 6}
                });

        Matrix m2 = new DenseMatrix(new double[][]{
                    {Double.NaN, 2, 3},
                    {4, 5, 6}
                });

        assertTrue(equal(m1, m2, 0));
    }

    @Test
    public void test_equal_matrices_0030() {
        Matrix m1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, Double.NaN}
                });

        Matrix m2 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, Double.NaN}
                });

        assertTrue(equal(m1, m2, 0));
    }

    @Test
    public void test_equal_matrices_0040() {
        Matrix m1 = new DenseMatrix(new double[][]{
                    {Double.NaN, 2, 3},
                    {4, 5, 6}
                });

        Matrix m2 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, Double.NaN}
                });

        assertFalse(equal(m1, m2, 0));
    }
    //</editor-fold>
}
