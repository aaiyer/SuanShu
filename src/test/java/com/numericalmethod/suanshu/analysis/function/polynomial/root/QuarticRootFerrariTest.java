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
import com.numericalmethod.suanshu.test.NumberAssert;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.complex.Complex;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class QuarticRootFerrariTest {

    @Test
    public void test_solve_0010() {
        // P(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        double a = 1.0;
        double b = -10.0;
        double c = 35.0;
        double d = -50.0;
        double e = 24.0;
        QuarticRootFerrari quartic = new QuarticRootFerrari();
        List<Integer> expResult = Arrays.asList(1, 2, 3, 4);
        List<Number> result = quartic.solve(a, b, c, d, e);
        NumberAssert.assertSameList(expResult, result, SuanShuUtils.autoEpsilon(a, b, c, d, e));
    }

    @Test
    public void test_solve_0020() {
        // P(x) = (x-1)(x-1)(x-1-0.1i)(x-1+0.1i) = x^4 - 4x^3 + 6.01x^2 - 4.02x + 1.01
        double a = 1.0;
        double b = -4.0;
        double c = 6.01;
        double d = -4.02;
        double e = 1.01;
        QuarticRootFerrari quartic = new QuarticRootFerrari();
        List<Number> result = quartic.solve(a, b, c, d, e);

        Polynomial poly = new Polynomial(a, b, c, d, e);
        for (Number root : result) {
            Complex pv = poly.evaluate(root);
            assertEquals(0.0, pv.modulus(), 1e-14);
        }
    }

    @Test
    public void test_solve_0030() {
        // P(x) = 999x^4 - x^3 + 1234.567x^2 - 0.001x + 11
        // check by R: polyroot(c(11, -0.001, 1234.567, -1, 999))
        double a = 999.0;
        double b = -1.0;
        double c = 1234.567;
        double d = -0.001;
        double e = 11;
        QuarticRootFerrari quartic = new QuarticRootFerrari();
        List<Number> result = quartic.solve(a, b, c, d, e);

        Polynomial poly = new Polynomial(a, b, c, d, e);
        for (Number root : result) {
            Complex pv = poly.evaluate(root);
            assertEquals(0.0, pv.modulus(), 1e-6);
        }
    }
}
