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

/**
 * Lanczos approximation, computations are done in {@code double}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Lanczos_approximation">Wikipedia: Lanczos approximation</a>
 */
public class GammaLanczosQuick extends UnivariateRealFunction implements Gamma {

    private final Lanczos lanczos;

    /**
     * Construct an instance of a Gamma function, computed using the Lanczos approximation.
     */
    public GammaLanczosQuick() {
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
    public GammaLanczosQuick(double g, int n, int scale) {
        this.lanczos = new Lanczos(g, n, scale);
    }

    @Override
    public double evaluate(double x) {
        if (isZero(x, 0)) {
            return Double.POSITIVE_INFINITY;
        }

        double z = x > 0 ? x : -x;
        double fx = lanczos.logGammaQuick(z);
        fx = exp(fx);
        fx = x > 0 ? fx : reflectionFormula(-x, fx);
        return fx;
    }

    /**
     * Compute Γ(-x) from Γ(x), x > 0 using Euler's reflection formula.
     *
     * @param x      <i>x</i>
     * @param gammaX <i>Γ(x)</i>
     * @see <a href="http://en.wikipedia.org/wiki/Reflection_formula">Wikipedia: Reflection formula</a>
     */
    static double reflectionFormula(double x, double gammaX) {
        double result = Math.PI / x;
        result /= gammaX;
        result /= Math.sin(-x * Math.PI);
        return result;
    }
}
