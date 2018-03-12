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

import com.numericalmethod.suanshu.DeepCopyable;
import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.CompositeDoubleArrayOperation;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayOperation;
import com.numericalmethod.suanshu.number.doublearray.ParallelDoubleArrayOperation;
import com.numericalmethod.suanshu.number.doublearray.SimpleDoubleArrayOperation;

/**
 * This implementation of the storage of a dense matrix stores the data of a 2D matrix as an 1D array.
 * In general, computing index for a {@code double[]} is faster than that for a {@code double[][]}.
 * Hence for most operations, e.g., element-by-element add, minus, this implementation has a better performance.
 *
 * @author Haksun Li
 */
//In Java, an initialized data array (1D or 2D) seems to have all entries 0. We use this "assumption" to save an Arrays.fill.
public abstract class DenseData implements MatrixAccess, DeepCopyable {

    private static final int SIZE_THRESHOLD = 100 * 100;
    private static final DoubleArrayOperation DEFAULT_OPERATION =
            new CompositeDoubleArrayOperation(
            SIZE_THRESHOLD,
            new SimpleDoubleArrayOperation(),
            new ParallelDoubleArrayOperation());
    /** stores the values of matrix entries */
    private double[] data = null; //caller throws NullPointerException if data is not initialized
    private final DoubleArrayOperation doubleArrayOperation;

    /**
     * Construct a storage, and specify the implementations of the element-wise operations.
     *
     * @param data                 the data
     * @param doubleArrayOperation the implementations of the element-wise operations
     */
    public DenseData(double[] data, DoubleArrayOperation doubleArrayOperation) {
        this.doubleArrayOperation = doubleArrayOperation;
        this.data = data;//NO COPYING for performance
    }

    /**
     * Construct a storage.
     *
     * @param data the data
     */
    public DenseData(double[] data) {
        this(data, DEFAULT_OPERATION);
    }

    /**
     * Construct a storage and initialize all data content to 0.
     *
     * @param length the size of the storage
     */
    public DenseData(int length) {
        this(new double[length]);
    }

    /**
     * Cast this data structure as a {@code double[]}.
     * Modifying the returned value modifies the internal data.
     *
     * @return itself as a {@code double[]}
     */
    public double[] asArray() {
        return data;
    }

    /**
     * Add up the elements in {@code this} and {@code that}, element-by-element.
     *
     * @param that an array of data
     * @return the sums of elements
     */
    public double[] add(DenseData that) {
        return doubleArrayOperation.add(data, that.data);
    }

    /**
     * Subtract the elements in {@code this} by {@code that}, element-by-element.
     *
     * @param that an array of data
     * @return the differences of elements
     */
    public double[] minus(DenseData that) {
        return doubleArrayOperation.minus(data, that.data);
    }

    /**
     * Multiply the elements in {@code this} by a scalar, element-by-element.
     *
     * @param c the scaling constant
     * @return the scaled elements
     */
    public double[] scaled(double c) {
        return doubleArrayOperation.scaled(data, c);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DenseData that = (DenseData) obj;
        if (this.data != that.data && (this.data == null || !DoubleUtils.equal(this.data, that.data, 0))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }
}
