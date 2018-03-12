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
package com.numericalmethod.suanshu.stats.regression.linear.ols;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * Beta coefficient estimates, β^, of an Ordinary Least Square linear regression model.
 *
 * <p>
 * Statistics of the β^ coefficients are also included:
 * {@link #t t-value}
 *
 * @author Haksun Li
 */
public class Beta extends com.numericalmethod.suanshu.stats.regression.linear.Beta {

    /**
     * the z- or t-value for the regression coefficients β^
     */
    public final ImmutableVector t;//TODO: Pr(>|t|)

    /**
     * Construct an instance of <tt>Beta</tt>.
     * 
     * @param betaHat β^
     * @param residuals the residuals of this ols regression
     */
    Beta(Vector betaHat, Residuals residuals) {
        this(betaHat, covariance(residuals));//This is a trick to compute first the covariance and then the stderr.
    }

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param betaHat β^
     * @param covariance the covariance of the residuals of this ols regression
     */
    protected Beta(Vector betaHat, Matrix covariance) {//This is a trick to compute first the covariance and then the stderr.
        super(betaHat, covariance, stderr(covariance));

        /*
         * z = betaHat / stderr
         */
        t = new ImmutableVector(betaHat.divide(stderr));
    }

    /**
     * cov(β^) = (residuals.stderr)^2 * (A' %*% A)^-1
     *
     * @param residuals the residuals of this ols regression
     * @return cov(β^)
     */
    private static Matrix covariance(Residuals residuals) {
        return residuals.problem.invOfwAtwA().scaled(residuals.stderr * residuals.stderr);
    }

    /**
     * stderr(β^) = sqrt(cov.diagonal)
     *
     * @param residuals the residuals of this ols regression
     * @return stderr(β^)
     */
    private static Vector stderr(Matrix covariance) {
        return CreateVector.diagonal(covariance).pow(0.5);
    }
}
