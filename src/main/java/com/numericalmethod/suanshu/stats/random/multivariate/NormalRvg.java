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
package com.numericalmethod.suanshu.stats.random.multivariate;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.CholeskyWang2006;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.normal.BoxMuller;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * A multivariate Normal random vector is said to be p-variate normally distributed
 * if every linear combination of its p components has a univariate normal distribution.
 * This implementation uses the "Best Approximation of Indefinite Matrices" in the reference.
 * In the case where the covariance matrix is not positive definite,
 * we force the diagonal elements in the eigen decomposition to a small non-negative number, e.g., 0.
 * Then, we re-construct a positive definite matrix from the new diagonal elements.
 * <p/>
 * The R equivalent function is {@code rmvnorm} in package {@code mvtnorm}.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Multivariate_normal_distribution">Wikipedia: Multivariate normal distribution</a>
 * <li>"Jin Wang, Chunlei Liu. "Generating Multivariate Mixture of Normal Distributions using a Modified Cholesky Decomposition," Simulation Conference, 2006. WSC 06. Proceedings of the Winter. p. 342 - 347. 3-6 Dec. 2006."
 * </ul>
 */
public class NormalRvg implements RandomVectorGenerator {

    private final int size;
    private final ImmutableVector mu;
    private final ImmutableMatrix A;
    private final IID iid;

    /**
     * Construct a multivariate Normal random vector generator.
     *
     * @param mu      the mean
     * @param sigma   the covariance matrix
     * @param uniform a uniform random number generator
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public NormalRvg(Vector mu, Matrix sigma, RandomLongGenerator uniform, double epsilon) {
        assertArgument((sigma.nRows() == mu.size()) && (sigma.nCols() == mu.size()),
                       "sigma should be a square matrix of dimension mu.size() by mu.size()");

        this.size = mu.size();
        this.mu = new ImmutableVector(mu);
        this.iid = new IID(new BoxMuller(uniform), size);
        this.A = new ImmutableMatrix(new CholeskyWang2006(sigma, epsilon));
    }

    /**
     * Construct a multivariate Normal random vector generator.
     *
     * @param mu    the mean
     * @param sigma the covariance matrix
     */
    public NormalRvg(Vector mu, Matrix sigma) {
        this(mu, sigma, new UniformRng(), SuanShuUtils.autoEpsilon(MatrixUtils.to1DArray(sigma)));
    }

    /**
     * Construct a standard multivariate Normal random vector generator.
     *
     * @param dim the dimension
     */
    public NormalRvg(int dim) {
        this(new DenseVector(R.rep(0., dim)),
             new DiagonalMatrix(dim).ONE());
    }

    @Override
    public void seed(long... seeds) {
        iid.seed(seeds);
    }

    @Override
    public double[] nextVector() {
        Vector z = new DenseVector(iid.nextVector());
        Vector x = mu.add(A.multiply(z));
        return x.toArray();
    }
}
