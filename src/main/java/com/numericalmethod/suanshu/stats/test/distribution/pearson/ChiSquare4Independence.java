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
package com.numericalmethod.suanshu.stats.test.distribution.pearson;

import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.sum;

/**
 * Pearson's chi-square test of independence assesses whether paired observations on two variables,
 * expressed in a contingency table, are independent of each other.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Pearson%27s_chi-square_test">Wikipedia: Pearson's chi-square test</a>
 */
public class ChiSquare4Independence extends HypothesisTest {

    /**
     * the distribution used for the test
     */
    public static enum Type {

        /**
         * the distribution for Fisher's exact test; appropriate when the sample size is small and contingency table unbalanced
         */
        EXACT,
        /**
         * default: the asymptotic distribution of the Fisher's exact test distribution; appropriate for a large and well balanced sample
         */
        ASYMPTOTIC_CHI_SQUARE
    }

    @Override
    public String getNullHypothesis() {
        return "the two random variables in the contingency table are independent";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the two random variables in the contingency table are not independent";
    }

    /**
     * Assess whether the two random variable in the contingency table is independent.
     *
     * @param sample a contingency table
     * @param nSim   number of simulation when EXACT distribution is used
     * @param type   the type of distribution
     */
    public ChiSquare4Independence(Matrix sample, int nSim, Type type) {//TODO: type safety; ensure XY entries are integers
        super(MatrixUtils.to1DArray(sample));

        int[] rowSums = MatrixUtils.rowSums(sample);
        int[] colSums = MatrixUtils.colSums(sample);
        Matrix E = getExpectedContingencyTable(rowSums, colSums);
        testStatistics = pearsonStat(sample, E, type == Type.ASYMPTOTIC_CHI_SQUARE);

        int df = (sample.nRows() - 1) * (sample.nCols() - 1);
        ProbabilityDistribution X2 = new ChiSquareDistribution(df);//default
        if (type == Type.EXACT) {
            X2 = new FisherExactDistribution(rowSums, colSums, nSim);//R uses nSim = B = 2000
        }

        pValue = oneSidedPvalue(X2, testStatistics);

//        pValue = 1.0 - X2.cdf(statistics - 0.01);//TODO!!!! something w/ the caling 1 -cdf
    }

    /**
     * Assess whether the two random variable in the contingency table is independent.
     *
     * @param sample a contingency table
     */
    public ChiSquare4Independence(Matrix sample) {
        this(sample, 0, Type.ASYMPTOTIC_CHI_SQUARE);
    }

    /**
     * Assume the null hypothesis of independence, we compute the expected frequency of each category.
     *
     * @param rowSums row totals
     * @param colSums column totals
     * @return a table of expected frequency under the null hypothesis
     */
    public static Matrix getExpectedContingencyTable(int[] rowSums, int[] colSums) {
        final int nRows = rowSums.length;
        final int nCols = colSums.length;

        int total = sum(rowSums);

        DenseMatrix E = new DenseMatrix(nRows, nCols);//the expected frequency matrix under the null hypothesis of independence
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                E.set(i, j, rowSums[i - 1] * colSums[j - 1] / (double) total);
            }
        }

        return E;
    }

    /**
     * Compute the Pearson's cumulative test statistic, which asymptotically approaches a χ2 distribution.
     *
     * @param O                         the observation matrix
     * @param E                         the expectation matrix
     * @param YatesContinuityCorrection {@code true} if to minus 0.5 for each observation in the test statistics
     * @return the Pearson's cumulative test statistic
     */
    public static double pearsonStat(Matrix O, Matrix E, boolean YatesContinuityCorrection) {
        final int nRows = O.nRows();
        final int nCols = O.nCols();

        double YATES = YatesContinuityCorrection ? 0.5 : 0.0;

        double stats = 0;
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                double o = O.get(i, j);
                double e = E.get(i, j);

                double d = Math.abs(o - e) - YATES;
                stats += d * d / e;
            }
        }

        return stats;
    }
}
