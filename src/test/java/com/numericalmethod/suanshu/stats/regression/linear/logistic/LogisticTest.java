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
package com.numericalmethod.suanshu.stats.regression.linear.logistic;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class LogisticTest {

    /**
     *
    options(digits=22)
    y<-c(0, 1, 0,1, 1)
    x<-c(1.52,3.22,4.32,10.1034,12.1)
    fitted<-glm(y~x,family=binomial)
    summary(fitted)
    fitted$fitted
     */
    @Test
    public void test_0010() throws Exception {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{0, 1, 0, 1, 1}),
                new DenseMatrix(new double[][]{
                    {1.52},
                    {3.22},
                    {4.32},
                    {10.1034},
                    {12.1}
                }),
                true);
        Logistic instance = new Logistic(problem);

        assertArrayEquals(new double[]{0.594921916060311, -2.509122488691752},
                instance.beta.betaHat.toArray(), 1e-7);//summary(fitted)
        assertArrayEquals(new double[]{0.618661478214277, 2.599971091872917},
                instance.beta.stderr.toArray(), 1e-4);//summary(fitted)
        assertArrayEquals(new double[]{0.96163, -0.96506},
                instance.beta.z.toArray(), 1e-4);//summary(fitted)
        assertArrayEquals(new double[]{-0.6051266782179974, 1.4375529347677263, -1.2033963739478868, 0.2437343458371517, 0.1352768338017255},
                instance.residuals.devianceResiduals.toArray(), 1e-8);//summary(fitted)
        assertArrayEquals(new double[]{0.1673060808571557, 0.3558381735599254, 0.5152303340479065, 0.9707335893187666, 0.9908918221712956},
                instance.residuals.fitted.toArray(), 1e-8);//fitted$fitted

        assertEquals(3.958605822885457, instance.residuals.deviance, 1e-14);//summary(fitted)
        assertEquals(6.730116670092565, instance.residuals.nullDeviance, 1e-15);//summary(fitted)
        assertEquals(7.958605822885457, instance.AIC, 1e-14);//summary(fitted)
    }

    /**
    y<-c(0,0,0,0,1,1,1)
    x1<-c(9.2,1.1,4.3,6.3,3.34,5.1,5.5)
    x2<-c(1.23,0.3,8.77,0.3,2.37,8.3,1.9)
    x3<-c(0.2,0.332,6.78,8.6,1.458,11.8,7.8)
    fitted<-glm(y~x1+x2+x3,family=binomial)
    summary(fitted)
    fitted$fitted
     */
    @Test
    public void test_0020() throws Exception {
        LMProblem problem = new LMProblem(
                new DenseVector(new double[]{0, 0, 0, 0, 1, 1, 1}),
                new DenseMatrix(new double[][]{
                    {9.2, 1.23, 0.2},
                    {1.1, 0.3, 0.332},
                    {4.3, 8.77, 6.78},
                    {6.3, 0.3, 8.6},
                    {3.34, 2.37, 1.458},
                    {5.1, 8.3, 11.8},
                    {5.5, 1.9, 7.8}
                }),
                true);
        Logistic instance = new Logistic(problem);

        assertArrayEquals(new double[]{-0.26675542154890985, -0.01308051082454171, 0.23414822651282638, -0.28602993928137477},
                instance.beta.betaHat.toArray(), 1e-7);//summary(fitted)
        assertArrayEquals(new double[]{0.54146017793580548, 0.28408879964905465, 0.29990352180121538, 2.22952383725227055},
                instance.beta.stderr.toArray(), 1e-6);//summary(fitted)
        assertArrayEquals(new double[]{-0.49266, -0.04604, 0.78075, -0.12829},
                instance.beta.z.toArray(), 1e-5);//summary(fitted)
        assertArrayEquals(new double[]{-0.3590313779397609, -0.9715435547108207, -1.1943280998397887, -1.1957932737149275, 1.5604661899163597, 0.7888518842954615, 1.1569613363422346},
                instance.residuals.devianceResiduals.toArray(), 1e-7);//summary(fitted)
        assertArrayEquals(new double[]{0.0624186627547027, 0.3762143251165861, 0.5099313545985285, 0.5107887001135342, 0.2959610666384316, 0.7326086307815295, 0.5120772599967988},
                instance.residuals.fitted.toArray(), 1e-7);//fitted$fitted

        assertEquals(8.32504313159363, instance.residuals.deviance, 1e-14);//summary(fitted)
        assertEquals(9.56071346580660, instance.residuals.nullDeviance, 1e-14);//summary(fitted)
        assertEquals(16.32504313159363, instance.AIC, 1e-14);//summary(fitted)
    }
}
