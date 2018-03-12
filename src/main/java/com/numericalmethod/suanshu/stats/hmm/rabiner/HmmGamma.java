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
 * <i>γ</i> is the probability of the system in state <i>s<sub>i</sub></i>,
 * given the model and observation sequence.
 * <blockquote><i>
 * γ<sub>t</sub>(i) = P(q<sub>t</sub> = s<sub>i</sub> | Ω, λ)
 * </i></blockquote>
 *
 * @author Kevin Sun
 */
public class HmmGamma {

    private final HiddenMarkovModel model;

    /**
     * Construct the gamma matrix generator.
     *
     * @param model a (discrete) hidden Markov model
     */
    public HmmGamma(HiddenMarkovModel model) {
        this.model = model;
    }

    /**
     * Get the <i>(T * N)</i> <i>γ</i> matrix,
     * where the (t, i)-th entry is <i>γ<sub>t</sub>(i)</i>.
     *
     * @param observations an integer array of observation symbols (length = <i>T</i>).
     * Each symbol is a positive integer less than or equal to <i>M</i> (the number of observation symbols per state).
     * @return the gamma matrix
     */
    public Matrix gamma(int[] observations) {
        final int T = observations.length;
        final int N = model.nStates();
        final Matrix[] xi = new HmmXi(model).xi(observations);

        Matrix gamma = new DenseMatrix(T - 1, N);
        for (int t = 1; t <= T - 1; ++t) {
            for (int i = 1; i <= N; ++i) {
                double gamma_ti = 0.;
                for (int j = 1; j <= N; ++j) {
                    gamma_ti += xi[t - 1].get(i, j);
                }

                gamma.set(t, i, gamma_ti);
            }
        }

        return gamma;
    }
}
