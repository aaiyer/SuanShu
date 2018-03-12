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

/**
 * The Euler scheme is the first order approximation of an SDE.
 *
 * <blockquote><code><pre>
 * dXt = μ * dt + σ * sqrt(dt) * Zt;
 * </pre></code></blockquote>
 *
 * @author Haksun Li
 */
public class Euler implements DiscretizedSDE {

    /**
     * the continuous-time univariate SDE
     */
    public final SDE sde;

    /**
     * Discretize a univariate SDE using the Euler scheme.
     *
     * @param sde continuous-time SDE
     */
    public Euler(SDE sde) {
        this.sde = sde;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * dXt = μ * dt + σ * sqrt(dt) * Zt;
     *
     * @param ft filtration
     * @return the increment of the process in {@code dt}
     */
    public double dXt(Ft ft) {
        double mu = sde.mu.evaluate(ft);
        double sigma = sde.sigma.evaluate(ft);

        double dWt = ft.dWt();
        double dXt = mu * ft.dt() + sigma * dWt;

        return dXt;
    }

    public Ft getNewFt() {
        return sde.getFt();
    }
}
