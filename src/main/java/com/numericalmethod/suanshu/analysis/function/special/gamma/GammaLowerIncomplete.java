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
 * The Lower Incomplete Gamma function is defined as:
 * \[
 * \gamma(s,x) = \int_0^x t^{s-1}\,e^{-t}\,{\rm d}t = P(s,x)\Gamma(s)
 * \]
 * <i>P(s,x)</i> is the Regularized Incomplete Gamma P function.
 * <p/>
 * The integrand has the same form as the Gamma function, but the upper limit of the integration is a variable.
 * The lower limit is fixed.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Incomplete_gamma_function">Wikipedia: Incomplete gamma function</a>
 */
public class GammaLowerIncomplete extends BivariateRealFunction {

    private static final GammaRegularizedP pgamma = new GammaRegularizedP();
    private static final Gamma gamma = new GammaLanczosQuick();

    /**
     * Evaluate \(\gamma(s,x)\).
     *
     * @param s <i>s &gt; 0</i>
     * @param x <i>x &ge; 0</i>
     * @return \(\gamma(s,x)\)
     */
    @Override
    public double evaluate(double s, double x) {
        SuanShuUtils.assertArgument(s > 0, "s must be > 0");

        double p = pgamma.evaluate(s, x);
        double g = gamma.evaluate(s);
        return p * g;
    }
}
