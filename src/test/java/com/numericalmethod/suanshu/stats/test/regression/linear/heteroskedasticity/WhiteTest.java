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
package com.numericalmethod.suanshu.stats.test.regression.linear.heteroskedasticity;

import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class WhiteTest {

    /**
     *
    x1 = c(1,2,3,4)
    x2 = c(1.1,-1.3,-0.7,0.9)
    #y <- 1 + x1 + x2 + rnorm(4)
    y = c(4.208556, 1.332570, 3.899288, 5.477989)
    lm1 = lm(y ~ x1 + x2)
    bptest(lm1, ~ x1*x2 + I(x1^2) + I(x2^2), studentize = TRUE)
     *
     * R will return BP same as the number of observations as in the auxiliary regression,
     * which has a residuals of exactly (0, ... 0). That is, R2 = 1.
     * R2 * n = n = observations;
     *
     * In our case, the auxiliary regression throws an exception because this is an NOT an over-constrained problem.
     * The regression is valid. The auxiliary regression is invalid.
     */
    @Test
    public void test_0010() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9});
//        Vector y = x1.add(x2).add(1);//1 + x1 + x2
        Vector y = new DenseVector(new double[]{4.208556, 1.332570, 3.899288, 5.477989});

        LMProblem problem = new LMProblem(y, CreateMatrix.cbind(x1, x2), true);
        OLSRegression ols = new OLSRegression(problem);

        White instance = new White(ols.residuals);

        assertEquals(4, instance.statistics(), 1e-3);
        assertEquals(0.5494, instance.pValue(), 1e-3);
    }

    /**
     *
    x1 = c(1,2,3,4,5)
    x2 = c(1.1,-1.3,-0.7,0.9,-5.2)
    #y <- 1 + x1 + x2 + rnorm(5)
    y = c(4.208556, 1.332570, 3.899288, 5.477989)
    lm1 = lm(y ~ x1 + x2)
    bptest(lm1, ~ x1*x2 + I(x1^2) + I(x2^2), studentize = TRUE)
     *
     * R will return BP same as the number of observations as in the auxiliary regression,
     * which has a residuals of exactly (0, ... 0). That is, R2 = 1.
     * R2 * n = n = observations;
     *
     * In our case, the auxiliary regression throws an exception because this is an NOT an over-constrained problem.
     * The regression is valid. The auxiliary regression is invalid.
     */
    @Test
    public void test_0020() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4, 5});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9, -5.2});
//        Vector y = x1.add(x2).add(1);//1 + x1 + x2
        Vector y = new DenseVector(new double[]{3.8055655, 2.2482719, 4.6966519, 5.3908669, -0.1810995});

        LMProblem problem = new LMProblem(y, CreateMatrix.cbind(x1, x2), true);
        OLSRegression ols = new OLSRegression(problem);

        White instance = new White(ols.residuals);

        assertEquals(5, instance.statistics(), 1e-3);
        assertEquals(0.4159, instance.pValue(), 1e-3);
    }

    /**
     *
    x1 = c(1,2,3,4,5,6)
    x2 = c(1.1,-1.3,-0.7,0.9,-5.2,1.9)
    #y <- 1 + x1 + x2 + rnorm(6)
    y = c(2.427100 , 1.177839 , 3.017418 , 5.398070, -1.745487,  9.189569)
    lm1 = lm(y ~ x1 + x2)
    bptest(lm1, ~ x1*x2 + I(x1^2) + I(x2^2), studentize = TRUE)
     *
     */
    @Test
    public void test_0030() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9, -5.2, 1.9});
//        Vector y = x1.add(x2).add(1);//1 + x1 + x2
        Vector y = new DenseVector(new double[]{2.427100, 1.177839, 3.017418, 5.398070, -1.745487, 9.189569});

        LMProblem problem = new LMProblem(y, CreateMatrix.cbind(x1, x2), true);
        OLSRegression ols = new OLSRegression(problem);

        White instance = new White(ols.residuals);

        assertEquals(6, instance.statistics(), 1e-3);
        assertEquals(0.3062, instance.pValue(), 1e-3);
    }

    /**
     *
    x1 = c(1,2,3,4,5,6,7)
    x2 = c(1.1,-1.3,-0.7,0.9,-5.2,1.9,5.5)
    y <- 1 + x1 + x2 + rnorm(7)
    y = c(5.269650, 2.601217, 2.041629, 5.650300, 1.932326, 8.967327, 12.512230)
    lm1 = lm(y ~ x1 + x2)
    bptest(lm1, ~ x1*x2 + I(x1^2) + I(x2^2), studentize = TRUE)
     *
     */
    @Test
    public void test_0040() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9, -5.2, 1.9, 5.5});
//        Vector y = x1.add(x2).add(1);//1 + x1 + x2
        Vector y = new DenseVector(new double[]{5.269650, 2.601217, 2.041629, 5.650300, 1.932326, 8.967327, 12.512230});

        LMProblem problem = new LMProblem(y, CreateMatrix.cbind(x1, x2), true);
        OLSRegression ols = new OLSRegression(problem);

        White instance = new White(ols.residuals);

        assertEquals(1.6761, instance.statistics(), 1e-3);
        assertEquals(0.8919, instance.pValue(), 1e-3);
    }

    /**
     *
    x1 = c(1,2,3,4,5,6,7,8,9,10,11,12,13)
    x2 = c(1.1,-1.3,-0.7,0.9,-5.2,-1.1,0.5,0.1,0.6,0.1,0.2,0.4,-0.5)
    x3 = c(0.1,3.5,0.25,-0.19,3.2,-0.35,0.6,0.12,-0.6,0.11,0.51,0.23,0.24)
    #y <- 1 + 2*x1 + x2 + x3 + rnorm(13)
    y = c(3.820349, 8.663654, 8.618842, 8.304151, 9.172394, 11.733422, 15.362680, 17.107826, 19.463393, 19.957873, 22.372256, 24.385497, 27.933842)
    lm1 = lm(y ~ x1 + x2 + x3)
    bptest(lm1, ~ x1*x2 + x1*x3 + x2*x3 + I(x1^2) + I(x2^2) + I(x3^2), studentize = TRUE)
     */
    @Test
    public void test_0050() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9, -5.2, -1.1, 0.5, 0.1, 0.6, 0.1, 0.2, 0.4, -0.5});
        Vector x3 = new DenseVector(new double[]{0.1, 3.5, 0.25, -0.19, 3.2, -0.35, 0.6, 0.12, -0.6, 0.11, 0.51, 0.23, 0.24});
//        Vector y = x1.add(x2.scaled(2)).add(x3).add(1);//1 + x1 + 2*x2 + x3
        Vector y = new DenseVector(new double[]{3.820349, 8.663654, 8.618842, 8.304151, 9.172394, 11.733422, 15.362680, 17.107826, 19.463393, 19.957873, 22.372256, 24.385497, 27.933842});

        LMProblem problem = new LMProblem(y, CreateMatrix.cbind(x1, x2, x3), true);
        OLSRegression ols = new OLSRegression(problem);

        White instance = new White(ols.residuals);

        assertEquals(9.4963, instance.statistics(), 1e-2);
        assertEquals(0.3928, instance.pValue(), 1e-3);
    }
}
