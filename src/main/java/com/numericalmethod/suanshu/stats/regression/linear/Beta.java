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
package com.numericalmethod.suanshu.stats.regression.linear;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Beta coefficients are the outcomes of fitting a linear regression model.
 * β are the coefficients of a linear model.
 * Its estimation is β^.
 *
 * <p>
 * Statistics of the β^ coefficients are also included:
 * {@link #stderr standard error} and {@link #covariance covariance matrix}.
 *
 * @author Ken Yiu
 */
public abstract class Beta {

    /**
     * the coefficient estimates, β^
     */
    public final ImmutableVector betaHat;
    /**
     * the covariance matrix of the coefficient estimates, β^
     */
    public final ImmutableMatrix covariance;
    /**
     * the standard errors of the coefficients β^
     */
    public final ImmutableVector stderr;

    /**
     * Construct an instance of <tt>Beta</tt>.
     *
     * @param betaHat the coefficient estimates, β^
     * @param covariance the covariance matrix of the coefficient estimates, β^
     * @param stderr the standard errors of the coefficients β^
     */
    protected Beta(Vector betaHat, Matrix covariance, Vector stderr) {
        this.betaHat = new ImmutableVector(betaHat);
        this.covariance = new ImmutableMatrix(covariance);
        this.stderr = new ImmutableVector(stderr);
    }
}
