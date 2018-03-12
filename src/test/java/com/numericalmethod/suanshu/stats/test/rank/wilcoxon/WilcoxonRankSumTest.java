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
package com.numericalmethod.suanshu.stats.test.rank.wilcoxon;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class WilcoxonRankSumTest {

    /**
     * 
    x=c(1.3,5.4,7.6,7.2,3.5);
    y=c(2.7,5.21,6.3,4.4,9.8,10.24);
    wilcox.test(x,y,exact=T,mu=2)$p.value
    wilcox.test(x,y,exact=T,mu=2,alternative="greater")$p.value
    wilcox.test(x,y,exact=T,mu=2,alternative="less")$p.value
     * 
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 2, true);
        assertEquals(6, instance.statistics(), 1e-15);

        assertEquals(0.1255411255411255, instance.pValue(), 1e-15);
        assertEquals(0.958874458874459, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.06277056277056277, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1.3,5.4,7.6,7.2,3.5);
    y=c(2.7,5.21,6.3,4.4,9.8,10.24);
    wilcox.test(x,y,exact=F,corr=F,mu=2)$p.value
    wilcox.test(x,y,exact=F,corr=F,mu=2,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=F,mu=2,alternative="less")$p.value
     *
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 2, false, false);
        assertEquals(6, instance.statistics(), 1e-15);

        assertEquals(0.1003482464622908, instance.pValue(), 1e-15);
        assertEquals(0.9498258767688547, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.0501741232311454, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1.3,5.4,7.6,7.2,3.5);
    y=c(2.7,5.21,6.3,4.4,9.8,10.24);
    wilcox.test(x,y,exact=F,corr=T,mu=2)$p.value
    wilcox.test(x,y,exact=F,corr=T,mu=2,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=T,mu=2,alternative="less")$p.value
     *
     */
    @Test
    public void test_0025() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 2, false, true);
        assertEquals(6, instance.statistics(), 1e-15);

        assertEquals(0.1206908005274455, instance.pValue(), 1e-15);
        assertEquals(0.958581287420597, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.06034540026372277, instance.pValue1SidedLess, 1e-15);
    }

    /**
     * 
    x=c(1,2.1,7,2.4,3,5.2);
    y=c(2,4,1.2,3.4,9.6,8);
    wilcox.test(x,y,exact=T)$p.value
    wilcox.test(x,y,exact=T,alternative="greater")$p.value
    wilcox.test(x,y,exact=T,alternative="less")$p.value
     *
     */
    @Test
    public void test_0030() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 1.2, 3.4, 9.6, 8};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1]);
        assertEquals(14, instance.statistics(), 1e-15);

        assertEquals(0.5887445887445887, instance.pValue(), 1e-15);
        assertEquals(0.7575757575757576, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.2943722943722943, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1,2.1,7,2.4,3,5.2);
    y=c(2,4,1.2,3.4,9.6,8);
    wilcox.test(x,y,exact=F,corr=F)$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="less")$p.value
     *
     */
    @Test
    public void test_0040() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 1.2, 3.4, 9.6, 8};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, false);
        assertEquals(14, instance.statistics(), 1e-15);

        assertEquals(0.5218393903336155, instance.pValue(), 1e-15);
        assertEquals(0.7390803048331923, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.2609196951668077, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1,2.1,7,2.4,3,5.2);
    y=c(2,4,1.2,3.4,9.6,8);
    wilcox.test(x,y,exact=F,corr=T)$p.value
    wilcox.test(x,y,exact=F,corr=T,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=T,alternative="less")$p.value
     *
     */
    @Test
    public void test_0045() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 1.2, 3.4, 9.6, 8};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, true);
        assertEquals(14, instance.statistics(), 1e-15);

        assertEquals(0.5751735319201966, instance.pValue(), 1e-15);
        assertEquals(0.764415000754972, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.2875867659600983, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
     * Ties.
     *
    x=c(1,1,1,1,2.1,7,2.4,3,5.2);
    y=c(2,4,4,4,1.2,3.4,9.6,8);
    wilcox.test(x,y,exact=F)$p.value
    wilcox.test(x,y,exact=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,alternative="less")$p.value
     *
     */
    @Test
    public void test_0050() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 1, 1, 1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 4, 4, 1.2, 3.4, 9.6, 8};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, true);
        assertEquals(18, instance.statistics(), 1e-15);

        assertEquals(0.0893997781071609, instance.pValue(), 1e-15);
        assertEquals(0.963723603180846, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.04469988905358047, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
     * Ties.
     *
    x=c(1,1,1,1,2.1,7,2.4,3,5.2);
    y=c(2,4,4,4,1.2,3.4,9.6,8);
    wilcox.test(x,y,exact=F,corr=F)$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="less")$p.value
     *
     */
    @Test
    public void test_0055() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 1, 1, 1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 4, 4, 1.2, 3.4, 9.6, 8};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, false);
        assertEquals(18, instance.statistics(), 1e-15);

        assertEquals(0.0806194639485262, instance.pValue(), 1e-15);
        assertEquals(0.959690268025737, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.0403097319742631, instance.pValue1SidedLess, 1e-15);
    }

    /**
     * Ties.
     *
    x = c(8.5,8.5,11.5,11.5,8.5,8.5)
    y = c(2.5,2.5,5,6,2.5,2.5)
    wilcox.test(x,y,exact=F)$p.value
    wilcox.test(x,y,exact=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,alternative="less")$p.value
     */
    @Test
    public void test_0060() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{8.5, 8.5, 11.5, 11.5, 8.5, 8.5};
        samples[1] = new double[]{2.5, 2.5, 5, 6, 2.5, 2.5};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, true);
        assertEquals(36, instance.statistics(), 1e-15);

        assertEquals(0.003600915066787423, instance.pValue(), 1e-15);
        assertEquals(0.001800457533393712, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.998956311612396, instance.pValue1SidedLess, 1e-15);
    }

    /**
     * Ties.
     *
    x = c(8.5,8.5,11.5,11.5,8.5,8.5)
    y = c(2.5,2.5,5,6,2.5,2.5)
    wilcox.test(x,y,exact=F,corr=F)$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,corr=F,alternative="less")$p.value
     */
    @Test
    public void test_0065() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{8.5, 8.5, 11.5, 11.5, 8.5, 8.5};
        samples[1] = new double[]{2.5, 2.5, 5, 6, 2.5, 2.5};

        WilcoxonRankSum instance = new WilcoxonRankSum(samples[0], samples[1], 0, false, false);
        assertEquals(36, instance.statistics(), 1e-15);

        assertEquals(0.002750444303039227, instance.pValue(), 1e-15);
        assertEquals(0.001375222151519614, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.99862477784848, instance.pValue1SidedLess, 1e-15);
    }
}
