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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.log;

/**
 * The implementation of the forward-backward algorithm computes the log of forward and backward probabilities,
 * give a sequence of observations and the hmm model.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>L. R. Rabiner, "A tutorial on hidden Markov models and selected applications in speech recognition," Proceedings of the IEEE, Volume: 77, Issue:2, 257 - 286, Feb 1989."
 * <li>W. Zucchini and I. L. MacDonald, "Hidden Markov Models for Time Series: An Introduction Using R," Boca Raton, Florida, CRC Press, 2009.
 * </ul>
 */
public class HmmForwardBackward {

    private final HiddenMarkovModel model;
    /** the log-transformed forward probabilities */
    private ImmutableMatrix logForward = null;
    /** the log-transformed backward probabilities */
    private ImmutableMatrix logBackward = null;
    /** the log-likelihood */
    private double logLikelihood = Double.NaN;

    /**
     * Construct an instance of {@code HmmForwardBackward} to compute
     * the forward and backward probabilities and the log-likelihood.
     *
     * @param model a mixture hidden Markov model
     */
    public HmmForwardBackward(HiddenMarkovModel model) {
        this.model = model;
    }

    /**
     * Get the log-transformed forward probability matrix.
     *
     * @param observations a sequence of observations
     * @return the log-transformed forward probabilities matrix (number of observations * number of states)
     */
    public Matrix logForward(double[] observations) {
        if (logForward == null) {// TODO: proper synchronization
            final int T = observations.length;// length of observations
            final int m = model.nStates();// number of hidden states
            final Matrix A = model.A();// transition matrix

            DenseMatrix logAlpha = new DenseMatrix(T, m);// log-transformed forward probabilities (T * m) and the loglikelihood
            double logScale = 0.;// scaling factor (log-transformed)
            Matrix delta = new DenseMatrix(model.PI().toArray(), 1, m);// initial probabilities as a row matrix
            for (int t = 1; t <= T; ++t) {
                // This is a log + scaled version of Section 4.1 of Zucchini and MacDonald (2009) pp. 59-63.
                // Scaling is also discussed in Rabiner (1989).
                if (t > 1) {
                    delta = delta.multiply(A);
                }

                Matrix D = new DiagonalMatrix(m);
                for (int j = 1; j <= m; ++j) {
                    double d = model.density(j, observations[t - 1]);
                    D.set(j, j, d);
                }
                delta = delta.multiply(D);

                double sum = 0.;
                for (int j = 1; j <= m; ++j) {
                    sum += delta.get(1, j);// summing up first row
                }
                delta = delta.scaled(1. / sum);
                logScale += log(sum);

                Vector alpha = new DenseVector(m);
                for (int k = 1; k <= m; ++k) {
                    alpha.set(k, log(delta.get(1, k)) + logScale);// log(delta) + logScale
                }
                logAlpha.setRow(t, alpha);
            }

            logForward = new ImmutableMatrix(logAlpha);
            logLikelihood = logScale;
        }

        return logForward;
    }

    /**
     * Get the log-transformed backward probability matrix.
     *
     * @param observations a sequence of observations
     * @return the log-transformed backward probabilities matrix (number of observations * number of states)
     */
    public Matrix logBackward(double[] observations) {
        if (logBackward == null) {
            final int T = observations.length;// length of observations
            final int m = model.nStates();// number of hidden states
            final Matrix A = model.A();// transition matrix

            DenseMatrix logBeta = new DenseMatrix(T, m);// log-transformed backward probabilities (T * m)
            double logScale = log(m);// scaling factor (log-transformed)
            Matrix delta = new DenseMatrix(R.rep(1. / m, m), m, 1);// initial probabilities as a column matrix
            for (int i = T - 1; i >= 1; --i) {
                Matrix D = new DiagonalMatrix(m);
                for (int j = 1; j <= m; ++j) {
                    double d = model.density(j, observations[i]);
                    D.set(j, j, d);
                }
                delta = A.multiply(D).multiply(delta);

                Vector beta = new DenseVector(m);
                for (int t = 1; t <= m; ++t) {
                    beta.set(t, log(delta.get(t, 1)) + logScale);// log(delta) + log_scale
                }
                logBeta.setRow(i, beta);

                double sum = 0.;
                for (int k = 1; k <= m; ++k) {
                    sum += delta.get(k, 1);
                }
                delta = delta.scaled(1. / sum);
                logScale += log(sum);
            }

            logBackward = new ImmutableMatrix(logBeta);
        }

        return logBackward;
    }

    /**
     * Get the log-likelihood.
     *
     * @param observations a sequence of observations
     * @return the log-likelihood
     */
    public double logLikelihood(double[] observations) {
        // TODO: proper synchronization
        logForward(observations);// make sure the log-likelihood is already computed
        return logLikelihood;
    }
}
