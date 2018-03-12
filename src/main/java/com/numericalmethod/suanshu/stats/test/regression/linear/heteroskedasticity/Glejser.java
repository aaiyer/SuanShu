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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.stats.regression.linear.Residuals;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.foreach;

/**
 * The Glejser test is used to test for heteroskedasticity in a linear regression model.
 * It tests whether the estimated variance of the residuals from a regression are dependent on the values of the independent variables (regressors).
 *
 * <p>
 * The Glejser test tests for conditional heteroskedasticity.
 * The test statistics is computed by regressing <em>absolute</em> residuals from the original regression against the original regressors (plus intercept).
 * The test is a chi-squared test: the test statistic distribution is nχ<sup>2</sup> with k degrees of freedom.
 *
 * @author Haksun Li
 */
public class Glejser extends Heteroskedasticity {

    /**
     * Perform the Glejser test to test for heteroskedasticity in a linear regression model.
     *
     * @param residuals the Residuals object from an OLS regression
     */
    public Glejser(Residuals residuals) {
        super(residuals);
    }

    public OLSRegression getAuxiliaryRegression() {
        Vector err = residuals.residuals;
//        final double RSS = Math.pow(err.norm(), 2);
//        final double sigma2 = RSS / n;//variance of disturbances

        Vector y = foreach(err, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.abs(x);
//                return x * x - sigma2;//Breusch-Pagan
            }
        });
        OLSRegression auxiliary = getAuxiliaryOLSRegression(y, residuals);
        return auxiliary;
    }
}
