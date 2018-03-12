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

import static com.numericalmethod.suanshu.Constant.EULER_MASCHERONI;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.analysis.sequence.Summation;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static java.lang.Math.*;

/**
 * The Weibull distribution interpolates between the exponential distribution <i>k = 1</i> and the Rayleigh distribution (<i>k = 2</i>),
 * where <i>k</i> is the shape parameter.
 * <p/>
 * The R equivalent functions are {@code dweibull, pweibull, qweibull, rweibull}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Weibull_distribution">Wikipedia: WeibullDistribution distribution</a>
 */
public class WeibullDistribution implements ProbabilityDistribution {

    /** the scale parameter */
    private final double lambda;
    /** the shape parameter */
    private final double k;
    private static final Gamma gamma = new GammaLanczosQuick();

    /**
     * Construct a Weibull distribution.
     *
     * @param lambda λ
     * @param k      the shape parameter
     */
    public WeibullDistribution(double lambda, double k) {
        SuanShuUtils.assertArgument(lambda > 0, "lambda must be > 0");
        SuanShuUtils.assertArgument(k > 0, "k must be > 0");

        this.lambda = lambda;
        this.k = k;
    }

    @Override
    public double mean() {
        return lambda * gamma.evaluate(1d + 1d / k);
    }

    @Override
    public double median() {
        return lambda * pow(log(2), 1d / k);
    }

    @Override
    public double variance() {
        double mu = mean();
        return lambda * lambda * gamma.evaluate(1d + 2d / k) - mu * mu;
    }

    @Override
    public double skew() {
        double mu = mean();
        double sigma = sqrt(variance());
        double result = gamma.evaluate(1d + 3d / k) * lambda * lambda * lambda;
        result -= 3d * mu * sigma * sigma;
        result -= mu * mu * mu;
        result /= sigma * sigma * sigma;
        return result;
    }

    @Override
    public double kurtosis() {
        double mu = mean();
        double sigma = sqrt(variance());
        double skew = skew();
        double result = gamma.evaluate(1d + 4d / k) * lambda * lambda * lambda * lambda;
        result -= 4d * skew * sigma * sigma * sigma * mu;
        result -= 6d * mu * mu * sigma * sigma;
        result -= mu * mu * mu * mu;
        result /= sigma * sigma * sigma * sigma;
        result -= 3d;
        return result;
    }

    @Override
    public double entropy() {
        return EULER_MASCHERONI * (1d - 1d / k) + log(lambda / k) + 1d;
    }

    @Override
    public double cdf(double x) {
        assert x >= 0 : "x must be non-negative";
        return 1d - exp(-pow(x / lambda, k));
    }

    @Override
    public double quantile(double u) {
        return lambda * pow(-log(1d - u), 1d / k);
    }

    @Override
    public double density(double x) {
        if (x < 0) {
            return 0;
        }
        double result = k / lambda;
        result *= pow(x / lambda, k - 1);
        result *= exp(-pow(x / lambda, k));
        return result;
    }

    @Override
    public double moment(double x) {
        double sum = 0;

        final double t = x;
        Summation series = new Summation(new Summation.Term() {

            @Override
            public double evaluate(double n) {
                double term = gamma.evaluate(1d + n / k);
                for (int i = 1; i <= Math.round(n); ++i) {
                    term *= t * lambda / i;
                }
                return term;
            }
        });

        sum = series.sumToInfinity(0, 1);
        return sum;
    }
}
