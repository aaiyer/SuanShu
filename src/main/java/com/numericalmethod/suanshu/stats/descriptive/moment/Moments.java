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

import static com.numericalmethod.suanshu.analysis.function.FunctionOps.combination;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.descriptive.Statistic;
import static java.lang.Math.pow;
import java.util.Arrays;

/**
 * Compute the central moment of a data set <em>incrementally</em>.
 * The <i>n</i>-th moment is the expected value of the <i>n</i>-th power of the differences from the mean.
 * That is,
 * <blockquote><i>
 * μ<sub>k</sub> = E[(X - E(X))<sup>k</sup>]
 * </i></blockquote>
 * This implementation uses Pébay's update formula to incrementally compute the new statistic.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Pébay, Philippe, "Formulas for Robust, One-Pass Parallel Computation of Covariances and Arbitrary-Order Statistical Moments," Technical Report SAND2008-6212, Sandia National Laboratories, 2008."
 * <li><a href="http://en.wikipedia.org/wiki/Moment_%28mathematics%29">Wikipedia: Moment (mathematics)</a>
 * </ul>
 */
public class Moments implements Statistic {

    /** the highest moment to compute */
    private final int order;
    private long N = 0;
    private double[] m;//sum of powers of differences

    /**
     * Construct an empty moment calculator, computing all moments up to and including the {@code order}-th moment.
     *
     * @param order the number of the highest moment
     */
    public Moments(int order) {
        this.order = order;
        m = new double[order];
    }

    /**
     * Construct a moment calculator, computing all moments up to and including the {@code order}-th moment.
     * initialized with a sample.
     *
     * @param order the order of the highest moment
     * @param data  a sample
     */
    public Moments(int order, double... data) {
        this(order);
        addData(data);
    }

    /**
     * Copy constructor.
     *
     * @param that a moment calculator
     */
    public Moments(Moments that) {
        this.order = that.order;
        this.N = that.N;
        this.m = Arrays.copyOf(that.m, that.m.length);
    }

    /**
     * Get the value of the <i>k</i>-th central moment.
     * This method can be used to compute the lower moments.
     * For example,
     * {@code centralMoment(1)} is the mean.
     * Note that higher central moments do not correspond to variance, skew, kurtosis, etc.
     *
     * @param k the order of the moment
     * @return the value of the <i>k</i>-th central moment
     */
    public double centralMoment(int k) {
        SuanShuUtils.assertArgument(k <= order, "only up to the %d moment are available", order);

        double result = m[k - 1] / (double) N;
        return result;
    }

    @Override
    public void addData(double... data) {
        //base case
        if (N == 0) {
            N = data.length;
            m[0] = Moments.sumsOfPowersOfDifferences(1, 0, data);//sum
            double mean = m[0] / N;
            for (int i = 2; i <= order; ++i) {//the i-th moment
                m[i - 1] = Moments.sumsOfPowersOfDifferences(i, mean, data);//sum of squares
            }
            return;
        }

        /*
         * Pébay's update formula.
         * <pre>
         * Pébay, Philippe (2008),
         * "Formulas for Robust, One-Pass Parallel Computation of Covariances and Arbitrary-Order Statistical Moments",
         * Technical Report SAND2008-6212,
         * Sandia National Laboratories.
         * </pre>
         */
        Moments A = new Moments(this);
        Moments B = new Moments(order, data);
        long Nboth = A.N + B.N;
        double delta = B.m[0] / B.N - A.m[0] / A.N;//B.mean - A.mean
        this.m[0] = A.m[0] + B.m[0];//update for p == 1

        for (int p = 2; p <= order; ++p) {//for the p-th moment
            double sum = 0;
            for (int k = 1; k <= p - 2; ++k) {
                int i = p - k;//i-th moment
                double term1 = pow((double) -B.N / Nboth, k) * A.m[i - 1];
                term1 += pow((double) A.N / Nboth, k) * B.m[i - 1];
                term1 *= pow(delta, k);
                term1 *= combination(p, k);

                sum += term1;
            }

            double term2 = (double) A.N / Nboth;
            term2 *= delta * B.N;
            term2 = pow(term2, p);
            term2 *= 1d / pow(B.N, p - 1) - pow(-1d / A.N, p - 1);

            this.m[p - 1] = A.m[p - 1] + B.m[p - 1] + sum + term2;
        }

        N = Nboth;
    }

    @Override
    public double value() {
        return centralMoment(order);
    }

    @Override
    public long N() {
        return N;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.length; ++i) {
            sb.append(String.format("m[%d]: %f; ", i, m[i]));
        }
        return sb.toString();
    }

    /**
     * Compute the {@code power}-th moment of an array of {@code data} with respect to a {@code mean}.
     *
     * @param power the power to raise the difference to
     * @param mean  the reference/center of the data, e.g., 0 or the mean
     * @param data  the data array
     * @return the {@code power}-th moment of an array of {@code data} with respect to a {@code mean}
     */
    public static double sumsOfPowersOfDifferences(int power, double mean, double... data) {
        double sum = 0;
        for (double d : data) {
            double delta = d - mean;
            sum += power == 1 ? delta : Math.pow(delta, power);
        }

        return sum;
    }
}
