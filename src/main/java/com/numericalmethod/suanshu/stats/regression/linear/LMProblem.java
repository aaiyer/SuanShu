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
package com.numericalmethod.suanshu.stats.regression.linear;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This class represents a linear regression or a linear model (LM) problem.
 *
 * <p>
 * Linear regression models the relationship between a scalar variable <i>y</i> and one or more variables denoted <i>X</i>.
 * In linear regression, models of the unknown parameters are estimated from the data using linear functions.
 * Most commonly, linear regression refers to a model in which the conditional mean of <i>y</i> given the value of <i>y</i> is an affine function of <i>y</i>.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Linear_regression">Wikipedia: Linear regression</a>
 */
public class LMProblem {

    /**
     * the response vector; the regressands; the dependent variables
     *
     * <p>
     * a vector of length <i>n</i>
     */
    public final ImmutableVector y;
    /**
     * the weighted response vector
     */
    public final ImmutableVector wy;
    /**
     * the design matrix, the regressors, including the intercept if any; each column corresponds to one regressor
     *
     * <p>
     * a <i>n x m</i> matrix
     */
    public final ImmutableMatrix A;
    /**
     * the weighted design matrix, <i>w</i>
     */
    public final ImmutableMatrix wA;
    /**
     * {@code true} iff to add an intercept term to the linear regression
     */
    public final boolean addIntercept;
    /**
     * the weights to each observation
     */
    public final ImmutableVector weights;
    /**
     * (wA' %*% wA)<sup>-1</sup>
     */
    private volatile ImmutableMatrix invOfwAtwA = null;

    /**
     * Construct a linear regression problem.
     *
     * @param y            the dependent variables
     * @param X            the factors
     * @param addIntercept {@code true} iff to add an intercept term to the linear regression
     * @param weights      the weights to each observation
     */
    public LMProblem(Vector y, Matrix X, boolean addIntercept, Vector weights) {
        this.y = !(y instanceof ImmutableVector) ? new ImmutableVector(y) : (ImmutableVector) y;
        this.addIntercept = addIntercept;

        //add intercept
        this.A = new ImmutableMatrix(
                (!addIntercept) ? X
                : CreateMatrix.cbind(X,//append A with a column vector of constant 1
                                     new DenseMatrix(R.rep(1.0, X.nRows()), X.nRows(), 1)));

        this.weights = weights != null ? !(weights instanceof ImmutableVector) ? new ImmutableVector(weights) : (ImmutableVector) weights : null;

        if (weights != null) {
            Vector rootWeights = weights.pow(0.5);
            Matrix W = new DiagonalMatrix(rootWeights.toArray());
            wA = new ImmutableMatrix(W.multiply(A));
            wy = new ImmutableVector(y.multiply(rootWeights));
        } else {
            wA = this.A;
            wy = this.y;
        }

        checkInputs();
    }

    /**
     * Construct a linear regression problem, assuming a constant term (the intercept).
     *
     * @param y       the dependent variables
     * @param X       the factors
     * @param weights the weights to each observation
     */
    public LMProblem(Vector y, Matrix X, Vector weights) {
        this(y, X, true, weights);
    }

    /**
     * Construct a linear regression problem, assuming equal weights to all observations.
     *
     * @param y            the dependent variables
     * @param X            the factors
     * @param addIntercept {@code true} iff to add an intercept term to the linear regression
     */
    public LMProblem(Vector y, Matrix X, boolean addIntercept) {
        this(y, X, addIntercept, null);
    }

    /**
     * Construct a linear regression problem, assuming
     * <ul>
     * <li>a constant term (the intercept)
     * <li>equal weights to all observations
     * </ul>
     *
     * @param y the dependent variables
     * @param X the factors
     */
    public LMProblem(Vector y, Matrix X) {
        this(y, X, true, null);
    }

    /**
     * Copy constructor.
     *
     * @param that another <tt>LMProblem</tt>
     */
    public LMProblem(LMProblem that) {
        this.y = new ImmutableVector(that.y);
        this.A = new ImmutableMatrix(that.A);
        this.wy = new ImmutableVector(that.wy);
        this.wA = new ImmutableMatrix(that.wA);
        this.addIntercept = that.addIntercept;
        this.weights = that.weights == null ? null : new ImmutableVector(that.weights);
    }

    /**
     * the number of observations
     *
     * @return the number of observations
     */
    public int nObs() {
        return A.nRows();
    }

    /**
     * the number of factors, including the intercept if any
     *
     * @return the number of factors, including the intercept if any
     */
    public int nFactors() {
        return A.nCols();
    }

    /**
     * the number of factors, excluding the intercept
     *
     * @return the number of factors, excluding the intercept
     */
    public int nExogenousFactors() {
        return A.nCols() - (addIntercept ? 1 : 0);
    }

    /**
     * Get a copy of the factor matrix.
     *
     * <p>
     * The automatically appended intercept is not included.
     *
     * @return a copy of the factor matrix.
     */
    public ImmutableMatrix X() {
        Matrix X = !addIntercept ? A : CreateMatrix.subMatrix(A, 1, A.nRows(), 1, A.nCols() - 1);
        return new ImmutableMatrix(X);
    }

    /**
     * (wA' %*% wA)<sup>-1</sup>
     *
     * @return (wA' %*% wA)<sup>-1</sup>
     */
    public ImmutableMatrix invOfwAtwA() {
        if (invOfwAtwA == null) {
            invOfwAtwA = new ImmutableMatrix(new Inverse((wA.t().multiply(wA))));
        }

        return invOfwAtwA;
    }

    /**
     * Check whether this <tt>LMProblem</tt> instance is valid.
     *
     * @throws IllegalArgumentException if this problem is invalid
     */
    protected void checkInputs() {
        assertArgument(y.size() == A.nRows(), "y and A do not have the same number of rows");

        if (weights != null) {
            assertArgument(weights.size() == y.size(), "weights and y do not have the same number of rows");

            for (int i = 1; i <= weights.size(); i++) {
                assertArgument(weights.get(i) >= 0, "all weights must be non negative");
            }
        }
    }
}
