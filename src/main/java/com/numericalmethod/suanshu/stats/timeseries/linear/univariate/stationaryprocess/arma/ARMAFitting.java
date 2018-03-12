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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface represents a fitting method for estimating <i>φ, θ, μ and σ^2</i> in an ARMA model.
 *
 * <p>
 * Some common methods are maximum likelihood, and minimizing the conditional sum of squares.
 *
 * @author Haksun Li
 *
 * @see ConditionalSumOfSquares
 */
public interface ARMAFitting {

    /**
     * Get the ARMA coefficients, <i>φ</i>.
     *
     * @return φ and θ
     */
    public ARMAModel getFittedARMA();

    /**
     * Get the variance of the white noise.
     *
     * @return σ^2
     */
    public double var();

    /**
     * Get the asymptotic standard errors of the estimators.
     *
     * @return the asymptotic standard errors of the estimators
     */
    public Vector stderr();

    /**
     * Get the asymptotic covariance matrix of the estimators.
     *
     * @return the asymptotic covariance matrix of the estimators
     */
    public Matrix covariance();

    /**
     * Compute the AIC of model fitting.
     *
     * @return the AIC
     */
    public double AIC();

    /**
     * Compute the AICC of model fitting.
     *
     * @return the AICC
     */
    public double AICC();
}
