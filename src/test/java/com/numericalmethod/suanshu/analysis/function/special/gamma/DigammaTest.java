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

import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class DigammaTest {

    /**
     * x < 1e-5
     */
    @Test
    public void test_0010() {
        Digamma digamma = new Digamma();
        assertEquals(-1000000000.23621, digamma.evaluate(0.000000001), 1);
        assertEquals(-18182.39530701523, digamma.evaluate(0.000055), 1e-9);
    }

    /**
     * x > 10
     */
    @Test
    public void test_0020() {
        Digamma digamma = new Digamma();
        assertEquals(3.701327374034843, digamma.evaluate(41), 1e-15);
        assertEquals(2.303001034297686, digamma.evaluate(10.5), 1e-15);
        assertEquals(3.035049255737562, digamma.evaluate(21.3), 1e-15);
        assertEquals(4.365806793119618, digamma.evaluate(79.21235), 1e-15);
        assertEquals(5.013506348539152, digamma.evaluate(150.931), 1e-15);
        assertEquals(5.29581528321991, digamma.evaluate(200), 1e-15);
        assertEquals(4.071152880714124, digamma.evaluate(59.1238), 1e-15);
    }

    /**
     * x < 10
     */
    @Test
    public void test_0030() {
        Digamma digamma = new Digamma();
        assertEquals(0.0364899739785767, digamma.evaluate(1.5), 1e-15);
        assertEquals(0.703156640645243, digamma.evaluate(2.5), 1e-15);
        assertEquals(1.103156640645243, digamma.evaluate(3.5), 1e-15);
        assertEquals(1.388870926359529, digamma.evaluate(4.5), 1e-15);
        assertEquals(1.61109314858175, digamma.evaluate(5.5), 1e-15);
        assertEquals(1.792911330399933, digamma.evaluate(6.5), 1e-15);
        assertEquals(1.946757484246087, digamma.evaluate(7.5), 1e-15);
        assertEquals(2.080090817579420, digamma.evaluate(8.5), 1e-15);
        assertEquals(2.19773787640295, digamma.evaluate(9.5), 1e-15);
    }

    /**
     * Very special case for recursive call; c.f., BIG_X
     */
    @Test
    public void test_0040() {
        Digamma digamma = new Digamma();
        assertEquals(-0.577215664901533, digamma.evaluate(1), 1e-15);
    }
}
