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

import com.numericalmethod.suanshu.analysis.function.special.gamma.Trigamma;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class TrigammaTest {

    /**
     * x < 30
     */
    @Test
    public void test_0010() {
        Trigamma instance = new Trigamma();
        assertEquals(1e18, instance.evaluate(0.000000001), 1.5e2);
        assertEquals(330578514.041496, instance.evaluate(0.000055), 1e-7);
        assertEquals(101.4332991507927, instance.evaluate(0.1), 1e-13);
        assertEquals(0.934802200544679, instance.evaluate(1.5), 1e-13);
        assertEquals(0.09084666127454624, instance.evaluate(11.5), 1e-13);
        assertEquals(0.0344793432004201, instance.evaluate(29.5), 1e-13);
    }

    /**
     * x > 30
     */
    @Test
    public void test_0020() {
        Trigamma instance = new Trigamma();
        assertEquals(0.03389506035773994, instance.evaluate(30), 1e-13);
        assertEquals(0.0201512637902929, instance.evaluate(50.123), 1e-13);
        assertEquals(0.00668343956254575, instance.evaluate(150.123), 1e-13);
        assertEquals(6.66683422396423e-05, instance.evaluate(15000.123), 1e-19);
        assertEquals(8.10000007699049e-10, instance.evaluate(1234567890), 1.1e-24);
    }

    @Test
    public void test_0030() {
        Trigamma instance = new Trigamma();
        assertEquals(Double.POSITIVE_INFINITY, instance.evaluate(0), 0);
    }

    /**
     * x < 0
     */
    @Test
    public void test_0040() {
        Trigamma instance = new Trigamma();
        assertEquals(100000001.6451745, instance.evaluate(-0.0001), 1.5e-8);
        assertEquals(101.9225399594772, instance.evaluate(-0.1), 1e-13);
        assertEquals(9.37924664498912, instance.evaluate(-1.5), 1e-13);
        assertEquals(9.86298190873674, instance.evaluate(-150.5), 1e-13);
    }
}
