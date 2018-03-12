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
package com.numericalmethod.suanshu.analysis.function.polynomial.root;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.test.NumberAssert;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Ken Yiu
 */
public class QuarticRootTest {

    @Test
    public void test_0010() {
        // P(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        double a = 1.0;
        double b = -10.0;
        double c = 35.0;
        double d = -50.0;
        double e = 24.0;
        Polynomial p = new Polynomial(a, b, c, d, e);

        QuarticRoot solver = new QuarticRoot();
        List<Integer> expResult = Arrays.asList(1, 2, 3, 4);
        List<Number> result = solver.solve(p);
        NumberAssert.assertSameList(expResult, result, SuanShuUtils.autoEpsilon(a, b, c, d, e));
    }

    @Test
    public void test_0020() {
        // P(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        double a = 1.0;
        double b = -10.0;
        double c = 35.0;
        double d = -50.0;
        double e = 24.0;
        Polynomial p = new Polynomial(a, b, c, d, e);

        QuarticRoot solver = new QuarticRoot(new QuarticRootFerrari());
        List<Integer> expResult = Arrays.asList(1, 2, 3, 4);
        List<Number> result = solver.solve(p);
        NumberAssert.assertSameList(expResult, result, SuanShuUtils.autoEpsilon(a, b, c, d, e));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exception_0010() {
        // use a dummy QuarticSolver here to show that the exception is only due
        // to the wrong degree of the polynomial
        QuarticRoot instance = new QuarticRoot(new QuarticRoot.QuarticSolver() {

            public List<Number> solve(double a, double b, double c, double d, double e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        List<Number> result = instance.solve(new Polynomial(1, 2));
    }
}
