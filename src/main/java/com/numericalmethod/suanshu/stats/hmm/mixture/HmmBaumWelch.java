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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.HMMDistribution;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;

/**
 * The Baum–Welch algorithm is used to find the unknown parameters of a hidden Markov model (HMM)
 * by making use of the forward-backward algorithm. It does the following.
 * <ul>
 * <li>the E step: Compute the conditional expectations given the observations and the current estimates of the parameters.
 * <li>the M step: Maximize, with respect to the estimated parameters and the data set, log-likelihood.
 * </ul>
 * These two steps are repeated until some convergence criterion has been satisfied.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>L. R. Rabiner, "A tutorial on hidden Markov models and selected applications in speech recognition," Proceedings of the IEEE, Volume: 77, Issue:2, 257 - 286, Feb 1989."
 * <li>W. Zucchini and I. L. MacDonald, "Hidden Markov Models for Time Series: An Introduction Using R," Boca Raton, Florida, CRC Press, 2009.
 * <li>L. R. Welch, "Hidden Markov models and the Baum–Welch algorithm," IEEE Information Theory Society Newsletter Volume 53, pp. 1, 10-13, Dec 2003.
 * <li><a href="http://en.wikipedia.org/wiki/Baum%E2%80%93Welch_algorithm">Wikipedia: Baum–Welch algorithm</a>
 * </ul>
 */
public class HmmBaumWelch extends HiddenMarkovModel {

    /**
     * the result of the Baum-Welch algorithm
     */
    public static class TrainedModel {

        /**
         * the newly trained model as a result of the Baum-Welch algorithm
         */
        public final HiddenMarkovModel model;
        /**
         * the log-likelihood
         */
        public final double logLikelihood;

        private TrainedModel(HiddenMarkovModel model, double logLikelihood) {
            this.model = model;
            this.logLikelihood = logLikelihood;
        }
    }

    /**
     * Construct a mixture HMM model by training an initial model using the Baum-Welch algorithm.
     *
     * @param observations  the observations
     * @param model0        an initial model
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public HmmBaumWelch(double[] observations, HiddenMarkovModel model0, double epsilon, int maxIterations) {
        super(getHMM(model0, observations, epsilon, maxIterations));
    }

    private static HiddenMarkovModel getHMM(HiddenMarkovModel model0, double[] observations, double epsilon, int maxIterations) {
        HiddenMarkovModel model = new HiddenMarkovModel(model0);
        double logLikelihood0 = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < maxIterations; ++i) {
            TrainedModel result = train(model, observations);
            model = result.model;

            double diff = result.logLikelihood - logLikelihood0;
            if (abs(diff) < epsilon) {
                break;
            }

            logLikelihood0 = result.logLikelihood;
        }

        return model;
    }

    /**
     * Construct a trained mixture hidden Markov model, one iteration.
     *
     * @param model0       the initial hidden Markov model
     * @param observations the observations
     * @return a trained mixture hidden Markov model
     */
    public static TrainedModel train(HiddenMarkovModel model0, double[] observations) {
        HiddenMarkovModel model1 = new HiddenMarkovModel(model0);
        int T = observations.length;// the length of observations
        int m = model0.nStates();// the number of hidden states

        // the E-step
        final Matrix A0 = model1.A(); //transition matrix
        final HmmForwardBackward fb = new HmmForwardBackward(model1);
        final Matrix log_alpha = fb.logForward(observations);
        final Matrix log_beta = fb.logBackward(observations);
        final Matrix log_ab = log_alpha.add(log_beta);

        double logLikelihood = fb.logLikelihood(observations);

        Matrix u = new DenseMatrix(T, m);
        for (int t = 1; t <= T; ++t) {
            for (int j = 1; j <= m; ++j) {
                u.set(t, j, exp(log_ab.get(t, j) - logLikelihood));
            }
        }// this is a log-transformed version of Eq. (4.13) of Zucchini and MacDonald (2009), p. 65

        Matrix[] v = new DenseMatrix[m];
        for (int k = 0; k < m; ++k) {
            v[k] = new DenseMatrix(T - 1, m);
            for (int t = 1; t < T; ++t) {
                for (int j = 1; j <= m; ++j) {
                    double log_A = log(A0.get(j, k + 1));
                    double log_p = log(model1.density(k + 1, observations[t]));
                    double log_v = log_A + log_alpha.get(t, j) + log_p + log_beta.get(t + 1, k + 1) - logLikelihood;
                    v[k].set(t, j, exp(log_v));
                }
            }
        }// this is a log-transformed version of Eq. (4.14) of Zucchini and MacDonald (2009), p. 65

        // the M-step: maximize (separately) the 3 terms in Eq. (4.12) of Zucchini and MacDonald (2009), p. 65
        // update the transition matrix
        DenseMatrix A1 = new DenseMatrix(m, m);
        for (int j = 1; j <= m; ++j) {
            Vector numerator = new DenseVector(m);
            double denominator = 0.;
            for (int k = 1; k <= m; ++k) {
                double sum = 0.;
                for (int q = 1; q < T; ++q) {
                    sum += v[k - 1].get(q, j);
                }

                numerator.set(k, sum);
                denominator += sum;
            }
            A1.setRow(j, numerator.scaled(1. / denominator));
        }// Zucchini and MacDonald (2009), p. 66, solution 2

        // update the initial probabilities
        Vector delta = u.getRow(1);// Zucchini and MacDonald (2009), p. 66, solution 1
//            delta = model1.getStationaryProbabilities(model1.A());

        // update the distribution-specific parameters; see also Section 8.2 of Zucchini and MacDonald (2009)
        final HMMDistribution dist = model1.getDistribution();
        Object[] lambda0 = dist.getParams();
        Object[] lambda1 = dist.getMStepParams(observations, u, lambda0);
        model1 = new HiddenMarkovModel(delta, A1, dist.newEMDistribution(lambda1));

        return new TrainedModel(model1, logLikelihood);
    }
}
