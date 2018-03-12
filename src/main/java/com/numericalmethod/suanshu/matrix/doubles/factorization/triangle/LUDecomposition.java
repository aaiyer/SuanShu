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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;

/**
 * LU decomposition decomposes an <i>n x n</i> matrix <i>A</i> so that <i>P * A = L * U</i>.
 * <i>P</i> is an <i>n x n</i> permutation matrix.
 * <i>L</i> is an <i>n x n</i> lower triangular matrix.
 * <i>U</i> is an <i>n x n</i> upper triangular matrix.
 * That is,
 * <blockquote><pre><i>
 * P.multiply(A) == L.multiply(U)
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/LU_decomposition">Wikipedia: LU decomposition</a>
 */
public interface LUDecomposition {

    /**
     * Get the lower triangular matrix <i>L</i> as in the LU decomposition.
     *
     * @return <i>L</i>
     */
    public LowerTriangularMatrix L();

    /**
     * Get the upper triangular matrix <i>U</i> as in the LU decomposition.
     *
     * @return <i>U</i>
     */
    public UpperTriangularMatrix U();

    /**
     * Get the permutation matrix <i>P</i> as in <i>P * A = L * U</i>.
     *
     * @return <i>P</i>
     */
    public PermutationMatrix P();
}
