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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Cloglog;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Inverse;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Probit;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.Binomial;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.Gamma;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.Gaussian;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.InverseGaussian;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.Poisson;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class GeneralizedLinearModelQuasiFamilyTest {

    /**
     *
    options(digits=22)
    y<-c(2,1,4,5,7)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(2.11,4.32,1.23,8.43,7.31)
    fit<-glm(y~x+x2,family=quasipoisson(link="log"))
    summary(fit)
     */
    @Test
    public void test_0010() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{2, 1, 4, 5, 7}),
                new DenseMatrix(new double[][]{
                    {1.52, 2.11},
                    {3.22, 4.32},
                    {4.32, 1.23},
                    {10.1034, 8.43},
                    {12.1, 7.31}
                }),
                true,
                new Poisson()));

        assertArrayEquals(new double[]{0.21759215788952188, -0.15816534883126429, 0.55792449196154736},
                instance.beta.betaHat.toArray(), 1e-13);//summary(fit)
        assertArrayEquals(new double[]{0.06578700632927305, 0.09428329837786431, 0.30199858202168173},
                instance.beta.stderr.toArray(), 1e-7);//summary(fit)
        assertArrayEquals(new double[]{3.30752, -1.67755, 1.84744},
                instance.beta.z.toArray(), 1e-5);//summary(fit)
        assertArrayEquals(new double[]{1.741828957044140, 1.777658294701912, 3.681717057165314, 4.149596951206723, 7.649198739882662},
                instance.residuals.fitted.toArray(), 1e-12);//fit$fitted
        assertArrayEquals(new double[]{0.1910599190030411, -0.6361782160482490, 0.1635699343862991, 0.4043030217604162, -0.2381741631340939},
                instance.residuals.devianceResiduals.toArray(), 1e-12);//summary(fit)
        assertEquals(0.688169604048149, instance.residuals.deviance, 1e-14);//summary(fit)
        assertEquals(0.3176772023867847, instance.residuals.overdispersion, 1e-6);//summary(fit)
    }

    /**
     *
    options(digits=22)
    y<-c(1,1,0,1,1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=quasibinomial(link="probit"))
    summary(fit)
     */
    @Test
    public void test_0020() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new Binomial(new Probit())));

        assertArrayEquals(new double[]{0.1059822214175312, 0.2668212611575769},
                instance.beta.betaHat.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.2172903664994691, 1.3505586395744520},
                instance.beta.stderr.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.48774, 0.19756},
                instance.beta.z.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.6656432195472630, 0.7284341229649013, 0.7656710476309836, 0.9094868999672544, 0.9393339130615579},
                instance.residuals.fitted.toArray(), 1e-5);//fit$fitted
        assertArrayEquals(new double[]{0.902221101913684, 0.796062919323877, -1.703542997081316, 0.435602306426660, 0.353791628001423},
                instance.residuals.devianceResiduals.toArray(), 1e-4);//summary(fit)
        assertEquals(4.66469571657371, instance.residuals.deviance, 1e-8);//summary(fit)
        assertEquals(1.43558775261363, instance.residuals.overdispersion, 1e-5);//summary(fit)
    }

    /**
     *
    options(digits=22)
    y<-c(1,1,0,1,1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=quasibinomial(link="logit"))
    summary(fit)
     */
    @Test
    public void test_0030() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new Binomial()));

        assertArrayEquals(new double[]{0.1652503104020512, 0.4875920935827166},
                instance.beta.betaHat.toArray(), 1e-7);//summary(fit)
        assertArrayEquals(new double[]{0.3962679217441660, 2.3122231183350035},
                instance.beta.stderr.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.41702, 0.21088},
                instance.beta.z.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.6767273917948458, 0.7349137874861811, 0.7687867955952535, 0.8963380104192372, 0.9232339971470082},
                instance.residuals.fitted.toArray(), 1e-7);//fit$fitted
        assertArrayEquals(new double[]{0.883727059755680, 0.784859328269946, -1.711382500399892, 0.467841198216398, 0.399681269448019},
                instance.residuals.devianceResiduals.toArray(), 1e-7);//summary(fit)
        assertEquals(4.70442824788789, instance.residuals.deviance, 1e-3);//summary(fit)
        assertEquals(1.4541562862236, instance.residuals.overdispersion, 1e-4);//summary(fit)
    }

    /**
     *
    options(digits=22)
    y<-c(1,1,0,1,1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=quasi(link="inverse"))
    summary(fit)
     */
    @Test
    public void test_0040() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new Gaussian(new Inverse())));

        assertArrayEquals(new double[]{-0.0454417040520402, 1.5660577937453493},
                instance.beta.betaHat.toArray(), 1e-5);//summary(fit)
        assertArrayEquals(new double[]{0.0836599753958259, 0.8004522658441250},
                instance.beta.stderr.toArray(), 1e-5);//summary(fit)
        assertArrayEquals(new double[]{-0.54317, 1.95647},
                instance.beta.z.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.6680087391604592, 0.7043565475980386, 0.7300604259803842, 0.9033896327016069, 0.9840454984061791},
                instance.residuals.fitted.toArray(), 1e-5);//fit$fitted
        assertArrayEquals(new double[]{0.33199126083954078, 0.29564345240196144, -0.73006042598038423, 0.09661036729839312, 0.01595450159382095},
                instance.residuals.devianceResiduals.toArray(), 1e-5);//summary(fit)
        assertEquals(0.7401995829952767, instance.residuals.deviance, 1e-10);//summary(fit)
        assertEquals(0.2467486830076983, instance.residuals.overdispersion, 1e-4);//summary(fit)
    }

    /**
     *
    options(digits=22)
    y<-c(2.3,1.5, 0.5, 10.9, 4.14)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(-3.2,-2.1,-4.2,-8.112,-5.3)
    fit<-glm(y~x+x2,family=quasi(link="identity"))
    summary(fit)
     */
    @Test
    public void test_0050() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{2.3, 1.5, 0.5, 10.9, 4.14}),
                new DenseMatrix(new double[][]{
                    {1.52, -3.2},
                    {3.22, -2.1},
                    {4.32, -4.2},
                    {10.1034, -8.112},
                    {12.1, -5.3}
                }),
                true,
                new Gaussian()));

        assertArrayEquals(new double[]{-0.0892699976294480, -1.7581212392231711, -3.6302380378385606},
                instance.beta.betaHat.toArray(), 1e-14);//summary(fit)
        assertArrayEquals(new double[]{0.4356626118230586, 0.8691613208648583, 2.8022273167644971},
                instance.beta.stderr.toArray(), 1e-14);//summary(fit)
        assertArrayEquals(new double[]{-0.20491, -2.02278, -1.29548},
                instance.beta.z.toArray(), 1e-5);//summary(fit)
        assertArrayEquals(new double[]{1.8600595312788262, -0.2256328278367238, 3.3682247771395435, 9.7297109606904382, 4.6076375587279257},
                instance.residuals.fitted.toArray(), 1e-14);//fit$fitted
        assertArrayEquals(new double[]{0.4399404687211736, 1.7256328278367237, -2.8682247771395435, 1.1702890393095622, -0.4676375587279260},
                instance.residuals.devianceResiduals.toArray(), 1e-14);//summary(fit)
        assertEquals(12.98633096658467, instance.residuals.deviance, 1e-14);//summary(fit)
        assertEquals(6.493165483292334, instance.residuals.overdispersion, 1e-14);//summary(fit)
    }

    /**
     *
    options(digits=22)
    y<-c(1,1,0,1,1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=quasibinomial(link="cloglog"))
    summary(fit)
     */
    @Test
    public void test_0060() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new Binomial(new Cloglog())));

        assertArrayEquals(new double[]{0.1097282488353725, -0.1350581528472387},
                instance.beta.betaHat.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.1795161906084604, 1.2468084630688607},
                instance.beta.stderr.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.61124, -0.10832},
                instance.beta.z.toArray(), 1e-4);//summary(fit)
        assertArrayEquals(new double[]{0.6437909528715554, 0.7117476338995481, 0.7542641909516876, 0.9291633939433972, 0.9629621746144192},
                instance.residuals.fitted.toArray(), 1e-4);//fit$fitted
        assertArrayEquals(new double[]{0.9384894383171886, 0.8246597809661420, -1.6754093631719411, 0.3833292947361568, 0.2747404107619544},
                instance.residuals.devianceResiduals.toArray(), 1e-4);//summary(fit)
        assertEquals(4.590246355888813, instance.residuals.deviance, 1e-5);//summary(fit)
        assertEquals(1.380806994195947, instance.residuals.overdispersion, 1e-5);//summary(fit)
    }

    /**
     * R does not support Quasi-Gamma.
     *
     * TODO: values are from debugger.
     */
    @Test
    public void test_0070() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1.3, 2.4, 0.4, 2, 5.2}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new InverseGaussian()));

        assertArrayEquals(new double[]{-0.054021, 0.700103},
                instance.beta.betaHat.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{0.041365, 0.475320},
                instance.beta.stderr.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{-1.305946, 1.472910},
                instance.beta.z.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{1.272063, 1.378614, 1.463746, 2.545690, 4.639885},
                instance.residuals.fitted.toArray(), 1e-6);//fit$fitted
        assertArrayEquals(new double[]{0.019262, 0.478235, -1.149059, -0.151574, 0.052938},
                instance.residuals.devianceResiduals.toArray(), 1e-6);//summary(fit)
        assertEquals(1.5751930855253442, instance.residuals.deviance, 1e-15);//summary(fit)
        assertEquals(0.2601779527252455, instance.residuals.overdispersion, 1e-15);//summary(fit)
    }

    /**
     * R does not support Quasi-Gamma.
     *
     * TODO: values are from debugger.
     */
    @Test
    public void test_0080() {
        GeneralizedLinearModelQuasiFamily instance = new GeneralizedLinearModelQuasiFamily(
                new QuasiGlmProblem(
                new DenseVector(new double[]{1.3, 2.4, 0.4, 2, 5.2}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true,
                new Gamma()));

        assertArrayEquals(new double[]{-0.056857, 0.918260},
                instance.beta.betaHat.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{0.035007, 0.371082},
                instance.beta.stderr.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{-1.624179, 2.474545},
                instance.beta.z.toArray(), 1e-6);//summary(fit)
        assertArrayEquals(new double[]{1.202159, 1.360211, 1.486685, 2.908582, 4.342363},
                instance.residuals.fitted.toArray(), 1e-6);//fit$fitted
        assertArrayEquals(new double[]{0.079279, 0.627062, -1.078791, -0.352530, 0.185821},
                instance.residuals.devianceResiduals.toArray(), 1e-6);//summary(fit)
        assertEquals(1.7220880413692266, instance.residuals.deviance, 1e-15);//summary(fit)
        assertEquals(0.42061674216227596, instance.residuals.overdispersion, 1e-15);//summary(fit)
    }
}
