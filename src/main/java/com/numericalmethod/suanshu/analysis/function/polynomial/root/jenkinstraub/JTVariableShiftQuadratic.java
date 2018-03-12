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
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.QuadraticMonomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.QuadraticSyntheticDivision;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.QuadraticRoot;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.number.complex.Complex;
import static java.lang.Math.*;
import java.util.List;

/**
 * Perform a variable shift with a quadratic factor (a pair of complex
 * conjugates or double real zeros).
 *
 * @author Ken Yiu
 */
class JTVariableShiftQuadratic extends JTStep {

    private final Polynomial pPoly;
    private final Polynomial kPoly;
    private final QuadraticMonomial qPoly0;
    private final int maxIterations;
    private final QuadraticRoot quadratic = new QuadraticRoot();

    JTVariableShiftQuadratic(Polynomial pPoly, Polynomial kPoly, QuadraticMonomial qPoly0, int maxIterations) {
        this.pPoly = pPoly;
        this.kPoly = kPoly;
        this.qPoly0 = qPoly0;
        this.maxIterations = maxIterations;
        run();
    }

    private void run() {
        /*
         * 2.4.1) QuadraticRoot Iteration (complex conjugate pair or double real zeros)
         * 2.4.1.1) find the roots of the quadratic factor x^2 + ux + v
         * 2.4.1.2) check if the roots are equimodular; if not, fail
         * 2.4.1.3) evaluate p(s1) and check if it is close to zero; if so, success
         * 2.4.1.4) if p(s1) is greater than the previous evaluation, fine tune (u,v) by 5 iterations of (K,u,v) computation
         * 2.4.1.5) if v turns to be zero, fail
         * 2.4.1.6) the loop ends until either success/failure/over 20 multiply
         * Variable-shift K-polynomial iteration for a quadratic factor converges only if the
         * zeros are equimodular or nearly so.
         */
        Polynomial kPolyNew = kPoly;
        boolean triedFlag = false;
        double absPvLast = 0.0;
        QuadraticMonomial qPoly = qPoly0;
        double relstp = 0.0;

        for (int stepCount = 0; stepCount < maxIterations; ++stepCount) {
            List<? extends Number> quadZeros = quadratic.solve(new Polynomial(1.0, qPoly.u(), qPoly.v()));
            Complex sz = null; // smaller zero of the quadratic
            Complex lz = null; // larger zero of the quadratic
            if (NumberUtils.isReal(quadZeros.get(0))) { // 2 real roots
                if (abs(quadZeros.get(0).doubleValue()) < abs(quadZeros.get(1).doubleValue())) {
                    sz = new Complex(quadZeros.get(0).doubleValue());
                    lz = new Complex(quadZeros.get(1).doubleValue());
                } else {
                    sz = new Complex(quadZeros.get(1).doubleValue());
                    lz = new Complex(quadZeros.get(0).doubleValue());
                }
            } else { // complex conjugates
                sz = (Complex) quadZeros.get(0);
                lz = (Complex) quadZeros.get(1);
            }

            //Return if roots of the quadratic are real and not close to multiple or nearly equal and of opposite sign.
            if (abs(abs(sz.real()) - abs(lz.real())) > 0.01 * abs(lz.real())) {
                break;
            }

            QuadraticSyntheticDivision division = new QuadraticSyntheticDivision(pPoly, qPoly);
            Polynomial qpPoly = division.quotient();
            double b = division.b();
            double a = division.a();
            double t = -(sz.real() * b);
            double absPvReal = abs(t + a);
            double absPv = absPvReal + abs(sz.imaginary() * b); // sum of absolute real and imaginary parts of P(sz)
            // Compute a rigorous bound on the rounding error in evaluating p
            double zm = sqrt(abs(qPoly.v()));
            double errBound = 2.0 * abs(qpPoly.getCoefficient(0));
            for (int j = 1; j < qpPoly.degree(); ++j) {
                errBound = errBound * zm + abs(qpPoly.getCoefficient(j));
            }
            errBound = errBound * zm + absPvReal;
            errBound = (9.0 * errBound + 2.0 * abs(t) - 7.0 * (absPvReal + zm * abs(b))) * Constant.MACH_EPS;
            // Iteration has converged sufficiently if the polynomial value is less than 20 multiply this bound
            if (absPv <= 20.0 * errBound) {
                zeros.addAll(quadZeros);
                deflatedPoly = qpPoly;
                break;
            }

            JTUtils.VarSet varSet = null;
            if (stepCount > 0) {
                if ((relstp <= 0.01) && (absPv >= absPvLast) && (!triedFlag)) {
                    // A cluster appears to be stalling the convergence. Five fixed shift
                    // steps are taken with a u, v close to the cluster.
                    relstp = ((relstp < Constant.MACH_EPS) ? sqrt(Constant.MACH_EPS) : sqrt(relstp));
                    double u = qPoly.u() - qPoly.u() * relstp;
                    double v = qPoly.v() + qPoly.v() * relstp;
                    qPoly = new QuadraticMonomial(u, v);
                    division = new QuadraticSyntheticDivision(pPoly, qPoly);
                    qpPoly = division.quotient();
                    b = division.b();
                    a = division.a();
                    for (int j = 0; j < 5; ++j) {
                        varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly);
                        kPolyNew = JTUtils.getNextK(kPolyNew, qpPoly, varSet);
                    }
                    triedFlag = true; // this fix is performed only once
                    stepCount = 0; // restart after fix
                }
            }

            // if this is not the last step, calculate next K polynomial and new u and v
            if (stepCount + 1 < maxIterations) {
                absPvLast = absPv;
                varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly);
                kPolyNew = JTUtils.getNextK(kPolyNew, qpPoly, varSet);
                varSet = JTUtils.getScalarsForNextK(kPolyNew, b, a, qPoly);
                double[] uvNewPair = JTUtils.getUVEstimation(pPoly, kPolyNew, varSet, qPoly);
                double uNew = uvNewPair[0];
                double vNew = uvNewPair[1];

                if (vNew != 0) {
                    relstp = abs((-qPoly.v() + vNew) / vNew);
                    qPoly = new QuadraticMonomial(uNew, vNew);
                } else {
                    // If vi is zero, the iteration is not converging
                    break;
                }
            }
        }
    }
}
