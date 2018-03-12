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
package com.numericalmethod.suanshu.stats.regression.linear.logistic;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Beta coefficient estimates, β^, of a logistic regression model.
 *
 * <p>
 * Statistics of the β^ coefficients are also included:
 * {@link #z z-value}
 *
 * @author Haksun Li
 */
public class Beta extends com.numericalmethod.suanshu.stats.regression.linear.ols.Beta {

    /**
     * the z-value for the regression coefficients β^
     */
    public final ImmutableVector z;

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param betaHat β^
     * @param residuals the residuals of this logistic regression
     */
    Beta(Vector betaHat, Residuals residuals) {
        super(betaHat, covariance(residuals));
        this.z = super.t;
    }

    /**
     * Compute the covariance matrix for β^.
     *
     * @param residuals the residuals of this logistic regression
     * @return the covariance matrix for β^
     *
     * @see P. J. MacCullagh and J. A. Nelder. "Generalized Linear Models," 2nd ed. Section 4.4, p.116-119."
     */
    private static Matrix covariance(Residuals residuals) {
        final ImmutableMatrix A = residuals.problem.A;
        final ImmutableVector fitted = residuals.fitted;

        //W = diag(fitted(i)(1 - fitted(i)))
        DiagonalMatrix W = new DiagonalMatrix(fitted.multiply(fitted.opposite().add(1.)).toArray());
        //covariance matrix for β^ = (t(A) %*% W %*% A)^(-1)
        Matrix cov4BetaHat = new Inverse(new CongruentMatrix(A, W));

        return cov4BetaHat;
    }
}
