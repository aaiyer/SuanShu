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

/**
 * This transformation is for mapping integral region from <i>[a, b]</i> to <i>[-1, 1]</i>.
 * The substitution rule is
 * \[
 * \int_{a}^{b}f(x)dx = \int_{-1}^{1}\frac{b-a}{2}f(\frac{(b-a)t+(a+b)}{2})dt
 * \]
 *
 * @author Haksun Li
 */
public class StandardInterval implements SubstitutionRule {

    private final double a;//the lower limit
    private final double b;//the upper limit

    /**
     * Construct a {@code StandardInterval} substitution rule.
     *
     * @param a the lower limit
     * @param b the upper limit
     */
    public StandardInterval(final double a, final double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public UnivariateRealFunction x() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return ((b - a) * t + (a + b)) / 2;
            }
        };
    }

    @Override
    public UnivariateRealFunction dx() {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return (b - a) / 2;
            }
        };
    }

    @Override
    public double ta() {
        return -1;
    }

    @Override
    public double tb() {
        return 1;
    }
}
