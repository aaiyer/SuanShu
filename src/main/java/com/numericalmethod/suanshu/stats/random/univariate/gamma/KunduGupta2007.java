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
import static java.lang.Math.*;

/**
 * Kundu-Gupta propose a very convenient way to generate gamma random variables using generalized exponential distribution,
 * when the shape parameter lies between 0 and 1.
 * This implementation is Algorithm 3 in the reference.
 *
 * @author Haksun Li
 * @see "Debasis Kundu, Rameshwar D. Gupta, "A convenient way of generating gamma random variables using generalized exponential distribution," Computational Statistics & Data Analysis, Volume 51 Issue 6, March, 2007."
 */
public class KunduGupta2007 implements RandomGammaGenerator {

    private final double theta;
    private final RandomLongGenerator uniform;
    private final double k;
    private final double a, b, c, d;
    private final double aab, k1;

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k       the shape parameter
     * @param theta   the scale parameter
     * @param uniform a uniform random number generator
     */
    public KunduGupta2007(double k, double theta, RandomLongGenerator uniform) {
        assertArgument(k > 0, "the shape parameter must be > 0");
        assertArgument(k < 1, "the shape parameter must be < 1");
        assertArgument(theta > 0, "the scale parameter must be > 0");

        this.theta = theta;
        this.uniform = uniform;

        this.k = k;
        this.k1 = k - 1;

        this.d = 1.0334 - 0.0766 * exp(2.2942 * k);
        this.a = pow(2, k) * pow(1 - exp(-d / 2.), k);
        this.b = k * pow(d, k1) * exp(-d);
        this.c = this.a + this.b;

        this.aab = a / (a + b);
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        for (;;) {
            double u = uniform.nextDouble();

            double x;
            if (u < aab) {
                x = 1. - 0.5 * (pow(c * u, 1. / k));
                x = log(x);
                x *= -2.;
            } else {
                x = c * (1. - u);
                x /= k * pow(d, k1);
                x = -log(x);
            }

            double v = uniform.nextDouble();

            if (x < d) {
                double t1 = pow(x, k1) * exp(-x / 2.);
                t1 /= pow(2, k1) * pow(1 - exp(-x / 2.), k1);
                if (v <= t1) {
                    return x * theta;
                }
            } else {// x > d
                double t2 = pow(d / x, 1. - k);
                if (v <= t2) {
                    return x * theta;
                }
            }
        }
    }
}
