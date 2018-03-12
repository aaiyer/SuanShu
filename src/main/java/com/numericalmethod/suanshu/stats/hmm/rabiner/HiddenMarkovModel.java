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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.random.multivariate.MultinomialRvg;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is the hidden Markov model as defined by Rabiner.
 *
 * @author Kevin Sun
 * @see "L. R. Rabiner, "A tutorial on hidden Markov models and selected applications in speech recognition," Proceedings of the IEEE, Volume: 77, Issue:2, 257 - 286, Feb 1989."
 */
public class HiddenMarkovModel extends com.numericalmethod.suanshu.stats.hmm.HiddenMarkovModel {

    /** the conditional probabilities of the observation symbols */
    private final ImmutableMatrix B;

    /**
     * Construct a Rabiner hidden Markov model.
     *
     * @param PI the initial state probabilities
     * @param A  the state transition probabilities
     * @param B  the conditional probabilities of the observation symbols: rows correspond to state; columns corresponds symbols
     */
    public HiddenMarkovModel(Vector PI, Matrix A, Matrix B) {
        super(PI, A, getB(B));
        this.B = new ImmutableMatrix(B);
    }

    /**
     * Copy constructor.
     *
     * @param model a {@code HiddenMarkovModel}
     */
    public HiddenMarkovModel(HiddenMarkovModel model) {
        this(model.PI(), model.A(), model.B());
    }

    private static RandomNumberGenerator[] getB(final Matrix B) {
        final int nRows = B.nRows();

        RandomNumberGenerator[] rng = new RandomNumberGenerator[nRows];
        for (int i = 0; i < nRows; ++i) {
            final int j = i;
            rng[i] = new RandomNumberGenerator() {

                private final MultinomialRvg rvg = new MultinomialRvg(1, B.getRow(j + 1).toArray());

                @Override
                public void seed(long... seeds) {
                    rvg.seed(seeds);
                }

                @Override
                public double nextDouble() {
                    double u = bin(rvg);
                    return u;
                }
            };
        }

        return rng;
    }

    /**
     * Get the conditional probabilities of the observation symbols:
     * rows correspond to state; columns corresponds symbols.
     *
     * @return the observation symbol probabilities
     */
    public ImmutableMatrix B() {
        return B;
    }

    /**
     * Get the number of observation symbols per state.
     *
     * @return the number of observation symbols per state
     */
    public int nSymbols() {
        return B.nCols();
    }
}
