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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaLanczosTest {

    /**
     * Positive numbers.
     */
    @Test
    public void testLanczos_0010() {
        Gamma gamma = new GammaLanczosQuick();
        assertEquals(1.772453850905516, gamma.evaluate(0.5), 1e-15);
        assertEquals(1.225416702465178, gamma.evaluate(0.75), 1e-15);
        assertEquals(1.031453317129032, gamma.evaluate(0.95), 1e-15);
        assertEquals(1.000578205629359, gamma.evaluate(0.999), 1e-15);
        assertEquals(0.886226925452758, gamma.evaluate(1.5), 1e-15);
        assertEquals(78.78448106132322, gamma.evaluate(5.75), 1e-12);
        assertEquals(660355655453.767, gamma.evaluate(15.75), 1e-2);
        assertEquals(1.52732311536218e105, gamma.evaluate(73.75), 1e93);
    }

    /**
     * Negative numbers.
     */
    @Test
    public void testLanczos_0020() {
        Gamma gamma = new GammaLanczosQuick();
        assertEquals(-3.544907701811032, gamma.evaluate(-0.5), 1e-14);
        assertEquals(-4.834146544295878, gamma.evaluate(-0.75), 1e-14);
        assertEquals(-20.49482664342684, gamma.evaluate(-0.95), 1e-13);
        assertEquals(-1000.424196681276, gamma.evaluate(-0.999), 1e-10);
        assertEquals(2.363271801207355, gamma.evaluate(-1.5), 1e-15);
        assertEquals(0.00980745551895347, gamma.evaluate(-5.75), 1e-16);
        assertEquals(4.27175573144018e-13, gamma.evaluate(-15.75), 1e-26);
        assertEquals(3.944317997381716e-107, gamma.evaluate(-73.75), 1e-119);
    }

    /**
     * 0.
     */
    @Test
    public void testLanczos_0030() {
        Gamma gamma = new GammaLanczosQuick();
        assertEquals(Double.POSITIVE_INFINITY, gamma.evaluate(0), 1e-15);
    }
}
