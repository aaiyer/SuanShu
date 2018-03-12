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

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import static com.numericalmethod.suanshu.number.DoubleUtils.foreach;
import com.numericalmethod.suanshu.number.Real;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import com.numericalmethod.suanshu.vector.doubles.IsVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.Arrays;

/**
 * This class implements the standard, dense, {@code double} based vector representation.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Euclidean_vector">Wikipedia: Euclidean vector</a>
 */
public class DenseVector implements Vector {//TODO: java.io.Serializable {

    /** data storage */
    private double[] data;
    /** the length of this vector, counting from 1 */
    private final int length;
    private VectorMathOperation math = new VectorMathOperation();

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    /**
     * Construct a vector.
     *
     * @param length the length of this vector
     */
    public DenseVector(int length) {
        this.length = length;
        this.data = new double[this.length];
    }

    /**
     * Construct a vector, initialized by repeating a value.
     *
     * @param length the length of this vector
     * @param value  the initial value
     */
    public DenseVector(int length, double value) {
        this.length = length;
        this.data = new double[this.length];
        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] = value;
        }
    }

    /**
     * Construct a vector, initialized by a {@code double[]}.
     *
     * @param data an 1D array
     */
    public DenseVector(double... data) {
        this.length = data.length;
        this.data = data;
    }

    /**
     * Construct a vector, initialized by a {@code int[]}.
     *
     * @param data an 1D array
     */
    public DenseVector(int[] data) {
        this.length = data.length;
        this.data = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            this.data[i] = data[i];
        }
    }

    /**
     * Construct a vector from a column or row matrix.
     *
     * @param A a column or row matrix
     */
    public DenseVector(Matrix A) {
        if (A.nCols() == 1) {
            this.data = A.getColumn(1).toArray();
            this.length = this.data.length;
            return;
        } else if (A.nRows() == 1) {
            this.data = A.getRow(1).toArray();
            this.length = this.data.length;
            return;
        }

        throw new IllegalArgumentException("A must be a column or row matrix");
    }

    /**
     * Cast any vector to a {@code DenseVector}.
     *
     * @param v a vector
     */
    public DenseVector(Vector v) {
        this.length = v.size();
        this.data = v.toArray();
    }

    /**
     * Copy constructor.
     *
     * @param vector a vector
     */
    public DenseVector(DenseVector vector) {
        this.length = vector.length;
        this.data = Arrays.copyOf(vector.data, length);
    }
    //</editor-fold>

    @Override
    public int size() {
        return length;
    }

    @Override
    public void set(int i, double value) {
        IsVector.throwIfInvalidIndex(this, i);
        data[i - 1] = value;
    }

    /**
     * Replace a sub-vector {@code v[from : replacement.length]} by a replacement starting at position {@code from}.
     *
     * @param from        the starting position of the replacement
     * @param replacement a vector for substitution
     * @throws IllegalArgumentException if the replacement length exceeds the end of this vector
     */
    public void set(int from, DenseVector replacement) {
        if (from + replacement.length - 1 > this.length) {
            throw new IllegalArgumentException("the replacement vector length starting from 'from' exceeds the original vector length");
        }
        for (int pos = from, i = 1; i <= replacement.length; ++pos, ++i) {
            this.set(pos, replacement.get(i));
        }
    }

    @Override
    public double get(int i) {
        IsVector.throwIfInvalidIndex(this, i);
        return data[i - 1];
    }

    //<editor-fold defaultstate="collapsed" desc="arithmetic operations">
    @Override
    public Vector add(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);

        if (that instanceof DenseVector) {
            DenseVector other = (DenseVector) that;
            DenseVector result = new DenseVector(length);
            for (int i = 0; i < length; ++i) {
                result.data[i] = this.data[i] + other.data[i];
            }

            return result;
        }

        return math.add(this, that);
    }

    @Override
    public Vector minus(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);

        if (that instanceof DenseVector) {
            DenseVector other = (DenseVector) that;
            DenseVector result = new DenseVector(length);
            for (int i = 0; i < length; ++i) {
                result.data[i] = this.data[i] - other.data[i];
            }

            return result;
        }

        return math.minus(this, that);
    }

    @Override
    public DenseVector scaled(double c) {
        DenseVector result = new DenseVector(length);
        for (int i = 0; i < length; ++i) {
            result.data[i] = this.data[i] * c;
        }

        return result;
    }

    @Override
    public Vector scaled(Real c) {
        return scaled(c.doubleValue());
    }

    @Override
    public Vector opposite() {
        return scaled(-1);
    }

    @Override
    public Vector multiply(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);

        if (that instanceof DenseVector) {
            DenseVector other = (DenseVector) that;
            double[] result = new double[this.length];
            for (int i = 0; i < this.length; ++i) {
                result[i] = this.get(i + 1) * other.get(i + 1);
            }

            return new DenseVector(result);
        }

        return math.multiply(this, that);
    }

    @Override
    public Vector divide(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);

        if (that instanceof DenseVector) {
            DenseVector other = (DenseVector) that;
            double[] result = new double[this.length];
            for (int i = 0; i < this.length; ++i) {
                result[i] = this.get(i + 1) / other.get(i + 1);
            }

            return new DenseVector(result);
        }

        return math.divide(this, that);
    }

    @Override
    public DenseVector add(final double scalar) {
        double[] result = foreach(data, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x + scalar;
            }
        });

        return new DenseVector(result);
    }

    @Override
    public DenseVector minus(final double scalar) {
        double[] result = foreach(data, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x - scalar;
            }
        });

        return new DenseVector(result);
    }

    @Override
    public DenseVector pow(final double scalar) {
        double[] result = foreach(data, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return Math.pow(x, scalar);
            }
        });

        return new DenseVector(result);
    }
    //</editor-fold>

    @Override
    public double norm(int p) {
        if (p == Integer.MAX_VALUE) {
            return max(abs(toArray()));
        }

        if (p == Integer.MIN_VALUE) {
            return min(abs(toArray()));
        }

        double result = 0;
        for (int i = 0; i < length; ++i) {
            result += Math.pow(data[i], p);
        }

        result = Math.pow(result, 1. / p);
        return result;
    }

    @Override
    public double norm() {
        return norm(2);
    }

    @Override
    public double angle(Vector that) {
        return math.angle(this, that);
    }

    @Override
    public double innerProduct(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);

        if (that instanceof DenseVector) {
            DenseVector other = (DenseVector) that;
            double result = FunctionOps.dotProduct(this.data, other.data);
            return result;
        }

        return math.innerProduct(this, that);
    }

    @Override
    public DenseVector ZERO() {
        return new DenseVector(length);
    }

    @Override
    public double[] toArray() {
        double[] result = Arrays.copyOf(data, data.length);
        return result;
    }

    @Override
    public DenseVector deepCopy() {
        return new DenseVector(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[");

        for (int i = 0; i < length; ++i) {
            result.append(String.format("%f, ", data[i]));
        }

        //replace the last ',' by ']'
        result.setCharAt(result.length() - 2, ']');

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Vector)) {
            return false;
        }
        final Vector that = (Vector) obj;
        if (!AreMatrices.equal(this, that, 0.)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.length;
        hash = 19 * hash + Arrays.hashCode(this.data);
        return hash;
    }
}
