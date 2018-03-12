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
package com.numericalmethod.suanshu.stats.hmm.rabiner;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This implementation trains an HMM model by observations using an EM algorithm.
 *
 * @author Kevin Sun
 */
public class HmmTrainByEM extends HiddenMarkovModel {

    /**
     * Construct an HMM model by training an initial model using an EM algorithm.
     *
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @param model0       an initial model
     * @param nIteraions   the number of iterations
     */
    public HmmTrainByEM(int[] observations, HiddenMarkovModel model0, int nIteraions) {
        super(getHMM(model0, observations, nIteraions));
    }

    private static HiddenMarkovModel getHMM(HiddenMarkovModel model0, int[] observations, int nIterations) {
        HiddenMarkovModel model = new HiddenMarkovModel(model0);

        for (int i = 0; i < nIterations; ++i) {
            model = train(model, observations);
        }

        return model;
    }

    /**
     * Construct a trained (discrete) hidden Markov model, one iteration.
     *
     * @param model0       the initial hidden Markov model
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @return a trained (discrete) hidden Markov model
     */
    public static HiddenMarkovModel train(HiddenMarkovModel model0, int[] observations) {
        final int T = observations.length;
        final int N = model0.nStates();
        final int M = model0.nSymbols();

        final Matrix[] xi = new HmmXi(model0).xi(observations);
        final Matrix gamma = new HmmGamma(model0).gamma(observations);

        Vector PI = new DenseVector(N);//initial state probabilities
        Matrix A = new DenseMatrix(N, N);//state transition probabilities
        Matrix B = new DenseMatrix(N, M);//observation symbol probabilities

        //generate the new initial state probabilities
        for (int i = 1; i <= N; ++i) {
            PI.set(i, gamma.get(1, i));
        }

        //generate the new state transition probabilities
        for (int i = 1; i <= N; ++i) {
            double denominator = 0;
            for (int t = 1; t <= T - 1; ++t) {
                denominator += gamma.get(t, i);
            }

            for (int j = 1; j <= N; ++j) {
                double numerator = 0;
                for (int s = 1; s <= T - 1; ++s) {
                    numerator += xi[s - 1].get(i, j);
                }

                A.set(i, j, numerator / denominator);
            }
        }

        //generate the new observation symbol probabilities
        for (int j = 1; j <= N; ++j) {
            double denominator = 0.;
            for (int t = 1; t <= T - 1; ++t) {
                denominator += gamma.get(t, j);
            }

            for (int k = 1; k <= M; ++k) {
                double numerator = 0.;
                for (int s = 1; s <= T - 1; ++s) {
                    if (observations[s - 1] == k) {
                        numerator += gamma.get(s, j);
                    }
                }

                B.set(j, k, numerator / denominator);
            }
        }

        HiddenMarkovModel hmm = new HiddenMarkovModel(PI, A, B);
        return hmm;
    }
}
