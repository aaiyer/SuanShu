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

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;

/**
 * The Moore–Penrose pseudo-inverse of an <i>m x n</i> matrix <i>A</i> is <i>A<sup>+</sup></i>.
 * It is a generalization of the inverse matrix.
 * This implementation uses the Singular Value decomposition to compute the pseudo-inverse. Specifically,
 * <blockquote><pre><i>
 * A = U * D * V'
 * A<sup>+</sup> = V * D<sup>+</sup> * U'
 * </i></pre></blockquote>
 * The properties are
 * <ul>
 * <li><i>A<sup>+</sup></i> has the dimension of <i>n x m</i>.
 * <li><i>A * A<sup>+</sup> * A = A</i>
 * <li><i>A<sup>+</sup> * A * A<sup>+</sup> = A<sup>+</sup></i>
 * <li><i>(A * A<sup>+</sup>)' = A * A<sup>+</sup></i>
 * <li><i>(A<sup>+</sup> * A)' = A<sup>+</sup> * A</i>
 * </ul>
 * When <i>A</i> is invertible, its pseudo-inverse coincides with its inverse.
 *
 * @author Haksun Li
 * @see SVD
 * @see <a href="http://en.wikipedia.org/wiki/Moore-Penrose_pseudo-inverse">Wikipedia: Moore–Penrose pseudo-inverse</a>
 */
public class PseudoInverse extends DenseMatrix {

    /**
     * Construct the Moore–Penrose pseudo-inverse matrix of a matrix.
     *
     * @param A       an <i>m x n</i> matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0.
     * This threshold truncates negligible singular values less than the smaller of ε and (t = machine_ε * max(m,n) * max(D)).
     * @see <a href="http://en.wikipedia.org/wiki/Moore-Penrose_pseudo-inverse#The_general_case_and_the_SVD_method">Wikipedia: The general case and the SVD method</a>
     */
    public PseudoInverse(Matrix A, double epsilon) {
        super(Ainv(A, epsilon));
    }

    private static Matrix Ainv(Matrix A, double epsilon) {
        SVD svd = new SVD(A, true, epsilon);
        DiagonalMatrix diag = svd.D();

        double precision = Math.min(getPrecision(A, diag), epsilon);

        for (int i = 1; i <= diag.nRows(); ++i) {
            double d = diag.get(i, i);

            if (compare(d, 0, precision) > 0) {//d > this.precision
                diag.set(i, i, 1d / d);//taking the reciprocal of each non-zero element
            } else {
                diag.set(i, i, 0);//leaving the isAllZeros in place
            }
        }

        Matrix Ainv = svd.V().multiply(diag).multiply(svd.Ut());
        return Ainv;
    }

    /**
     * Construct the Moore–Penrose pseudo-inverse matrix of <i>A</i>.
     *
     * @param A an <i>m x n</i> matrix
     */
    public PseudoInverse(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Compute the threshold smaller than which the singular values are ignored.
     *
     * @param A the matrix to take the inverse with
     * @param D the singular values of <i>A</i>
     * @return the precision
     * @see <a href="http://en.wikipedia.org/wiki/Moore-Penrose_pseudo-inverse#The_general_case_and_the_SVD_method">Wikipedia: The general case and the SVD method</a>
     */
    private static double getPrecision(Matrix A, DiagonalMatrix D) {
        double m1 = DoubleArrayMath.max(D.getDiagonal().toArray());
        double m2 = Math.max(A.nRows(), A.nCols());
        double t = EPSILON * m1 * m2;
        return t;
    }
}
