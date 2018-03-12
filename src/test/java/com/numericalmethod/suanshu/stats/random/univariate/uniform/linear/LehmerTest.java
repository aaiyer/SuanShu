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
public class LehmerTest {

    /**
     * Test of of class Lehmer.
     */
    @Test
    public void testLehmer_0010() {


        long a = 23;
        long m = 2012;
        long x = 823;
        Lehmer rng = new Lehmer(a, m, x);

        int k = 3;
        Lehmer rng1 = new Lehmer(a, m, k, x);

        for (int i = 1; i <= 1000; ++i) {
            x = (a * x) % m;
            assertEquals(x, rng.nextLong());

            if (i % k == 0) {
                assertEquals(x, rng1.nextLong());
            }
        }
    }

    /**
     * Test of of class Lehmer.
     * 8682522807148012L is too big to do it using straightforward computation
     */
    @Test
    public void testLehmer_0020() {


        long a = 40014;
        long m = 2147483563;
        long x = 800; //8682522807148012L;
        Lehmer rng = new Lehmer(a, m, x);

        for (int i = 1; i <= 1000; ++i) {
            x = (a * x) % m;
            assertEquals(x, rng.nextLong());
        }
    }

    /**
     * Test of of class Lehmer.
     * 8682522807148012L is too big to do it using straightforward computation
     */
    @Test
    public void testLehmer_0030() {


        long a = 40014;
        long m = 2147483563;
        long x = 8682522807L; //8682522807148012L
        Lehmer rng = new Lehmer(a, m, x);

        for (int i = 1; i <= 1000; ++i) {
            x = (a * x) % m;
            assertEquals(x, rng.nextLong());
        }
    }

    /**
     * Test of of class Lehmer.
     */
    @Test
    public void testLehmer_0040() {


        long a = 23;
        long m = 201225356926684L;
        long x = 823;
        Lehmer rng = new Lehmer(a, m, x);

        int k = 5;
        Lehmer rng1 = new Lehmer(a, m, k, x);

        for (int i = 1; i <= 1000; ++i) {
            x = (a * x) % m;
            assertEquals(x, rng.nextLong());

            if (i % k == 0) {
                assertEquals(x, rng1.nextLong());
            }
        }
    }

    /**
     * Test of of class Lehmer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLehmer_0050() {


        long a = -23;
        long m = 2012;
        long x = 823;
        Lehmer rng = new Lehmer(a, m, x);
    }

    /**
     * Test of of class Lehmer.
     */
    @Test
    public void testLehmer_0060() {


        Lehmer rng = new Lehmer();
        for (int i = 1; i <= 1000; ++i) {
            rng.nextLong();
            rng.nextDouble();
        }
    }
}
