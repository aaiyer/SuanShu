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
import static java.lang.Math.abs;

/**
 * Utility functions for the Jenkins-Traub algorithm.
 *
 * @author Ken Yiu
 */
class JTUtils {

    private JTUtils() {
    }

    /**
     * Store all the scalars for computing next K-polynomial and (u, v) pair.
     */
    static class VarSet {

        static enum Type {

            /** a1, a3, a7, e, f, g are divided by c */
            DIVIDED_BY_C,
            /** a1, a3, a7, e, f, g are divided by d */
            DIVIDED_BY_D,
            /** The quadratic is almost a factor of K(z) */
            UNSCALED
        }

        final Type type;
        final double a1, a3, a7, a, b, c, d, e, f, g, h;
        final Polynomial qkPoly;

        VarSet(Type type, double a1, double a3, double a7, double a, double b, double c, double d, double e, double f, double g, double h, Polynomial qkPoly) {
            this.type = type;
            this.a1 = a1;
            this.a3 = a3;
            this.a7 = a7;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.qkPoly = qkPoly;
        }
    }

    /**
     * This method calculates scalar quantities used to compute the next K polynomial and
     * new estimates of the quadratic coefficients.
     * VarSet.Type indicates how the calculations are normalized to avoid overflow.
     * ref: p.20 in the paper
     */
    static VarSet getScalarsForNextK(Polynomial kPoly, double b, double a, QuadraticMonomial qPoly) {
        double u = qPoly.u();
        double v = qPoly.v();
        double a1 = 0., a3 = 0., a7 = 0., c = 0., d = 0., e = 0., f = 0., g = 0., h = 0.;

        // Synthetic division of K by the quadratic 1, u, v
        QuadraticSyntheticDivision quadSD = new QuadraticSyntheticDivision(kPoly, qPoly);
        Polynomial qkPoly = quadSD.quotient();
        d = quadSD.b();
        c = quadSD.a();

        if (abs(c) <= (100.0 * Constant.MACH_EPS * abs(kPoly.getCoefficient(kPoly.degree() - 1)))) {
            if (abs(d) <= (100.0 * Constant.MACH_EPS * abs(kPoly.getCoefficient(kPoly.degree() - 2)))) {
                // both c and d are close to zero, i.e., no remainder.
                // QK(z) will be the next K-polynomial
                return new VarSet(VarSet.Type.UNSCALED, a1, a3, a7, a, b, c, d, e, f, g, h, qkPoly);
            }
        }

        h = v * b;
        // scalars
        if (abs(d) >= abs(c)) {
            e = a / d;
            f = c / d;
            g = u * b;
            a3 = e * (g + a) + h * (b / d); // ( a^2 + uab + vb^2 ) / d
            a7 = h + (f + u) * a;           //   ( ac + uad + vbd ) / d
            a1 = -a + f * b;                //          ( bc - ad ) / d
            return new VarSet(VarSet.Type.DIVIDED_BY_D, a1, a3, a7, a, b, c, d, e, f, g, h, qkPoly);
        } else {
            e = a / c;
            f = d / c;
            g = e * u;
            a3 = e * a + (g + h / c) * b; // ( a^2 + uab + vb^2 ) / c
            a7 = g * d + h * f + a;       //   ( ac + uad + vbd ) / c
            a1 = -(a * (d / c)) + b;      //          ( bc - ad ) / c
            return new VarSet(VarSet.Type.DIVIDED_BY_C, a1, a3, a7, a, b, c, d, e, f, g, h, qkPoly);
        }
    }

    static Polynomial getNextK(Polynomial kPoly, Polynomial qpPoly, VarSet varSet) {
        // Computes the next K polynomials using the scalars computed
        double temp;
        if (varSet.type == VarSet.Type.UNSCALED) { // Use unscaled form of the recurrence
            //K(z) = QK(z)
            return varSet.qkPoly;
        }

        temp = ((varSet.type == VarSet.Type.DIVIDED_BY_C) ? varSet.b : varSet.a);
        if (abs(varSet.a1) > (10.0 * Constant.MACH_EPS * abs(temp))) {
            /*
             * Use scaled form of the recurrence:
             * K(z) = a3/a1 * QK(z) - a7/a1 * QP(z) + z * QP(z) + b
             */
            double a7a1 = varSet.a7 / varSet.a1;
            double a3a1 = varSet.a3 / varSet.a1;
            return varSet.qkPoly.scaled(a3a1).
                    add(qpPoly.scaled(-a7a1)).
                    add(qpPoly.multiply(new Polynomial(1, 0))).
                    add(new Polynomial(varSet.b));
        } else {
            /*
             * If a1 is nearly zero, then use a special form of the recurrence:
             * K(z) = a3 * QK(z) - a7 * QP(z)
             */
            return varSet.qkPoly.scaled(varSet.a3).
                    add(qpPoly.scaled(-varSet.a7));
        }
    }

    /**
     * Estimate a new pair of u and v.
     *
     * @param varSet varSet
     * @param u      the old value of u
     * @param v      the old value of v
     * @return a new pair of estimated (u, v) in a double array [u' v']
     */
    static double[] getUVEstimation(Polynomial pPoly, Polynomial kPoly, VarSet varSet, QuadraticMonomial qPoly) {
        double u = qPoly.u();
        double v = qPoly.v();

        // Compute new estimates of the quadratic coefficients using the scalars computed
        double a4 = 0.0;
        double a5 = 0.0;

        switch (varSet.type) {
            case UNSCALED:
                return new double[]{0.0, 0.0}; // The quadratic is zeroed
            case DIVIDED_BY_C:
                a4 = varSet.a + u * varSet.b + varSet.h * varSet.f;
                a5 = varSet.c + (u + v * varSet.f) * varSet.d;
                break;
            case DIVIDED_BY_D:
                a4 = (varSet.a + varSet.g) * varSet.f + varSet.h;
                a5 = (varSet.f + u) * varSet.c + v * varSet.d;
                break;
        }

        // Evaluate new quadratic coefficients
        double b1, b2, c1, c2, c3, c4, temp;
        b1 = -kPoly.getCoefficient(kPoly.degree()) / pPoly.getCoefficient(pPoly.degree());
        b2 = -(kPoly.getCoefficient(kPoly.degree() - 1) + b1 * pPoly.getCoefficient(pPoly.degree() - 1)) / pPoly.getCoefficient(pPoly.degree());
        c1 = v * b2 * varSet.a1;
        c2 = b1 * varSet.a7;
        c3 = b1 * b1 * varSet.a3;
        c4 = -(c2 + c3) + c1;
        temp = -c4 + a5 + b1 * a4;
        if (temp != 0.0) {
            return new double[]{
                        -((u * (c3 + c2) + v * (b1 * varSet.a1 + b2 * varSet.a7)) / temp) + u, // new estimate of u
                        v * (1.0 + c4 / temp) // new estimate of v
                    };
        }

        return new double[]{0.0, 0.0};
    }
}
