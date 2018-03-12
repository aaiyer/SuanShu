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

import static com.numericalmethod.suanshu.Constant.*;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Erf;
import static java.lang.Math.*;

/**
 * The L2 norm of <i>(x1, x2)</i>, where <i>x<sub>i</sub></i>'s are normal, uncorrelated, equal variance and
 * have the Rayleigh distributions.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Rayleigh_distribution">Wikipedia: RayleighDistribution distribution</a>
 */
public class RayleighDistribution implements ProbabilityDistribution {

    /** the standard deviation */
    private final double sigma;

    /**
     * Construct a Rayleigh distribution.
     *
     * @param sigma the standard deviation
     */
    public RayleighDistribution(double sigma) {
        if (sigma <= 0) {
            throw new IllegalArgumentException("sigma must be > 0");
        }

        this.sigma = sigma;
    }

    @Override
    public double mean() {
        return sigma * sqrt(Math.PI / 2d);
    }

    @Override
    public double median() {
        return sigma * sqrt(log(4d));
    }

    @Override
    public double variance() {
        return sigma * sigma * (4d - Math.PI) / 2d;
    }

    @Override
    public double skew() {
        return 2d * ROOT_PI * (Math.PI - 3d) / Math.pow(4d - Math.PI, 1.5);
    }

    @Override
    public double kurtosis() {
        return -(6d * PI_SQ - 24d * Math.PI + 16d) / Math.pow(4d - Math.PI, 2);
    }

    @Override
    public double entropy() {
        return 1d + log(sigma / ROOT_2) + EULER_MASCHERONI / 2d;
    }

    @Override
    public double cdf(double x) {
        assert x >= 0 : "x must be non-negative";
        return 1d - exp(-x * x / 2 / sigma / sigma);
    }

    @Override
    public double quantile(double u) {
        return sigma * sqrt(-2d * log(1d - u));
    }

    @Override
    public double density(double x) {
        assert x >= 0 : "x must be positive";
        return x / sigma / sigma * exp(-x * x / 2 / sigma / sigma);
    }

    @Override
    public double moment(double t) {
        Erf erf = new Erf();
        return 1d + sigma * t * exp(sigma * sigma * t * t / 2) * sqrt(Math.PI / 2d)
                    * (erf.evaluate(sigma * t / ROOT_2) + 1d);
    }
}
