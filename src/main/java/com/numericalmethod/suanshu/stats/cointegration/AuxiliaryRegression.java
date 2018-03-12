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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * Auxiliary regression:
 * <blockquote><i>
 * regressand = μ + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </i></blockquote>
 * The regressand can be, e.g., ΔY_t or Y_t.
 * The OLS regression is performed on each dimension of the multivariate time series separately.
 *
 * @author Kevin Sun
 */
abstract class AuxiliaryRegression {

    abstract Matrix getRegressand();
    final SimpleMultiVariateTimeSeries ts;
    private final ImmutableMatrix errors;

    /**
     * Perform the OLS regression
     * <blockquote><i>
     * regressand = μ + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
     * </i></blockquote>
     *
     * @param ts        a multivariate time series
     * @param p         the number of lags, e.g., 2
     * @param D         the exogenous factor matrix (excluding the intercept)
     * @param intercept indicate whether an intercept is included in the estimation
     */
    AuxiliaryRegression(SimpleMultiVariateTimeSeries ts, int p, Matrix D, boolean intercept) {
        this.ts = ts;

        final int dim = ts.dimension(); //the dimension of the multivariate time series)
        final int T = ts.size(); //the length of the multivariate time series)
        final int n = T - p; //the number of rows of the multivariate time series used in regression
        final int m = D != null ? D.nCols() : 0; //the number of exogeneous variables (excluding the intercept)
        /*
         * number of parameters to estimate for each dimension
         * Γ_i is a (dimension x dimension) matrix.
         * We have (p - 1) of Γ_i.
         * So, each dimension has dimension * (p - 1) parameters to estimate for the Γ_i's.
         */
        final int nparams = dim * (p - 1) + m + (intercept ? 1 : 0);
        final Matrix dyt = new DenseMatrix(ts.diff(1).toMatrix());

        //create the X matrix
        Matrix X = new DenseMatrix(n, nparams);
        for (int t = p + 1; t <= T; ++t) {
            int row = t - p; //row index
            int col = 0; //column index

            if (intercept) {
                X.set(row, ++col, 1);
            }

            if (p > 1) {
                for (int i = 1; i <= (p - 1); ++i) {
                    for (int j = 1; j <= dim; ++j) {
                        X.set(row, ++col, dyt.get(t - i - 1, j));
                    }
                }
            }

            if (D != null) {
                for (int k = 1; k <= m; ++k) {
                    X.set(row, ++col, D.get(t, k));
                }
            }
        }

        Matrix regressand = getRegressand();//e.g., y_t or dy_t

        //calculate the (transpose of) coefficient matrix
        DenseMatrix coeff_t = new DenseMatrix(nparams, dim);//the transpose of the coefficient matrix
        for (int i = 1; i <= dim; ++i) {
            Vector y = new DenseVector(Arrays.copyOfRange(regressand.getColumn(i).toArray(), p - 1, T - 1));
            LMProblem problem = new LMProblem(y, X, false);//we already take care of the intercept
            OLSRegression ols = new OLSRegression(problem);
            coeff_t.setColumn(i, ols.beta.betaHat);
        }

        //calculate the errors
        Matrix data = new DenseMatrix(CreateMatrix.subMatrix(regressand, p, (T - 1), 1, dim));
        Matrix err = data.minus(X.multiply(coeff_t));

        this.errors = new ImmutableMatrix(err);
    }

    /**
     * Get the errors of the OLS regression.
     *
     * @return the errors
     */
    ImmutableMatrix errors() {
        return errors;
    }
}
