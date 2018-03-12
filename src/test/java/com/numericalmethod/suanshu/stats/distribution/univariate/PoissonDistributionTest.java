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
package com.numericalmethod.suanshu.stats.distribution.univariate;

import com.numericalmethod.suanshu.stats.distribution.univariate.PoissonDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PoissonDistributionTest {

    @Test
    public void test_0010() {
        PoissonDistribution dist = new PoissonDistribution(1);
        assertEquals(0.00306566200976202, dist.density(5), 1e-16);
        assertEquals(0.3678794411714423, dist.cdf(0.2), 1e-16);
        assertEquals(2, dist.quantile(0.9), 0);
    }

    @Test
    public void test_0020() {
        PoissonDistribution dist = new PoissonDistribution(2.5);
        assertEquals(0.06680094289054266, dist.density(5), 1e-16);
        assertEquals(0.0820849986238988, dist.cdf(0.2), 1e-16);
        assertEquals(5, dist.quantile(0.9), 0);
    }

    @Test
    public void test_entropy_0010() {
        PoissonDistribution dist = new PoissonDistribution(100);
        assertEquals(dist.entropy2(), dist.entropy1(), 1e-8);
    }
}
