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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.sum;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Residual analysis of the results of an Ordinary Least Square linear regression model.
 *
 * <p>
 * Once a regression model has been constructed,
 * it may be important to confirm the goodness of fit of the model and the statistical significance of the estimated parameters.
 * Commonly used checks of goodness of fit include the R-squared, analysis of the pattern of residuals and hypothesis testing.
 * Statistical significance can be checked by an F-test of the overall fit, followed by t-tests of individual parameters.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Regression_analysis#Regression_diagnostics">Wikipedia: Regression diagnostics</a>
 * <li><a href="http://en.wikipedia.org/wiki/R_square">Wikipedia: Coefficient of determination</a>
 * <li><a href="http://en.wikipedia.org/wiki/Residual_sum_of_squares">Wikipedia: Residual sum of squares</a>
 * <li><a href="http://en.wikipedia.org/wiki/Explained_sum_of_squares">Wikipedia: Explained sum of squares</a>
 * <li><a href="http://en.wikipedia.org/wiki/Total_sum_of_squares">Wikipedia: Total sum of squares</a>
 * </ul>
 */
public class Residuals extends com.numericalmethod.suanshu.stats.regression.linear.Residuals {

    /**
     * the weighted, fitted values
     */
    public final ImmutableVector wFitted;
    /**
     * the weighted residuals
     */
    public final ImmutableVector wResiduals;
    /**
     * the standard error of the residuals
     */
    public final double stderr;
    /**
     * diagnostic measure: the sum of squared residuals, Σ(ε^2)
     */
    public final double RSS;
    /**
     * diagnostic measure: the total sum of squares, Σ((y-y_mean)^2)
     */
    public final double TSS;
    /**
     * diagnostic measure: the R-squared
     */
    public final double R2;
    /**
     * diagnostic measure: the adjusted R-squared
     */
    public final double AR2;
    /**
     * diagnostic measure: F statistics
     *
     * <blockquote><i>
     * mean of regression / mean squared error =
     * sum((y_i_hat-y_mean)^2) / mean squared error
     * [(TSS-RSS)/n] / [RSS/(m-n)]
     * </i></blockquote>
     *
     * <i>y_i_hat</i> are the fitted values of the regression.
     *
     * @see "p.69, equation (2.60). Applied linear regression models. Kutner, Nachtsheim and Neter. 4th edition."
     */
    public final double f;//TODO: p-value for f-stat
    /**
     * projection matrix H-hat
     *
     * @see "p.168 Section 8.1, Chapter 8. Applied Linear Regression, 3rd edition, 2005. Sanford Weisberg. Wiley-Interscience."
     */
    public final ImmutableMatrix hHat;//TODO
    /**
     * leverage; the bigger the leverage for an observation, the bigger influence on the prediction
     */
    public final ImmutableVector leverage;//TODO
    /**
     * number of observations
     */
    private final int n;
    /**
     * number of factors
     */
    private final int m;

    /**
     * Perform the residual analysis for a linear regression problem.
     *
     * @param problem the linear regression problem to be solved
     * @param betaHat β^
     */
    Residuals(LMProblem problem, Vector betaHat) {
        super(problem, problem.A.multiply(betaHat));

        n = problem.nObs();
        m = problem.nFactors();

        if (problem.weights != null) {
            wFitted = new ImmutableVector(problem.wA.multiply(betaHat));
            wResiduals = new ImmutableVector(problem.wy.minus(wFitted));
        } else {
            wFitted = fitted;
            wResiduals = residuals;
        }

        //the following computations are for the diagnostic measures
        //

        RSS = pow(wResiduals.norm(), 2);

        Vector weights = problem.weights != null ? problem.weights : new DenseVector(n, 1);//equal weights
        double yMean = sum(problem.y.multiply(weights).toArray());//the (weighted) mean
        yMean /= sum(weights.toArray());

        //TSS = sum((y-mean(y))^2 * weights)
        Vector dy = problem.y.minus(yMean * (problem.addIntercept ? 1 : 0));//y - yMean
        TSS = sum(dy.multiply(dy).multiply(weights).toArray());

        R2 = 1 - (RSS / TSS);
        AR2 = 1 - (double) (n - (problem.addIntercept ? 1 : 0)) / (n - m) * (1 - R2);

        stderr = sqrt(RSS / (n - m));

        f = (TSS - RSS) / (m - (problem.addIntercept ? 1 : 0)) / (RSS / (n - m));


        hHat = new ImmutableMatrix(new CongruentMatrix(problem.wA.t(), problem.invOfwAtwA()));//Hhat = wA %*% (wA' %*% wA)^-1 %*% wA'
        leverage = new ImmutableVector(CreateVector.diagonal(hHat));//leverage = [Hhat[i,i]] = [hii]
    }

    /**
     * standard residual = residual / v1 / sqrt(RSS / (n-m))
     *
     * @return standardized residuals
     */
    public ImmutableVector standardized() {
        Vector v1 = (leverage.minus(1).scaled(-1)).pow(0.5);//v1 = sqrt(1 - leverage)
        Vector v2 = wResiduals.divide(v1);
        v2 = v2.scaled(sqrt(1.0 / (RSS / (n - m))));

        ImmutableVector standardized = new ImmutableVector(v2);
        return standardized;
    }

    /**
     * studentized residual = standardized * sqrt((n-m-1) / (n-m-standardized^2))
     *
     * @return studentized residuals
     *
     * @see
     * <ul>
     * <li>"p.90 (4.15). Section 4.3. Regression Analysis by Example, 3rd edition, 2000. Chatterjee, Hadi and Price. Wiley Series in Probability and Statistics."
     * <li>@see <a href="http://en.wikipedia.org/wiki/Studentized_residual">Wikipedia: Studentized residual</a>
     * </ul>
     */
    public Vector studentized() {
        ImmutableVector standardized = standardized();

        double[] v2b = new double[n];
        for (int i = 0; i < n; i++) {
            double std = standardized.get(i + 1);
            v2b[i] = std * sqrt((n - m - 1) / (n - m - std * std));
        }

        return new DenseVector(v2b);
    }
}
