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

import com.numericalmethod.suanshu.Constant;
import static java.lang.Math.*;

/**
 * This constructs a scaled polynomial that has neither too big or too small coefficients,
 * hence avoiding overflow or underflow.
 * This scaling factor is automatically computed and is a power of the base.
 *
 * @author Haksun Li
 */
public class ScaledPolynomial extends Polynomial {

    private static final double LO = Double.MIN_VALUE / Constant.MACH_EPS;

    /**
     * Construct a scaled polynomial.
     *
     * @param p    a polynomial
     * @param base base of the scaling factor
     */
    public ScaledPolynomial(Polynomial p, double base) {
        super(getScaledPolynomialForAvoidingOverflowUnderflow(p, base));
    }

    /**
     * Construct a scaled polynomial.
     *
     * @param p a polynomial
     */
    public ScaledPolynomial(Polynomial p) {
        this(p, 2.);
    }

    private static Polynomial getScaledPolynomialForAvoidingOverflowUnderflow(Polynomial p, double base) {
        double log_base = Math.log(base);
        double[] pCoeff = p.getCoefficients();

        // Find the largest and smallest of moduli of coefficients
        double maxModuli = 0.0;
        double minModuli = Double.MAX_VALUE;
        double absA = 0.0;

        for (int i = 0; i < pCoeff.length; i++) {
            absA = abs(pCoeff[i]);
            if (absA > maxModuli) {
                maxModuli = absA;
            }
            if (absA != 0.0 && absA < minModuli) {
                minModuli = absA;
            }
        }

        /*
         * Scale if there are large or very small coefficients.
         * Compute a scale factor to multiply the coefficients of the polynomial.
         * The scaling is done to avoid overflow and to avoid undetected underflow interfering with the convergence criterion.
         * The factor is a power of the base.
         */
        double scale = LO / minModuli;
        if ((scale <= 1.0 && maxModuli >= 10.0)
            || (scale > 1.0 && Double.MAX_VALUE / scale >= maxModuli)) {
            if (scale == 0.0) {
                scale = Double.MIN_VALUE;
            }
            int logScale = (int) floor(log(scale) / log_base + 0.5); // TODO: is this the same as ceil()?
            if (logScale != 0) {
                double factor = pow(base, logScale);
                return p.scaled(factor);
            }
        }

        return p; // unscaled
    }
}
