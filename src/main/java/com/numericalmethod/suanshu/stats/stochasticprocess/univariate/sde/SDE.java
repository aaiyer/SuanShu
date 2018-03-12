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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde;

import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Diffusion;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Drift;

/**
 * This class represents a univariate, continuous-time Stochastic Differential Equation of this form:
 * <i>dX(t) = μ(t, Xt, Zt, ...) * dt + σ(t, Xt, Zt, ...) * dB(t)</i>.
 *
 * @author Haksun Li
 */
public class SDE {

    /**
     * the drift
     *
     * <blockquote><code><pre>
     * μ(t, Xt, Zt, ...)
     * </pre></code></blockquote>
     */
    public final Drift mu;
    /**
     * the diffusion coefficient
     *
     * <blockquote><code><pre>
     * σ(t, Xt, Zt, ...)
     * </pre></code></blockquote>
     */
    public final Diffusion sigma;

    /**
     * Construct a univariate diffusion type stochastic differential equation.
     *
     * @param mu the drift
     * @param sigma the diffusion coefficient
     */
    public SDE(Drift mu, Diffusion sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    /**
     * Get an empty filtration for the process.
     *
     * @return an empty filtration
     */
    public Ft getFt() {
        return new Ft();
    }
}
