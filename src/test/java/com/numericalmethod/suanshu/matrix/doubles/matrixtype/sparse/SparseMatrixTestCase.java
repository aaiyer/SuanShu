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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Testcases for different implementations of SparseMatrix.
 *
 * Subclasses of SparseMatrix can use these testcases by extending this abstract
 * class and implementing the 4 abstract methods.
 *
 * @author Ken Yiu
 */
@Ignore("This contains generic testcases for SparseMatrix")
public abstract class SparseMatrixTestCase<T extends SparseMatrix> {

    /**
     * Note: this is quite repeating itself, but this is the simplest way to
     * obtain the class of the type parameter.
     *
     * @return the implementation class
     */
    public abstract Class<T> getImplementationClass();

    public abstract T newInstance(int nRows, int nCols);

    public abstract T newInstance(int nRows, int nCols, int[] rowIndices, int[] columnIndices, double[] values);

    public abstract T newInstance(int nRows, int nCols, List<SparseEntry> elementList);

    @Test
    public void test_construction_0010() {
        SparseMatrix result = newInstance(5, 3);
        assertThat(result, is(getImplementationClass()));
        assertEquals(5, result.nRows());
        assertEquals(3, result.nCols());
        assertEquals(0, result.nNonZeros());
    }

    @Test
    public void test_construction_0020() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix result = newInstance(3, 4);
        result.set(1, 1, 1);
        result.set(1, 2, 2);
        result.set(2, 2, 3);
        result.set(3, 2, 1);
        result.set(2, 3, 9);
        result.set(3, 3, 4);

        assertThat(result, is(getImplementationClass()));
        assertEquals(6, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_construction_0030() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix result = newInstance(3, 4,
                                          new int[]{1, 1, 2, 3, 2, 3},
                                          new int[]{1, 2, 2, 2, 3, 3},
                                          new double[]{1, 2, 3, 1, 9, 4});

        assertThat(result, is(getImplementationClass()));
        assertEquals(6, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_construction_0040() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix result = newInstance(3, 4,
                                          new int[]{3, 2, 1, 3, 2, 1}, // not in order
                                          new int[]{3, 2, 1, 2, 3, 2},
                                          new double[]{4, 3, 1, 1, 9, 2});

        assertThat(result, is(getImplementationClass()));
        assertEquals(6, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_construction_0050() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix result = newInstance(3, 4,
                                          Arrays.<SparseEntry>asList(
                new SparseEntry(new Coordinates(3, 3), 4),
                new SparseEntry(new Coordinates(2, 2), 3),
                new SparseEntry(new Coordinates(1, 1), 1),
                new SparseEntry(new Coordinates(3, 2), 1),
                new SparseEntry(new Coordinates(2, 3), 9),
                new SparseEntry(new Coordinates(1, 2), 2)));

        assertThat(result, is(getImplementationClass()));
        assertEquals(6, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_0100() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3}, // missing one column index
                                      new double[]{1, 2, 3, 1, 9, 4});
    }

    @Test
    public void test_set_0010() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix result = newInstance(3, 4);
        result.set(1, 1, 1);
        result.set(1, 2, 2);
        result.set(2, 2, 3);
        result.set(3, 2, 1);
        result.set(2, 3, 9);
        result.set(3, 3, 4);

        assertEquals(6, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_set_0020() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 1, 0, 0}
                });
        SparseMatrix result = newInstance(3, 4);
        result.set(1, 1, 1);
        result.set(1, 2, 2);
        result.set(2, 2, 3);
        result.set(3, 2, 1);
        result.set(2, 3, 9);
        result.set(3, 3, 4);
        // set entries back to zeros
        result.set(1, 1, 0);
        result.set(1, 2, 0);
        result.set(2, 2, 0);
        result.set(3, 2, 1);
        result.set(2, 3, 0);
        result.set(3, 3, 0);

        assertEquals(1, result.nNonZeros());
        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test(expected = MatrixAccessException.class)
    public void test_set_0100() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        m1.set(0, 4, 1);
    }

    @Test
    public void test_get_0010() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        for (int i = 1; i <= expected.nRows(); ++i) {
            for (int j = 1; j <= expected.nCols(); ++j) {
                assertEquals(expected.get(i, j), m1.get(i, j), 1e-15);
            }
        }
    }

    @Test(expected = MatrixAccessException.class)
    public void test_get_0100() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        double result = m1.get(0, 4);
    }

    @Test
    public void test_row_0010() {
        double[][] expected = new double[][]{
            {1, 2, 0, 0},
            {0, 3, 9, 0},
            {0, 1, 4, 0}
        };
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        for (int i = 1; i <= m1.nRows(); ++i) {
            Vector result = m1.getRow(i);
            assertThat(result, is(SparseVector.class));
            assertArrayEquals(expected[i - 1], result.toArray(), 1e-15);
        }
    }

    @Test(expected = MatrixAccessException.class)
    public void test_row_0100() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        Vector result = m1.getRow(0);
    }

    @Test(expected = MatrixAccessException.class)
    public void test_row_0110() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        Vector result = m1.getRow(4);
    }

    @Test
    public void test_column_0010() {
        double[][] expected = new double[][]{
            {1, 0, 0},
            {2, 3, 1},
            {0, 9, 4},
            {0, 0, 0}
        };
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        for (int i = 1; i <= m1.nCols(); ++i) {
            Vector result = m1.getColumn(i);
            assertThat(result, is(SparseVector.class));
            assertArrayEquals(expected[i - 1], result.toArray(), 1e-15);
        }
    }

    @Test(expected = MatrixAccessException.class)
    public void test_column_0100() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        Vector result = m1.getColumn(0);
    }

    @Test(expected = MatrixAccessException.class)
    public void test_column_0110() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        Vector result = m1.getColumn(5);
    }

    @Test
    public void test_toElementList_0010() {
        int[] i = new int[]{1, 1, 2, 2, 3, 3};
        int[] j = new int[]{1, 2, 2, 3, 2, 3};
        double[] values = new double[]{1, 2, 3, 9, 1, 4};
        SparseMatrix m1 = newInstance(3, 4,
                                      i, j, values);

        List<SparseEntry> result = m1.getEntrytList();
        Collections.sort(result, SparseEntry.TopLeftFirstComparator.INSTANCE);

        assertEquals(6, result.size());
        for (int k = 0; k < values.length; ++k) {
            SparseEntry element = result.get(k);
            assertEquals(i[k], element.coordinates.i);
            assertEquals(j[k], element.coordinates.j);
            assertEquals(values[k], element.value, 1e-15);
        }
    }

    @Test
    public void test_add_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        /*
         * -1 2 0 1
         * 1 -1 0 0
         * 0 1 -4 0
         */
        Matrix m2 = newInstance(3, 4,
                                new int[]{1, 2, 1, 2, 3, 3, 1},
                                new int[]{1, 1, 2, 2, 2, 3, 4},
                                new double[]{-1, 1, 2, -1, 1, -4, 1});

        Matrix result = m1.add(m2);

        Matrix expected = new DenseMatrix(new double[][]{
                    {0, 4, 0, 1},
                    {1, 2, 9, 0},
                    {0, 2, 0, 0}
                });

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_add_0020() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = newInstance(3, 4); // zero matrix

        Matrix result = m1.add(m2);

        assertTrue(AreMatrices.equal(m1, result, 1e-15));
    }

    @Test
    public void test_add_0030() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        /*
         * -1 2 0 1
         * 1 -1 0 0
         * 0 1 -4 0
         */
        Matrix m2 = new DenseMatrix(new double[][]{
                    {-1, 2, 0, 1},
                    {1, -1, 0, 0},
                    {0, 1, -4, 0}
                });

        Matrix result = m1.add(m2);

        Matrix expected = new DenseMatrix(new double[][]{
                    {0, 4, 0, 1},
                    {1, 2, 9, 0},
                    {0, 2, 0, 0}
                });

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_add_0100() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = new DenseMatrix(4, 7).ONE();

        Matrix result = m1.add(m2);
    }

    @Test
    public void test_minus_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        /*
         * -1 2 0 1
         * 1 -1 0 0
         * 0 1 -4 0
         */
        Matrix m2 = newInstance(3, 4,
                                new int[]{1, 2, 1, 2, 3, 3, 1},
                                new int[]{1, 1, 2, 2, 2, 3, 4},
                                new double[]{-1, 1, 2, -1, 1, -4, 1});

        Matrix result = m1.minus(m2);

        Matrix expected = new DenseMatrix(new double[][]{
                    {2, 0, 0, -1},
                    {-1, 4, 9, 0},
                    {0, 0, 8, 0}
                });

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_minus_0020() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = newInstance(3, 4); // zero matrix

        Matrix result = m1.minus(m2);

        assertTrue(AreMatrices.equal(m1, result, 1e-15));
    }

    @Test
    public void test_minus_0030() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix result = m1.minus(m1);

        assertTrue(AreMatrices.equal(m1.ZERO(), result, 1e-15));
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_minus_0100() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = new DenseMatrix(4, 7).ONE();

        Matrix result = m1.minus(m2);
    }

    @Test
    public void test_multiply_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        /*
         * -1 2 0 1 0
         * 1 -1 0 0 0
         * 0 1 -4 0 2
         * 0 0 0 0 1
         */
        Matrix m2 = newInstance(4, 5,
                                new int[]{1, 2, 1, 2, 3, 3, 1, 3, 4},
                                new int[]{1, 1, 2, 2, 2, 3, 4, 5, 5},
                                new double[]{-1, 1, 2, -1, 1, -4, 1, 2, 1});

        Matrix result = m1.multiply(m2);

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 0, 0, 1, 0},
                    {3, 6, -36, 0, 18},
                    {1, 3, -16, 0, 8}
                });

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_multiply_0020() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = newInstance(4, 4).ONE(); // identity matrix

        Matrix result = m1.multiply(m2);

        assertTrue(AreMatrices.equal(m1, result, 1e-15));
    }

    @Test
    public void test_multiply_0030() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = newInstance(4, 7).ZERO(); // zero matrix

        Matrix result = m1.multiply(m2);

        Matrix expected = new DenseMatrix(3, 7).ZERO();

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_multiply_0100() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix m2 = new DenseMatrix(3, 4).ONE();

        Matrix result = m1.multiply(m2);
    }

    @Test
    public void test_multiply_Vector_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Vector v = new SparseVector(4,
                                    new int[]{2, 4},
                                    new double[]{2, 5});

        Vector result = m1.multiply(v);

        double[] expected = new double[]{
            4, 6, 2
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_multiply_Vector_0020() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Vector v = new SparseVector(4,
                                    new int[]{1, 2},
                                    new double[]{1, -1});

        Vector result = m1.multiply(v);

        double[] expected = new double[]{
            -1, -3, -1
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_multiply_Vector_0030() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Vector v = new SparseVector(4); // zero vector

        Vector result = m1.multiply(v);

        double[] expected = new double[]{
            0, 0, 0
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_multiply_Vector_0040() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Vector v = new DenseVector(new double[]{
                    2, 1, -1, 1
                });

        Vector result = m1.multiply(v);

        double[] expected = new double[]{
            4, -6, -3
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_multiply_Vector_0100() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Vector v = new SparseVector(6); // dimension mismatch

        Vector result = m1.multiply(v);
    }

    @Test
    public void test_scaled_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        double scalar = -3;

        Matrix result = m1.scaled(scalar);

        Matrix expected = new DenseMatrix(new double[][]{
                    {-3, -6, 0, 0},
                    {0, -9, -27, 0},
                    {0, -3, -12, 0},});

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_scaled_0020() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        double scalar = 0;

        Matrix result = m1.scaled(scalar);

        assertThat(result, is(getImplementationClass()));
        assertEquals(0, ((SparseMatrix) result).nNonZeros());
    }

    @Test
    public void test_opposite_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix result = m1.opposite();

        Matrix expected = new DenseMatrix(new double[][]{
                    {-1, -2, 0, 0},
                    {0, -3, -9, 0},
                    {0, -1, -4, 0},});

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_t_0010() {
        /*
         * 1 2 0 0
         * 0 3 9 0
         * 0 1 4 0
         */
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix result = m1.t();

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {2, 3, 1},
                    {0, 9, 4},
                    {0, 0, 0}
                });

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

//    @Test
//    public void test_dropTolerance_0010() {
//        SparseMatrix m1 = newInstance(3, 4,
//                new int[]{1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3},
//                new int[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4},
//                new double[]{
//                    1, 2, 0.0000013, 0.0000014,
//                    0.0000021, 3, 9, 0.0000024,
//                    0.0000031, 1, 4, 0.0000034});
//
//        double tolerance = 1e-5;
//        int result = m1.dropTolerance(tolerance);
//
//        Matrix expected = new DenseMatrix(new double[][]{
//                    {1, 2, 0, 0},
//                    {0, 3, 9, 0},
//                    {0, 1, 4, 0}
//                });
//
//        assertEquals(6, result);
//        assertEquals(6, m1.nnz());
//        assertTrue(AreMatrices.equal(expected, m1, 1e-15));
//    }
    @Test
    public void test_ZERO_0010() {
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 1, 2, 3, 2, 3},
                                new int[]{1, 2, 2, 2, 3, 3},
                                new double[]{1, 2, 3, 1, 9, 4});

        Matrix result = m1.ZERO();

        assertEquals(3, result.nRows());
        assertEquals(4, result.nCols());
        assertThat(result, is(getImplementationClass()));
        assertEquals(0, ((SparseMatrix) result).nNonZeros());
        assertTrue(AreMatrices.equal(m1, m1.add(result), 1e-15));
        assertTrue(AreMatrices.equal(m1, m1.minus(result), 1e-15));
        assertTrue(AreMatrices.equal(result, m1.add(m1.opposite()), 1e-15));
    }

    @Test
    public void test_toDense_0010() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 3, 9, 0},
                    {0, 1, 4, 0}
                });
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        DenseMatrix result = m1.toDense();

        assertTrue(AreMatrices.equal(expected, result, 1e-15));
    }

    @Test
    public void test_toString_0010() {
        Matrix m1 = newInstance(3, 4,
                                new int[]{1, 3},
                                new int[]{2, 4},
                                new double[]{3.4, 5.6});

        String result = m1.toString();
        assertEquals("3x4 nnz = 2\n(1, 2): 3.4\n(3, 4): 5.6\n", result);
    }

    @Test
    public void test_equals_0010() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});
        SparseMatrix m2 = newInstance(3, 4, // identical
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});

        assertTrue(m1.equals(m2));
    }

    @Test
    public void test_equals_0020() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});
        SparseMatrix m2 = newInstance(3, 4, // with elements shuffled
                                      new int[]{3, 2, 1, 1, 2, 3},
                                      new int[]{2, 3, 1, 2, 2, 3},
                                      new double[]{1, 9, 1, 2, 3, 4});

        assertTrue(m1.equals(m2));
    }

    @Test
    public void test_equals_0030() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});
        SparseMatrix m2 = newInstance(3, 4, // with 1 element less
                                      new int[]{1, 1, 2, 3, 2},
                                      new int[]{1, 2, 2, 2, 3},
                                      new double[]{1, 2, 3, 1, 9});

        assertFalse(m1.equals(m2));
    }

    @Test
    public void test_equals_0040() {
        SparseMatrix m1 = newInstance(3, 4,
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4});
        SparseMatrix m2 = newInstance(3, 4, // with 1 element different
                                      new int[]{1, 1, 2, 3, 2, 3},
                                      new int[]{1, 2, 2, 2, 3, 3},
                                      new double[]{1, 2, 3, 1, 9, 4.01});

        assertFalse(m1.equals(m2));
    }
}
