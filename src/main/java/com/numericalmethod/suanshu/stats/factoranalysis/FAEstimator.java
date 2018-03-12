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
package com.numericalmethod.suanshu.stats.factoranalysis;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * These are the estimators
 * (estimated psi, loading matrix, scores, degrees of freedom, test statistics, p-value, etc.)
 * from the factor analysis MLE optimization.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li> M. S. Bartlett, "The Statistical Conception of Mental Factors," The British Journal of Psychology, vol. 28, 97–104, 1937.
 * <li> M. S. Bartlett, "A Note on Multiplying Factors for Various Chi-Squared Approximations," Journal of the Royal Statistical Society, Series B, vol. 16, 296–298, 1954.
 * <li> D. N. Lawley and A. E. Maxwell, "Factor Analysis as a Statistical Method," Second Edition, Butterworths, 1971.
 * <li> G. H. Thomson, "The Factorial Analysis of Human Ability," London University Press, 1951.
 * <li><a href="http://en.wikipedia.org/wiki/Factor_analysis">Wikipedia: Factor analysis</a>
 * </ul>
 */
public class FAEstimator extends HypothesisTest {

    private final int nObs;
    private final ImmutableVector psi;
    private final ImmutableMatrix loadings;
    private final double logLikelihood;
    private final int dof;
    private final ImmutableMatrix scores;

    /**
     * Construct the factor analysis MLE estimators.
     *
     * @param nObs          the sample size, i.e. <i>N</i> in Lawley and Maxwell (1971)
     * @param psi           the estimated (optimal) psi
     * @param loadings      This is the loading matrix, one column for each factor.
     * The factors are ordered in decreasing order of sums of squares of loadings,
     * The loadings are given the sign such that the sum of each loading is positive.
     * @param logLikelihood the log-likelihood value, f<sub>k</sub>
     * @param dof           the degree of freedom in the factor analysis model
     * @param scores        the matrix of scores, computed using either Thompson's (1951) scores or Bartlett's (1937) weighted least-squares scores
     */
    FAEstimator(int nObs, Vector psi, Matrix loadings, double logLikelihood, int dof, Matrix scores) {
        this.nObs = nObs;
        this.psi = new ImmutableVector(psi);
        this.loadings = new ImmutableMatrix(loadings);
        this.logLikelihood = logLikelihood;
        this.dof = dof;
        this.scores = new ImmutableMatrix(scores);
    }

    /**
     * Get the estimated (optimal) psi, <i>E(ee')</i>, p. 6.
     *
     * @return the psi vector
     */
    public ImmutableVector psi() {
        return psi;
    }

    /**
     * Get the rotated loading matrix.
     *
     * @return the rotated matrix of loadings
     */
    public ImmutableMatrix loadings() {
        return loadings;
    }

    /**
     * Get the degree of freedom in the factor analysis model.
     *
     * @return the degree of freedom
     */
    public int dof() {
        return dof;
    }

    /**
     * Get the log-likelihood value.
     *
     * @return the log-likelihood
     */
    public double logLikelihood() {
        return logLikelihood;
    }

    /**
     * Get the matrix of scores, computed using either Thompson's (1951) scores,
     * or Bartlett's (1937) weighted least-squares scores.
     *
     * @return the matrix of scores
     */
    public ImmutableMatrix scores() {
        return scores;
    }

    @Override
    public String getNullHypothesis() {
        return String.format("There are at least %d factors.", loadings.nCols());
    }

    @Override
    public String getAlternativeHypothesis() {
        return String.format("There are less than %d factors.", loadings.nCols());
    }

    /**
     * Get the test statistics of the factor analysis. Bartlett (1954) has shown
     * that the chi-squared approximation to the distribution can be improved by
     * using a multiplying factor of <i>(N - 1) - (2p + 4k + 5) / 6</i>, which is the
     * same multiplying factor used here and often used in empirical studies. N.B.
     * the same multiplying factor is used in Bartlett's test of sphericity.
     *
     * @return the test statistics
     */
    @Override
    public double statistics() {
        final int nVariables = loadings.nRows(); //the number of variables in the original data
        final int nFactors = loadings.nCols(); //the number of factors used in factor analysis

        double stat = nObs; //equation (4.30) on p.36 of Lawley and Maxwell (1971)
        stat -= 1.; //n = sample size - 1 = N - 1; see p.25 of Lawley and Maxwell (1971)
        stat -= (2. * nVariables + 5.) / 6.;
        stat -= (2. * nFactors) / 3.;
        stat *= logLikelihood;
        return stat; //result = [n - (2 * p + 5) / 6 - 2 * k / 3] * f_k
    }

    /**
     * Calculate the p-value of the test statistics, given the degree of freedom.
     *
     * @return the p-value
     */
    @Override
    public double pValue() {
        testStatistics = statistics();

        if (dof <= 0) { //if dof <= 0, return -1
            return -1.0;
        }

        ChiSquareDistribution chi = new ChiSquareDistribution(dof);
        return 1. - chi.cdf(testStatistics); //right test, so the p-value = 1 - CDF(testStatistics)
    }
}
