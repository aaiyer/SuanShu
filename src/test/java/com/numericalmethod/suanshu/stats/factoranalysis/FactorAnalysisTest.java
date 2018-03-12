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
package com.numericalmethod.suanshu.stats.factoranalysis;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class FactorAnalysisTest {

    private final static Matrix data = new DenseMatrix(new double[][]{
                {1., 1., 3., 3., 1., 1.},
                {1., 2., 3., 3., 1., 1.},
                {1., 1., 3., 4., 1., 1.},
                {1., 1., 3., 3., 1., 2.},
                {1., 1., 3., 3., 1., 1.},
                {1., 1., 1., 1., 3., 3.},
                {1., 2., 1., 1., 3., 3.},
                {1., 1., 1., 2., 3., 3.},
                {1., 2., 1., 1., 3., 4.},
                {1., 1., 1., 1., 3., 3.},
                {3., 3., 1., 1., 1., 1.},
                {3., 4., 1., 1., 1., 1.},
                {3., 3., 1., 2., 1., 1.},
                {3., 3., 1., 1., 1., 2.},
                {3., 3., 1., 1., 1., 1.},
                {4., 4., 5., 5., 6., 6.},
                {5., 6., 4., 6., 4., 5.},
                {6., 5., 6., 4., 5., 4.}
            });

    /**
     * THOMSON
     */
    @Test
    public void test_0010() {
        int nFactors = 3;
        FactorAnalysis instance = new FactorAnalysis(data, nFactors, FactorAnalysis.ScoringRule.THOMSON);

        assertEquals(instance.nObs(), 18., 1e-15);
        assertEquals(instance.nVariables(), 6., 1e-15);
        assertEquals(instance.nFactors(), 3., 1e-15);

        Matrix corr = instance.S();
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.0000000, 0.9393083, 0.5128866, 0.4320310, 0.4664948, 0.4086076},
                    {0.9393083, 1.0000000, 0.4124441, 0.4084281, 0.4363925, 0.4326113},
                    {0.5128866, 0.4124441, 1.0000000, 0.8770750, 0.5128866, 0.4320310},
                    {0.4320310, 0.4084281, 0.8770750, 1.0000000, 0.4320310, 0.4323259},
                    {0.4664948, 0.4363925, 0.5128866, 0.4320310, 1.0000000, 0.9473451},
                    {0.4086076, 0.4326113, 0.4320310, 0.4323259, 0.9473451, 1.0000000}
                }),
                corr,
                1e-6));

        FAEstimator estimators = instance.getEstimators(300);

        Vector uniqueness = estimators.psi();
//        System.out.println(uniqueness);
        assertArrayEquals(
                new double[]{0.005, 0.101, 0.005, 0.224, 0.084, 0.005},
                uniqueness.toArray(),
                1e-3);

        int dof = estimators.dof();
        assertEquals(dof, 0);

        double fitted = estimators.logLikelihood();
        assertEquals(fitted, 0.4755, 1e-3);

        Matrix loadings = estimators.loadings();
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.944, 0.182, 0.267},
                    {0.905, 0.235, 0.159},
                    {0.236, 0.210, 0.946},
                    {0.180, 0.242, 0.828},
                    {0.242, 0.881, 0.286},
                    {0.193, 0.959, 0.196}
                }),
                loadings,
                1e-3));

        double testStats = estimators.statistics();
        assertEquals(testStats, 5.79, 1e-2);

        double pValue = estimators.pValue();
        assertEquals(pValue, -1., 1e-15);

        Matrix scores = estimators.scores();
        assertTrue(AreMatrices.equal( //Thomson's (1951) scores
                new DenseMatrix(new double[][]{
                    {-0.8965113, -0.9246889, 0.9363764},
                    {-0.8613962, -0.9266171, 0.9242550},
                    {-0.9007301, -0.9257723, 0.9503640},
                    {-0.9933051, -0.2514130, 0.8086594},
                    {-0.8965113, -0.9246889, 0.9363764},
                    {-0.7411512, 0.7201081, -0.7835330},
                    {-0.7060361, 0.7181799, -0.7956543},
                    {-0.7453700, 0.7190246, -0.7695453},
                    {-0.8028298, 1.3914557, -0.9233713},
                    {-0.7411512, 0.7201081, -0.7835330},
                    {0.9168926, -0.9250562, -0.8303339},
                    {0.9520078, -0.9269844, -0.8424553},
                    {0.9126739, -0.9261397, -0.8163463},
                    {0.8200989, -0.2517803, -0.9580509},
                    {0.9168926, -0.9250562, -0.8303339},
                    {0.4264536, 2.0356890, 1.2824424},
                    {1.4647876, 1.2901018, 0.5479527},
                    {1.8751853, 0.3135297, 1.9467308}
                }),
                scores,
                1e-3));

//        System.out.println(uniqueness.toString());
//        System.out.println(fitted);
//        System.out.println(loadings.toString());
//        System.out.println(scores.toString());
    }

    /**
     * BARTLETT
     */
    @Test
    public void test_0015() {
        int nFactors = 3;

        FactorAnalysis instance = new FactorAnalysis(data, nFactors, FactorAnalysis.ScoringRule.BARTLETT);

        assertEquals(instance.nObs(), 18., 1e-15);
        assertEquals(instance.nVariables(), 6., 1e-15);
        assertEquals(instance.nFactors(), 3., 1e-15);

        Matrix corr = instance.S();
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.0000000, 0.9393083, 0.5128866, 0.4320310, 0.4664948, 0.4086076},
                    {0.9393083, 1.0000000, 0.4124441, 0.4084281, 0.4363925, 0.4326113},
                    {0.5128866, 0.4124441, 1.0000000, 0.8770750, 0.5128866, 0.4320310},
                    {0.4320310, 0.4084281, 0.8770750, 1.0000000, 0.4320310, 0.4323259},
                    {0.4664948, 0.4363925, 0.5128866, 0.4320310, 1.0000000, 0.9473451},
                    {0.4086076, 0.4326113, 0.4320310, 0.4323259, 0.9473451, 1.0000000}
                }),
                corr,
                1e-6));

        FAEstimator estimators = instance.getEstimators(300);

        Vector uniqueness = estimators.psi();
//        System.out.println(uniqueness);
        assertArrayEquals(
                new double[]{0.005, 0.101, 0.005, 0.224, 0.084, 0.005},
                uniqueness.toArray(),
                1e-3);

        int dof = estimators.dof();
        assertEquals(dof, 0);

        double fitted = estimators.logLikelihood();
        assertEquals(fitted, 0.4755, 1e-3);

        Matrix loadings = estimators.loadings();
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.944, 0.182, 0.267},
                    {0.905, 0.235, 0.159},
                    {0.236, 0.210, 0.946},
                    {0.180, 0.242, 0.828},
                    {0.242, 0.881, 0.286},
                    {0.193, 0.959, 0.196}
                }),
                loadings,
                1e-3));

        double testStats = estimators.statistics();
        assertEquals(testStats, 5.79, 1e-2);

        double pValue = estimators.pValue();
        assertEquals(pValue, -1., 1e-15);

        Matrix scores = estimators.scores();
        assertTrue(AreMatrices.equal( //Bartlett's (1937) scores
                new DenseMatrix(new double[][]{
                    {-0.9039949, -0.9308984, 0.9475392},
                    {-0.8685952, -0.9328721, 0.9352330},
                    {-0.9082818, -0.9320093, 0.9616422},
                    {-1.0021975, -0.2529689, 0.8178552},
                    {-0.9039949, -0.9308984, 0.9475392},
                    {-0.7452711, 0.7273960, -0.7884733},
                    {-0.7098714, 0.7254223, -0.8007795},
                    {-0.7495580, 0.7262851, -0.7743704},
                    {-0.8080740, 1.4033517, -0.9304636},
                    {-0.7452711, 0.7273960, -0.7884733},
                    {0.9272282, -0.9307506, -0.8371538},
                    {0.9626279, -0.9327243, -0.8494600},
                    {0.9229413, -0.9318615, -0.8230509},
                    {0.8290256, -0.2528211, -0.9668378},
                    {0.9272282, -0.9307506, -0.8371538},
                    {0.4224366, 2.0453079, 1.2864761},
                    {1.4713902, 1.2947716, 0.5451562},
                    {1.8822320, 0.3086244, 1.9547752}
                }),
                scores,
                1e-3));

//        System.out.println(uniqueness.toString());
//        System.out.println(fitted);
//        System.out.println(loadings.toString());
//        System.out.println(scores.toString());
    }

    //TODO: fix this test case
//    @Test
    public void test_0020() {
        int nFactors = 2;
        FactorAnalysis instance = new FactorAnalysis(data, nFactors);

        assertEquals(instance.nObs(), 18., 1e-15);
        assertEquals(instance.nVariables(), 6., 1e-15);
        assertEquals(instance.nFactors(), 2., 1e-15);

        FAEstimator estimators = instance.getEstimators(400);

        Vector uniqueness = estimators.psi();
        assertArrayEquals(
                new double[]{0.005, 0.114, 0.642, 0.742, 0.005, 0.097},
                uniqueness.toArray(),
                2e-2);

        int dof = estimators.dof();
        assertEquals(dof, 4);

        double fitted = estimators.logLikelihood();
        assertEquals(fitted, 1.803, 1e-3);

        Matrix loadings = estimators.loadings();
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.971, 0.228},
                    {0.917, 0.213},
                    {0.429, 0.418},
                    {0.363, 0.355},
                    {0.254, 0.965},
                    {0.205, 0.928}
                }),
                loadings,
                1e-3));

        double testStats = estimators.statistics();
        assertEquals(testStats, 23.14, 1e-2);

        double pValue = estimators.pValue();
        assertEquals(pValue, 0.000119, 1e-6);//R: 1-pchisq(23.14, df=4) = 0.0001187266

//        System.out.println(uniqueness.toString());
//        System.out.println(fitted);
//        System.out.println(loadings.toString());
    }

    @Test
    public void test_getLoadings_0010() {
        DenseMatrix lambda = new DenseMatrix(new double[][]{
                    {0.808425, -0.385122, -0.439481},
                    {0.751916, -0.290233, -0.499539},
                    {0.813597, -0.228543, 0.529954},
                    {0.729299, -0.138824, 0.474316},
                    {0.801547, 0.521102, -0.039936},
                    {0.763710, 0.636342, -0.082725}
                });

        Matrix loadings = FactorAnalysis.getRotatedLoadings(lambda, true, 1e-5);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.944, 0.182, 0.267},
                    {0.905, 0.235, 0.159},
                    {0.236, 0.210, 0.946},
                    {0.180, 0.242, 0.828},
                    {0.242, 0.881, 0.286},
                    {0.193, 0.959, 0.196}
                }),
                loadings,
                1e-3));

        Matrix rotation = FactorAnalysis.getVarimaxRotation(lambda, true, 1e-5);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.582, 0.559, 0.591},
                    {-0.481, 0.822, -0.304},
                    {-0.656, -0.107, 0.747}
                }),
                rotation,
                1e-3));

//        System.out.println(loadings.toString());
//        System.out.println(rot.toString());
    }
}
