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
package com.numericalmethod.suanshu.vector.doubles.dense;

import com.numericalmethod.suanshu.number.Real;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import com.numericalmethod.suanshu.vector.doubles.IsVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a generic implementation of the math operations of {@code double} based {@link Vector}.
 * It assumes no knowledge of how a vector is implemented.
 * A particular implementation of {@link Vector} can improve performance by taking advantage of having access to the internal members.
 *
 * @author Ken Yiu
 */
public class VectorMathOperation {

    public DenseVector add(Vector v1, Vector v2) {
        IsVector.throwIfNotEqualSize(v1, v2);

        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) + v2.get(i));
        }

        return result;
    }

    public DenseVector minus(Vector v1, Vector v2) {
        IsVector.throwIfNotEqualSize(v1, v2);

        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) - v2.get(i));
        }

        return result;
    }

    public DenseVector multiply(Vector v1, Vector v2) {
        IsVector.throwIfNotEqualSize(v1, v2);

        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) * v2.get(i));
        }

        return result;
    }

    public DenseVector divide(Vector v1, Vector v2) {
        IsVector.throwIfNotEqualSize(v1, v2);

        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) / v2.get(i));
        }

        return result;
    }

    public double innerProduct(Vector v1, Vector v2) {
        IsVector.throwIfNotEqualSize(v1, v2);

        int size = v1.size();
        double result = 0.;
        for (int i = 1; i <= size; ++i) {
            result += v1.get(i) * v2.get(i);
        }

        return result;
    }

    public DenseVector pow(Vector v1, double scalar) {
        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, Math.pow(v1.get(i), scalar));
        }
        return result;
    }

    public DenseVector scaled(Vector v1, double c) {
        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, c * v1.get(i));
        }

        return result;
    }

    public DenseVector add(Vector v1, double c) {
        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) + c);
        }

        return result;
    }

    public DenseVector minus(Vector v1, double c) {
        int size = v1.size();
        DenseVector result = new DenseVector(size);
        for (int i = 1; i <= size; ++i) {
            result.set(i, v1.get(i) - c);
        }

        return result;
    }

    public DenseVector scaled(Vector v1, Real scalar) {
        return scaled(v1, scalar.doubleValue());
    }

    public DenseVector opposite(Vector v1) {
        return scaled(v1, -1);
    }

    public double angle(Vector v1, Vector v2) {
        double result = innerProduct(v1, v2);
        result /= v1.norm();
        result /= v2.norm();
        result = Math.acos(result);
        return result;
    }

    public double norm(Vector v1, int p) {
        double[] arr = new double[v1.size()];
        for (int i = 0; i < v1.size(); ++i) {
            arr[i] = v1.get(i + 1);
        }


        if (p == Integer.MAX_VALUE) {
            return max(abs(arr));
        }

        if (p == Integer.MIN_VALUE) {
            return min(abs(arr));
        }

        double result = 0;
        for (int i = 0; i < v1.size(); ++i) {
            result += Math.pow(arr[i], p);
        }

        result = Math.pow(result, 1. / p);
        return result;
    }

    public double norm(Vector v1) {
        return norm(v1, 2);
    }
}
