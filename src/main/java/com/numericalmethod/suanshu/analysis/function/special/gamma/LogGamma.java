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

/**
 * The log-Gamma function, \(\log (\Gamma(z))\), for positive real numbers, is the log of the Gamma function.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Gamma_function">Wikipedia: Gamma function</a>
 * <li><a href="http://en.wikipedia.org/wiki/Lanczos_approximation">Wikipedia: Lanczos approximation</a>
 * </ul>
 */
public class LogGamma extends UnivariateRealFunction {

    /** the methods available to compute \(\log (\Gamma(z))\) */
    public static enum Method {

        /**
         * Lanczos approximation.
         * <p/>
         * This accuracy can be made arbitrary precise.
         * Yet, the computations can be time-consuming.
         */
        LANCZOS,
        /**
         * Quick Lanczos approximation,
         * where all computations are done in {@code double} precision.
         */
        LANCZOS_QUICK
    }

    private final Method method;
    private final Lanczos lanczos;

    /**
     * Construct an instance of log-Gamma.
     *
     * @param method  the Lanczos computation to use
     * @param lanczos the Lanczos approximation to use
     */
    public LogGamma(Method method, Lanczos lanczos) {
        this.method = method;
        this.lanczos = lanczos;
    }

    /**
     * Construct an instance of log-Gamma.
     */
    public LogGamma() {
        this(Method.LANCZOS_QUICK, new Lanczos());
    }

    /**
     * Evaluate the log of the Gamma function in the positive real domain.
     *
     * @param x a real number &gt; 0
     * @return \(\log (\Gamma(z))\)
     */
    @Override
    public double evaluate(double x) {
        double z = x > 0 ? x : -x;
        double result = method == Method.LANCZOS_QUICK ? lanczos.logGammaQuick(z) : lanczos.logGamma(z);
        return result;
    }
}
