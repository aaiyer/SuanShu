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

import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.OLSSolver;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * (Weighted) Ordinary Least Squares (OLS) is a method for fitting a linear regression model.
 * This method minimizes the (weighted) sum of squared distances between the observed responses in the dataset,
 * and the responses predicted by the linear approximation.
 *
 * <p>
 * The OLS estimator is consistent when the regressors are exogenous and there is no multicollinearity,
 * and optimal in the class of linear unbiased estimators when the errors are homoscedastic and serially uncorrelated.
 * OLS can be derived as a maximum likelihood estimator under the assumption that the errors are normally distributed,
 * however the method has good statistical properties for a much broader class of distributions (except for efficiency).
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Linear_regression">Wikipedia: Linear regression</a>
 * <li><a href="http://en.wikipedia.org/wiki/Least_squares">Wikipedia: Least squares</a>
 * <li><a href="http://en.wikipedia.org/wiki/Linear_least_squares">Wikipedia: Ordinary least squares</a>
 * <ul>
 */
public class OLSRegression {

    /**
     * the ordinary linear regression problem to be solved
     */
    public final LMProblem problem;
    /**
     * the \(\hat{\beta}\) statistics
     */
    public final Beta beta;
    /**
     * the residual analysis of this OLS regression
     */
    public final Residuals residuals;
    /**
     * the diagnostic measures of this linear regression
     */
    public final Diagnostics diagnostics;
    /**
     * the model selection criteria
     */
    public final InformationCriteria informationCriteria;

    /**
     * Construct an <tt>OLSRegression</tt> instance.
     *
     * @param problem the linear regression problem to be solved
     */
    public OLSRegression(LMProblem problem) {
        this.problem = problem;

        //fitting an OLS regression
        OLSSolver ols = new OLSSolver(0);
        Vector betaHat = ols.solve(new LSProblem(problem.wA, problem.wy));

        residuals = new Residuals(problem, betaHat);

        beta = new Beta(betaHat, residuals);

        diagnostics = new Diagnostics(residuals);

        informationCriteria = new InformationCriteria(residuals);
    }
}
