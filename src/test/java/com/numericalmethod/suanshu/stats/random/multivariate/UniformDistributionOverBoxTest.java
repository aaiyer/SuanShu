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
package com.numericalmethod.suanshu.stats.random.multivariate;

import com.numericalmethod.suanshu.interval.RealInterval;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class UniformDistributionOverBoxTest {

    @Test
    public void test_0010() {
        UniformDistributionOverBox instance = new UniformDistributionOverBox(new RealInterval[]{
                    new RealInterval(-1.0, 1.0),
                    new RealInterval(0.0, 10.0)
                });

        instance.seed(123456789L);

        double[] mu = new double[2];

        int N = 100000;
        for (int i = 0; i < N; ++i) {
            double[] v = instance.nextVector();
            mu[0] += v[0];
            mu[1] += v[1];
        }

        assertEquals(0.0, mu[0] / N, 1e-3);
        assertEquals(5.0, mu[1] / N, 1e-2);
    }
}
