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
package com.numericalmethod.suanshu.stats.random.univariate.normal;

import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;

/**
 * This is a random number generator that generates random deviates according to the Normal distribution.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Normal_distribution">Wikipedia: Normal distribution</a>
 */
public class NormalRng implements RandomNumberGenerator {

    private final double mean;//the mean
    private final double sigma;//the standard deviation
    private final RandomStandardNormalNumberGenerator rnorm;

    /**
     * Construct a random number generator to sample from the Normal distribution.
     *
     * @param mean  the mean
     * @param sigma the standard deviation
     * @param rnorm a standard random normal number generator
     */
    public NormalRng(double mean, double sigma, RandomStandardNormalNumberGenerator rnorm) {
        this.mean = mean;
        this.sigma = sigma;
        this.rnorm = rnorm;
    }

    /**
     * Construct a random number generator to sample from the Normal distribution.
     *
     * @param mean  the mean
     * @param sigma the standard deviation
     */
    public NormalRng(double mean, double sigma) {
        this(mean, sigma, new StandardNormalRng());
    }

    @Override
    public void seed(long... seeds) {
        rnorm.seed(seeds);
    }

    @Override
    public double nextDouble() {
        double z = rnorm.nextDouble();
        return mean + sigma * z;
    }
}
