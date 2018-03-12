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
package com.numericalmethod.suanshu.stats.cointegration;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;

/**
 * Auxiliary regression 1:
 * <blockquote><i>
 * ΔY_t = μ + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </i></blockquote>
 * The OLS regression is performed on each dimension of the multivariate time series separately.
 *
 * @author Kevin Sun
 */
class AuxiliaryRegression1 extends AuxiliaryRegression {

    /**
     * Perform the OLS regression
     * <blockquote><i>
     * ΔY_t = μ + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
     * </i></blockquote>
     *
     * @param ts        a multivariate time series
     * @param p         the number of lags, e.g., 2
     * @param D         the exogenous factor matrix (excluding the intercept)
     * @param intercept indicate whether an intercept is included in the estimation
     */
    AuxiliaryRegression1(SimpleMultiVariateTimeSeries ts, int p, Matrix D, boolean addIntercept) {
        super(ts, p, D, addIntercept);
    }

    @Override
    Matrix getRegressand() {
        return new DenseMatrix(ts.diff(1).toMatrix());
    }
}
