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
package com.numericalmethod.suanshu.analysis.function.polynomial;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class QuadraticSyntheticDivisionTest {

    @Test
    public void test_0010() {
        Polynomial poly = new Polynomial(5, 4, -2, -1, 1);
        double u = 3;
        double v = -2;

        QuadraticSyntheticDivision quadsd = new QuadraticSyntheticDivision(poly, new QuadraticMonomial(u, v));
        Polynomial expQuotient = new Polynomial(5, -11, 41);
        double expB = -146;
        double expA = 521;
        Polynomial quotient = quadsd.quotient();
        double resultB = quadsd.b();
        double resultA = quadsd.a();
        assertArrayEquals(expQuotient.getCoefficients(), quotient.getCoefficients(), 1e-15);
        assertEquals(expB, resultB, 1e-15);
        assertEquals(expA, resultA, 1e-15);
    }

    @Test
    public void test_0020() {
        Polynomial poly = new Polynomial(5, -4, 3, -2, 1);
        double u = 3;
        double v = 5;

        QuadraticSyntheticDivision quadsd = new QuadraticSyntheticDivision(poly, new QuadraticMonomial(u, v));
        Polynomial expQuotient = new Polynomial(5, -19, 35);
        double expB = -12;
        double expA = -138;
        Polynomial quotient = quadsd.quotient();
        double resultB = quadsd.b();
        double resultA = quadsd.a();
        assertArrayEquals(expQuotient.getCoefficients(), quotient.getCoefficients(), 1e-15);
        assertEquals(expB, resultB, 1e-15);
        assertEquals(expA, resultA, 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_0030() {
        Polynomial poly = new Polynomial(-2, 1);
        double u = 3;
        double v = 5;

        QuadraticSyntheticDivision quadsd = new QuadraticSyntheticDivision(poly, new QuadraticMonomial(u, v));
    }
}
