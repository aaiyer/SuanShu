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
package com.numericalmethod.suanshu.matrix.doubles.factorization.triangle;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DoolittleTest {

    @Test
    public void test_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0012() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0015() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {7, 8, 9},
                    {4, 5, 6}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0017() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {7, 8, 9},
                    {4, 5, 6}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}});
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0025() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 5},
                    {7, 4}});
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {5, 2, 3, 4},
                    {5, 6, 7, 4},
                    {1, 3, 2, 3},
                    {1, 7, 6, 2}});
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0035() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {5, 2, 3, 4},
                    {5, 6, 7, 4},
                    {1, 3, 2, 3},
                    {1, 7, 6, 2}});
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {10, 2, 3, 4, 5},
                    {6, 7, 88, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 79, 20},
                    {21, 22, 23, 24, 95}});
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0045() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {10, 2, 3, 4, 5},
                    {6, 7, 88, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 79, 20},
                    {21, 22, 23, 24, 95}});
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {5, 4, 4, 1, 5, 4, 2, 4, 1, 1},
                    {4, 5, 2, 2, 1, 2, 4, 5, 5, 2},
                    {5, 5, 3, 3, 5, 2, 3, 4, 1, 3},
                    {1, 2, 5, 5, 3, 1, 4, 3, 3, 3},
                    {2, 2, 4, 2, 3, 1, 3, 5, 4, 4},
                    {5, 4, 5, 1, 1, 3, 2, 3, 3, 4},
                    {3, 4, 4, 3, 4, 3, 2, 5, 5, 5},
                    {3, 4, 3, 3, 2, 1, 4, 2, 2, 1},
                    {4, 1, 1, 1, 1, 4, 4, 2, 2, 1},
                    {1, 5, 5, 5, 1, 1, 2, 4, 1, 4}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0055() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {5, 4, 4, 1, 5, 4, 2, 4, 1, 1},
                    {4, 5, 2, 2, 1, 2, 4, 5, 5, 2},
                    {5, 5, 3, 3, 5, 2, 3, 4, 1, 3},
                    {1, 2, 5, 5, 3, 1, 4, 3, 3, 3},
                    {2, 2, 4, 2, 3, 1, 3, 5, 4, 4},
                    {5, 4, 5, 1, 1, 3, 2, 3, 3, 4},
                    {3, 4, 4, 3, 4, 3, 2, 5, 5, 5},
                    {3, 4, 3, 3, 2, 1, 4, 2, 2, 1},
                    {4, 1, 1, 1, 1, 4, 4, 2, 2, 1},
                    {1, 5, 5, 5, 1, 1, 2, 4, 1, 4}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1, 4, 5, 1, 2, 3, 5, 1, 4},
                    {2, 5, 3, 4, 1, 5, 1, 2, 4, 3},
                    {4, 2, 2, 3, 3, 2, 1, 3, 3, 2},
                    {4, 1, 4, 5, 5, 1, 5, 1, 5, 5},
                    {2, 3, 5, 2, 4, 5, 5, 1, 5, 1},
                    {4, 2, 1, 4, 1, 3, 2, 2, 5, 3},
                    {2, 5, 1, 2, 3, 2, 3, 2, 1, 4},
                    {1, 1, 1, 5, 1, 3, 5, 3, 4, 5},
                    {4, 4, 2, 3, 5, 3, 5, 2, 5, 4},
                    {2, 2, 3, 3, 5, 1, 4, 4, 5, 4}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0065() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1, 4, 5, 1, 2, 3, 5, 1, 4},
                    {2, 5, 3, 4, 1, 5, 1, 2, 4, 3},
                    {4, 2, 2, 3, 3, 2, 1, 3, 3, 2},
                    {4, 1, 4, 5, 5, 1, 5, 1, 5, 5},
                    {2, 3, 5, 2, 4, 5, 5, 1, 5, 1},
                    {4, 2, 1, 4, 1, 3, 2, 2, 5, 3},
                    {2, 5, 1, 2, 3, 2, 3, 2, 1, 4},
                    {1, 1, 1, 5, 1, 3, 5, 3, 4, 5},
                    {4, 4, 2, 3, 5, 3, 5, 2, 5, 4},
                    {2, 2, 3, 3, 5, 1, 4, 4, 5, 4}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-14));
    }

    @Test
    public void test_0070() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0075() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0080() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {100}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0085() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {100}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0090() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {100}
                });
        Doolittle instance = new Doolittle(A1, true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0095() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {100}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * input already a lower triangular matrix, with pivoting
     */
    @Test
    public void testLU_0100() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        Doolittle instance = new Doolittle(A1.toDense(), true, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * input already a lower triangular matrix, with pivoting
     */
    @Test
    public void testLU_0110() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        Doolittle instance = new Doolittle(A1.toDense());
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * input already a lower triangular matrix, no pivoting
     */
    @Test
    public void testLU_0120() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 1},
                    {2, 1, 2}
                });

        Doolittle instance = new Doolittle(A1.toDense(), false, 1e-15);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * input already a lower triangular matrix, with pivoting
     */
    @Test
    public void testLU_0130() {
        LowerTriangularMatrix A1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 1},
                    {2, 1, 2}
                });

        Doolittle instance = new Doolittle(A1.toDense(), true, 1e-15);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * input already an upper triangular matrix
     */
    @Test
    public void testLU_0140() {
        UpperTriangularMatrix A1 = new UpperTriangularMatrix(new double[][]{
                    {3, 2, 1},
                    {2, 1},
                    {1}
                });

        Doolittle instance = new Doolittle(A1.toDense());
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * not every non-singular matrix can be factored as A = LU
     *
     * http://www.alkires.com/103/6_lu.pdfS
     */
    @Test(expected = MatrixSingularityException.class)
    public void testLU_0150() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 2},
                    {0, 1, -1}
                });

        Doolittle instance = new Doolittle(A1.toDense(), false, 1e-10);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * not every non-singular matrix can be factored as A = LU
     *
     * http://www.alkires.com/103/6_lu.pdfS
     */
    @Test
    public void testLU_0160() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 0, 2},
                    {0, 1, -1}
                });

        Doolittle instance = new Doolittle(A1.toDense(), true, 1e-10);//with pivoting
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * tiny matrix
     */
    @Test
    public void test_0170() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1e-100, 2e-100, 3e-100},
                    {7e-100, 8e-100, 9e-100},
                    {4e-100, 5e-100, 6e-100}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    /**
     * zero matrix
     */
    @Test
    public void test_0180() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        Doolittle instance = new Doolittle(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0310() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 16}
                });
        Doolittle instance = new Doolittle(A1, true, 1e-14);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-13));
    }

    @Test
    public void test_0315() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 16}
                });
        Doolittle instance = new Doolittle(A1, false, 0);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertEquals(A2, A3);
    }

    @Test
    public void test_0320() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        Doolittle instance = new Doolittle(A1, true, 1e-14);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix U = instance.U();
        PermutationMatrix P = instance.P();

        Matrix A2 = P.multiply(A1);
        Matrix A3 = L.multiply(U);

        assertTrue(AreMatrices.equal(A2, A3, 1e-13));
    }

    /**
     * need a bigger threshold to identify practical 0s to pass this test case
     */
    @Test
    public void test_0330() {
        int dim = 50;
        Matrix A1 = new DenseMatrix(dim, dim);
        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        Doolittle instance = new Doolittle(A1, true, 1e-8);
    }

    /**
     * need a bigger threshold to identify practical 0s to pass this test case
     */
    @Test
    public void test_0340() {
        int dim = 50;
        Matrix A1 = new DenseMatrix(dim, dim);
        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        Doolittle instance = new Doolittle(A1);
    }
}
