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

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LEcuyerTest {

    /**
     * Test of of class LEcuyer.
     */
    @Test
    public void testLEcuyer_0010() {
        long m1 = 2147483647;
        long[] a1 = new long[]{0, 63308, -183326};
        long[] x1 = new long[]{1, 2, 3};
        MRG rng1 = new MRG(m1, a1);
        rng1.seed(x1);

        long m2 = 2145483479;
        long[] a2 = new long[]{86098, 0, -539608};
        long[] x2 = new long[]{1, 2, 3};
        MRG rng2 = new MRG(m2, a2);
        rng2.seed(x1);

        long rnum1 = FunctionOps.dotProduct(a1, x1);
        while (rnum1 < 0) {
            rnum1 += m1;
        }

        long rnum2 = FunctionOps.dotProduct(a2, x2);
        while (rnum2 < 0) {
            rnum2 += m2;
        }

        long rnum = rnum1 - rnum2;

        long r1 = rng1.nextLong();
        long r2 = rng2.nextLong();
        long r = r1 - r2;
        assertEquals(r, rnum);

        LEcuyer instance = new LEcuyer(1, 2, 3, 1, 2, 3);//test CombinedLinearCongruentialGenerator.seed(long... seeds)
        assertEquals(rnum, instance.nextLong());

        for (int i = 1; i <= 10000; ++i) {
            r1 = rng1.nextLong();
            r2 = rng2.nextLong();
            r = r1 - r2;
            while (r < 0) {
                r += Math.max(m1, m2);
            }

            assertEquals(r, instance.nextLong());
        }
    }
}
