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
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.number.complex.ElementaryFunction;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a quartic equation solver that solves \(ax^4 + bx^3 + cx^2 + dx + e = 0\) using a root-finding formula.
 *
 * @author Ken Yiu
 * @see <a href="http://planetmath.org/encyclopedia/QuarticFormula.html">PlanetMath: QuarticRoot Formula</a>
 */
public class QuarticRootFormula implements QuarticSolver {

    private static final double CUBIC_ROOT_2 = Math.cbrt(2.);
    private static final double ONE_THIRD = 1. / 3.;

    @Override
    public List<Number> solve(double a, double b, double c, double d, double e) {
        List<Number> roots = new ArrayList<Number>();

        double aa = a * a;
        double aaa = aa * a;
        double bb = b * b;
        double bbb = bb * b;
        double cc = c * c;
        double ccc = cc * c;
        double bd = b * d;
        double ae = a * e;
        double k1 = bb / aa / 2.;
        double k2 = 2. * ONE_THIRD * c / a;

        double s = cc - 3. * bd + 12. * ae;
        double t = 2. * ccc - 9. * bd * c + 27. * a * d * d + 27. * bb * e - 72. * ae * c;
        double u = (-bbb + 4. * a * b * c - 8. * aa * d) / aaa / 4.;

        Complex v = ElementaryFunction.pow(
                ElementaryFunction.sqrt(new Complex(-4. * s * s * s + t * t, 0.)).
                add(new Complex(t, 0.)),
                new Complex(ONE_THIRD, 0.));

        Complex w1 = new Complex(CUBIC_ROOT_2 * ONE_THIRD * s / a, 0.).divide(v);
        double a3cbrt2 = a * 3. * CUBIC_ROOT_2;
        Complex w2 = new Complex(v.real() / a3cbrt2, v.imaginary() / a3cbrt2);
        Complex w = w1.add(w2);

        Complex x = ElementaryFunction.sqrt(new Complex(k1 / 2. - k2, 0.).add(w));

        Complex y1 = new Complex(k1 - k2 * 2., 0.).minus(w);
        Complex y2 = new Complex(u, 0.).divide(x);
        Complex sqrty1py2 = ElementaryFunction.sqrt(y1.add(y2));
        Complex sqrty1my2 = ElementaryFunction.sqrt(y1.minus(y2));

        Complex z1 = new Complex(-b / 4. / a, 0.);
        Complex z2 = new Complex(x.real() / 2., x.imaginary() / 2.);
        Complex z31 = new Complex(sqrty1py2.real() / 2., sqrty1py2.imaginary() / 2.);
        Complex z32 = new Complex(sqrty1my2.real() / 2., sqrty1my2.imaginary() / 2.);

        roots.add(z1.add(z2).minus(z31));
        roots.add(z1.add(z2).add(z31));
        roots.add(z1.minus(z2).minus(z32));
        roots.add(z1.minus(z2).add(z32));

        return roots;
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
