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
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.number.complex.Complex;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class CubicRootTest {

    @Test
    public void test_solve_0010() {
        Polynomial polynomial = new Polynomial(new double[]{1, 0, 0, 0});
        CubicRoot instance = new CubicRoot();
        List<Number> roots = instance.solve(polynomial);
        for (int i = 0; i < roots.size(); ++i) {
            Number root = roots.get(i);
            assertTrue(NumberUtils.isReal(root));
            Complex p_root = polynomial.evaluate(root);
            assertEquals(0., p_root.modulus(), 0);
        }
    }

    /**
     * Test of roots method, of class CubicRoot.
     */
    @Test
    public void test_solve_0020() {
        Polynomial polynomial = new Polynomial(1, 1, 1, 1);
        CubicRoot instance = new CubicRoot();
        List<Number> roots = instance.solve(polynomial);
        for (int i = 0; i < roots.size(); ++i) {
            Number root = roots.get(i);
            Complex p_root = polynomial.evaluate(root);
            assertEquals(0., p_root.modulus(), 1e-15);
        }
    }

    /**
     * polyroot(c(9991, -0.0000001, 1.5678901234, 1))
     */
    @Test
    public void test_solve_0030() {
        Polynomial polynomial = new Polynomial(1, 1.5678901234, -0.0000001, 9991);
        CubicRoot instance = new CubicRoot();
        List<Number> roots = instance.solve(polynomial);
        for (int i = 0; i < roots.size(); ++i) {
            Number root = roots.get(i);
//            System.out.println("root = " + root.toString());
            Complex p_root = polynomial.evaluate(root);
            assertEquals(0., p_root.modulus(), 1e-6);//TODO: the accuracy is quite poor compared to those in R
        }
    }

    @Test
    public void test_solve_0040() {
        // P(x) = (x - 0.5)(x - 0.5 - 0.5i)(x - 0.5 + 0.5i) = x^3 - 1.5x^2 + x - 0.25
        Polynomial polynomial = new Polynomial(1, -1.5, 1, -0.25);
        CubicRoot instance = new CubicRoot();
        List<Number> roots = instance.solve(polynomial);
        for (int i = 0; i < roots.size(); ++i) {
            Number root = roots.get(i);
            Complex p_root = polynomial.evaluate(root);
            assertEquals(0., p_root.modulus(), 1e-15);
        }
    }

    @Test
    public void test_solve_0050() {
        // P(x) = (x - 1)(x - 2)(x - 3) = x^3 - 6x^2 + 11x - 6
        Polynomial polynomial = new Polynomial(1, -6, 11, -6);
        CubicRoot instance = new CubicRoot();
        List<Number> roots = instance.solve(polynomial);
        for (int i = 0; i < roots.size(); ++i) {
            Number root = roots.get(i);
            assertTrue(NumberUtils.isReal(root));
            Complex p_root = polynomial.evaluate(root);
            assertEquals(0., p_root.modulus(), 1e-15);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exception_0010() {
        CubicRoot instance = new CubicRoot();
        instance.solve(new Polynomial(1, 2));
    }
}
