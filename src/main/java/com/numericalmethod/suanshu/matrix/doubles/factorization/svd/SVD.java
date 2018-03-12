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

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

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
public class SVD implements SVDDecomposition {

    private final Matrix A;
    private final boolean fat;
    private final boolean doUV;
    private final SVDDecomposition impl;
    public final double epsilon;

    /**
     * Run the SVD decomposition on a matrix.
     *
     * @param A       a matrix
     * @param doUV    {@code false} if to compute only the singular values but not <i>U</i> and <i>V</i>
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public SVD(Matrix A, boolean doUV, double epsilon) {
        this.A = A;
        this.fat = DimensionCheck.isFat(A);
        this.doUV = doUV;
        this.impl = new GloubKahanSVD(fat ? this.A.t() : this.A, this.doUV, true, epsilon);//change the implementaiton here, if needed
        this.epsilon = epsilon;
    }

    /**
     * Run the SVD decomposition on a matrix.
     *
     * @param A    a matrix
     * @param doUV {@code false} if to compute only the singular values but not <i>U</i> and <i>V</i>
     */
    public SVD(Matrix A, boolean doUV) {
        this(A, doUV, SuanShuUtils.autoEpsilon(A));
    }

    @Override
    public double[] getSingularValues() {
        return impl.getSingularValues();
    }

    @Override
    public DiagonalMatrix D() {
        return impl.D();
    }

    @Override
    public Matrix U() {
        Matrix U = fat ? impl.V() : impl.U();
        return U;
    }

    @Override
    public Matrix Ut() {
        return U().t();
    }

    @Override
    public Matrix V() {
        Matrix V = fat ? impl.U() : impl.V();
        return V;
    }
}
