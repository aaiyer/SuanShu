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

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.QuadraticMonomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.QuadraticSyntheticDivision;
import com.numericalmethod.suanshu.number.complex.Complex;
import static java.lang.Math.abs;

/**
 * Compute the fixed shift K-polynomials, testing for convergence in the linear or quadratic case.
 * Initiate one of the variable shift iterations and return with the number of zeros found.
 *
 * @author Ken Yiu
 */
class JTFixedShiftStep extends JTStep {

    private final Polynomial pPoly;
    private final Polynomial kPoly;
    private final Complex s1;
    private final int maxIterations;
    private final QuadraticMonomial qPoly0;
    private static final double CONVERGENCE_CRITERIA_FOR_S_AND_V = 0.25;

    JTFixedShiftStep(Polynomial pPoly, Polynomial kPoly, Complex s1, int maxIterations) {
        this.pPoly = pPoly;
        this.kPoly = kPoly;
        /*
         * Since s1 and s2 are supposed to be a pair of complex conjugates.
         * u = (sum of roots) = -(s1 + s2) = -2 * Re(s1)
         * v = (product of roots) = s1 * s2 = |s1|^2
         */
        this.s1 = s1;
        double u0 = -2 * s1.real();
        double v0 = s1.modulus(); // TODO: should it be |s1|^2 ?
        this.qPoly0 = new QuadraticMonomial(u0, v0);
        this.maxIterations = maxIterations;
        run();
    }

    private void run() {//TODO: too many nested levels
        boolean areZerosNearRealAxis = true;
        double ui;
        double vi;
        boolean stry;
        boolean vtry;
        boolean spass;
        boolean vpass;
        double s;
        double ss; // s1.real is passed in as initial ss
        double oss; // last ss
        double ts; // converge step of s
        double tss; // product of 2 consecutive converge steps of s
        double ots; // last converge step of s
        double betas = CONVERGENCE_CRITERIA_FOR_S_AND_V;
        double v; // v is passed in as initial v
        double ovv; // last v
        double tv; // converge step of v
        double tvv; // product of 2 consecutive converge steps of v
        double otv; // last converge step of v
        double betav = CONVERGENCE_CRITERIA_FOR_S_AND_V;
        oss = s1.real();
        ovv = qPoly0.v();
        ots = otv = 0.0;
        ui = vi = 0.0;
        Polynomial kPolyNew = kPoly;
        // 2.3.3.2.1) compute a, b from u, v, P using quadratic synthetic division
        QuadraticSyntheticDivision division = new QuadraticSyntheticDivision(pPoly, qPoly0);
        Polynomial qpPoly = division.quotient(); // QP(z)
        double b = division.b();
        double a = division.a();
        // 2.3.3.2.2) compute c, d, a1, a3, a7 from a, b, u, v, K by Eq.(9.8)
        JTUtils.VarSet varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly0);
        // iterate for fixed-shift
        for (int i = 0; i < maxIterations; ++i) {
            // 2.3.3.2.3) generate next K from a1, a3, a7
            kPolyNew = JTUtils.getNextK(kPolyNew, qpPoly, varSet);
            // 2.3.3.2.4) estimate the next pair of (u, v) in x^2 + ux + v from K
            varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly0);
            double[] uvPair = JTUtils.getUVEstimation(pPoly, kPolyNew, varSet, qPoly0); // estimate next shift (x^2 + ux + v)
            ui = uvPair[0];
            vi = uvPair[1];
            v = vi;
            /*
             * Estimate new s1
             * s1 = s1 - P(s1)/K(s1)
             */
            double k_0 = kPolyNew.getCoefficient(kPolyNew.degree()); // the constant term of K
            double p_0 = pPoly.getCoefficient(pPoly.degree()); // the constant term of P
            ss = ((k_0 != 0.0) ? -(p_0 / k_0) : 0.0);
            tv = 1.0;
            ts = 1.0;
            if ((i != 0) && (varSet.type != JTUtils.VarSet.Type.UNSCALED)) {
                // Compute relative measures of convergence of v and s sequences
                tv = ((v != 0.0) ? abs((v - ovv) / v) : tv);
                ts = ((ss != 0.0) ? abs((ss - oss) / ss) : ts);
                // If decreasing, multiply the two most recent convergence measures
                tvv = ((tv < otv) ? tv * otv : 1.0);
                tss = ((ts < ots) ? ts * ots : 1.0);
                // Check against the convergence criteria
                vpass = (tvv < betav);
                spass = (tss < betas);
                if (spass || vpass) {
                    /*
                     * 2.3.3.2.5) enter variable-shift stage if one of the
                     * sequences (s or v) passes the convergence test.
                     * If variable-shift succeeds, finish; otherwise, loop
                     * again to try a different pair of K and sigma.
                     */
                    s = ss;
                    // Choose iteration according to the fastest converging sequence
                    for (stry = vtry = areZerosNearRealAxis = false; (spass && !stry) || (vpass && !vtry) || areZerosNearRealAxis;) {
                        /*
                         * check quadratic shift if any of these 4 conditions is fulfilled:
                         * 1. only v converges
                         * 2. both s and v converge, but real iteration failed
                         * 3. both s and v converge, but v converges faster
                         * 4. real iteration indicates there are zeros near the real axis
                         */
                        if ((vpass && (!spass || stry || tvv <= tss)) || areZerosNearRealAxis) {
                            JTVariableShiftQuadratic iteration = new JTVariableShiftQuadratic(pPoly, kPolyNew, new QuadraticMonomial(ui, vi), 20);
                            if (!iteration.getZeros().isEmpty()) {
                                zeros.addAll(iteration.getZeros());
                                deflatedPoly = iteration.deflatedPoly;
                                return;
                            }
                            vtry = true;
                            betav *= CONVERGENCE_CRITERIA_FOR_S_AND_V; // the previous one is too loose, tighten the criterion and try again
                            if (areZerosNearRealAxis) {
                                areZerosNearRealAxis = false;
                            }
                        }
                        if (spass && !stry) {
                            // try real iteration to check if s1 and s2 are double real roots
                            JTVariableShiftLinear iteration = new JTVariableShiftLinear(pPoly, kPolyNew, s, 10);
                            areZerosNearRealAxis = iteration.areZerosNearRealAxis();
                            s = iteration.finalS();
                            if (!iteration.getZeros().isEmpty()) {
                                zeros.addAll(iteration.getZeros());
                                deflatedPoly = iteration.deflatedPoly;
                                return;
                            }
                            stry = true;
                            betas *= CONVERGENCE_CRITERIA_FOR_S_AND_V;
                            if (areZerosNearRealAxis) {
                                // If linear iteration signals an almost double real zero, attempt quadratic iteration with new (u, v) pair
                                ui = -(s + s);
                                vi = s * s;
                            }
                        }
                    }
                    // Re-compute QP(z) and scalar values to continue the second stage
                    division = new QuadraticSyntheticDivision(pPoly, qPoly0);
                    qpPoly = division.quotient();
                    b = division.b();
                    a = division.a();
                    varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly0);
                }
            }
            ovv = v;
            oss = ss;
            otv = tv;
            ots = ts;
        }
    }
}
