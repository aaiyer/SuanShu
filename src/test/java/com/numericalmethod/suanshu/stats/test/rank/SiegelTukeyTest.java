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
package com.numericalmethod.suanshu.stats.test.rank;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SiegelTukeyTest {

    /**
     * from wiki
     * http://en.wikipedia.org/wiki/Siegel%E2%80%93Tukey_test
     */
    @Test
    public void test_0010() {
        SiegelTukey instance = new SiegelTukey(
                new double[]{4, 16, 48, 51, 66, 98},
                new double[]{33, 62, 84, 85, 88, 93, 97},
                0, true);

        assertEquals(16, instance.statistics(), 1e-4);
        assertEquals(0.2669, instance.pValue1SidedLess, 1e-4);
    }

    /**
     * With ties.
     * 
     * c.f.,
     * http://www.r-statistics.com/2010/02/siegel-tukey-a-non-parametric-test-for-equality-in-variability-r-code/
     */
    @Test
    public void test_0020() {
        SiegelTukey instance = new SiegelTukey(
                new double[]{4, 4, 5, 5, 6, 6},
                new double[]{0, 0, 1, 9, 10, 10},
                0, false);

        assertEquals(36, instance.statistics(), 1e-4);
        assertEquals(0.003600915066787423, instance.pValue(), 1e-6);
    }
}
