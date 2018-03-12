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
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The F distribution is the distribution of the ratio of two independent chi-squared variates.
 * <p/>
 * The R equivalent functions are {@code df, pf, qf, rf}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/F_distribution">Wikipedia: FDistribution-distribution</a>
 */
public class FDistribution implements ProbabilityDistribution {

    /** the first degree of freedom */
    private final double df1;
    /** the second degree of freedom */
    private final double df2;
    private final BetaRegularized Ix;
    private final BetaRegularizedInverse IxInv;
    private static final LogBeta lbeta = new LogBeta();

    /**
     * Construct an F distribution.
     *
     * @param df1 the first degree of freedom
     * @param df2 the second degree of freedom
     */
    public FDistribution(double df1, double df2) {
        SuanShuUtils.assertArgument(df1 > 0, "df1 must be > 0");
        SuanShuUtils.assertArgument(df2 > 0, "df2 must be > 0");

        this.df1 = df1;
        this.df2 = df2;
        Ix = new BetaRegularized(df1 / 2, df2 / 2);
        IxInv = new BetaRegularizedInverse(df1 / 2, df2 / 2);
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when <i>df2 &le; 2</i>
     */
    @Override
    public double mean() {
        SuanShuUtils.assertOrThrow(df2 > 2 ? null
                                   : new UnsupportedOperationException("only supported for df2 > 2"));

        return df2 / (df2 - 2);
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double median() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when <i>df2 &le; 4</i>
     */
    @Override
    public double variance() {
        SuanShuUtils.assertOrThrow(df2 > 4 ? null
                                   : new UnsupportedOperationException("only supported for df2 > 4"));

        double result = 2 * df2 * df2 * (df1 + df2 - 2);
        result /= df1 * (df2 - 2) * (df2 - 2) * (df2 - 4);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when <i>df2 &le; 6</i>
     */
    @Override
    public double skew() {
        SuanShuUtils.assertOrThrow(df2 > 6 ? null
                                   : new UnsupportedOperationException("only supported for df2 > 6"));

        double result = (2 * df1 + df2 - 2) * sqrt(8 * (df2 - 4));
        result /= (df2 - 6) * sqrt(df1 * (df1 + df2 - 2));
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException when <i>df2 &le; 8</i>
     */
    @Override
    public double kurtosis() {
        SuanShuUtils.assertOrThrow(df2 > 8 ? null
                                   : new UnsupportedOperationException("only supported for df2 > 8"));

        double A = 5 * df2 * df2 * df1 - 22 * df1 * df1 + 5 * df2 * df1 * df1 - 16;
        double result = 20 * df2 - 8 * df2 * df2 + df2 * df2 * df2 + 44 * df1 - 32 * df1 * df2 + A;
        result /= df1 * (df2 - 6) * (df2 - 8) * (df1 + df2 - 2) / 12;
        return result - 3;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double entropy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double cdf(double x) {
        SuanShuUtils.assertArgument(x >= 0, "x must be >= 0");
        return Ix.evaluate(df1 * x / (df1 * x + df2));
    }

    @Override
    public double density(double x) {
        SuanShuUtils.assertArgument(x >= 0, "x must be >= 0");

        //special cases
        if (isZero(x, 0)) {
            if (df1 < 2) {
                return Double.POSITIVE_INFINITY;
            } else if (df1 == 2) {
                return 1;
            } else {
                return 0;
            }
        }

        double result = df1 * log(df1 * x);
        result += df2 * log(df2);
        result -= (df1 + df2) * log(df1 * x + df2);
        result /= 2;
        result -= log(x);
        result -= lbeta.evaluate(df1 / 2, df2 / 2);
        result = exp(result);

        return result;
    }

    @Override
    public double quantile(double u) {
        double x = IxInv.evaluate(u);
        return df2 * x / (df1 - df1 * x);
    }

    @Override
    public double moment(double x) {
        throw new UnsupportedOperationException("does not exist.");
    }
}
