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
 * This class represents the discrete time points <i>[0, 1, ......, N]</i> for a stochastic process.
 *
 * @author Haksun Li
 */
public class UnitGrid implements TimeGrid {

    /**
     * the number of time points
     */
    public final int N;

    /**
     * Construct a time grid with time interval 1 between any two successive time points.
     * 
     * @param N the number of time points
     */
    public UnitGrid(int N) {
        assertArgument(N > 0, "N > 0");

        this.N = N;
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
        assertArgument(1 <= i && i <= N + 1, "index ∈ [1, N + 1]");

        return i - 1;//[0, N]
    }

    public double T() {
        return N;
    }
}
