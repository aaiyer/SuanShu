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
package com.numericalmethod.suanshu.stats.random.multivariate;

import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;

/**
 * An i.i.d. random vector has elements drawn from the same distribution.
 *
 * @author Haksun Li
 */
public class IID implements RandomVectorGenerator {

    private final RandomNumberGenerator rng;
    private final int length;

    /**
     * Construct a rvg that outputs vectors that have i.i.d. elements drawn from the same distribution.
     *
     * @param rng    the underlying (univariate) random number generator
     * @param length the length of the output vectors
     */
    public IID(RandomNumberGenerator rng, int length) {
        this.rng = rng;
        this.length = length;
    }

    @Override
    public void seed(long... seeds) {
        rng.seed(seeds);
    }

    @Override
    public double[] nextVector() {
        double[] result = new double[length];

        for (int i = 0; i < length; ++i) {
            result[i] = rng.nextDouble();
        }

        return result;
    }
}
