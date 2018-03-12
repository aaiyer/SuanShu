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
package com.numericalmethod.suanshu.stats.random.univariate.beta;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * Cheng, 1978, is a new rejection method for generating beta variates.
 * The method is compared with previously published methods both theoretically and through computer timings.
 * It is suggested that the method has advantages in both speed and programming simplicity over previous methods,
 * especially for "difficult" combinations of parameter values.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"R. C. H. Cheng, "Generating beta variates with nonintegral shape parameters," Communications of the ACM 21, 317-322. 1978."
 * <li>"http://svn.r-project.org/R/trunk/src/nmath/rbeta.c"
 * </ul>
 */
public class Cheng1978 implements RandomBetaGenerator {

    private double aa, bb;
    private double a, b, alpha, beta, delta, k1, k2, gamma;
    private final RandomLongGenerator uniform;
    private static final double EXP_MAX = log(Double.MAX_VALUE);

    /**
     * Construct a random number generator to sample from the beta distribution.
     *
     * @param aa      the degree of freedom
     * @param bb      the degree of freedom
     * @param uniform a uniform random number generator
     */
    public Cheng1978(double aa, double bb, RandomLongGenerator uniform) {
        assertArgument(aa > 0, "alpha must be > 0");
        assertArgument(bb > 0, "beta must be > 0");

        this.aa = aa;
        this.bb = bb;
        this.uniform = uniform;

        a = Math.min(aa, bb);
        b = Math.max(aa, bb);//a <= b
        alpha = a + b;

        if (a <= 1.) {// algorithm BC
            beta = 1.0 / a;
            delta = 1.0 + b - a;
            k1 = delta * (0.0138889 + 0.0416667 * a) / (b * beta - 0.777778);
            k2 = 0.25 + (0.5 + 0.25 / delta) * a;
        } else {// algorithm BB
            beta = Math.sqrt((alpha - 2.0) / (2.0 * a * b - alpha));
            gamma = a + 1.0 / beta;
        }
    }

    /**
     * Construct a random number generator to sample from the beta distribution.
     *
     * @param aa the degree of freedom
     * @param bb the degree of freedom
     */
    public Cheng1978(double aa, double bb) {
        this(aa, bb, new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        if (a <= 1.) {
            return BC();
        } else {
            return BB();
        }
    }

    private double v_from__bet(double u1) {
        double v = beta * log(u1 / (1.0 - u1));
        return v;
    }

    private double w_from__bet(double v, double AA) {
        double w;

        if (v <= EXP_MAX) {
            w = AA * exp(v);
            if (Double.isInfinite(w)) {
                w = Double.MAX_VALUE;
            }
        } else {
            w = Double.MAX_VALUE;
        }

        return w;
    }

    private double BC() {
        double u1, u2, y, z;// declaration here to speed up performance by avoiding allocation and deallocation of local variables in the loop?
        double v, w;

        for (;;) {
            u1 = uniform.nextDouble();
            u2 = uniform.nextDouble();
            if (u1 < 0.5) {
                y = u1 * u2;
                z = u1 * y;
                if (0.25 * u2 + z - y >= k1) {
                    continue;
                }
            } else {
                z = u1 * u1 * u2;
                if (z <= 0.25) {
                    v = v_from__bet(u1);
                    w = w_from__bet(v, b);
                    break;
                }
                if (z >= k2) {
                    continue;
                }
            }

            v = v_from__bet(u1);
            w = w_from__bet(v, b);

            if (alpha * (log(alpha / (a + w)) + v) - 1.3862944 >= log(z)) {
                break;
            }
        }

        return (aa == a) ? a / (a + w) : w / (a + w);
    }

    private double BB() {
        double u1, u2, z, r, s, t;// declaration here to speed up performance by avoiding allocation and deallocation of local variables in the loop?
        double v, w;

        do {
            u1 = uniform.nextDouble();
            u2 = uniform.nextDouble();

            v = v_from__bet(u1);
            w = w_from__bet(v, a);

            z = u1 * u1 * u2;
            r = gamma * v - 1.3862944;
            s = a + r - w;
            if (s + 2.609438 >= 5.0 * z) {
                break;
            }
            t = log(z);
            if (s > t) {
                break;
            }
        } while (r + alpha * log(alpha / (b + w)) < t);

        return (aa != a) ? b / (b + w) : w / (b + w);
    }
}
