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
package com.numericalmethod.suanshu.matrix.doubles;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.isSquare;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen.Method;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.Hessenberg;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.Hessenberg.Deflation;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.Cholesky;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.number.DoubleUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * These are the boolean operators that take a matrix or a vector and check if it satisfies a certain property.
 *
 * @author Haksun Li
 */
public class IsMatrix {

    private IsMatrix() {
        // private constructor for utility class
    }

    /**
     * Check if a matrix is symmetric.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <blockquote><code>A.t() == A</blockquote></code>
     */
    public static boolean symmetric(Matrix A, double epsilon) {
        if (A instanceof SymmetricMatrix) {
            return true;
        }

        return AreMatrices.equal(A.t(), A, epsilon);
    }

    /**
     * Check if a matrix is skew symmetric.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <blockquote><code>A.t() == -A</blockquote></code>
     */
    public static boolean skewSymmetric(Matrix A, double epsilon) {
        return AreMatrices.equal(A.t(), A.opposite(), epsilon);
    }

    /**
     * Check if a matrix is idempotent.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <blockquote><code>A = A*A or A = A<sup>2</sup></blockquote></code>
     */
    public static boolean idempotent(Matrix A, double epsilon) {
        return AreMatrices.equal(A.t(), A.multiply(A), epsilon);
    }

    /**
     * Check if a matrix is orthogonal, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <blockquote><code>AA' = 1</blockquote></code>
     */
    public static boolean orthogonal(Matrix A, double epsilon) {
        return AreMatrices.equal(A.multiply(A.t()), A.ONE(), epsilon);
    }

    /**
     * Check if a matrix is a magic square.
     * <em>Not yet implemented.</em>
     *
     * @param A a matrix
     * @return {@code true} if <i>A</i> is a square matrix having distinct positive integers,
     * arranged such that the sums of the numbers in any rows, columns, or diagonals are equal
     * @deprecated Not supported yet.
     */
    @Deprecated
    public static boolean magicSquare(Matrix A) {
        SuanShuUtils.assertArgument(isSquare(A), "A must be a square matrix");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Check if a <em>square</em> matrix is singular, i.e, having no inverse, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>A<sup>-1</sup></i> does not exist
     */
    public static boolean singular(Matrix A, double epsilon) {
        SuanShuUtils.assertArgument(isSquare(A), "A must be a square matrix");

        if (A instanceof LowerTriangularMatrix || A instanceof UpperTriangularMatrix) {
            boolean result = true;
            //a triangular matrix is singular if A[i][i] == 0, for any i
            for (int i = 1; i <= A.nRows(); ++i) {
                result &= !equal(A.get(i, i), 0, epsilon);
            }
            //result == true implies none on the diagonal is 0, hence non-singular
            return !result;
        }

        return isZero(MatrixMeasure.det(A), epsilon);
    }

    /**
     * Check if a <em>square</em> matrix is a diagonal matrix, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>A</i> is square and <i>A<sub>i</sub><sub>j</sub>=0</i>, for all <i>i != j</i>.
     */
    public static boolean diagonal(Matrix A, double epsilon) {
        boolean result = A.nRows() == A.nCols();

        if (result) {
            for (int i = 1; i <= A.nRows(); ++i) {
                for (int j = 1; j <= A.nCols(); ++j) {
                    if (i == j) {
                        result &= (compare(A.get(i, j), 0, epsilon) != 0);
                    } else {
                        result &= (compare(A.get(i, j), 0, epsilon) == 0);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Check if a matrix is an identity matrix, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>A</i> is square, and <i>A<sub>i</sub><sub>j</sub>=0</i> for all <i>i != j</i>, and
     * <i>A<sub>i</sub><sub>j</sub>=1</i>, for all <i>i == j</i>.
     */
    public static boolean identity(Matrix A, double epsilon) {
        boolean result = A.nRows() == A.nCols();

        if (result) {
            for (int i = 1; i <= A.nRows(); ++i) {
                for (int j = 1; j <= A.nCols(); ++j) {
                    if (i == j) {
                        result &= (compare(A.get(i, j), 1, epsilon) == 0);
                    } else {
                        result &= (compare(A.get(i, j), 0, epsilon) == 0);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Check if a matrix is upper triangular, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is upper triangular
     */
    public static boolean upperTriangular(Matrix A, double epsilon) {
        if (A instanceof UpperTriangularMatrix) {
            return true;
        }

        for (int i = 2; i <= A.nRows(); ++i) {
            int boundary = Math.min(i, A.nCols());//in case that nRows > nCols
            for (int j = 1; j < boundary; ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if a matrix is lower triangular, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is lower triangular
     */
    public static boolean lowerTriangular(Matrix A, double epsilon) {
        if (A instanceof LowerTriangularMatrix) {
            return true;
        }

        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = i + 1; j <= A.nCols(); ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if a matrix is quasi (upper) triangular, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is quasi (upper) triangular
     */
    public static boolean quasiTriangular(Matrix A, double epsilon) {
        if (A instanceof LowerTriangularMatrix || A instanceof UpperTriangularMatrix) {
            return true;
        }

        Hessenberg hss = new Hessenberg(new Hessenberg.DeflationCriterion() {

            @Override
            public boolean isNegligible(Matrix H, int row, int col, double epsilon) {
                return equal(0, H.get(row, col), epsilon);
            }
        });

        if (!Hessenberg.isHessenberg(A, epsilon)) {
            return false;
        }

        Matrix H = new DenseMatrix(A);//TODO: how to avoid copying? backSearch modifies the input... (is this a desirable effect? if not, backSearch() should do the copying internally)

        Deflation deflation = hss.backSearch(H, H.nRows(), epsilon);
        return deflation.isQuasiTriangular;
    }

    /**
     * Check if a matrix is upper bidiagonal, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is upper bidiagonal
     */
    public static boolean upperBidiagonal(Matrix A, double epsilon) {
        //check the top half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = A.nCols(); j >= i + 2; --j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        //check the bottom half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= i - 1 && j <= A.nCols(); ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if a matrix is lower bidiagonal, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is lower bidiagonal
     */
    public static boolean lowerBidiagonal(Matrix A, double epsilon) {
        //check the bottom half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= i - 2 && j <= A.nCols(); ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        //check the top half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = A.nCols(); j >= i + 1; --j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if a matrix is tridiagonal, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is tridiagonal
     */
    public static boolean tridiagonal(Matrix A, double epsilon) {
        SuanShuUtils.assertArgument(isSquare(A), "A must be a square matrix");

        //check the bottom half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= i - 2; ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        //check the top half
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = A.nCols(); j >= i + 2; --j) {
                if (!equal(0, A.get(i, j), epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if a matrix is in the row echelon form, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is in row echelon form
     * @see <a href="http://en.wikipedia.org/wiki/Reduced_row_echelon_form#Reduced_row_echelon_form">Wikipedia: Row echelon form</a>
     */
    public static boolean rowEchelonForm(Matrix A, double epsilon) {
        int lead = Integer.MIN_VALUE;
        for (int i = 1; i <= A.nRows(); ++i) {
            //check the first non-zero entry in this row
            int j = 1;
            for (; j <= A.nCols(); ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {//first non-zero entry in this row found
                    if (j <= lead) {
                        return false;
                    }
                    lead = j;
                    break;
                }
            }
            if (j > A.nCols()) {//the whole row are 0s
                lead = j;
            }
        }

        return true;
    }

    /**
     * Check if a matrix is in the reduced row echelon form, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the matrix is in reduced row echelon form
     * @see <a href="http://en.wikipedia.org/wiki/Reduced_row_echelon_form#Reduced_row_echelon_form">Wikipedia: Row echelon form</a>
     */
    public static boolean reducedRowEchelonForm(Matrix A, double epsilon) {
        int lead = Integer.MIN_VALUE;
        for (int i = 1; i <= A.nRows(); ++i) {
            //check the first non-zero entry in this row
            int j = 1;
            for (; j <= A.nCols(); ++j) {
                if (!equal(0, A.get(i, j), epsilon)) {//first non-zero entry in this row found
                    if (!equal(1d, A.get(i, j), epsilon) || (j <= lead)) {
                        return false;
                    }

                    //every leading coefficient is 1 and is the only nonzero entry in its column
                    for (int k = i - 1; k >= 1; --k) {
                        if (!equal(0, A.get(k, j), epsilon)) {
                            return false;
                        }
                    }

                    lead = j;
                    break;
                }
            }
            if (j > A.nCols()) {//the whole row are 0s
                lead = j;
            }
        }

        return true;
    }

    /**
     * Check if a <em>square</em> matrix is a scalar matrix, up to a precision.
     *
     * @param A a matrix
     * @return {@code true} if <i>A</i> is diagonal and, <i>a<sub>i</sub><sub>j</sub>=c</i>, for all <i>i == j</i>, <i>c</i> is a constant.
     * @deprecated Not supported yet.
     */
    @Deprecated
    public static boolean scalar(Matrix A, double epsilon) {
        throw new UnsupportedOperationException("Not supported yet.");//TODO: the diagonal elements can be off by a tiny error
    }

    /**
     * Check if a <em>square</em> matrix is symmetric and positive definite.
     *
     * @param A a matrix
     * @return {@code true} if A is symmetric, and <i>z'Mz &gt; 0</i>, for all non-zero real vectors <i>z</i>.
     * @see <a href="http://en.wikipedia.org/wiki/Positive-definite_matrix">Wikipedia: Positive-definite matrix</a>
     */
    public static boolean symmetricPositiveDefinite(Matrix A) {
        try {
            Cholesky cholesky = new Cholesky(A);// a matrix is positive definite if it possesses a Cholesky decomposition
            LowerTriangularMatrix L = cholesky.L();
        } catch (RuntimeException ex) {
            return false;
        }
        return true;
    }

    /**
     * Check if a <em>square</em> matrix is positive definite; the matrix needs not be symmetric.
     * A real matrix <i>A</i> is positive definite if
     * the symmetric part <i>A<sub>symmetric</sub> = 1/2 * (A + A')</i> is symmetric positive definite.
     *
     * @param A a matrix
     * @return {@code true} if <i>A</i> satisfies <i>z'Mz &gt; 0</i>, for all non-zero real vectors <i>z</i>.
     * @see <a href="http://en.wikipedia.org/wiki/Positive-definite_matrix">Wikipedia: Positive-definite matrix</a>
     */
    public static boolean positiveDefinite(Matrix A) {
        Matrix At = A.t();
        Matrix S = A.add(At).scaled(0.5);
        return symmetricPositiveDefinite(S);
    }

    /**
     * Check if a <em>square</em> matrix is positive definite, up to a precision.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>A</i> satisfies <i>z'Mz &gt; 0</i>, for all non-zero real vectors <i>z</i>.
     * @see <a href="http://en.wikipedia.org/wiki/Positive-definite_matrix#Negative-definite.2C_semidefinite_and_indefinite_matrices">Wikipedia: Negative-definite, semidefinite and indefinite matrices</a>
     */
    public static boolean positiveSemiDefinite(Matrix A, double epsilon) {
        if (!symmetric(A, epsilon)) {
            return false;
        }

        Eigen eigen = new Eigen(A, Method.QR, epsilon);
        double[] ev = eigen.getRealEigenvalues();
        double min = DoubleArrayMath.min(ev);

        // A positive semidefinite matrix is a symmetric (Hermitian) matrix all of whose eigenvalues are nonnegative.
        if (min < 0) {
            return false;
        }

        return true;
    }

    /**
     * Check if a vector is a zero vector, i.e., all its entries are 0, up to a precision.
     *
     * @param v       a vector
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>v</i> is a zero vector
     */
    public static boolean zero(Vector v, double epsilon) {
        for (int i = 1; i <= v.size(); ++i) {
            if (!DoubleUtils.equal(v.get(i), 0, epsilon)) {//an entry is != 0
                return false;
            }
        }

        return true;
    }
}
