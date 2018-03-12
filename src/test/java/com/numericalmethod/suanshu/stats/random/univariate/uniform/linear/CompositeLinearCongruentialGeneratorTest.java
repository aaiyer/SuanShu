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
package com.numericalmethod.suanshu.stats.random.univariate.uniform.linear;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CompositeLinearCongruentialGeneratorTest {

    /**
     * Test of of class CompositeLinearCongruentialGenerator.
     */
    @Test
    public void testCombinedLinearCongruentialGenerator_0010() {
        long a1 = 23;
        long m1 = 2012;
        long x1 = 823;
        Lehmer rng1 = new Lehmer(a1, m1, x1);

        long a2 = 31;
        long m2 = 49512;
        long x2 = 123;
        Lehmer rng2 = new Lehmer(a2, m2, x2);

        CompositeLinearCongruentialGenerator rng = new CompositeLinearCongruentialGenerator(new LinearCongruentialGenerator[]{
                    rng1, rng2
                });

        for (int i = 1; i <= 1000000; ++i) {
            x1 = (a1 * x1) % m1;
            x2 = (a2 * x2) % m2;

            long xx = x1 - x2;
            xx = xx < 0 ? xx + Math.max(m1, m2) : xx;

            assertEquals(xx, rng.nextLong());
        }
    }

    /**
     * Test of of class CompositeLinearCongruentialGenerator.
     */
    @Test
    public void testCombinedLinearCongruentialGenerator_0020() {
        long a1 = 23;
        long m1 = 2012;
        long x1 = 10;
        Lehmer rng1 = new Lehmer(a1, m1, x1);

        long a2 = 231;
        long m2 = 899512;
        long x2 = 1;
        Lehmer rng2 = new Lehmer(a2, m2, x2);

        long a3 = 23;
        long m3 = 8992;
        long x3 = 101;
        Lehmer rng3 = new Lehmer(a3, m3, x3);

        long max = Math.max(m3, Math.max(m1, m2));

        CompositeLinearCongruentialGenerator rng = new CompositeLinearCongruentialGenerator(new LinearCongruentialGenerator[]{
                    rng1, rng2, rng3
                });

        for (int i = 1; i <= 1000000; ++i) {
            x1 = (a1 * x1) % m1;
            x2 = (a2 * x2) % m2;
            x3 = (a3 * x3) % m3;

            long xx = x1 - x2 + x3;
            xx = xx < 0 ? xx + max : xx % max;

            assertEquals(xx, rng.nextLong());
        }
    }

    /**
     * Test of of class CompositeLinearCongruentialGenerator.
     */
    @Test
    public void testCombinedLinearCongruentialGenerator_0030() {
        long a1 = 23;
        long m1 = 2012;
        long x1 = 10;
        Lehmer rng1 = new Lehmer(a1, m1, x1);
        CompositeLinearCongruentialGenerator rng = new CompositeLinearCongruentialGenerator(new LinearCongruentialGenerator[]{
                    rng1
                });

        for (int i = 1; i <= 1000000; ++i) {
            x1 = (a1 * x1) % m1;
            assertEquals(x1, rng.nextLong());
        }
    }

    /**
     * Test of of class CompositeLinearCongruentialGenerator.
     */
    @Test
    public void testCombinedLinearCongruentialGenerator_0040() {
        long a1 = 23;
        long m1 = 2012;
        long x1 = 10;
        Lehmer rng1 = new Lehmer(a1, m1, x1);
        CompositeLinearCongruentialGenerator rng = new CompositeLinearCongruentialGenerator(new LinearCongruentialGenerator[]{
                    rng1
                });

        for (int i = 1; i <= 1000000; ++i) {
            double v = rng.nextDouble();
            assertTrue(v >= 0);
            assertTrue(v <= 1);
        }
    }
}
