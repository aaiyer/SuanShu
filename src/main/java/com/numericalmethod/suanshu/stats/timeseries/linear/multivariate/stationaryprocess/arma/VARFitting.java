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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class estimates the coefficients for a VAR model.
 *
 * @author Kevin Sun
 */
public class VARFitting {

    private static class Estimators {

        /**
         * the estimated intercept (constant) vector
         */
        private final ImmutableVector mu;
        /**
         * the estimated AR coefficients (excluding the initial 1)
         */
        private final ImmutableMatrix[] phi;

        private Estimators(Matrix coefficients, int p) {//read-only
            int dimension = coefficients.nRows();
            this.mu = new ImmutableVector(coefficients.getColumn(1));
            this.phi = new ImmutableMatrix[p];

            for (int i = 1; i <= p; ++i) {
                DenseMatrix phiTemp = new DenseMatrix(dimension, dimension);
                for (int j = 1; j <= dimension; ++j) {
                    double[] temp = coefficients.getRow(j).toArray();
                    phiTemp.setRow(j, new DenseVector(Arrays.copyOfRange(temp, (i - 1) * dimension + 1, i * dimension + 1)));
                }
                this.phi[i - 1] = new ImmutableMatrix(phiTemp);
            }
        }
    }
    private final Estimators estimators;

    public VARFitting(MultiVariateTimeSeries mts, int p) {
        Matrix coefficients = getCoefficients(mts, p);
        this.estimators = new Estimators(coefficients, p);
    }

    /**
     * We compute the coefficients by solving an OLS regression, one column (dimension) each.
     *
     * @param mts
     * @param p
     * @return
     */
    private Matrix getCoefficients(MultiVariateTimeSeries mts, int p) {
        Matrix series = mts.toMatrix();//the first row corresponds to earliest time; the last row the ending time
        int dimension = series.nCols();
        int T = series.nRows() - p;//we need p lags in each regression
        int length = 1 + (dimension * p);//mu + the AR coefficients for each dimension
        DenseMatrix coefficients = new DenseMatrix(dimension, length);//first column is 'mu'

        DenseMatrix X = new DenseMatrix(T, length);//y = mu + φ * X + ε
        for (int t = p + 1; t <= series.nRows(); ++t) {
            int i = t - p;//row
            int j = 0;//column
            X.set(i, ++j, 1);//for 'mu', the constant intercept
            for (int u = 1; u <= p; ++u) {//the last p rows in 'series'
                for (int v = 1; v <= dimension; ++v) {//the columns in each row (observation)
                    X.set(i, ++j, series.get(t - u, v));
                }
            }
        }

        for (int i = 1; i <= dimension; ++i) {
            DenseVector y = new DenseVector(Arrays.copyOfRange(series.getColumn(i).toArray(), p, series.nRows()));
            LMProblem problem = new LMProblem(y, X, false);
            OLSRegression instance = new OLSRegression(problem);
            coefficients.setRow(i, instance.beta.betaHat);
        }

        return coefficients;
    }

    public ARModel getFittedArModel() {
        ImmutableVector mu = estimators.mu;
        ImmutableMatrix[] phi = estimators.phi;
        ARModel fitted = new ARModel(mu, phi);
        return fitted;
    }
}
