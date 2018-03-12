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
package com.numericalmethod.suanshu.analysis.function.polynomial.root.jenkinstraub;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.complex.Complex;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.test.NumberAssert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ken Yiu
 */
public class JenkinsTraubRealTest {

    @Test
    public void test_0010() {
        Polynomial polynomial = new Polynomial(2);
        List<Number> expRoots = new ArrayList<Number>();

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", expRoots.size(), roots.size());
        assertSameList(expRoots, roots, 1e-15);
    }

    @Test
    public void test_0020() {
        Polynomial polynomial = new Polynomial(-3, 7);
        List<Double> expRoots = Arrays.asList(7. / 3.);

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", expRoots.size(), roots.size());
        assertSameList(expRoots, roots, 1e-15);
    }

    @Test
    public void test_0030() {
        Polynomial polynomial = new Polynomial(1, -3, 2);
        List<Integer> expRoots = Arrays.asList(1, 2);

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", expRoots.size(), roots.size());
        assertSameList(expRoots, roots, 1e-15);
    }

    @Test
    public void test_0040() {
        // P(x) = (x-1)(x-2)(x-3)(x-4) = x^4 - 10x^3 + 35x^2 - 50x + 24
        Polynomial polynomial = new Polynomial(1, -10, 35, -50, 24);
        List<Integer> expRoots = Arrays.asList(1, 2, 3, 4);
        double epsilon = SuanShuUtils.autoEpsilon(polynomial.getCoefficients());

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", expRoots.size(), roots.size());
        assertSameList(expRoots, roots, epsilon);
    }

    @Test
    public void test_0050() {
        // P(x) = (x-1)(x-2)(x-3)(x-4)(x-5) = x^5 - 15x^4 + 85x^3 - 225x^2 + 274x - 120
        Polynomial polynomial = new Polynomial(1, -15, 85, -225, 274, -120);
        List<Integer> expRoots = Arrays.asList(1, 2, 3, 4, 5);
        double epsilon = SuanShuUtils.autoEpsilon(polynomial.getCoefficients());

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", expRoots.size(), roots.size());
        assertSameList(expRoots, roots, epsilon);
    }

    @Test
    public void test_0060() {
        // P(z) = (z - 0.5 + 0.5i)(z - 0.5 - 0.5i)(z-1)^2(z+1)(z-2)(z-2.01)
        //      = x^7 - 6.01x^6 + 12.54x^5 - 8.545x^4 - 5.505x^3 + 12.545x^2 - 8.035x + 2.01
        Polynomial polynomial = new Polynomial(1, -6.01, 12.54, -8.545, -5.505, 12.545, -8.035, 2.01);
        List<Number> expRoots = new ArrayList<Number>();
        expRoots.addAll(Arrays.asList(new Complex(0.5, 0.5), new Complex(0.5, -0.5), 1, 1, -1, 2, 2.01));

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", polynomial.degree(), roots.size());
        assertSameList(expRoots, roots, 1e-5); // TODO: not accurate enough, check which part has compromised it

        Complex pv = null;
        for (Number root : roots) {
            pv = polynomial.evaluate(root);
            assertEquals(0.0, pv.modulus(), 1e-6);
        }
    }

    /**
     * polyroot(c(1,-2,1,0,0,0,0,0,0,0,0,0,-2,5,-2,0,0,0,0,0,0,0,0,0,1,-2,1))
     */
    @Test
    public void test_0070() {
        Polynomial polynomial = new Polynomial(1, -2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 5, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -2, 1);

        JenkinsTraubReal solver = new JenkinsTraubReal();
        List<Number> roots = solver.solve(polynomial);

        assertEquals("number of roots", polynomial.degree(), roots.size());

        Complex pv = null;
        for (Number root : roots) {
            pv = polynomial.evaluate(root);
            assertEquals(0.0, pv.modulus(), 1e-11);
        }
    }
}
