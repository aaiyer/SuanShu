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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Kevin Sun
 */
public class HiddenMarkovModelTest {

    @Test
    public void test_0010() {
        int T = 100000;
        DenseVector PI = new DenseVector(new double[]{0.2, 0.8});
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.8, 0.2},
                    {0.3, 0.7}
                });
        DenseMatrix B = new DenseMatrix(new double[][]{
                    {0.8, 0.1, 0.1},
                    {0.1, 0.1, 0.8}
                });
        HiddenMarkovModel instance = new HiddenMarkovModel(PI, A, B);

        HmmInnovation[] innovations = new HmmInnovation[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = instance.next();
        }

        int N = instance.nStates();
        double[] counter_states = new double[N];
        for (int t = 0; t < T; ++t) {
            counter_states[innovations[t].getState() - 1] += 1;
        }

        int M = instance.nSymbols();
        double[] counter_observations = new double[M];
        for (int t = 0; t < T; ++t) {
            counter_observations[(int) innovations[t].getObservation() - 1] += 1;
        }

        //compare with theoretical stationary distribution
        assertEquals(0.6, counter_states[0] / T, 1e-2);
        assertEquals(0.4, counter_states[1] / T, 1e-2);
        assertEquals(0.52, counter_observations[0] / T, 1e-2);
        assertEquals(0.10, counter_observations[1] / T, 1e-2);
        assertEquals(0.38, counter_observations[2] / T, 1e-2);
    }
}
