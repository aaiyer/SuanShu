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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde;

import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.Realization;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.SDE;
import java.util.Iterator;

/**
 * This class computes the expectation of a stochastic integral.
 * It does so by Monte Carlo simulation over a discretized interval,
 * compute the mean (the expectation) and the variance.
 *
 * @author Haksun Li
 */
public class Expectation {

    /**
     * the value of the integral
     */
    public final double value;
    /**
     * the variance of the integral
     */
    public final double variance;

    /**
     * Compute an expectation of a stochastic integral.
     * 
     * @param integrand a simulation of the integrand
     * @param x0 the initial value
     * @param N the number of simulations
     */
    public Expectation(Construction integrand, double x0, int N) {
        Mean mean = new Mean();
        Variance var = new Variance();

        for (int i = 0; i < N; ++i) {
            Realization xt = integrand.nextRealization(x0);
            Iterator<Realization.Entry> it = xt.iterator();

            double xT = Double.NaN;
            for (; it.hasNext();) {
                xT = it.next().getValue();
            }

            mean.addData(xT);
            var.addData(xT);
        }

        value = mean.value();
        variance = var.value();
    }

    /**
     * Compute an expectation of a stochastic integral.
     *
     * @param integrand the integrand SDE
     * @param T0 the beginning of the integral time interval
     * @param T the ending of the integral time interval
     * @param nT the number of sub-intervals in [T0, T], hence time interval discretization
     * @param x0 the initial value
     * @param N the number of simulations
     */
    public Expectation(SDE integrand, double T0, double T, int nT, double x0, int N) {
        this(new Euler(integrand, new EvenlySpacedGrid(T0, T, nT)),
                x0,
                N);
    }

    /**
     * Get the integral value.
     *
     * @return the integral value
     */
    public double value() {
        return value;
    }

    /**
     * Get the integral variance.
     *
     * @return the integral variance
     */
    public double variance() {
        return variance;
    }
}
