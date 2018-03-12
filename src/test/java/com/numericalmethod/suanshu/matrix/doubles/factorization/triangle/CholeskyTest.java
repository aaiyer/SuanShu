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
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CholeskyTest {

    @Test
    public void testCholesky_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {1, 2, 1},
                    {1, 1, 2}
                });
        Cholesky instance = new Cholesky(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();

        Matrix LLt = L.multiply(Lt);
        assertTrue(AreMatrices.equal(A1, LLt, 1e-15));
    }

    @Test
    public void testCholesky_0020() {
        LowerTriangularMatrix L1 = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}
                });
        Cholesky instance = new Cholesky(L1.multiply(L1.t()));
        LowerTriangularMatrix L = instance.L();
        assertEquals(L1, L);
    }

    @Test
    public void testCholesky_0030() {
        LowerTriangularMatrix L1 = new LowerTriangularMatrix(new double[][]{
                    {10.2},
                    {2.96, 3.36},
                    {456, 59.3, 602.369},
                    {456, 59.3, 602.369, 45.1}
                });
        Cholesky instance = new Cholesky(L1.multiply(L1.t()));
        LowerTriangularMatrix L = instance.L();
        assertTrue(AreMatrices.equal(L1, L, 1e-11));
    }

    @Test
    public void testCholesky_0040() {
        LowerTriangularMatrix L1 = new LowerTriangularMatrix(new double[][]{
                    {10.2},
                    {2.96, 3.36},
                    {456, 59.3, 602.369},
                    {456, 59.3, 602.369, 45.1},
                    {0, 0, 0, 0, 99}
                });
        Cholesky instance = new Cholesky(L1.multiply(L1.t()));
        LowerTriangularMatrix L = instance.L();
        assertTrue(AreMatrices.equal(L1, L, 1e-11));
    }

    @Test
    public void testCholesky_0050() {
        LowerTriangularMatrix L1 = new LowerTriangularMatrix(new double[][]{
                    {10.2},
                    {0, 3.36},
                    {0, 59.3, 602.369},
                    {0, 59.3, 602.369, 45.1},
                    {0, 0, 0, 0, 99}
                });
        Cholesky instance = new Cholesky(L1.multiply(L1.t()));
        LowerTriangularMatrix L = instance.L();
        assertTrue(AreMatrices.equal(L1, L, 1e-13));
    }

    @Test
    public void testCholesky_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {4, 2, -2},
                    {2, 10, 2},
                    {-2, 2, 5}
                });
        Cholesky instance = new Cholesky(A);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        assertEquals(A, L.multiply(Lt));
    }

    @Test(expected = RuntimeException.class)
    public void testCholesky_0110() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 5, 6},
                    {3, 6, 9}
                });
        Cholesky instance = new Cholesky(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();

        Matrix LLt = L.multiply(Lt);
        assertEquals(A1, LLt);
    }

    @Test(expected = RuntimeException.class)
    public void testCholesky_0130() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4.52},
                    {2, 5, 6, 9.35},
                    {3, 6, 7, 0.59},
                    {4.52, 9.35, 0.59, 0.59}
                });
        Cholesky instance = new Cholesky(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();

        Matrix LLt = L.multiply(Lt);
        assertEquals(A1, LLt);
    }

    @Test
    public void testCholesky_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {110.00000000074328, -3.061255122206001E-9, 0, -219.99999999999997},
                    {-3.061255122206001E-9, 6.000000012597035, -59.999999999999964, 0},
                    {0, -59.999999999999964, 600.0000000013468, -1.3468377878912817E-9},
                    {-219.99999999999997, 0, -1.3468377878912817E-9, 440.0000000013467}
                });
        Cholesky instance = new Cholesky(A);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        assertTrue(AreMatrices.equal(A, L.multiply(Lt), 1e-15));
    }
}
