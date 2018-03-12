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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.pow;

/**
 * Residual analysis of the results of a quasi Generalized Linear Model regression.
 *
 * @author Haksun Li
 */
public class Residuals extends com.numericalmethod.suanshu.stats.regression.linear.glm.Residuals {

    /**
     * Perform the residual analysis for a quasi GLM problem.
     *
     * @param problem the quasi GLM problem to be solved
     * @param fitted the fitted values
     */
    Residuals(QuasiGlmProblem problem, Vector fitted) {
        super(problem, fitted);
    }

    /**
     * Compute the over-dispersion.
     * 
     * @return the over-dispersion
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Section 4.5. Equation 4.23."
     */
    @Override
    public double overdispersion() {
        final QuasiGlmProblem p = (QuasiGlmProblem) problem;
        final int n = problem.nObs();
        final int m = problem.nFactors();

        double sigma2 = 0;
        for (int i = 1; i <= n; i++) {
            double mu_i = fitted.get(i);
            sigma2 = sigma2 + pow(problem.y.get(i) - mu_i, 2) / p.family.variance(mu_i);
        }

        return sigma2 / (n - m);
    }

    @Override
    public double[] deviances() {
        final int nObs = problem.y.size();
        final QuasiGlmProblem p = (QuasiGlmProblem) problem;

        double[] deviances = new double[nObs];
        for (int i = 1; i <= nObs; ++i) {
            deviances[i - 1] = p.quasiFamily.quasiDeviance(problem.y.get(i), fitted.get(i));
        }

        return deviances;
    }
}
