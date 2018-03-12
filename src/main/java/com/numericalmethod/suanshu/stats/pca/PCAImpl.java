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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 *
 * @author Haksun Li
 */
abstract class PCAImpl implements PCA {

    /**
     * a matrix which provides the original data for the principal component analysis
     *
     * <p>
     * The rows are samples; the columns are factors.
     */
    private final ImmutableMatrix data;

    PCAImpl(Matrix data) {
        this.data = new ImmutableMatrix(data);
    }

    /**
     * Get the original data matrix.
     *
     * @return the original data matrix
     */
    public ImmutableMatrix data() {
        return data;
    }

    @Override
    public int nObs() {
        return data.nRows();//the sample size
    }

    @Override
    public int nFactors() {
        return data.nCols();//the number of variables
    }

    @Override
    public Vector mean() {
        double[] mu = new double[nFactors()];//the sample mean of the original data
        for (int i = 0; i < mu.length; ++i) {
            mu[i] = new Mean(data.getColumn(i + 1).toArray()).value();
        }

        return new DenseVector(mu);
    }

    @Override
    public Vector scale() {
        double[] sigma = new double[nFactors()];//the sample standard deviation of the original data
        for (int i = 0; i < sigma.length; ++i) {
            sigma[i] = Math.sqrt(new Variance(data.getColumn(i + 1).toArray()).value());
        }

        return new DenseVector(sigma);
    }

    @Override
    public Matrix X() {
        Vector mu = mean();
        Vector sigma = scale();

        int nFactors = nFactors();
        DenseMatrix X = new DenseMatrix(nObs(), nFactors);
        for (int j = 1; j <= nFactors; ++j) {
            X.setColumn(j, data().getColumn(j).minus(mu.get(j)).scaled(1. / sigma.get(j)));
        }

        return X;
    }

    @Override
    public double sdPrincipalComponent(int i) {
        return sdPrincipalComponent().get(i);
    }

    @Override
    public Vector loadings(int i) {
        Matrix loadings = this.loadings();
        return loadings.getColumn(i);
    }

    @Override
    public Vector proportionVar() {
        Vector sd = sdPrincipalComponent();

        double total_var = 0.;
        int length = sd.size();

        double[] var = new double[length];
        for (int i = 0; i < length; ++i) {
            var[i] = Math.pow(sd.get(i + 1), 2);
            total_var += var[i];
        }

        return new DenseVector(var).scaled(1. / total_var);
    }

    @Override
    public double proportionVar(int i) {
        return proportionVar().get(i);
    }

    @Override
    public Vector cumulativeProportionVar() {
        double[] proportion = this.proportionVar().toArray();
        double[] cum_proportion = R.cumsum(proportion);
        return new DenseVector(cum_proportion);
    }

    @Override
    public Matrix scores() {
        Matrix loadings = this.loadings();
        return X().multiply(loadings);
    }
}
