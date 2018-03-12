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
public class KruskalWallisTest {

    /**
     * R code for the Kruskal-Wallis test
    x=c(1.3,5.4,7.6,7.2,3.5,2.7,5.21,6.3,4.4,9.8,10.24);
    g=c(1,1,1,1,1,0,0,0,0,0,0)
    kruskal.test(x,g)
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(0.3, instance.statistics(), 1e-14);
        assertEquals(0.5839, instance.pValue(), 1e-4);
    }

    /**
     * R code for the Kruskal-Wallis test
    x=c(1.3,5.4,7.6,7.2,3.5,2.7,5.21,6.3,4.4,9.8,10.24,-2.3,-5.3,-4.33,-5.4,0.21,0.34,0.27,0.86,0.902,0.663);
    g=c(1,1,1,1,1,0,0,0,0,0,0,2,2,2,2,3,3,3,3,3,3)
    kruskal.test(x,g)
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};
        samples[2] = new double[]{-2.3, -5.3, -4.33, -5.4};
        samples[3] = new double[]{0.21, 0.34, 0.27, 0.86, 0.902, 0.663};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(16.6442, instance.statistics(), 1e-4);
        assertEquals(0.0008364, instance.pValue(), 1e-7);
    }

    /**
     * With ties
     *
     * R code for the Kruskal-Wallis test
    x=c(1,1,7.6,7.2,3.5,2,2,6.3,4.4,9.8,10.24,-9,-9,-4.33,-5.4,0.21,0.21,0.21,0.86,0.902,0.663);
    g=c(1,1,1,1,1,0,0,0,0,0,0,2,2,2,2,3,3,3,3,3,3)
    kruskal.test(x,g)
     */
    @Test
    public void test_0030() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{1, 1, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2, 2, 6.3, 4.4, 9.8, 10.24};
        samples[2] = new double[]{-9, -9, -4.33, -5.4};
        samples[3] = new double[]{0.21, 0.21, 0.21, 0.86, 0.902, 0.663};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(16.8732, instance.statistics(), 1e-4);
        assertEquals(0.0007505, instance.pValue(), 1e-7);
    }

    /**
     * With ties
     *
     * R code for the Kruskal-Wallis test
    x=c(1,1,1,1,1,0,0,0,0,0);
    g=c(1,1,1,1,1,0,0,0,0,0)
    kruskal.test(x,g)
     */
    @Test
    public void test_0040() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 1, 1, 1, 1};
        samples[1] = new double[]{0, 0, 0, 0, 0};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(9, instance.statistics(), 1e-14);
        assertEquals(0.0027, instance.pValue(), 1e-4);
    }

    /**
     * All ties
     *
     * R code for the Kruskal-Wallis test
    x=c(1,1,1,1,1,1,1,1,1,1);
    g=c(1,1,1,1,1,0,0,0,0,0)
    kruskal.test(x,g)
     */
    @Test
    public void test_0050() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 1, 1, 1, 1};
        samples[1] = new double[]{1, 1, 1, 1, 1};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(Double.NaN, instance.statistics(), 1e-15);
        assertEquals(Double.NaN, instance.pValue(), 1e-4);
    }

    /**
     * Same data.
     *
     * R code for the Kruskal-Wallis test
    x=c(1,2,3,4,5,1,2,3,4,5);
    g=c(1,1,1,1,1,0,0,0,0,0)
    kruskal.test(x,g)
     */
    @Test
    public void test_0060() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 2, 3, 4, 5};
        samples[1] = new double[]{1, 2, 3, 4, 5};

        KruskalWallis instance = new KruskalWallis(samples);
        assertEquals(0, instance.statistics(), 1e-15);
        assertEquals(1, instance.pValue(), 1e-4);
    }
}
