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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * This class represents a function of time and a (fixed) Brownian path.
 * The function is not necessarily adapted.
 *
 * @author Haksun Li
 */
public abstract class FiltrationFunction extends UnivariateRealFunction {

    /**
     * Compute the value at the t-th time point, f(T[t]).
     * 
     * @param t the index to time
     * @return f(T[t])
     */
    public abstract double evaluate(int t);
    /**
     * the filtration, containing all histories
     */
    protected Filtration FT;

    /**
     * Set the filtration for this function.
     *
     * <p>
     * This function is called for each call to {@link Integrator#integral} before doing the integration.
     * 
     * @param FT a filtration
     */
    public void setFT(Filtration FT) {
        this.FT = FT;
    }

    /**
     * Compute all values at all time points.
     * 
     * @return {f(T[t])}, for each time point
     */
    public double[] ft() {
        int n = FT.size();
        double[] ft = new double[n];
        for (int t = 0; t < n; ++t) {//TODO: t in [1, n] instead?
            ft[t] = evaluate(t);
        }

        return ft;
    }

    public double evaluate(double t) {
        return evaluate((int) t);
    }
}
