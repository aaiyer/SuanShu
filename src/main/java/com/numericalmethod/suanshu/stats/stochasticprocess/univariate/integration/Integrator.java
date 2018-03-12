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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration;

import com.numericalmethod.suanshu.analysis.integration.univariate.Lebesgue;

/**
 * The class represents an integral for a function, in the Lebesgue sense.
 *
 * @author Haksun Li
 */
public abstract class Integrator {

    /**
     * Get an array of function values.
     *
     * @return an array of function values
     */
    public abstract double[] ft();

    /**
     * Get an array of the measure values.
     *
     * @return an array of the measure values
     */
    public abstract double[] du();
    /**
     * the integrand
     */
    public final FiltrationFunction f;

    /**
     * Construct an integral from an integrand.
     * 
     * @param f an integrand
     */
    public Integrator(FiltrationFunction f) {
        this.f = f;
    }

    /**
     * Integrate the function for a given filtration.
     * 
     * @param FT a filtration
     * @return the integral value
     */
    public double integral(Filtration FT) {
        f.setFT(FT);

        double[] ft = ft();
        double[] dt = du();
        Lebesgue integral = new Lebesgue(ft, dt);
        double value = integral.value();
        return value;
    }
}
