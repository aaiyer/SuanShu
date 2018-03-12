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
package com.numericalmethod.suanshu.stats.regression.linear.ols;

import static java.lang.Math.log;

/**
 * The information criteria measure the goodness of fit of an estimated statistical model.
 * The information criteria (IC) are not tests of the model in the sense of hypothesis testing;
 * rather they are tests between models - a tool for model selection.
 * Given a data set, several competing models may be ranked according to their IC,
 * with the one having the lowest IC being the best.
 * From the IC value one may infer that e.g. the top three models are in a tie and the rest are far worse,
 * but it would be arbitrary to assign a value above which a given model is 'rejected'.
 *
 * <p>
 * Akaike's information criterion is a measure of the goodness of fit of an estimated statistical model.
 * It is grounded in the concept of entropy,
 * in effect offering a relative measure of the information lost when a given model is used to describe reality and can be said to describe the tradeoff between bias and variance in model construction,
 * or loosely speaking that of accuracy and complexity of the model.
 *
 * <p>
 * The BIC is very closely related to the Akaike information criterion (AIC).
 * In BIC, the penalty for additional parameters is stronger than that of the AIC.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Akaike_information_criterion">Wikipedia: Akaike information criterion</a>
 * <li><a href="http://en.wikipedia.org/wiki/Bayesian_information_criterion">Wikipedia: Bayesian information criterion</a>
 * </ul>
 */
public class InformationCriteria {

    /**
     * Akaike information criterion
     *
     * @see <a href="http://en.wikipedia.org/wiki/Akaike_information_criterion">Wikipedia: Akaike information criterion</a>
     */
    public final double AIC;
    /**
     * Bayesian information criterion
     *
     * @see <a href="http://en.wikipedia.org/wiki/Bayesian_information_criterion">Wikipedia: Bayesian information criterion</a>
     */
    public final double BIC;

    /**
     * Compute the information criteria from residual analysis.
     * 
     * @param residuals the residual analysis of a linear regression problem
     */
    InformationCriteria(Residuals residuals) {
        final int n = residuals.problem.nObs();
        final int m = residuals.problem.nFactors();

        //sum of log-weights
        double sumLogWeights = 0;
        if (residuals.problem.weights != null) {
            for (int i = 0; i < n; ++i) {
                sumLogWeights += log(residuals.problem.weights.get(i + 1));
            }
        }

        /*
         * The formula is given in the description of the command "extractAIC" in R.
         * Type "help(extractAIC)" in R.
         * Formula: 2 * (n+1) + n log (RSS/n) + n log 2π - n - sum log w, where w are the weights.
         */
        AIC = 2 * (m + 1) + n * log(2 * Math.PI * residuals.RSS / n) + n - sumLogWeights;

        /*
         * Schwarz' BC
         *
         * Estimating the dimension of a model.
         * Schwarz, G. (1978).
         * Annals of Statistics, 6, 461–464.
         *
         * c.f.,
         * eq. 10.8
         * Applied Linear Regression
         * 3rd edition
         * Sanford Weisberg
         * John Wiley & Sons
         */
        BIC = log(n) * (m + 1) + n * log(2 * Math.PI * residuals.RSS / n) + n - sumLogWeights;
    }
}
