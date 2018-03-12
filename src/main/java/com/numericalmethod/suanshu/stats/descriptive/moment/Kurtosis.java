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
package com.numericalmethod.suanshu.stats.descriptive.moment;

import com.numericalmethod.suanshu.stats.descriptive.Statistic;

/**
 * Kurtosis measures the "peakedness" of the probability distribution of a real-valued random variable.
 * Higher kurtosis means that there are more infrequent extreme deviations than frequent modestly sized deviations, hence a fatter tail.
 * This implementation computes the <em>excess</em> kurtosis. That is,
 * <blockquote><i>
 * γ = E[((X - E(X)) / σ)<sup>4</sup>]
 * </i></blockquote>
 * This implementation uses Chan's update formula to incrementally compute the new statistic.
 * <p/>
 * The R equivalent function is {@code kurtosis}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Kurtosis">Wikipedia: Kurtosis</a>
 * <li><a href="http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance">Wikipedia: Algorithms for calculating variance</a>
 * <li>"Tony F, Chan, Gene H, Golub, Randall J, LeVeque, "Updating Formulae and a Pairwise Algorithm for Computing Sample Variances," Technical Report STAN-CS-79-773, Department of Computer Science, Stanford University, 1979."
 * </ul>
 */
public class Kurtosis implements Statistic {

    private Moments moment = new Moments(4);

    /**
     * Construct an empty {@code Kurtosis} calculator.
     */
    public Kurtosis() {
    }

    /**
     * Construct a {@code Kurtosis} calculator,
     * initialized with a sample.
     *
     * @param data a sample
     */
    public Kurtosis(double[] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Kurtosis} calculator
     */
    public Kurtosis(Kurtosis that) {
        this.moment = new Moments(that.moment);
    }

    /**
     * Get the sample kurtosis (biased estimator).
     *
     * @return the sample kurtosis
     */
    public double sample() {
        double m2 = moment.centralMoment(2);
        double m4 = moment.centralMoment(4);
        return m4 / m2 / m2 - 3;
    }

    @Override
    public void addData(double... data) {
        moment.addData(data);
    }

    @Override
    public double value() {
        double var = moment.centralMoment(2) * ((double) N() / (N() - 1));
        return moment.centralMoment(4) / var / var - 3;
    }

    @Override
    public long N() {
        return moment.N();
    }

    @Override
    public String toString() {
        return String.format("kurtosis: %f; N: %d",
                             value(),
                             N());
    }
}
