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
 * The forward–backward algorithm is an inference algorithm for hidden Markov models
 * which computes the posterior marginals of all hidden state variables given a sequence of observations.
 * This inference task is usually called smoothing.
 * The algorithm makes use of the principle of dynamic programming to efficiently compute the values
 * that are required to obtain the posterior marginal distributions in two passes.
 * The first pass goes forward in time while the second goes backward in time; hence the name forward–backward algorithm.
 * <p/>
 * The forward probability, <i>α</i>, is the probability of the partial observation sequence until time <i>t</i>
 * and the system in state <i>s<sub>i</sub></i> and time <i>t</i>.
 * <blockquote><i>
 * α<sub>t</sub>(i) = p(ω<sub>1</sub>, ω<sub>2</sub>, ... ω<sub>t</sub>, q<sub>t</sub> = s<sub>i</sub> | λ)
 * </i></blockquote>
 * The backward probability, <i>β</i>, is the probability of the system in state <i>s<sub>i</sub></i> at time <i>t</i>,
 * and the partial observations from then onward till time <i>t</i>.
 * <blockquote><i>
 * β<sub>t</sub>(i) = p(ω<sub>t+1</sub>, ω<sub>t+2</sub>, ... ω<sub>T</sub> | q<sub>t</sub> = s<sub>i</sub>, λ)
 * </i></blockquote>
 *
 * @author Kevin Sun
 * @see <a href="http://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm">Wikipedia: Forward–backward algorithm</a>
 */
public class HmmForwardBackward {

    private HiddenMarkovModel model;
    /** the scaling vector for α and β, row by row for T rows */
    private Vector scales = null;

    /**
     * Construct the forward and backward probability matrix generator for an HMM model.
     *
     * @param model a (discrete) hidden Markov model
     */
    public HmmForwardBackward(HiddenMarkovModel model) {
        this.model = model;
    }

    /**
     * Get the scaled forward probability matrix, dimension <i>(T * N)</i>.
     *
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @return scaled alpha, the scaled forward probability matrix
     */
    public Matrix scaledAlpha(int[] observations) {
        final int T = observations.length;
        final int N = model.nStates();

        final Vector PI = model.PI();
        final Matrix A = model.A();
        final Matrix B = model.B();

        DenseMatrix alpha = new DenseMatrix(T, N);
        scales = new DenseVector(T);

        //create the scaled forward variable matrix (alpha) and the scaling vector
        for (int t = 1; t <= T; ++t) {//forward
            double sum_alpha_ti = 0.;
            for (int i = 1; i <= N; ++i) {//create the t-th row of alpha
                double sum = 0.;
                if (t == 1) {
                    sum = PI.get(i);
                } else {
                    for (int j = 1; j <= N; ++j) {
                        //sum_t(i) = sum_{j = 1:N}[scaled_alpha_{t - 1,j} * a_{j,i}]
                        sum += alpha.get(t - 1, j) * A.get(j, i);
                    }
                }

                //alpha_{t,i}) = sum_t(i) * b_{i,O_{t}}
                double alpha_ti = sum * B.get(i, observations[t - 1]);
                alpha.set(t, i, alpha_ti);

                sum_alpha_ti += alpha_ti;
            }

            //scale_t = 1 / sum(alpha_{t,i})
            scales.set(t, 1. / sum_alpha_ti);

            //scale the t-th row of alpha
            //without this scaling, alpha may blow up to infinity
            alpha.setRow(t, alpha.getRow(t).scaled(scales.get(t)));
        }

        return alpha;
    }

    /**
     * Get the scaled backward probability matrix, dimension <i>(T * N)</i>.
     *
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @return scaled beta, the scaled backward probability matrix
     */
    public Matrix scaledBeta(int[] observations) {
        if (scales == null) {
            scaledAlpha(observations);//compute 'scales'
        }

        final int T = observations.length;
        final int N = model.nStates();

        final Matrix A = model.A();
        final Matrix B = model.B();

        DenseMatrix beta = new DenseMatrix(T, N);

        //create the scaled backward variable matrix (beta)
        for (int t = T; t >= 1; --t) {//backward
            for (int i = 1; i <= N; ++i) {//create the t-th row of beta
                double beta_ti = 0.;

                if (t == T) {
                    beta_ti = 1;
                } else {
                    for (int j = 1; j <= N; ++j) {
                        //beta_t(i) = sum_{j = 1:N}[a_{i,j} * b_{j, O_{t+1}} * beta_{t + 1, j}]
                        beta_ti += A.get(i, j) * B.get(j, observations[t]) * beta.get(t + 1, j);
                    }
                }

                beta.set(t, i, beta_ti);
            }

            //scale the t-th row of beta
            beta.setRow(t, beta.getRow(t).scaled(scales.get(t)));
        }

        scales = null;//make sure the same set of observations is used to compute alpha and beta

        return beta;
    }
}
