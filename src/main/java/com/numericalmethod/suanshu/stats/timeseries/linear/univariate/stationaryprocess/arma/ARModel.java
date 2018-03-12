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

/**
 * This class represents an AR model.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_model">Wikipedia: Autoregressive model</a>
 */
public class ARModel extends ARMAModel {

    /**
     * Construct a univariate AR model.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1)
     * @param sigma the white noise variance
     */
    public ARModel(double mu, double[] AR, double sigma) {
        super(mu, AR, null, sigma);
    }

    /**
     * Construct a univariate AR model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1)
     */
    public ARModel(double mu, double[] AR) {
        this(mu, AR, 1);
    }

    /**
     * Construct a zero-intercept (mu) univariate AR model.
     *
     * @param AR the AR coefficients (excluding the initial 1)
     * @param sigma the white noise variance
     */
    public ARModel(double[] AR, double sigma) {
        this(0, AR, sigma);
    }

    /**
     * Construct a zero-intercept (mu) univariate AR model with unit variance.
     *
     * @param AR the AR coefficients (excluding the initial 1)
     */
    public ARModel(double[] AR) {
        this(AR, 1);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate AR model
     */
    public ARModel(ARModel that) {
        super(that);
    }
}
