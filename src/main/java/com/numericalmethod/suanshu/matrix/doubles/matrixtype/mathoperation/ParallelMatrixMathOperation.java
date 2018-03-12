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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a multi-threaded implementation of the matrix math operations.
 *
 * @author Ken Yiu
 */
class ParallelMatrixMathOperation implements MatrixMathOperation {

    private final ParallelExecutor parallel = new ParallelExecutor();

    @Override
    public Matrix add(final MatrixAccess A1, final MatrixAccess A2) {
        throwIfDifferentDimension(A1, A2);

        final Matrix result = new DenseMatrix(A1.nRows(), A1.nCols());
        result.set(1, 1, 0.); // trigger space allocation in this main thread
        try {
            parallel.forLoop(1, A1.nRows() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    for (int j = 1; j <= A1.nCols(); ++j) {
                        result.set(i, j, A1.get(i, j) + A2.get(i, j));
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public Matrix minus(final MatrixAccess A1, final MatrixAccess A2) {
        throwIfDifferentDimension(A1, A2);

        final Matrix result = new DenseMatrix(A1.nRows(), A1.nCols());
        result.set(1, 1, 0.); // trigger space allocation in this main thread
        try {
            parallel.forLoop(1, A1.nRows() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    for (int j = 1; j <= A1.nCols(); ++j) {
                        result.set(i, j, A1.get(i, j) - A2.get(i, j));
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public Matrix multiply(final MatrixAccess A1, final MatrixAccess A2) {
        throwIfIncompatible4Multiplication(A1, A2);

        final Matrix result = new DenseMatrix(A1.nRows(), A2.nCols());
        result.set(1, 1, 0.); // trigger space allocation in this main thread
        try {
            parallel.forLoop(1, result.nRows() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    for (int j = 1; j <= result.nCols(); ++j) {
                        double sum = 0.;
                        for (int k = 1; k <= A1.nCols(); ++k) {
                            sum += A1.get(i, k) * A2.get(k, j);
                        }
                        result.set(i, j, sum);
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public Vector multiply(final MatrixAccess A, final Vector v) {
        throwIfIncompatible4Multiplication(A, v);

        final DenseVector result = new DenseVector(A.nRows());
        try {
            parallel.forLoop(1, result.size() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    double sum = 0.;
                    for (int j = 1; j <= v.size(); ++j) {
                        sum += A.get(i, j) * v.get(j);
                    }
                    result.set(i, sum);
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public Matrix scaled(final MatrixAccess A, final double scalar) {
        final Matrix result = new DenseMatrix(A.nRows(), A.nCols());
        result.set(1, 1, 0.); // trigger space allocation in this main thread
        try {
            parallel.forLoop(1, A.nRows() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    for (int j = 1; j <= A.nCols(); ++j) {
                        result.set(i, j, A.get(i, j) * scalar);
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public Matrix transpose(final MatrixAccess A) {
        final Matrix result = new DenseMatrix(A.nCols(), A.nRows());
        result.set(1, 1, 0.); // trigger space allocation in this main thread
        try {
            parallel.forLoop(1, A.nCols() + 1, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    for (int j = 1; j <= A.nRows(); ++j) {
                        result.set(i, j, A.get(j, i));
                    }
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
}
