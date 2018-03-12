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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.garch;

import static com.numericalmethod.suanshu.analysis.function.FunctionOps.dotProduct;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.min;
import java.util.Arrays;

/**
 * This class represents a GARCH specification.
 *
 * The GARCH(p, q) model (where p is the order of the GARCH terms h_(t-i) and q is the order of the ARCH terms e_(t-i)^2) is given by
 *
 * <blockquote><code><pre>
 * h(t)= α0 + Σ(α_i * e_(t-i)^2) + Σ(β_i * h_(t-i))
 * </pre></code></blockquote>
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_conditional_heteroskedasticity#GARCH">Wikipedia: GARCH</a>
 */
public class GARCHModel {

    /**
     * the constant term
     */
    private double a0;
    /**
     * the ARCH coefficients; {@code null} if no ARCH coefficients
     */
    private double[] a;
    /*
     * the GARCH coefficients; {@code null} if no GARCH coefficients
     */
    private double[] b;

    /**
     * Construct a GARCH model.
     *
     * @param a0 the constant term
     * @param a the ARCH coefficients; {@code null} if no ARCH coefficients
     * @param b the GARCH coefficients; {@code null} if no GARCH coefficients
     */
    public GARCHModel(double a0, double[] a, double[] b) {//TODO: use Vector
        assertArgument(a0 > 0, "a0 > 0");
        assertArgument(min(a) >= 0, "{a} >= 0");
        assertArgument(min(b) >= 0, "{b} >= 0");

        double sum = 0;
        for (int i = 1; i < a.length; ++i) {
            sum += a[i];
        }
        for (int i = 1; i < b.length; ++i) {
            sum += b[i];
        }
        assertArgument(sum < 1, "Σ{α} + Σ{β} < 1, (except for α<sub>0</sub>)");

        this.a0 = a0;
        this.a = a != null ? Arrays.copyOf(a, a.length) : null;
        this.b = b != null ? Arrays.copyOf(b, b.length) : null;
    }

    /**
     * Copy constructor.
     *
     * @param that a GARCH model
     */
    public GARCHModel(GARCHModel that) {
        this.a0 = that.a0;
        this.a = that.a != null ? Arrays.copyOf(that.a, that.a.length) : null;
        this.b = that.b != null ? Arrays.copyOf(that.b, that.b.length) : null;
    }

    /**
     * Get the constant term.
     *
     * @return the constant term
     */
    public double a0() {
        return a0;
    }

    /**
     * Get the ARCH coefficients.
     *
     * @return the ARCH coefficients; could be {@code null}
     */
    public double[] alpha() {
        return a != null ? Arrays.copyOf(a, a.length) : null;
    }

    /**
     * Get the GARCH coefficients.
     *
     * @return the GARCH coefficients; could be {@code null}
     */
    public double[] beta() {
        return b != null ? Arrays.copyOf(b, b.length) : null;
    }

    /**
     * Get the number of GARCH terms.
     *
     * @return the number of GARCH terms
     */
    public int p() {
        return b != null ? b.length : 0;
    }

    /**
     * Get the number of ARCH terms.
     *
     * @return the number of ARCH terms
     */
    public int q() {
        return a != null ? a.length : 0;
    }

    /**
     * Get the maximum of ARCH length or GARCH length.
     *
     * @return max(ARCH terms, GARCH terms)
     */
    public int maxPQ() {
        return Math.max(p(), q());
    }

    /**
     * Compute the unconditional variance of the GARCH model.
     *
     * @return the unconditional variance
     */
    public double var() {
        double unconditionalVariance = 1;
        for (int i = 0; i < q(); ++i) {//a0 is the constant term
            unconditionalVariance -= a[i];
        }
        for (int i = 0; i < p(); ++i) {
            unconditionalVariance -= b[i];
        }
        unconditionalVariance = a0 / unconditionalVariance;

        assertArgument(unconditionalVariance > 0,
                "invalid GARCH specification (except α<sub>0</sub>), Σ{α} + Σ{β} < 1; invalid variance");

        return unconditionalVariance;
    }

    /**
     * Compute the conditional variance based on the past information.
     *
     * @param e2 the last q squared observations
     * @param sigma2_lag the last p conditional variance
     * @return the conditional variance, <i>h(t | F<sub>t</sub>)</i>
     */
    public double sigma2(double[] e2, double[] sigma2_lag) {
        double sigma2 = a0 + dotProduct(a, e2) + dotProduct(b, sigma2_lag);
        return sigma2;
    }

    /**
     * Print out
     *
     * <blockquote><code><pre>
     * h(t)= α0 + Σ(α_i * e_(t-i)^2) + Σ(β_i * h_(t-i))
     * </pre></code></blockquote>
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(a0);

        for (int i = 0; i < a.length; ++i) {
            str.append(String.format("+ %f * e_{t-%d}", a[i], i + 1));
        }

        for (int i = 0; i < b.length; ++i) {
            str.append(String.format("+ %f * h_{t-%d}", b[i], i + 1));
        }

        return str.toString();
    }
}
