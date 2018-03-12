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
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import com.numericalmethod.suanshu.number.complex.Complex;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a solver for finding the roots of a quadratic equation, \(ax^2 + bx + c = 0\).
 * The roots are:
 * \[
 * \frac{-b}{2a} \pm \frac{\sqrt{b^2-4ac}}{2a}
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Quadratic_equation">Wikipedia: QuadraticRoot equation</a>
 */
public class QuadraticRoot implements PolyRootSolver {

    /**
     * Solve \(ax^2 + bx + c = 0\).
     *
     * @param polynomial a quadratic equation to be solved
     * @return the roots of the quadratic equation
     * @throws IllegalArgumentException if the polynomial degree is not 2
     */
    @Override
    public List<Number> solve(Polynomial polynomial) {
        SuanShuUtils.assertArgument(polynomial.degree() == 2, "polynomial is not of degree 2");

        List<Number> roots = new ArrayList<Number>();
        double[] coefficients = polynomial.getCoefficients();
        final double a = coefficients[0];
        final double b = coefficients[1];
        final double c = coefficients[2];

        if (isZero(c, 0)) {
            // c == 0, fast return one root as zero, another as -b/a
            roots.add(new Double(0.0));
            roots.add(new Double(-b / a));
            return roots;
        }

        /*
         * Discriminant D = b^2 - 4ac
         *
         * To avoid overflow / underflow, the following intermediate value is
         * computed instead of direct calculation of discriminant:
         * If |b/2| < c,
         * e = (b/4c) - a, and d = SQRT(|e|) * SQRT(|c|)
         * else,
         * e = 1 - (4ac/b^2), and d = SQRT(|e|) * SQRT(|b / 2|)
         *
         * Note: d is equivalent to SQRT(D) / 2
         */
        final double b_OVER_2 = b / 2.0;
        double d = 0.0;
        double e = 0.0;
        if (abs(b_OVER_2) < abs(c)) {
            e = signum(c) * a;
            e = -e + b_OVER_2 * (b_OVER_2 / abs(c));
            d = sqrt(abs(e)) * sqrt(abs(c));
        } else {
            e = 1.0 - ((a / b_OVER_2) * (c / b_OVER_2));
            d = sqrt(abs(e)) * (abs(b_OVER_2));
        }

        /*
         * Then,
         * if e > 0,
         * x1 = (- b/2 + d)/a, and x2 = c/(x1 * a)
         * else,
         * the complex conjugate roots are
         * x1 = (- b/2 + d)/a, x2 = - b/2a - d/a
         */
        if (compare(e, 0, 0) >= 0) {
            // real roots
            d = -signum(b_OVER_2) * d;
            double x1 = (-b_OVER_2 + d) / a;
            roots.add(x1);
            roots.add(!isZero(x1, 0) ? (c / x1) / a : 0.0);
        } else {
            // Complex conjugate zeros
            double rootRe = -(b_OVER_2 / a);
            double rootIm = d / a;
            roots.add(new Complex(rootRe, rootIm));
            roots.add(new Complex(rootRe, -rootIm));
        }

        return roots;
    }
}
