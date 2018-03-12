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
package com.numericalmethod.suanshu.analysis.function.polynomial;

import com.numericalmethod.suanshu.analysis.uniroot.Newton;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The Cauchy's polynomial of a polynomial takes this form:
 * <pre><i>
 * C(x) = |p_n|x^n + |p_{n-1}|x^{n-1} + ... + |p_1|x - |p_0| = 0
 * </i></pre>
 *
 * Note: the sign of the constant is negative.
 *
 * @author Haksun Li
 */
public class CauchyPolynomial extends Polynomial {

    public CauchyPolynomial(Polynomial p) {
        super(getCauchyCoefficients(p));
    }

    private static double[] getCauchyCoefficients(Polynomial p) {
        int degree = p.degree();
        double[] pCoeff = p.getCoefficients();
        double[] cauchyCoeff = DoubleArrayMath.abs(pCoeff); // absolute coefficients
        cauchyCoeff[degree] = -cauchyCoeff[degree]; // negative constant
        return cauchyCoeff;
    }

    /**
     * Cauchy's lower bound on polynomial zeros is the unique positive root of the Cauchy polynomial.
     *
     * @return Cauchy's lower bound
     */
    public double lowerBound() {
        final double[] cauchyCoeff = getCoefficients();
        final int degree = degree();

        // Compute the upper estimate of the lower bound (= absolute geometric mean of zeros of P(x))
        // ref: http://eprints.maths.ox.ac.uk/16/1/mekwi.pdf
        double startPoint = exp((log(-cauchyCoeff[degree]) - log(cauchyCoeff[0])) / (double) degree);
        double x = 0.0;
        if (cauchyCoeff[degree - 1] != 0) {
            // Note: we know that the lowerBound (lower bound) lies between 0 and upper bound
            // If Newton step at the origin is better, use it as the starting point
            x = -cauchyCoeff[degree] / cauchyCoeff[degree - 1]; // -P(0)/P'(0)
            if (x < startPoint) { // closer to the lowerBound than the upper bound
                startPoint = x;
            }
        }

        // before using Newton's method, get closer to the lowerBound by exponentially scaling down the point
        HornerScheme horner = null; // for evaluating the polynomial at x
        x = startPoint;
        do {
            startPoint = x;
            x = 0.1 * startPoint; // try a smaller x (10 multiply smaller)
            horner = new HornerScheme(this, x); // evaluate Cauchy's polynomial at x by Horner scheme
        } while (horner.remainder() > 0);

        try {// do the Newton iteration until x converges
            Newton newton = new Newton(SuanShuUtils.autoEpsilon(cauchyCoeff), 100);
            return newton.solve(this, startPoint); // return the lower bound of zeros
        } catch (Exception ex) {
            return startPoint;//re-throw the error instead?
        }
    }
}
