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
package com.numericalmethod.suanshu.vector.doubles.dense.operation;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * These are the utility functions that create new instances of vectors from existing ones.
 *
 * @author Haksun Li
 */
public class CreateVector {

    private CreateVector() {
        // private constructor for utility class
    }

    /**
     * Concatenate an array of vectors into one vector.
     *
     * @param vectors an array of vector, e.g., \((v_1, v_2, v_3, ...)\).
     * @return \(c(v_1, v_2, v_3, ...)\)
     */
    public static DenseVector concat(Vector... vectors) {//TODO: improve performance by forming 1D/2D array first then Vector/MatrixA1, A2, A3, ...
        int length = 0;
        for (int i = 0; i < vectors.length; ++i) {
            length += vectors[i] != null ? vectors[i].size() : 0;
        }

        DenseVector result = new DenseVector(length);
        for (int pos = 1, i = 0; i < vectors.length; ++i) {
            if (vectors[i] == null) {
                continue;
            }

            for (int j = 1; j <= vectors[i].size(); ++pos, ++j) {
                result.set(pos, vectors[i].get(j));
            }
        }

        return result;
    }

    /**
     * Construct a new vector in which each entry is the result of applying a
     * function to the corresponding entry of a vector.
     *
     * @param f      the function to be applied to each entry of a vector
     * @param vector a vector
     * @return <i>[f(v<sub>i</sub>)]</i> for all <i>i</i>s
     */
    public static DenseVector foreach(Vector vector, UnivariateRealFunction f) {
        double[] data = vector.toArray();
        return new DenseVector(DoubleUtils.foreach(data, f));
    }

    /**
     * Get a sub-vector from a vector.
     *
     * @param v    a vector
     * @param from the beginning index
     * @param to   the ending index
     * @return {@code v[from : to]}
     */
    public static DenseVector subVector(Vector v, int from, int to) {
        double[] data = Arrays.copyOfRange(v.toArray(), from - 1, to);
        return new DenseVector(data);
    }

    /**
     * Get the diagonal of a matrix as a vector.
     *
     * @param A a matrix
     * @return a vector whose entries are <i>A<sub>i</sub></i>
     */
    public static DenseVector diagonal(Matrix A) {
        int dim = Math.min(A.nRows(), A.nCols());
        double[] data = new double[dim];
        for (int i = 1; i <= dim; ++i) {
            data[i - 1] = A.get(i, i);
        }

        return new DenseVector(data);
    }

    /**
     * Get the super-diagonal of a matrix as a vector.
     *
     * @param A a matrix
     * @return a vector whose entries are <i>A<sub>i,i+1</sub></i>
     */
    public static DenseVector superDiagonal(Matrix A) {
        int dim = Math.min(A.nRows(), A.nCols()) - 1;
        SuanShuUtils.assertArgument(dim > 0, "1x1 matrix has no super-diagonal");

        double[] data = new double[dim];
        for (int i = 1; i <= dim; ++i) {
            data[i - 1] = A.get(i, i + 1);
        }

        return new DenseVector(data);
    }

    /**
     * Get the sub-diagonal of a matrix as a vector.
     *
     * @param A a matrix
     * @return a vector whose entries are <i>A<sub>i+1,i</sub></i>
     */
    public static DenseVector subDiagonal(Matrix A) {
        int dim = Math.min(A.nRows(), A.nCols()) - 1;
        SuanShuUtils.assertArgument(dim > 0, "1x1 matrix has no sub-diagonal");

        double[] data = new double[dim];
        for (int i = 1; i <= dim; ++i) {
            data[i - 1] = A.get(i + 1, i);
        }

        return new DenseVector(data);
    }
}
