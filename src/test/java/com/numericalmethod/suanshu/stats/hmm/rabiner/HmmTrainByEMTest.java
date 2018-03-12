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

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Kevin Sun
 */
public class HmmTrainByEMTest {

    /**
     * Explicit iteration.
     */
    @Test
    public void test_0010() {
        int T = 10000;
        DenseVector PI = new DenseVector(new double[]{0.2, 0.8});
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.8, 0.2},
                    {0.3, 0.7}
                });
        DenseMatrix B = new DenseMatrix(new double[][]{
                    {0.8, 0.1, 0.1},
                    {0.1, 0.1, 0.8}
                });
        HiddenMarkovModel model = new HiddenMarkovModel(PI, A, B);

        HmmInnovation[] innovations = new HmmInnovation[T];
        int[] states = new int[T];
        int[] observations = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = (int) innovations[t].getObservation();
        }

        DenseVector PI_0 = new DenseVector(new double[]{0.5, 0.5});
        DenseMatrix A_0 = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0.5, 0.5}
                });
        DenseMatrix B_0 = new DenseMatrix(new double[][]{
                    {0.60, 0.20, 0.20},
                    {0.20, 0.20, 0.60}
                });
        HiddenMarkovModel model_0 = new HiddenMarkovModel(PI_0, A_0, B_0);

        int nIterations = 100;
        for (int i = 1; i <= nIterations; ++i) {
            model_0 = HmmTrainByEM.train(model_0, observations);
        }

//        System.out.println(model_0.A());
//        System.out.println(model_0.B());

        //PI should be the stationary distribution of transition matrix A
        assertTrue(AreMatrices.equal(model_0.A(), A, 1e-1));
        assertTrue(AreMatrices.equal(model_0.B(), B, 1e-1));
    }

    /**
     * Use ctor.
     */
    @Test
    public void test_0020() {
        int T = 10000;
        DenseVector PI = new DenseVector(new double[]{0.2, 0.8});
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.8, 0.2},
                    {0.3, 0.7}
                });
        DenseMatrix B = new DenseMatrix(new double[][]{
                    {0.8, 0.1, 0.1},
                    {0.1, 0.1, 0.8}
                });
        HiddenMarkovModel model = new HiddenMarkovModel(PI, A, B);

        HmmInnovation[] innovations = new HmmInnovation[T];
        int[] states = new int[T];
        int[] observations = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = (int) innovations[t].getObservation();
        }

        DenseVector PI_0 = new DenseVector(new double[]{0.5, 0.5});
        DenseMatrix A_0 = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0.5, 0.5}
                });
        DenseMatrix B_0 = new DenseMatrix(new double[][]{
                    {0.60, 0.20, 0.20},
                    {0.20, 0.20, 0.60}
                });
        HiddenMarkovModel model_0 = new HiddenMarkovModel(PI_0, A_0, B_0);
        int nIterations = 100;
        model_0 = new HmmTrainByEM(observations, model_0, nIterations);

//        System.out.println(model_0.A());
//        System.out.println(model_0.B());

        //PI should be the stationary distribution of transition matrix A
        assertTrue(AreMatrices.equal(model_0.A(), A, 1e-1));
        assertTrue(AreMatrices.equal(model_0.B(), B, 1e-1));
    }
}
