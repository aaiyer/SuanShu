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
package com.numericalmethod.suanshu.datastructure;

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.MatrixMismatchException;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * These are the utility functions for checking table dimension.
 *
 * @author Haksun Li
 */
public class DimensionCheck {

    private DimensionCheck() {
        // private constructor for utility class
    }

    /**
     * Check if a table is a column.
     *
     * @param A a table
     * @return {@code true} if the table has only one column
     */
    public static boolean isColumn(Table A) {
        return A.nCols() == 1;
    }

    /**
     * Check if a table is a row.
     *
     * @param A a table
     * @return {@code true} if the table has only one row
     */
    public static boolean isRow(Table A) {
        return A.nRows() == 1;
    }

    /**
     * Check if a table is a row or a column.
     *
     * @param A a table
     * @return {@code true} if the table has only one row or one column
     */
    public static boolean isArray(Table A) {
        return (A.nCols() == 1 || A.nRows() == 1);
    }

    /**
     * Check if a table is square.
     *
     * @param A a table
     * @return {@code true} if the table has as many rows as it has columns
     */
    public static boolean isSquare(Table A) {
        return A.nRows() == A.nCols();
    }

    /**
     * Check if a table is tall.
     *
     * @param A a table
     * @return {@code true} if the table has no fewer rows than it has columns
     */
    public static boolean isTall(Table A) {
        return A.nRows() >= A.nCols();
    }

    /**
     * Check if a table is fat.
     *
     * @param A a table
     * @return {@code true} if the table has no fewer columns than it has rows
     */
    public static boolean isFat(Table A) {
        return A.nCols() >= A.nRows();
    }

    /**
     * Check if two tables have the same dimension.
     *
     * @param A1 a table
     * @param A2 a table
     * @return {@code true} if the rows and columns, i.e. the dimensions of <i>A1</i> and <i>A2</i>, are equal
     */
    public static boolean isSameDimension(Table A1, Table A2) {
        return (A1.nRows() == A2.nRows() && A1.nCols() == A2.nCols());
    }

    /**
     * Throws if
     * <blockquote><code>A1.nRows() != A2.nRows()</code></blockquote>
     * Or
     * <blockquote><code>A1.nCols() != A2.nCols()</code></blockquote>
     *
     * @param A1 a table
     * @param A2 a table
     */
    public static void throwIfDifferentDimension(Table A1, Table A2) {
        if (!isSameDimension(A1, A2)) {
            throw new MatrixMismatchException("tables do not have the same dimension");
        }
    }

    /**
     * Throws if accessing an out of range row.
     *
     * @param A a table
     * @param i a row index
     */
    public static void throwIfInvalidRow(Table A, int i) {
        if (i < 1 || i > A.nRows()) {
            throw new MatrixAccessException(
                    String.format("out of range [1:%d] row index: %d",
                                  A.nRows(), i));
        }
    }

    /**
     * Throws if accessing an out of range column.
     *
     * @param A a table
     * @param j a column index
     */
    public static void throwIfInvalidColumn(Table A, int j) {
        if (j < 1 || j > A.nCols()) {
            throw new MatrixAccessException(
                    String.format("out of range [1:%d] column index: %d",
                                  A.nCols(), j));
        }
    }

    /**
     * Throws if
     * <blockquote><code>A1.nCols() != A2.nRows()</code></blockquote>
     *
     * @param A1 a table
     * @param A2 a table
     */
    public static void throwIfIncompatible4Multiplication(Table A1, Table A2) {
        if (A1.nCols() != A2.nRows()) {
            throw new MatrixMismatchException(
                    String.format("matrix with %d columns and matrix with %d rows cannot multiply due to mis-matched dimension",
                                  A1.nCols(), A2.nRows()));
        }
    }

    /**
     * Throws if
     * <blockquote><code>A.nCols() != v.size()</code></blockquote>
     *
     * @param A a table
     * @param v a vector
     */
    public static void throwIfIncompatible4Multiplication(Table A, Vector v) {
        if (A.nCols() != v.size()) {
            throw new MatrixMismatchException(
                    String.format("matrix with %d columns and vector with %d elements cannot multiply due to mis-matched dimension",
                                  A.nCols(), v.size()));
        }
    }
}
