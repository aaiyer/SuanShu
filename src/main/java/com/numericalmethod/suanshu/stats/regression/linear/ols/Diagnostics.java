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

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import static java.lang.Math.*;

/**
 * This class collects some diagnostics measures for the goodness of fit for an Ordinary Least Square linear regression model.
 *
 * <p>
 * DFFITS is a diagnostic meant to show how influential a point is in a statistical regression.
 * It is defined as the change ("DFFIT"), in the predicted value for a point, obtained when that point is left out of the regression, "Studentized" by dividing by the estimated standard deviation of the fit at that point.
 *
 * <p>
 * Cook's distance is a commonly used estimate of the influence of a data point.
 * Cook's distance can be used in several ways: to indicate data points that are particularly worth checking for validity; to indicate regions of the design space where it would be good to be able obtain more data points.
 *
 * <p>
 * Hadi proposed a measure of influence of the i-th observation based on the fact that influential observations are outliers in either the response variable or in the predictors, or both.
 *
 * @author Haksun Li
 *
 * @see
 * <ul><a href="http://en.wikipedia.org/wiki/Cook%27s_distance">Wikipedia: Cook's distance</a>
 * <li><a href="http://en.wikipedia.org/wiki/DFFITS">Wikipedia: DFFITS</a>
 * </ul>
 */
public class Diagnostics {

    /**
     * DFFITS, Welsch and Kuh Measure
     *
     * @see
     * <ul>
     * <li>"p.105 (4.23). Section 4.9.2. Regression Analysis by Example, 3rd edition, 2000. Chatterjee, Hadi and Price. Wiley Series in Probability and Statistics."
     * <li>Belsley, David A.; Edwin Kuh, Roy E. Welsch. Regression diagnostics : identifying influential data and sources of collinearity. Wiley series in probability and mathematical statistics. New York: John Wiley & Sons. ISBN 0471058564. 1980.
     * </ul>
     */
    public final ImmutableVector DFFITS;
    /**
     * Cook distance
     *
     * @see "p.200. Applied Linear Regression, 3rd edition, 2005. Sanford Weisberg. Wiley-Interscience."
     */
    public final ImmutableVector cookDistances;
    /**
     * Hadi's influence measure
     *
     * @see "p.105 (4.24). Section 4.9.2. Regression Analysis by Example, 3rd edition, 2000. Chatterjee, Hadi and Price. Wiley Series in Probability and Statistics."
     */
    public final ImmutableVector Hadi;

    /**
     * Construct an instance of the <tt>Diagnostics</tt> from the results of the residual analysis.
     * 
     * @param residuals the residual analysis of a linear regression problem
     */
    Diagnostics(Residuals residuals) {
        final int m = residuals.problem.nObs();
        final int n = residuals.problem.nFactors();

        //DFFITS = student residuals * sqrt(leverage[i]/(1-leverage[i]))
        double[] v2c = new double[m];
        for (int i = 0; i < m; i++) {
            double leverage_i = residuals.leverage.get(i + 1);
            v2c[i] = residuals.studentized().get(i + 1) * sqrt(leverage_i / (1 - leverage_i));
        }
        DFFITS = new ImmutableVector(new DenseVector(v2c));

        //cook distances = standardized^2 * leverage / (1 - leverage) / m
        Vector v3 = residuals.standardized().pow(2);
        v3 = v3.multiply(residuals.leverage);
        v3 = v3.divide(residuals.leverage.minus(1));
        v3 = v3.scaled(-1.0 / n);
        cookDistances = new ImmutableVector(v3);

        //Hadi = leverage[i] / (1-leverage[i]) + m / (1-leverage[i])*d(i)^2 / (1-d(i)^2)
        //d(i) = residuals(i) / sqrt(RSS)
        double[] v2d = new double[m];
        double[] v2e = new double[m];

        for (int i = 0; i < m; i++) {
            v2d[i] = pow(residuals.wResiduals.get(i + 1) / sqrt(residuals.RSS), 2);
            double leverage_i = residuals.leverage.get(i + 1);
            v2e[i] = leverage_i / (1 - leverage_i) + n / (1 - leverage_i) * v2d[i] / (1 - v2d[i]);
        }
        Hadi = new ImmutableVector(new DenseVector(v2e));
    }
}
