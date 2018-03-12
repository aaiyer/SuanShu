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
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.CholeskyWang2006;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.multivariate.NormalRvg;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is the state equation in a controlled dynamic linear model.
 * <blockquote><i>
 * x<sub>t</sub> = G<sub>t</sub> * x<sub>t-1</sub> + H<sub>t</sub> * u<sub>t</sub> + w<sub>t</sub>,
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class StateEquation {

    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a (p * p) constant coefficient matrix of x_{t_1} in the state equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs the (p * p) coefficient matrix of x_{t_1} in the state equation.
     */
    private final R1toMatrix G;
    /**
     * For a time-invariant controlled DLM,
     * this represents a (p * m) constant coefficient matrix of u_t (an m-dimensional vector of control variables).
     * <p/>
     * For a time varying controlled DLM,
     * this outputs the initial (p * m) coefficient matrix of u_t (an m-dimensional vector of control variables).
     */
    private final R1toMatrix H;
    /**
     * For a time-invariant DLM (or time-invariant controlled DLM),
     * this represents a (p * p) constant covariance matrix of {w_t} in the state equation.
     * <p/>
     * For a time varying DLM (or time varying controlled DLM),
     * this outputs the initial (p * p) covariance matrix of {w_t} in the state equation.
     */
    private final R1toMatrix W;
    /** the dimension of state x_t */
    private final int p;
    private final NormalRvg rmvnorm;

    /**
     * Construct a state equation.
     *
     * @param G       the coefficient matrix function of <i>x<sub>t - 1</sub></i>
     * @param H       the coefficient matrix function of control variables <i>u<sub>t</sub></i>; use {@code null} if there isn't any
     * @param W       the covariance matrix function of <i>w<sub>t</sub></i>
     * @param rmvnorm a <i>p</i>-dimensional standard multivariate Gaussian random vector generator (for seeding); <i>p</i> = the dimension of <i>W</i> or <i>x<sub>t</sub></i>
     */
    public StateEquation(R1toMatrix G, R1toMatrix H, R1toMatrix W, NormalRvg rmvnorm) {
        this.p = G.evaluate(1).nRows();

        assertArgument((W.evaluate(1).nRows() == p) && (W.evaluate(1).nCols() == p),
                       "the dimension of W is the dimension of each state x_t");
        if (H != null) {
            assertArgument((H.evaluate(1).nRows() == p), "the dimension of W is the dimension of each state x_t");
        }

        this.G = G;
        this.H = H;
        this.W = W;
        this.rmvnorm = rmvnorm == null ? new NormalRvg(p) : rmvnorm;
    }

    /**
     * Construct a state equation.
     *
     * @param G the coefficient matrix function of <i>x<sub>t - 1</sub></i>
     * @param H the coefficient matrix function of control variables <i>u<sub>t</sub></i>; use {@code null} if there isn't any
     * @param W the covariance matrix function of <i>w<sub>t</sub></i>
     */
    public StateEquation(R1toMatrix G, R1toMatrix H, R1toMatrix W) {
        this(G, H, W, null);
    }

    /**
     * Construct a state equation without control variables.
     *
     * @param G the coefficient matrix function of <i>x<sub>t - 1</sub></i>
     * @param W the covariance matrix function of <i>w<sub>t</sub></i>
     */
    public StateEquation(R1toMatrix G, R1toMatrix W) {
        this(G, null, W);
    }

    /**
     * Construct a time-invariant state equation.
     *
     * @param G       the coefficient matrix function of <i>x<sub>t - 1</sub></i>
     * @param H       the coefficient matrix function of control variables <i>u<sub>t</sub></i>; use {@code null} if there isn't any
     * @param W       the covariance matrix function of <i>w<sub>t</sub></i>
     * @param rmvnorm a <i>p</i>-dimensional standard multivariate Gaussian random vector generator; <i>p</i> = the dimension of <i>W</i> or <i>x<sub>t</sub></i>
     */
    public StateEquation(Matrix G, Matrix H, Matrix W, NormalRvg rmvnorm) {
        this(
                new R1toConstantMatrix(G),
                H != null ? new R1toConstantMatrix(H) : null,
                new R1toConstantMatrix(W),
                rmvnorm);
    }

    /**
     * Construct a time-invariant state equation without control variables.
     *
     * @param G the coefficient matrix function of <i>x<sub>t - 1</sub></i>
     * @param W the covariance matrix function of <i>w<sub>t</sub></i>
     */
    public StateEquation(Matrix G, Matrix W) {
        this(G, null, W, null);
    }

    /**
     * Construct a multivariate state equation from a univariate state equation.
     *
     * @param states a univariate state equation
     */
    public StateEquation(final com.numericalmethod.suanshu.stats.dlm.univariate.StateEquation states) {
        this(
                new R1toMatrix() {

                    @Override
                    public Matrix evaluate(double t) {
                        return new DenseMatrix(new double[][]{{states.G((int) t)}});
                    }
                },
                new R1toMatrix() {

                    @Override
                    public Matrix evaluate(double t) {
                        return new DenseMatrix(new double[][]{{states.H((int) t)}});
                    }
                },
                new R1toMatrix() {

                    @Override
                    public Matrix evaluate(double t) {
                        return new DenseMatrix(new double[][]{{states.W((int) t)}});
                    }
                });
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code StateEquation}
     */
    public StateEquation(StateEquation that) {
        this(that.G, that.H, that.W);
    }

    /**
     * Get the dimension of state <i>x<sub>t</sub></i>.
     *
     * @return the dimension of states
     */
    public int dimension() {
        return p;
    }

    /**
     * Get <i>G(t)</i>, the coefficient matrix of <i>x<sub>t - 1</sub></i>.
     *
     * @param t time
     * @return <i>G(t)</i>
     */
    public Matrix G(int t) {
        return G.evaluate(t);
    }

    /**
     * Get <i>H(t)</i>, the covariance matrix of <i>u<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>H(t)</i>
     */
    public Matrix H(int t) {
        return H != null ? H.evaluate(t) : null;
    }

    /**
     * Get <i>W(t)</i>, the covariance matrix of <i>w<sub>t</sub></i>.
     *
     * @param t time
     * @return <i>W(t)</i>
     */
    public Matrix W(int t) {
        return W.evaluate(t);
    }

    /**
     * Predict the next state.
     * <blockquote><i>
     * E(x_t) = G_t * x_{t - 1} + H_t * u_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @param ut   the control variable <i>u<sub>t</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public Vector xt_mean(int t, Vector xt_1, Vector ut) {
        Vector xt = G.evaluate(t).multiply(xt_1);

        if (H != null) {
            Vector Hu = H.evaluate(t).multiply(ut);
            xt = xt.add(Hu);
        }

        return xt;
    }

    /**
     * Predict the next state without control variable.
     * <blockquote><i>
     * E(x_t) = G_t * x_{t - 1} + H_t * u_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public Vector xt_mean(int t, Vector xt_1) {
        return xt_mean(t, xt_1, null);
    }

    /**
     * Get the variance of the apriori prediction for the next state.
     * <blockquote><i>
     * Var(x_{t | t - 1}) = G_t * Var(x_{t - 1| t - 1}) * G_t' + W_t
     * </i></blockquote>
     *
     * @param t             time
     * @param var_tlag_tlag <i>Var(x_{t - 1 | t - 1})</i>, the covariance of the posterior update
     * @return <i>Var(x_{t | t - 1})</i>
     */
    public ImmutableMatrix xt_var(int t, Matrix var_tlag_tlag) {
        Matrix G = G(t);
        Matrix W = W(t);
        Matrix var = new CongruentMatrix(G.t(), var_tlag_tlag).add(W);
        return new ImmutableMatrix(var);
    }

    /**
     * Evaluate the state equation.
     * <blockquote><i>
     * x_t = G_t * x_{t - 1} + H_t * u_t + w_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @param ut   the control variable <i>u<sub>t</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public ImmutableVector xt(int t, Vector xt_1, Vector ut) {
        Vector xt = xt_mean(t, xt_1, ut);

        Matrix Wt = W.evaluate(t);
        Matrix A = new CholeskyWang2006(Wt, SuanShuUtils.autoEpsilon(MatrixUtils.to1DArray(Wt)));
        Vector wt = A.multiply(new DenseVector(rmvnorm.nextVector()));

        xt = xt.add(wt);
        return new ImmutableVector(xt);
    }

    /**
     * Evaluate the state equation without the control variable.
     * <blockquote><i>
     * x_t = G_t * x_{t - 1} + H_t * u_t + w_t
     * </i></blockquote>
     *
     * @param t    time
     * @param xt_1 x lag <i>x<sub>t - 1</sub></i>
     * @return <i>x<sub>t</sub></i>
     */
    public ImmutableVector xt(int t, Vector xt_1) {
        assertArgument(H == null, "H is not null and thus it requires the control variable");
        return xt(t, xt_1, null);
    }
}
