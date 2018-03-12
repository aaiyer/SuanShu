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
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.test.NumberAssert;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class QuadraticRootTest {

    @Test
    public void test_solve_0010() {
        QuadraticRoot instance = new QuadraticRoot();
        Polynomial polynomial = new Polynomial(1, -2, 1); // x^2 -2x + 1
        NumberAssert.assertSameList(Arrays.asList(1, 1), instance.solve(polynomial), 0);
    }

    @Test
    public void test_solve_0020() {
        QuadraticRoot instance = new QuadraticRoot();
        Polynomial polynomial = new Polynomial(2, -281.256, 9740.6784); // 2x^2 -281.256x + 9740.6784
        List<Number> roots = instance.solve(polynomial);
        double epsilon = SuanShuUtils.autoEpsilon(polynomial.getCoefficients());
        NumberAssert.assertSameList(Arrays.asList(61.728, 78.9), instance.solve(polynomial), 1e-12);
    }

    @Test
    public void test_solve_0030() {
        QuadraticRoot instance = new QuadraticRoot();
        Polynomial polynomial = new Polynomial(1, 0, 1); // x^2 + 1
        List<Number> roots = instance.solve(polynomial);
        assertEquals(Arrays.asList(Complex.I, Complex.I.opposite()), roots);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exception_0010() {
        QuadraticRoot instance = new QuadraticRoot();
        instance.solve(new Polynomial(0, 1, 1)); // x + 1
    }
}
