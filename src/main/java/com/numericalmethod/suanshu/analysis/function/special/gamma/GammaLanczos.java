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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.exp;
import java.math.BigDecimal;

/**
 * Lanczos approximation provides a way to compute the Gamma function such that the accuracy can be made arbitrarily precise.
 * Yet, the computations can be time-consuming if done in, e.g., {@link BigDecimal}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Lanczos_approximation">Wikipedia: Lanczos approximation</a>
 */
public class GammaLanczos extends UnivariateRealFunction implements Gamma {

    private final Lanczos lanczos;

    /**
     * Construct an instance of a Gamma function, computed using the Lanczos approximation.
     */
    public GammaLanczos() {
        this.lanczos = new Lanczos();
    }

    /**
     * Construct an instance of a Gamma function, computed using the Lanczos approximation.
     * <p/>
     * Recommended settings are
     * <blockquote><code>
     * g = 607.0 / 128.0;
     * n = 15;
     * scale = 30;
     * </code></blockquote>
     *
     * @param g     <i>g</i>
     * @param n     <i>n</i>
     * @param scale precision
     */
    public GammaLanczos(double g, int n, int scale) {
        this.lanczos = new Lanczos(g, n, scale);
    }

    @Override
    public double evaluate(double x) {
        if (isZero(x, 0)) {
            return Double.POSITIVE_INFINITY;
        }

        double z = x > 0 ? x : -x;
        double fx = lanczos.logGamma(z);
        fx = exp(fx);
        fx = x > 0 ? fx : GammaLanczosQuick.reflectionFormula(-x, fx);
        return fx;
    }
}
