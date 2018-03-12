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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Compute the symmetric Kronecker product of two matrices.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Chapter 14.4.2, Symmetric Kronecker product," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SymmetricKronecker extends DenseMatrix {

    /**
     * Compute the symmetric Kronecker product of two matrices.
     *
     * @param M a matrix
     * @param N a matrix
     */
    public SymmetricKronecker(Matrix M, Matrix N) {
        super(dim(M, N), dim(M, N));
        final int m = this.nRows();

        for (int j = 1; j <= m; ++j) {
            Vector svecK = new DenseVector(m);
            svecK.set(j, 1.);

            Matrix K = new MAT(svecK);
            Matrix Q = N.multiply(K).multiply(M.t()).add(M.multiply(K).multiply(N.t()));
            _setColumn(j, new SVEC(Q.scaled(0.5)));
        }
    }

    private void _setColumn(int j, Vector v) {
        for (int i = 1; i <= nRows(); ++i) {
            super.set(i, j, v.get(i));
        }
    }

    private static int dim(Matrix M, Matrix N) {
        int n = M.nRows();
        int m = n * (n + 1) / 2;
        return m;
    }
}
