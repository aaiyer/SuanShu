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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;

/**
 * Given an <i>m-by-n</i> matrix <i>A</i> and a <i>p-by-q</i> matrix <i>B</i>,
 * their Kronecker product <i>C</i>, also called their matrix direct product, is
 * an <i>(mp)-by-(nq)</i> matrix with entries defined by
 * <blockquote><i>
 * c<sub>st</sub> = a<sub>ij</sub> b<sub>kl</sub>
 * </i></blockquote>
 * where
 * <blockquote><pre><i>
 * s = p(i - 1) + k
 * t = q(j - 1) + l
 * </i></pre></blockquote>
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Kronecker_product">Wikipedia: Kronecker product</a>
 */
public class KroneckerProduct extends DenseMatrix {

    /**
     * Construct the Kronecker product of two matrices.
     *
     * @param A a matrix
     * @param B a matrix
     */
    public KroneckerProduct(Matrix A, Matrix B) {
        super(kronecker(A, B));
    }

    private static double[][] kronecker(Matrix A, Matrix B) {
        int m = A.nRows();
        int n = A.nCols();
        int p = B.nRows();
        int q = B.nCols();
        int nrows = m * p;
        int ncols = n * q;
        double[][] data = new double[nrows][ncols];

        for (int s = 0; s < nrows; ++s) {
            for (int t = 0; t < ncols; ++t) {
                data[s][t] = A.get(s / p + 1, t / q + 1) * B.get(s % p + 1, t % q + 1);
            }
        }

        return data;
    }
}
