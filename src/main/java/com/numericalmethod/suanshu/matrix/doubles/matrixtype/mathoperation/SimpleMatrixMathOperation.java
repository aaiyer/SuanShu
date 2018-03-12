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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a generic, single-threaded implementation of matrix math operations.
 * It assumes no knowledge of how a matrix is implemented.
 * Consequently, they are bound to have suboptimal performance.
 * A particular implementation of {@link Matrix} can improve performance by taking advantage of having access to the internal members.
 *
 * @author Haksun Li
 */
public class SimpleMatrixMathOperation implements MatrixMathOperation {

    @Override
    public Matrix add(MatrixAccess A1, MatrixAccess A2) {//this function is optimized for space complexity
        throwIfDifferentDimension(A1, A2);

        Matrix result = new DenseMatrix(A1.nRows(), A1.nCols());
        for (int i = 1; i <= A1.nRows(); ++i) {
            for (int j = 1; j <= A1.nCols(); ++j) {
                result.set(i, j, A1.get(i, j) + A2.get(i, j));
            }
        }

        return result;
    }

    @Override
    public Matrix minus(MatrixAccess A1, MatrixAccess A2) {//me function is optimized for space complexity
        throwIfDifferentDimension(A1, A2);

        Matrix result = new DenseMatrix(A1.nRows(), A1.nCols());
        for (int i = 1; i <= A1.nRows(); ++i) {
            for (int j = 1; j <= A1.nCols(); ++j) {
                result.set(i, j, A1.get(i, j) - A2.get(i, j));
            }
        }

        return result;
    }

    @Override
    public Matrix multiply(MatrixAccess A1, MatrixAccess A2) {
        throwIfIncompatible4Multiplication(A1, A2);

        Matrix result = new DenseMatrix(A1.nRows(), A2.nCols());
        for (int i = 1; i <= result.nRows(); ++i) {
            for (int j = 1; j <= result.nCols(); ++j) {
                double sum = 0.;
                for (int k = 1; k <= A1.nCols(); ++k) {
                    sum += A1.get(i, k) * A2.get(k, j);
                }
                result.set(i, j, sum);
            }
        }

        return result;
    }

    @Override
    public Vector multiply(MatrixAccess A, Vector v) {
        throwIfIncompatible4Multiplication(A, v);

        DenseVector result = new DenseVector(A.nRows());
        for (int i = 1; i <= result.size(); ++i) {
            double sum = 0.;
            for (int j = 1; j <= v.size(); ++j) {
                sum += A.get(i, j) * v.get(j);
            }
            result.set(i, sum);
        }

        return result;
    }

    @Override
    public Matrix scaled(MatrixAccess A, double scalar) {
        Matrix result = new DenseMatrix(A.nRows(), A.nCols());
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= A.nCols(); ++j) {
                result.set(i, j, A.get(i, j) * scalar);
            }
        }
        return result;
    }

    @Override
    public Matrix transpose(MatrixAccess A) {
        Matrix result = new DenseMatrix(A.nCols(), A.nRows());
        for (int i = 1; i <= A.nCols(); ++i) {
            for (int j = 1; j <= A.nRows(); ++j) {
                result.set(i, j, A.get(j, i));
            }
        }
        return result;
    }
}
