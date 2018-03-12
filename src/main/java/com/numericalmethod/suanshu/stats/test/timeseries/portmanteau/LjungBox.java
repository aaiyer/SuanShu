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
package com.numericalmethod.suanshu.stats.test.timeseries.portmanteau;

/**
 * The Ljung–Box test (named for Greta M. Ljung and George E. P. Box) is a portmanteau test for autocorrelated errors.
 * A portmanteau test tests whether any of a group of autocorrelations of a time series are different from zero.
 *
 * <p>
 * The Ljung–Box statistic is better for all sample sizes including small ones.
 * In fact, the Ljung–Box statistic was described explicitly in the paper that lead to the use of the Box-Pierce statistic,
 * and from which the statistic takes its name.
 *
 * <p>
 * The R equivalent function is {@code Box.test}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Box%E2%80%93Pierce_test">Wikipedia: Box–Pierce test</a>
 * <li><a href="http://en.wikipedia.org/wiki/Portmanteau_test">Wikipedia: Portmanteau test</a>
 * </ul>
 */
public class LjungBox extends BoxPierce {

    public LjungBox(double[] xt, int lag, int fitdf) {
        super(xt, lag, fitdf);
    }

    /**
     * Compute the Q statistics.
     *
     * @return the Q statistics
     */
    @Override
    double Q(double[] obs, int N) {
        double sum = 0;
        for (int i = 0; i < obs.length; ++i) {
            sum += obs[i] * obs[i] / (N - i - 1);
        }

        return N * (N + 2) * sum;
    }
}
