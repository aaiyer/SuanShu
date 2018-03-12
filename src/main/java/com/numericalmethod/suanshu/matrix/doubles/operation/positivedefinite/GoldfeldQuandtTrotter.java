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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;

/**
 * Goldfeld, Quandt and Trotter propose the following way
 * to coerce a non-positive definite Hessian matrix to become symmetric, positive definite.
 * For a non-positive definite Hessian matrix <i>H</i>, we compute
 * \[
 * \widehat{H} = \frac{H + \beta I}{1 + \beta}
 * \]
 *
 * @author Haksun Li
 */
public class GoldfeldQuandtTrotter extends DenseMatrix {

    /**
     * Construct a symmetric, positive definite matrix using the Goldfeld-Quandt-Trotter algorithm.
     *
     * @param H    a non-positive definite matrix
     * @param beta a positive number.
     * The bigger beta is, the closer <i>H^</i> is to <i>I</i>.
     * If {@code beta == Double.POSITIVE_INFINITY}, then <i>H^ = I</i>.
     */
    public GoldfeldQuandtTrotter(Matrix H, double beta) {
        super(Hhat(H, beta));
    }

    private static Matrix Hhat(Matrix H, double beta) {
        assertArgument(beta > 0, "beta > 0");
        assertArgument(DimensionCheck.isSquare(H), "H must be a square matrix");

        Matrix Hhat = H.ONE();
        if (beta != Double.POSITIVE_INFINITY) {
            Hhat = H.add(H.ONE().scaled(beta)).
                    scaled(1 / (1 + beta));
        }

        return Hhat;
    }
}
