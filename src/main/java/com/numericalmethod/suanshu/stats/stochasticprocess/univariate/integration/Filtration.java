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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class represents the filtration information known at the end of time.
 * It contains all histories about a simulation.
 *
 * @author Haksun Li
 */
public class Filtration {

    /**
     * the time grid
     */
    private double[] T;
    /**
     * the time increments
     */
    private double[] dt;
    /**
     * the simulated Brownian path
     */
    private double[] B;
    /**
     * the Brownian increments
     */
    private double[] dB;

    /**
     * Construct a <tt>Filtration</tt> from a Brownian path.
     * 
     * @param Bt a Brownian path
     */
    public Filtration(TimeSeries<Double, ? extends TimeSeries.Entry<Double>> Bt) {
        int n = Bt.size();
        this.T = new double[n];
        this.B = new double[n];

        int i = 0;
        for (Iterator<? extends TimeSeries.Entry<Double>> it = Bt.iterator(); it.hasNext(); ++i) {
            TimeSeries.Entry<Double> entry = it.next();
            this.T[i] = entry.getTime();
            this.B[i] = entry.getValue();
        }

        this.dB = R.diff(this.B);
        this.dt = R.diff(this.T);
    }

    /**
     * Get the length of the history, excluding the initial value (0).
     * 
     * @return the length of the Brownian path - 1
     */
    public int size() {
        return dB.length;
    }

    /**
     * Get the Brownian motion value at time t.
     *
     * <p>
     * B(0) = 0
     *
     * @param t time, counting from 0
     * @return Bt the Brownian motion value at time t
     */
    public double B(int t) {
        return B[t];
    }

    /**
     * Get the entire Brownian path.
     *
     * <p>
     * B[0] = 0
     * 
     * @return the entire Brownian path
     */
    public double[] Bt() {
        return Arrays.copyOf(B, dB.length);
    }

    /**
     * Get the Brownian increment at the t-th time grid point.
     *
     * <p>
     * dB[0] = B(1) - B(0) = B(1)
     *
     * @param t time, counting from 0
     * @return dBt
     */
    public double dB(int t) {
        return dB[t];
    }

    /**
     * Get all the Brownian increments.
     * 
     * @return the Brownian increments
     */
    public double[] dBt() {
        return Arrays.copyOf(dB, dB.length);
    }

    /**
     * Get the t-th time point.
     * 
     * @param t the time index
     * @return the t-th time point
     */
    public double T(int t) {
        return T[t];
    }

    /**
     * Get the entire time grid.
     * 
     * @return the times
     */
    public double[] T() {
        return Arrays.copyOf(T, T.length);
    }

    /**
     * Get the t-th time increment.
     *
     * <p>
     * dt[0] = t[1] - t[0] = t[1] - t0
     *
     * @param t time, counting from 0
     * @return dt the t-th time increment
     */
    public double dt(int t) {
        return dt[t];
    }

    /**
     * Get all the time increments.
     * 
     * @return the time increments
     */
    public double[] dt() {
        return Arrays.copyOf(dt, dt.length);
    }
}
