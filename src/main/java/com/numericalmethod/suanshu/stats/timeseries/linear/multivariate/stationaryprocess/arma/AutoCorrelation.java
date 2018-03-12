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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCorrelationFunction;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel;

/**
 * Compute the Auto-Correlation Function (ACF) for a vector AutoRegressive Moving Average (ARMA) model, assuming that
 * EX<sub>t</sub> = 0.
 *
 * <p>
 * This implementation solves the Yule-Walker equation.
 *
 * <p>
 * The R equivalent function are {@code ARMAacf} and {@code TacvfAR} in package {@code FitAR}.
 *
 * @author Haksun Li, Kevin Sun
 *
 * @see "P. J. Brockwell and R. A. Davis, "p. 420. Eq. 11.3.15. The Covariance Matrix Function of a Causal ARMA Process. Chapter 11.3. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class AutoCorrelation extends AutoCorrelationFunction {

    private final AutoCovariance cov;

    /** 
     * Compute the auto-correlation function of a vector ARMA model.
     * 
     * @param model an ARIMA specification
     * @param nLags the number of lags in the result
     */
    public AutoCorrelation(ARIMAModel model, int nLags) {
        cov = new AutoCovariance(model, nLags);
    }

    public Matrix evaluate(double lag) {
        Matrix Gamma = cov.evaluate(lag);
        Matrix Var = cov.evaluate(0);

        int nRows = Gamma.nRows();
        int nCols = Gamma.nCols();
        Matrix rho = new DenseMatrix(nRows, nCols);
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                rho.set(i, j, Gamma.get(i, j) / Math.sqrt(Var.get(i, i) * Var.get(j, j)));
            }
        }

        return rho;
    }

    @Override
    public Matrix evaluate(double x1, double x2) {
        return evaluate(Math.abs(x2 - x1));
    }
}
