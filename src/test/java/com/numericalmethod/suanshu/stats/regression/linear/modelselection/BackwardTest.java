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
package com.numericalmethod.suanshu.stats.regression.linear.modelselection;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Binomial;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Gaussian;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class BackwardTest {

    /**
     *
    options(digits=22)
    x1=c(1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.8, 9.2)
    x2=c(3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.3)
    x3=c(-8, -5, -3, 0.2, 2.3, 6.4, 9.8, 13.1)
    x4<-c(1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119)
    x5<-c( 0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552)
    x6<-c(-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560)
    
    #y<-3*x1 + 5.2*x2 - 6.9*x3
    y<-c(75.74, 68.06, 63.14, 39.34, 32.27, 19.12, 20.04, 46.76)
    fit = glm(y~x1+x2+x3-1,family="gaussian")
    summary(fit)
    fit$fitted
     */
    @Test
    public void test_0010() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.8, 9.2},
                    {3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.3},
                    {-8, -5, -3, 0.2, 2.3, 6.4, 9.8, 13.1},
                    {1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119},
                    {0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552},
                    {-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560}});

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{75.74, 68.06, 63.14, 39.34, 32.27, 19.12, 20.04, 46.76}),
                Xt.t(),
                true,
                new Gaussian());
        Backward instance = new Backward(problem, 0.05);

        assertArrayEquals(new double[]{2.62404509616778636, 5.05260439883254620, -6.68537962678411191, 2.67057616779516360},
                instance.getModel().beta.betaHat.toArray(), 1e-14);
        assertArrayEquals(new double[]{0.19371708019586653, 0.03407157484932706, 0.08947914134234226, 1.03013437761952042},
                instance.getModel().beta.stderr.toArray(), 1e-13);
        assertArrayEquals(new double[]{13.54576, 148.29383, -74.71439, 2.59245},
                instance.getModel().beta.z.toArray(), 1e-5);
        assertArrayEquals(new double[]{75.73320588335034, 68.12436780786379, 62.97461564787976, 39.27013301560766, 32.52778930864276, 19.21765501879559, 19.76844168105992, 46.85379163680017},
                instance.getModel().residuals.fitted.toArray(), 1e-13);
        assertEquals(0.1949553762102486, instance.getModel().residuals.deviance, 1e-14);
        assertEquals(0.04873884405256215, instance.getModel().residuals.overdispersion, 1e-15);
        assertArrayEquals(new double[]{0.006794116649658122, -0.064367807863789039, 0.165384352120241829, 0.069866984392348286, -0.257789308642756509, -0.097655018795585136, 0.271558318940083865, -0.093791636800176548},
                instance.getModel().residuals.devianceResiduals.toArray(), 1e-13);
        assertEquals(2.987607505406562, instance.getModel().AIC, 1e-10);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, instance.getFactors());
    }

    /**
     * Without intercept.
     * 
    options(digits=22)
    x1=c(1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.8, 9.2)
    x2=c(3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.3)
    x3=c(-8, -5, -3, 0.2, 2.3, 6.4, 9.8, 13.1)
    x4<-c(1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119)
    x5<-c( 0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552)
    x6<-c(-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560)

    y<-3*x1 + 5.2*x2 - 6.9*x3
    fit = glm(y~x1+x2+x3-1,family="gaussian")
    summary(fit)
    fit$fitted
     */
    @Test
    public void test_0020() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.8, 9.2},
                    {3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.3},
                    {-8, -5, -3, 0.2, 2.3, 6.4, 9.8, 13.1},
                    {1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119},
                    {0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552},
                    {-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560}});

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{75.74, 68.06, 63.14, 39.34, 32.27, 19.12, 19.74, 47.97}),
                Xt.t(),
                false,
                new Gaussian());
        Backward instance = new Backward(problem, 0.05);

        assertArrayEquals(new double[]{3, 5.2, -6.9},
                instance.getModel().beta.betaHat.toArray(), 1e-14);
        assertArrayEquals(new double[]{0, 0, 0},
                instance.getModel().beta.stderr.toArray(), 1e-14);
//        assertArrayEquals(new double[]{1786125171110555.0, 5184533478410349.0, -10940469607968536.0}), instance.getModel().beta.z, 1e10));//invalid when stderr = 0
        assertArrayEquals(new double[]{75.74, 68.06, 63.14, 39.34, 32.27, 19.12, 19.74, 47.97},
                instance.getModel().residuals.fitted.toArray(), 1e-13);
        assertEquals(0, instance.getModel().residuals.deviance, 1e-14);
        assertEquals(0, instance.getModel().residuals.overdispersion, 1e-15);
        assertArrayEquals(new double[]{0, 0, 0, 0, 0, 0, 0, 0},
                instance.getModel().residuals.devianceResiduals.toArray(), 1e-13);
        /**
         * In this case, the variance is too small.
         * It raise numerical inaccuracy so the AIC is unreliable.
         * (AIC involves a term which the variance is in the denominator).
         * Speaking intuitively, when the variance is so small, you have a perfect fit and you don't need AIC to do getModel selection -
         * you already have a perfect getModel.
         */
//        assertEquals(-490.0586662752726, instance.getModel().AIC, 1e-15);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, instance.getFactors());
    }

    /**
     *
    options(digits=22)
    x1=c(3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2)
    x2=c(1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2)
    x3<-c(-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560)
    
    #y is very similar to x2
    y<-c(0,1,1,0,1,0,1,1)
    fit = glm(y~x2, family="binomial")
    summary(fit)
    fit$fitted
     */
    @Test
    public void test_0030() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2},
                    {1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2},
                    {-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560}
                });

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{0, 1, 1, 0, 1, 0, 1, 1}),
                Xt.t(),
                true,
                new Binomial());
        Backward instance = new Backward(problem, 0.222);

        assertArrayEquals(new double[]{2.339453236843583, -2.782847247534908},
                instance.getModel().beta.betaHat.toArray(), 1e-6);
        assertArrayEquals(new double[]{1.911885258172989, 2.834896873746017},
                instance.getModel().beta.stderr.toArray(), 1e-6);
        assertArrayEquals(new double[]{1.22364, -0.98164},
                instance.getModel().beta.z.toArray(), 1e-5);
        assertArrayEquals(new double[]{0.44782841414471247, 0.94437236963462079, 0.62066865398529736, 0.07250028973841405, 0.50612385293628015, 0.80661579872954681, 0.73244576351188140, 0.86944485731924726},
                instance.getModel().residuals.fitted.toArray(), 1e-6);
        assertEquals(7.95733696534099, instance.getModel().residuals.deviance, 1e-13);
        assertEquals(1.0, instance.getModel().residuals.overdispersion, 1e-15);
        //TODO
        assertArrayEquals(new double[]{-1.089859, 0.338333, 0.976686, -0.387976, 1.167025, -1.812775, 0.789134, 0.528962},
                instance.getModel().residuals.devianceResiduals.toArray(), 1e-6);
        assertEquals(11.95733696534099, instance.getModel().AIC, 1e-14);
        assertArrayEquals(new int[]{0, 2, 0}, instance.getFactors());
    }

    /**
     *
    options(digits=22)
    x1=c(1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.9, 9.2)
    x2=c(3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2)
    x3=c(1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2)
    x4<-c(1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119)
    x5<-c(0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552)
    x6<-c(-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560)

    #y is very similar to x2
    y<-c(0,1,1,0,1,0,1,1)
    fit = glm(y~x2, family="binomial")
    summary(fit)
    fit$fitted
     */
    @Test(expected = SingleFactorSelection.ModelNotFound.class)
    public void test_0040() {
        Matrix Xt = new DenseMatrix(new double[][]{
                    {1.3, 2, 3.4, 5.6, 5.3, 7.4, 7.9, 9.2},
                    {3.2, 5.3, 6.2, 4.6, 6.2, 7.9, 12.3, 21.2},
                    {1.1, 2.4, 1.4, 0.1, 1.2, 1.8, 1.62, 2},
                    {1.1869843, 0.3584140, -2.0058741, 0.7554718, -1.3300818, -0.3357401, -0.6884670, 1.0674119},
                    {0.5617266, 0.9333335, 0.2278393, 0.6291345, 0.4491227, 0.3592713, 0.5380655, 0.6780552},
                    {-0.3360348, -0.2853039, 10.1616588, 0.7067691, 0.8530748, 3.9728762, -5.1659371, 1.8891560}
                });

        GLMProblem problem = new GLMProblem(
                new DenseVector(new double[]{0, 1, 1, 0, 1, 0, 1, 1}),
                Xt.t(),
                true,
                new Binomial());
        Backward instance = new Backward(problem, 0.222);

        instance.getModel();

        /*
         * the following expected values are from R
         */ //        assertArrayEquals(new DenseVector(new double[]{-7.691382531028652, 5.870632166482913, -8.663823756061754, -28.947644594170185, 84.490757574226251, -38.254082707591259}), instance.getModel().beta.betaHat, 1e-15));
        //        assertArrayEquals(new DenseVector(new double[]{59115.808627243481169, 24727.330863713759754, 96246.013017172939726, 71084.177331602943013, 568541.028277326957323, 509418.102722537121736}), instance.getModel().beta.stderr, 1e-15));
        //        assertArrayEquals(new DenseVector(new double[]{-0.00013, 0.00024, -0.00009, -0.00041, 0.00015}), instance.getModel().beta.z, 1e-5));
        //        assertArrayEquals(new DenseVector(new double[]{5.664758409478923e-12, 9.999999999882496e-01, 9.999999999999998e-01, 4.170592389362525e-11, 9.999999999334038e-01, 2.759979749310325e-11, 9.999999999781852e-01, 9.999999999772234e-01}), instance.getModel().residuals.fitted, 1e-15));
        //        assertEquals(3.958167127312598e-10, instance.getModel().residuals.deviance, 1e-15);
        //        assertEquals(1, instance.getModel().residuals.overdispersion, 1e-15);
        //        assertArrayEquals(new DenseVector(new double[]{-3.365947700375439e-06, 4.847757924640771e-06, 2.107342425544701e-08, -9.133013961270854e-06, 1.154088922016910e-05, -7.429629910377861e-06, 6.605266415614401e-06, 6.749288465391093e-06}), instance.getModel().residuals.devianceResiduals, 1e-15));
        //        assertEquals(12.00000000039582, instance.getModel().AIC, 1e-15);
        //        assertArrayEquals(new int[]{0, 2, 0, 0, 0, 0}, instance.getFactors());
    }
}
