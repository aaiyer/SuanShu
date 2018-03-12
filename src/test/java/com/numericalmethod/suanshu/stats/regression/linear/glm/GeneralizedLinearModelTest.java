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
package com.numericalmethod.suanshu.stats.regression.linear.glm;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Gamma;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Poisson;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Binomial;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Gaussian;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.InverseGaussian;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Sqrt;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class GeneralizedLinearModelTest {

    /**
     *
    options(digits=22)
    y<-c(2,1,4,5,7)
    x1<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(2.11,4.32,1.23,8.43,7.31)
    fit<-glm(y~x1+x2,family=poisson(link="sqrt"))
    summary(fit)
     */
    @Test
    public void test_0010() {
        GLMProblem problem = new GLMProblem(new DenseVector(new double[]{2, 1, 4, 5, 7}),
                new DenseMatrix(new double[][]{
                    {1.52, 2.11},
                    {3.22, 4.32},
                    {4.32, 1.23},
                    {10.1034, 8.43},
                    {12.1, 7.31}
                }),
                true,
                new Poisson(new Sqrt()));//override the canonical link function
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{0.220000, -0.164378, 1.261568},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{1.560324, 1.587231, 4.039222, 4.404148, 7.409074},
                instance.residuals.fitted.toArray(), 1e-6);//fit$fitted
        assertArrayEquals(new double[]{0.337139, -0.500480, -0.019547, 0.277861, -0.151702},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.106852, 0.155915, 0.435700},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{2.058915, -1.054278, 2.895493},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(0.4647449603635064, instance.residuals.deviance, 1e-12);
        assertEquals(1.0, instance.residuals.overdispersion, 1e-15);
        assertEquals(21.6323883675599, instance.AIC, 1e-12);
    }

    /*
    options(digits=22)
    y<-c(2,1,4,5.3,7.4)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=Gamma(link="inverse"))
    summary(fit)
     */
    @Test
    public void test_0020() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{2, 1, 4, 5.3, 7.4}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true, new Gamma());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{-0.034346, 0.544317},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{2.032063, 2.305623, 2.525627, 5.068341, 7.768347},
                instance.residuals.fitted.toArray(), 1e-5);
        assertArrayEquals(new double[]{-0.015862, -0.733585, 0.497916, 0.045029, -0.048187},
                instance.residuals.devianceResiduals.toArray(), 1e-5);
        assertArrayEquals(new double[]{0.014836, 0.158602},
                instance.beta.stderr.toArray(), 1e-5);
        assertArrayEquals(new double[]{-2.315118, 3.431978},
                instance.beta.z.toArray(), 1e-5);
        assertEquals(0.790668071251061, instance.residuals.deviance, 1e-15);
        assertEquals(0.2220129776387595, instance.residuals.overdispersion, 1e-11);
        assertEquals(22.59673627939517, instance.AIC, 1e-13);
    }

    /*
    options(digits=22)
    y<-c(0.2, 1, 0.52,1, 1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=Gamma(link=inverse))
    summary(fit)
     */
    @Test
    public void test_0030() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{0.2, 1, 0.52, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true, new Gamma());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{-0.097741, 2.079081},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.517997, 0.566779, 0.603558, 0.916114, 1.115552},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{-0.821893, 0.627009, -0.145403, 0.088912, -0.107392},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.084639, 0.810619},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{-1.154790, 2.564806},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(1.109228903354208, instance.residuals.deviance, 1e-15);
        assertEquals(0.3331297374957642, instance.residuals.overdispersion, 1e-11);
        assertEquals(8.31836478746396, instance.AIC, 1e-13);
    }

    /*
    options(digits=22)
    y<-c(1, 1, 0,1, 1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fit<-glm(y~x,family=binomial(link="logit"))
    summary(fit)
     */
    @Test
    public void test_0040() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{1, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true, new Binomial());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{0.165250, 0.487592},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.676727, 0.734914, 0.768787, 0.896338, 0.923234},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.883727, 0.784859, -1.711383, 0.467841, 0.399681},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.328662, 1.917517},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.502798, 0.254283},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(4.70442824788789, instance.residuals.deviance, 1e-14);
        assertEquals(1.0, instance.residuals.overdispersion, 1e-15);
        assertEquals(8.70442824788789, instance.AIC, 1e-14);
    }

    /*
    options(digits=22)
    y<-c(1.8,5,0.6,-1,1)
    x<-c(3.52,3.72,4.32,10.1034,12.1)
    fit<-glm(y~x,family=gaussian(link = "identity"))
    summary(fit)
     */
    @Test
    public void test_0050() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{1.8, 5, 0.6, -1, 1}),
                new DenseMatrix(new double[][]{
                    {3.52},
                    {3.72},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true, new Gaussian());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{-0.312683, 3.591448},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{2.490804, 2.428267, 2.240658, 0.432287, -0.192016},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{-0.690804, 2.571733, -1.640658, -1.432287, 1.192016},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.259949, 1.991212},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{-1.202865, 1.803649},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(13.25512387005473, instance.residuals.deviance, 1e-14);
        assertEquals(4.4183746233515775, instance.residuals.overdispersion, 1e-15);
        assertEquals(25.06411669414735, instance.AIC, 1e-15);
    }

    /*
    options(digits=22)
    y<-c(1.8,5,0.6,12,1)
    x<-c(3.52,3.72,4.32,10.1034,12.1)
    fit<-glm(y~x,family=inverse.gaussian(link = "1/mu^2"),start=c(1,1))
    summary(fit)
     */
    @Test
    public void test_0060() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{1.8, 5, 0.6, 12, 1}),
                new DenseMatrix(new double[][]{
                    {3.52},
                    {3.72},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true, new InverseGaussian());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{-0.009689, 0.141971},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{3.044813, 3.072538, 3.160491, 4.763204, 6.358953},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{-0.304724, 0.280546, -1.045907, 0.438588, -0.842741},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.017133, 0.179837},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{-0.565523, 0.789443},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(2.168056534543066, instance.residuals.deviance, 1e-15);
        assertEquals(0.32898268455539764, instance.residuals.overdispersion, 1e-10);
        assertEquals(28.52526838023173, instance.AIC, 1e-14);
    }

    /*
    options(digits=22)
    y<-c(4,1,4,5,7)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(2.11,4.32,1.23,8.43,7.31)
    fit<-glm(y~x+x2,family=poisson(link="log"))
    summary(fit)
     */
    @Test
    public void test_0070() {
        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{4, 1, 4, 5, 7}),
                new DenseMatrix(new double[][]{
                    {1.52, 2.11},
                    {3.22, 4.32},
                    {4.32, 1.23},
                    {10.1034, 8.43},
                    {12.1, 7.31}
                }),
                true, new Poisson());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{0.159667, -0.126816, 0.952348},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{2.528082, 2.505862, 4.419947, 4.465919, 7.080190},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.852544, -1.083724, -0.203045, 0.247925, -0.030194},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.107230, 0.154416, 0.470909},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{1.489023, -0.821261, 2.022359},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(2.004895830058038, instance.residuals.deviance, 1e-15);
        assertEquals(1.0, instance.residuals.overdispersion, 1e-15);
        assertEquals(23.8245863701111, instance.AIC, 1e-15);
    }

    /*
    options(digits=22)
    y<-c(0, 1, 1, 0, 1, 1, 0, 0)
    x<-c(0.2, 1.2, 1.3, 1.02, 2.02, 0.9, 0.21, .31)
    fit<-glm(y~x,family=binomial(link="logit"))
    summary(fit)
     */
    @Test
    public void test_0080() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {0.2, 1.2, 1.3, 1.02, 2.02, 0.9, 0.21, .31},});

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{0, 1, 1, 0, 1, 1, 0, 0}),
                Xt.t(),
                true, new Binomial());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{6.932054, -6.328706},
                instance.beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{0.007088, 0.879718, 0.936014, 0.677431, 0.999535, 0.477551, 0.007592, 0.015071},
                instance.residuals.fitted.toArray(), 1e-6);
        assertArrayEquals(new double[]{-0.119271, 0.506269, 0.363662, -1.504285, 0.030484, 1.215800, -0.123462, -0.174276},
                instance.residuals.devianceResiduals.toArray(), 1e-6);
        assertArrayEquals(new double[]{5.877248, 5.979806},
                instance.beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{1.179473, -1.058346},
                instance.beta.z.toArray(), 1e-6);
        assertEquals(4.190372679824502, instance.residuals.deviance, 1e-15);
        assertEquals(1.0, instance.residuals.overdispersion, 1e-15);
        assertEquals(8.1903726798245, instance.AIC, 1e-14);
    }

    /*
    options(digits=22)
    x1=c(1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.9, 9.2)
    x2=c(3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2)
    x3=c(1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2)
    x4<-c(1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119)
    x5<-c(0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552)

    y<-c(0,1,1,0,1,0,1,1)
    fit<-glm(y~x1+x2+x3+x4+x5,family=binomial(link="logit"))
    summary(fit)
     *
     * For this particular example,
     * we do not expect our answers to be the same as those in R.
     *
     * The IRLS convergence fails and throws an exception.
     *
     * There is a perfect fitting in this example (the deviances are 0).
     */
    @Test
    public void test_0090() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.9, 9.2},
                    {3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2},
                    {1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2},
                    {1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119},
                    {0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552},});

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{0, 1, 1, 0, 1, 0, 1, 1}),
                Xt.t(),
                true,
                new Binomial());
        GeneralizedLinearModel instance = new GeneralizedLinearModel(problem);

        assertArrayEquals(new double[]{-7.691382531028652, 5.870632166482913, -8.663823756061754, -28.947644594170185, 84.490757574226251, -38.254082707591259},
                instance.beta.betaHat.toArray(), 1e1);
//        assertArrayEquals(new double[]{59115.808627243481169, 24727.330863713759754, 96246.013017172939726, 71084.177331602943013, 568541.028277326957323, 509418.102722537121736},
//                instance.beta.stderr.toArray(), 1e-15);
//        assertArrayEquals(new double[]{-0.00013, 0.00024, -0.00009, -0.00041, 0.00015},
//                instance.beta.z.toArray(), 1e-5);
        assertArrayEquals(new double[]{5.664758409478923e-12, 9.999999999882496e-01, 9.999999999999998e-01, 4.170592389362525e-11, 9.999999999334038e-01, 2.759979749310325e-11, 9.999999999781852e-01, 9.999999999772234e-01},
                instance.residuals.fitted.toArray(), 1e-10);
        assertEquals(3.958167127312598e-10, instance.residuals.deviance, 1e-9);
        assertEquals(1.0, instance.residuals.overdispersion, 1e-15);
//        assertArrayEquals(new DenseVector(new double[]{-3.365947700375439e-06, 4.847757924640771e-06, 2.107342425544701e-08, -9.133013961270854e-06, 1.154088922016910e-05, -7.429629910377861e-06, 6.605266415614401e-06, 6.749288465391093e-06}), instance.residuals.devianceResiduals, 1e-15));
        assertEquals(12.00000000039582, instance.AIC, 1e-9);
    }
}
