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
package com.numericalmethod.suanshu.analysis.function.polynomial.root.jenkinstraub;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.differentiation.univariate.DPolynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.HornerScheme;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.abs;

/**
 * Compute the initial K polynomial (as the derivative of P) and
 * run a specified number of steps with no shift.
 *
 * @author Ken Yiu
 */
class JTNoShift {

    final Polynomial kPoly;
    private static double epsilon = 0;

    JTNoShift(Polynomial pPoly, int nIterations) {
        kPoly = run(pPoly, nIterations);
    }

    private static Polynomial run(Polynomial pPoly, int nIterations) {
        // 2.2.1) construct initial K-polynomial as P's 1st derivative, i.e., K(z) = P'(z)
        // Note: K is downscaled by P's degree to restore P's leading coefficient
        Polynomial kPoly = new DPolynomial(pPoly); // K(z) as the 1st derivative of P(z)
        kPoly = kPoly.scaled(1. / pPoly.degree()); // scaled down K(z) to preserve the leading coefficient of P(z)
        double[] kCoeff = kPoly.getCoefficients();
        kCoeff[0] = pPoly.getCoefficient(0); // avoid rounding error for the leading coefficient
        kPoly = new Polynomial(kCoeff);

        double p_n = pPoly.getCoefficient(pPoly.degree()); // constant term of P, p_n
        double p_n1 = pPoly.getCoefficient(pPoly.degree() - 1); // coefficient of x in P, p_{n-1}, for checking if k_n is large enough
        boolean zerok = isZero(kPoly.getCoefficient(kPoly.degree()), epsilon); // {@code true} if k_n is zero or too small compared to p_{n-1}

        // 2.2.2) compute K-polynomial for some iterations with no shift
        for (int j = 0; j < nIterations; ++j) {
            if (zerok) {
                /*
                 * 2.2.2.1) if k_n == 0, use the unscaled K
                 * use the unscaled form of recurrence
                 * K(z) = K(z) / z
                 */
                HornerScheme horner = new HornerScheme(kPoly, 0);
                kPoly = horner.quotient();

                zerok = isZero(kPoly.getCoefficient(kPoly.degree()), epsilon);
            } else {
                /*
                 * 2.2.2.2) else, scale K by -p_n/k_n to restore the leading coefficient as P
                 * use the scaled form of recurrence if value of K at 0 is nonzero
                 *
                 * K(z) = 1/z * [P(z) - p_n/k_n * K(z)]
                 *
                 * Note: -p_n/k_n is the scaling factor of K for cancelling
                 * the constant terms such that the sum is divisible by z
                 */
                double scaleK = -p_n / kPoly.getCoefficient(kPoly.degree()); // -p_n/k_n
                kPoly = pPoly.add(kPoly.scaled(scaleK));
                HornerScheme horner = new HornerScheme(kPoly, 0);
                kPoly = horner.quotient();

                zerok = (abs(kPoly.getCoefficient(0)) <= abs(p_n1) * Constant.MACH_EPS * 10.0);
            }
        }

        return kPoly;
    }
}
