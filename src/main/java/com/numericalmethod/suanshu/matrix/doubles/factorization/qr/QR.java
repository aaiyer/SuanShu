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
import com.numericalmethod.suanshu.misc.SuanShuUtils;

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
public class QR implements QRDecomposition {

    QRDecomposition impl;

    /**
     * Run the QR decomposition on a matrix.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public QR(Matrix A, double epsilon) {
        impl = new HouseholderReflection(A, epsilon);//change the implementation here if needed
    }

    /**
     * Run the QR decomposition on a matrix.
     *
     * @param A a matrix
     */
    public QR(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(A));
    }

    @Override
    public PermutationMatrix P() {
        return impl.P();
    }

    @Override
    public Matrix Q() {
        return impl.Q();
    }

    @Override
    public UpperTriangularMatrix R() {
        return impl.R();
    }

    @Override
    public int rank() {
        return impl.rank();
    }

    @Override
    public Matrix squareQ() {
        return impl.squareQ();
    }

    @Override
    public Matrix tallR() {
        return impl.tallR();
    }
}
