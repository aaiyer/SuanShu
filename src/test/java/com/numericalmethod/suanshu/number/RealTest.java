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
package com.numericalmethod.suanshu.number;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RealTest {

    /**
     * Test of class Real.
     */
    @Test
    public void test_Real_0010() {
        Real r1 = new Real("100");
        Real r2 = new Real(100);
        Real r3 = new Real("100000000000000000000");
        Real r4 = new Real("-100000000000000000000");

        assertEquals(r1, r2);
        assertEquals(-1, r4.compareTo(r3));
    }

    /**
     * Test of class Real.
     */
    @Test
    public void test_Real_0020() {
        Real r100 = new Real("100");
        Real r200 = new Real(200);
        Real r300 = new Real(300);
        Real r20000 = new Real(20000);
        Real rHalf = new Real(0.5);

        assertEquals(r100.add(r200), r300);
        assertEquals(r200.minus(r100), r100);
        assertEquals(r100.multiply(r200), r20000);
        assertEquals(r100.divide(r200), rHalf);
    }

    /**
     * Test of class Real.
     */
    @Test
    public void test_Real_0030() {
        Real ONE = new Real(1).ONE();
        Real ZERO = new Real(0).ZERO();

        assertEquals(ONE, ONE.add(ZERO));
        assertEquals(ONE, ONE.minus(ZERO));
        assertEquals(ZERO, ONE.minus(ONE));
        assertEquals(ONE, ONE.multiply(ONE));
        assertEquals(ONE, ONE.divide(ONE));
        assertEquals(ZERO, ZERO.multiply(ONE));
        assertEquals(ZERO, ZERO.divide(ONE));
    }
}
