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
package com.numericalmethod.suanshu.stats.test.distribution.normality;

import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static java.lang.Math.*;

/**
 * D'Agostino's K<sup>2</sup> test is a goodness-of-fit measure of departure from normality.
 *
 * <p>
 * It tests whether or not the given sample comes from a normally distributed population.
 * The test is based on transformations of the sample kurtosis and skewness,
 * and has power only against the alternatives that the distribution is skewed and/or kurtic.
 *
 * <p>
 * The R equivalent function is {@code dagoTest} in {@code fBasics}.
 *
 * @author Haksun Li
 *
 * @see "D'Agostino, Ralph B., Albert Belanger, and Ralph B. D'Agostino, Jr. "A Suggestion for Using Powerful and Informative Tests of Normality", The American Statistician, Vol. 44, No. 4. (Nov., 1990), pp. 316-321."
 */
//TODO: the R function "dagoTest" in package "fBasics" does not seem to be correct; probably b/c they use the biased estimator?
public class DAgostino extends HypothesisTest {//TODO: the formulae on Wikipedia and Wiki Doc appear to be wrong as of 4/8/2010; consult the original paper

    /**
     * test statistics K<sup>2</sup>
     */
    public final double K2;
    /**
     * Z1
     */
    public final double Z1;
    /**
     * Z2
     */
    public final double Z2;
    /**
     * the p-value for Z1
     */
    public final double pvalueZ1;
    /**
     * the p-value for Z2
     */
    public final double pvalueZ2;

    @Override
    public String getNullHypothesis() {
        return "both the skewness and the excess kurtosis are 0";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "either the skewness or the excess kurtosis is non-zero";
    }

    /**
     * Perform D'Agostino's test to test for the departure from normality.
     * 
     * @param sample a sample
     */
    public DAgostino(double[] sample) {
        super(sample);

        Skewness skew = new Skewness(sample);
        Kurtosis kurtosis = new Kurtosis(sample);

        //compute Z1
        /*
         * TODO: the paper is inconsistent about which skewness value to use; either the unbiased or the biased one.
         * The derivation in Section 3 uses the biased estimator but the numerical example in Section 5 use the unbiased indicator.
         */
        double g1 = skew.sample();

        double Y = (N + 1) * (N + 3) / 6. / (N - 2);
        Y = sqrt(Y);
        Y *= g1;

        double b1 = 3. * (N * N + 27 * N - 70) * (N + 1) * (N + 3);//beta
        b1 /= (N - 2) * (N + 5) * (N + 7) * (N + 9);

        double W2 = -1;
        W2 += sqrt(2 * b1 - 2);
        double W = sqrt(W2);
        double d = 1 / sqrt(log(W));
        double a = sqrt(2. / (W2 - 1));

        double z1 = d;
        z1 *= log(Y / a + sqrt(Y * Y / a / a + 1));
        Z1 = z1;

        //compute Z2
        /*
         * TODO: the paper is inconsistent about which skewness value to use; either the unbiased or the biased one.
         * The derivation in Section 3 uses the biased estimator but the numerical example in Section 5 use the unbiased indicator.
         */
        double g2 = kurtosis.sample() + 3;

        double mean = 3. * (N - 1) / (N + 1);
        double var = 24. * N * (N - 2) * (N - 3);
        var /= (N + 1) * (N + 1) * (N + 3) * (N + 5);

        double x = (g2 - mean) / sqrt(var);

        double b2 = 6. * (N * N - 5 * N + 2);//root beta
        b2 /= (N + 7) * (N + 9);
        b2 *= sqrt(6 * (N + 3) * (N + 5));
        b2 /= sqrt(N * (N - 2) * (N - 3));

        double A = 1 + 4 / b2 / b2;
        A = sqrt(A);
        A += 2 / b2;
        A *= 8 / b2;
        A += 6;

        double z2 = 1 - 2. / A;
        z2 /= 1 + x * sqrt(2 / (A - 4));
        z2 = -1 * cbrt(z2);
        z2 += 1 - 2. / 9. / A;
        z2 /= sqrt(2. / 9. / A);
        Z2 = z2;

        //compute the test statistics
        K2 = Z1 * Z1 + Z2 * Z2;
        testStatistics = K2;

        //compute the p-value
        ProbabilityDistribution X2 = new ChiSquareDistribution(2);
        pValue = oneSidedPvalue(X2, testStatistics);

        ProbabilityDistribution pnorm = new NormalDistribution();
        pvalueZ1 = 2 * (1 - pnorm.cdf(abs(Z1)));
        pvalueZ2 = 2 * (1 - pnorm.cdf(abs(Z2)));
    }
}
