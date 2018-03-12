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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.sample;

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.AutoCorrelationFunction;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import java.util.ArrayList;

/**
 * This computes the sample partial Auto-Correlation Function (PACF) for a univariate data set.
 *
 * @author Haksun Li
 *
 * @see "William W.S. Wei, "Section 2.5.4. Sample Partial Auto-correlation function" in <i>Time Series Analysis : Univariate and Multivariate Methods (2nd Edition)</i>, Addison Wesley; 2 edition (July 17, 2005)"
 */
public class PartialAutoCorrelation extends AutoCorrelationFunction {

    private final AutoCorrelation acf;
    private ArrayList<ArrayList<Double>> phi = new ArrayList<ArrayList<Double>>();

    public PartialAutoCorrelation(TimeSeries xt, AutoCovariance.Type type) {
        this.acf = new AutoCorrelation(xt, type);
        setPhi(1, 1, this.acf.evaluate(1));//k = 0;
    }

    public PartialAutoCorrelation(TimeSeries xt) {
        this(xt, AutoCovariance.Type.TYPE_I);
    }

    @Override
    public double evaluate(double x1, double x2) {
        return evaluate(Math.round((float) Math.abs(x1 - x2)));
    }

    /**
     * Compute the partial auto-correlation for lag {@code k}.
     *
     * @param lag lag >= 1
     * @return ρ(k)
     */
    public double evaluate(int lag) {
        for (int t = phi.size() + 1; t <= lag; ++t) {
            int k = t - 1;

            double phi_k1_k1 = phi_k1_k1(k);
            setPhi(t, t, phi_k1_k1);

            for (int j = 1; j <= k; ++j) {
                double phi_k1_j = getPhi(k, j) - getPhi(k + 1, k + 1) * getPhi(k, k + 1 - j);//eq. 2.5.26
                setPhi(k + 1, j, phi_k1_j);
            }
        }

        return getPhi(lag, lag);
    }

    private double phi_k1_k1(int k) {//eq. 2.5.25
        double sum1 = 0;
        for (int j = 1; j <= k; ++j) {
            sum1 += getPhi(k, j) * acf.evaluate(k + 1 - j);
        }

        double sum2 = 0;
        for (int j = 1; j <= k; ++j) {
            sum2 += getPhi(k, j) * acf.evaluate(j);
        }

        double result = acf.evaluate(k + 1) - sum1;
        result /= 1 - sum2;

        return result;
    }

    private void setPhi(int i, int j, double rho) {
        if (phi.size() < i) {
            //array initialization
            ArrayList<Double> list = new ArrayList<Double>();
            for (int p = 0; p <= i - 1; ++p) {
                list.add(p, new Double(0));
            }

            phi.add(i - 1, list);
        }

        ArrayList<Double> phi_i = phi.get(i - 1);
        phi_i.set(j - 1, rho);
    }

    private double getPhi(int i, int j) {
        ArrayList<Double> phi_i = phi.get(i - 1);
        return phi_i.get(j - 1);
    }
}
