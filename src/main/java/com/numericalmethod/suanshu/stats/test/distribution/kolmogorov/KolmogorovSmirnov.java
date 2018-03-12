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

import com.numericalmethod.suanshu.stats.test.HypothesisTest;

/**
 * The Kolmogorov–Smirnov test (KS test) is used to compare a sample with a reference probability distribution (one-sample KS test),
 * or to compare two samples (two-sample KS test).
 *
 * <p>
 * The Kolmogorov–Smirnov statistic quantifies a distance between the empirical distribution function of the sample
 * and the cumulative distribution function of the reference distribution (one-sample KS test),
 * or between the empirical distribution functions of two samples (two-sample KS test).
 *
 * <p>
 * The null distribution of this statistic is calculated under the null hypothesis
 * that the sample is drawn from the reference distribution (in the one-sample case), or
 * that the samples are drawn from the same distribution (in the two-sample case)
 *
 * <p>
 * In each case, the distributions considered under the null hypothesis are continuous distributions but are otherwise unrestricted.
 *
 * <p>
 * The test-statistics is either D<sub>n</sub>, D<sub>n</sub><sup>+</sup>, or D<sub>n</sub><sup>-</sup>,
 * depending on the side to compute
 *
 * <p>
 * By R convention, when exact p-values are not available, we use limiting distribution.
 * Exact p-values are not available in the case of ties for one-sample case.
 *
 * <p>
 * The R equivalent function is {@code ks.test}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li>Nonparametric Statistical Inference. 4th edition. Jean Dickinson Gibbons, Subhabrata Chakraborti. CRC.
 * <li><a href="http://en.wikipedia.org/wiki/Kolmogorov%E2%80%93Smirnov_test">Wikipedia: Kolmogorov–Smirnov test</a>
 * </ul>
 */
/*
 * TODO: ties for one-sample case
 *
 * Ties within and across samples can be handled by considering only the
 * r distinct ordered observations in the combined sample as values of x
 * in computing Sm(x) and Sn(x) for r<=m and r<=n. Then we find the
 * empirical cdf for each different x and their differences at these
 * observations and calculate the statistic in the usual way.
 */
public class KolmogorovSmirnov extends HypothesisTest {//TODO: confidence interval

    /**
     * the types of Kolmogorov-Smirnov tests available
     */
    public static enum Type {

        /**
         * One-sample Kolmogorov-Smirnov test
         */
        ONE_SAMPLE,
        /**
         * Two-sample Kolmogorov-Smirnov test
         */
        TWO_SAMPLE
    }

    /**
     * the type of Kolmogorov-Smirnov statistic available
     */
    public static enum Side {

        /**
         * compute D<sub>n</sub>
         */
        TWO_SIDED,
        /**
         * compute D<sub>n</sub><sup>+</sup>;
         * check whether the CDF of a sample lies above the null hypothesis
         */
        GREATER,
        /**
         * compute D<sub>n</sub><sup>-</sup>;
         * check whether the CDF of a sample lies below the null hypothesis
         */
        LESS
    }
    /**
     * the type of Kolmogorov-Smirnov test to be performed
     */
    public final Type type;
    /**
     * the type of Kolmogorov-Smirnov statistic to be computed
     */
    public final Side side;
    /**
     * D<sub>n</sub>
     */
    double Dn;
    /**
     * D<sub>n</sub><sup>+</sup>
     */
    double Dnp;
    /**
     * D<sub>n</sub><sup>-</sup>
     */
    double Dnn;
    /**
     * Indicate whether there are ties in the sample.
     * If yes, the p-value computed is not accurate.
     */
    boolean ties;

    @Override
    public String getNullHypothesis() {
        String hypothesis = "the true distribution function of sample is ";

        switch (side) {
            case TWO_SIDED:
                hypothesis = hypothesis.concat("equal to ");
                break;
            case GREATER:
                hypothesis = hypothesis.concat("greater than ");
                break;
            case LESS:
                hypothesis = hypothesis.concat("less than ");
                break;
            default:
                throw new RuntimeException("not reachable");//make complier happy
        }

        switch (type) {
            case ONE_SAMPLE:
                hypothesis = hypothesis.concat("the hypothesized distribution function");
                break;
            case TWO_SAMPLE:
                hypothesis = hypothesis.concat("the distribution function of the other sample");
                break;
        }

        return hypothesis;
    }

    @Override
    public String getAlternativeHypothesis() {
        throw new UnsupportedOperationException("Not yet supported.");
    }

    /**
     * Construct an instance of the Kolmogorov-Smirnov test.
     * 
     * @param samples two samples
     * @param side indicate whether this is a one (less or greater) or two (equal) sided test
     * @param type indicate whether it is a one or two sample test
     */
    KolmogorovSmirnov(double[][] samples, Side side, Type type) {
        super(samples);

        this.side = side;
        this.type = type;
    }
}
