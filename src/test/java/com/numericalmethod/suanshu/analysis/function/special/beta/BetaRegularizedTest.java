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
package com.numericalmethod.suanshu.analysis.function.special.beta;

import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularized;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BetaRegularizedTest {

    //<editor-fold defaultstate="collapsed" desc="tests for the classical Continued Fraction">
    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0010() {
        BetaRegularized Ix = new BetaRegularized(1, 1);
        assertEquals(0, Ix.evaluate(0), 1e-15);
        assertEquals(0.1, Ix.evaluate(0.1), 1e-15);
        assertEquals(0.2, Ix.evaluate(0.2), 1e-15);
        assertEquals(0.3, Ix.evaluate(0.3), 1e-15);
        assertEquals(0.4, Ix.evaluate(0.4), 1e-15);
        assertEquals(0.5, Ix.evaluate(0.5), 1e-15);
        assertEquals(0.6, Ix.evaluate(0.6), 1e-15);
        assertEquals(0.7, Ix.evaluate(0.7), 1e-15);
        assertEquals(0.8, Ix.evaluate(0.8), 1e-15);
        assertEquals(0.9999, Ix.evaluate(0.9999), 1e-14);
        assertEquals(1, Ix.evaluate(1), 1e-15);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0020() {
        BetaRegularized Ix = new BetaRegularized(1, 2);
        assertEquals(0, Ix.evaluate(0), 1e-15);
        assertEquals(0.1899999999999997, Ix.evaluate(0.1), 1e-15);
        assertEquals(0.3599999999999998, Ix.evaluate(0.2), 1e-15);
        assertEquals(0.51, Ix.evaluate(0.3), 1e-15);
        assertEquals(0.64, Ix.evaluate(0.4), 1e-15);
        assertEquals(0.75, Ix.evaluate(0.5), 1e-15);
        assertEquals(0.84, Ix.evaluate(0.6), 1e-15);
        assertEquals(0.91, Ix.evaluate(0.7), 1e-15);
        assertEquals(0.96, Ix.evaluate(0.8), 1e-14);
        assertEquals(0.99999999, Ix.evaluate(0.9999), 1e-14);
        assertEquals(1, Ix.evaluate(1), 1e-15);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0025() {
        BetaRegularized Ix = new BetaRegularized(2, 1);
        assertEquals(0.99999999, 1 - Ix.evaluate(1 - 0.9999), 1e-14);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0030() {
        BetaRegularized Ix = new BetaRegularized(1.1, 3.3);
        assertEquals(0.88372236490555, Ix.evaluate(0.5), 1e-15);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0040() {
        assertEquals(0.750033259858168, (new BetaRegularized(10.1, 13.3)).evaluate(0.5), 1e-15);
        assertEquals(0.999999994211437, (new BetaRegularized(20.123456, 73.3691113)).evaluate(0.5), 1e-15);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     */
    @Test
    public void test_ClassicalCF_0050() {
        assertEquals(0.0004558031635199315, (new BetaRegularized(50.25, 0.75)).evaluate(0.87012987012987), 1e-15);
    }

    /**
     * Test of the classical Continued Fraction of class BetaRegularized.
     * The numerical error is due to using LANCZOS_QUICK.
     */
    @Test
    public void test_ClassicalCF_0060() {
        assertEquals(0.9, (new BetaRegularized(20000, 10000)).evaluate(1 - 0.3298477983630155), 1e-11);
        assertEquals(0.1, (new BetaRegularized(10000, 20000)).evaluate(0.3298477983630155), 1e-11);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="tests for the Algorithm 708 Continued Fraction">
//    /**
//     * Test of the Algorithm 708 Continued Fraction of class BetaRegularized.
//     */
//    @Test
//    public void test_Algo708CF_0010() {
////
//        BetaRegularized Ix = new BetaRegularized(30, 40);
//        assertEquals(0.01212616062332972, Ix.evaluate(0.3), 1e-14);
//    }
//
//    /**
//     * Test of the Algorithm 708 Continued Fraction of class BetaRegularized.
//     */
//    @Test
//    public void test_Algo708CF_0020() {
////
//        BetaRegularized Ix = new BetaRegularized(100, 80);
//        assertEquals(0.885574044073776, Ix.evaluate(0.6), 1e-14);
//    }
    //</editor-fold>
}