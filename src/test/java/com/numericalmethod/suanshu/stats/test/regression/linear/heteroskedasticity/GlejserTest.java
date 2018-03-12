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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GlejserTest {

    /**
     * test_0020 in BreuschPaganTest
     * studentize=T
     */
    @Test
    public void test_0010() {
        Vector x1 = new DenseVector(new double[]{1, 2, 3, 4});
        Vector x2 = new DenseVector(new double[]{1.1, -1.3, -0.7, 0.9});
        Vector y = x1.add(x2).add(1);//1 + x + res

        LMProblem problem = new LMProblem(y, new DenseMatrix(x1), true);
        OLSRegression ols = new OLSRegression(problem);

        Glejser instance = new Glejser(ols.residuals);

        assertEquals(1.4399999999999995, instance.statistics(), 1e-3);//TODO: from debugger; vs. 1.4286 comparing to Breusch-Pagan
        assertEquals(0.230139340443417, instance.pValue(), 1e-3);//TODO: from debugger; vs. 0.232 comparing to Breusch-Pagan
    }
}
