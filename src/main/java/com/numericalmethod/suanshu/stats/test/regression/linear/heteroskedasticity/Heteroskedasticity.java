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
package com.numericalmethod.suanshu.stats.test.regression.linear.heteroskedasticity;

import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.Residuals;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A heteroskedasticity test is used to test for heteroskedasticity in a linear regression model.
 * It tests whether the estimated variance of the residuals from a regression are dependent on the values of the independent variables (regressors).
 *
 * The test statistics is computed by regressing transformed residuals from the original regression against the original regressors (plus intercept).
 * The test distribution is a Chi-squared distribution.
 *
 * @author Haksun Li
 */
abstract class Heteroskedasticity extends HypothesisTest {

    protected abstract OLSRegression getAuxiliaryRegression();
    protected final Residuals residuals;
    /**
     * number of observations
     */
    protected final int n;

    @Override
    public String getNullHypothesis() {
        return "there is homoskedasticity";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "there is conditional heteroscedasticity";
    }

    Heteroskedasticity(Residuals residuals) {
        this.residuals = residuals;
        this.n = residuals.problem.nObs();
    }

    @Override
    public double statistics() {
        OLSRegression auxiliary = getAuxiliaryRegression();
        return n * auxiliary.residuals.R2;
    }

    @Override
    public double pValue() {
        return oneSidedPvalue(getX2(), statistics());
    }

    protected ChiSquareDistribution getX2() {
        return new ChiSquareDistribution(residuals.problem.nExogenousFactors());
    }

    /**
     * the auxiliary regression
     *
     * @return the auxiliary regression
     */
    protected OLSRegression getAuxiliaryOLSRegression(Vector y, Residuals residuals) {
        LMProblem problem = new LMProblem(y, residuals.problem.X(), true);
        OLSRegression auxiliary = new OLSRegression(problem);
        return auxiliary;
    }
}
