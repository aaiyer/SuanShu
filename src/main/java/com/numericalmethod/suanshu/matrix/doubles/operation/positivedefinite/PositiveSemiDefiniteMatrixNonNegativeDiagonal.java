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

/**
 * This class "converts" a matrix into a symmetric, positive semi-definite matrix, if it is not already so, by
 * forcing the negative diagonal entries in the eigen decomposition to 0.
 *
 * @author Haksun Li
 * @see "Jin Wang, Chunlei Liu. "Generating Multivariate Mixture of Normal Distributions using a Modified Cholesky Decomposition," Simulation Conference, 2006. WSC 06. Proceedings of the Winter. p. 342 - 347. 3-6 Dec. 2006."
 */
public class PositiveSemiDefiniteMatrixNonNegativeDiagonal extends PositiveDefiniteMatrixByPositiveDiagonal {

    /**
     * Construct a positive semi-definite matrix
     * by forcing the negative diagonal entries in the eigen decomposition to 0.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0; used in the eigen decomposition (don't make it 0)
     */
    public PositiveSemiDefiniteMatrixNonNegativeDiagonal(Matrix A, double epsilon) {
        super(A, epsilon, 0);
    }
}
