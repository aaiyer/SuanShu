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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LDLTest {

    @Test
    public void test_LDL_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {2, 1, 1},
                    {1, 2, 1},
                    {1, 1, 2}
                });
        LDL instance = new LDL(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        DiagonalMatrix D = instance.D();

        Matrix LDLt = L.multiply(D).multiply(Lt);
        assertEquals(A1, LDLt);
    }

    @Test
    public void test_LDL_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, -6},
                    {-6, 59 / 5}
                });
        LDL instance = new LDL(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        DiagonalMatrix D = instance.D();

        Matrix LDLt = L.multiply(D).multiply(Lt);
        assertEquals(A1, LDLt);
    }

    @Test
    public void test_LDL_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {110, 0, 0, -220},
                    {0, 6, -60, 0},
                    {0, -60, 600, 0},
                    {-220, 0, 0, 440}
                });
        LDL instance = new LDL(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        DiagonalMatrix D = instance.D();

        Matrix LDLt = L.multiply(D).multiply(Lt);
        assertEquals(A1, LDLt);
    }

    /**
     * identity
     */
    @Test
    public void test_LDL_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
                });
        LDL instance = new LDL(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        DiagonalMatrix D = instance.D();

        Matrix LDLt = L.multiply(D).multiply(Lt);
        assertEquals(A1, LDLt);
    }

    /**
     * zero
     */
    @Test
    public void test_LDL_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        LDL instance = new LDL(A1);
        LowerTriangularMatrix L = instance.L();
        UpperTriangularMatrix Lt = instance.Lt();
        DiagonalMatrix D = instance.D();

        Matrix LDLt = L.multiply(D).multiply(Lt);
        assertEquals(A1, LDLt);
    }
}
