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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class PolyRootTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_0010() {
        List<? extends Number> roots = new PolyRoot().solve(new Polynomial(1)); // 1
    }

    @Test
    public void test_0020() {
        PolyRoot instance = new PolyRoot();
        List<? extends Number> roots = instance.solve(new Polynomial(1, 1)); // x + 1
        Number expResult = new Double(-1);
        assertEquals(expResult, roots.get(0));

        List<Double> realRoots = PolyRoot.getRealRoots(roots);
        assertEquals(1, realRoots.size());
        assertEquals(-1., realRoots.get(0), 0.);
    }

    @Test
    public void test_0030() {
        PolyRoot instance = new PolyRoot();
        List<? extends Number> roots = instance.solve(new Polynomial(1, -2, 1)); // x^2 -2x + 1
        assertEquals(1., roots.get(0).doubleValue(), 0.);
        assertEquals(1., roots.get(1).doubleValue(), 0.);
    }

    @Test
    public void test_0040() {
        PolyRoot instance = new PolyRoot();
        List<? extends Number> roots = instance.solve(new Polynomial(2, -281.256, 9740.6784)); // 2x^2 -281.256x + 9740.6784
        NumberAssert.assertSameList(Arrays.asList(61.728, 78.9), roots, 1e-12);
    }

    @Test
    public void test_0050() {
        PolyRoot instance = new PolyRoot();
        List<? extends Number> roots = instance.solve(new Polynomial(1, 0, 1)); // x^2 + 1
        assertEquals(Complex.I, (Complex) roots.get(0));
        assertEquals(Complex.I.opposite(), (Complex) roots.get(1));
    }

    @Test
    public void test_0060() {
        PolyRoot instance = new PolyRoot();
        List<? extends Number> roots = instance.solve(new Polynomial(0, 1, 1)); // 0x^2 + x + 1
        assertEquals(-1.0, roots.get(0));
    }

    @Test
    public void test_0070() {
        // P(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        Polynomial polynomial = new Polynomial(1, -10, 35, -50, 24);
        List<Number> expRoots = new ArrayList<Number>();
        expRoots.add(1);
        expRoots.add(2);
        expRoots.add(3);
        expRoots.add(4);
        double epsilon = SuanShuUtils.autoEpsilon(polynomial.getCoefficients());

        PolyRoot solver = new PolyRoot();
        List<? extends Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", 4, roots.size());
        NumberAssert.assertSameList(expRoots, roots, epsilon);
    }
}
