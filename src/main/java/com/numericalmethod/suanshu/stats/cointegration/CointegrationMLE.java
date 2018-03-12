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
package com.numericalmethod.suanshu.stats.cointegration;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.EigenProperty;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.Cholesky;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Two or more time series are cointegrated if they each share a common type of stochastic drift,
 * that is, to a limited degree they share a certain type of behavior in terms of their long-term fluctuations,
 * but they do not necessarily move together and may be otherwise unrelated.
 * If two or more series are individually integrated (in the time series sense)
 * but some linear combination of them has a lower order of integration,
 * then the series are said to be cointegrated.
 * A common example is where the individual series are first-order integrated (<i>I(1)</i>)
 * but some (cointegrating) vector of coefficients exists to form a stationary linear combination of them.
 * <p/>
 * This implementation to estimate the cointegrating factors is the Johansen method, named after Søren Johansen.
 * It is an MLE (maximum likelihood estimation) method.
 * The Johansen test is a procedure for testing cointegration of several <i>I(1)</i> time series using MLE.
 * This test permits more than one cointegrating relationship. It is more generally applicable than the Engle–Granger test.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>Søren Johansen, Likelihood-Based Inference in Cointegrated Vector Autoregressive Models, Oxford University Press, USA. February 1, 1996.
 * <li>Kevin Sun, Notes on Cointegration, February 23, 2011.
 * <li><a href="http://en.wikipedia.org/wiki/Cointegration">Wikipedia: Cointegration</a>
 * <li><a href="http://en.wikipedia.org/wiki/Johansen_test">Wikipedia: Johansen test</a>
 * </ul>
 */
public class CointegrationMLE {

    /** This is the loading matrix (read by columns). Speed of adjustment. */
    private final ImmutableMatrix alpha;
    /** These (columns) are the cointegration relations. */
    private final DenseMatrix beta;
    private final ImmutableVector eigenvalues;
    /** the number of rows of the multivariate time series used in regression */
    private final int n;

    /**
     * Perform the Johansen MLE procedure on a multivariate time series.
     *
     * @param ts        a multivariate time series
     * @param intercept indicate whether an intercept is included in the estimation
     * @param p         the number of lags, e.g., 2
     * @param D         the exogenous factor matrix (excluding the intercept)
     */
    public CointegrationMLE(SimpleMultiVariateTimeSeries ts, boolean intercept, int p, Matrix D) {
        assertArgument(p >= 1, "p >= 1");

        final int dimension = ts.dimension(); //the dimension of the multivariate time series)
        final int T = ts.size(); //the length of the multivariate time series)

        final int m = D == null ? 0 : D.nCols(); //the number of exogeneous variables (excluding the intercept)
        assertArgument(D == null || D.nRows() == T, "D and the multivariate time series must have the same number of rows (timed data)");

        this.n = T - p;
        final int nparams = dimension * (p - 1) + m + (intercept ? 1 : 0); //the number of parameters to estimtate for each dimension
        assertArgument(T - p > nparams + (p * (p + 1) / 2), "the multivariate time series must be long enough for estimation (enough timed data)");

        AuxiliaryRegression1 regression_1 = new AuxiliaryRegression1(ts, p, D, intercept);
        AuxiliaryRegression2 regression_2 = new AuxiliaryRegression2(ts, p, D, intercept);
        Matrix errors_1 = regression_1.errors();
        Matrix errors_2 = regression_2.errors();

        Matrix s_11 = errors_1.t().multiply(errors_1).scaled(1. / n);
        Matrix s_12 = errors_1.t().multiply(errors_2).scaled(1. / n);
        Matrix s_21 = s_12.t();
        Matrix s_22 = errors_2.t().multiply(errors_2).scaled(1. / n);

        Cholesky cholesky = new Cholesky(s_22);
        LowerTriangularMatrix s = cholesky.L();
        Matrix s_inv = new Inverse(s);
        Matrix S = s_inv.multiply(s_21).multiply(new Inverse(s_11)).multiply(s_12).multiply(s_inv.t());

        Eigen eigen = new Eigen(S);
        this.eigenvalues = new ImmutableVector(new DenseVector(eigen.getRealEigenvalues()));

        int nRealEigenvalues = this.eigenvalues.size();
        this.beta = new DenseMatrix(dimension, nRealEigenvalues);//each column is a cointegrating factor
        for (int i = 0; i < nRealEigenvalues; ++i) {
            EigenProperty property = eigen.getProperty(i);
            Vector ev = s_inv.t().multiply(property.eigenVector());
            ev = ev.scaled(1. / ev.get(1));
            this.beta.setColumn(i + 1, ev);
        }

        this.alpha = new ImmutableMatrix(s_12.multiply(this.beta).multiply(new Inverse(new CongruentMatrix(this.beta, s_22))));
    }

    /**
     * Perform the Johansen MLE procedure on a multivariate time series, using the EIGEN test.
     *
     * @param ts        a multivariate time series
     * @param intercept indicate whether an intercept is included in the estimation
     * @param p         the number of lags, e.g., 2
     */
    public CointegrationMLE(SimpleMultiVariateTimeSeries ts, boolean intercept, int p) {
        this(ts, intercept, p, null);
    }

    /**
     * Perform the Johansen MLE procedure on a multivariate time series,
     * using the EIGEN test, with the number of lags = 2.
     *
     * @param ts        a multivariate time series
     * @param intercept indicate whether an intercept is included in the estimation
     */
    public CointegrationMLE(SimpleMultiVariateTimeSeries ts, boolean intercept) {
        this(ts, intercept, 2);
    }

    /**
     * Get the set of adjusting coefficients, by columns.
     *
     * @return <i>α</i>, the adjusting coefficients
     */
    public ImmutableMatrix alpha() {
        return alpha;
    }

    /**
     * Get the set of cointegrating factors, by columns.
     *
     * @return <i>β</i>, the cointegrating factors
     */
    public ImmutableMatrix beta() {
        return new ImmutableMatrix(beta);
    }

    /**
     * Get the <i>r</i>-th cointegrating factor, counting from 1.
     *
     * @param r an index
     * @return <i>β<sub>r</sub></i>, the <i>r</i>-th cointegrating factor
     */
    public ImmutableVector beta(int r) {
        return new ImmutableVector(beta.getColumn(r));
    }

    /**
     * Get the set of <em>real</em> eigenvalues.
     *
     * @return the real eigenvalues
     */
    public ImmutableVector getEigenvalues() {
        return eigenvalues;
    }

    /**
     * Get the rank of the system, i.e., the number of (real) eigenvalues.
     *
     * @return the rank
     */
    public int rank() {
        return eigenvalues.size();
    }

    /**
     * Get the number of rows of the multivariate time series used in regression.
     *
     * @return {@code ts.size - p}
     */
    public int n() {
        return n;
    }
}
