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
package com.numericalmethod.suanshu.matrix.doubles.factorization.svd;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;

/**
 * SVD decomposition decomposes a matrix <i>A</i> of dimension <i>m x n</i>, where <i>m >= n</i>, such that
 * <i>U' * A * V = D</i>, or <i>U * D * V' = A</i>.
 * <ul>
 * <li><i>U</i> is orthogonal and has the dimension <i>m x n</i>.
 * <li><i>D</i> is diagonal and has the dimension <i>n x n</i>.
 * <li><i>V</i> is orthogonal and has the dimension <i>n x n</i>.
 * </ul>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Singular_value_decomposition">Wikipedia: Singular value decomposition</a>
 */
public interface SVDDecomposition {

    /**
     * Get the normalized, hence positive, singular values.
     * They may differ from the values in <i>D</i> if this computation turns off normalization.
     *
     * @return the singular values
     */
    public double[] getSingularValues();

    /**
     * Get the <i>D</i> matrix as in SVD decomposition.
     *
     * @return <i>D</i>
     */
    public DiagonalMatrix D();

    /**
     * Get the <i>U</i> matrix as in SVD decomposition.
     *
     * @return <i>U</i>
     */
    public Matrix U();

    /**
     * Get the transpose of i>U</i>, i.e., {@code U().t()}.
     *
     * @return {@code U().t()}
     */
    public Matrix Ut();

    /**
     * Get the <i>V</i> matrix as in SVD decomposition.
     *
     * @return <i>V</i>
     */
    public Matrix V();
}
