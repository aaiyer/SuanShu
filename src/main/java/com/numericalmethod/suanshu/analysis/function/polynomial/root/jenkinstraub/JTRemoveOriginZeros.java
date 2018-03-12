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
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.Arrays;

/**
 * Deflate a polynomial by removing the zeros at the origin.
 *
 * @author Ken Yiu
 */
class JTRemoveOriginZeros extends JTStep {

    private final Polynomial pPoly;
    private static double epsilon = 0;

    JTRemoveOriginZeros(Polynomial pPoly) {
        this.pPoly = pPoly;
        run();
    }

    private void run() {
        double[] pCoeff = pPoly.getCoefficients();
        int nZeros = 0; // count the number of zeros at the origin, same as the number of 0 coefficients
        while (DoubleUtils.isZero(pCoeff[pPoly.degree() - nZeros], epsilon)) {//starting checking from the constant term
            zeros.add(0.0);
            nZeros++;
        }

        deflatedPoly = new Polynomial(Arrays.copyOf(pCoeff, pCoeff.length - nZeros));
    }
}
