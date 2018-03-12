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
package com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LDL;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.autoEpsilon;

/**
 * Matthews and Davies propose the following way
 * to coerce a non-positive definite Hessian matrix to become symmetric, positive definite.
 * An LDL decomposition is performed on the non-positive definite matrix.
 * The zero and negative entries are then replaced by the smallest positive entry in the diagonal matrix.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou and Wu-Sheng Lu, "Algorithm 5.4," Practical Optimization: Algorithms and Engineering Applications."
 */
public class MatthewsDavies extends DenseMatrix {

    private final LowerTriangularMatrix L;
    private final UpperTriangularMatrix Lt;
    private final DiagonalMatrix D;
    private final DiagonalMatrix Dhat;

    /**
     * Construct a symmetric, positive definite matrix using the Matthews-Davies algorithm.
     * This implementation replaces all zero or negative entries on the diagonal by the smallest positive entry on the diagonal.
     * If all entries in the diagonal are negative, <i>D</i> becomes an identity matrix.
     *
     * @param H a non-positive definite matrix
     */
    public MatthewsDavies(Matrix H) {
        super(H.nCols(), H.nRows());

        assertArgument(DimensionCheck.isSquare(H), "H must be a square matrix");

        final double epsilon = autoEpsilon(H);//to avoid minD very "close" to 0 due to numerical rounding error

        LDL ldl = new LDL(H);
        this.L = ldl.L();
        this.Lt = ldl.Lt();
        this.D = ldl.D();

        this.Dhat = new DiagonalMatrix(this.D);

        //find the smallest positive entry in D
        double minD = 1;
        for (int i = 1; i <= this.Dhat.nRows(); ++i) {
            double value = this.Dhat.get(i, i);
            if (value > epsilon && value < minD) {
                minD = value;
            }
        }

        for (int i = 1; i <= this.Dhat.nRows(); ++i) {
            if (this.Dhat.get(i, i) <= epsilon) {
                this.Dhat.set(i, i, minD);
            }
        }

        Matrix Hhat = this.L.multiply(this.Dhat).multiply(this.Lt);

        //overwrite this
        for (int i = 1; i <= nRows(); ++i) {
            for (int j = 1; j <= nCols(); ++j) {
                super.set(i, j, Hhat.get(i, j));
            }
        }
    }

    /**
     * Get the lower triangular matrix <i>L</i> in the LDL decomposition.
     *
     * @return <i>L</i>
     */
    public LowerTriangularMatrix L() {
        return new LowerTriangularMatrix(L);
    }

    /**
     * Get the transpose of the lower triangular matrix <i>L</i> in the LDL decomposition.
     * The transpose is upper triangular.
     *
     * @return <i>L<sup>t</sup></i>
     */
    public UpperTriangularMatrix Lt() {
        return L.t();
    }

    /**
     * Get the diagonal matrix <i>D</i> in the LDL decomposition.
     *
     * @return <i>D</i>
     */
    public DiagonalMatrix D() {
        return new DiagonalMatrix(D);
    }

    /**
     * Get the modified diagonal matrix which is positive definite.
     *
     * @return <i>D^</i>
     */
    public DiagonalMatrix Dhat() {
        return new DiagonalMatrix(Dhat);
    }
}
