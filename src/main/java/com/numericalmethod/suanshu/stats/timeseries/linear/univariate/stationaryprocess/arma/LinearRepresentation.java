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

import static com.numericalmethod.suanshu.number.DoubleUtils.concat;
import java.util.Arrays;

/**
 * This class computes the linear representation of an Autoregressive Moving Average (ARMA) model.
 * A linear representation writes the time series as an (infinite) AR model.
 *
 * @author Haksun Li
 *
 * @see "P. J. Brockwell and R. A. Davis, "Eqs. 3.3.3, 3.3.4, Theorem 3.1.1., Chapter 11.3 Multivariate ARMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class LinearRepresentation {

    /**
     * the default number of lags
     */
    public static final int DEFAULT_NUMBER_OF_LAGS = 15;
    /**
     * {ψ<sub>i</sub>}
     *
     * <p>
     * the coefficients of the linear representation of the time series
     */
    private double[] psi;

    /**
     * Construct the linear representation of an ARMA model.
     * 
     * @param arma an ARMA model
     * @param nlags number of lags
     */
    public LinearRepresentation(ARMAModel arma, int nlags) {
        psi = new double[nlags];

        int p = arma.p();
        int q = arma.q();
        int pq1 = Math.max(p, q + 1);
        //we prepend ...[0] so that the code looks similiar to the equations
        double[] theta = concat(new double[]{1}, arma.MA());//theta[0] = 1
        double[] phi = concat(new double[]{1}, arma.AR());//not using phi[0]

        for (int j = 0; j < nlags; ++j) {
            if (j <= pq1) {//eq. 3.3.3
                psi[j] = j <= q ? theta[j] : 0;
                for (int k = 1; k <= j; ++k) {
                    psi[j] += k <= q ? phi[k] * psi[j - k] : 0;
                }
            } else {//eq. 3.3.4
                for (int k = 1; k <= p; ++k) {
                    psi[j] += k <= q ? phi[k] * psi[j - k] : 0;
                }
            }
        }
    }

    /**
     * Construct the linear representation of an ARMA model.
     *
     * @param arma an ARMA model
     */
    public LinearRepresentation(ARMAModel arma) {
        this(arma, DEFAULT_NUMBER_OF_LAGS);
    }

    /**
     * Get a copy of the linear representation coefficients.
     * 
     * @return {ψ<sub>i</sub>}
     */
    public double[] psi() {
        return Arrays.copyOf(psi, psi.length);
    }
}
