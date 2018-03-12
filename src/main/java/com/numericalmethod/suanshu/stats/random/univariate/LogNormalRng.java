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
package com.numericalmethod.suanshu.stats.random.univariate;

import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;

/**
 * This random number generator samples from the log-normal distribution.
 * <p/>
 * The R equivalent class are {@code dlnorm, plnorm, qlnorm, rlnorm}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Log-normal_distribution">Wikipedia: Log-normal distribution</a>
 */
public class LogNormalRng implements RandomNumberGenerator {

    private final NormalRng rnorm;

    /**
     * Construct a random number generator to sample from the log-normal distribution.
     *
     * @param rnorm a random number generator that samples from the normal distribution
     */
    public LogNormalRng(NormalRng rnorm) {
        this.rnorm = rnorm;
    }

    /**
     * Construct a random number generator to sample from the log-normal distribution.
     *
     * @param logMean  the log of mean
     * @param logSigma the log of standard deviation
     */
    public LogNormalRng(double logMean, double logSigma) {//TODO: take variance instead?
        this(new NormalRng(logMean, logSigma));
    }

    @Override
    public void seed(long... seeds) {
        rnorm.seed(seeds);
    }

    @Override
    public double nextDouble() {
        return Math.exp(rnorm.nextDouble());
    }
}
