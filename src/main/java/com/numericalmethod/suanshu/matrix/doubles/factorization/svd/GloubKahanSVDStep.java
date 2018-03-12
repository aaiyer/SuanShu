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
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.CharacteristicPolynomial;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.GivensMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.BidiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.diagonal;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.superDiagonal;

/**
 * A Gloub-Kahan SVD step decomposes a <em>square</em>, <em>unreduced</em>, <em>upper</em> bidiagonal matrix, <i>B</i> such that
 * <i>D = U<sup>t</sup> * B * V</i>.
 * <i>D</i> is upper bi-diagonal;
 * <i>B</i> is upper bi-diagonal;
 * <i>U</i> is orthogonal;
 * <i>V</i> is orthogonal.
 * After several iterations of this step, <i>D[n-1,n]</i> converges to 0.
 *
 * @author Haksun Li
 */
class GloubKahanSVDStep {

    private final int dim;
    private final BidiagonalMatrix B;
    private final BidiagonalMatrix UtBV;
    private final GivensMatrix[] Us;
    private final GivensMatrix[] Vs;

    /**
     * Run a Gloub-Kahan SVD step.
     *
     * @param B a <em>square</em>, <em>unreduced</em>, <em>upper</em> bidiagonal matrix; i.e., it is necessary to make sure that
     * <blockquote><pre><code>
     * Is.upperBidiagonal(B, ε) == true;
     * Check.square(B) == true;
     * B.isUnreduced(ε) == true;
     * </code></pre></blockquote>
     */
    GloubKahanSVDStep(BidiagonalMatrix B) {
        this.B = B;
        this.dim = B.nRows();
        this.Us = new GivensMatrix[dim - 1];
        this.Vs = new GivensMatrix[dim - 1];
        UtBV = doGloubKahanSVDStep();
    }

    /**
     * TODO:
     * Demmel, James; Kahan, William (1990),
     * "Accurate singular values of bidiagonal matrices",
     * Society for Industrial and Applied Mathematics.
     * Journal on Scientific and Statistical Computing 11 (5): 873–912.
     *
     * @return <i>D = U<sup>t</sup> %*% B %*% V</i>
     * @see "Algorithm 8.6.1. Matrix Computations, 3rd edition. Golub G. H., van Loan C. F."
     */
    private BidiagonalMatrix doGloubKahanSVDStep() {
        Matrix T = B.t().multiply(B); // TODO: compute the trailing 2x2 matrix of T without computing the whole T
        double Tnn = T.get(dim, dim);
        CharacteristicPolynomial chi = new CharacteristicPolynomial(CreateMatrix.subMatrix(T, dim - 1, dim, dim - 1, dim));
        double eigen1 = chi.getEigenvalues().get(0).doubleValue();
        double eigen2 = chi.getEigenvalues().get(1).doubleValue();

        //mu is the eigenvalue closer to T[n,n]
        double mu = Math.abs(eigen1 - Tnn) < Math.abs(eigen2 - Tnn) ? eigen1 : eigen2;

        Matrix D = B.deepCopy();
        double y = T.get(1, 1) - mu;
        double z = T.get(1, 2);

        for (int k = 1; k <= dim - 1; ++k) {
            Vs[k - 1] = GivensMatrix.CtorToRotateColumns(dim, k, k + 1, y, z);
            D = Vs[k - 1].rightMultiply(D);//D = D %*% V

            y = D.get(k, k);
            z = D.get(k + 1, k);
            Us[k - 1] = GivensMatrix.CtorToRotateColumns(dim, k, k + 1, y, z);
            D = Us[k - 1].t().multiply(D);//D = U' %*% D

            if (k < dim - 1) {
                y = D.get(k, k + 1);
                z = D.get(k, k + 2);
            }
        }

        //for debugging; replace 0 with ε
//        assert Is.upperBidiagonal(D, 0) : "checkpoint failed: D must be upper bidiagonal";

        return (new BidiagonalMatrix(new double[][]{
                    superDiagonal(D).toArray(),
                    diagonal(D).toArray()
                }));
    }

    /**
     * Get <i>U<sup>t</sup>*B*V</i> matrix, which is bi-diagonal. The dimension is <i>dim x dim</i>.
     *
     * @return <i>U<sup>t</sup>*B*V</i>
     */
    BidiagonalMatrix UtBV() {
        return new BidiagonalMatrix(UtBV);
    }

    /**
     * Get the <i>U</i> matrix. The dimension is <i>dim x dim</i>.
     *
     * @return <i>U</i>
     */
    Matrix U() {
        Matrix result = GivensMatrix.product(Us);
        return result;
    }

    /**
     * Get the <i>V</i> matrix. The dimension of <i>V</i> is <i>dim x dim</i>.
     *
     * @return <i>V</i>
     */
    Matrix V() {
        Matrix result = GivensMatrix.product(Vs);
        return result;
    }
}
