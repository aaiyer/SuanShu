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
package com.numericalmethod.suanshu.stats.test.distribution.kolmogorov;

import com.numericalmethod.suanshu.misc.R;
import java.util.Arrays;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import static com.numericalmethod.suanshu.misc.R.*;

/**
 * The two-sample Kolmogorov–Smirnov test tests for the equality of the distributions of two samples (two-sample KS test).
 *
 * @author Haksun Li
 */
public class KolmogorovSmirnov2Samples extends KolmogorovSmirnov {

    /**
     * Construct a two-sample Kolmogorov-Smirnov test.
     *
     * @param sample1 a sample
     * @param sample2 another sample
     * @param side specifying two-sided, one-sided
     *
     * @see "Jean Dickinson Gibbons, Subhabrata Chakraborti. "Nonparametric Statistical Inference", 4th edition. Section 6.3."
     */
    public KolmogorovSmirnov2Samples(double[] sample1, double[] sample2, Side side) {
        super(new double[][]{sample1, sample2}, side, Type.TWO_SAMPLE);

        final double n1 = sample1.length;
        final double n2 = sample2.length;

        double[] w = concat(sample1, sample2);
        double[] order = intArray2doubleArray(order(w));

        Arrays.sort(w);
        double[] diff = diff(w);
        int[] steps = which(diff, new R.which() {

            public boolean isTrue(double x, int index) {
                return !equal(0d, x, 0);
            }
        });
        ties = steps.length < n1 + n2 - 1 ? true : false;

        /*
         * from R source code
         *
         * the distance between two empirical cdfs
         *
         * w <- c(x, y)
         * steps = which(diff(sort(w)) != 0)
         * if(length(steps) > 0)
         *     z <- cumsum(ifelse(order(w) <= n.x, 1 / n.x, - 1 / n.y))[steps]
         * else
         *     z <- c(0)
         */
        double[] distance = new double[]{0};
        if (steps.length > 0) {
            double[] ifelse = ifelse(order, new R.ifelse() {

                public boolean test(double x) {
                    return x <= n1;
                }

                public double yes(double arg0) {
                    return 1.0 / n1;
                }

                public double no(double arg0) {
                    return -1.0 / n2;
                }
            });

            double[] cumsum = cumsum(ifelse);
            distance = subarray(cumsum, steps);
        }

        this.Dnp = max(distance);
        this.Dnn = -min(distance);
        this.Dn = max(Dnp, Dnn);

        /*
         * compute p-value
         * use limiting distribution for one-sided alternative
         * (the R convention)
         */
        switch (side) {
            case TWO_SIDED:
                this.testStatistics = Dn;
                this.pValue = oneSidedPvalue(new KolmogorovTwoSamplesDistribution(sample1, sample2, KolmogorovTwoSamplesDistribution.Side.EQUAL), testStatistics);
                break;
            case GREATER:
                this.testStatistics = Dnp;
                this.pValue = oneSidedPvalue(new KolmogorovTwoSamplesDistribution((int) n1, (int) n2, KolmogorovTwoSamplesDistribution.Side.GREATER, 0), testStatistics);
                break;
            case LESS:
                this.testStatistics = Dnn;
                this.pValue = oneSidedPvalue(new KolmogorovTwoSamplesDistribution((int) n1, (int) n2, KolmogorovTwoSamplesDistribution.Side.LESS, 0), testStatistics);
                break;
            default://keep the complier happy
                throw new RuntimeException("logical error");
        }
    }
}
