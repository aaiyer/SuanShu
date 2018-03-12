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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static java.lang.Math.pow;

/**
 * This transformation is good for an integral which diverges at one of the end points.
 * <p/>
 * For singularity at the lower limit, we have \((x-a)^{-\gamma}\) diverging near \(x = a\), \(0 \leq \gamma < 1\).
 * The substitution rule is
 * \[
 * \int_{a}^{b}f(x)dx = \int_{0}^{(b-a)^{1-\gamma}}\frac{t^{\frac{\gamma}{1-\gamma}}f(t^\frac{1}{1-\gamma}+a)}{1-\gamma}dt, b > a
 * \]
 * <p/>
 * For singularity at the upper limit, we have \((x-b)^{-\gamma}\) diverging near \(x = b\), \(0 \leq \gamma < 1\).
 * \[
 * \int_{a}^{b}f(x)dx = \int_{0}^{(b-a)^{1-\gamma}}\frac{t^{\frac{\gamma}{1-\gamma}}f(b-t^\frac{1}{1-\gamma})}{1-\gamma}dt, b > a
 * \]
 * A common case is when \(\gamma = 0.5\).
 *
 * @author Haksun Li
 */
public class PowerLawSingularity implements SubstitutionRule {

    /** the type of end point divergence */
    public static enum PowerLawSingularityType {

        /** diverge near <i>x = b</i> */
        UPPER,
        /** diverge near <i>x = a</i> */
        LOWER
    }

    private final PowerLawSingularityType type;
    private final double gamma;
    private final double a;//the lower limit
    private final double b;//the upper limit

    /**
     * Construct a {@code PowerLawSingularity} substitution rule.
     *
     * @param type  the type of end point divergence
     * @param gamma the exponential
     * @param a     the lower limit
     * @param b     the upper limit
     */
    public PowerLawSingularity(final PowerLawSingularityType type, final double gamma, final double a, final double b) {
        this.type = type;
        this.gamma = gamma;
        this.a = a;
        this.b = b;
    }

    @Override
    public UnivariateRealFunction x() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                switch (type) {
                    case LOWER:
                        return pow(t, 1 / (1 - gamma)) + a;
                    case UPPER:
                        return b - pow(t, 1 / (1 - gamma));
                    default:
                        throw new IllegalArgumentException("unrecognized singularity type");//unreachable
                }
            }
        };
    }

    @Override
    public UnivariateRealFunction dx() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return pow(t, gamma / (1 - gamma)) / (1 - gamma);
            }
        };
    }

    @Override
    public double ta() {
        return 0;
    }

    @Override
    public double tb() {
        return pow(b - a, 1 - gamma);
    }
}
