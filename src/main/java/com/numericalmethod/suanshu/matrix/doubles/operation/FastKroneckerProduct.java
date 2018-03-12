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

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfInvalidColumn;
import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfInvalidRow;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a fast and memory-saving implementation of computing the Kronecker product.
 * It computes the matrix entry values only on demand by calling the {@link #get(int, int)} method.
 * If, however, the Kronecker product is used for matrix operations frequently,
 * the class {@link KroneckerProduct}, which computes all entries at
 * construction, should be used instead.
 * <p/>
 * This class is immutable.
 *
 * @author Ken Yiu
 * @see KroneckerProduct
 */
public class FastKroneckerProduct implements Matrix {

    private final Matrix A;
    private final Matrix B;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a Kronecker product for read-only.
     *
     * @param A a matrix
     * @param B a matrix
     */
    public FastKroneckerProduct(Matrix A, Matrix B) {
        this.A = A.deepCopy();
        this.B = B.deepCopy();
    }

    @Override
    public int nRows() {
        return A.nRows() * B.nRows();
    }

    @Override
    public int nCols() {
        return A.nCols() * B.nCols();
    }

    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        throw new MatrixAccessException("this matrix is immutable");
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        throwIfInvalidRow(this, i);
        throwIfInvalidColumn(this, j);

        int u = i - 1;
        int v = j - 1;
        int p = B.nRows();
        int q = B.nCols();
        return A.get(u / p + 1, v / q + 1) * B.get(u % p + 1, v % q + 1);
    }

    @Override
    public DenseVector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        double[] resultData = new double[this.nCols()];
        for (int j = 0; j < resultData.length; ++j) {
            resultData[j] = this.get(i, j + 1);
        }
        return new DenseVector(resultData);
    }

    @Override
    public DenseVector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        double[] resultData = new double[this.nRows()];
        for (int i = 0; i < resultData.length; ++i) {
            resultData[i] = this.get(i + 1, j);
        }
        return new DenseVector(resultData);
    }

    @Override
    public Matrix add(Matrix that) {
        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        return math.multiply(this, that);
    }

    @Override
    public Vector multiply(Vector v) {
        return math.multiply(this, v);
    }

    @Override
    public Matrix scaled(double scalar) {
        return math.scaled(this, scalar);
    }

    @Override
    public Matrix opposite() {
        return this.scaled(-1);
    }

    @Override
    public DenseMatrix ZERO() {
        return new DenseMatrix(this.nRows(), this.nCols()).ZERO();
    }

    @Override
    public DenseMatrix ONE() {
        return new DenseMatrix(this.nRows(), this.nCols()).ONE();
    }

    @Override
    public Matrix t() {
        return math.transpose(this);
    }

    /**
     * Return {@code this} as this {@link Matrix} is immutable.
     *
     * @return {@code this}
     */
    @Override
    public FastKroneckerProduct deepCopy() {
        return this;
    }
}
