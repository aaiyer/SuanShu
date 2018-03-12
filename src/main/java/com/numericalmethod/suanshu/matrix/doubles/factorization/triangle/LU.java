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
package com.numericalmethod.suanshu.matrix.doubles.factorization.triangle;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * LU decomposition decomposes an <i>n x n</i> matrix <i>A</i> so that <i>P * A = L * U</i>.
 * <i>P</i> is an <i>n x n</i> permutation matrix.
 * <i>L</i> is an <i>n x n</i> lower triangular matrix.
 * <i>U</i> is an <i>n x n</i> upper triangular matrix.
 * That is,
 * <blockquote><pre><code>
 * P.multiply(A) == L.multiply(U)
 * </code></pre></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/LU_decomposition">Wikipedia: LU decomposition</a>
 */
public class LU implements LUDecomposition {

    private final LUDecomposition impl;

    /**
     * Run the LU decomposition on a square matrix.
     *
     * @param A       a square matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public LU(Matrix A, double epsilon) {
        impl = new Doolittle(A, true, epsilon);
    }

    /**
     * Run the LU decomposition on a square matrix.
     *
     * @param A a matrix
     */
    public LU(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(A));
    }

    @Override
    public LowerTriangularMatrix L() {
        return impl.L();
    }

    @Override
    public UpperTriangularMatrix U() {
        return impl.U();
    }

    @Override
    public PermutationMatrix P() {
        return impl.P();
    }
}
