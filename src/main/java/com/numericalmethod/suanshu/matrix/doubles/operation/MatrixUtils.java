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

import com.numericalmethod.suanshu.matrix.doubles.MatrixTable;

/**
 * These are the utility functions to apply to matrices.
 *
 * @author Haksun Li
 */
public class MatrixUtils {

    /**
     * Get the row sums.
     *
     * @param A a matrix
     * @return the row sums
     */
    public static int[] rowSums(MatrixTable A) {
        int[] rowSums = new int[A.nRows()];
        for (int i = 1; i <= A.nRows(); ++i) {
            rowSums[i - 1] = 0;
            for (int j = 1; j <= A.nCols(); ++j) {
                rowSums[i - 1] += A.get(i, j);
            }
        }

        return rowSums;
    }

    /**
     * Get the column sums.
     *
     * @param A a matrix
     * @return the column sums
     */
    public static int[] colSums(MatrixTable A) {
        int[] colSums = new int[A.nCols()];
        for (int j = 1; j <= A.nCols(); ++j) {
            colSums[j - 1] = 0;
            for (int i = 1; i <= A.nRows(); ++i) {
                colSums[j - 1] += A.get(i, j);
            }
        }

        return colSums;
    }

    /**
     * Get all matrix entries in the form of an 1D {@code double[]}.
     *
     * @param A a matrix
     * @return all matrix entries in {@code double[]}
     */
    public static double[] to1DArray(MatrixTable A) {
        final int nRows = A.nRows();
        final int nCols = A.nCols();

        double[] result = new double[nRows * nCols];
        int k = 0;
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                result[k++] = A.get(i, j);
            }
        }

        return result;
    }

    /**
     * Get all matrix entries in the form of a 2D {@code double[][]} array.
     *
     * @param A a matrix
     * @return all matrix entries in {@code double[][]}
     */
    public static double[][] to2DArray(MatrixTable A) {
        final int nRows = A.nRows();
        final int nCols = A.nCols();

        double[][] result = new double[nRows][nCols];
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                result[i - 1][j - 1] = A.get(i, j);
            }
        }

        return result;
    }

    /**
     * Get the {@code String} representation of a matrix.
     *
     * @param A a matrix
     * @return the {@code String} representation of a matrix
     */
    public static String toString(MatrixTable A) {
        final int nRows = A.nRows();
        final int nCols = A.nCols();

        StringBuilder result = new StringBuilder();
        // dimensions
        result.append(String.format("%dx%d\n", nRows, nCols));

        // col headers
        result.append("\t");
        for (int j = 1; j <= nCols; ++j) {
            result.append(String.format("[,%d] ", j));
        }
        result.append("\n");

        for (int i = 1; i <= nRows; ++i) {
            result.append(String.format("[%d,] ", i));
            for (int j = 1; j <= nCols; ++j) {
                result.append(String.format("%f, ", A.get(i, j)));
            }

            if (i != nRows) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
