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
package com.numericalmethod.suanshu.matrix.doubles.factorization.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;

/**
 * QR decomposition of a matrix decomposes an <i>m x n</i> matrix <i>A</i> so that <i>A = Q * R</i>.
 * <ul>
 * <li><i>Q</i> is an <i>m x n</i> orthogonal matrix;
 * <li><i>R</i> is a <i>n x n</i> upper triangular matrix.
 * </ul>
 * Alternatively, we can have <i>A = sqQ * tallR</i>, where
 * <ul>
 * <li><i>sqQ</i> is a square <i>m x m</i> orthogonal matrix;
 * <li><i>tallR</i> is a <i>m x n</i> matrix.
 * </ul>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/QR_decomposition">Wikipedia: QR decomposition</a>
 */
public interface QRDecomposition {

    /**
     * Get <i>P</i>, the pivoting matrix in the QR decomposition.
     *
     * @return <i>P</i>
     */
    public PermutationMatrix P();

    /**
     * Get the orthogonal <i>Q</i> matrix in the QR decomposition, <i>A = QR</i>.
     * The dimension of <i>Q</i> is <i>m x n</i>, the same as <i>A</i>, the matrix to be orthogonalized.
     *
     * @return <i>Q</i>
     */
    public Matrix Q();

    /**
     * Get the upper triangular matrix <i>R</i> in the QR decomposition, <i>A = QR</i>.
     * The dimension of <i>R</i> is <i>n x n</i>, a square matrix.
     *
     * @return <i>R</i>
     */
    public UpperTriangularMatrix R();

    /**
     * Get the numerical rank of <i>A</i> as computed by the QR decomposition.
     * Numerical determination of rank requires a criterion to decide when a value should be treated as zero, hence a precision parameter.
     * This is a practical choice which depends on both the matrix and the application.
     * For instance, for a matrix with a big first eigenvector, we should accordingly decrease the precision to compute the rank.
     *
     * @return the rank of <i>A</i>
     */
    public int rank();

    /**
     * Get the square <i>Q</i> matrix. This is an arbitrary orthogonal completion of the <i>Q</i> matrix in the QR decomposition.
     * The dimension is <i>m x m</i> (square). We have <i>A = sqQ * tallR</i>.
     *
     * @return the square <i>Q</i> matrix
     */
    public Matrix squareQ();

    /**
     * Get the tall <i>R</i> matrix. This is completed by binding zero rows beneath the square upper triangular matrix <i>R</i> in the QR decomposition.
     * The dimension is <i>m x n</i>. It may not be square. We have <i>A = sqQ * tallR</i>.
     *
     * @return the tall <i>R</i> matrix
     */
    public Matrix tallR();
}
