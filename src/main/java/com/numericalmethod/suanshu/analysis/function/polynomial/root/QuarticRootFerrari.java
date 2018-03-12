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
package com.numericalmethod.suanshu.analysis.function.polynomial.root;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.QuarticRoot.QuarticSolver;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.number.complex.ElementaryFunction;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a quartic equation solver that solves \(ax^4 + bx^3 + cx^2 + dx + e = 0\) using the Ferrari method.
 *
 * @author Ken Yiu
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Quartic_function#Ferrari.27s_solution">Wikipedia: Ferrari's solution</a>
 * <li><a href="http://mathworld.wolfram.com/QuarticEquation.html">QuarticRoot Equation from Wolfram MathWorld</a>
 * </ul>
 */
public class QuarticRootFerrari implements QuarticSolver {

    private static final double ONETHIRD = 1. / 3.;

    /**
     * Solve \(ax^4 + bx^3 + cx^2 + dx + e = 0\).
     *
     * @param a       <i>a</i>
     * @param b       <i>b</i>
     * @param c       <i>c</i>
     * @param d       <i>d</i>
     * @param e       <i>e</i>
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return the list of roots
     * @see <a href="http://en.wikipedia.org/wiki/Quartic_function#Ferrari.27s_solution">Wikipedia: Ferrari's solution</a>
     */
    public List<Number> solve(double a, double b, double c, double d, double e, double epsilon) {
        List<Number> roots = new ArrayList<Number>();

        // pre-compute often used values
        double aa = a * a;
        double aaa = aa * a;
        double aaaa = aa * aa;
        double bb = b * b;
        double bbb = bb * b;
        double bbbb = bb * bb;
        double bc = b * c;
        double bbc = bb * c;

        /*
         * First, compute the followings:
         * alpha = - 3b^2/8a^2 + c/a
         * beta = b^3/8a^3 - bc/2a^2 + d/a
         * gamma = - 3b^4/256a^4 + cb^2/16a^3 - bd/16a^3 + e/a
         */
        double alpha = -3. * bb / 8. / aa + c / a;
        double beta = bbb / 8. / aaa - bc / 2. / aa + d / a;
        double gamma = -3. * bbbb / 256. / aaaa + bbc / 16. / aaa - b * d / 4. / aa + e / a;


        if (equal(beta, 0, epsilon)) {
            /*
             * If beta = 0, the roots are
             * x = - b/4a ± sqrt(-alpha ± sqrt(alpha^2 - 4*gamma)/2)
             */
            double delta = alpha * alpha - 4. * gamma;

            Complex z1 = new Complex(-b / a / 4., 0.);
            Complex z2 = new Complex(-alpha / 2., 0.);
            Complex z3 = ElementaryFunction.sqrt(new Complex(delta / 4., 0.));

            roots.add(z1.add(ElementaryFunction.sqrt(z2.add(z3))));
            roots.add(z1.add(ElementaryFunction.sqrt(z2.minus(z3))));
            roots.add(z1.minus(ElementaryFunction.sqrt(z2.add(z3))));
            roots.add(z1.minus(ElementaryFunction.sqrt(z2.minus(z3))));
        } else {
            /*
             * Otherwise, compute the followings:
             * p = - alpha^2/12 - gamma
             * q = - alpha^3/108 + alpha*gamma/3 - beta^2/8
             * r = - q/2 ± sqrt(q^2/4 + p^3/27)
             * u = cbrt(r)
             */
            double alpha2 = alpha * alpha;
            double alpha3 = alpha2 * alpha;

            double p = -alpha2 / 12. - gamma;
            double q = -alpha3 / 108. + alpha * gamma / 3. - beta * beta / 8.;
            double ppp = p * p * p;
            double qq = q * q;
            Complex r = new Complex(-q / 2., 0.).add(ElementaryFunction.sqrt(new Complex(qq / 4. + ppp / 27., 0.)));
            Complex u = ElementaryFunction.pow(r, new Complex(ONETHIRD, 0.));

            /*
             * If u =/= 0,
             * y = - 5/6 alpha + u - p/3u
             * If u == 0,
             * y = - 5/6 alpha + u - cbrt(q)
             * w = sqrt(alpha + 2y)
             *
             * Finally, the roots are
             * x = - b/4a + ± w ± sqrt(-(3*alpha + 2y ± 2*beta/w))/2
             *
             * Note: the choices of 1st and 3rd ± are the same, so there are in total 4 combinations
             */
            Complex y = new Complex(-5. * alpha / 6., 0.).add(u).
                    minus(u.equals(u.ZERO())
                          ? ElementaryFunction.pow(new Complex(q, 0.), new Complex(ONETHIRD, 0.))
                          : new Complex(p / 3., 0.).divide(u));
            Complex w = ElementaryFunction.sqrt(new Complex(alpha, 0.).add(new Complex(y.real() * 2., y.imaginary() * 2.)));

            Complex z1 = new Complex(-b / 4. / a, 0.);
            Complex z2 = new Complex(w.real() / 2., w.imaginary() / 2.);
            Complex z3 = new Complex(3. * alpha / 4. + y.real() / 2., y.imaginary() / 2.);
            Complex z4 = new Complex(beta / 2., 0.).divide(w);

            Complex z5 = ElementaryFunction.sqrt(z3.add(z4).opposite());
            Complex z6 = ElementaryFunction.sqrt(z3.minus(z4).opposite());

            roots.add(z1.add(z2).add(z5));
            roots.add(z1.add(z2).minus(z5));
            roots.add(z1.minus(z2).add(z6));
            roots.add(z1.minus(z2).minus(z6));
        }

        return roots;
    }

    @Override
    public List<Number> solve(double a, double b, double c, double d, double e) {
        double epsilon = SuanShuUtils.autoEpsilon(a, b, c, d, e);
        return solve(a, b, c, d, e, epsilon);
    }

    /**
     * Solve \(ax^4 + bx^3 + cx^2 + dx + e = 0\).
     *
     * @param polynomial a quartic equation to be solved
     * @return the roots of the quartic equation
     * @throws IllegalArgumentException if the polynomial degree is not 4
     */
    public List<Number> solve(Polynomial polynomial) {
        return solve(polynomial.getCoefficient(5),
                     polynomial.getCoefficient(4),
                     polynomial.getCoefficient(3),
                     polynomial.getCoefficient(2),
                     polynomial.getCoefficient(1));
    }
}
