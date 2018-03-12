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
package com.numericalmethod.suanshu.stats.pca;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.stats.descriptive.CorrelationMatrix;
import com.numericalmethod.suanshu.stats.descriptive.CovarianceMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class performs a principal component analysis (PCA) on the given data matrix.
 * The calculation is done using eigen on the correlation or covariance matrix. A preferred
 * method of calculation is to use singular value decomposition on the data matrix,
 * as is done in PrincipalComponentsAnalysis.
 *
 * <p>
 * Each eigenvalue is proportional to the portion of the "variance"
 * (more correctly of the sum of the squared distances of the points from their multidimensional mean)
 * that is correlated with each eigenvector.
 * The sum of all the eigenvalues is equal to the sum of the squared distances of the points from their multidimensional mean.
 * PCA essentially rotates the set of points around their mean in order to align with the principal components.
 * This moves as much of the variance as possible (using an orthogonal transformation) into the first few dimensions.
 * The values in the remaining dimensions, therefore, tend to be small and may be dropped with minimal loss of information.
 *
 * <p>
 * The R equivalent function is {@code princomp}. The main difference is that we use divisor (nObs - 1)
 * instead of nObs for the sample covariance matrix.
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> K. V. Mardia, J. T. Kent and J. M. Bibby, "Multivariate Analysis," London, Academic Press, 1979.
 * <li> W. N. Venables and B. D. Ripley, "Modern Applied Statistics with S," New York, Springer-Verlag, 2002.
 * <li> <a href="http://en.wikipedia.org/wiki/Principal_component_analysis">Wikipedia: Principal component analysis</a>
 * </ul>
 */
public class PCAbyEigen extends PCAImpl {

    private final boolean correlation;
    private final ImmutableMatrix V;//make sure this does change anywhere in and out of this class

    /**
     * Perform a principal component analysis, using the eigen method,
     * on a given data matrix with an optional correlation (or covariance) matrix provided.
     *
     * @param data        an nObs*nFactors numeric matrix which provides the original data for the principal component analysis
     * @param correlation a logical value indicating whether the correlation matrix (preferred) or the covariance matrix should be used (N.B. the correlation matrix can only be used if there is no constant variable)
     * @param V           an optional correlation (or covariance) matrix; if supplied, this is used rather than the correlation (or covariance) matrix of the centered (and possibly scaled) data
     */
    public PCAbyEigen(Matrix data, boolean correlation, Matrix V) {
        super(data);

        if (V != null) {
            assertArgument((V.nCols() == nFactors()) && (V.nRows() == nFactors()),
                    "V is a p*p square matrix, where p is the number of columns of data");
        }

        this.correlation = correlation;
        this.V = V != null ? new ImmutableMatrix(V) : null;
    }

    /**
     * Perform a principal component analysis, using the eigen method, on a given data matrix.
     *
     * @param data        a matrix which provides the original data for the principal component analysis
     * @param correlation a logical value indicating whether the correlation matrix (preferred) or the covariance matrix should be used
     * (N.B. the correlation matrix can only be used if there is no constant variable)
     */
    public PCAbyEigen(Matrix data, boolean correlation) {
        this(data, correlation, null);
    }

    /**
     * Perform a principal component analysis, using the eigen method and the preferred correlation matrix,
     * on a given data matrix.
     *
     * @param data matrix which provides the original data for the principal component analysis
     */
    public PCAbyEigen(Matrix data) {
        this(data, true);
    }

    /**
     * Get the scalings applied to each variable. If covariance matrix is used instead
     * of the (preferred) correlation matrix, no scaling is performed.
     *
     * @return the scalings applied to each variable in the original data
     */
    @Override
    public Vector scale() {
        if (correlation) {
            return super.scale();
        } else {
            return new DenseVector(R.rep(1., nFactors()));
        }
    }

    /**
     * Get the correlation (or covariance) matrix used for the PCA.
     *
     * @return the correlation (or covariance) matrix
     */
    public Matrix V() {
        DenseMatrix cov = correlation ? new CorrelationMatrix(new CovarianceMatrix(X())) : new CovarianceMatrix(X());
        return V != null ? V : cov;
    }

    /**
     * Get the eigenvalue decomposition of the correlation (or covariance) matrix.
     *
     * @return the eigenvalue decomposition of the correlation (or covariance) matrix
     */
    public Eigen eigen() {
        return new Eigen(V());
    }

    /**
     * Get the standard deviations of the principal components (i.e., the square roots of the eigenvalues of the covariance or correlation matrix).
     *
     * @return the standard deviations of the principal components
     */
    @Override
    public Vector sdPrincipalComponent() {
        Eigen eigen = this.eigen();
        double[] eigen_values = eigen.getRealEigenvalues();
        double[] stdev = DoubleArrayMath.sqrt(eigen_values);

        return new DenseVector(stdev);
    }

    /**
     * Get the matrix of variable loadings. The signs of the columns of the loading are arbitrary.
     *
     * @return the matrix of variable loadings
     */
    @Override
    public Matrix loadings() {
        Eigen eigen = this.eigen();
        double[] eigen_values = eigen.getRealEigenvalues();
        int length = eigen_values.length;

        int nFactors = this.nFactors();
        DenseMatrix loadings = new DenseMatrix(nFactors, length);
        for (int i = 1; i <= nFactors; ++i) {
            loadings.setColumn(i, eigen.getProperty(i - 1).eigenVector());
        }

        return loadings;
    }

    /**
     * Get the proportion of overall variance explained by each of the principal components.
     *
     * @return the proportion of overall variance explained by each of the principal components
     */
    @Override
    public Vector proportionVar() {
        Vector sd = sdPrincipalComponent();

        double total_var = MatrixMeasure.tr(V());
        int length = sd.size();

        double[] var = new double[length];
        for (int i = 0; i < length; ++i) {
            var[i] = Math.pow(sd.get(i + 1), 2);
        }

        return new DenseVector(var).scaled(1. / total_var);
    }
}
