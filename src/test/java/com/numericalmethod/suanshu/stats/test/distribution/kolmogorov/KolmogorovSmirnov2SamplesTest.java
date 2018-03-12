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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class KolmogorovSmirnov2SamplesTest {

    /**
     * ks.test(rnorm(10), rnorm(15))
     */
    @Test
    public void test_0010() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new double[]{
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.TWO_SIDED);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.TWO_SIDED, instance.side);
        assertEquals(0.7315, instance.pValue(), 1e-4);
        assertEquals(0.2667, instance.Dn, 1e-4);
        assertEquals(0.2667, instance.Dnp, 1e-4);
        assertEquals(0.1, instance.Dnn, 1e-4);
        assertEquals(0.2667, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is equal to the distribution function of the other sample", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * ks.test(rnorm(10), rnorm(15))
     */
    @Test
    public void test_0020() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new double[]{
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.GREATER);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.GREATER, instance.side);
        assertEquals(0.426, instance.pValue(), 1e-4);
        assertEquals(0.2667, instance.Dn, 1e-4);
        assertEquals(0.2667, instance.Dnp, 1e-4);
        assertEquals(0.1, instance.Dnn, 1e-4);
        assertEquals(0.2667, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is greater than the distribution function of the other sample", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * ks.test(rnorm(10), rnorm(15))
     */
    @Test
    public void test_0030() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },//x = rnorm(10)
                new double[]{
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.LESS);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.LESS, instance.side);
        assertEquals(0.887, instance.pValue(), 1e-4);
        assertEquals(0.2667, instance.Dn, 1e-4);
        assertEquals(0.2667, instance.Dnp, 1e-4);
        assertEquals(0.1, instance.Dnn, 1e-4);
        assertEquals(0.1, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is less than the distribution function of the other sample", instance.getNullHypothesis());
        assertFalse(instance.ties);
    }

    /**
     * with ties
     */
    @Test
    public void test_0040() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },
                new double[]{
                    -1.2340784297133520,//tie
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.GREATER);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.GREATER, instance.side);
        assertEquals(0.5736, instance.pValue(), 1e-4);
        assertEquals(0.2125, instance.Dnp, 1e-4);
        assertEquals(0.2125, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is greater than the distribution function of the other sample", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }

    /**
     * with ties
     */
    @Test
    public void test_0050() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },
                new double[]{
                    -1.2340784297133520,//tie
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.LESS);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.LESS, instance.side);
        assertEquals(0.825, instance.pValue(), 1e-4);
        assertEquals(0.125, instance.Dnn, 1e-4);
        assertEquals(0.125, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is less than the distribution function of the other sample", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }

    /**
     * with ties
     *
     * R does not give the exact value.
     */
    @Test
    public void test_0060() {
        KolmogorovSmirnov2Samples instance = new KolmogorovSmirnov2Samples(
                new double[]{
                    1.2142038235675114, 0.8271665834857130, -2.2786245743283295, 0.8414895245471727,
                    -1.4327682855296735, -0.2501807766164897, -1.9512765152306415, 0.6963626117638846,
                    0.4741320101265005, -1.2340784297133520
                },
                new double[]{
                    -1.2340784297133520,//tie
                    1.7996197748754565, -1.1371109188816089, 0.8179707525071304, 0.3809791236763478,
                    0.1644848304811257, 0.3397412780581336, -2.2571685407244795, 0.4137315314876659,
                    0.7318687611171864, 0.9905218801425318, -0.4748590846019594, 0.8882674167954235,
                    1.0534065683777052, 0.2553123235884622, -2.3172807717538038},//x = rnorm(15)
                KolmogorovSmirnov.Side.TWO_SIDED);

        assertEquals(KolmogorovSmirnov.Type.TWO_SAMPLE, instance.type);
        assertEquals(KolmogorovSmirnov.Side.TWO_SIDED, instance.side);
        assertEquals(0.8734153718135411, instance.pValue(), 1e-4);//R gives 0.9439
        assertEquals(0.2125, instance.Dn, 1e-4);
        assertEquals(0.2125, instance.Dnp, 1e-4);
        assertEquals(0.125, instance.Dnn, 1e-4);
        assertEquals(0.2125, instance.statistics(), 1e-4);
        assertEquals("the true distribution function of sample is equal to the distribution function of the other sample", instance.getNullHypothesis());
        assertTrue(instance.ties);
    }
}
