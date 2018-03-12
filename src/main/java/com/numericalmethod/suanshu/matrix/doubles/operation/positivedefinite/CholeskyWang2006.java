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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.Cholesky;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;

/**
 * Cholesky decomposition works only for a positive definite matrix.
 * If a matrix is not positive definite, Wang (2006) suggests to first do an eigen decomposition.
 * Second, force the non-negative diagonal elements in the diagonal matrix to to a small non-negative number, e.g., 0.
 * Third, re-construct a positive definite matrix from the new diagonal elements.
 * Finally, Cholesky decomposition can proceed as usual to the "modified" matrix.
 *
 * @author Haksun Li
 */
public class CholeskyWang2006 extends LowerTriangularMatrix {

    /**
     * Construct the Cholesky decomposition of a matrix.
     *
     * @param Sigma   a matrix, e.g., a covariance matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public CholeskyWang2006(Matrix Sigma, double epsilon) {
        super(chol(Sigma, epsilon));
    }

    private static LowerTriangularMatrix chol(Matrix Sigma, double epsilon) {
        DenseMatrix symmetricSigma = new PositiveDefiniteMatrixByPositiveDiagonal(Sigma, epsilon, epsilon);
        Cholesky cholesky = new Cholesky(symmetricSigma);
        return cholesky.L();
    }
}
