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

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * The Upper Incomplete Gamma function is defined as:
 * \[
 * \Gamma(s,x) = \int_x^{\infty} t^{s-1}\,e^{-t}\,{\rm d}t = Q(s,x) \times \Gamma(s)
 * \]
 * The integrand has the same form as the Gamma function, but the lower limit of the integration is a variable.
 * The upper limit is fixed.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Incomplete_gamma_function">Wikipedia: Incomplete gamma function</a>
 */
public class GammaUpperIncomplete extends BivariateRealFunction {

    private static final GammaRegularizedQ qgamma = new GammaRegularizedQ();
    private static final Gamma gamma = new GammaLanczosQuick();

    /**
     * Compute <i>Γ(s,x)</i>.
     *
     * @param s <i>s &gt; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return <i>Γ(s,x)</i>
     */
    @Override
    public double evaluate(final double s, final double x) {
        SuanShuUtils.assertArgument(s > 0, "s must be > 0");

        double q = qgamma.evaluate(s, x);
        double g = gamma.evaluate(s);
        return q * g;
    }
}
