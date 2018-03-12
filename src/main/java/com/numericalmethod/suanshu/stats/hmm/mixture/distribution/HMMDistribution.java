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
package com.numericalmethod.suanshu.stats.hmm.mixture.distribution;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.HmmBaumWelch;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;

/**
 * This is the conditional distribution of the observations in each state
 * (possibly differently parameterized) of a mixture hidden Markov model.
 * An EM algorithm, such as {@linkplain HmmBaumWelch the Baum-Welch algorihtm} can fit the parameters.
 *
 * @author Haksun Li
 */
public interface HMMDistribution {

    /**
     * Get the parameters, for each state, of the distribution.
     *
     * @return the parameters, for each state, of the distribution
     */
    public Object[] getParams();

    /**
     * Get the distributions (possibly differently parameterized) for all states.
     *
     * @return the distributions
     */
    public ProbabilityDistribution[] getDistributions();

    /**
     * Get the random number generators corresponding to the distributions (possibly differently parameterized) for all states.
     *
     * @return the random number generators corresponding to the distributions
     */
    public RandomNumberGenerator[] getRandomNumberGenerators();

    /**
     * Maximize, for each state, the log-likelihood of the distribution with respect to the observations and current estimators.
     *
     * @param observations the observations
     * @param u            this is a log-transformed version of eq. (4.13) of Zucchini and MacDonald (2009), p. 65
     * @param param0       the current estimators of the distribution parameters
     * @return the MLE estimators
     */
    public Object[] getMStepParams(double[] observations, Matrix u, Object[] param0);

    /**
     * Construct a new distribution from a set of parameters, one set per state.
     *
     * @param lambda the parameters of the distribution, one set per state
     * @return a new HMM distribution
     */
    public HMMDistribution newEMDistribution(Object[] lambda);
}
