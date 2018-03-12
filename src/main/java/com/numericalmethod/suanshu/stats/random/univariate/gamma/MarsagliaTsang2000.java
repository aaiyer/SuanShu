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
import com.numericalmethod.suanshu.stats.random.univariate.normal.RandomStandardNormalNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static java.lang.Math.*;

/**
 * Marsaglia-Tsang is a procedure for generating a gamma variate as the cube of a suitably scaled normal variate.
 * It is fast and simple, assuming one has a fast way to generate standard normal variables.
 * This algorithm is (significantly) slowed down for when k &lt; 1 because an extra uniform rng needs to be generated.
 * Consider using {@link KunduGupta2007} instead for k &lt; 1.
 *
 * @author Haksun Li
 * @see "G. Marsaglia, W. W. Tsang, "A simple method for generating gamma variables," ACM Transactions on Mathematical Software (TOMS), Vol 26, Issue 3, Sept., 2000."
 */
public class MarsagliaTsang2000 implements RandomGammaGenerator {

    private final double theta;
    private final RandomStandardNormalNumberGenerator normal;
    private final RandomLongGenerator uniform;
    private final Generate generate;

    private interface Generate {

        public double nextDouble();
    }

    private class Kg1 implements Generate {//k >= 1

        private final double d;
        private final double c;

        private Kg1(double k) {
            d = k - 1. / 3.;
            c = 1. / sqrt(9 * d);
        }

        @Override
        public double nextDouble() {
            for (;;) {
                double x;//standard normal
                double v;
                do {
                    x = normal.nextDouble();// x >= - sqrt(9.*k-3.)
                    v = 1. + c * x;
                } while (v <= 0);

                v = v * v * v;

                double u = uniform.nextDouble();

                double x2 = x * x;
                double x4 = x2 * x2;
                if (u < 1.0 - 0.0331 * x4) {
                    return d * v * theta;
                }

                if (log(u) < 0.5 * x2 + d * (1.0 - v + log(v))) {
                    return d * v * theta;
                }
            }
        }
    }

    private class Kl1 implements Generate {//k < 1

        private final Kg1 kg1;
        private final double one_over_k;

        private Kl1(double k) {
            kg1 = new Kg1(k + 1);
            one_over_k = 1. / k;
        }

        @Override
        public double nextDouble() {
            double z = kg1.nextDouble();
            double u = uniform.nextDouble();

            double x = pow(u, one_over_k);
            x *= z;

            return x;
        }
    }

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k       the shape parameter
     * @param theta   the scale parameter
     * @param normal  a standard normal random number generator
     * @param uniform a uniform random number generator
     */
    public MarsagliaTsang2000(double k, double theta, RandomStandardNormalNumberGenerator normal, RandomLongGenerator uniform) {
        assertArgument(k > 0, "the shape parameter must be > 0");
        assertArgument(theta > 0, "the scale parameter must be > 0");

        this.theta = theta;
        this.normal = normal;
        this.uniform = uniform;


        //an efficient implementation to avoid checking each time
        this.generate = k >= 1 ? new Kg1(k) : new Kl1(k);
    }

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k     the shape parameter
     * @param theta the scale parameter
     */
    public MarsagliaTsang2000(double k, double theta) {
        this(k, theta, new StandardNormalRng(), new UniformRng());
    }

    /**
     * Construct a random number generator to sample from the standard gamma distribution.
     */
    public MarsagliaTsang2000() {
        this(1d, 1d);
    }

    @Override
    public void seed(long... seeds) {
        normal.seed(seeds);
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        return generate.nextDouble();
    }
}
