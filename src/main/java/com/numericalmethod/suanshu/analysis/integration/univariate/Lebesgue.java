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
package com.numericalmethod.suanshu.analysis.integration.univariate;

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import java.util.Arrays;

/**
 * Lebesgue integration is the general theory of integration of a function with respect to a general measure.
 * <p/>
 * This implementation defines an Lebesgue integral as a dot product between the integrand <i>f</i> and the measure <i>d\mu</i>.
 * \[
 * \int_E f \, d \mu = \int_E f(x) \, \mu(dx) \approx \sum_E f(x)\mu(dx)
 * \]
 *
 * @author Haksun Li
 */
public class Lebesgue {

    /** the function values */
    private double[] fx = new double[]{0};
    /** the measure values */
    private double[] du = new double[]{0};//a set of measure 0

    /**
     * Construct a Lebesgue integral.
     *
     * @param fx the integrand values
     * @param du the measure values
     */
    public Lebesgue(double[] fx, double[] du) {
        this.fx = Arrays.copyOf(fx, fx.length);
        this.du = Arrays.copyOf(du, du.length);
    }

    /**
     * Get the integral value.
     *
     * @return the integral value
     */
    public double value() {
        return FunctionOps.dotProduct(fx, du);
    }
}
