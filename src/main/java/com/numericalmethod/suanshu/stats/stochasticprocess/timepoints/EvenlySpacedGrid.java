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
package com.numericalmethod.suanshu.stats.stochasticprocess.timepoints;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;

/**
 * This class represents an evenly spaced/discretized time grid for a stochastic process.
 *
 * @author Haksun Li
 */
public class EvenlySpacedGrid implements TimeGrid {

    /**
     * the number of time points
     */
    public final int N;
    /**
     * the beginning of the time interval
     */
    public final double T0;
    /**
     * the ending of the time interval
     */
    public final double T;
    /**
     * the duration between any two successive time points
     */
    private final double dt;

    /**
     * Construct an evenly spaced/discretized time grid.
     *
     * @param T0 the beginning of the time interval
     * @param T the ending of the time interval
     * @param N the number of time points
     */
    public EvenlySpacedGrid(double T0, double T, int N) {
        assertArgument(T0 < T, "T0 < T");

        assertArgument(N > 1, "N < 1");

        this.N = N;
        this.T0 = T0;
        this.T = T;
        this.dt = (T - T0) / N;
    }

    public int size() {
        return N + 1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * {@code i} ∈ [1, N + 1].
     *
     * @param i the index
     * @return the {@code i}-th time
     */
    public double t(int i) {
        return T0 + (i - 1) * dt;
    }

    public double T() {
        return T;
    }
}
