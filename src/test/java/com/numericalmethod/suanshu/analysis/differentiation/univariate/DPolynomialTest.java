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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DPolynomialTest {

    @Test
    public void test_DPolynomial_0010() {
        Polynomial p = new Polynomial(new double[]{1, 2, 1});
        Polynomial dp = new Polynomial(new double[]{2, 2});

        DPolynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0020() {
        Polynomial p = new Polynomial(new double[]{1});
        Polynomial dp = new Polynomial(new double[]{0});

        Polynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0030() {
        Polynomial p = new Polynomial(new double[]{12.9657});
        Polynomial dp = new Polynomial(new double[]{0});

        Polynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0040() {
        Polynomial p = new Polynomial(new double[]{-9901});
        Polynomial dp = new Polynomial(new double[]{0});

        Polynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0050() {
        Polynomial p = new Polynomial(new double[]{0});
        Polynomial dp = new Polynomial(new double[]{0});

        Polynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0060() {
        Polynomial p = new Polynomial(new double[]{3.5, 7.1, 1, 2, 1});
        Polynomial dp = new Polynomial(new double[]{3.5 * 4.0, 7.1 * 3.0, 1 * 2.0, 2 * 1.0});
//        Polynomial dp = new Polynomial(new double[]{14, 21.3, 2, 2});//floating point arithmetic error

        UnivariateRealFunction instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0070() {
        Polynomial p = new Polynomial(new double[]{2, 0});
        Polynomial dp = new Polynomial(new double[]{2});

        DPolynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0080() {
        Polynomial p = new Polynomial(new double[]{0, 0});
        Polynomial dp = new Polynomial(new double[]{0});

        DPolynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }

    @Test
    public void test_DPolynomial_0090() {
        Polynomial p = new Polynomial(new double[]{0, 0, 0});
        Polynomial dp = new Polynomial(new double[]{0});

        DPolynomial instance = new DPolynomial(p);
        assertEquals(dp, instance);
    }
}
