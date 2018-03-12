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
package com.numericalmethod.suanshu.stats.hmm.mixture;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.HMMDistribution;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is the mixture hidden Markov model (HMM). The observations are continuous and follow a continuous distribution.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>W. Zucchini and I. L. MacDonald, "Hidden Markov Models for Time Series: An Introduction Using R," Boca Raton, Florida, CRC Press, 2009.
 * <li><a href="http://en.wikipedia.org/wiki/Hidden_Markov_model">Wikipedia: Hidden Markov model</a>
 * </ul>
 */
public class HiddenMarkovModel extends com.numericalmethod.suanshu.stats.hmm.HiddenMarkovModel {

    private final HMMDistribution dist;

    /**
     * Construct a mixture hidden Markov model.
     *
     * @param PI   the initial state probabilities
     * @param A    the state transition probabilities of the homogeneous hidden Markov chain
     * @param dist the conditional distribution in the hidden Markov model
     */
    public HiddenMarkovModel(Vector PI, Matrix A, HMMDistribution dist) {
        super(PI, A, dist.getRandomNumberGenerators());
        this.dist = dist;
    }

    /**
     * Copy constructor.
     *
     * @param model a {@code HiddenMarkovModel}
     */
    public HiddenMarkovModel(HiddenMarkovModel model) {
        this(model.PI(), model.A(), model.getDistribution());
    }

    /**
     * Get the distribution in the hidden Markov model.
     *
     * @return the distribution in the hidden Markov model
     */
    public HMMDistribution getDistribution() {
        return dist;
    }

    /**
     * Get the probability density of making an observation in a particular state.
     *
     * @param observation the observation value
     * @param state       the hidden state label, counting from 1
     * @return the probability density
     */
    public double density(int state, double observation) {
        double density = dist.getDistributions()[state - 1].density(observation);
        return density;
    }
}
