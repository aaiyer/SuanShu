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
package com.numericalmethod.suanshu.analysis.sequence;

import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class FibonacciTest {

    /**
     * Test of class Fibonacci.
     */
    @Test
    public void test_Fibonacci_0010() {
        double[] expected = new double[]{1, 1, 2, 3, 5, 8, 13};

        Fibonacci instance = new Fibonacci(7);
        assertEquals(7, instance.length());
        assertTrue(equal(expected, instance.getAll(), 0));

        for (int i = 1; i <= instance.length(); ++i) {
            assertEquals(expected[i - 1], instance.get(i), 1e-15);
        }
    }
}
