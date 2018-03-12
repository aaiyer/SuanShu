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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class EigenDecompositionTest {

    @Test
    public void test_decomposition_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {5, 2},
                    {2, 5}
                });

        EigenDecomposition decomp = new EigenDecomposition(A);

        Matrix D = decomp.D();
        Matrix Q = decomp.Q();
        Matrix Qt = decomp.Qt();

        Matrix B = Q.multiply(D).multiply(Qt);
        assertTrue(AreMatrices.equal(A, B, 1e-14));
    }

    @Test
    public void test_decomposition_0020() {
        DenseMatrix A = new DenseMatrix(
                new SymmetricMatrix(new double[][]{
                    {1,},
                    {2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 10}
                }));

        EigenDecomposition decomp = new EigenDecomposition(A);

        Matrix D = decomp.D();
        Matrix Q = decomp.Q();
        Matrix Qt = decomp.Qt();

        Matrix B = Q.multiply(D).multiply(Qt);
        assertTrue(AreMatrices.equal(A, B, 1e-10));
    }

    /**
     * symmetric matrix, with an eigenvalue of multiple multiplicity
     */
    @Test
    public void test_decomposition_0030() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {3, 2, 4},
                    {2, 0, 2},
                    {4, 2, 3}
                });
        EigenDecomposition decomp = new EigenDecomposition(A);

        Matrix D = decomp.D();
        assertEquals(8, D.get(1, 1), 1e-14);
        assertEquals(-1, D.get(2, 2), 1e-14);
        assertEquals(-1, D.get(3, 3), 1e-14);

        Matrix Q = decomp.Q();
        Matrix Qt = decomp.Qt();

        Matrix B = Q.multiply(D).multiply(Qt);
        assertTrue(AreMatrices.equal(A, B, 1e-10));
    }

    /**
     * asymmetric matrix, with an eigenvalue of multiple multiplicity
     * 
     * This is a non-diagonalizable matrix.
     */
    @Test
    public void test_decomposition_0040() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1., 0.5},
                    {0., 1.}
                });

        EigenDecomposition decomp = new EigenDecomposition(A);

        Matrix D = decomp.D();
        assertTrue(AreMatrices.equal(A.ONE(), D, 1e-14));

        Matrix Q = decomp.Q();
        assertEquals(2, Q.nRows());
        assertEquals(2, Q.nCols());
    }

    @Test
    public void test_decomposition_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        EigenDecomposition decomp = new EigenDecomposition(A);

        Matrix D = decomp.D();
        Matrix Q = decomp.Q();
        Matrix Qt = decomp.Qt();

        Matrix B = Q.multiply(D).multiply(Qt);
        assertTrue(AreMatrices.equal(A, B, 1e-14));
    }
}
