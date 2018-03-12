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
import static java.lang.Math.pow;

/**
 * Skewness is a measure of the asymmetry of the probability distribution.
 * A distribution may either be positively or negatively skewed.
 * For positive skew (or right-skewed), the right tail is longer.
 * The mass of the distribution is concentrated on the left.
 * For negative skew (or left-skewed), the left tail is longer.
 * The mass of the distribution is concentrated on the right.
 * The definition is:
 * <blockquote><i>
 * γ = E[((X - E(X)) / σ)<sup>3</sup>]
 * </i></blockquote>
 * This implementation uses Chan's update formula to incrementally compute the new statistic.
 * <p/>
 * The R equivalent function is {@code skewness}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Skewness">Wikipedia: Skewness</a>
 * <li><a href="http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance">Wikipedia: Algorithms for calculating variance</a>
 * <li>"Chan, Tony F.; Golub, Gene H.; LeVeque, Randall J. (1979), "Updating Formulae and a Pairwise Algorithm for Computing Sample Variances," Technical Report STAN-CS-79-773, Department of Computer Science, Stanford University."
 * </ul>
 */
public class Skewness implements Statistic {

    private long N = 0;
    private Moments moment = new Moments(2);
    private double m3 = 0;//the sum of cubes of differences from the mean

    /**
     * Construct an empty {@code Skewness} calculator.
     */
    public Skewness() {
    }

    /**
     * Construct a {@code Skewness} calculator,
     * initialized with a sample.
     *
     * @param data a sample
     */
    public Skewness(double[] data) {
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code Skewness} calculator
     */
    public Skewness(Skewness that) {
        this.N = that.N;
        this.moment = new Moments(that.moment);
        this.m3 = that.m3;
    }

    /**
     * Get the sample skewness (biased estimator).
     *
     * @return the sample skewness
     */
    public double sample() {
        double m2 = moment.centralMoment(2);
        double m3 = this.m3 / N;//the local m3 is the central moment
        return m3 / pow(m2, 1.5);
    }

    /**
     * Get the sample mean.
     *
     * @return the mean
     */
    private double mean() {
        return moment.centralMoment(1);
    }

    @Override
    public void addData(double... data) {
        //base case
        if (N == 0) {//store the datum as the mean of 'var'
            N = data.length;
            moment.addData(data);
            m3 = Moments.sumsOfPowersOfDifferences(3, moment.centralMoment(1), data);//sum of cubes
            return;
        }

        /*
         * Chan's update formula.
         * Chan, Tony F.; Golub, Gene H.; LeVeque, Randall J. (1979),
         * "Updating Formulae and a Pairwise Algorithm for Computing Sample Variances.",
         * Technical Report STAN-CS-79-773,
         * Department of Computer Science, Stanford University.
         */
        Skewness that = new Skewness(data);
        double delta = that.mean() - this.mean();//mean difference
        double M2this = this.M2();//this sum of squares of differences
        double M2that = that.M2();//that sum of squares of differences
        long Nboth = this.N + that.N;

        double term1 = 3 * delta * (this.N * M2that - that.N * M2this) / Nboth;

//        double term2 = delta * delta * delta * (this.N * that.N * (this.N - that.N)) / Nboth / Nboth;//This statement does not work!!! Guess why?
        double term2 = (double) this.N / Nboth;
        term2 *= delta * that.N;
        term2 = term2 * term2 * term2;
        term2 *= 1d / that.N / that.N - 1d / this.N / this.N;

        m3 += that.m3 + term1 + term2;

        //update states
        moment.addData(data);
        N = Nboth;
    }

    private double M2() {
        return moment.centralMoment(2) * moment.N();
    }

    @Override
    public double value() {
        double var = M2() / (N - 1);
        return N >= 2 ? m3 / N / Math.pow(var, 1.5) : 0;
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        return String.format("skew: %f; N: %d",
                             value(),
                             N);
    }
}
