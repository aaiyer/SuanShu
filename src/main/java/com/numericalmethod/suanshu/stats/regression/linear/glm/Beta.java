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
package com.numericalmethod.suanshu.stats.regression.linear.glm;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * This class represents the estimates of the beta in a Generalized Linear Model.
 *
 * <p>
 * Statistics of the β^ coefficients are also included:
 * {@link #z the z-values}.
 *
 * @author Haksun Li
 */
public class Beta extends com.numericalmethod.suanshu.stats.regression.linear.Beta {

    /**
     * the z-values for the GLM coefficients β^
     */
    public final ImmutableVector z;//TODO: Pr(>|z|)

    /**
     * Construct an instance of <tt>Beta</tt>.
     * 
     * @param fitting the fitting results of a GLM
     * @param residuals the residual analysis of a GLM
     */
    Beta(Fitting fitting, Residuals residuals) {
        this(fitting.betaHat(), covariance(fitting, residuals));//This is a trick to compute first covariance and then stderr.
    }

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param betaHat β^
     * @param covariance the covariance matrix of β^
     */
    protected Beta(Vector betaHat, Matrix covariance) {//This is a trick to compute first covariance and then stderr.
        super(betaHat, covariance, stderr(covariance));

        /*
         * z = betaHat / stderr
         */
        z = new ImmutableVector(betaHat.divide(stderr));
    }

    /**
     * Compute the covariance matrix for β^.
     *
     * First, we compute the matrix W = diag(fit_{i}(1 - fit(i))) where fit(i) is the fitted value.
     * Then, covariance matrix = (t(A) %*% W %*% A)^(-1).
     *
     * @param fitting the fitting results of a GLM
     * @param residuals the residual analysis of a GLM
     * @return the covariance matrix of β^
     *
     * @see p.116-119. Section 4.4. Generalized Linear Models. Second edition. P. J. MacCullagh and J. A. Nelder.
     */
    private static Matrix covariance(Fitting fitting, Residuals residuals) {
        final Matrix A = residuals.problem.A;
        Matrix W = new DiagonalMatrix(fitting.weights().toArray());
        Matrix covariance = new Inverse(new CongruentMatrix(A, W)).scaled(residuals.overdispersion);
        return covariance;
    }

    /**
     * stderr = sqrt(covariance * overdispersion)
     *
     * @param covariance the covariance matrix for β^
     * @return the standard error vector of β^
     */
    private static Vector stderr(Matrix covariance) {
        Vector stderr = CreateVector.diagonal(covariance).pow(0.5);//TODO: scale this by overdispersion?
        return stderr;
    }
}
