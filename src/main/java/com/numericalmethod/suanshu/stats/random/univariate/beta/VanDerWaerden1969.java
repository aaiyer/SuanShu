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
package com.numericalmethod.suanshu.stats.random.univariate.beta;

import com.numericalmethod.suanshu.stats.random.univariate.gamma.RandomGammaGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.gamma.XiTanLiu2010a;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * If <i>X</i> and <i>Y</i> are independent, with <i>X ~ Γ(α,θ)</i> and <i>Y ~ Γ(β,θ)</i> then <i>X/(X+Y) ~ Beta(α,β)</i>,
 * so one algorithm for generating beta variates is to generate <i>X/(X+Y)</i>,
 * where <i>X</i> is a gamma variate with parameters <i>Γ(α,1)</i> and <i>Y</i> is an independent gamma variate with parameters <i>Γ(β,1)</i>.
 *
 * @author Haksun Li
 * @see "van der Waerden, B. L., "Mathematical Statistics", Springer, ISBN 978-3540045076."
 * @deprecated {@link Cheng1978} is a much better algorithm.
 */
@Deprecated
public class VanDerWaerden1969 implements RandomBetaGenerator {

    private RandomGammaGenerator X, Y;

    /**
     * Construct a random number generator to sample from the beta distribution.
     *
     * @param X a random gamma generator
     * @param Y a random gamma generator
     */
    public VanDerWaerden1969(RandomGammaGenerator X, RandomGammaGenerator Y) {
        this.X = X;
        this.Y = Y;
    }

    /**
     * Construct a random number generator to sample from the beta distribution.
     *
     * @param alpha the gamma distribution parameter, <i>α</i>, for <i>X</i>
     * @param beta  the gamma distribution parameter, <i>β</i>, for <i>X</i>
     */
    public VanDerWaerden1969(double alpha, double beta) {
        this(new XiTanLiu2010a(alpha, new UniformRng()), new XiTanLiu2010a(beta, new UniformRng()));
    }

    @Override
    public void seed(long... seeds) {
        X.seed(seeds);
        Y.seed(seeds);
    }

    @Override
    public double nextDouble() {
        double x = X.nextDouble();
        double y = Y.nextDouble();
        double r = x / (x + y);
        return r;
    }
}
