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
package com.numericalmethod.suanshu.stats.test.distribution.kolmogorov;

import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class KolmogorovSmirnov1SampleTest {

    /**
     * ks.test(rnorm(10), "pnorm")
     */
    @Test
    public void test_0010() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new NormalDistribution(), KolmogorovSmirnov.Side.TWO_SIDED);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.TWO_SIDED, instance.side);
        assertEquals(0.3014, instance.pValue(), 1e-4);
        assertEquals(0.2914, instance.Dn, 1e-4);
        assertEquals(0.2914, instance.Dnp, 1e-4);
        assertEquals(0.1823, instance.Dnn, 1e-4);
        assertEquals(0.2914, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is equal to the hypothesized distribution function", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * ks.test(rnorm(10), "pnorm", alternative ="greater")
     */
    @Test
    public void test_0020() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new NormalDistribution(), KolmogorovSmirnov.Side.GREATER);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.GREATER, instance.side);
        assertEquals(0.1510, instance.pValue(), 1e-4);
        assertEquals(0.2914, instance.Dn, 1e-4);
        assertEquals(0.2914, instance.Dnp, 1e-4);
        assertEquals(0.1823, instance.Dnn, 1e-4);
        assertEquals(0.2914, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is greater than the hypothesized distribution function", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * ks.test(rnorm(10), "pnorm", alternative ="less")
     */
    @Test
    public void test_0030() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new NormalDistribution(), KolmogorovSmirnov.Side.LESS);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.LESS, instance.side);
        assertEquals(0.4596, instance.pValue(), 1e-4);
        assertEquals(0.2914, instance.Dn, 1e-4);
        assertEquals(0.2914, instance.Dnp, 1e-4);
        assertEquals(0.1823, instance.Dnn, 1e-4);
        assertEquals(0.1823, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is less than the hypothesized distribution function", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * With duplicates/ties.
     */
    @Test
    public void test_0040() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, 1.2142038235675114
                },
                new NormalDistribution(), KolmogorovSmirnov.Side.TWO_SIDED);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.TWO_SIDED, instance.side);
        assertEquals(0.4029, instance.pValue(), 1e-4);
        assertEquals(0.2822971133259846, instance.Dn, 1e-15);
        assertEquals(0.2240379662551978, instance.Dnp, 1e-15);
        assertEquals(0.2822971133259846, instance.Dnn, 1e-15);
        assertEquals(0.2823, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is equal to the hypothesized distribution function", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }

    /**
     * With duplicates/ties.
     */
    @Test
    public void test_0050() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, 1.2142038235675114
                },
                new NormalDistribution(), KolmogorovSmirnov.Side.GREATER);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.GREATER, instance.side);
        assertEquals(0.3665, instance.pValue(), 1e-4);
        assertEquals(0.2822971133259846, instance.Dn, 1e-15);
        assertEquals(0.2240379662551978, instance.Dnp, 1e-15);
        assertEquals(0.2822971133259846, instance.Dnn, 1e-15);
        assertEquals(0.224, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is greater than the hypothesized distribution function", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }

    /**
     * With duplicates/ties.
     */
    @Test
    public void test_0060() {
        KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, 1.2142038235675114
                },
                new NormalDistribution(), KolmogorovSmirnov.Side.LESS);

        assertEquals(KolmogorovSmirnov.Type.ONE_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.LESS, instance.side);
        assertEquals(0.2031, instance.pValue(), 1e-4);
        assertEquals(0.2822971133259846, instance.Dn, 1e-15);
        assertEquals(0.2240379662551978, instance.Dnp, 1e-15);
        assertEquals(0.2822971133259846, instance.Dnn, 1e-15);
        assertEquals(0.2823, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is less than the hypothesized distribution function", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }
}
