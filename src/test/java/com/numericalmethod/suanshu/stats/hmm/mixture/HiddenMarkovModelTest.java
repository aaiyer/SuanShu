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
import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.GammaDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.NormalDistribution;
import com.numericalmethod.suanshu.stats.markovchain.SimpleMC;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class HiddenMarkovModelTest {

    @Test
    public void test_0010() {
        Vector PI = new DenseVector(0., 1., 0.);
        Matrix A = new DenseMatrix(new double[][]{
                    {1. / 2., 1. / 2., 0.},
                    {1. / 3., 1. / 3., 1. / 3.},
                    {0., 1. / 2., 1. / 2.}
                });
        NormalDistribution.Lambda[] lambda = new NormalDistribution.Lambda[]{
            new NormalDistribution.Lambda(1., .5),// (mu, sigma)
            new NormalDistribution.Lambda(10., 1.),
            new NormalDistribution.Lambda(3., .5)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(
                PI, A,
                new NormalDistribution(lambda));
        model.seed(1234567890L);

        int T = 10000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
        }

        int[] visits = new int[model.nStates()];
        double[] sum = new double[T];
        for (int t = 0; t < T; ++t) {
            int state = innovations[t].getState();
            visits[state - 1] += 1;
            sum[state - 1] += innovations[t].getObservation();
        }

        double[] prob = new DenseVector(visits).scaled(1. / T).toArray();
        double[] expectedProb = SimpleMC.getStationaryProbabilities(A).toArray();
        assertArrayEquals(expectedProb, prob, 3e-3);

        double[] mean = new double[model.nStates()];
        for (int i = 0; i < model.nStates(); ++i) {
            mean[i] = sum[i] / visits[i];
            assertEquals(mean[i], lambda[i].mu, 3e-2);
        }
    }

    @Test
    public void test_0020() {
        Vector PI = new DenseVector(new double[]{0., 1.});
        Matrix A = new DenseMatrix(new double[][]{
                    {.8, .2},
                    {.3, .7}
                });
        GammaDistribution.Lambda[] lambda = new GammaDistribution.Lambda[]{
            new GammaDistribution.Lambda(3., 1. / 4),// (k, theta)
            new GammaDistribution.Lambda(3., 2.)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(
                PI, A,
                new GammaDistribution(lambda, 100));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
        }

        int[] visits = new int[model.nStates()];
        double[] sum = new double[T];
        for (int t = 0; t < T; ++t) {
            int state = innovations[t].getState();
            visits[state - 1] += 1;
            sum[state - 1] += innovations[t].getObservation();
        }

        double[] prob = new DenseVector(visits).scaled(1. / T).toArray();
        double[] expectedProb = SimpleMC.getStationaryProbabilities(A).toArray();
        assertArrayEquals(expectedProb, prob, 2e-3);

        double[] mean = new double[model.nStates()];
        for (int i = 0; i < model.nStates(); ++i) {
            mean[i] = sum[i] / visits[i];
            assertEquals(mean[i], lambda[i].k * lambda[i].theta, 3e-2);
        }
    }
}
