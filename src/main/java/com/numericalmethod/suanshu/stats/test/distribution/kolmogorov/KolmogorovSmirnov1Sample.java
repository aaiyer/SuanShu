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

import com.numericalmethod.suanshu.number.DoubleUtils;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.max;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import java.util.Arrays;

/**
 * The one-sample KolmogorovDistribution–Smirnov test compares a sample with a reference probability distribution.
 * This class computes the one-sample KolmogorovDistribution-Smirnov statistic.
 *
 * @author Haksun Li
 *
 * @see "Jean Dickinson Gibbons, Subhabrata Chakraborti. "Nonparametric Statistical Inference", 4th edition. Section 4.3."
 */
public class KolmogorovSmirnov1Sample extends KolmogorovSmirnov {

    /**
     * Construct an one-sample KolmogorovDistribution-Smirnov test.
     *
     * @param sample a sample
     * @param F the reference probability distribution to test against
     * @param side specifying two-sided, one-sided
     *
     * @see "Jean Dickinson Gibbons, Subhabrata Chakraborti. "Nonparametric Statistical Inference", 4th edition. Section 4.3."
     */
    public KolmogorovSmirnov1Sample(double[] sample, ProbabilityDistribution F, Side side) {
        super(new double[][]{sample}, side, Type.ONE_SAMPLE);

        double n = sample.length;

        //check if there are ties
        if (DoubleUtils.hasDuplicate(sample, 0)) {
            this.ties = true;
        } else {
            this.ties = false;
        }

        //compute distance, the D statistics
        double[] sorted = Arrays.copyOf(sample, sample.length);
        Arrays.sort(sorted);

        double[] distance = new double[sorted.length];
        for (int i = 0; i < sorted.length; ++i) {
            distance[i] = F.cdf(sorted[i]) - i / n;
        }

        double D = Double.MIN_VALUE;
        double tmp;
        for (int i = 0; i < distance.length; ++i) {
            tmp = 1 / n - distance[i];
            D = tmp > D ? tmp : D;//taking max
        }
        this.Dnp = D;
        this.Dnn = max(distance);
        this.Dn = max(Dnp, Dnn);

        /*
         * compute p-value
         * use limiting distribution when there are ties
         * (the R convention)
         */
        switch (side) {
            case TWO_SIDED:
                this.testStatistics = Dn;
                this.pValue = oneSidedPvalue(new KolmogorovDistribution((int) n, ties ? 0 : 16000, false), testStatistics);
                break;
            case GREATER:
                this.testStatistics = Dnp;
                this.pValue = oneSidedPvalue(new KolmogorovOneSidedDistribution((int) n, ties ? 0 : 50), testStatistics);
                break;
            case LESS:
                this.testStatistics = Dnn;
                this.pValue = oneSidedPvalue(new KolmogorovOneSidedDistribution((int) n, ties ? 0 : 50), testStatistics);
                break;
            default://keep the complier happy
                throw new RuntimeException("logical error");
        }
    }
}
