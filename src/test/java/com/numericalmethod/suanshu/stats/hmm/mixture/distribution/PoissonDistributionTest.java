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
package com.numericalmethod.suanshu.stats.hmm.mixture.distribution;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.stats.hmm.mixture.HiddenMarkovModel;
import com.numericalmethod.suanshu.stats.hmm.mixture.HmmBaumWelch;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PoissonDistributionTest {

    @Test
    public void test_poisson_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {.8, .2},
                    {.3, .7}
                });
        Double[] lambda0 = new Double[]{4., 1.};
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new PoissonDistribution(lambda0));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            observations[t] = innovations[t].getObservation();
        }

        Vector PI1 = new DenseVector(new double[]{1. / 2., 1. / 2.});
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1. / 2., 1. / 2.},
                    {1. / 2., 1. / 2.}
                });
        Double[] lambda1 = new Double[]{2., .5};
        HiddenMarkovModel model1 = new HiddenMarkovModel(PI1, A1, new PoissonDistribution(lambda1));
//        HiddenMarkovModel model1 = model; //using true parameters as initial estimates
        HiddenMarkovModel model2 = new HmmBaumWelch(observations, model1, 1e-5, 200);
        Matrix A2 = model2.A();
        Object[] lambda2 = model2.getDistribution().getParams();

        assertTrue(AreMatrices.equal(A0, A2, 1e-1));
        for (int i = 0; i < lambda0.length; ++i) {
            assertEquals(1., lambda0[i] / (Double) lambda2[i], 3e-2);
        }
    }
}
