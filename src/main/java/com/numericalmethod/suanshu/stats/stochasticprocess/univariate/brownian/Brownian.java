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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.brownian;

import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.DiscretizedSDE;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.Ft;

/**
 * A Brownian motion is a stochastic process with the following properties.
 * 
 * <ul>
 * <li>B(0) = 0;
 * <li>B(t), t >= 0, are continuous functions of t;
 * <li>the increments, B(t) - B(s), t > s, are independent of the past;
 * <li>the increments, B(t) - B(s), are normally distributed with mean 0 and variance (t - s).
 * </ul>
 *
 * @author Haksun Li
 *
 * @see "Fima C. Klebaner. Introduction to Stochastic Calculus with Applications. 2nd ed. Section 3.1. Imperial College Press. 2006."
 */
public class Brownian implements DiscretizedSDE {

    /**
     * μ, the drift
     */
    public final double mu;
    /**
     * σ, the diffusion constant
     */
    public final double sigma;

    /**
     * Construct a univariate Brownian motion.
     *
     * @param mu μ, the drift
     * @param sigma σ, the diffusion constant
     */
    public Brownian(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    /**
     * Construct a univariate standard Brownian motion.
     */
    public Brownian() {
        mu = 0;
        sigma = 1;
    }

    public double dXt(Ft ft) {
        return mu * ft.dt() + sigma * ft.dWt();
    }

    public Ft getNewFt() {
        return new Ft();
    }
}
