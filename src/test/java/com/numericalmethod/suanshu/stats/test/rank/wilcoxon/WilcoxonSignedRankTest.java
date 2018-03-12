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
public class WilcoxonSignedRankTest {

    /**
     *
    x=c(1.2,3.1,5.5,2.3,3.5,5.2)
    wilcox.test(x,exact=F,correct=F)
    wilcox.test(x,exact=F,correct=F,alternative="greater")$p.value
    wilcox.test(x,exact=F,correct=F,alternative="less")$p.value
     */
    @Test
    public void test_0010() {
        double[] sample = new double[]{1.2, 3.1, 5.5, 2.3, 3.5, 5.2};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample, null, 0, false);

        assertEquals(21, instance.statistics(), 1e-15);
        assertEquals(0.02771, instance.pValue(), 1e-5);
        assertEquals(0.01385392467903993, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.98614607532096, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1.2,3.1,5.5,2.3,3.5,5.2)
    wilcox.test(x,exact=T)
    wilcox.test(x,exact=T,alternative="greater")$p.value
    wilcox.test(x,exact=T,alternative="less")$p.value
     */
    @Test
    public void test_0015() {
        double[] sample = new double[]{1.2, 3.1, 5.5, 2.3, 3.5, 5.2};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample, null, 0, true);

        assertEquals(21, instance.statistics(), 1e-15);
        assertEquals(0.03125, instance.pValue(), 1e-5);
        assertEquals(0.015625, instance.pValue1SidedGreater, 1e-15);
        assertEquals(1, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1.3,5.4,7.6,7.2,3.5);
    y=c(2.7,5.2,6.3,4.4,9.8);
    wilcox.test(x,y,mu=2,exact=F,paired=T,correct=F)
    wilcox.test(x,y,mu=2,exact=F,paired=T,correct=F,alternative="greater")$p.value
    wilcox.test(x,y,mu=2,exact=F,paired=T,correct=F,alternative="less")$p.value
     */
    @Test
    public void test_0020() {
        double[] sample1 = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        double[] sample2 = new double[]{2.7, 5.2, 6.3, 4.4, 9.8};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample1, sample2, 2, false);

        assertEquals(2, instance.statistics(), 1e-15);
        assertEquals(0.138, instance.pValue(), 1e-3);
        assertEquals(0.93099463121567, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.06900536878432977, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1.3,5.4,7.6,7.2,3.5);
    y=c(2.7,5.2,6.3,4.4,9.8);
    wilcox.test(x,y,mu=2,exact=T,paired=T)
    wilcox.test(x,y,mu=2,exact=T,paired=T,correct=F,alternative="greater")$p.value
    wilcox.test(x,y,mu=2,exact=T,paired=T,correct=F,alternative="less")$p.value
     */
    @Test
    public void test_0025() {
        double[] sample1 = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        double[] sample2 = new double[]{2.7, 5.2, 6.3, 4.4, 9.8};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample1, sample2, 2, true);

        assertEquals(2, instance.statistics(), 1e-15);
        assertEquals(0.1875, instance.pValue(), 1e-4);
        assertEquals(0.9375, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.09375, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1,2,7,2.4,3,5.2);
    y=c(2,4,1,4,9.6,8);
    wilcox.test(x,y,exact=F,paired=T,correct=F)
    wilcox.test(x,y,exact=F,paired=T,correct=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,paired=T,correct=F,alternative="less")$p.value
     */
    @Test
    public void test_0030() {
        double[] sample1 = new double[]{1, 2, 7, 2.4, 3, 5.2};
        double[] sample2 = new double[]{2, 4, 1, 4, 9.6, 8};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample1, sample2, 0, false);

        assertEquals(5.0, instance.statistics(), 1e-15);
        assertEquals(0.2489, instance.pValue(), 1e-4);
        assertEquals(0.875568062531039, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.124431937468961, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
    x=c(1,2,7,2.4,3,5.2);
    y=c(2,4,1,4,9.6,8);
    wilcox.test(x,y,exact=T,paired=T)
    wilcox.test(x,y,exact=T,paired=T,alternative="greater")$p.value
    wilcox.test(x,y,exact=T,paired=T,alternative="less")$p.value
     */
    @Test
    public void test_0040() {
        double[] sample1 = new double[]{1, 2, 7, 2.4, 3, 5.2};
        double[] sample2 = new double[]{2, 4, 1, 4, 9.6, 8};
        WilcoxonSignedRank instance = new WilcoxonSignedRank(sample1, sample2, 0, true);

        assertEquals(5.0, instance.statistics(), 1e-15);
        assertEquals(0.3125, instance.pValue(), 1e-4);
        assertEquals(0.890625, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.15625, instance.pValue1SidedLess, 1e-15);
    }

    /**
     *
     * Ties.
     *
    x=c(1,1,1,1,2.1,7,2.4,3,5.2);
    y=c(2,4,4,4,1.2,3.4,9.6,8,8);
    wilcox.test(x,y,exact=F,paired=T,correct=F)
    wilcox.test(x,y,exact=F,paired=T,correct=F,alternative="greater")$p.value
    wilcox.test(x,y,exact=F,paired=T,correct=F,alternative="less")$p.value
     *
     */
    @Test
    public void test_0050() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1, 1, 1, 1, 2.1, 7, 2.4, 3, 5.2};
        samples[1] = new double[]{2, 4, 4, 4, 1.2, 3.4, 9.6, 8, 8};

        WilcoxonSignedRank instance = new WilcoxonSignedRank(samples[0], samples[1], 0, false);
        assertEquals(8, instance.statistics(), 1e-15);

        assertEquals(0.08473, instance.pValue(), 1e-5);
        assertEquals(0.957634439052641, instance.pValue1SidedGreater, 1e-15);
        assertEquals(0.04236556094735867, instance.pValue1SidedLess, 1e-15);
    }
}
