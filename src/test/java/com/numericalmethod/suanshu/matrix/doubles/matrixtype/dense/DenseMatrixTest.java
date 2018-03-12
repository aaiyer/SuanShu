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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense;

import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils.*;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.stats.random.multivariate.IID;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class DenseMatrixTest {

    private long timer = 0;

    @Before
    public void setUp() {
        timer = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        timer = System.currentTimeMillis() - timer;
//        System.out.println(String.format("time elapsed: %d msec", timer));
    }

    //<editor-fold defaultstate="collapsed" desc="tests for constructors of DenseMatrix">
    /**
     * Test of constructing a jagged matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_jaggedMatrix() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4, 5}
                });
        assertEquals(2, instance.nRows());
    }

    /**
     * Creating a DenseMatrix from a 2D array.
     */
    @Test
    public void test_ctor_001() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                });
        assertEquals(2, instance.nRows());
        assertEquals(3, instance.nCols());
    }

    /**
     * Creating a DenseMatrix from a 1D array.
     */
    @Test
    public void test_ctor_002() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        Matrix instance = new DenseMatrix(R.seq(1.0, 9.0, 1.0), 3, 3);
        assertEquals(expected, instance);
    }

    /**
     * Creating a DenseMatrix from a 1D array.
     */
    @Test
    public void test_ctor_003() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {1}
                });

        Matrix instance = new DenseMatrix(R.seq(1.0, 1.0, 1.0), 1, 1);
        assertEquals(expected, instance);
    }

    /**
     * Creating a column DenseMatrix from a vector.
     */
    @Test
    public void test_ctor_004() {
        DenseVector v = new DenseVector(new double[]{1, 2, 3, 4, 5});
        Matrix expected = new DenseMatrix(v.toArray(), v.size(), 1);//column vector
        assertEquals(v.size(), expected.nRows());
        assertEquals(1, expected.nCols());
    }

    /**
     * Creating a row DenseMatrix from a vector.
     */
    @Test
    public void test_ctor_005() {
        DenseVector v = new DenseVector(new double[]{1, 2, 3, 4, 5});
        Matrix expected = new DenseMatrix(v.toArray(), 1, v.size());//row vector
        assertEquals(v.size(), expected.nCols());
        assertEquals(1, expected.nRows());
    }

    /**
     * Creating a row DenseMatrix from a vector.
     */
    @Test
    public void test_ctor_006() {
        DenseVector v = new DenseVector(new double[]{1, 2, 3, 4, 5});
        Matrix expected = new DenseMatrix(v);//column vector
        assertEquals(1, expected.nCols());
        assertEquals(v.size(), expected.nRows());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for row and column">
    @Test
    public void test_rowcol_0010() {
        DenseMatrix instance = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });
        assertEquals(2, instance.nRows());
        assertEquals(2, instance.nCols());
        assertEquals(new DenseVector(new double[]{1, 2}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{2}), instance.getRow(1, 2, 2));
        assertEquals(new DenseVector(new double[]{3, 4}), instance.getRow(2));
        assertEquals(new DenseVector(new double[]{3}), instance.getRow(2, 1, 1));
        assertEquals(new DenseVector(new double[]{1, 3}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{3}), instance.getColumn(1, 2, 2));
        assertEquals(new DenseVector(new double[]{2}), instance.getColumn(2, 1, 1));
    }

    @Test
    public void test_rowcol_0020() {
        DenseMatrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());
        assertEquals(new DenseVector(new double[]{1, 2, 3}), instance.getRow(1));
        assertEquals(new DenseVector(new double[]{2, 3}), instance.getRow(1, 2, 3));
        assertEquals(new DenseVector(new double[]{4, 5}), instance.getRow(2, 1, 2));
        assertEquals(new DenseVector(new double[]{1, 4, 7}), instance.getColumn(1));
        assertEquals(new DenseVector(new double[]{1, 4}), instance.getColumn(1, 1, 2));
        assertEquals(new DenseVector(new double[]{5, 8}), instance.getColumn(2, 2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRow_0010() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());
        assertEquals(new DenseVector(3), instance.getRow(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRow_0020() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());
        instance.getRow(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumn_0040() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());
        assertEquals(new DenseVector(3), instance.getColumn(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumn_0050() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());
        instance.getColumn(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumn_0060() {
        DenseMatrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        instance.getColumn(1, 2, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumn_0070() {
        DenseMatrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        instance.getRow(2, 3, 4);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for add, minus methods of class DenseMatrix">
    @Test
    public void test_add_001() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {6, 8},
                    {10, 12}});

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}}); //2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}}); //2x2

        Matrix instance = A1.add(A2);
        assertEquals(expected, instance);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_add_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix instance = A1.add(A2);
    }

    @Test
    public void test_minus_001() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {-4, -4},
                    {-4, -4}});

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}});//2x2

        Matrix instance = A1.minus(A2);
        assertEquals(expected, instance);
    }

    @Test(expected = MatrixMismatchException.class)
    public void test_minus_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix instance = A1.minus(A2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for multiply method of class DenseMatrix">
    @Test
    public void test_multiply_001() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {19, 22},
                    {43, 50}});

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});//2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}});//2x2

        Matrix instance = A1.multiply(A2);
        assertEquals(expected, instance);
    }

    @Test
    public void test_multiply_002() {
        Matrix expected = new DenseMatrix(new double[][]{
                    {21, 24, 27},
                    {47, 54, 61}});

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}}); //2x2

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6, 7},
                    {8, 9, 10}});//2x3

        Matrix instance = A1.multiply(A2);
        assertEquals(expected, instance);
    }

    @Test
    public void test_multiply_003() {
        Matrix expected = new DenseMatrix(new double[][]{{100}});
        Matrix A1 = new DenseMatrix(new double[][]{{10}});//1x1
        Matrix A2 = new DenseMatrix(new double[][]{{10},});//1x1

        Matrix instance = A1.multiply(A2);
        assertEquals(expected, instance);
    }

    /**
     * just to check speed
     *
     * last record (3/May/2012) by Haksun:
     * time taken (DenseMatrix multiplication): 53168 ms
     */
    @Test
    public void test_multiply_0040() {
        int matrixSize = 5000;
        IID iid = new IID(new UniformRng(), matrixSize * matrixSize);
        Matrix A1 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);
        Matrix A2 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);

        long start = System.currentTimeMillis();
        Matrix instance = A1.multiply(A2);
        System.out.println(String.format("time taken (DenseMatrix multiplication): %d ms", System.currentTimeMillis() - start));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for scaled methods of class DenseMatrix">
    @Test
    public void testScaled_001() {
        Matrix instance = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}});
        assertEquals(3, instance.nRows());
        assertEquals(3, instance.nCols());

        Matrix expected1 = instance.scaled(-1);
        Matrix expected2 = instance.opposite();
        assertEquals(expected1, expected2);
    }

    @Test
    public void testScaled_002() {
        Matrix instance1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}});
        assertEquals(3, instance1.nRows());
        assertEquals(3, instance1.nCols());

        Matrix instance2 = new DenseMatrix(new double[][]{
                    {1.5, 3., 4.5},
                    {6., 7.5, 9.},
                    {10.5, 12., 13.5}});
        Matrix expected = instance1.scaled(1.5);
        assertEquals(expected, instance2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set and get methods of class DenseMatrix">
    /**
     * Test of set and get methods, of class DenseMatrix.
     */
    @Test
    public void testSetAndGet_001() {
        int size = 500;
        Matrix instance = new DenseMatrix(size, size);

        int value = 0;
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                instance.set(i, j, value++ % 2 + 1);
            }
        }

        Matrix result = instance.multiply(instance);

        int expected1 = (1 + 2) * size / 2;
        int expected2 = (1 + 2) * size;

        assertEquals(expected1, result.get(1, 1), 0);
        assertEquals(expected2, result.get(1, 2), 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for transpose">
    /**
     * The input is a 2x3 matrix.
     */
    @Test
    public void test_t_001() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 1, 2},
                    {3, 4, 5}});
        Matrix instance = A1.t();

        assertEquals(3, instance.nRows());
        assertEquals(2, instance.nCols());
    }

    /**
     * The input is a 1x1 matrix.
     */
    @Test
    public void test_t_002() {
        Matrix A1 = new DenseMatrix(new double[][]{{100},});
        Matrix instance = A1.t();
        assertEquals(A1, instance);
    }

    /**
     * The input is a 1x3 matrix (row vector).
     */
    @Test
    public void test_t_003() {
        Matrix A1 = new DenseMatrix(new double[][]{{0, 1, 2},});
        Matrix instance = A1.t();

        assertEquals(3, instance.nRows());
        assertEquals(1, instance.nCols());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ZERO">
    /**
     * Test of ZERO method, of class DenseMatrix.
     */
    @Test
    public void testZERO_001() {
        Matrix instance = new DenseMatrix(2, 2).ZERO();

        assertEquals(0, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(0, instance.get(2, 2), 0);

        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});

        Matrix A2 = A1.multiply(instance);
        assertEquals(instance, A2);

        Matrix A3 = instance.multiply(A1);
        assertEquals(instance, A3);
    }

    /**
     * Test of ZERO method, of class DenseMatrix.
     * Check if a double array is initialized to 0 by jvm.
     */
    @Test
    public void testZERO_002() {
        int size = 1000;
        Matrix instance = new DenseMatrix(size, size).ZERO();

        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                assertEquals(0, instance.get(i, j), 0);
            }
        }
    }

    /**
     * Test of ZERO method, of class DenseMatrix.
     */
    @Test(expected = MatrixMismatchException.class)
    public void testZERO_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix A2 = (new DenseMatrix(3, 3)).ZERO().multiply(A1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for ONE">
    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_001() {
        Matrix instance = new DenseMatrix(2, 2).ONE();

        assertEquals(1, instance.get(1, 1), 0);
        assertEquals(0, instance.get(1, 2), 0);
        assertEquals(0, instance.get(2, 1), 0);
        assertEquals(1, instance.get(2, 2), 0);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test
    public void testONE_002() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new DenseMatrix(2, 2).ONE();

        Matrix instance1 = A1.multiply(I);
        assertEquals(A1, instance1);

        Matrix instance2 = I.multiply(A1);
        assertEquals(A1, instance2);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     * For non-square matrix with nRows > nCols
     */
    @Test
    public void testONE_003() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1},
                    {0, 0}
                });
        Matrix I = new DenseMatrix(3, 2).ONE();
        assertEquals(A1, I);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     * For non-square matrix with nCols > nRows
     */
    @Test
    public void testONE_004() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0}
                });
        Matrix I = new DenseMatrix(2, 3).ONE();
        assertEquals(A1, I);
    }

    /**
     * Test of ONE method, of class DenseMatrix.
     */
    @Test(expected = MatrixMismatchException.class)
    public void testONE_005() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}});
        Matrix I = new DenseMatrix(3, 3).ONE();

        Matrix instance1 = A1.multiply(I);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for toArray">
    /**
     * Test of to1DArray method, of class DenseMatrix.
     */
    @Test
    public void testToArray_001() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}});

        double[] instance = to1DArray(A1);
        double[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertTrue(equal(expected, instance, 0));
    }

    /**
     * Test of to1DArray method, of class DenseMatrix.
     */
    @Test
    public void testToArray_002() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}};
        DenseMatrix A1 = new DenseMatrix(data);

        double[][] instance = to2DArray(A1);
        assertTrue(equal(data, instance, 0));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for matrix * vector multiplication">
    @Test
    public void test_multiply_vector_0010() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3});
        DenseVector expected = new DenseVector(new double[]{14, 32, 50});

        assertEquals(expected, A1.multiply(v1));
    }

    @Test
    public void test_multiply_vector_0020() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 1.5},
                    {4, 5, 6, 6.3},
                    {7, 8, 9, 8.9}
                });
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3.5, 1.234});
        DenseVector expected = new DenseVector(new double[]{17.3510, 42.7742, 65.4826});

        assertEquals(expected, A1.multiply(v1));
    }

    @Test
    public void test_multiply_vector_0030() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                });
        DenseVector v1 = new DenseVector(new double[]{0, 0, 0, 0});
        DenseVector expected = new DenseVector(new double[]{0, 0, 0});

        assertEquals(expected, A1.multiply(v1));
    }

    @Test
    public void test_multiply_vector_0040() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {0}
                });
        DenseVector v1 = new DenseVector(new double[]{0});
        DenseVector expected = new DenseVector(new double[]{0});

        assertEquals(expected, A1.multiply(v1));
    }
    //</editor-fold>

    /**
     * This test makes sure the returned value of {@code toDense} is a new independent copy.
     */
    @Test
    public void test_toDense() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1}
                });

        DenseMatrix A2 = A1.toDense();
        A2.set(1, 1, -1);
        assertEquals(A1.ZERO(), A1.add(A2));
    }
}
