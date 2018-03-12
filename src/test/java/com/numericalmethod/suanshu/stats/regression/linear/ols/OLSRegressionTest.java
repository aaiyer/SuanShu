/*
 * Copyright (c)
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
package com.numericalmethod.suanshu.stats.regression.linear.ols;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class OLSRegressionTest {

    /**
     * R code for the OLS regression
     *
    options(digits=22)
    y<-c(2.32, 0.452, 4.53, 12.34, 32.2)
    x1<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(2.23,6.34,12.2,43.2,2.12)
    x3<-c(4.31,3.46,23.1,22.3,3.27)
    fitted<-lm(y~x1+x2+x3)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    cooks.distance(fitted)
    hatvalues(fitted)
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     *
     * There is no corresponding code for Hadi's measure in R
     */
    @Test
    public void test_OLSRegression_0010() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{2.32, 0.452, 4.53, 12.34, 32.2}),
                new DenseMatrix(new double[][]{
                    {1.52, 2.23, 4.31},
                    {3.22, 6.34, 3.46},
                    {4.32, 12.2, 23.1},
                    {10.1034, 43.2, 22.3},
                    {12.1, 2.12, 3.27}
                }),
                true);

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(
                new double[]{3.05526367241960983, -0.34757163218629139, 0.01921862042814120, -4.35792401241910454},
                instance.beta.betaHat.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(
                new double[]{0.49199823389061365, 0.1945400045869793, 0.29920689101703202, 3.95885424688395782},
                instance.beta.stderr.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(
                new double[]{6.20991, -1.78663, 0.06423, -1.10080},
                instance.beta.t.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(
                new double[]{2.7261757160714390, -2.8909172913923262, -0.5143912716509186, 0.4158922993950584, 0.2632405475767475},
                instance.residuals.residuals.toArray(), 1e-14);//summary(fitted)
        assertEquals(0.9759536870161641398, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(0.903814748064656559, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(4.036867249675673, instance.residuals.stderr, 1e-14);//summary(fitted)
        assertEquals(13.52880567972044368, instance.residuals.f, 1e-13);//summary(fitted)

        assertArrayEquals(
                new double[]{-0.4061757160714392, 3.3429172913923262, 5.0443912716509187, 11.9241077006049423, 31.9367594524232565},
                instance.residuals.fitted.toArray(), 1e-14);//fitted$fitted.value
        assertArrayEquals(
                new double[]{0.5439433910930228, 0.4871594026876104, 0.9837632820976907, 0.9893861530221550, 0.9957477710995214},
                instance.residuals.leverage.toArray(), 1e-14);//hatvalues(fitted)
        assertArrayEquals(
                new double[]{1, -1, -1, 1, 1},
                instance.residuals.standardized().toArray(), 1e-12);//rstandard(fitted)
        assertArrayEquals(
                new double[]{0, 0, 0, 0, 0},
                instance.residuals.studentized().toArray(), 1e-15);//rstudent(fitted)

        assertEquals(16.2962971915040313, instance.residuals.RSS, 1e-12);//anova(fitted)
        assertEquals(677.7046112, instance.residuals.TSS, 1e-15);//sum(anova(fitted)$"Sum Sq")

        assertArrayEquals(
                new double[]{0, 0, 0, 0, 0},
                instance.diagnostics.DFFITS.toArray(), 1e-15);//dffits(fitted)
        assertArrayEquals(
                new double[]{8.546417, 9.160788, 64.654821, 97.259457, 238.187865},
                instance.diagnostics.Hadi.toArray(), 1e-6);//TODO: to check
        assertArrayEquals(
                new double[]{0.2981775619898822, 0.2374809079276461, 15.1472004381775953, 23.3041364523008383, 58.5426957487815756},
                instance.diagnostics.cookDistances.toArray(), 1e-10);//cooks.distance(fitted)

        assertEquals(30.0968853490821, instance.informationCriteria.AIC, 1e-12);//AIC(fitted)
        assertEquals(28.14407491125262, instance.informationCriteria.BIC, 1e-12);//AIC(fitted,k=log(length(y)))
    }

    /**
     * R code for the OLS regression
    options(digits=22)
    y<-c(6,5,7,10)
    x<-1:4
    fitted<-lm(y~x)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    cooks.distance(fitted)
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    hatvalues(fitted) #leverage
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     *
     * There is no corresponding code for Hadi's measure in R.
     */
    @Test
    public void test_OLSRegression_0020() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{6, 5, 7, 10}),
                new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                }),
                true);

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(new double[]{1.4, 3.5},
                instance.beta.betaHat.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(new double[]{0.648074069840786, 1.774823934929886},
                instance.beta.stderr.toArray(), 1e-14);//summary(fitted)
        assertArrayEquals(new double[]{2.16025, 1.97203},
                instance.beta.t.toArray(), 1e-4);//summary(fitted)
        assertArrayEquals(new double[]{1.1, -1.3, -0.7, 0.9},
                instance.residuals.residuals.toArray(), 1e-15);//summary(fitted)

        assertEquals(0.6999999999999998446, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(0.5499999999999998224, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(1.449137674618944, instance.residuals.stderr, 1e-15);//summary(fitted)
        assertEquals(4.66666666666666341, instance.residuals.f, 1e-14);//summary(fitted)

        assertArrayEquals(new double[]{4.9, 6.3, 7.7, 9.1},
                instance.residuals.fitted.toArray(), 1e-15);//fitted$fitted.value
        assertArrayEquals(new double[]{0.7, 0.3, 0.3, 0.7},
                instance.residuals.leverage.toArray(), 1e-15);//hatvalues(fitted)
        assertArrayEquals(new double[]{1.3858697343671664, -1.0722219284950192, -0.5773502691896256, 1.1338934190276815},
                instance.residuals.standardized().toArray(), 1e-15);//rstandard(fitted)
        assertArrayEquals(new double[]{4.919349550499549, -1.162755348299891, -0.447213595499958, 1.341640786499874},
                instance.residuals.studentized().toArray(), 1e-13);//rstudent(fitted)

        assertEquals(4.2, instance.residuals.RSS, 1e-15);//anova(fitted)
        assertEquals(14, instance.residuals.TSS, 1e-15);// sum(anova(fitted)$"Sum Sq")

        assertArrayEquals(new double[]{7.514430561703726, -0.761202056899856, -0.292770021884560, 2.049390153191920},
                instance.diagnostics.DFFITS.toArray(), 1e-13);//dffits(fitted)
        assertArrayEquals(new double[]{5.031215, 2.352305, 0.805930, 3.926254},
                instance.diagnostics.Hadi.toArray(), 1e-6);//TODO: to check
        assertArrayEquals(new double[]{2.2407407407407414, 0.2463556851311954, 0.0714285714285714, 1.5000000000000000},
                instance.diagnostics.cookDistances.toArray(), 1e-14);//cooks.distance(fitted)

        assertEquals(17.54666892231511, instance.informationCriteria.AIC, 1e-15);//AIC(fitted)
        assertEquals(15.70555200567479, instance.informationCriteria.BIC, 1e-12);//AIC(fitted,k=log(length(y)))
    }

    /**
     * Case without intercept
     *
     * R code for the OLS regression
    options(digits=22)
    y<-c(6,5,7,10)
    x<-1:4
    fitted<-lm(y~x-1)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    cooks.distance(fitted)
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    hatvalues(fitted) #leverage
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     *
     * There is no corresponding code for Hadi's measure in R.
     */
    @Test
    public void test_OLSRegression_0030() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{6, 5, 7, 10}),
                new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                }),
                false);

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(new double[]{2.566666666666666},
                instance.beta.betaHat.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(new double[]{0.370685051502495},
                instance.beta.stderr.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{6.92412},
                instance.beta.t.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{3.4333333333333318, -0.1333333333333334, -0.7000000000000002, -0.2666666666666669},
                instance.residuals.residuals.toArray(), 1e-14);//summary(fitted)

        assertEquals(0.9411111111111112315, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(0.921481481481481679, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(2.030325644378807, instance.residuals.stderr, 1e-15);//summary(fitted)
        assertEquals(47.94339622641513898, instance.residuals.f, 1e-10);//summary(fitted)

        assertArrayEquals(new double[]{2.566666666666668, 5.133333333333334, 7.700000000000000, 10.266666666666667},
                instance.residuals.fitted.toArray(), 1e-14);//fitted$fitted.values
        assertArrayEquals(new double[]{0.03333333333333333, 0.13333333333333330, 0.29999999999999993, 0.53333333333333321},
                instance.residuals.leverage.toArray(), 1e-15);//hatvalues(fitted)
        assertArrayEquals(new double[]{1.71993446140783135, -0.07054187351886934, -0.41208169184606736, -0.19226473854584400},
                instance.residuals.standardized().toArray(), 1e-15);//rstandard(fitted)
        assertArrayEquals(new double[]{11.8934155453062562, -0.0576450268222823, -0.3464101615137757, -0.1579597007357581},
                instance.residuals.studentized().toArray(), 1e-13);//rstudent(fitted)

        assertEquals(12.36666666666666, instance.residuals.RSS, 1e-12);//anova(fitted)
        assertEquals(210, instance.residuals.TSS, 1e-12);//sum(anova(fitted)$"Sum Sq")

        assertArrayEquals(new double[]{2.20855182176585085, -0.02261023974064593, -0.22677868380553651, -0.16886602316301444},
                instance.diagnostics.DFFITS.toArray(), 1e-13);//dffits(fitted)
//        assertArrayEquals(new Vector(new double[]{5.031215, 2.352305, 0.805930, 3.926254}), instance.diagnostics.Hadi, 1e-6));//TODO: to check
        assertArrayEquals(new double[]{0.10200601901856021, 0.00076556244916187, 0.07277628032345021, 0.04224654821497338},
                instance.diagnostics.cookDistances.toArray(), 1e-14);//cooks.distance(fitted)

        assertEquals(19.86634954493893, instance.informationCriteria.AIC, 1e-12);//AIC(fitted)
        assertEquals(18.63893826717872, instance.informationCriteria.BIC, 1e-15);//AIC(fitted,k=log(length(y)))
    }

    /**
     * Case without intercept
     *
     * R code for the above Diagnostic statistics
    options(digits=22)
    y<-c(0,0,0,1)
    x<-c(1,0,0,0)
    fitted<-lm(y~x-1)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    cooks.distance(fitted)
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    hatvalues(fitted) #leverage
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     *
     * There is no corresponding code for Hadi's measure in R.
     */
    @Test
    public void test_OLSRegression_0040() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{0, 0, 0, 1}),
                new DenseMatrix(new double[][]{
                    {1},
                    {0},
                    {0},
                    {0}
                }),
                false);

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(new double[]{0},
                instance.beta.betaHat.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(new double[]{0.577350269189626},
                instance.beta.stderr.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{0},
                instance.beta.t.toArray(), 1e-15);//summary(fitted)
        assertArrayEquals(new double[]{0, 0, 0, 1},
                instance.residuals.residuals.toArray(), 1e-15);//summary(fitted)

        assertEquals(0, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(-0.3333333333333332593, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(0.577350269189626, instance.residuals.stderr, 1e-15);//summary(fitted)
        assertEquals(0, instance.residuals.f, 1e-10);//summary(fitted)

        assertArrayEquals(new double[]{0, 0, 0, 0},
                instance.residuals.fitted.toArray(), 1e-15);//fitted$fitted.values
        assertArrayEquals(new double[]{1, 0, 0, 0},
                instance.residuals.leverage.toArray(), 1e-15);//hatvalues(fitted)
        assertArrayEquals(new double[]{Double.NaN, 0, 0, 1.732050807568877},
                instance.residuals.standardized().toArray(), 1e-15);//rstandard(fitted)
//        assertArrayEquals(new Vector(new double[]{Double.NaN, 0, 0, Double.NaN}), instance.residuals.studentized(), 1e-15));//rstudent(fitted)

        assertEquals(1, instance.residuals.RSS, 1e-15);//anova(fitted)
        assertEquals(1, instance.residuals.TSS, 1e-12);//sum(anova(fitted)$"Sum Sq")

//        assertArrayEquals(new Vector(new double[]{Double.NaN, 0, 0, Double.NaN}), instance.diagnostics.DFFITS, 1e-13));//dffits(fitted)
//        assertArrayEquals(new Vector(new double[]{5.031215, 2.352305, 0.805930, 3.926254}), instance.diagnostics.Hadi, 1e-6));//TODO: to check
        assertArrayEquals(new double[]{Double.NaN, 0, 0, 0},
                instance.diagnostics.cookDistances.toArray(), 1e-14);//cooks.distance(fitted)

        assertEquals(9.80633082115782, instance.informationCriteria.AIC, 1e-14);//AIC(fitted)
        assertEquals(8.5789195433976, instance.informationCriteria.BIC, 1e-15);//AIC(fitted,k=log(length(y)))
    }

    /**
     * R code for the weighted OLS regression
    options(digits=22)
    w<-c(0.2, 0.4, 0.1, 0.3, 0.1)
    y<-c(2.32, 0.452, 4.53, 12.34, 32.2)
    x1<-c(1.52,3.22,4.32,10.1034,12.1)
    x2<-c(2.23,6.34,12.2,43.2,2.12)
    x3<-c(4.31,3.46,23.1,22.3,3.27)
    fitted<-lm(y~x1+x2+x3,weight=w)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    cooks.distance(fitted)
    hatvalues(fitted)
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     */
    @Test
    public void test_0050() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{2.32, 0.452, 4.53, 12.34, 32.2}),
                new DenseMatrix(new double[][]{
                    {1.52, 2.23, 4.31},
                    {3.22, 6.34, 3.46},
                    {4.32, 12.2, 23.1},
                    {10.1034, 43.2, 22.3},
                    {12.1, 2.12, 3.27}
                }),
                true,
                new DenseVector(new double[]{0.2, 0.4, 0.1, 0.3, 0.1}));

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(new double[]{3.1053332718685862, -0.3797216772416540, 0.1191154264341778, -5.6512849678621606},
                instance.beta.betaHat.toArray(), 1e-10);//summary(fitted)
        assertArrayEquals(new double[]{0.7071749168382593, 0.2579815580438225, 0.4280166360399592, 3.8765108505406451},
                instance.beta.stderr.toArray(), 1e-10);//summary(fitted)
        assertArrayEquals(new double[]{4.39118, -1.47189, 0.27830, -1.45783},
                instance.beta.t.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{1.6030685484559810, -1.2020399936694142, -0.4277665658300979, 0.1996795081160786, 0.2189102172413164},
                instance.residuals.wResiduals.toArray(), 1e-8);//summary(fitted)
        assertArrayEquals(new double[]{3.584570246939489, -1.900592109304864, -1.352716654891465, 0.364563236222374, 0.692254889564822},
                instance.residuals.residuals.toArray(), 1e-8);//fitted$residuals

        assertEquals(0.9544612410874238062, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(0.8178449643496952248, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(2.070146550717056, instance.residuals.stderr, 1e-10);//summary(fitted)
        assertEquals(6.986438687095588129, instance.residuals.f, 1e-10);//summary(fitted)

        assertArrayEquals(new double[]{-1.264570246939489, 2.352592109304864, 5.882716654891465, 11.975436763777626, 31.507745110435181},
                instance.residuals.fitted.toArray(), 1e-10);//fitted$fitted.values
        assertArrayEquals(new double[]{0.4003442472285486, 0.6628403048799489, 0.9573016107821252, 0.9906961047159552, 0.9888177323934220},
                instance.residuals.leverage.toArray(), 1e-10);//hatvalues(fitted)
        assertArrayEquals(new double[]{1.000000, -1.000000, -1.000000, 1.000000, 1.000000},
                instance.residuals.standardized().toArray(), 1e-10);//rstandard(fitted)
        assertArrayEquals(new double[]{-0.000000, 0.000000, -0.000000, -0.000000, 0.000000},
                instance.residuals.studentized().toArray(), 1e-10);//rstudent(fitted)

        assertEquals(4.2855067414457260, instance.residuals.RSS, 1e-10);//anova(fitted)
        assertEquals(94.1067970181819, instance.residuals.TSS, 1e-10);//sum(anova(fitted)$"Sum Sq")

        assertEquals(31.7532614266896, instance.informationCriteria.AIC, 1e-10);//AIC(fitted)
        assertEquals(29.8004509888601, instance.informationCriteria.BIC, 1e-10);//AIC(fitted,k=log(length(y)))

        assertArrayEquals(new double[]{-0.000000, 0.000000, -0.000000, -0.000000, 0.000000},
                instance.diagnostics.DFFITS.toArray(), 1e-6);//dffits(fitted)
        assertArrayEquals(new double[]{0.1669058644806885, 0.4914883914608584, 5.6050218071304121, 26.6204657960543862, 22.1068250014814751},
                instance.diagnostics.cookDistances.toArray(), 1e-6);//cooks.distance(fitted)
//        assertArrayEquals(new Vector(new double[]{10.659025, 8.000590, 26.598499, 110.519428, 92.472535}), instance.diagnostics.Hadi, 1e-6));//TODO: to check

    }

    /**
     * R code for the weighted OLS regression
    options(digits=22)
    w<-c(0.25,0.4,0.05,0.4)
    y<-c(6,5,7,10)
    x<-1:4
    fitted<-lm(y~x, weight=w)
    summary(fitted)
    fitted$fitted.value
    fitted$residuals
    dffits(fitted)
    rstandard(fitted)
    rstudent(fitted)
    cooks.distance(fitted)
    hatvalues(fitted)
    anova(fitted)
    sum(anova(fitted)$"Sum Sq")
    AIC(fitted)
    AIC(fitted,k=log(length(y))) # This is BIC
     */
    @Test
    public void test_0060() {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{6, 5, 7, 10}),
                new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                }),
                true,
                new DenseVector(new double[]{0.25, 0.4, 0.05, 0.4}));

        OLSRegression instance = new OLSRegression(problem);

        assertArrayEquals(new double[]{1.632947976878614, 2.979768786127164},
                instance.beta.betaHat.toArray(), 1e-12);//summary(fitted)
        assertArrayEquals(new double[]{0.626814583244666, 1.762797296495030},
                instance.beta.stderr.toArray(), 1e-12);//summary(fitted)
        assertArrayEquals(new double[]{2.60515, 1.69036}, instance.beta.t.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{0.6936416184971098, -0.7878275557991741, -0.1964637760577850, 0.3089161413690497},
                instance.residuals.wResiduals.toArray(), 1e-8);//summary(fitted)
        assertArrayEquals(new double[]{1.3872832369942196, -1.2456647398843927, -0.8786127167630060, 0.4884393063583813},
                instance.residuals.residuals.toArray(), 1e-8);//summary(fitted)

        assertEquals(0.7723864436470625483, instance.residuals.R2, 1e-15);//summary(fitted)
        assertEquals(0.6585796654705937669, instance.residuals.AR2, 1e-15);//summary(fitted)
        assertEquals(0.78607828817778, instance.residuals.stderr, 1e-15);//summary(fitted)
        assertEquals(6.786822859086659498, instance.residuals.f, 1e-12);//summary(fitted)

        assertArrayEquals(new double[]{4.612716763005780, 6.245664739884393, 7.878612716763006, 9.511560693641618},
                instance.residuals.fitted.toArray(), 1e-12);//fitted$fitted.values
        assertArrayEquals(new double[]{0.60693641618497096, 0.43930635838150300, 0.05202312138728324, 0.90173410404624288},
                instance.residuals.leverage.toArray(), 1e-15);//hatvalues(fitted)
        assertArrayEquals(new double[]{1.4074660798653360, -1.3384512277706537, -0.2566952388358888, 1.2536415892744883},
                instance.residuals.standardized().toArray(), 1e-12);//rstandard(fitted)
        assertArrayEquals(new double[]{10.2003060137706498, -2.9308879279567783, -0.1845769659634697, 1.9153907959191656},
                instance.residuals.studentized().toArray(), 1e-10);//rstudent(fitted)

        assertEquals(1.235838150289017, instance.residuals.RSS, 1e-15);//anova(fitted)
        assertEquals(5.42954545454546, instance.residuals.TSS, 1e-12);//sum(anova(fitted)$"Sum Sq")

        assertEquals(18.86793653595267, instance.informationCriteria.AIC, 1e-12);//AIC(fitted)
        assertEquals(17.02681961931235, instance.informationCriteria.BIC, 1e-12);//AIC(fitted,k=log(length(y)))

        assertArrayEquals(new double[]{12.6751583650214563, -2.5942996741159372, -0.0432391186987504, 5.8022339337348443},
                instance.diagnostics.DFFITS.toArray(), 1e-10);//dffits(fitted)
        assertArrayEquals(new double[]{1.529418238433875032, 0.701805816356590673, 0.001808024423076603, 7.210949663527999931},
                instance.diagnostics.cookDistances.toArray(), 1e-12);//cooks.distance(fitted)
//        assertArrayEquals(new Vector(new double[]{4.787988, 4.382444, 0.122895, 10.879600}), instance.diagnostics.Hadi, 1e-6));//TODO: to check
    }
}
