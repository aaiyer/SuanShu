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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

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
     * the continuous-time multivariate SDE
     */
    public final SDE sde;

    /**
     * Discretize a multivariate SDE using the Euler scheme.
     *
     * @param sde a continuous-time SDE
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
    public Vector dXt(Ft ft) {
        Vector mu = sde.mu.evaluate(ft);
        Matrix sigma = sde.sigma.evaluate(ft);

        Vector dWt = ft.dWt();
        Vector dXt = sigma.multiply(dWt);
        dXt = dXt.add(mu.scaled(ft.dt()));

        return dXt;
    }

    public int nB() {
        return sde.nB;
    }

    public Ft getNewFt() {
        return sde.getFt();
    }
}
