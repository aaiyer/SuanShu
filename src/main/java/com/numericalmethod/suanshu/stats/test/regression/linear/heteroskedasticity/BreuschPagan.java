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

import com.numericalmethod.suanshu.stats.regression.linear.Residuals;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.pow;

/**
 * The Breusch–Pagan test is used to test for heteroskedasticity in a linear regression model.
 * It tests whether the estimated variance of the residuals from a regression are dependent on the values of the independent variables (regressors).
 *
 * <p>
 * The Breusch–Pagan test tests for conditional heteroskedasticity.
 * The test statistics is computed by regressing </em>squared</em> residuals from the original regression against the original regressors (plus intercept).
 * The test is a chi-squared test: the test statistic distribution is nχ<sup>2</sup> with k degrees of freedom.
 * If the Breush–Pagan test shows that there is conditional heteroscedasticity,
 * it can be corrected by using the Hansen method, using robust standard errors, or re-thinking the regression equation.
 *
 * <p>
 * The R equivalent function is {@code bptest}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li>T. S. Breusch and A. R. Pagan. "A simple test for heteroscedasticity and random coefficient variation," Econometrica 47 (5): 1287–1294, 1979.
 * <li>R. Koenker. "A Note on Studentizing a Test for Heteroscedasticity," Journal of Econometrics 17, 107–112. 1981.
 * <li><a href="http://en.wikipedia.org/wiki/Breusch-Pagan_test">Wikipedia: Breusch–Pagan test</a>
 * </ul>
 */
public class BreuschPagan extends Heteroskedasticity {

    private final boolean studentized;

    /**
     * Perform the Breusch-Pagan test to test for heteroskedasticity in a linear regression model.
     *
     * @param residuals the Residuals object from an OLS regression
     * @param studentized {@code true} if to use the Koenker's studentized version of the test statistic
     */
    public BreuschPagan(Residuals residuals, boolean studentized) {
        super(residuals);
        this.studentized = studentized;
    }

    @Override
    public OLSRegression getAuxiliaryRegression() {
        Vector err = residuals.residuals;
        final double RSS = pow(err.norm(), 2);
        final double sigma2 = RSS / n;//variance of disturbances

        Vector y = studentized ? err.pow(2).minus(sigma2)
                : err.pow(2).scaled(1 / sigma2).minus(1);

        OLSRegression auxiliary = getAuxiliaryOLSRegression(y, residuals);

        return auxiliary;
    }

    @Override
    public double statistics() {
        OLSRegression auxiliary = getAuxiliaryRegression();

        return studentized ? n * auxiliary.residuals.R2//studentized Breusch-Pagan statistic
                : 0.5 * auxiliary.residuals.fitted.innerProduct(auxiliary.residuals.fitted);//Breusch-Pagan statistic
    }
}
