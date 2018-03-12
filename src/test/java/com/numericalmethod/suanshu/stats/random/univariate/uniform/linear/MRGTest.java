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
public class MRGTest {

    @Test
    public void test_0010() {
        long m = 2012;
        long[] a = new long[]{23, 31, 71};
        long[] x = new long[]{823, 23, 7};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 100000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }

    @Test
    public void test_0020() {
        long m = 2147483563;
        long[] a = new long[]{23, 31, 71, 40014};
        long[] x = new long[]{823, 23, 7, 823};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 100000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }

    @Test
    public void test_0030() {
        long m = 2147483563;
        long[] a = new long[]{23, 31, 71, 40014, 59021};
        long[] x = new long[]{823, 23, 7, 823, 723};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 100000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }

    @Test
    public void test_0040() {
        long m = 2012;
        long[] a = new long[]{23, 0, 0};
        long[] x = new long[]{823, 23, 7};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 100000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }

    @Test
    public void test_0050() {
        long m = 2147483563;
        long[] a = new long[]{23, -31, 0, -40014, 59021};
        long[] x = new long[]{823, 23, 7, 823, 723};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 100000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            while (rnum < 0) {
                rnum += m;
            }
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }

    @Test
    public void test_0060() {
        long m = 2147483563;
        long[] a = new long[]{0, -31, 0, -40014, 59021};
        long[] x = new long[]{823, 23, 7, 823, 723};
        MRG rng = new MRG(m, a);
        rng.seed(x);

        for (int i = 1; i <= 1000000; ++i) {
            long rnum = FunctionOps.dotProduct(a, x);
            while (rnum < 0) {
                rnum += m;
            }
            rnum %= m;

            //shift x
            for (int j = x.length - 1; j > 0; --j) {
                x[j] = x[j - 1];
            }
            x[0] = rnum;

            assertEquals(rnum, rng.nextLong());
        }
    }
}
