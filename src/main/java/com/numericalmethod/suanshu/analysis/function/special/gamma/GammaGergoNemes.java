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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * The Gergo Nemes' algorithm is very simple and quick to compute the Gamma function, if accuracy is not critical.
 * It may work better for large input.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Stirling%27s_approximation#A_version_suitable_for_calculators">Wikipedia: A version suitable for calculators</a>
 */
public class GammaGergoNemes extends UnivariateRealFunction implements Gamma {

    @Override
    public double evaluate(double x) {
        if (isZero(x, 0)) {
            return Double.POSITIVE_INFINITY;
        }

        double z = x > 0 ? x : -x;
        double term1 = sqrt(2d * Math.PI / z);
        double term2 = (z + 1d / (12d * z - 0.1 * z)) / Math.E;
        double term3 = pow(term2, z);
        double result = term1 * term3;
        result = x > 0 ? result : GammaLanczosQuick.reflectionFormula(-x, result);
        return result;
    }
}
