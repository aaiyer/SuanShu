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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.SparseVector;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.Basis;
import java.util.ArrayList;
import java.util.List;

/**
 * A Householder transformation in the 3-dimensional space is the reflection of a vector in the plane.
 * The plane, containing the origin, is uniquely defined by a unit vector, called the generator, orthogonal to the plane.
 * The reflection <i>Hx</i> can be computed without explicitly expanding <i>H</i>.
 * <blockquote><pre><i>
 * H = I - 2vv'
 * Hx = (I - 2vv')x = Ix - 2vv'x = x - 2v&lt;v,x&gt;, where x is a column vector
 * yH = (H'y')' = (Hy')', where y is a row vector
 * </i></pre></blockquote>
 * When <i>H</i> is applied to a set of column vectors, it transforms each vector individually, as in block matrix multiplication.
 * <blockquote><pre><i>
 * H * A = H * [A<sub>1</sub> A<sub>2</sub> ... A<sub>n</sub>] = [H * A<sub>1</sub> H * A<sub>2</sub> ... H * A<sub>n</sub>]
 * </i></pre></blockquote>
 * When <i>H</i> is applied to a set of row vectors, it transforms each vector individually, as in block matrix multiplication.
 * \[
 * AH = \begin{bmatrix}
 * A_1H\\
 * A_2H\\
 * A_3H\\
 * A_4H\\
 * A_5H
 * \end{bmatrix}
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Householder_reflection">Wikipedia: Householder transformation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Householder_operator">Wikipedia: Householder operator</a>
 * </ul>
 */
public class Householder {

    /** the vector that defines the Householder matrix */
    private final Vector v4H;

    /**
     * Construct a Householder matrix from the vector that defines the hyperplane orthogonal to the vector.
     * If the generator is a 0-vector, the Householder matrix is the identity matrix.
     *
     * @param generator the hyperplane defining vector
     */
    public Householder(Vector generator) {
        double norm = generator.norm();

        if (isZero(norm, 0)) {
            this.v4H = new SparseVector(generator.size());//0 vector
        } else {
            //We store the unit version of the hyperplane defining vector.
            this.v4H = generator.scaled(1d / norm);
        }
    }

    /**
     * Get the Householder generating vector.
     *
     * @return the Householder generating vector
     */
    public Vector generator() {
        return v4H.deepCopy();
    }

    /**
     * Apply the Householder matrix, <i>H</i>, to a column vector, <i>x</i>.
     * <blockquote><pre><i>
     * Hx = x - 2 * &lt;v,x&gt; * v
     * </i></pre></blockquote>
     *
     * @param x a vector
     * @return <i>Hx</i>
     */
    public Vector reflect(Vector x) {
        Vector result = x.minus(v4H.scaled(2 * x.innerProduct(v4H)));
        return result;
    }

    /**
     * Apply the Householder matrix, <i>H</i>, to a matrix (a set of column vectors), <i>A</i>.
     * <blockquote><pre><i>
     * H * A = [H * A<sub>1</sub> H * A<sub>2</sub> ... H * A<sub>n</sub>]
     * </i></pre></blockquote>
     *
     * @param A a matrix
     * @return <i>H * A</i>
     */
    public Matrix reflect(Matrix A) {
        List<Vector> vectors = new ArrayList<Vector>(A.nCols());
        for (int i = 1; i <= A.nCols(); ++i) {
            Vector v = A.getColumn(i);
            vectors.add(this.reflect(v));
        }

        return CreateMatrix.cbind(vectors);
    }

    /**
     * Apply the Householder matrix, <i>H</i>, to a matrix (a set of row vectors), <i>A</i>.
     * \[
     * AH = \begin{bmatrix}
     * A_1H\\
     * A_2H\\
     * A_3H\\
     * A_4H\\
     * A_5H
     * \end{bmatrix}
     * \]
     *
     * @param A a matrix
     * @return <i>A * H</i>
     */
    public Matrix reflectRows(Matrix A) {
        List<Vector> vectors = new ArrayList<Vector>(A.nRows());
        for (int i = 1; i <= A.nRows(); ++i) {
            Vector v = A.getRow(i);
            vectors.add(this.reflect(v));
        }

        return CreateMatrix.rbind(vectors);
    }

    /**
     * Get the Householder matrix <i>H = I - 2 * v * v'</i>.
     * To compute <i>H *v</i>, do not use this method. Instead use {@link #reflect(Vector)}.
     *
     * @return the Householder matrix
     */
    public Matrix H() {
        Matrix I = new DenseMatrix(v4H.size(), v4H.size()).ONE();
        Matrix V = new DenseMatrix(v4H.toArray(), v4H.size(), 1);
        Matrix H = I.minus(V.multiply(V.t()).scaled(2));
        return H;
    }

    /**
     * Compute <i>Q</i> from Householder matrices <i>{Q<sub>i</sub>}</i>.
     * <blockquote><pre><i>
     * Q = Q<sub>1</sub> * Q<sub>2</sub> * ... * Q<sub>n</sub> * I
     * </i></pre></blockquote>
     * The identity matrix, <i>I</i>, may have more rows than columns.
     * The bottom rows are padded with zeros.
     * <p/>
     * This implementation use an efficient way to compute <i>Q</i>, i.e.,
     * by applying <i>Q<sub>i</sub></i>'s repeatedly on an identity matrix.
     *
     * @param Hs    an array of Householders
     * @param from  the beginning index of <i>H</i>'s; <i>Q<sub>1</sub></i> is <i>Q<sub>from</sub></i>
     * @param to    the ending index of <i>H</i>'s; <i>Q<sub>n</sub></i> is <i>Q<sub>to</sub></i>
     * @param nRows the number of rows of <i>I</i>
     * @param nCols the number of columns of <i>I</i>
     * @return the product of an array of Householder matrices, from {@code Hs[from]} to {@code Hs[to]}
     */
    public static Matrix product(Householder[] Hs, int from, int to, int nRows, int nCols) {//TODO: write test case for from/to, where from < to and from > to, and ==
//        DenseMatrix Q = new DenseMatrix(nRows, nCols).ONE();
//
//        for (int i = nCols; i > 0; --i) {
//            if (Hs[i] != null) {
//                Q = Hs[i].reflect(Q);
//            }
//        }

        /*
         * Faster implementation
         * by skipping copying and passing the whole matrices
         * at each reflection
         */
        List<Vector> cols4Q = Basis.getBasis(nRows, nCols);
        int[] indices = R.seq(to, from);//from 'to' to 'from', i.e., backward

        for (int i : indices) { //  for (int i = to; i >= from; --i) {
            int j = i;
            if (Hs[j] != null) {
                for (int k = 0; k < nCols; ++k) {
                    cols4Q.set(k, Hs[j].reflect(cols4Q.get(k)));//TODO: to be parallelized
                }
            }
        }

        Matrix Q = CreateMatrix.cbind(cols4Q);
        return Q;
    }

    /**
     * Compute <i>Q</i> from Householder matrices <i>{Q<sub>i</sub>}</i>.
     * <blockquote><pre><i>
     * Q = Q<sub>1</sub> * Q<sub>2</sub> * ... * Q<sub>n</sub> * I
     * </i></pre></blockquote>
     * This implementation use an efficient way to compute <i>Q</i>, i.e.,
     * by applying <i>Q<sub>i</sub></i>'s repeatedly on an identity matrix.
     *
     * @param Hs   an array of Householders
     * @param from the beginning index of <i>H</i>'s; <i>Q<sub>1</sub></i> is <i>Q<sub>from</sub></i>
     * @param to   the ending index of <i>H</i>'s; <i>Q<sub>n</sub></i> is <i>Q<sub>to</sub></i>
     * @return the product of an array of Householder matrices, from {@code Hs[from]} to {@code Hs[to]}
     */
    public static Matrix product(Householder[] Hs, int from, int to) {
        int dim = Hs[from].generator().size();
        return product(Hs, from, to, dim, dim);
    }

    /**
     * This is the context information about a Householder transformation.
     * It tells
     * <ol>
     * <li><i>v</i>, the defining vector which is perpendicular to the Householder hyperplane;
     * <li>λ, the norm of <i>v</i> with the sign chosen to be the opposite of the first coordinate of <i>v</i>
     * </ol>
     */
    public static class Context {

        /**
         * the defining vector which is perpendicular to the Householder hyperplane
         */
        public final Vector generator;
        /**
         * the norm of <i>v</i> with the sign chosen to be the opposite of the first coordinate of <i>v</i>
         */
        public final double lambda;

        /**
         * Construct a Householder context information.
         *
         * @param v      the defining vector which is perpendicular to the Householder hyperplane
         * @param lambda λ, the -ve. norm of <i>v</i>
         */
        Context(Vector v, double lambda) {
            this.generator = v;
            this.lambda = lambda;
        }

        @Override
        public String toString() {
            return String.format("λ=%f; [%s]", lambda, generator.toString());
        }
    }

    /**
     * Generate the context information from a generating vector <i>x</i>.
     * Given a vector <i>x</i>, return a vector generator, such that
     * <blockquote><pre><i>
     * Hx = ±||x|| * e<sub>1</sub>
     * </i></pre></blockquote>
     * That is,
     * <blockquote><pre><code>
     * H.reflect(x) == new Vector(new double[]{±x.norm(), 0, ...})
     * </code></pre></blockquote>
     *
     * @param x a vector
     * @return the context information for a Householder transformation
     * @see "G. W. Steward, "Algorithm 1.1," Matrix Algorithms, Volume 1"
     */
    public static Context getContext(Vector x) {
        Vector v = new SparseVector(x.size());//0 vector
        double norm = x.norm();

        /*
         * For numerical stability,
         * the sign of a is chosen to be opposite to the sign of the first entry in the column.
         */
        double lambda = x.get(1) > 0 ? -norm : norm;

        if (!isZero(lambda, 0)) {//if a is 0, then this column has already 0 in the entries below the i-th entry
            Basis e = new Basis(x.size(), 1);//basis vector with '1' in the first entry and all else '0's
            v = x.minus(e.scaled(lambda));//v = x - λ * e
        }

        return new Context(v, lambda);
    }
}
