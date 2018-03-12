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
package com.numericalmethod.suanshu.stats.random.univariate.gamma;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static java.lang.Math.*;

/**
 * Xi, Tan and Liu proposed two simple algorithms to generate gamma random numbers based on
 * the ratio-of-uniforms method and logarithmic transformations of gamma random variable.
 * This implementation is Algorithm 4.1 in the reference. It restricts the range of the shape parameter to be equal to or less than 1.
 * It, however, assumes the scale parameter is always 1.
 * Note that for when the shape parameter &lt; 0.01, it returns numbers in logarithmic scale.
 * Take exp to convert.
 *
 * @author Haksun Li
 * @see "B. Xi, K. M. Tan, and C. Liu, "Two New Ratio-of-Uniforms Gamma Random Number Generators," Technical Report #10-03, Purdue University."
 */
public class XiTanLiu2010b implements RandomGammaGenerator {

    private final double k;
    private final RandomLongGenerator uniform;
    private final double u_max, v_min, v_max;
    private final double dv;
    private static final double e = exp(1);

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k       the shape parameter
     * @param uniform a uniform random number generator
     */
    public XiTanLiu2010b(double k, RandomLongGenerator uniform) {
        assertArgument(k > 0, "the shape parameter must be > 0");
        assertArgument(k <= 1, "the shape parameter must be <= 1");

        this.k = k;
        this.uniform = uniform;

        this.u_max = pow(k / e, k / 2.);
        this.v_min = -2. / e;
        this.v_max = 2. * k / e / (e - k);

        this.dv = v_max - v_min;
    }

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k the shape parameter
     */
    public XiTanLiu2010b(double k) {
        this(k, new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        for (;;) {
            double u = uniform.nextDouble();
            u *= u_max;

            double t = uniform.nextDouble();
            t *= dv;
            t += v_min;
            t /= u;

            double t2 = t / k;
            double t1 = exp(t2);

            if (2 * log(u) <= t - t1) {
                if (k >= 0.01) {
                    return t1;
                } else {
                    return t2;
                }
            }
        }
    }
}
