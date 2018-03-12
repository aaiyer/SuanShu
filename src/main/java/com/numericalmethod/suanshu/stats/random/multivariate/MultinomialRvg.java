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

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * A multinomial distribution puts <i>N</i> objects into <i>K</i> bins according to the bins' probabilities.
 * An output random vector counts the number of objects in each bin, making a total of <i>N</i>.
 * <p/>
 * The R equivalent function is {@code rmultinom} in package {@code normix}.
 *
 * @author Haksun Li
 */
public class MultinomialRvg implements RandomVectorGenerator {

    private final int size;
    private final double[] cumprob;
    private final RandomLongGenerator rng;

    /**
     * Construct a multinomial random vector generator.
     *
     * @param size an integer, say <i>N</i>, specifying the total number of objects that are put into <i>K</i> boxes in a typical multinomial experiment
     * @param prob a numeric non-negative vector of length <i>K</i>, specifying the probability for the <i>K</i> boxes
     * @param rng  a uniform random number generator
     */
    public MultinomialRvg(int size, double[] prob, RandomLongGenerator rng) {
        this.size = size;

        this.cumprob = new double[prob.length];
        this.cumprob[0] = prob[0];

        for (int i = 1; i < prob.length; ++i) {
            this.cumprob[i] = this.cumprob[i - 1] + prob[i];
        }
        for (int i = 0; i < prob.length - 1; ++i) {
            this.cumprob[i] /= this.cumprob[prob.length - 1];
        }
        this.cumprob[prob.length - 1] = 1;//avoid rounding error

        this.rng = rng;
    }

    /**
     * Construct a multinomial random vector generator.
     *
     * @param size an integer, say <i>N</i>, specifying the total number of objects that are put into <i>K</i> boxes in a typical multinomial experiment
     * @param prob a numeric non-negative vector of length <i>K</i>, specifying the probability for the <i>K</i> boxes
     */
    public MultinomialRvg(int size, double[] prob) {
        this(size, prob, new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        rng.seed(seeds);
    }

    @Override
    public double[] nextVector() {
        double[] bin = new double[cumprob.length];
        IID iid = new IID(rng, size);
        double[] sample = iid.nextVector();//sample size is 'size'

        for (int i = 0; i < sample.length; ++i) {
            double s = sample[i];

            for (int j = 0; j < cumprob.length; ++j) {
                if (s <= cumprob[j]) {//= to handle the rare case where s = 1 to avoid array out-of-bound exception
                    bin[j] += 1;
                    break;
                }
            }
        }

        return bin;
    }
}
