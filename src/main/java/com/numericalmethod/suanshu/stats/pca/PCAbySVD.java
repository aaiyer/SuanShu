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

import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;

/**
 * This class performs a Principal Component Analysis (PCA) on the given data matrix
 * using the preferred singular value decomposition (SVD) method.
 *
 * <nFactors>
 * The R equivalent function is {@code prcomp}.
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
public class PCAbySVD extends PCAImpl {

    private final boolean centered;
    private final boolean scaled;
    private final ImmutableVector mean;//make sure this does change anywhere in and out of this class
    private final ImmutableVector scale;//make sure this does change anywhere in and out of this class

    /**
     * Perform a Principal Component Analysis, using the preferred SVD method,
     * on a given data matrix with (optional) mean vector and scaling vector provided.
     *
     * @param data a matrix which provides the original data for the principal component analysis
     * @param centered a logical value indicating whether the variables should be shifted to be zero centered
     * @param scaled a logical value indicating whether the variables should be scaled to have unit variance before the analysis takes place
     * (N.B. in general scaling is advisable; however, it should only be used if there is no constant variable)
     * @param mean an optional mean vector (of length equal to nFactors) to be subtracted regardless of the flag 'centered'
     * @param scale an optional scaling vector (of length equal to nFactors) to be divided regardless of the flag 'scaled'
     */
    public PCAbySVD(Matrix data, boolean centered, boolean scaled, Vector mean, Vector scale) {
        super(data);

        this.centered = centered;
        this.scaled = scaled;
        this.mean = mean != null ? new ImmutableVector(mean) : null;
        this.scale = scale != null ? new ImmutableVector(scale) : null;
    }

    /**
     * Perform a principal component analysis, using the preferred SVD method, on a given data matrix (possibly centered and/or scaled).
     *
     * @param data a matrix which provides the original data for the principal component analysis
     * @param centered a logical value indicating whether the variables should be shifted to be zero centered
     * @param scaled a logical value indicating whether the variables should be scaled to have unit variance before the analysis takes place
     * (N.B. in general scaling is advisable; however, it should only be used if there is no constant variable)
     */
    public PCAbySVD(Matrix data, boolean centered, boolean scaled) {
        this(data, centered, scaled, null, null);
    }

    /**
     * Perform a principal component analysis, using the preferred SVD method, on a centered and scaled data matrix.
     *
     * @param data a matrix which provides the original data for the principal component analysis
     */
    public PCAbySVD(Matrix data) {
        this(data, true, true);
    }

    /**
     * Get the sample means that were subtracted.
     *
     * @return the sample means of each variable in the original data
     */
    @Override
    public Vector mean() {
        if (mean != null) {
            return mean;
        } else {
            if (centered) {
                return super.mean();
            } else {
                return new DenseVector(R.rep(0., nFactors()));
            }
        }
    }

    /**
     * Get the scalings applied to each variable,
     * the scaling vector to be divided.
     *
     * @return the scalings applied to each variable in the original data
     */
    @Override
    public Vector scale() {
        if (scale != null) {
            return scale;
        } else {
            if (scaled) {
                return super.scale();
            } else {
                return new DenseVector(R.rep(1., nFactors()));
            }
        }
    }

    /**
     * Get the singular value decomposition (SVD) of matrix X.
     *
     * @return the singular value decomposition (SVD) of matrix X
     */
    public SVD svd() {
        SVD svd = new SVD(X(), true);
        return svd;
    }

    /**
     * Get the standard deviations of the principal components 
     * (i.e., the square roots of the eigenvalues of the correlation (or covariance) matrix,
     * though the calculation is actually done with the singular values of the data matrix)
     *
     * @return the standard deviations of the principal components
     */
    public DenseVector sdPrincipalComponent() {
        SVD svd = svd();
        double[] singular_values = svd.getSingularValues();
        int length = singular_values.length;

        final double n = Math.sqrt(Math.max(1, this.nObs() - 1));
        double[] stdev = new double[length];
        for (int i = 0; i < length; ++i) {
            stdev[i] = singular_values[i] / n;
        }

        return new DenseVector(stdev);
    }

    /**
     * Get the matrix of variable loadings. The signs of the columns of the loading are arbitrary.
     *
     * @return the matrix of variable loadings
     */
    public Matrix loadings() {
        SVD svd = svd();
        return svd.V();
    }
}
