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
package com.numericalmethod.suanshu.analysis.function.special.gaussian;

import static com.numericalmethod.suanshu.Constant.ROOT_2_PI;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static java.lang.Math.exp;

/**
 * The Gaussian function is defined as:
 * \[
 * f(x) = a e^{- { \frac{(x-b)^2 }{ 2 c^2} } }
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Gaussian_function">Wikipedia: Gaussian function</a>
 */
public class Gaussian extends UnivariateRealFunction {

    private final double a;
    private final double b;
    private final double c;

    /**
     * Construct an instance of the Gaussian function.
     *
     * @param a <i>a</i>
     * @param b <i>b</i>
     * @param c <i>c</i>
     */
    public Gaussian(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Construct an instance of the standard Gaussian function: \(f(x) = e^{-{\frac{(x)^2}{2}}}\)
     */
    public Gaussian() {
        this(1d / ROOT_2_PI, 0, 1d);
    }

    /**
     * Get <i>a</i>.
     *
     * @return <i>a</i>
     */
    public double a() {
        return a;
    }

    /**
     * Get <i>b</i>.
     *
     * @return <i>b</i>
     */
    public double b() {
        return b;
    }

    /**
     * Get <i>c</i>.
     *
     * @return <i>c</i>
     */
    public double c() {
        return c;
    }

    @Override
    public double evaluate(double x) {
        double y = (x - b) / c;
        y *= y;
        y /= -2;
        y = a * exp(y);
        return y;
    }
}
