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
 * This class represents an MA model.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Moving_average_model">Wikipedia: Moving average model</a>
 */
public class MAModel extends ARMAModel {

    /**
     * Construct a univariate MA model.
     *
     * @param mu the intercept (constant) term
     * @param MA the MA coefficients (excluding the initial 1)
     * @param sigma the white noise variance
     */
    public MAModel(double mu, double[] MA, double sigma) {
        super(mu, null, MA, sigma);
    }

    /**
     * Construct a univariate MA model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param MA the MA coefficients (excluding the initial 1)
     */
    public MAModel(double mu, double[] MA) {
        super(mu, null, MA);
    }

    /**
     * Construct a zero-mean univariate MA model.
     *
     * @param MA the MA coefficients (excluding the initial 1)
     * @param sigma the white noise variance
     */
    public MAModel(double[] MA, double sigma) {
        this(0, MA, sigma);
    }

    /**
     * Construct a zero-mean univariate MA model with unit variance.
     *
     * @param MA the MA coefficients (excluding the initial 1)
     */
    public MAModel(double[] MA) {
        this(MA, 1);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate MA model
     */
    public MAModel(MAModel that) {
        super(that);
    }
}
