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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.isSquare;
import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfDifferentDimension;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.Densifiable;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * An upper triangular matrix has 0 entries where row index > column index.
 * An upper triangular matrix is always square.
 *
 * @author Haksun Li
 */
//This implementation saves about half of the storage cost (except for the diagonal) when compared to the {@link DenseMatrix} implementation.
public class UpperTriangularMatrix implements Matrix, Densifiable {

    //<editor-fold defaultstate="collapsed" desc="customize the view/usage of the data array">
    private static class MyDenseDataImpl extends TriangularData {

        private final double[] data = asArray();

        private MyDenseDataImpl(int dim) {
            super(dim);
        }

        private MyDenseDataImpl(double[] data) {
            super(data);
        }

        /*
         * We use a row view of data and store only non-zero values, so the indices are, for example,:
         * <pre>
         * 1, 2, 3, 4, 5
         * 6, 7, 8, 9,
         * 10, 11, 12,
         * 13, 14,
         * 15
         * </pre>
         * Note that we count from 1, NOT 0.
         */
        @Override
        public void set(int i, int j, double value) {
            if (i > j) {
                // TODO: commented out for precision reason
//                if (compare(value, 0) != 0) {
//                    throw new IllegalArgumentException(String.format("U[%d][%d] is always 0", row, col));
//                }
                return; // ignore
            }

            int index = i == 1 ? j - 1 : (i - 1) * dim - (((i - 1) * (i - 2)) >> 1) + j - i;
            data[index] = value;
        }

        @Override
        public double get(int i, int j) {
            if (i <= j) { // in the upper triangle
                int index = i == 1 ? j - 1 : (i - 1) * dim - (((i - 1) * (i - 2)) >> 1) + j - i;
                return data[index];
            }

            return 0;
        }

        @Override
        public MyDenseDataImpl deepCopy() {
            MyDenseDataImpl result = new MyDenseDataImpl(Arrays.copyOf(data, data.length));
            return result;
        }

        @Override
        public int nRows() {
            return dim;
        }

        @Override
        public int nCols() {
            return dim;
        }
    }
    //</editor-fold>    

    private final MyDenseDataImpl storage;
    private final int dim;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    private UpperTriangularMatrix(MyDenseDataImpl data) {
        this.storage = data;
        this.dim = data.dim;
    }

    /**
     * Construct an upper triangular matrix of dimension <i>dim * dim</i>.
     *
     * @param dim the matrix dimension
     */
    public UpperTriangularMatrix(int dim) {
        this(new MyDenseDataImpl(dim));
    }

    /**
     * Construct an upper triangular matrix from a 2D {@code double[][]} array.
     *
     * @param data the 2D array input
     * @throws IllegalArgumentException when the input {@code data} is not a upper triangular
     */
    public UpperTriangularMatrix(double[][] data) {
        this(data[0].length);//the dimension of data should be data[0].length

        //make sure data has the correct row lengths
        for (int i = 1; i <= dim; ++i) {
            if (data[i - 1].length != dim - i + 1) {
                throw new IllegalArgumentException("data set is not upper triangular");
            }

            for (int j = i; j <= dim; ++j) {
                set(i, j, data[i - 1][j - i]);
            }
        }
    }

    /**
     * Construct an upper triangular matrix from a matrix.
     *
     * @param A a matrix
     * @throws IllegalArgumentException when {@code A} is not square
     */
    public UpperTriangularMatrix(Matrix A) {
        this(A.nRows());//the dimension of A

        if (!isSquare(A)) {
            throw new IllegalArgumentException("a upper triangular matrix must be square");
        }

        //make sure data has the correct row lengths
        for (int i = 1; i <= dim; ++i) {
            //TODO: commented out for precision reason
//            for (int j = 1; j < i; ++j) {
//                if (compare(0d, U.get(i, j)) != 0) {
//                    throw new IllegalArgumentException("data set is not upper triangular");
//                }
//            }

            for (int j = i; j <= dim; ++j) {
                set(i, j, A.get(i, j));
            }
        }
    }

    /**
     * Copy constructor.
     *
     * @param U an upper triangular matrix
     */
    public UpperTriangularMatrix(UpperTriangularMatrix U) {
        this(U.storage);
    }

    UpperTriangularMatrix(double[] data) {
        this(new MyDenseDataImpl(data));
    }
    //</editor-fold>

    @Override
    public UpperTriangularMatrix deepCopy() {
        return new UpperTriangularMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        final double[] data = storage.asArray();// triangular data

        int k = 0;
        double[] denseData = new double[nRows() * nCols()];
        for (int row = 1, i = 0; i < denseData.length; i += nCols() + 1, ++row) {
            for (int j = 0; j <= nCols() - row; ++j) {
                denseData[i + j] = data[k++];
            }
        }

        return new DenseMatrix(denseData, nRows(), nCols());
    }

    @Override
    public int nRows() {
        return dim;
    }

    @Override
    public int nCols() {
        return dim;
    }

    //<editor-fold defaultstate="collapsed" desc="setters and getters">
    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        storage.set(i, j, value);
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        return storage.get(i, j);
    }

    @Override
    public Vector getRow(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("row/col counts from 1");
        }
        if (i > nRows()) {
            throw new IllegalArgumentException("invalid index");
        }

        double[] result = new double[nCols()];
        int srcPos = (i - 1) * nCols();
        if (i >= 3) {
            srcPos -= ((i - 1) * (i - 2)) >> 1;
        }

        int destPos = i - 1;
        int length = nCols() - destPos;
        System.arraycopy(storage.asArray(), srcPos, result, destPos, length);
        return new DenseVector(result);
    }

    @Override
    public Vector getColumn(int j) {
        if (j < 1) {
            throw new IllegalArgumentException("row/col counts from 1");
        }
        if (j > nCols()) {
            throw new IllegalArgumentException("invalid index");
        }

        final double[] data = storage.asArray();

        double[] result = new double[nRows()];
        for (int p = 0, q = j - 1; p < j; ++p, q += nCols() - p) {
            result[p] = data[q];
        }

        return new DenseVector(result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="the math operations">
    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof UpperTriangularMatrix) {
            return new UpperTriangularMatrix(new MyDenseDataImpl(storage.add(((UpperTriangularMatrix) that).storage)));
        }

        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof UpperTriangularMatrix) {
            return new UpperTriangularMatrix(new MyDenseDataImpl(storage.minus(((UpperTriangularMatrix) that).storage)));
        }

        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (!(that instanceof UpperTriangularMatrix)) {
            return math.multiply(this, that);
        }

        final TriangularData thisData = this.storage;
        final TriangularData thatData = ((UpperTriangularMatrix) that).storage;

        MyDenseDataImpl product = new MyDenseDataImpl(dim);
        for (int i = 1; i <= dim; ++i) {
            for (int j = i; j <= dim; ++j) {
                double sum = 0.;
                for (int k = i; k <= j; ++k) {
                    sum += thisData.get(i, k) * thatData.get(k, j);
                }
                product.set(i, j, sum);
            }
        }

        return new UpperTriangularMatrix(product);
    }

    @Override
    public LowerTriangularMatrix t() {
        // direct access to double[] for performance
        final double[] LData = new double[storage.asArray().length];
        final double[] thisData = storage.asArray();

        int p = 0;
        for (int i = 0; i < nCols(); ++i) {//loop over columns
            for (int j = 0, k = 0; j <= i; ++j, k += nRows() - j) {//scan the column
                LData[p++] = thisData[i + k];
            }
        }

        return new LowerTriangularMatrix(LData);
    }

    @Override
    public UpperTriangularMatrix scaled(double scalar) {
        return new UpperTriangularMatrix(new MyDenseDataImpl(storage.scaled(scalar)));
    }

    @Override
    public UpperTriangularMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public Vector multiply(Vector v) {
        return math.multiply(this, v);
    }

    @Override
    public UpperTriangularMatrix ZERO() {
        UpperTriangularMatrix A = new UpperTriangularMatrix(dim);
        return A;
    }

    @Override
    public UpperTriangularMatrix ONE() {
        UpperTriangularMatrix A = new UpperTriangularMatrix(dim);
        for (int i = 1; i <= dim; ++i) {
            A.set(i, i, 1);
        }
        return A;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return MatrixUtils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!UpperTriangularMatrix.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final UpperTriangularMatrix other = (UpperTriangularMatrix) obj;
        if (this.dim != other.dim) {
            return false;
        }
        if (this.storage != other.storage && (this.storage == null || !this.storage.equals(other.storage))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        hash = 31 * hash + this.dim;
        return hash;
    }
}
