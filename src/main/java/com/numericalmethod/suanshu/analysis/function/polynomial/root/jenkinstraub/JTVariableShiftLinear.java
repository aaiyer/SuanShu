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
import com.numericalmethod.suanshu.analysis.function.polynomial.HornerScheme;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import static java.lang.Math.abs;

/**
 * Perform a variable shift with a linear factor (a single real zero).
 * This step can be used in both algorithms for real and complex polynomials.
 *
 * @author Ken Yiu
 */
class JTVariableShiftLinear extends JTStep {

    private final Polynomial pPoly;
    private final Polynomial kPoly;
    private final double initialS;
    private final int maxIterations;
    private double finalS = 0.0;
    /** flag to indicate a pair of zeros near real axis */
    private boolean areZerosNearRealAxis = false;

    JTVariableShiftLinear(Polynomial pPoly, Polynomial kPoly, double s, int maxIterations) {
        this.pPoly = pPoly;
        this.kPoly = kPoly;
        this.initialS = s;
        this.maxIterations = maxIterations;
        run();
    }

    /**
     * 2.4.2) Real Iteration (linear) (same as the algorithm for complex polynomial)
     * 2.4.2.1) loop to evolve s and K
     * 2.4.2.1.1) evaluate p(s) and check if p(s) is close enough to zero; if so, success
     * 2.4.2.1.2) if p(s) increases significantly, failure and suggest to run Quadratic Iteration instead
     * 2.4.2.1.3) compute (K, s)
     * 2.4.2.1.4) the loop ends until success/failure/over 10 multiply
     */
    private final void run() {
        // Variable-shift H-polynomial iteration for a real zero
        Polynomial kPolyNew = kPoly;
        Polynomial qpPoly = null;
        Polynomial qkPoly = null;
        HornerScheme horner = null; // for computing polynomial division by (x-s)
        double ee;
        double absS;
        double sInc = 0.0; // increment to s after each iteration
        double ps = 0.0; // value of P(x)
        double absPs1;
        double absPs0 = 0.0;
        double ks = 0.0; // value of K(x)
        double s = initialS;

        for (int stepCount = 0; stepCount < maxIterations; ++stepCount) {
            // Evaluate P(s)
            horner = new HornerScheme(pPoly, s);
            qpPoly = horner.quotient(); // the quotient QP(x)
            ps = horner.remainder(); // value of P(s)
            absPs1 = abs(ps);
            // Compute a rigorous bound on the error in evaluating p
            absS = abs(s);
            ee = 0.5 * abs(qpPoly.getCoefficient(0));
            for (int i = 1; i <= qpPoly.degree(); ++i) {
                ee = ee * absS + abs(qpPoly.getCoefficient(i));
            }

            //Iteration has converged sufficiently if the polynomial value is less than 20 times this bound.
            if (absPs1 <= 20.0 * Constant.MACH_EPS * (2.0 * ee - absPs1)) {
                zeros.add(s);
                deflatedPoly = qpPoly;
                break;
            }

            if (stepCount > 0) {
                // compare with the last P(s) value
                if ((abs(sInc) <= 0.0010 * abs(-sInc + s)) && (absPs1 > absPs0)) {
                    // A cluster of zeros near the real axis has been encountered;
                    // Return with a flag set to initiate a quadratic iteration
                    areZerosNearRealAxis = true;
                    finalS = s;
                    break; // Return if the polynomial value has increased significantly
                }
            }

            // if this is not the last step, compute the next polynomial, the new iterate, and sInc
            if (stepCount + 1 < maxIterations) {
                absPs0 = absPs1;
                horner = new HornerScheme(kPolyNew, s);
                qkPoly = horner.quotient();
                ks = horner.remainder();
                if (abs(ks) > abs(kPolyNew.getCoefficient(kPolyNew.degree())) * 10.0 * Constant.MACH_EPS) {
                    // Use the scaled form of the recurrence if the value of K at s is non-zero
                    kPolyNew = qpPoly.add(qkPoly.scaled(-(ps / ks)));
                } else {
                    // Use unscaled form
                    // K(z) = QK(z) / z
                    horner = new HornerScheme(qkPoly, 0);
                    kPolyNew = horner.quotient();
                }
                horner = new HornerScheme(kPolyNew, s);
                ks = horner.remainder();
                sInc = (abs(ks) > (abs(kPolyNew.getCoefficient(kPolyNew.degree())) * 10.0 * Constant.MACH_EPS)) ? -(ps / ks) : 0.0;
                s += sInc;
            }
        }
    }

    /**
     * @return {@code true} if it requires a QuadraticIteration to find the zeros
     */
    boolean areZerosNearRealAxis() {
        return areZerosNearRealAxis;
    }

    double finalS() {
        return finalS;
    }
}
