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
public class HornerSchemeTest {

    /**
     * Test of remainder method, of class Horner.
     */
    @Test
    public void test_0010() {
        // P(x) = 2x^3 - 6x^2 + 2x -1
        Polynomial p = new Polynomial(2, -6, 2, -1);
        double x = 3; // divided by (x - 3)

        HornerScheme horner = new HornerScheme(p, 3);
        Polynomial expQuotient = new Polynomial(2, 0, 2);
        double expRemainder = 5.0;
        Polynomial quotient = horner.quotient();
        double remainder = horner.remainder();
        assertArrayEquals(expQuotient.getCoefficients(), quotient.getCoefficients(), 1e-15);
        assertEquals(expRemainder, remainder, 1e-15);
    }
}
