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
package com.numericalmethod.suanshu.stats.sampling.discrete;

import com.numericalmethod.suanshu.stats.distribution.ProbabilityMassFunction;

/**
 * This class samples from a discrete probability distribution.
 *
 * @author Haksun Li
 */
public class DiscreteSampling<X> {

    private final Iterable<X> collection;
    private final ProbabilityMassFunction<X> pmf;

    public DiscreteSampling(Iterable<X> collection, ProbabilityMassFunction<X> pmf) {
        this.collection = collection;
        this.pmf = pmf;
    }

    /**
     * Get a sample from the probability distribution.
     *
     * @param cm cumulative probability mass
     * @return a sample; {@code null} if none exists for {@code cm}
     */
    public X getSample(double cm) {
        double cumprob = 0;

        for (X x : collection) {
            double pm = pmf.evaluate(x);
            cumprob += pm;

            if (cumprob >= cm) {
                return x;
            }
        }

        return null;
    }
}
