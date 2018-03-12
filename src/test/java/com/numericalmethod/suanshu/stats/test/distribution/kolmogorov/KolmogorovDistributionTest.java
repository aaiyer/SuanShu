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
public class KolmogorovDistributionTest {

    @Test
    public void testKolmogorov_0010() {
        assertEquals(0.628479615456504275298526691328, (new KolmogorovDistribution(10)).cdf(0.274), 1e-15);
        assertEquals(0.628479615456504275298526691328, (new KolmogorovDistribution(10, Integer.MAX_VALUE, false)).cdf(0.274), 1e-15);
        assertEquals(0.9967694319171325, (new KolmogorovDistribution(2000, Integer.MAX_VALUE, false)).cdf(0.04), 1e-13);
        assertEquals(0.9999989395692991, (new KolmogorovDistribution(2000, Integer.MAX_VALUE, false)).cdf(0.06), 1e-13);
        assertEquals(0.9994523491380971, (new KolmogorovDistribution(16000, Integer.MAX_VALUE, false)).cdf(0.016), 1e-13);
    }

    @Test
    public void testKolmogorov_0020() {
        assertEquals(0.9999989395692991, (new KolmogorovDistribution(2000, Integer.MAX_VALUE, true)).cdf(0.06), 1e-8);
        assertEquals(0.9994523491380971, (new KolmogorovDistribution(16000, Integer.MAX_VALUE, true)).cdf(0.016), 1e-6);
    }

    @Test
    public void testKolmogorov_0030() {
        assertEquals(0.9999293653066222, (new KolmogorovDistribution(20000, 1, false)).cdf(0.016), 1e-6);
        assertEquals(0.9994523491380971, (new KolmogorovDistribution(16000, 1, false)).cdf(0.016), 1e-5);
    }

    /**
     * TODO: Out of heap space.
     */
//    @Test
    public void testKolmogorov_0040() {
        double cdf = new KolmogorovDistribution(10000, Integer.MAX_VALUE, false).cdf(0.8);
    }
}
