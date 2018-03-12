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
package com.numericalmethod.suanshu.stats.dlm.multivariate;

import com.numericalmethod.suanshu.analysis.function.matrix.R1toConstantMatrix;
import com.numericalmethod.suanshu.analysis.function.matrix.R1toMatrix;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.matrix.doubles.operation.SimilarMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.CholeskyWang2006;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.multivariate.NormalRvg;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is the observation equation in a controlled dynamic linear model.
 * <blockquote><i>
 * y<sub>t</sub> = F<sub>t</sub> * x<sub>t</sub> + v<sub>t</sub>
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class ObservationEquation {

    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a (d * p) constant coefficient matrix of x_t in the observation equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs a time dependent (d * p) coefficient matrix of x_t in the observation equation.
     */
    private final R1toMatrix F;
    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a (d * d) constant covariance matrix of v_t in the observation equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs a time dependent (d * d) covariance matrix of v_t in the observation equation.
     */
    private final R1toMatrix V;
    /**
     * the dimension of each observation y_t
     */
    private final int d;
    private final NormalRvg rmvnorm;

    /**
     * Construct an observation equation.
     *
     * @param F       the coefficient matrix function of <i>x<sub>t</sub></i>, a function of time
     * @param V       the covariance matrix function of <i>v<sub>t</sub></i>, a function of time
     * @param rmvnorm a <i>d</i>-dimensional standard multivariate Gaussian random vector generator (for seeding); <i>d</i> = the dimension of <i>V</i> or <i>y<sub>t</sub></i>
     */
    public ObservationEquation(R1toMatrix F, R1toMatrix V, NormalRvg rmvnorm) {
        this.d = F.evaluate(1).nRows();
        assertArgument((V.evaluate(1).nRows() == d) && (V.evaluate(1).nCols() == d),
                       "the dimension of V is the same as the dimension of observation y_t");

        this.F = F;
        this.V = V;
        this.rmvnorm = rmvnorm == null ? new NormalRvg(d) : rmvnorm;
    }

    /**
     * Construct an observation equation.
     *
     * @param F the coefficient matrix function of <i>x<sub>t</sub></i>, a function of time
     * @param V the covariance matrix function of <i>v<sub>t</sub></i>, a function of time
     */
    public ObservationEquation(R1toMatrix F, R1toMatrix V) {
        this(F, V, null);
    }

    /**
     * Construct a time-invariant an observation equation.
     *
     * @param F       the coefficient matrix of <i>x<sub>t</sub></i>
     * @param V       the covariance matrix of <i>v<sub>t</sub></i>
     * @param rmvnorm a <i>d</i>-dimensional standard multivariate Gaussian random vector generator (for seeding); <i>d</i> = the dimension of <i>V</i> or <i>y<sub>t</sub></i>
     */
    public ObservationEquation(Matrix F, Matrix V, NormalRvg rmvnorm) {
        this(new R1toConstantMatrix(F), new R1toConstantMatrix(V), rmvnorm);
    }

    /**
     * Construct a time-invariant an observation equation.
     *
     * @param F the coefficient matrix of <i>x<sub>t</sub></i>
     * @param V the covariance matrix of <i>v<sub>t</sub></i>
     */
    public ObservationEquation(Matrix F, Matrix V) {
        this(new R1toConstantMatrix(F), new R1toConstantMatrix(V), null);
    }

    /**
     * Construct a multivariate observation equation from a univariate observation equation.
     *
     * @param obs a univariate observation equation
     */
    public ObservationEquation(final com.numericalmethod.suanshu.stats.dlm.univariate.ObservationEquation obs) {
        this(
                new R1toMatrix() {

                    @Override
                    public Matrix evaluate(double t) {
                        return new DenseMatrix(new double[][]{{obs.F((int) t)}});
                    }
                },
                new R1toMatrix() {

                    @Override
                    public Matrix evaluate(double t) {
                        return new DenseMatrix(new double[][]{{obs.V((int) t)}});
                    }
                });
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code ObservationEquation}
     */
    public ObservationEquation(ObservationEquation that) {
        this(that.F, that.V);
    }

    /**
     * Get the dimension of observation <i>y<sub>t</sub></i>.
     *
     * @return the dimension of observations
     */
    public int dimension() {
        return d;
    }

    /**
     * Get <i>F(t)</i>, the coefficient matrix of <i>x<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>F(t)</i>
     */
    public ImmutableMatrix F(int t) {
        return new ImmutableMatrix(F.evaluate(t));
    }

    /**
     * Get <i>V(t)</i>, the covariance matrix of <i>v<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>V(t)</i>
     */
    public ImmutableMatrix V(int t) {
        return new ImmutableMatrix(V.evaluate(t));
    }

    /**
     * Predict the next observation.
     * <blockquote><i>
     * E(y_t) = F_t * x_t
     * </i></blockquote>
     *
     * @param t  time
     * @param xt state <i>x<sub>t</sub></i>
     * @return the mean observation
     */
    public ImmutableVector yt_mean(int t, Vector xt) {
        Vector yt = F.evaluate(t).multiply(xt);
        return new ImmutableVector(yt);
    }

    /**
     * Get the covariance of the apriori prediction for the next observation.
     * <blockquote><i>
     * Var(y_{t | t - 1}) = F_t * Var(x_{t | t - 1}) * F_t' + V_t
     * </i></blockquote>
     *
     * @param t          time
     * @param var_t_tlag <i>Var(y_{t | t - 1})</i>, the variance of the apriori prediction
     * @return <i>Var(y_{t | t - 1})</i>
     */
    public ImmutableMatrix yt_var(int t, Matrix var_t_tlag) {
        Matrix F = F(t);
        Matrix V = V(t);
        Matrix var = new SimilarMatrix(F.t(), var_t_tlag).add(V);
        return new ImmutableMatrix(var);
    }

    /**
     * Evaluate the observation equation.
     * <blockquote><i>
     * y_t = F_t * x_t + v_t
     * </i></blockquote>
     *
     * @param t  time
     * @param xt state <i>x<sub>t</sub></i>
     * @return the mean observation
     */
    public ImmutableVector yt(int t, Vector xt) {
        Vector yt = yt_mean(t, xt);

        Matrix Vt = V.evaluate(t);
        Matrix A = new CholeskyWang2006(Vt, SuanShuUtils.autoEpsilon(MatrixUtils.to1DArray(Vt)));
        Vector vt = A.multiply(new DenseVector(rmvnorm.nextVector()));

        yt = yt.add(vt);
        return new ImmutableVector(yt);
    }
}
