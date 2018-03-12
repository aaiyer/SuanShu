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

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CreateMatrixTest {

    //<editor-fold defaultstate="collapsed" desc="tests for cbind for vectors">
    @Test
    public void test_cbind_vectors_001() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5});
        DenseVector v2 = new DenseVector(new double[]{11, 12, 13, 14, 15});
        DenseVector v3 = new DenseVector(new double[]{21, 22, 23, 24, 25});
        DenseVector[] vectors = {v1, v2, v3};

        Matrix instance = CreateMatrix.cbind(vectors);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 11, 21},
                    {2, 12, 22},
                    {3, 13, 23},
                    {4, 14, 24},
                    {5, 15, 25}
                });
        assertEquals(expected, instance);
    }

    @Test
    public void test_cbind_vectors_002() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5});
        DenseVector v2 = new DenseVector(new double[]{11, 12, 13, 14, 15});
        DenseVector v3 = new DenseVector(new double[]{21, 22, 23, 24, 25});
        List<Vector> vectors = new ArrayList<Vector>();
        vectors.add(v1);
        vectors.add(v2);
        vectors.add(v3);

        Matrix instance = CreateMatrix.cbind(vectors);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 11, 21},
                    {2, 12, 22},
                    {3, 13, 23},
                    {4, 14, 24},
                    {5, 15, 25}
                });
        assertEquals(expected, instance);
    }

    @Test
    public void test_cbind_vectors_020() {
        DenseVector v1 = new DenseVector(new double[]{0});
        DenseVector[] vectors = {v1};
        Matrix instance = CreateMatrix.cbind(vectors);
        Matrix expected = new DenseMatrix(new double[][]{{0}});
        assertEquals(expected, instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_cbind_vectors_030() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5});
        DenseVector v2 = new DenseVector(new double[]{11});
        DenseVector v3 = new DenseVector(new double[]{21, 22, 23});
        DenseVector[] vectors = {v1, v2, v3};
        Matrix instance = CreateMatrix.cbind(vectors);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for rbind for vectors">
    @Test
    public void test_rbind_vectors_001() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5});
        DenseVector v2 = new DenseVector(new double[]{11, 12, 13, 14, 15});
        DenseVector v3 = new DenseVector(new double[]{21, 22, 23, 24, 25});
        DenseVector[] vectors = {v1, v2, v3};

        Matrix instance = CreateMatrix.rbind(vectors);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {11, 12, 13, 14, 15},
                    {21, 22, 23, 24, 25}
                });
        assertEquals(expected, instance);
    }

    @Test
    public void test_rbind_vectors_002() {
        DenseVector v1 = new DenseVector(new double[]{0});
        DenseVector[] vectors = {v1};
        Matrix instance = CreateMatrix.rbind(vectors);
        Matrix expected = new DenseMatrix(new double[][]{{0}});
        assertEquals(expected, instance);
    }
    //TODO: add more test cases like for cbind
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for replace">
    @Test
    public void test_replace_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 113, 114, 115},
                    {16, 17, 118, 119, 120},
                    {21, 22, 123, 124, 125}
                });

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);

        assertEquals(expected, A1);
    }

    @Test
    public void test_replace_0020() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {7, 8, 9, 10},
                    {13, 14, 15},
                    {19, 20},
                    {25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {0, 119, 120},
                    {0, 0, 125}});

        UpperTriangularMatrix expected = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {7, 8, 9, 10},
                    {113, 114, 115},
                    {119, 120},
                    {125}
                });

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);

        assertEquals(expected, A1);
    }

    @Test
    public void test_replace_0030() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1,},
                    {6, 7},
                    {11, 12, 13},
                    {16, 17, 18, 19},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 0, 0},
                    {118, 119, 0},
                    {123, 124, 125}});

        LowerTriangularMatrix expected = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {6, 7},
                    {11, 12, 113},
                    {16, 17, 118, 119},
                    {21, 22, 123, 124, 125}
                });

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);

        assertEquals(expected, A1);
    }

    @Test
    public void test_replace_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 113, 114, 115},
                    {16, 17, 118, 119, 120},
                    {21, 22, 123, 124, 125}
                });

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);

        assertEquals(expected, A1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_replace_0045() {
        PermutationMatrix A1 = new PermutationMatrix(5);

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_replace_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115, 116},
                    {118, 119, 120, 117},
                    {123, 124, 125, 118}
                });

        CreateMatrix.replace(A1, 3, 5, 3, 5, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_replace_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        CreateMatrix.replace(A1, 3, 50, 3, 5, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_replace_0070() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        CreateMatrix.replace(A1, 3, 5, 3, 50, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_replace_0080() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        CreateMatrix.replace(A1, 5, 3, 3, 5, A2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_replace_0090() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {113, 114, 115},
                    {118, 119, 120},
                    {123, 124, 125}});

        CreateMatrix.replace(A1, 3, 5, 5, 3, A2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for cbind for matrices">
    @Test
    public void test_cbind_matrices_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}
                });

        Matrix[] matrices = {A1, A2};

        Matrix instance = CreateMatrix.cbind(matrices);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 5, 6},
                    {3, 4, 7, 8}
                });

        assertEquals(expected, instance);
    }

    @Test
    public void test_cbind_matrices_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}
                });

        Matrix[] matrices = {A1, null, A2};

        Matrix instance = CreateMatrix.cbind(matrices);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 5, 6},
                    {3, 4, 7, 8}
                });

        assertEquals(expected, instance);
    }

    @Test
    public void test_cbind_matrices_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}
                });

        Matrix A3 = new DenseMatrix(new double[][]{
                    {9, 10},
                    {11, 12}
                });

        Matrix[] matrices = {null, A1, null, A2, A3};

        Matrix instance = CreateMatrix.cbind(matrices);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2, 5, 6, 9, 10},
                    {3, 4, 7, 8, 11, 12}
                });

        assertEquals(expected, instance);
    }

    /**
     * bug reported by Aleksandar Cvetkovic <coacoacoa@gmail.com>, 12/8/2011
     */
    @Test
    public void test_cbind_matrices_0040() {
        List<Matrix> mL = new ArrayList<Matrix>();
        mL.add((new DenseMatrix(2, 2)).ZERO());
        mL.add((new DenseMatrix(2, 2)).ONE());
        CreateMatrix.cbind(mL.toArray(new Matrix[0]));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for rbind for matrices">
    @Test
    public void test_rbind_matrices_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A2 = new DenseMatrix(new double[][]{
                    {5, 6},
                    {7, 8}
                });

        Matrix[] matrices = {A1, A2};

        Matrix instance = CreateMatrix.rbind(matrices);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4},
                    {5, 6},
                    {7, 8}
                });

        assertEquals(expected, instance);
    }

    @Test
    public void test_rbind_matrices_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        Matrix A2 = null;

        Matrix[] matrices = {A1, A2};

        Matrix instance = CreateMatrix.rbind(matrices);
        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        assertEquals(expected, instance);
    }

    @Test
    public void test_rbind_matrices_0030() {
        List<Matrix> mL = new ArrayList<Matrix>();
        mL.add((new DenseMatrix(2, 2)).ZERO());
        mL.add((new DenseMatrix(2, 2)).ONE());
        CreateMatrix.rbind(mL.toArray(new Matrix[0]));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for subMatrix">
    @Test
    public void test_subMatrix_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 3, 5},
                    {11, 13, 15},
                    {21, 23, 25}
                });

        Matrix A2 = CreateMatrix.subMatrix(A1,
                                           new int[]{1, 3, 5},
                                           new int[]{1, 3, 5});

        assertEquals(expected, A2);
    }

    @Test
    public void test_subMatrix_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {2, 4},
                    {12, 14},
                    {22, 24}
                });

        Matrix A2 = CreateMatrix.subMatrix(A1,
                                           new int[]{1, 3, 5},
                                           new int[]{2, 4});

        assertEquals(expected, A2);
    }

    @Test
    public void test_subMatrix_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {6, 7, 8, 9, 10},
                    {16, 17, 18, 19, 20}
                });

        Matrix A2 = CreateMatrix.rows(A1, new int[]{2, 4});
        assertEquals(expected, A2);
    }

    @Test
    public void test_subMatrix_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20}
                });

        Matrix A2 = CreateMatrix.rows(A1, 2, 4);
        assertEquals(expected, A2);
    }

    @Test
    public void test_subMatrix_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {2, 4},
                    {7, 9},
                    {12, 14},
                    {17, 19},
                    {22, 24}
                });

        Matrix A2 = CreateMatrix.columns(A1, new int[]{2, 4});
        assertEquals(expected, A2);
    }

    @Test
    public void test_subMatrix_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {2, 3, 4},
                    {7, 8, 9},
                    {12, 13, 14},
                    {17, 18, 19},
                    {22, 23, 24}
                });

        Matrix A2 = CreateMatrix.columns(A1, 2, 4);
        assertEquals(expected, A2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for diagonalMatrix">
    @Test
    public void test_diagonalMatrix_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        Matrix expected = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 7, 0, 0, 0},
                    {0, 0, 13, 0, 0},
                    {0, 0, 0, 19, 0},
                    {0, 0, 0, 0, 25}
                });

        Matrix A2 = CreateMatrix.diagonalMatrix(A1);
        assertTrue(AreMatrices.equal(expected, A2, 0));
    }
    //</editor-fold>
}
