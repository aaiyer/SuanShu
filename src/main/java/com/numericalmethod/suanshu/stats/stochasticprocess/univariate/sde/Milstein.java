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

import com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference;
import com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference.Type;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * Milstein scheme is a first-order approximation to a continuous-time SDE.
 * It adds a term to the Euler scheme by expanding both the drift and diffusion terms to O(dt).
 *
 * <blockquote><code><pre>
 * dXt = μ * dt + σ * sqrt(dt) * Zt + 0.5 * σ' * σ * dt * (Zt<sup>2</sup> - 1);
 * </pre></code></blockquote>
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Milstein_method">Wikipedia: Milstein method</a>
 */
public class Milstein implements DiscretizedSDE {

    /**
     * the continuous-time SDE specification
     */
    public final SDE sde;

    /**
     * Discretize a univariate SDE using the Milstein scheme.
     *
     * @param sde a continuous-time SDE
     */
    public Milstein(SDE sde) {
        this.sde = sde;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This is an implementation of the Milstein scheme.
     *
     * @param ft filtration
     * @return the increment of the process in {@code dt}
     */
    public double dXt(Ft ft) {
        double a = sde.mu.evaluate(ft);
        double b = sde.sigma.evaluate(ft);
        double db = db(ft);

        double dt = ft.dt();
        double Zt = ft.Zt();

        double dXt = a * dt;
        dXt += b * ft.dWt();
        dXt += 0.5 * db * b * dt * (Zt * Zt - 1);

        return dXt;
    }

    public Ft getNewFt() {
        return sde.getFt();
    }

    public double db(final Ft ft) {

        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {//x = Xt
                Ft xt = ft.deepCopy();
                xt.setXt(x);
                double fx = sde.sigma.evaluate(xt);
                return fx;
            }
        };

        FiniteDifference dfdx = new FiniteDifference(f, 1, Type.CENTRAL);
        double db = dfdx.evaluate(ft.Xt());

        return db;
    }
}
