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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class implements the standard, dense, {@code double} based matrix representation.
 * There are two additional methods to change the matrix content:
 * <ul>
 * <li>{@link #setRow(int, com.numericalmethod.suanshu.vector.doubles.Vector)}
 * <li>{@link #setColumn(int, com.numericalmethod.suanshu.vector.doubles.Vector)}
 * </ul>
 *
 * @author Haksun Li
 */
public class DenseMatrix implements Matrix, Densifiable {

    private static class ParallelExecutorInstanceHolder { // thread-safe lazy initialization idiom

        private static final ParallelExecutor instance = new ParallelExecutor();
    }

    //<editor-fold defaultstate="collapsed" desc="customize the view/usage of the data array">
    private static class MyDenseDataImpl extends DenseData {

        private final double[] data = asArray();
        private final int nCols;

        private MyDenseDataImpl(int nRows, int nCols) {
            super(nRows * nCols);
            this.nCols = nCols;
        }

        private MyDenseDataImpl(double[] data, int nCols) {
            super(data);
            this.nCols = nCols;
        }

        /**
         * We use a row view of data, so the indices are, for example,:
         * <pre>
         * 1, 2, 3, 4, 5,
         * 6, 7, 8, 9, 10,
         * 11, 12, 13, 14, 15,
         * ......
         * </pre>
         * Note that the indices count from 1, NOT 0.
         */
        @Override
        public void set(int i, int j, double value) {
            this.data[(i - 1) * nCols + (j - 1)] = value;
        }

        @Override
        public double get(int i, int j) {
            if (data == null) {
                return 0.;
            }

            return this.data[(i - 1) * nCols + (j - 1)];
        }

        @Override
        public MyDenseDataImpl deepCopy() {
            MyDenseDataImpl result = new MyDenseDataImpl(Arrays.copyOf(data, data.length), nCols);
            return result;
        }

        @Override
        public int nRows() {
            return nCols > 0 ? data.length / nCols : 0;
        }

        @Override
        public int nCols() {
            return nCols;
        }
    }
    //</editor-fold>
    private MyDenseDataImpl storage;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();
    /** for parallel algorithm execution */
    private static final int LENGTH_THRESHOLD = 100 * 100;

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    /**
     * Construct a 0 matrix of dimension <i>nRows * nCols</i>.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public DenseMatrix(int nRows, int nCols) {
        this.storage = new MyDenseDataImpl(nRows, nCols);
    }

    /**
     * Construct a matrix from a 2D {@code double[][]} array.
     *
     * @param data a 2D array input
     * @throws IllegalArgumentException when {@code data} is a jagged array
     */
    public DenseMatrix(double[][] data) {
        this(data.length, data[0].length);

        final int nRows = nRows();
        final int nCols = nCols();

        double[] denseData = storage.asArray();
        int i = 0;
        for (int j = 1; j <= nRows; ++j) {
            //make sure data is not a jagged array; throw an exception if otherwise
            if (data[j - 1].length != nCols) {
                storage = null;
                throw new IllegalArgumentException("data is a jagged array; make sure that all rows have the same length");
            }

            for (int k = 1; k <= nCols; ++k) {
                denseData[i++] = data[j - 1][k - 1];
            }
        }
    }

    /**
     * Construct a matrix from a 1D {@code double[]}. The array is a concatenation of the matrix rows.
     * A sample usage is to convert a vector to a matrix.
     * For example, to construct a column vector, we do
     * <blockquote><code>DenseMatrix V = new DenseMatrix(v.toArray(), v.length, 1)</code></blockquote>
     * To construct a row vector, we do
     * <blockquote><code>DenseMatrix V = new DenseMatrix(v.toArray(), 1, v.length)</code></blockquote>
     *
     * @param data  the 1D array input
     * @param nRows the number or rows
     * @param nCols the number of columns
     * @throws IllegalArgumentException when the length of {@code data} is different from <i>nRows * nCols</i>
     */
    public DenseMatrix(double[] data, int nRows, int nCols) {
        SuanShuUtils.assertArgument(data.length == nRows * nCols, "the data length does not match the matrix dimension");
        this.storage = new MyDenseDataImpl(data, nCols);
    }

    /**
     * Construct a column matrix from a vector.
     *
     * @param v a vector
     */
    public DenseMatrix(Vector v) {
        this(v.toArray(), v.size(), 1);//copy; independent of the input 'vector'
    }

    /**
     * Convert any matrix to the standard matrix representation.
     * This method is the same as {@link #toDense} if {@code A} is {@link Densifiable}.
     *
     * @param A a matrix
     */
    public DenseMatrix(Matrix A) {
        this(A.nRows(), A.nCols());

        final int nRows = A.nRows();
        final int nCols = A.nCols();

        if (A instanceof Densifiable) {
            DenseMatrix M = ((Densifiable) A).toDense();
            double[] m = M.storage.asArray();
            this.storage = new MyDenseDataImpl(m, nCols);
            return;
        }

        double[] data = storage.asArray();
        int k = 0;
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                data[k++] = A.get(i, j);
            }
        }
    }

    /**
     * Copy constructor performing a deep copy.
     *
     * @param A a {@code DenseMatrix}
     */
    public DenseMatrix(DenseMatrix A) {
        double[] a0 = A.storage.asArray();
        double[] a1 = Arrays.copyOf(a0, a0.length);//it is very important to make a copy
        this.storage = new MyDenseDataImpl(a1, A.nCols());
    }
    //</editor-fold>

    @Override
    public DenseMatrix deepCopy() {
        return new DenseMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        return new DenseMatrix(this);
    }

    @Override
    public int nRows() {
        return storage.nRows();
    }

    @Override
    public int nCols() {
        return storage.nCols();
    }

    //<editor-fold defaultstate="collapsed" desc="setters and getters">
    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        storage.set(i, j, value);
    }

    /**
     * Change the matrix row values to a vector value.
     *
     * @param i the <i>i</i>-th row to change
     * @param v the values to change the row to
     */
    public void setRow(int i, Vector v) {
        SuanShuUtils.assertArgument(v.size() == nCols(), "vector v has the wrong dimension");
        for (int j = 1; j <= nCols(); ++j) {
            storage.set(i, j, v.get(j));
        }
    }

    /**
     * Change the matrix column values to a vector value.
     *
     * @param j the <i>j</i>-th column to change
     * @param v the values to change the column to
     */
    public void setColumn(int j, Vector v) {
        SuanShuUtils.assertArgument(v.size() == nRows(), "vector v has the wrong dimension");
        for (int i = 1; i <= nRows(); ++i) {
            storage.set(i, j, v.get(i));
        }
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        return storage.get(i, j);
    }

    @Override
    public Vector getRow(int i) {
        return getRow(i, 1, nCols());
    }

    /**
     * Get a sub-row of the <i>i</i>-th row, from {@code beginCol} column to {@code endCol} column, inclusively.
     *
     * @param i        the row to extract
     * @param beginCol the beginning column of the sub-row
     * @param endCol   the ending column of the sub-row
     * @return a sub-row
     */
    public Vector getRow(int i, int beginCol, int endCol) {
        final int nCols = nCols();

        if (i < 1) {
            throw new IllegalArgumentException("row/col counts from 1");
        }

        if (i > nRows() || beginCol < 1 || beginCol > nCols || endCol < 1 || endCol > nCols) {
            throw new IllegalArgumentException("invalid index");
        }

        double[] result = null;
        int p = nCols * (i - 1);
        result = Arrays.copyOfRange(storage.asArray(), p + beginCol - 1, p + endCol);
        return new DenseVector(result);
    }

    @Override
    public Vector getColumn(int j) {
        return getColumn(j, 1, nRows());
    }

    /**
     * Get a sub-column of the <i>j</i>-th column, from {@code beginRow} row to {@code endRow} row, inclusively.
     *
     * @param j        the column to extract
     * @param beginRow the beginning row of the sub-column
     * @param endRow   the ending row of the sub-column
     * @return a sub-column
     */
    public Vector getColumn(int j, int beginRow, int endRow) {
        final int nRows = nRows();
        final int nCols = nCols();

        if (j < 1) {
            throw new IllegalArgumentException("row/col counts from 1");
        }

        if (j > nCols || beginRow < 1 || beginRow > nRows || endRow < 1 || endRow > nRows) {
            throw new IllegalArgumentException("invalid index");
        }

        double[] data = storage.asArray();
        double[] result = new double[endRow - beginRow + 1];
        for (int k = 0, i = (j - 1) + ((beginRow - 1) * nCols); k <= (endRow - beginRow); ++k, i += nCols) {
            result[k] = data[i];
        }

        return new DenseVector(result);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="the math operations">
    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof DenseMatrix) {
            DenseMatrix result = new DenseMatrix(storage.add(((DenseMatrix) that).storage),
                    nRows(), nCols());
            return result;
        }

        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof DenseMatrix) {
            DenseMatrix result = new DenseMatrix(storage.minus(((DenseMatrix) that).storage),
                    nRows(), nCols());
            return result;
        }

        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        throwIfIncompatible4Multiplication(this, that);

        if (!(that instanceof DenseMatrix)) {
            return math.multiply(this, that);
        }

        final DenseMatrix other_t = ((DenseMatrix) that).t();//we transpose the multiplicand for easy indexing

        final double[] thisData = storage.asArray(); //for performance: avoid indirect access of the data array in the object
        final double[] thatData = other_t.storage.asArray(); //for performance: avoid indirect access of the data array in the object
        final double[] resultData = new double[this.nRows() * that.nCols()];

        final int nCols = nCols();
        final int nThatCols = that.nCols();
        /* original single-threaded algorithm */
//        int m = 0;
//        for (int i = 0; i < thisData.length; i += nCols) {//loop over rows of 'this'
//            for (int j = 0; j < thatData.length; j += nCols) {//loop over cols of 'that'
//                    /*
//                 * dot product
//                 * result[p][q] = Sum{this.row(p) * other.column(q)}
//                 */
//                double sum = 0.;
//                for (int p = i, q = j; p < i + nCols; ++p, ++q) { //for performance: make p, q loop variables
////                    sum += this.denseData.data[p] * other_t.denseData.data[q]; //slow
//                    sum += thisData[p] * thatData[q];//TODO: to be parallelized
//                }
//
//                resultData[m++] = sum;
//            }
//        }
        try {
            ParallelExecutorInstanceHolder.instance.conditionalForLoop(
                    thisData.length >= LENGTH_THRESHOLD,
                    0, thisData.length, nCols,
                    new LoopBody() { //loop over rows of 'this'

                        @Override
                        public void run(int i) throws Exception {
                            int resultIndex = i / nCols * nThatCols;
                            final int rowLastIndex = i + nCols;
                            for (int j = 0; j < thatData.length; j += nCols) {//loop over cols of 'that'
                                double sum = 0.;
                                for (int p = i, q = j; p < rowLastIndex; ++p, ++q) { //for performance: make p, q loop variables
                                    sum += thisData[p] * thatData[q];
                                }

                                resultData[resultIndex++] = sum;
                            }
                        }
                    });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        DenseMatrix result = new DenseMatrix(resultData, nRows(), that.nCols());
        return result;
    }

    @Override
    public DenseMatrix ZERO() {
        return new DenseMatrix(nRows(), nCols());
    }

    @Override
    public DenseMatrix ONE() {
        final int nCols = nCols();

        int j = 0;
        double[] data = new double[nRows() * nCols];
        for (int i = 0; i < data.length && j < nCols; i += nCols) {//loop over rows
            data[i + j] = 1;
            ++j;
        }

        DenseMatrix result = new DenseMatrix(data, nRows(), nCols);
        return result;
    }

    @Override
    public DenseMatrix t() {
        final int nRows = nRows();
        final int nCols = nCols();

        final double[] thisData = storage.asArray();
        final double[] resultData = new double[thisData.length];

        /* original single-threaded algorithm */
//        for (int i = 0, j = 0; i < nRows; ++i, j += nCols) {
//            for (int p = 0, q = 0; p < nCols; ++p, q += nRows) { //fill result's i-th column
//                resultData[i + q] = thisData[j + p];
//            }
//        }

        try {
            ParallelExecutorInstanceHolder.instance.conditionalForLoop(
                    thisData.length >= LENGTH_THRESHOLD,
                    0, nRows, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    int j = i * nCols;
                    for (int p = 0, q = 0; p < nCols; ++p, q += nRows) { //fill result's i-th column
                        resultData[i + q] = thisData[j + p];
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        DenseMatrix result = new DenseMatrix(resultData, nCols, nRows);
        return result;
    }

    @Override
    public Vector multiply(final Vector v) {
        throwIfIncompatible4Multiplication(this, v);
        final int nCols = nCols();

        final double[] thisData = storage.asArray();
        final double[] resultData = new double[nRows()];

        /* original single-threaded version */
//        for (int i = 0; i < data.length; i += nCols) {//loop over rows of 'this'
//            double sum = 0.;
//            for (int j = 0; j < v.size(); ++j) {//loop over cols of 'that'
//                /*
//                 * dot product
//                 * result[p] = Sum{this.row(p) * other.column(1)}
//                 */
//                sum += data[i + j] * v.get(j + 1);//TODO: to be parallelized; use sub-row/column
//            }
//            result.set(i / nCols + 1, sum);
//        }
        try {
            ParallelExecutorInstanceHolder.instance.conditionalForLoop(
                    resultData.length >= LENGTH_THRESHOLD,
                    0, resultData.length, 1,
                    new LoopBody() {

                        @Override
                        public void run(int i) throws Exception {
                            double sum = 0.;
                            for (int j = 0; j < v.size(); ++j) { // loop over cols of 'that'
                                sum += thisData[i * nCols + j] * v.get(j + 1);
                            }
                            resultData[i] = sum;
                        }
                    });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        DenseVector result = new DenseVector(resultData);
        return result;
    }

    @Override
    public DenseMatrix scaled(double c) {
        DenseMatrix result = new DenseMatrix(storage.scaled(c), nRows(), nCols());
        return result;
    }

    @Override
    public Matrix opposite() {
        return scaled(-1);
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
        if (!DenseMatrix.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final DenseMatrix other = (DenseMatrix) obj;
        if (this.storage != other.storage && (this.storage == null || !this.storage.equals(other.storage))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        return hash;
    }
}
