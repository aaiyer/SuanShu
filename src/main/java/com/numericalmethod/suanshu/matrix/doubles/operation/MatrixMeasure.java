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

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.isSquare;
import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LU;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * A measure, &mu;, of a matrix, <i>A</i>, is a map from the Matrix space to the Real line.
 * That is,
 * <blockquote><i>
 * &mu;: A -> R
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class MatrixMeasure {

    /**
     * Compute the <em>numerical</em> rank of a matrix.
     * Ignore all singular values smaller than a threshold.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return the numerical rank of <i>A</i>
     * @see <a href="http://en.wikipedia.org/wiki/Rank_(linear_algebra)">Wikipedia: Rank (linear algebra)</a>
     */
    public static int rank(Matrix A, double epsilon) {
        SVD svd = new SVD(A, false, epsilon);
        double[] sv = svd.getSingularValues();

        int result = 0;
        for (int i = 0; i < Math.min(A.nRows(), A.nCols()); ++i) {
            if (sv[i] > epsilon) {
                result += 1;
            }
        }

        return result;
    }

    /**
     * Compute the <em>numerical</em> rank of a matrix.
     * Ignore all singular values less than a threshold.
     *
     * @param A a matrix
     * @return the numerical rank of <i>A</i>
     */
    public static int rank(Matrix A) {
        return rank(A, SuanShuUtils.autoEpsilon(A));//TODO: the casting is unnecessary and is inefficient
    }

    /**
     * Compute the nullity of a matrix.
     *
     * @param A a matrix
     * @return the nullity of <i>A</i>
     * @see <a href="http://en.wikipedia.org/wiki/Rank-nullity_theorem">Wikipedia: Rank–nullity theorem</a>
     * @deprecated Not supported yet.
     */
    @Deprecated
    public static int nullity(Matrix A) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Compute the determinant of a matrix.
     *
     * @param A a matrix
     * @return the determinant of <i>A</i>
     * @see <a href="http://en.wikipedia.org/wiki/Determinant">Wikipedia: Determinant</a>
     */
    public static double det(Matrix A) {
        SuanShuUtils.assertArgument(isSquare(A), "only a square matrix has determinant");

        double det = 0;

        if (A.nRows() == 1) {
            det = A.get(1, 1);
        } else if (A.nRows() == 2) {
            det = A.get(1, 1) * A.get(2, 2) - A.get(2, 1) * A.get(1, 2);
        } else if (A.nRows() == 3) {
            det =
                    A.get(1, 1) * A.get(2, 2) * A.get(3, 3)
                    - A.get(1, 1) * A.get(2, 3) * A.get(3, 2)
                    - A.get(1, 2) * A.get(2, 1) * A.get(3, 3)
                    + A.get(1, 2) * A.get(2, 3) * A.get(3, 1)
                    + A.get(1, 3) * A.get(2, 1) * A.get(3, 2)
                    - A.get(1, 3) * A.get(2, 2) * A.get(3, 1);
        } else {
            try {
                LU lu = new LU(A);
                det = lu.P().sign();
                UpperTriangularMatrix U = lu.U();

                for (int i = 1; i <= A.nRows(); ++i) {
                    det *= U.get(i, i);
                }
            } catch (MatrixSingularityException ex) {
                det = 0;
            }
        }

        return det;
    }

    /**
     * Compute the sum of the diagonal elements, i.e., the trace of a matrix.
     *
     * @param A a matrix
     * @return the trace of <i>A</i>
     * @see <a href="http://en.wikipedia.org/wiki/Trace_(linear_algebra)">Wikipedia: Trace (linear algebra)</a>
     */
    public static double tr(Matrix A) {
        SuanShuUtils.assertArgument(isSquare(A), "trace applies only to square matrix");

        double result = 0;
        for (int i = 1; i <= A.nRows(); ++i) {
            result += A.get(i, i);
        }

        return result;
    }

    /**
     * Compute the Frobenius norm, i.e., the sqrt of the sum of squares of all elements of a matrix.
     *
     * @param A a matrix
     * @return the sqrt of sum of squares of all elements in <i>A</i>
     * @see <a href="http://en.wikipedia.org/wiki/Matrix_norm#Frobenius_norm">Wikipedia: Frobenius norm</a>
     */
    public static double Frobenius(Matrix A) {
        double result = 0;
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= A.nCols(); ++j) {
                double Mij = A.get(i, j);
                result += Mij * Mij;
            }
        }

        return Math.sqrt(result);
    }

    /**
     * Compute the maximal entry in a matrix.
     *
     * @param A a matrix
     * @return the maximal entry
     */
    public static double max(Matrix A) {
        double max = Double.MIN_VALUE;
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= A.nCols(); ++j) {
                if (A.get(i, j) > max) {
                    max = A.get(i, j);
                }
            }
        }

        return max;
    }

    /**
     * Compute the minimal entry in a matrix.
     *
     * @param A a matrix
     * @return the minimal entry
     */
    public static double min(Matrix A) {
        double min = Double.MAX_VALUE;
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= A.nCols(); ++j) {
                if (A.get(i, j) < min) {
                    min = A.get(i, j);
                }
            }
        }

        return min;
    }
}
