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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.stats.regression.linear.glm.Residuals;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This class represents the estimates of the beta in a quasi Generalized Linear Model,
 * i.e., a GLM with a quasi-family.
 *
 * @author Haksun Li
 */
public class Beta extends com.numericalmethod.suanshu.stats.regression.linear.glm.Beta {

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param fitting the fitting results of a quasi-GLM
     * @param residuals the residual analysis of a quasi-GLM
     */
    Beta(NewtonRaphson fitting, Residuals residuals) {
        this(fitting.betaHat(), covariance(fitting, residuals));
    }

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param betaHat β^
     * @param covariance the covariance matrix of β^
     */
    private Beta(Vector betaHat, Matrix covariance) {
        super(betaHat, covariance);
    }

    /**
     * Compute the covariance matrix for β^.
     * 
     * @param fitting the fitting results of a quasi-GLM
     * @param residuals the residual analysis of a quasi-GLM
     * @return the covariance matrix for β^
     */
    private static Matrix covariance(NewtonRaphson fitting, Residuals residuals) {
        Matrix covariance = new Inverse(fitting.DVInv().multiply(fitting.D()));
        covariance = covariance.scaled(residuals.overdispersion);
        return covariance;
    }
}
