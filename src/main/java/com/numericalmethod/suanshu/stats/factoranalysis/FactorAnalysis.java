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
package com.numericalmethod.suanshu.stats.factoranalysis;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.foreach;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.sum;
import com.numericalmethod.suanshu.stats.descriptive.CorrelationMatrix;
import com.numericalmethod.suanshu.stats.descriptive.CovarianceMatrix;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;
import java.util.Arrays;

/**
 * Factor analysis is a statistical method used to describe variability among
 * observed variables in terms of a potentially lower number of unobserved variables called factors.
 * Factor analysis searches for joint variations in response to unobserved latent variables.
 * The observed variables are modeled as linear combinations of the potential factors, plus "error" terms.
 * The information gained about the interdependencies between observed variables
 * can be used later to reduce the set of variables in a dataset.
 * Although factor analysis and principal component analysis (PCA) are both dimension reduction techniques,
 * those two are not identical.
 * Because PCA performs a variance-maximizing rotation of the variable space,
 * it takes into account all variability in the variables.
 * In contrast, factor analysis estimates how much of the variability is due to common factors.
 * The two methods become essentially equivalent
 * if the error terms in the factor analysis model can be assumed to all have the same variance.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li> M. S. Bartlett, "The Statistical Conception of Mental Factors," The British Journal of Psychology, vol. 28, 97–104, 1937.
 * <li> M. S. Bartlett, "A Note on Multiplying Factors for Various Chi-Squared Approximations," Journal of the Royal Statistical Society, Series B, vol. 16, 296–298, 1954.
 * <li> K. G. Jöreskog, "Statistical Estimation in Factor Analysis: A New Technique and Its Foundation," Almqvist and Wicksell, 1963.
 * <li> D. N. Lawley and A. E. Maxwell, "Factor Analysis as a Statistical Method," Second Edition, Butterworths, 1971.
 * <li> G. H. Thomson, "The Factorial Analysis of Human Ability," London University Press, 1951.
 * <li><a href="http://en.wikipedia.org/wiki/Factor_analysis">Wikipedia: Factor analysis</a>
 * </ul>
 */
public class FactorAnalysis {

    /**
     * These are the different ways to compute the factor analysis scores.
     */
    public static enum ScoringRule {

        /**
         * Thomson's (1951) scores
         */
        THOMSON,
        /**
         * Bartlett's (1937) weighted least-squares scores
         */
        BARTLETT
    }

    private final ImmutableMatrix data;
    private final ImmutableMatrix S;
    private final int k;
    private final ScoringRule rule;

    /**
     * Perform factor analysis on the data set with a user defined scoring rule
     * and a user defined covariance (or correlation) matrix.
     *
     * @param data     the data set
     * @param nFactors the number of factors
     * @param rule     the scoring rule
     * @param S        a covariance (or correlation) matrix,
     * usually taken to be the covariance (or correlation) matrix of the {@code data} set.
     * Using a correlation matrix amounts to scaling the original data set.
     */
    public FactorAnalysis(Matrix data, int nFactors, ScoringRule rule, Matrix S) {
        this.data = new ImmutableMatrix(data);
        this.k = nFactors;
        this.rule = rule;
        this.S = new ImmutableMatrix(S);
    }

    /**
     * Perform factor analysis on the data set with a user defined scoring rule.
     *
     * @param data     the data set
     * @param nFactors the number of factors
     * @param rule     the scoring rule
     */
    public FactorAnalysis(Matrix data, int nFactors, ScoringRule rule) {
        this(data, nFactors, rule, new CorrelationMatrix(new CovarianceMatrix(data)));
    }

    /**
     * Perform factor analysis on the data set,
     * using Bartlett's weighted least-squares scores, and sample correlation matrix.
     *
     * @param data     the data set
     * @param nFactors the number of factors
     */
    public FactorAnalysis(Matrix data, int nFactors) {
        this(data, nFactors, ScoringRule.BARTLETT, new CorrelationMatrix(new CovarianceMatrix(data)));
    }

    /**
     * Get the covariance (or correlation) matrix.
     *
     * @return the covariance (or correlation) matrix
     */
    public ImmutableMatrix S() {
        return S;
    }

    /**
     * Get the number of observations.
     *
     * @return the number of observations
     */
    public int nObs() {
        return data.nRows();
    }

    /**
     * Get the number of variables in the original data set.
     *
     * @return the number of variables in the original data set
     */
    public int nVariables() {
        return S.nRows();
    }

    /**
     * Get the number of factors.
     *
     * @return the number of factors
     */
    public int nFactors() {
        return k;
    }

    /**
     * Get the scoring rule.
     *
     * @return the scoring rule
     */
    public ScoringRule scoringRule() {
        return rule;
    }

    /**
     * Get the estimators (estimated psi, loading matrix, degree of
     * freedom, test statistics, p-value, etc) obtained from the factor analysis,
     * given the maximum number of iterations.
     *
     * @param maxIterations the maximum number of iterations
     * @return the estimators from the factor analysis
     */
    public FAEstimator getEstimators(int maxIterations) {
        //These initial psi values are given in equation (4.21) on page 31 of Lawley
        //and Maxwell (1971) and was first proposed by Jöreskog (1963).
        final int p = nVariables();
        final double scale = 1. - 0.5 * k / p; //(1 - 1/2 * k / p) in equation (4.21) on p.31 of Lawley and Maxwell (1971)
        final Matrix s = new Inverse(S);

        Vector initials = new DenseVector(p);
        for (int i = 1; i <= p; ++i) {
            initials.set(i, scale / s.get(i, i)); //equation (4.21) of Lawley and Maxwell (1971)
        }

        return getEstimators(initials, maxIterations);
    }

    /**
     * Get the estimators (estimated psi, loading matrix, degree of
     * freedom, test statistics, p-value, etc) obtained from the factor analysis,
     * given the initial psi and the maximum number of iterations.
     *
     * @param initial       the initial values of psi
     * @param maxIterations the maximum number of iterations
     * @return the estimators from the factor analysis
     */
    public FAEstimator getEstimators(Vector initial, int maxIterations) {
        //TODO: use FactorAnalysisMLE.GRADIENT.ANALYTICAL
        //TODO: although the gradient function has been defined, it will not used until the L-BFGS-B algorithm is implemented
        FactorAnalysisMLE mle = new FactorAnalysisMLE(S(), k, FactorAnalysisMLE.GRADIENT.NUMERICAL, SuanShuUtils.autoEpsilon(S), maxIterations);
        Vector psi = mle.estimate(initial);
        Matrix LAMBDA0 = getLoadings(psi, k, S); //get the unrotated loadings
        Matrix LAMBDA = getRotatedLoadings(LAMBDA0, true, 1e-5); //get the varimax-rotated loadings

        //for uniqueness: make sure that the column sums of the loading matrix are all positive
        for (int i = 1; i <= LAMBDA.nCols(); ++i) {
            double colSum = 0;
            for (int j = 1; j <= LAMBDA.nRows(); ++j) {
                colSum += LAMBDA.get(j, i);
            }

            if (colSum < 0) {
                ((DenseMatrix) LAMBDA).setColumn(i, LAMBDA.getColumn(i).scaled(-1.));
            }
        }

        //get the log-likelihood of MLE optimizaiton
        double likelihood = mle.nL.evaluate(psi);

        //get the degree of freedom
        int dof = nVariables() - k;
        dof *= dof;
        dof -= nVariables() + k;
        dof /= 2; //dof = ((p - k)^2 - (p + k)) / 2; this is derived on p.10 (Section 2.3) of Lawley and Maxwell (1971)

        //produce the scores
        Matrix scores = getScores(data, S, psi, LAMBDA, rule);

        return new FAEstimator(nObs(), psi, LAMBDA, likelihood, dof, scores);
    }

    /**
     * Get the (unrotated) loading matrix obtained by using a given set of
     * psi, the number of factors and a covariance (or correlation) matrix.
     *
     * @param PSI <i>E(ee')</i>
     * @param k   the number of factors
     * @param S   the covariance (or correlation) matrix used for factor analysis
     * @return the (unrotated) loading matrix, the lambda
     */
    static Matrix getLoadings(Vector psi, int k, Matrix S) {
        final int p = S.nRows(); //number of variables
        double[] psiArr = psi.toArray();

        Matrix PSI_SQRT = new DiagonalMatrix(foreach(
                psiArr,
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double psi) {
                        return sqrt(psi);
                    }
                }));

        Matrix PSI_INV_SQRT = new DiagonalMatrix(foreach(
                psiArr,
                new UnivariateRealFunction() { //sqx_recip = 1/sqrt(x)

                    @Override
                    public double evaluate(double psi) {
                        return 1. / sqrt(psi);
                    }
                })); //forms the diagonal matrix Psi^{-1/2} used in (4.11) of Lawley and Maxwell (1971)

        Matrix S_STAR = PSI_INV_SQRT.multiply(S).multiply(PSI_INV_SQRT); //(4.11) on p. 28 of Lawley and Maxwell (1971)

        Eigen eigen = new Eigen(S_STAR); //find the eigenvalues of S_STAR (theta_1, ..., theta_p)
        double[] theta = Arrays.copyOfRange(eigen.getRealEigenvalues(), 0, k); //list the k largest eigenvalues (theta_1, ..., theta_k) in descending order

        Matrix DELTA_SQRT = new DiagonalMatrix(foreach(
                theta,
                new UnivariateRealFunction() { //sqrt(max(x - 1, 0))

                    @Override
                    public double evaluate(double theta) {
                        //if any of the eigenvalues is less than 1, replace it by 0; otherwise, use the square root of (theta_i - 1)
                        return sqrt(max(theta - 1., 0.));
                    }
                }));//eq. 4.12

        DenseMatrix OMEGA = new DenseMatrix(p, k);
        for (int i = 1; i <= k; ++i) {
            OMEGA.setColumn(i, eigen.getProperty(i - 1).eigenVector());
        } //this is the matrix of the first k (nFactors) normalized eigenvectors

        //p. 8, 28 of Lawley and Maxwell (1971) for details
        Matrix LAMBDA = PSI_SQRT.multiply(OMEGA).multiply(DELTA_SQRT);//eq. 4.13

        return LAMBDA;
    }

    /**
     * Get the varimax-rotated (and possibly normalized) loading matrix.
     *
     * @param lambda0       the unrotated loadings
     * @param normalization indicate whether normalization is used in the varimax rotation
     * @param epsilon       the precision used in the SVD of the varimax rotation
     * @return the varimax-rotated loadings
     */
    static Matrix getRotatedLoadings(Matrix lambda0, boolean normalization, double epsilon) {
        if (lambda0.nCols() < 2) {
            return lambda0; //if there is only 1 column, return the original loading matrix (vector)
        }

        Matrix M = getVarimaxRotation(lambda0, normalization, epsilon); //get the varimax rotation matrix
        return lambda0.multiply(M);//p. 72 bottom
    }

    /**
     * This performs the varimax rotation of an unrotated loadings matrix; see
     * Section 6.3 of Lawley and Maxwell (1971) for details. A varimax rotation
     * is a change of coordinates used in factor analysis (and PCA) that
     * maximizes the sum of the variances of the squared loadings.
     *
     * @param unroated_loadings the unrotated loadings
     * @param normalization     indicate whether normalization is used in varimax rotation
     * @param epsilon           the precision used in the SVD of varimax rotation
     * @return the varimax rotation matrix
     */
    static Matrix getVarimaxRotation(Matrix unrotatedLoadings, boolean normalization, double epsilon) {
        final int p = unrotatedLoadings.nRows(); //p, the number of variables
        final int k = unrotatedLoadings.nCols(); //k, the number of factors used in factor analysis, i.e. nFactors
        if (k < 2) {
            return new DenseMatrix(new double[]{1.}, 1, 1); //if only 1 factor is used, no rotation is needed, so return the identity matrix
        }

        DenseMatrix lambda0 = new DenseMatrix(unrotatedLoadings); //make a copy of the unrotated loadings
        double[] norm = R.rep(1., p);
        if (normalization) { //get the normalized unrotated loadings if normalization is to be used
            for (int i = 1; i <= p; ++i) {
                norm[i - 1] = lambda0.getRow(i).norm();
                lambda0.setRow(i, lambda0.getRow(i).scaled(1. / norm[i - 1]));
            }
        }

        //this is the implimentation of the algorithm in Section 6.2 (between (6.3)
        //and (6.4) on page 73, and also pages 74-75) of Lawley and Maxwell (1971).
        Matrix M = new DiagonalMatrix(R.rep(1., k));
        for (double diagSum0 = 0, diagSum1 = 0;; diagSum0 = diagSum1) {
            Matrix lambda = lambda0.multiply(M); //the (temparary) rotated loadings; p. 72

            Matrix C = new DenseMatrix(p, k); //p. 73: c_{i,r} = lambda_{i,r} ^ 3; loadings cubed
            for (int i = 1; i <= p; ++i) {
                for (int j = 1; j <= k; ++j) {
                    C.set(i, j, pow(lambda.get(i, j), 3)); //get the cubed loadings for each entry of lambda
                }
            }

            double[] dr = new double[k];//eq. 6.1: sum of squares of the loadings in the r-th column of lambda
            for (int i = 0; i < k; ++i) {
                dr[i] = lambda.getColumn(i + 1).norm();
                dr[i] *= dr[i];
            }
            DiagonalMatrix D = new DiagonalMatrix(dr);

            Matrix B = lambda0.t().multiply(C.minus(lambda.multiply(D).scaled(1. / p)));//eq. 6.4

            /*
             * We use a slightly different technique than that in the book.
             *
             * B1 = U * D * V'
             *
             * A1 = (V')^(-1) * D * V^(-1)
             * = V * D * V'
             *
             * M2 = B1 * A1^(-1) = U * D * V' * V * D^(-1) * V'
             * = U * V'
             */
            SVD svd = new SVD(B, true);
            M = svd.U().multiply(svd.V().t());

            diagSum1 = sum(MatrixUtils.to1DArray(svd.D()));
            if (diagSum1 < diagSum0 * (1. + epsilon)) { //stop the iterative procedure once the trace has converged
                break;
            }
        }

        return M; //return the varimax rotation matrix
    }

    /**
     * This function calculates the scores according to the user defined scoring
     * rule (Thomson's (1951) scores or Bartlett's (1937) weighted least-squares
     * scores).
     *
     * @param data      the matrix of original data set
     * @param S         the covariance (or correlation) matrix used for factor analysis
     * @param psi       the psi
     * @param lambda    the matrix of loadings
     * @param scoreType the scoring rule
     * @return a matrix of scores
     */
    private Matrix getScores(Matrix data, Matrix S, Vector psi, Matrix lambda, ScoringRule scoreType) {
//        final int nRows = data.nRows(); //number of observations
        final int nCols = data.nCols(); //number of variables

        DenseMatrix z = new DenseMatrix(data); //z is a copy of the original data
        for (int i = 1; i <= nCols; ++i) { //get the scaled data matrix
            double[] col = z.getColumn(i).toArray();
            double mean = new Mean(col).value();
            double sd = new Variance(col).standardDeviation();
            z.setColumn(i, z.getColumn(i).minus(mean).scaled(1. / sd));
        }

        Matrix scores;
        switch (scoreType) {
            //Thomson's scores = z %*% S^{-1} %*% loadings; see Thomson (1951) for details
            case THOMSON: {
                Matrix x = new Inverse(S).multiply(lambda); //x = solve(S, loadings) = S^{-1} %*% loadings
                scores = z.multiply(x);
                break;
            }

            //Bartlett's scores = t((x %*% loadings)^{-1} %*% (x %*% t(z))); see Bartlett (1937) for details
            case BARTLETT: {
                DenseMatrix x = new DenseMatrix(lambda.nRows(), lambda.nCols());
                for (int i = 1; i <= lambda.nRows(); ++i) {
                    for (int j = 1; j <= lambda.nCols(); ++j) {
                        x.set(i, j, lambda.get(i, j) / psi.get(i)); //x[i, j] = loadings[i, j] / psi[i]
                    }
                }
                x = x.t();
                scores = new Inverse(x.multiply(lambda)).multiply(x.multiply(z.t())).t();
                break;
            }

            default:
                throw new UnsupportedOperationException("");
        }

        return scores;
    }
}
