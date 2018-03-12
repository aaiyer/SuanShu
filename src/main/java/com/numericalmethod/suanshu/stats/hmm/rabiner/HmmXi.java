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

/**
 * <i>ξ</i> is the probability of the system being in state <i>s<sub>i</sub><i> at time <i>t</i> and state <i>s<sub>j</sub><i> at time <i>t+1</i>,
 * given the model and observation sequence.
 * <blockquote><i>
 * ξ<sub>t</sub>(i, j) = P(q<sub>t</sub> = s<sub>i</sub>, q<sub>t+1</sub> = s<sub>j</sub> | Ω, λ)
 * </i></blockquote>
 *
 * @author Kevin Sun
 */
public class HmmXi {

    private final HiddenMarkovModel model;

    /**
     * Construct the xi matrices generator.
     * Generate the xi matrices of a (discrete) HMM, where for 1 ≤ t ≤ T - 1,
     * the t-th entry of xi is an (N * N) matrix, for which the (i, j)-th entry is xi_t(i, j).
     *
     * @param model a (discrete) hidden Markov model
     */
    public HmmXi(HiddenMarkovModel model) {
        this.model = model;
    }

    /**
     * Get the <i>ξ</i> matrices, where for <i>1 &le; t &le; T - 1</i>,
     * the <i>t</i>-th entry of <i>ξ</i> is an <i>(N * N)</i> matrix, for which the (i, j)-th entry is <i>ξ<sub>t</sub>(i, j)</i>.
     *
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @return the <i>ξ</i> matrices
     */
    public Matrix[] xi(int[] observations) {
        final int T = observations.length;
        final int N = model.nStates();

        final Matrix A = model.A();
        final Matrix B = model.B();

        HmmForwardBackward hmm_fb = new HmmForwardBackward(model);
        final Matrix alpha = hmm_fb.scaledAlpha(observations);
        final Matrix beta = hmm_fb.scaledBeta(observations);

        Matrix[] xi = new DenseMatrix[T - 1];
        for (int t = 1; t <= T - 1; ++t) {
            xi[t - 1] = new DenseMatrix(N, N);

            double sum = 0.;
            for (int i = 1; i <= N; ++i) {
                for (int j = 1; j <= N; ++j) {
                    double xi_t_ij = alpha.get(t, i) * A.get(i, j) * B.get(j, observations[t]) * beta.get(t + 1, j);
                    xi[t - 1].set(i, j, xi_t_ij);

                    sum += xi_t_ij;
                }
            }

            xi[t - 1] = xi[t - 1].scaled(1. / sum);
        }

        return xi;
    }
}
