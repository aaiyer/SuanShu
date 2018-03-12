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
package com.numericalmethod.suanshu.vector.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CreateVectorTest {

    //<editor-fold defaultstate="collapsed" desc="tests for concat">
    @Test
    public void test_concat_0010() {
        Vector v1 = new DenseVector(new double[]{1, 2, 3});
        Vector v2 = new DenseVector(new double[]{4, 5, 6});
        Vector v3 = new DenseVector(new double[]{7, 8, 9, 10});

        Vector v4 = CreateVector.concat(v1, v2);
        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5, 6}), v4);

        Vector v5 = CreateVector.concat(v1, v2, v3);
        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}), v5);

        Vector v6 = CreateVector.concat(v1, v1);
        assertEquals(new DenseVector(new double[]{1, 2, 3, 1, 2, 3}), v6);
    }

    @Test
    public void test_concat_0020() {
        Vector v1 = new DenseVector(new double[]{1, 2, 3});
        Vector v2 = new DenseVector(new double[]{4, 5, 6});
        Vector v3 = null;

        Vector v4 = CreateVector.concat(v1, v2);
        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5, 6}), v4);

        Vector v5 = CreateVector.concat(v1, v2, v3);
        assertEquals(new DenseVector(new double[]{1, 2, 3, 4, 5, 6}), v5);

        Vector v6 = CreateVector.concat(v1, v3);
        assertEquals(new DenseVector(new double[]{1, 2, 3}), v6);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for foreach">
    @Test
    public void test_foreach_0010() {
        Vector v1 = new DenseVector(1., 2., 3., 4., 5., 6., 7.);
        Vector v2 = foreach(v1,
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                });

        assertArrayEquals(new double[]{1, 4, 9, 16, 25, 36, 49}, v2.toArray(), 1e-15);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for subVector">
    @Test
    public void testSubVector_001() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        Vector v2 = CreateVector.subVector(v1, 1, 10);
        assertEquals(v1, v2);
        Vector v3 = CreateVector.subVector(v1, 2, 8);
        assertEquals(new DenseVector(new double[]{2, 3, 4, 5, 6, 7, 8}), v3);
        Vector v4 = CreateVector.subVector(v1, 3, 7);
        assertEquals(new DenseVector(new double[]{3, 4, 5, 6, 7}), v4);
        Vector v5 = CreateVector.subVector(v1, 3, 10);
        assertEquals(new DenseVector(new double[]{3, 4, 5, 6, 7, 8, 9, 10}), v5);
        Vector v6 = CreateVector.subVector(v1, 1, 1);
        assertEquals(new DenseVector(new double[]{1}), v6);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for extracting diagonals">
    @Test
    public void test_diagonal_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Vector expected = new DenseVector(new double[]{
                    1, 7, 13, 19, 25
                });

        Vector v = diagonal(A1);
        assertEquals(expected, v);
    }

    @Test
    public void test_superDiagonal_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Vector expected = new DenseVector(new double[]{
                    2, 8, 14, 20
                });

        Vector v = superDiagonal(A1);
        assertEquals(expected, v);
    }

    @Test
    public void test_subDiagonal_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Vector expected = new DenseVector(new double[]{
                    6, 12, 18, 24
                });

        Vector v = subDiagonal(A1);
        assertEquals(expected, v);
    }
    //</editor-fold>
}
