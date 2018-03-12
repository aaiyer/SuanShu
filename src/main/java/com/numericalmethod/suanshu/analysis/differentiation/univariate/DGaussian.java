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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Gaussian;

/**
 * This is the first order derivative function of a {@link Gaussian} function, \({d \mathrm{\phi}(x) \over dx}\).
 *
 * @author Haksun Li
 * @see Gaussian
 */
public class DGaussian extends UnivariateRealFunction {

    private final Gaussian phi;//the Gaussian function to take the derivative of
    private static final Gaussian gaussian = new Gaussian();

    /**
     * Construct the derivative function of a Gaussian function.
     *
     * @param phi a {@link Gaussian} function
     */
    public DGaussian(Gaussian phi) {
        this.phi = phi;
    }

    @Override
    public double evaluate(double x) {
        return -(x - phi.b()) / phi.c() / phi.c() * gaussian.evaluate(x);
    }
}
