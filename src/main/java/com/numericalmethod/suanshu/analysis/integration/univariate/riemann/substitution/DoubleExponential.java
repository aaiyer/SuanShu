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
import static java.lang.Math.*;

/**
 * This transformation speeds up the convergence of the Trapezoidal Rule exponentially.
 * It applies to a finite integral region <i>[a, b]</i>.
 * The tricky part of using this transformation is to figure out a good range for <i>t</i>.
 * If there is information about the integrand available, {@link SubstitutionRule#ta()} and {@link SubstitutionRule#tb()} should be overridden.
 * The substitution is
 * <pre><i>
 * x = 0.5 * (b + a) + 0.5 * (b - a) * tanh(c * sinh(t))
 * </i></pre>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Tanh-sinh_quadrature">Wikipedia: Tanh-sinh quadrature</a>
 */
//TODO: the convergence criterion costs a double in the number of function evaluations, c.f., chapter 4.5.4 in NR
public class DoubleExponential implements SubstitutionRule {

    private final UnivariateRealFunction x;//x(t)
    private final UnivariateRealFunction dx;//x'(t) = dx(t)/dt
    private final UnivariateRealFunction f;//the original integrand
    private double dt = 0.001;//step size

    /**
     * Construct a {@code DoubleExponential} substitution rule
     * by specifying the exact change of variable from <i>t</i> to <i>x</i>.
     *
     * @param x  the substitution <i>x(t)</i>
     * @param dx <i>x'(t)</i>
     * @param f  the integrand
     * @param a  the lower limit
     * @param b  the upper limit
     * @param c  a constant; usually either 0 or 0.5 * PI
     */
    DoubleExponential(final UnivariateRealFunction x, final UnivariateRealFunction dx,
                      final UnivariateRealFunction f, final double a, final double b,
                      final double c) {
        this.x = x;
        this.dx = dx;
        this.f = f;
    }

    /**
     * Construct a {@code DoubleExponential} substitution rule by trying to automatically determine the substitution rule.
     *
     * @param f the integrand
     * @param a the lower limit
     * @param b the upper limit
     * @param c a constant; usually either 0 or 0.5 * PI
     */
    public DoubleExponential(final UnivariateRealFunction f, final double a, final double b, final double c) {
        this(
                xt(a, b, c),//x(t)
                dxdt(a, b, c),//dx(t)/dt = x'(t)
                f, a, b,
                c);
    }

    @Override
    public UnivariateRealFunction x() {
        return x;
    }

    @Override
    public UnivariateRealFunction dx() {
        return dx;
    }

    /**
     * {@inheritDoc}
     *
     * The accuracy of using the double exponential transformation lies in choosing the correct region of <i>t</i> for integration.
     * It is very hard to determine the region without knowing about the function, such as where the singularities are.
     * If the region of <i>t</i> is known, override this function.
     *
     * @return the lower limit
     */
    @Override
    public double ta() {
        double t = -4.3;//4.3, 3.7;
        if (f != null) {
            double gt;
            do {
                t += dt;
                gt = g(t);
            } while (Double.isInfinite(gt) || Double.isNaN(gt));
        }

        return t;
    }

    /**
     * {@inheritDoc}
     *
     * The accuracy of using the double exponential transformation lies in choosing the correct region of <i>t</i> for integration.
     * It is very hard to determine the region without knowing about the function, such as where the singularities are.
     * If the region of <i>t</i> is known, override this function.
     *
     * @return the upper limit
     */
    @Override
    public double tb() {
        double t = 4.3;//4.3, 3.7;
        if (f != null) {
            double gt;
            do {
                t -= dt;
                gt = g(t);
            } while (Double.isInfinite(gt) || Double.isNaN(gt));
        }

        return t;
    }

    /** g(t) = f(x(t)) * x'(t) */
    private double g(double t) {
        double xt = x.evaluate(t);//x(t)
        double dxdt = dx.evaluate(t);//x'(t) = dx(t)
        double fxt = f.evaluate(xt);
        double gt = fxt * dxdt;//f(x(t)) * x'(t)

        return gt;
    }

    /** x(t) */
    private static UnivariateRealFunction xt(final double a, final double b, final double c) {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                return 0.5 * (b + a) + 0.5 * (b - a) * tanh(c * sinh(t));
            }
        };
    }

    /** x'(t) = dx(t)/dt */
    private static UnivariateRealFunction dxdt(final double a, final double b, final double c) {
        return new UnivariateRealFunction() {

            @Override
            public double evaluate(double t) {
                double q = exp(-2. * sinh(t));
                return 2. * (b - a) * q / (1 + q) / (1 + q) * cosh(t);
            }
        };
    }
}
