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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * Fisher's exact test is a statistical significance test used in the analysis of contingency tables where nextSample sizes are small.
 * It converges to Chi-square distribution for a large and well balanced nextSample.
 *
 * <p>
 * For small, sparse, or unbalanced data,
 * the exact and asymptotic p-values can be quite different and may lead to opposite conclusions concerning the hypothesis of interest.
 * In contrast the Fisher exact test is, as its name states, exact,
 * and it can therefore be used regardless of the nextSample characteristics.
 * 
 * <p>
 * It becomes difficult to calculate with large samples or well-balanced tables,
 * but fortunately these are exactly the conditions where the chi-square distribution is appropriate.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Fisher%27s_exact_test">Wikipedia: Fisher's exact test</a>
 */
public class FisherExactDistribution extends EmpiricalDistribution {

    /**
     * Construct the distribution for the Fisher's exact test.
     * 
     * @param rowSums row totals
     * @param colSums column totals
     * @param nSim number of simulations
     * @param rng a uniform random number generator
     */
    public FisherExactDistribution(int[] rowSums, int[] colSums, int nSim, RandomLongGenerator rng) {
        super(simulation(rowSums, colSums, nSim, rng));
    }

    /**
     * Construct the distribution for the Fisher's exact test.
     * 
     * @param rowSums row totals
     * @param colSums column totals
     * @param nSim number of simulations
     */
    public FisherExactDistribution(int[] rowSums, int[] colSums, int nSim) {
        this(rowSums, colSums, nSim, new UniformRng());
    }

    /**
     * Simulate the statistics.
     * The distribution is then computed empirically.
     *
     * @param rowSums row sums
     * @param colSums column sums
     * @param nSim number of simulations
     * @param rng a uniform random number generator
     * @return a set of statistics
     */
    private static double[] simulation(int[] rowSums, int[] colSums, int nSim, RandomLongGenerator rng) {
        final Matrix E = ChiSquare4Independence.getExpectedContingencyTable(rowSums, colSums);
        final AS159 as159 = new AS159(rowSums, colSums, rng);

        double[] stats = new double[nSim];
        //simulate the test statistics
        for (int b = 0; b < nSim; ++b) {//nSim is B in the R code
            stats[b] = ChiSquare4Independence.pearsonStat(as159.nextSample().A, E, false);
        }
        
        return stats;
    }
}
