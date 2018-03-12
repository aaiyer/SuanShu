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
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.log;

/**
 * The Viterbi algorithm is a dynamic programming algorithm for finding the most likely sequence of hidden states
 * – called the Viterbi path –
 * that results in a sequence of observed events,
 * especially in the context of Markov information sources and hidden Markov models.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>Rabiner, L.R. "A tutorial on hidden Markov models and selected applications in speech recognition," Proceedings of the IEEE. Volume 77, Issue 2, 257 - 286. Feb 1989.
 * <li>W. Zucchini and I. L. MacDonald, "Hidden Markov Models for Time Series: An Introduction Using R," Boca Raton, Florida, CRC Press, 2009.
 * <li><a href="http://en.wikipedia.org/wiki/Viterbi_algorithm">Wikipedia: Viterbi algorithm</a>
 * </ul>
 */
public class HmmViterbi {

    /** the underlying hidden Markov model */
    private final HiddenMarkovModel model;

    /**
     * Construct an Viterbi algorithm for an HMM.
     *
     * @param model the underlying hidden Markov model
     */
    public HmmViterbi(HiddenMarkovModel model) {
        this.model = model;
    }

    /**
     * Generate the most likely sequence of states by using Viterbi algorithm (global decoding),
     * given the observations and the parameters of the underlying hidden Markov model.
     *
     * @param observations the observations
     * @return the most likely sequence of states
     */
    public int[] getViterbiStates(double[] observations) {
        final int T = observations.length;// the length of observations
        final int m = model.nStates();// the number of hidden states
        final Vector delta = model.PI();// the initial state probabilities
        final Matrix A = model.A();// the transition matrix

        Matrix logA = new DenseMatrix(m, m);// log(transition matrix)
        for (int i = 1; i <= m; ++i) {// get the log-transformed transition matrix
            for (int j = 1; j <= m; ++j) {
                logA.set(i, j, log(A.get(i, j)));
            }
        }

        Matrix xi = new DenseMatrix(T, m);// the log-transformed xi matrix; pp. 83-84 of Zucchini and MacDonald (2009)
        for (int i = 1; i <= m; ++i) {// xi_{1,i} = delta_t * p_i(x1), p.83 of Zucchini and MacDonald (2009)
            xi.set(1, i, log(delta.get(i)) + log(model.density(i, observations[0])));
        }
        for (int t = 2; t <= T; ++t) {// equation (5.9), p.83 of Zucchini and MacDonald (2009)
            DenseMatrix xi_matrix = new DenseMatrix(m, m);
            for (int i = 1; i <= m; ++i) {// this is a log-transformed version of Eq. (5.9)
                xi_matrix.setColumn(i, xi.getRow(t - 1));
                for (int j = 1; j <= m; ++j) {
                    xi_matrix.set(i, j, xi_matrix.get(i, j) + logA.get(i, j));
                }
            }
            for (int k = 1; k <= m; ++k) {
                double max = DoubleArrayMath.max(xi_matrix.getColumn(k).toArray());
                xi.set(t, k, max + log(model.density(k, observations[t - 1])));
            }
        }

        int[] states = new int[T];// the most likely sequence of states
        states[T - 1] = DoubleUtils.maxIndex(xi.getRow(T).toArray()) + 1;// equation (5.10), p.84 of Zucchini and MacDonald (2009)
        for (int t = T - 1; t > 0; --t) {// equation (5.11), p.84 of Zucchini and MacDonald (2009)
            states[t - 1] = DoubleUtils.maxIndex(logA.getColumn(states[t]).add(xi.getRow(t)).toArray()) + 1;
        }
        return states;// return the most likely sequence of states
    }
}