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
package com.numericalmethod.suanshu.stats.sampling.resampling.bootstrap;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.sampling.resampling.Resampling;
import java.util.Arrays;

/**
 * This is the classical bootstrap method described in the reference.
 * It draws from the original sample with replacement.
 *
 * @author Haksun Li
 *
 * @see "B. Efron and R. Tibshirani. An Introduction to the Bootstrap. 1993. Chapman and Hall, New York, London."
 */
public class NonParametricBootstrap implements Resampling {

    private double[] sample;// make a copy to make sure the sample never changes
    private RandomLongGenerator uniform = new UniformRng();

    /**
     * Construct a bootstrap sample generator.
     * This is the classical bootstrap with replacement.
     *
     * @param sample the original sample.
     */
    public NonParametricBootstrap(double[] sample) {
        this.sample = Arrays.copyOfRange(sample, 0, sample.length);
    }

    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    public double[] getResample() {
        double[] resample = new double[sample.length];
        for (int i = 0; i < sample.length; ++i) {
            int j = (int) (uniform.nextLong() % sample.length);
            resample[i] = sample[j];
        }

        return resample;
    }
}
