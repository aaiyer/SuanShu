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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 *
 * @author Haksun Li
 */
public class BreuschPaganTest {

    /**
     *
    x1 = c(1,2,3,4)
    x2 = c(1.1,-1.3,-0.7,0.9)
    y <- 1 + x1 + x2
    bptest(y ~ x1, studentize=F)
     */
    @Test
    public void test_0010() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9});
        Vector y = x1.add(x2).add(1);//1 + x + res

        LMProblem problem = new LMProblem(y, new DenseMatrix(x1), true);
        OLSRegression ols = new OLSRegression(problem);

        BreuschPagan instance = new BreuschPagan(ols.residuals, false);

        assertEquals(0.1306, instance.statistics(), 1e-3);
        assertEquals(0.7178, instance.pValue(), 1e-3);
    }

    /**
     *
    x1 = c(1,2,3,4)
    x2 = c(1.1,-1.3,-0.7,0.9)
    y <- 1 + x1 + x2
    bptest(y ~ x1, studentize=T)
     */
    @Test
    public void test_0020() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9});
        Vector y = x1.add(x2).add(1);//1 + x1 + x2

        LMProblem problem = new LMProblem(y, new DenseMatrix(x1), true);
        OLSRegression ols = new OLSRegression(problem);

        BreuschPagan instance = new BreuschPagan(ols.residuals, true);

        assertEquals(1.4286, instance.statistics(), 1e-3);
        assertEquals(0.232, instance.pValue(), 1e-3);
    }
}
