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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.brownian;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.DiscretizedSDE;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.Ft;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A multivariate Brownian motion is a stochastic process with the following properties.
 * 
 * <ul>
 * <li>B(0) = 0;
 * <li>B(t), t >= 0, are continuous functions of t;
 * <li>the increments, B(t) - B(s), t > s, are independent of the past;
 * <li>the increments, B(t) - B(s), are (correlated) multi- normally distributed with mean 0.
 * </ul>
 *
 * @author Haksun Li
 *
 * @see "Fima C. Klebaner. Introduction to Stochastic Calculus with Applications. 2nd ed. Section 3.1. Imperial College Press. 2006."
 */
public class Brownian implements DiscretizedSDE {

    /**
     * the dimension of this Brownian motion
     */
    public final int d;
    /**
     * μ, the drift
     */
    public final Vector mu;
    /**
     * σ, the diffusion constant
     */
    public final Matrix sigma;

    /**
     * Construct a multi-dimensional Brownian motion.
     * 
     * @param d the dimension
     */
    public Brownian(int d) {
        this.d = d;
        mu = null;//0 vector
        sigma = null;//identity matrix
    }

    /**
     * Construct a multi-dimensional Brownian motion with μ and σ.
     * 
     * @param mu μ
     * @param sigma σ
     */
    public Brownian(Vector mu, Matrix sigma) {
        this.d = sigma.nCols();
        this.mu = mu;
        this.sigma = sigma;
    }

    public Vector dXt(Ft ft) {
        //dXt = mu * dt + sigma * sqrt(dt) * Zt;
        Vector dXt = ft.dWt();

        dXt = sigma == null ? dXt : sigma.multiply(dXt);
        dXt = mu == null ? dXt : dXt.add(mu.scaled(ft.dt()));

        return dXt;
    }

    public int nB() {
        return d;
    }

    public Ft getNewFt() {
        return new Ft();
    }
}
