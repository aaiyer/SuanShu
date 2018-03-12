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

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.subVector;
import java.util.ArrayList;
import java.util.List;

/**
 * These are the utility functions to create a new matrix/vector from existing ones.
 *
 * @author Haksun Li
 */
public class CreateMatrix {

    private CreateMatrix() {
        // no constructor for utility class
    }

    /**
     * Combine an array of vectors by columns.
     * The vectors must have the same length.
     *
     * @param vectors an array of vectors, e.g., <i>v<sub>1</sub>, v<sub>2</sub>, v<sub>3</sub>, ...</i>
     * @return <i>[v<sub>1</sub> v<sub>2</sub> v<sub>3</sub> ...]</i>
     */
    public static DenseMatrix cbind(Vector... vectors) {//TODO: make this faster by using array operations?
//        int nCols = vectors.length;
//        int nRows = vectors[0].length;
//        DenseMatrix A = new DenseMatrix(nRows, nCols);
//
//        for (int i = 1; i <= nCols; ++i) {
//            for (int j = 1; j <= nRows; ++j) {
//                Vector v = vectors[i - 1];
//
//                if (v == null) {//bind a 0 column
//                    A.set(j, i, 0);
//                } else if (v.length != nRows) {//v != null
//                    throw new IllegalArgumentException("vectors is a jagged array");
//                } else {
//                    A.set(j, i, v.get(j));
//                }
//            }
//        }

        //This implementation may be faster for big matrices, by skipping the many A.set and v.get.
        DenseMatrix A = rbind(vectors);
        A = A.t();

        return A;
    }

    /**
     * Combine a list of vectors by columns.
     * The vectors must have the same length.
     *
     * @param vectors a list of vectors, e.g., <i>v<sub>1</sub>, v<sub>2</sub>, v<sub>3</sub>, ...</i>
     * @return <i>[v<sub>1</sub> v<sub>2</sub> v<sub>3</sub> ...]</i>
     */
    public static DenseMatrix cbind(List<Vector> vectors) {
        return cbind(vectors.toArray(new Vector[0]));
    }

    /**
     * Combine an array of matrices by columns.
     * The matrices must have the same number of rows.
     *
     * @param matrices an array of matrices, e.g., <i>A<sub>1</sub>, A<sub>2</sub>, A<sub>3</sub>, ...</i>
     * @return <i>[A<sub>1</sub> A<sub>2</sub> A<sub>3</sub> ...]</i>
     */
    public static DenseMatrix cbind(Matrix... matrices) {
        List<Vector> cols = new ArrayList<Vector>();

        for (Matrix m : matrices) {
            if (m != null && m.nCols() > 0) {
                for (int i = 1; i <= m.nCols(); ++i) {
                    cols.add(m.getColumn(i));
                }
            }
        }

        DenseMatrix result = cbind(cols);
        return result;
    }

//    /**
//     * Combine an array of matrices by columns.
//     * The matrices must have the same number of rows.
//     *
//     * @param matrices an array of matrices, e.g., <i>A<sub>1</sub>, A<sub>2</sub>, A<sub>3</sub>, ...</i>
//     * @return <i>[A<sub>1</sub> A<sub>2</sub> A<sub>3</sub> ...]</i>
//     */
//    public static DenseMatrix cbind(List<Matrix> matrices) {
//        return cbind(matrices.toArray(new Matrix[0]));
//    }
    /**
     * Combine an array of vectors by rows.
     * The vectors must have the same length.
     *
     * @param vectors an array of vector, e.g., <i>v<sub>1</sub>, v<sub>2</sub>, v<sub>3</sub>, ...</i>
     * @return
     * \[
     * \begin{bmatrix}
     * v_1\\
     * v_2\\
     * v_3\\
     * ...
     * \end{bmatrix}
     * \]
     * @throws IllegalArgumentException if the vectors form a jagged array
     */
    public static DenseMatrix rbind(Vector... vectors) {//All cbind and rbind ultimately call this method.
        int nCols = vectors[0].size();
        int nRows = 0;
        for (Vector v : vectors) {//count the number of non-null rows
            if (v != null) {
                ++nRows;
            }
        }

        double[][] data = new double[nRows][];
        for (int i = 0; i < nRows; ++i) {
            Vector v = vectors[i];
            if (v != null) {//ignore the null
                if (v.size() != nCols) {
                    throw new IllegalArgumentException("vectors is a jagged array");
                }
                data[i] = v.toArray();
            }
        }

        return new DenseMatrix(data);
    }

    /**
     * Combine a list of array of vectors by rows.
     * The vectors must have the same length.
     *
     * @param vectors a list of vector, e.g., <i>v<sub>1</sub>, v<sub>2</sub>, v<sub>3</sub>, ...</i>
     * @return
     * \[
     * \begin{bmatrix}
     * v_1\\
     * v_2\\
     * v_3\\
     * ...
     * \end{bmatrix}
     * \]
     * @throws IllegalArgumentException if the vectors form a jagged array
     */
    public static DenseMatrix rbind(List<Vector> vectors) {
        int nrows = vectors.size();
        Vector[] vectorArray = new Vector[nrows];
        for (int i = 0; i < nrows; ++i) {
            vectorArray[i] = vectors.get(i);
        }

        return rbind(vectorArray);
    }

    /**
     * Combine an array of matrices by rows.
     * The matrices must have the same number of columns.
     *
     * @param matrices an array of matrices, e.g., <i>A<sub>1</sub>, A<sub>2</sub>, A<sub>3</sub>, ...</i>
     * @return
     * \[
     * \begin{bmatrix}
     * A_1\\
     * A_2\\
     * A_3\\
     * ...
     * \end{bmatrix}
     * \]
     */
    public static DenseMatrix rbind(Matrix... matrices) {
        List<Vector> rows = new ArrayList<Vector>();

        for (Matrix m : matrices) {
            if (m != null && m.nRows() > 0) {
                for (int i = 1; i <= m.nRows(); ++i) {
                    rows.add(m.getRow(i));
                }
            }
        }

        DenseMatrix result = rbind(rows);
        return result;
    }

//    /**
//     * Combine a list of matrices by rows.
//     * The matrices must have the same number of columns.
//     *
//     * @param matrices a list of matrices, e.g., <i>A<sub>1</sub>, A<sub>2</sub>, A<sub>3</sub>, ...</i>
//     * @return
//     * \[
//     * \begin{bmatrix}
//     * A_1\\
//     * A_2\\
//     * A_3\\
//     * ...
//     * \end{bmatrix}
//     * \]
//     */
//    public static DenseMatrix rbind(List<Matrix> matrices) {
//        return rbind(matrices.toArray(new Matrix[0]));
//    }
    /**
     * Construct a sub-matrix from the four corners of a matrix.
     *
     * @param A       a matrix
     * @param rowFrom the beginning row index
     * @param rowTo   the ending row index
     * @param colFrom the beginning column index
     * @param colTo   the ending column index
     * @return <i>A[rowFrom:rowTo, colFrom:colTo]</i>
     */
    public static DenseMatrix subMatrix(Matrix A, int rowFrom, int rowTo, int colFrom, int colTo) {
        List<Vector> rows = new ArrayList<Vector>();
        for (int i = rowFrom; i <= rowTo; ++i) {
            Vector row = subVector(A.getRow(i), colFrom, colTo);
            rows.add(row);
        }

        return rbind(rows);
    }

    /**
     * Construct a sub-matrix from the intersections of rows and columns of a matrix.
     *
     * @param A    a matrix
     * @param rows the rows to be extracted
     * @param cols the columns to be extracted
     * @return <i>A[rows, cols]</i>
     */
    public static DenseMatrix subMatrix(Matrix A, int[] rows, int[] cols) {
        DenseMatrix result = new DenseMatrix(rows.length, cols.length);

        for (int i = 0; i < rows.length; ++i) {
            for (int j = 0; j < cols.length; ++j) {
                result.set(i + 1, j + 1, A.get(rows[i], cols[j]));
            }
        }

        return result;
    }

    /**
     * Construct a sub-matrix from the rows of a matrix.
     *
     * @param A    a matrix
     * @param rows the rows to be extracted
     * @return <i>A[rows, *]</i>
     */
    public static DenseMatrix rows(Matrix A, int[] rows) {
        return subMatrix(A, rows, R.seq(1, A.nCols()));
    }

    /**
     * Construct a sub-matrix from the rows of a matrix.
     *
     * @param A     a matrix
     * @param begin the beginning row index (counting from 1)
     * @param end   the ending row index (counting from 1)
     * @return <i>A[begin:end, *]</i>
     */
    public static DenseMatrix rows(Matrix A, int begin, int end) {
        return rows(A, R.seq(begin, end));
    }

    /**
     * Construct a sub-matrix from the columns of a matrix.
     *
     * @param A    a matrix
     * @param cols the columns to be extracted
     * @return <i>A[*, cols]</i>
     */
    public static DenseMatrix columns(Matrix A, int[] cols) {
        return subMatrix(A, R.seq(1, A.nRows()), cols);
    }

    /**
     * Construct a sub-matrix from the columns of a matrix.
     *
     * @param A     a matrix
     * @param begin the beginning column index (counting from 1)
     * @param end   the ending column index (counting from 1)
     * @return <i>A[*, begin:end]</i>
     */
    public static DenseMatrix columns(Matrix A, int begin, int end) {
        return columns(A, R.seq(begin, end));
    }

    /**
     * Replace a sub-matrix of a matrix with a smaller matrix.
     * This method is best NOT to apply to {@link SymmetricMatrix}
     * because this method does not ensure the symmetry property after replacement.
     *
     * <p>
     * <em>Note that The original matrix is modified afterward (for performance reason in case of a big matrix).
     * No new {@code Matrix} instance is constructed.</em>
     *
     * @param original    the matrix whose entries are to be replaced
     * @param rowFrom     the beginning row index
     * @param rowTo       the ending row index
     * @param colFrom     the beginning column index
     * @param colTo       the ending column index
     * @param replacement the matrix to be inserted into the original matrix
     * @return the modified matrix
     */
    public static Matrix replace(Matrix original,
                                 int rowFrom, int rowTo, int colFrom, int colTo,
                                 Matrix replacement) {

        int nrows = rowTo - rowFrom + 1;
        int ncols = colTo - colFrom + 1;

        assertArgument(rowFrom <= rowTo,
                       "invalid row indices");
        assertArgument(colFrom <= colTo,
                       "invalid column indices");
        assertArgument(replacement.nRows() == nrows,
                       String.format("the replacement matrix does not have %d rows", nrows));
        assertArgument(replacement.nCols() == ncols,
                       String.format("the replacement matrix does not have %d columns", ncols));
        assertArgument(rowTo <= original.nRows(),
                       "rowTo exceeds the boundary");
        assertArgument(colTo <= original.nCols(),
                       "colTo exceeds the boundary");

        for (int i = 1, m = rowFrom; i <= nrows; ++i, ++m) {
            for (int j = 1, n = colFrom; j <= ncols; ++j, ++n) {
                original.set(m, n, replacement.get(i, j));
            }
        }

        return original;
    }

    /**
     * Make a copy of a matrix and then replace a sub-matrix of the copy.
     * The two input matrices are not modified.
     * This method is best NOT to apply to {@link SymmetricMatrix}
     * because this method does not ensure the symmetry property after replacement.
     *
     * @param original    the matrix whose entries are to be replaced
     * @param rowFrom     the beginning row index
     * @param rowTo       the ending row index
     * @param colFrom     the beginning column index
     * @param colTo       the ending column index
     * @param replacement the matrix to be inserted into the original matrix
     * @return the modified matrix
     * @deprecated Not supported yet.
     */
    @Deprecated
    public static Matrix copyAndReplace(Matrix original,
                                        int rowFrom, int rowTo, int colFrom, int colTo,
                                        Matrix replacement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the diagonal of a matrix.
     *
     * @param A a matrix
     * @return a diagonal matrix whose diagonal entries are <i>A<sub>i,i</sub></i>, zeros elsewhere
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public static DiagonalMatrix diagonalMatrix(Matrix A) {
        assertArgument(DimensionCheck.isSquare(A), "only a square matrix has a diagonal matrix");

        double[] diag = new double[A.nRows()];
        for (int i = 1; i <= A.nRows(); ++i) {
            diag[i - 1] = A.get(i, i);
        }

        return new DiagonalMatrix(diag);
    }
}
