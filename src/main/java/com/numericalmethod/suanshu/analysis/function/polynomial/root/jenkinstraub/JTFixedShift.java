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

import com.numericalmethod.suanshu.analysis.function.polynomial.CauchyPolynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.number.complex.Complex;
import static java.lang.Math.toRadians;
import java.util.List;

/**
 * Fixed-shift in the Jenkins-Traub algorithm.
 *
 * @author Ken Yiu
 */
class JTFixedShift extends JTStep {

    private final int maxIterations;
    private final Polynomial pPoly;
    private final Polynomial kPoly;

    JTFixedShift(Polynomial pPoly, Polynomial kPoly, int maxIterations) {
        this.pPoly = pPoly;
        this.kPoly = kPoly;
        this.maxIterations = maxIterations;
        run();
    }

    private void run() {
        // 2.3.1) guess the modulus of the smallest root by Newton's method on Cauchy's bound
        CauchyPolynomial cp = new CauchyPolynomial(pPoly);
        double rootModulus = cp.lowerBound();

        /*
         * Initialize the previous quadratic shift (x-s1)(x-s2) = (x^2 + ux + v)
         * as the complex conjugates at -45 degree (theta), such that 1st trial
         * would be at 49 (= -45 + 94) degree (as the paper suggested).
         * And the moduli of both s1 and s2 are supposed to be the modulus which
         * are just estimated before.
         */
        double prevTheta = -45; // degree

        // 2.3.3) loop to try different quadratic shifts until the zeros are found (at most nIterations shifts)
        for (int i = 1; i <= maxIterations; ++i) {
            /*
             * 2.3.3.1) QuadraticRoot corresponds to a double shift to a non-real
             * point and its complex conjugate. The point has modulus as the
             * lower bound and amplitude rotated by 94 degrees from the previous shift.
             */
            prevTheta += 94;
            Complex s1 = Complex.fromPolar(rootModulus, toRadians(prevTheta)); // compute the new s1

            /*
             * 2.3.3.2) Second stage calculation, fixed quadratic
             * using the computed shift and the current K, generate K sequence,
             * until converge or fail. (as suggested in the paper)
             * This step jumps directly to one of the third stage iterations and
             * returns the deflated polynomial, and the zero(s) found.
             *
             * Note: the maximum number of iterations (20*i iterations) for each
             * step is increased as additional shifts are tried
             * ref: p.19 in the paper (the paragraph under (9.4))
             */
            JTFixedShiftStep step = new JTFixedShiftStep(pPoly, kPoly, s1, 20 * i);//2.3.2, 2.3.3.3) restore K and try a rotated shift
            List<Number> stepZeros = step.getZeros();

            if (!stepZeros.isEmpty()) {
                zeros.addAll(stepZeros);
                deflatedPoly = step.getDeflatedPolynomial();
                return;
            }
        }

        throw new RuntimeException("no convergence after 20 shifts");
    }
}
