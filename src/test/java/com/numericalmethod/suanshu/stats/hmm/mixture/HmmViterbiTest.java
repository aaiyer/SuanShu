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
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.BetaDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.BinomialDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.ExponentialDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.GammaDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.LogNormalDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.NormalDistribution;
import com.numericalmethod.suanshu.stats.hmm.mixture.distribution.PoissonDistribution;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HmmViterbiTest {

    @Test
    public void test_beta_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {.8, .2},
                    {.3, .7}
                });
        BetaDistribution.Lambda[] lambda0 = new BetaDistribution.Lambda[]{
            new BetaDistribution.Lambda(0.5, 2.2),// (alpha, beta)
            new BetaDistribution.Lambda(1.5, 0.7)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new BetaDistribution(lambda0, 200));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

    @Test
    public void test_binomial_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {.8, .2},
                    {.3, .7}
                });
        BinomialDistribution.Lambda[] lambda0 = new BinomialDistribution.Lambda[]{
            new BinomialDistribution.Lambda(30, .2),// (size, p)
            new BinomialDistribution.Lambda(100, .8)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new BinomialDistribution(lambda0));

        model.seed(1234567890L);
        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

    @Test
    public void test_exponential_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {.7, .3},
                    {.3, .7}
                });
        Double[] lambda0 = new Double[]{3., .1};
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new ExponentialDistribution(lambda0));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

    @Test
    public void test_gamma_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {.8, .2},
                    {.3, .7}
                });
        GammaDistribution.Lambda[] lambda0 = new GammaDistribution.Lambda[]{
            new GammaDistribution.Lambda(4., 1. / 4),// (k, theta)
            new GammaDistribution.Lambda(3., 2.)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new GammaDistribution(lambda0, 200));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

    @Test
    public void test_lognormal_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1., 0.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {1. / 2., 1. / 2., 0.},
                    {1. / 3., 1. / 3., 1. / 3.},
                    {0., 1. / 2., 1. / 2.}
                });
        LogNormalDistribution.Lambda[] lambda0 = new LogNormalDistribution.Lambda[]{
            new LogNormalDistribution.Lambda(1., .5),// (mu, sigma)
            new LogNormalDistribution.Lambda(10., 1.),
            new LogNormalDistribution.Lambda(3., .5)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new LogNormalDistribution(lambda0));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

    @Test
    public void test_normal_0010() {
        Vector PI0 = new DenseVector(new double[]{0., 1., 0.});
        Matrix A0 = new DenseMatrix(new double[][]{
                    {1. / 2., 1. / 2., 0.},
                    {1. / 3., 1. / 3., 1. / 3.},
                    {0., 1. / 2., 1. / 2.}
                });
        NormalDistribution.Lambda[] lambda0 = new NormalDistribution.Lambda[]{
            new NormalDistribution.Lambda(1., .5),// (mu, sigma)
            new NormalDistribution.Lambda(10., 1.),
            new NormalDistribution.Lambda(3., .5)
        };
        HiddenMarkovModel model = new HiddenMarkovModel(PI0, A0, new NormalDistribution(lambda0));
        model.seed(1234567890L);

        int T = 1000;
        HmmInnovation[] innovations = new HmmInnovation[T];
        double[] observations = new double[T];
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }

        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }

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
        int[] states = new int[T];
        for (int t = 0; t < T; ++t) {
            innovations[t] = model.next();
            states[t] = innovations[t].getState();
            observations[t] = innovations[t].getObservation();
        }
        HmmViterbi instance = new HmmViterbi(model);
        int[] viterbi_states = instance.getViterbiStates(observations);

        int count = 0;
        for (int t = 0; t < T; ++t) {
            if (viterbi_states[t] == states[t]) {
                count += 1;
            }
        }
        assertEquals(1., (double) count / T, 2e-1);

//        //print out the original states and viterbi states
//        System.out.print("# matching states: " + count + "/" + T + "\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(states[t]);
//        }
//        System.out.print("\n");
//        for (int t = 0; t < T; ++t) {
//            System.out.print(viterbi_states[t]);
//        }
    }
}
