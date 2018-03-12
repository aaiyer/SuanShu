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

import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularized;
import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularizedInverse;
import com.numericalmethod.suanshu.analysis.function.special.beta.LogBeta;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The Student t distribution is the probability distribution of <i>t</i>, where
 * \[
 * t = \frac{\bar{x} - \mu}{s / \sqrt N}
 * \]
 * <ul>
 * <li>\(\bar{x}\) is the sample mean;
 * <li><i>μ</i> is the population mean;
 * <li><i>s</i> is the square root of the sample variance;
 * <li><i>N</i> is the sample size;
 * </ul>
 * The importance of the Student's distribution is
 * when (as in nearly all practical statistical work) the population standard deviation is unknown and has to be estimated from the data.
 * This is especially true when the sample size is small.
 * When the sample size is large, the Student's distribution converges to the Normal distribution.
 * <p/>
 * The R equivalent functions are {@code dt, pt, qt, rt}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Student_t">Wikipedia: Student's t-distribution</a>
 */
public class TDistribution implements ProbabilityDistribution {

    /** the degree of freedom */
    private final double v;
    private final BetaRegularized Ix;
    private final BetaRegularizedInverse IxInv;
    private static final LogBeta lbeta = new LogBeta();
    private static final LogGamma lgamma = new LogGamma();
    private static final Digamma digamma = new Digamma();

    /**
     * Construct a Student's t distribution.
     *
     * @param v the degree of freedom
     */
    public TDistribution(double v) {
        SuanShuUtils.assertArgument(v > 0, "only for v > 0");

        this.v = v;
        Ix = new BetaRegularized(v / 2, v / 2);
        IxInv = new BetaRegularizedInverse(v / 2, v / 2);
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when {@code v <= 1}
     */
    @Override
    public double mean() {
        SuanShuUtils.assertOrThrow(v > 1 ? null
                                   : new UnsupportedOperationException("only for v > 1"));

        return 0;
    }

    @Override
    public double median() {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when {@code v < 2}
     */
    @Override
    public double variance() {
        SuanShuUtils.assertOrThrow(v >= 2 ? null
                                   : new UnsupportedOperationException("only for v >= 2"));

        if (v == 2) {
            return Double.POSITIVE_INFINITY;
        } else {
            return v / (v - 2);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when {@code v <= 3}
     */
    @Override
    public double skew() {
        SuanShuUtils.assertOrThrow(v > 3 ? null
                                   : new UnsupportedOperationException("only for v > 3"));

        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when {@code v <= 4}
     */
    @Override
    public double kurtosis() {
        SuanShuUtils.assertOrThrow(v > 4 ? null
                                   : new UnsupportedOperationException("only for v > 4"));

        return 6 / (v - 4);
    }

    @Override
    public double entropy() {
        double result = (v + 1) / 2;
        result *= digamma.evaluate((1 + v) / 2) - digamma.evaluate(v / 2);
        result += log(sqrt(v)) + lbeta.evaluate(v / 2, 0.5);

        return result;
    }

    @Override
    public double cdf(double x) {
        double t = x;
        t /= 2 * sqrt(x * x + v);
        t += 0.5;
        return Ix.evaluate(t);
    }

    @Override
    public double density(double x) {
        //special cases
        if (isZero(x, 0)) {
            return 0;
        }

        double result = lgamma.evaluate((v + 1) / 2);
        result -= log(sqrt(v * Math.PI)) + lgamma.evaluate(v / 2);
        result += -(v + 1) / 2 * log(1 + x * x / v);
        result = exp(result);

        return result;
    }

    @Override
    public double quantile(double u) {
        double t = IxInv.evaluate(u);
        double x = 1d / 4 / (t - 0.5) / (t - 0.5) / v - 1 / v;
        x = 1 / x;
        x = sqrt(x);//x > 0 here
        x = t - 0.5 > 0 ? x : -x;
        return x;
    }

    @Override
    public double moment(double x) {
        throw new UnsupportedOperationException("not defined.");
    }
}
