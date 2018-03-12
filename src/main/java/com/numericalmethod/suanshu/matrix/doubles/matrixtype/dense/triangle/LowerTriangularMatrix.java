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
 * A lower triangular matrix has 0 entries where column index > row index.
 * A lower triangular matrix is always square.
 *
 * @author Haksun Li
 */
//This implementation saves about half of the storage cost (except for the diagonal) when compared to the {@link DenseMatrix} implementation.
public class LowerTriangularMatrix implements Matrix, Densifiable {

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
         * 1,
         * 2, 3,
         * 4, 5, 6,
         * 7, 8, 9, 10,
         * 11, 12, 13, 14, 15,
         * ......
         * </pre>
         * Note that we count from 1, NOT 0.
         */
        @Override
        public void set(int row, int col, double value) {
            if (row < col) {
                //TODO: commented out for precision reason
//                if (compare(value, 0) != 0) {
//                    throw new IllegalArgumentException(String.format("L[%d][%d] is always 0", row, col));
//                }
                return;//ignore
            }

            int index = ((row * (row - 1)) >> 1) + col - 1;
            data[index] = value;
        }

        @Override
        public double get(int row, int col) {
            if (row >= col) {
                int index = ((row * (row - 1)) >> 1) + col - 1;
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
    private LowerTriangularMatrix(MyDenseDataImpl data) {
        this.storage = data;
        this.dim = data.dim;
    }

    /**
     * Construct a lower triangular matrix of dimension <i>dim * dim</i>.
     *
     * @param dim the matrix dimension
     */
    public LowerTriangularMatrix(int dim) {
        this(new MyDenseDataImpl(dim));
    }

    /**
     * Construct a lower triangular matrix from a 2D {@code double[][]} array.
     *
     * @param data the 2D array input
     * @throws IllegalArgumentException when the input {@code data} is not a lower triangular
     */
    public LowerTriangularMatrix(double[][] data) {
        this(data[data.length - 1].length);//the dimension

        //make sure data has the correct row lengths
        for (int i = 1; i <= dim; ++i) {
            if (data[i - 1].length != i) {
                throw new IllegalArgumentException("data set is not lower triangular");
            }

            for (int j = 1; j <= i; ++j) {
                set(i, j, data[i - 1][j - 1]);
            }
        }
    }

    /**
     * Construct a lower triangular matrix from a matrix.
     *
     * @param A a matrix
     * @throws IllegalArgumentException when {@code A} is not square
     */
    public LowerTriangularMatrix(Matrix A) {
        this(A.nRows());//the dimension

        if (!isSquare(A)) {
            throw new IllegalArgumentException("a lower triangular matrix must be square");
        }

        //make sure A is in fact lower triangular; throw an exception if otherwise
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= i; ++j) {
                set(i, j, A.get(i, j));
            }

            //TODO: commented out for precision reason
//            for (int j = i + 1; j <= this.dim; ++j) {
//                if (compare(A.get(i, j), 0) != 0) {
//                    throw new IllegalArgumentException("A is not lower triangular");
//                }
//            }
        }
    }

    /**
     * Copy constructor.
     *
     * @param L a lower triangular matrix
     */
    public LowerTriangularMatrix(LowerTriangularMatrix L) {
        this(L.storage);
    }

    LowerTriangularMatrix(double[] data) {
        this(new MyDenseDataImpl(data));
    }
    //</editor-fold>

    @Override
    public LowerTriangularMatrix deepCopy() {
        return new LowerTriangularMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        final double[] data = storage.asArray();

        int k = 0;
        double[] denseData = new double[nRows() * nCols()];
        for (int row = 1, i = 0; i < denseData.length; i += nCols(), ++row) {
            for (int j = 0; j < row; ++j) {
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

        final double[] data = storage.asArray();

        double[] result = new double[nCols()];
        int offset = (i * (i - 1)) >> 1;//index of the first entry in the row
        for (int j = 0; j < i; ++j) {//scan the row
            result[j] = data[j + offset];
        }

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
        int k = ((j + 2) * (j - 1)) >> 1; //index of the first entry in column col
        for (int p = j - 1, q = j; p < result.length; ++p, ++q) {//scan the column
            result[p] = data[k];
            k += q;
        }

        return new DenseVector(result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="the math operations">
    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof LowerTriangularMatrix) {
            return new LowerTriangularMatrix(new MyDenseDataImpl(storage.add(((LowerTriangularMatrix) that).storage)));
        }

        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof LowerTriangularMatrix) {
            return new LowerTriangularMatrix(new MyDenseDataImpl(storage.minus(((LowerTriangularMatrix) that).storage)));
        }

        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (!(that instanceof LowerTriangularMatrix)) {
            return math.multiply(this, that);
        }

        final TriangularData thisData = this.storage;
        final TriangularData thatData = ((LowerTriangularMatrix) that).storage;

        MyDenseDataImpl product = new MyDenseDataImpl(dim);
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= i; ++j) {
                double sum = 0.;
                for (int k = j; k <= i; ++k) {
                    sum += thisData.get(i, k) * thatData.get(k, j);
                }
                product.set(i, j, sum);
            }
        }

        return new LowerTriangularMatrix(product);
    }

    /**
     * <code>t(A)</code>
     *
     * @return an upper triangular matrix which is the transpose of
     * <code>this</code>
     */
    @Override
    public UpperTriangularMatrix t() {
        // direct access to double[] for performance
        final double[] UData = new double[storage.asArray().length];
        final double[] thisData = storage.asArray();

        int k = 0;
        for (int col = 1; col <= nCols(); ++col) {//loop over columns
            int i = ((col + 2) * (col - 1)) >> 1; //index of the first entry in the column
            for (int row = col - 1, j = 0; row < nRows(); ++row, j += row) {//scan the column; j = 0 + 1 + 2 + ... + nRows()-1
                UData[k++] = thisData[i + j];
            }
        }

        return new UpperTriangularMatrix(UData);
    }

    @Override
    public LowerTriangularMatrix scaled(double scalar) {
        return new LowerTriangularMatrix(new MyDenseDataImpl(storage.scaled(scalar)));
    }

    @Override
    public LowerTriangularMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public Vector multiply(Vector v) {
        return math.multiply(this, v);
    }

    @Override
    public LowerTriangularMatrix ZERO() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(dim);
        return A;
    }

    @Override
    public LowerTriangularMatrix ONE() {
        LowerTriangularMatrix A = new LowerTriangularMatrix(dim);
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
        if (!LowerTriangularMatrix.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final LowerTriangularMatrix other = (LowerTriangularMatrix) obj;
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
        int hash = 7;
        hash = 17 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        hash = 17 * hash + this.dim;
        return hash;
    }
}
