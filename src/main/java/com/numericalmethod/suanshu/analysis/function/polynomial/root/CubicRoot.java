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
import com.numericalmethod.suanshu.number.complex.Complex;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a cubic equation solver. This implementation solves \(ax^3 + bx^2 + cx + d = 0\) using Cardano's method.
 *
 * @author Ken Yiu
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Cubic_function">Wikipedia: CubicRoot equation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Cubic_function#Cardano.27s_method">Wikipedia: Cardano's method</a>
 * <li><a href="http://en.wikipedia.org/wiki/Cubic_function#Root-finding_formula">Wikipedia: Root-finding formula for cubic equation</a>
 * </ul>
 */
public class CubicRoot implements PolyRootSolver {

    private static final double ROOT_3_OVER_2 = sqrt(3) / 2;

    /**
     * Solve \(ax^3 + bx^2 + cx + d = 0\).
     *
     * @param polynomial a cubic equation to be solved
     * @return the roots of the cubic equation
     * @throws IllegalArgumentException if the polynomial degree is not 3
     */
    @Override
    public List<Number> solve(Polynomial polynomial) {
        SuanShuUtils.assertArgument(polynomial.degree() == 3, "polynomial is not of degree 3");

        List<Number> roots = new ArrayList<Number>();
        double[] coefficients = polynomial.getCoefficients();
        double a = coefficients[0];
        double b = coefficients[1];
        double c = coefficients[2];
        double d = coefficients[3];

        // pre-compute often used values
        double aa = a * a;
        double bb = b * b;

        /*
         * Find the discriminant D = q^3 + r^2
         * where
         * q = (3ac - b^2)/ (9a^2)
         * and,
         * r = (9abc - 27a^2d - 2b^3) / (54a^3)
         */
        double q = (3 * a * c - bb) / (9 * aa);
        double r = (9 * a * b * c - 27 * aa * d - 2 * bb * b) / (54 * aa * a);
        double bOver3a = b / (3 * a);
        double qqq = q * q * q;

        double D = qqq + r * r;

        /*
         * The roots are:
         * x1 = s + t - b/3a
         * x2 = - 1/2 (s + t) - b/3a + sqrt(3)/2 (s - t) i
         * x3 = - 1/2 (s + t) - b/3a - sqrt(3)/2 (s - t) i
         * where s and t are determined based on the discriminant D.
         */
        if (D >= 0) {
            /*
             * There are 1 real root and a pair of complex conjugates.
             * Define
             * s = cbrt(r + sqrt(q^3 + r^3))
             * t = cbrt(r - sqrt(q^3 + r^3))
             */
            double s = cbrt(r + sqrt(D));
            double t = cbrt(r - sqrt(D));

            double spt = s + t;
            double smt = s - t;
            double rootRe = -spt / 2 - bOver3a;
            double rootIm = smt * ROOT_3_OVER_2;

            roots.add(new Double(spt - bOver3a));
            roots.add(new Complex(rootRe, rootIm));
            roots.add(new Complex(rootRe, -rootIm));
        } else {
            /*
             * There are 3 real roots.
             * Define
             * rho = sqrt(-q^3)
             * theta = arccos(r / rho)
             * s = complex of polar form (cbrt(rho), theta / 3)
             * t = complex of polar form (cbrt(rho), -theta / 3)
             */
            double rho = sqrt(-qqq);
            double theta = acos(r / rho);
            double mod = cbrt(rho);
            double arg = theta / 3;
            Complex s = Complex.fromPolar(mod, arg);
            Complex t = Complex.fromPolar(mod, -arg);

            Complex spt = s.add(t);
            Complex smt = s.minus(t);
            Complex r1 = spt.divide(new Complex(-2, 0)).minus(new Complex(bOver3a, 0));
            Complex r2 = smt.multiply(new Complex(0, ROOT_3_OVER_2));

            roots.add(spt.minus(new Complex(bOver3a, 0)).real());
            roots.add(r1.add(r2).real());
            roots.add(r1.minus(r2).real());
        }

        return roots;
    }
}
