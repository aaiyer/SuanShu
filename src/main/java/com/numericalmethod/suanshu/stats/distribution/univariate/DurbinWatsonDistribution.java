///*
// * Copyright (c) Numerical Method Inc.
// * http://www.numericalmethod.com/
// *
// * THIS SOFTWARE IS LICENSED, NOT SOLD.
// *
// * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
// * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
// * DO NOT USE THIS SOFTWARE.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
// * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
// * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
// * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT,
// * TITLE AND USEFULNESS.
// *
// * IN NO EVENT AND UNDER NO LEGAL THEORY,
// * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
// * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
// * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
// */
//package com.numericalmethod.suanshu.stats.distribution.univariate;
//
//import com.numericalmethod.suanshu.matrix.doubles.Matrix;
//import com.numericalmethod.suanshu.matrix.doubles.dense.operation.Measure;
//import com.numericalmethod.suanshu.matrix.doubles.dense.DenseMatrix;
//import com.numericalmethod.suanshu.misc.Util;
//import com.numericalmethod.suanshu.stats.random.distribution.Gaussian;
//import java.util.Arrays;
//import static com.numericalmethod.suanshu.misc.Util.*;
//import static com.numericalmethod.suanshu.number.DoubleUtil.reverse;
//import static java.lang.Math.*;
//
///**
// * Durbin-Watson distribution is the probability that <code>Pr(DW<sub>l</sub> < q)</code>.
// *
// * <p>
// * This implementation is based on Ansley, Kohn and Shively.
// *
// * @author Haksun Li
// *
// * @see "Computing p-values for the generalized Durbin-Watson and other invariant test statistics. Craig F. Ansley, Robert Kohn, Thomas S. Shively."
// */
//public class DurbinWatson implements ProbabilityDistribution {
//
//    /**
//     * maximum allowable error of computation
//     */
//    public final double E;
//    /**
//     * maximum integration error
//     */
//    public final double integrationErr;
//    /**
//     * truncation error 1
//     */
//    public final double truncationErr1;
//    /**
//     * truncation error 2
//     */
//    public final double truncationErr2;
//    public final Matrix X;
//    public final int lag;
//    public final Matrix A;//TODO: use sparse matrix implementation
//    public final double[] eigenvalues4A;
//    public final double sqrtXtX;
//
//    /**
//     * Construct an instance of the Durbin-Watson distribution.
//     *
//     * @param X
//     * @param lag
//     * @param E maximum allowable error of computation
//     */
//    public DurbinWatson(Matrix X, int lag, double E) {
//        Util.assertArgument(X.nRows() > 2 * lag, "number of observations must be > 2 * lag");
//
//        this.X = X;
//        this.sqrtXtX = sqrt(Measure.det(X.t().multiply(X)));
//
//        this.lag = lag;
//        this.A = A(lag, X.nRows());
//        eigenvalues4A = eigenvalues4A(lag, A.nRows());
//
//        this.E = E;
//        this.integrationErr = 0.1 * E;
//        this.truncationErr1 = 0.1 * 0.9 * E;
//        this.truncationErr2 = 0.9 * 0.9 * E;
//    }
//
//    /**
//     *
//     * @param nObservations number of observations
//     * @param nFactor number of regressors (excluding the intercept)
//     * @param lag
//     * @param E maximum allowable error of computation
//     */
//    public DurbinWatson(int nObservations, int nFactor, int lag, double E) {
//        this(getX(nObservations, nFactor), lag, E);
//    }
//
//    private static Matrix getX(int nObservations, int nFactor) {
//        Gaussian rng = new Gaussian(0, 1);//standard normals
//        Matrix X = new DenseMatrix(nObservations, nFactor + 1);
//
//        for (int i = 1; i <= X.nRows(); ++i) {
//            X.set(i, 1, 1);//first column is the intercept; a constant 1
//            for (int j = 2; j <= X.nCols(); ++j) {
//                X.set(i, j, rng.nextDouble());
//            }
//        }
//
//        return X;
//    }
//
//    /**
//     * @see "Top of p. 279."
//     *
//     * @param lag
//     * @param n number of observations
//     * @return
//     */
//    private static Matrix A(int lag, int n) {
//        Matrix M = new DenseMatrix(n, n);
//
//        //compute the diagonal values
//        for (int i = 1; i <= n; ++i) {
//            if (i >= lag + 1 && i <= n - lag) {
//                M.set(i, i, 2);
//            } else {
//                M.set(i, i, 1);
//            }
//        }
//
//        for (int i = lag + 1; i <= n; ++i) {
//            M.set(i, i - lag, -1);//the lag-th sub-diagonal values
//            M.set(i - lag, i, -1);//the lag-th super-diagonal values
//        }
//
//        return M;
//    }
//
//    /**
//     * @see "Lemma 3.1"
//     *
//     * @param lag
//     * @param n number of observations; dimension of A; number of rows of X
//     * @return the eigenvalues of A, independent of X
//     */
//    private static double[] eigenvalues4A(int lag, int n) {
//        double[] eigenvalues = new double[n];
//
//        int m = n / lag;
//        int k = n % lag;
//
//        int idx = 0;
//
//        if (k > 0) {
//            for (int j = 1; j <= m + 1; ++j) {
//                for (int i = 1; i <= k; ++i) {//multiplicity k
//                    double value = 2 - 2 * cos(PI * (j - 1) / (m + 1));
//                    eigenvalues[idx++] = value;
//                }
//            }
//        }
//
//        for (int j = 1; j <= m; ++j) {
//            for (int i = 1; i <= (lag - k); ++i) {//multiplicity (lag - k)
//                double value = 2 - 2 * cos(PI * (j - 1) / m);
//                eigenvalues[idx++] = value;
//            }
//        }
//
//        assertOrThrow(idx == n ? null
//                : new RuntimeException("error in filling out A's eigenvalues"));
//
//        Arrays.sort(eigenvalues);//sort in ascending order
//        reverse(eigenvalues);//get descending order
//
//        return eigenvalues;
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double mean() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double median() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double variance() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double skew() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double kurtosis() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double entropy() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double cdf(double q) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double quantile(double u) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double density(double x) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    /**
//     * @deprecated Not supported yet.
//     */
//    public double moment(double x) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}

