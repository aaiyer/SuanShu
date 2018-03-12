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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;

/**
 * This is the first order derivative function of a {@link Polynomial}, which, again, is a polynomial.
 *
 * @author Haksun Li
 * @see Polynomial
 */
public class DPolynomial extends Polynomial {

    /**
     * Construct the derivative function of a {@link Polynomial}, which, again, is a polynomial.
     *
     * @param polynomial a polynomial
     */
    public DPolynomial(Polynomial polynomial) {
        super(derivative(polynomial));
    }

    //TODO: how to get rid of the 'static'?
    private static double[] derivative(Polynomial polynomial) {
        double[] p = polynomial.getCoefficients();
        double[] dp = new double[p.length > 1 ? p.length - 1 : 1];//d(constant) = 0

        for (int i = 0; i < p.length - 1; ++i) {
            dp[i] = (p.length - 1 - i) * p[i];
        }

        return dp;
    }
}
