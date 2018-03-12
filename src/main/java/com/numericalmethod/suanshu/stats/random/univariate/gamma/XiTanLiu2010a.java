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
 * This implementation is Algorithm 3.1 in the reference. It does not restrict the range of the shape parameter.
 * It, however, assumes the scale parameter is always 1.
 * Note that for when the shape parameter &lt; 0.01, it returns numbers in logarithmic scale.
 * Take exp to convert.
 *
 * @author Haksun Li
 * @see "B. Xi, K. M. Tan, and C. Liu, "Two New Ratio-of-Uniforms Gamma Random Number Generators," Technical Report #10-03, Purdue University."
 */
public class XiTanLiu2010a implements RandomGammaGenerator {

    private final double k;
    private final RandomLongGenerator uniform;
    private final double theta, c, B_max, B_min;

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k       the shape parameter
     * @param uniform a uniform random number generator
     */
    public XiTanLiu2010a(double k, RandomLongGenerator uniform) {
        assertArgument(k > 0, "the shape parameter must be > 0");

        this.k = k;
        this.uniform = uniform;

        this.theta = log(k);
        this.c = sqrt(k);
        double bs = bs(theta);
        double bw = bw(theta);
        B_max = exp(bs);
        B_min = -exp(bw);
    }

    /**
     * Construct a random number generator to sample from the gamma distribution.
     *
     * @param k the shape parameter
     */
    public XiTanLiu2010a(double k) {
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
            double v = uniform.nextDouble();
            v *= B_max - B_min;
            v += B_min;

            double t = v / u;
            double t2 = t / c + theta;
            double t1 = exp(t2);

            if (2 * log(u) <= k + c * t - t1) {
                if (k >= 0.01) {
                    return t1;
                } else {
                    return t2;
                }
            }
        }
    }

    //Table 3.1
    private double bw(double theta) {
        if (theta > 1.764216686288215) {
            return -0.048065894062201;
        } else if (0.521223243207446 <= theta && theta < 1.764216686288215) {
            return -0.084763530978316 * theta + 0.101475344169199;
        } else if (0.209314923020777 <= theta && theta < 0.521223243207446) {
            return -0.135460234584798 * theta + 0.127899644442896;
        } else {//theta > 0.209314923020777
            return -0.306852819440055 - theta / 2. + k / 2.;
        }
    }

    //Table 3.2
    private double bs(double theta) {
        if (theta > 1.448931546292675) {
            return -0.153426409720027;
        } else if (-3.333189906461192 <= theta && theta < 1.448931546292675) {
            return 0.124651796958072 * theta - 0.334038330634647;
        } else {//theta > -3.333189906461192
            return 0.306252995504409 * theta + 0.271272951361260;
        }
    }
}
