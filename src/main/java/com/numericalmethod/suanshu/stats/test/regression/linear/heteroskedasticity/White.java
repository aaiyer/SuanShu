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
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.Residuals;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import static java.lang.Math.pow;

/**
 * The White test is used to test for heteroskedasticity in a linear regression model.
 * It tests whether the estimated variance of the residuals from a regression are dependent on the values of the independent variables (regressors).
 *
 * <p>
 * The White test tests for conditional heteroskedasticity.
 * It is a chi-squared test: the test statistic is nχ<sup>2</sup> with k degrees of freedom.
 * If the White test shows that there is conditional heteroskedasticity, we can consider a GARCH model.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li>White, H. "A Heteroskedasticity-Consistent Covariance Matrix Estimator and a Direct Test for Heteroskedasticity," Econometrica 48 (4): 817–838. MR575027 JSTOR 1912934. 1980.
 * <li>R. Koenker. "A Note on Studentizing a Test for Heteroskedasticity," Journal of Econometrics 17, 107–112. 1981.
 * <li><a href="http://en.wikipedia.org/wiki/White_test">Wikipedia: White test</a>
 * </ul>
 */
public class White extends Heteroskedasticity {

    private final int K;
    private final int nAuxiliaryFactors;

    /**
     * Perform the White test to test for heteroskedasticity in a linear regression model.
     *
     * @param residuals the Residuals object from an OLS regression
     */
    public White(Residuals residuals) {
        super(residuals);

        this.K = residuals.problem.nExogenousFactors();
        this.nAuxiliaryFactors = K + K * (K + 1) / 2;
    }

    @Override
    public double statistics() {
        if (nAuxiliaryFactors < n) {
            return super.statistics();
        }

        return n;//when nAuxiliaryFactors >= n, there are not enough observations for an OLS problem; this is the R convention
    }

    @Override
    protected ChiSquareDistribution getX2() {
        return new ChiSquareDistribution(nAuxiliaryFactors);
    }

    protected OLSRegression getAuxiliaryRegression() {
        OLSRegression auxiliary = null;

        if (nAuxiliaryFactors < n) {//a over-constrained problem for a valid OLS problem
            final Vector err = residuals.residuals;
            double RSS = pow(err.norm(), 2);
            double sigma2 = RSS / n;//variance of disturbances
            Vector y = err.pow(2).minus(sigma2);

            auxiliary = getAuxiliaryOLSRegression(y, residuals);
        }

        return auxiliary;
    }

    /**
     * the auxiliary regression
     *
     * @return the auxiliary regression
     */
    @Override
    protected OLSRegression getAuxiliaryOLSRegression(Vector y, Residuals residuals) {
        final Matrix regressors = residuals.problem.X();//original regressors

        /**
         * To test for constant variance one regresses the squared residuals from a regression model onto
         * 1) the regressors,
         * 2) the cross-products of the regressors, and
         * 3) the squared regressors.
         */
        double[][] regressor2 = new double[nAuxiliaryFactors][];//cross-products (squares) of the original regressors
        int s = 0;
        for (int i = 1; i <= K; ++i) {
            regressor2[s++] = regressors.getColumn(i).toArray();
        }

        for (int i = 1; i <= K; ++i) {
            for (int j = i; j <= K; ++j) {
                regressor2[s++] = crossProduct(regressors.getColumn(i), regressors.getColumn(j));
            }
        }

        Matrix regressor2M = new DenseMatrix(regressor2).t();
        LMProblem problem = new LMProblem(y, regressor2M, true);
        OLSRegression auxiliary = new OLSRegression(problem);

        return auxiliary;
    }

    private double[] crossProduct(Vector x, Vector y) {
        int length = x.size();
        double[] result = new double[length];
        for (int i = 1; i <= length; ++i) {
            result[i - 1] = x.get(i) * y.get(i);
        }

        return result;
    }
}
