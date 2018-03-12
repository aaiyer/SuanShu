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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import static java.lang.Math.*;

/**
 * This class computes the inverse representation of an Autoregressive Moving Average (ARMA) model.
 *
 * @author Chun Yip Yau
 *
 * @see "P. J. Brockwell and R. A. Davis, "Eq. 11.3.13., Theorem 13.3.2., Chapter 11.3 Multivariate ARMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class Invertibility {//TODO: test cases

    /**
     * the default number of lags
     */
    public static final int DEFAULT_NLAGS = 15;
    /**
     * the coefficients of the linear representation of the time series
     */
    public final ImmutableMatrix[] PI;

    /**
     * Construct the inverse representation of an ARMA model.
     *    
     * @param model the ARMA model
     * @param nLags the number of lags in the series
     */
    public Invertibility(ARMAModel model, int nLags) {
        int p = model.p();
        int q = model.q();

        Matrix dim = model.AR(0);//track the dimension of the time series

        Matrix[] PI = new Matrix[nLags + 1];
        PI[0] = dim.ONE();

        for (int j = 1; j <= nLags; ++j) {
            PI[j] = j > q ? dim.ZERO() : model.AR(j).scaled(-1);//Θj = 0 for j > q
            for (int i = 1; i <= min(p, j); ++i) {//Φi = 0 for i > p
                PI[j] = PI[j].minus(model.MA(i).multiply(PI[j - i]));
            }
        }

        this.PI = new ImmutableMatrix[PI.length];
        for (int i = 0; i < PI.length; ++i) {
            this.PI[i] = new ImmutableMatrix(PI[i]);
        }
    }

    /**
     * Construct the inverse representation of an ARMA model up to the default number of lags {@link #DEFAULT_NLAGS}.
     *
     * @param model the ARMA model
     */
    public Invertibility(ARMAModel model) {
        this(model, DEFAULT_NLAGS);
    }
}
