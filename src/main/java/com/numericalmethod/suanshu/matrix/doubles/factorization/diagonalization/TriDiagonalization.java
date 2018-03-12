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
package com.numericalmethod.suanshu.matrix.doubles.factorization.diagonalization;

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.HessenbergDecomposition;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * A tri-diagonal matrix <i>A</i> is a matrix such that
 * it has non-zero elements only in the main diagonal, the first diagonal below, and the first diagonal above.
 * Successive Householder reflections on columns and then rows gradually transform a <em>symmetric</em> matrix
 * <i>A</i> to the tri-diagonal form.
 * The procedure is essentially the same as in Hessenberg decomposition, c.f., {@link HessenbergDecomposition},
 * except that here we apply the procedure only to symmetric matrix.
 * The trailing elements in rows are also zeroed out, due to symmetry.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Tridiagonal_matrix">Wikipedia: Tridiagonal matrix</a>
 * <li><a href="http://en.wikipedia.org/wiki/Householder_transformation#TriDiagonalization">Wikipedia: TriDiagonalization</a>
 * </ul>
 */
public class TriDiagonalization {

    private final HessenbergDecomposition decomp;

    /**
     * Run the tri-diagonalization process for a symmetric matrix.
     *
     * @param A a symmetric matrix
     * @throws IllegalArgumentException if <i>A</i> is not symmetric
     */
    public TriDiagonalization(Matrix A) {
        SuanShuUtils.assertArgument(IsMatrix.symmetric(A, 0), "Tridiagonalization applies to only symmetric matrix");

        this.decomp = new HessenbergDecomposition(A);
    }

    /**
     * Get <i>T</i>, such that <i>T = Q * A * Q</i>.
     * <i>T</i> is triangular.
     * <i>Q</i> is orthogonal.
     *
     * @return the <i>T</i> matrix
     */
    public Matrix T() {//TODO: return a TridiagonalMatrix
        return decomp.H();
    }

    /**
     * Get <i>Q</i>, such that <i>Q * A * Q = T</i>.
     * <i>Q</i> is orthogonal.
     * <i>T</i> is triangular.
     *
     * @return the <i>Q</i> matrix
     */
    public Matrix Q() {
        return decomp.Q();
    }
}
