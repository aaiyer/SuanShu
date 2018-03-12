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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal;

import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.Densifiable;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import java.util.Arrays;

/**
 * This implementation stores the super, main, and sub diagonals of a matrix.
 *
 * @author Haksun Li
 */
class DiagonalData implements MatrixAccess, Densifiable {

    /** the types of matrices the data represent */
    static enum Type {

        /** a tridiagonal matrix has the super, main and sub diagonals */
        TRI_DIAGONAL,
        /** an upper diagonal matrix has the super and main diagonals */
        BI_DIAGONAL_UPPER,
        /** a lower diagonal matrix has the main and sub diagonals */
        BI_DIAGONAL_LOWER,
        /** a diagonal matrix has only the main diagonal */
        DIAGONAL
    };

    final Type type;
    final int dim;
    /** row view, recording the row index of the super/main/sub diagonals */
    private double[][] data = null;

    /**
     * Construct a storage for the super, main, and sub diagonals.
     * Initialize the contents to 0s.
     *
     * @param type the matrix type
     * @param dim  the (square) matrix dimension
     */
    DiagonalData(Type type, int dim) {
        this.dim = dim;
        this.type = type;

        this.data = new double[3][];
        switch (type) {
            case TRI_DIAGONAL:
                this.data[0] = new double[dim - 1];//super diagonal
                this.data[1] = new double[dim];//main diagonal
                this.data[2] = new double[dim - 1];//sub diagonal
                break;
            case BI_DIAGONAL_UPPER:
                this.data[0] = new double[dim - 1];//super diagonal
                this.data[1] = new double[dim];//main diagonal
                break;
            case BI_DIAGONAL_LOWER:
                this.data[1] = new double[dim];//main diagonal
                this.data[2] = new double[dim - 1];//sub diagonal
                break;
            case DIAGONAL:
                this.data[1] = new double[dim];//main diagonal
                break;
        }
    }

    /**
     * Construct a storage for the super, main, and sub diagonals.
     * Initialize the contents by a {@code double[][]} array.
     * The ordering of the rows, if exist or {@code null}, are: super, main, sub.
     * Note that there must be a main diagonal (not {@code null}), and there must be at least one diagonal.
     *
     * @param diags the diagonal(s)
     * @throws IllegalArgumentException if {@code diags} is invalid
     */
    DiagonalData(double[][] diags) {
        this.data = new double[3][]; // 3 diagonals: super, main, sub
        switch (diags.length) {
            case 1:
                this.type = Type.DIAGONAL;
                this.dim = diags[0].length;
                allocateDiagonal(diags[0]);
                break;
            case 2:
                if (diags[0].length > diags[1].length) {//TODO: both diagonals must not be null
                    this.type = Type.BI_DIAGONAL_LOWER;
                    this.dim = diags[0].length;
                    allocateDiagonal(diags[0]);
                    allocateSubDiagonal(diags[1]);
                } else {
                    this.type = Type.BI_DIAGONAL_UPPER;
                    this.dim = diags[1].length;
                    allocateSuperDiagonal(diags[0]);
                    allocateDiagonal(diags[1]);
                }
                break;
            case 3:
                this.type = Type.TRI_DIAGONAL;
                this.dim = diags[1].length;
                allocateSuperDiagonal(diags[0]);
                allocateDiagonal(diags[1]);
                allocateSubDiagonal(diags[2]);
                break;
            default:
                throw new IllegalArgumentException("invalid data set");
        }
    }

    /**
     * Copy constructor.
     *
     * @param that a {@code DiagonalData}
     */
    DiagonalData(DiagonalData that) {
        this.dim = that.dim;
        this.type = that.type;

        this.data = new double[3][];
        this.data[0] = that.data[0] != null ? Arrays.copyOf(that.data[0], that.data[0].length) : null;
        this.data[1] = that.data[1] != null ? Arrays.copyOf(that.data[1], that.data[1].length) : null;
        this.data[2] = that.data[2] != null ? Arrays.copyOf(that.data[2], that.data[2].length) : null;
    }

    @Override
    public DenseMatrix toDense() {
        DenseMatrix A = new DenseMatrix(dim, dim);

        //first row
        A.set(1, 1, get(1, 1));

        if (dim >= 2) {
            A.set(1, 2, get(1, 2));
            for (int i = 2; i < dim; ++i) {
                A.set(i, i - 1, get(i, i - 1));
                A.set(i, i, get(i, i));
                A.set(i, i + 1, get(i, i + 1));
            }

            //last row
            A.set(dim, dim, get(dim, dim));
            A.set(dim, dim - 1, get(dim, dim - 1));
        }

        return A;
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
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if attempt to assign a non-zero value to off diagonal entries
     */
    @Override
    public void set(int i, int j, double value) {
        if ((i == j - 1) && (data[0] != null)) {//super-diagonal, e.g., (1,2)
            data[0][i - 1] = value;
        } else if (i == j) {//main diagonal, e.g., (1,1); main diagonal always exists
            data[1][i - 1] = value;
        } else if ((i == j + 1) && (data[2] != null)) {//sub-diagonal, e.g., (2,1)
            data[2][i - 2] = value;
        } else {//zero entries
            if (!isZero(value, 0)) {
                throw new IllegalArgumentException(String.format("U[%d][%d] is always 0", i, j));
            }
        }
    }

    @Override
    public double get(int i, int j) {
        if (data == null) {
            return 0;
        }

        if (i == j - 1) {//super-diagonal, e.g., (1,2)
            return data[0] != null ? data[0][i - 1] : 0;
        } else if (i == j) {//main diagonal, e.g., (1,1)
            return data[1] != null ? data[1][i - 1] : 0;
        } else if (i == j + 1) {//sub-diagonal, e.g., (2,1)
            return data[2] != null ? data[2][i - 2] : 0;
        }

        return 0;// else, zero entries
    }

    /**
     * Get the main diagonal.
     *
     * @return the main diagonal
     */
    double[] getDiagonal() {
        double[] copy = Arrays.copyOf(data[1], data[1].length);
        return copy;
    }

    /**
     * Get the super-diagonal.
     *
     * @return the super-diagonal
     */
    double[] getSuperDiagonal() {
        double[] copy;
        if (data[0] != null) {
            copy = Arrays.copyOf(data[0], data[0].length);
        } else {
            copy = new double[this.dim - 1];
        }

        return copy;
    }

    /**
     * Get the sub-diagonal.
     *
     * @return the sub-diagonal
     */
    double[] getSubDiagonal() {
        double[] copy;
        if (data[2] != null) {
            copy = Arrays.copyOf(data[2], data[2].length);
        } else {
            copy = new double[this.dim - 1];
        }

        return copy;
    }

    /**
     * Add up two {@code DiagonalData} of the same type.
     *
     * @param that a {@code DiagonalData}
     * @return the sums of diagonal elements
     */
    DiagonalData add(DiagonalData that) {
        assertSameType(this, that);

        DiagonalData result = new DiagonalData(type, dim);
        for (int i = 0; i <= 2; ++i) {
            if (this.data[i] != null) {
                int length = this.data[i].length;
                result.data[i] = new double[length];
                for (int j = 0; j < length; ++j) {
                    result.data[i][j] = this.data[i][j] + that.data[i][j];
                }
            }
        }

        return result;
    }

    /**
     * Subtract {@code that} from {@code this}; both are of the same type.
     *
     * @param that a {@code DiagonalData}
     * @return the differences between diagonal elements
     */
    DiagonalData minus(DiagonalData that) {
        assertSameType(this, that);

        DiagonalData result = new DiagonalData(type, dim);
        for (int i = 0; i <= 2; ++i) {
            if (this.data[i] != null) {
                int length = this.data[i].length;
                result.data[i] = new double[length];
                for (int j = 0; j < length; ++j) {
                    result.data[i][j] = this.data[i][j] - that.data[i][j];
                }
            }
        }

        return result;
    }

    /**
     * Scale {@code this} by a constant.
     *
     * @param c a scaling factor
     * @return the scaled elements
     */
    DiagonalData scaled(double c) {
        DiagonalData result = new DiagonalData(type, dim);
        for (int i = 0; i <= 2; ++i) {
            if (this.data[i] != null) {
                int length = this.data[i].length;
                result.data[i] = new double[length];
                for (int j = 0; j < length; ++j) {
                    result.data[i][j] = this.data[i][j] * c;
                }
            }
        }

        return result;
    }

    /**
     * Compute <i>-D</i>.
     *
     * @return {@code this.scaled(-1)}
     */
    DiagonalData opposite() {
        return scaled(-1);
    }

    @Override
    public String toString() {
        return toDense().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiagonalData that = (DiagonalData) obj;
        if (this.dim != that.dim) {
            return false;
        }
//        if (this.type != that.type) {//Don't check type because a matrix may belong to multiple type, e.g., ZERO, ONE
//            return false;
//        }
        if (this.data != that.data) {
            for (int i = 0; i < 3; ++i) {
                if (((this.data[i] != null)) && (that.data[i] != null)) {
                    if (!equal(this.data[i], that.data[i], 0)) {
                        return false;
                    }
                } else if (this.data[i] == null) {//if both are null, continue
                    if ((that.data[i] != null) && !isAllZeros(that.data[i], 0)) {
                        return false;
                    }
                } else if (that.data[i] == null) {//if both are null, continue
                    if (!isAllZeros(this.data[i], 0)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.dim;
        hash = 47 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 47 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    private void allocateSuperDiagonal(double[] sup) {
        if (sup != null) {
            SuanShuUtils.assertArgument(sup.length == dim - 1, "data set is not super-diagonal"); // make sure data has the correct row lengths
            this.data[0] = Arrays.copyOf(sup, dim - 1);
        } else {
            this.data[0] = new double[dim - 1];
        }
    }

    private void allocateDiagonal(double[] main) {
        if (main != null) {
            SuanShuUtils.assertArgument(main.length == dim, "data set is not diagonal"); // make sure data has the correct row lengths
            this.data[1] = Arrays.copyOf(main, dim);
        } else {
            this.data[1] = new double[dim];
        }
    }

    private void allocateSubDiagonal(double[] sub) {
        if (sub != null) {
            SuanShuUtils.assertArgument(sub.length == (dim - 1), "data set is not sub-diagonal"); // make sure data has the correct row lengths
            this.data[2] = Arrays.copyOf(sub, dim - 1);
        } else {
            this.data[2] = new double[dim - 1];
        }
    }

    private void assertSameType(DiagonalData d1, DiagonalData d2) {
        if (d1.type != d2.type) {
            throw new IllegalArgumentException("the two data set are not of the same type");
        }
    }
}
