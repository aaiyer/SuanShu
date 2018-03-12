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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * A permutation matrix is a square matrix that has exactly one entry '1' in each row and each column and 0's elsewhere.
 * Suppose <i>P</i> is a permutation matrix, <i>A</i> any matrix, then
 * <ol>
 * <li><i>P * A</i> swaps the rows of <i>A</i>;
 * <li><i>A * P</i> swaps the columns of <i>A</i>;
 * </ol>
 * To ensure that the matrix represents a consistent permutation, there is no method 'set' in this class.
 * The matrix entries can only be modified by the swap functions.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Permutation_matrix">Wikipedia: Permutation matrix</a>
 */
public class PermutationMatrix implements Matrix {

    /**
     * This implementation uses row view.
     * We store the column position of the 1 in each row.
     * For examples,
     * <blockquote><code><pre>
     * | 1 0 0 |
     * | 0 1 0 |  is
     * | 0 0 1 |
     *
     * [1, 2, 3];
     *
     * | 0 0 1 |
     * | 0 1 0 |  is
     * | 1 0 0 |
     *
     * [3, 2, 1]
     * </pre></code></blockquote>
     */
    private int[] data;
    private int sign = 1;
    private final int dim;

    //<editor-fold defaultstate="collapsed" desc="ctors">
    /**
     * Construct an identity permutation matrix.
     *
     * @param dim the matrix dimension
     */
    public PermutationMatrix(int dim) {
        this.dim = dim;
        this.data = new int[dim + 1];//data[0] is not used
        for (int i = 0; i <= dim; ++i) {
            this.data[i] = i;
        }
    }

    /**
     * Construct a permutation matrix from an 1D {@code double[]}.
     *
     * @param data the 1D {@code double[]} indicating the column position of the 1 in each row
     * @throws IllegalArgumentException if {@code data} is not a permutation matrix
     */
    public PermutationMatrix(int data[]) {
        this.dim = data.length;
        this.data = new int[dim + 1];//data[0] is not used

        boolean[] checksum = new boolean[dim + 1];
        for (int i = 1; i <= dim; ++i) {
            int value = data[i - 1];
            this.data[i] = value;
            checksum[value] = true;
        }

        boolean sum = true; //checksum
        for (int i = 1; i <= dim; ++i) {
            sum &= checksum[i];
        }

        if (!sum) {
            throw new IllegalArgumentException("data is not a permutation matrix");
        }
    }

    /**
     * Copy constructor.
     *
     * @param P a permutation matrix
     */
    public PermutationMatrix(PermutationMatrix P) {
        this.dim = P.dim;
        this.data = Arrays.copyOf(P.data, P.data.length);
        this.sign = P.sign;
    }
    //</editor-fold>

    @Override
    public PermutationMatrix deepCopy() {
        return new PermutationMatrix(this);
    }

    @Override
    public int nRows() {
        return dim;
    }

    @Override
    public int nCols() {
        return dim;
    }

    /**
     * Get the sign of the permutation matrix which is also the determinant.
     * It is +1 for an even (or 0) number of swaps and -1 for an odd number of swaps.
     *
     * @return the sign
     */
    public double sign() {
        return Math.signum(sign);
    }

    //<editor-fold defaultstate="collapsed" desc="the swapping operations">    
    /**
     * Swap two rows of a permutation matrix.
     *
     * @param i1 row 1
     * @param i2 row 2
     */
    public void swapRow(int i1, int i2) {
        throwIfInvalidRow(this, i1);
        throwIfInvalidRow(this, i2);
        int tmp = data[i1];
        data[i1] = data[i2];
        data[i2] = tmp;

        sign *= -1;
    }

    /**
     * Swap two columns of a permutation matrix.
     *
     * @param j1 column 1
     * @param j2 column 2
     */
    public void swapColumn(int j1, int j2) {
        throwIfInvalidColumn(this, j1);
        throwIfInvalidColumn(this, j2);
        for (int i = 1; i <= dim; ++i) {
            if (data[i] == j1) {
                data[i] = j2;
            } else if (data[i] == j2) {
                data[i] = j1;
            }
        }

        sign *= -1;
    }

    /**
     * Swap a row of the permutation matrix with the last row.
     *
     * @param i the row to be swapped with the last row
     */
    public void moveRow2End(int i) {
        throwIfInvalidRow(this, i);
        int tmp = data[i];
        for (int k = i; k < nRows(); ++k) {
            data[k] = data[k + 1]; //shift rows upward (toward the left)
        }
        data[nRows()] = tmp;
    }

    /**
     * Swap a column of a permutation matrix with the last column.
     *
     * @param j the column to be swapped with the last column
     */
    public void moveColumn2End(int j) {
        throwIfInvalidColumn(this, j);
        for (int k = 1; k <= nRows(); ++k) {
            if (data[k] == j) {
                data[k] = nCols();//move the column to the end
            } else if (data[k] > j) {
                data[k]--;//shift the column to the left by 1 column
            }
        }
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="setters and getters">
    @Override
    public double get(int i, int j) throws MatrixAccessException {
        throwIfInvalidRow(this, i);
        throwIfInvalidColumn(this, i);

        double result = 0;
        if (data[i] == j) {
            result = 1;
        }
        return result;
    }

    @Override
    public Vector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        DenseVector result = new DenseVector(dim);
        result.set(data[i], 1);
        return result;
    }

    @Override
    public Vector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        DenseVector result = new DenseVector(dim);
        for (int k = 1; k <= dim; ++k) {
            if (data[k] == j) {
                result.set(k, 1);
                break;
            }
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="the math operations">
    /**
     * Right multiplication by <i>P</i>.
     * <i>A * P</i> is the same as swapping columns in <i>A</i> according to <i>P</i>.
     *
     * @param A a matrix
     * @return the matrix with the columns swapped
     */
    public Matrix rightMultiply(Matrix A) {
        throwIfIncompatible4Multiplication(A, this);

        Vector[] cols = new Vector[A.nCols()];
        for (int i = 1; i <= A.nCols(); ++i) {
            cols[data[i] - 1] = A.getColumn(i);
        }

        Matrix result = CreateMatrix.cbind(cols);
        return result;
    }

    /**
     * Left multiplication by <i>P</i>.
     * <i>P * v</i> is the same as swapping the vector entries/rows according to <i>P</i>.
     *
     * @param v a column vector
     * @return the vector with the rows swapped
     */
    @Override
    public Vector multiply(Vector v) {
        double[] elm = new double[v.size()];
        for (int i = 0; i < elm.length; ++i) {
            elm[i] = v.get(data[i + 1]);
        }

        DenseVector result = new DenseVector(elm);
        return result;
    }

    /**
     * Left multiplication by <i>P</i>.
     * <i>P * A</i> is the same as swapping rows in <i>A</i> according to <i>P</i>.
     *
     * @param A a matrix
     * @return the matrix with the rows swapped
     */
    @Override
    public Matrix multiply(Matrix A) {
        throwIfIncompatible4Multiplication(this, A);

        Vector[] rows = new Vector[A.nRows()];
        for (int i = 0; i < A.nRows(); ++i) {
            rows[i] = A.getRow(data[i + 1]);
        }

        Matrix result = CreateMatrix.rbind(rows);
        return result;
    }

    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        Matrix result = new DenseMatrix(that);
        for (int i = 1; i < data.length; ++i) {
            result.set(i, data[i], 1 + result.get(i, data[i]));
        }
        return result;
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        Matrix result = new DenseMatrix(that);
        for (int i = 1; i < data.length; ++i) {
            result.set(i, data[i], 1 - result.get(i, data[i]));
        }
        return result;
    }

    @Override
    public Matrix scaled(double scalar) {
        Matrix result = new DenseMatrix(dim, dim);
        for (int i = 1; i < data.length; ++i) {
            result.set(i, data[i], scalar);
        }
        return result;
    }

    @Override
    public Matrix opposite() {
        return scaled(-1);
    }

    /**
     * The transpose of a permutation matric is the same as its inverse.
     * That is,
     * <blockquote><code>P.multiply(P.t()) == P.t().multiply(P) == P.toDense().ONE()</code></blockquote>
     *
     * @return a copy of itself
     */
    @Override
    public PermutationMatrix t() {
        PermutationMatrix P = new PermutationMatrix(dim);
        for (int i = 1; i <= dim; ++i) {
            P.data[this.data[i]] = i;
        }

        return P;
    }

    @Override
    public PermutationMatrix ONE() {
        return new PermutationMatrix(dim);
    }

    @Override
    public Matrix ZERO() {
        throw new UnsupportedOperationException("permutation matrix cannot be zero");
    }
    //</editor-fold>

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PermutationMatrix) && (obj instanceof Matrix)) {
            return AreMatrices.equal(this, (Matrix) obj, 0);
        }
        final PermutationMatrix that = (PermutationMatrix) obj;
        if (this.dim != that.dim) {
            return false;
        }
        if (this.data != that.data && (this.data == null || !Arrays.equals(this.data, that.data))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.dim;
        hash = 59 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    /**
     * Don't use this function to change entries in a Permutation matrix.
     * Use the swap functions instead.
     * <p/>
     * {@link #swapRow(int, int)},
     * {@link #swapColumn(int, int)},
     * {@link #moveRow2End(int)},
     * {@link #moveColumn2End(int)},
     *
     * @throws UnsupportedOperationException when called
     * @deprecated use the swap functions instead
     */
    @Deprecated
    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        throw new UnsupportedOperationException("Please use swapRow or swapColumn instead.");
    }
}
