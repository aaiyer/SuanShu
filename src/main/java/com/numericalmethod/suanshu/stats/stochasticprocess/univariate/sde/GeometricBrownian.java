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
 * A Geometric Brownian motion (GBM) (occasionally, exponential Brownian motion) is
 * a continuous-time stochastic process in which the logarithm of the randomly varying quantity follows a Brownian motion.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Geometric_Brownian_motion">Wikipedia: Geometric Brownian motion</a>
 */
public class GeometricBrownian extends SDE {

    /**
     * Construct a Geometric Brownian motion.
     *
     * <blockquote><code><pre>
     * dS = rSdt + σSdW
     * </pre></code></blockquote>
     * 
     * @param r the log drift
     * @param sigma the log diffusion coefficient
     */
    public GeometricBrownian(final double r, final double sigma) {
        super(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return r * ft.Xt();
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sigma * ft.Xt();
                    }
                });
    }
}
