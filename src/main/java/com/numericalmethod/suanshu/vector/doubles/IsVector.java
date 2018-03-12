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
package com.numericalmethod.suanshu.vector.doubles;

import com.numericalmethod.suanshu.number.DoubleUtils;

/**
 * These are the utility functions to validate input arguments for vector operations.
 *
 * @author Ken Yiu
 */
public class IsVector {

    private IsVector() {
        // utility class has no constructor
    }

    /**
     * This is the exception thrown when an operation is performed on
     * two vectors with different sizes.
     */
    public static class SizeMismatch extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * Construct an instance of {@code SizeMismatch}.
         *
         * @param size1 size 1
         * @param size2 size 2
         */
        public SizeMismatch(int size1, int size2) {
            super(String.format(
                    "vectors do not have the same size: %d and %d",
                    size1,
                    size2));
        }
    }

    /**
     * This is the exception thrown when any invalid access to a
     * {@link Vector} instance is detected. For example, out-of-range index or
     * write to an immutable instance.
     */
    public static class VectorAccessException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * Construct an instance of {@code VectorAccessException} for out-of-range access.
         *
         * @param size  the vector size
         * @param index the out-of-range index
         */
        public VectorAccessException(int size, int index) {
            super(String.format(
                    "out-of-range [1:%d] index: %d",
                    size,
                    index));
        }

        /**
         * Construct an instance of {@code VectorAccessException}.
         *
         * @param msg the error message
         */
        public VectorAccessException(String msg) {
            super(msg);
        }
    }

    /**
     * Check if the input vectors have the same size.
     *
     * @param v1 a vector
     * @param v2 a vector
     * @throws SizeMismatch if sizes do not match
     */
    public static void throwIfNotEqualSize(Vector v1, Vector v2) {
        if (v1.size() != v2.size()) {
            throw new SizeMismatch(v1.size(), v2.size());
        }
    }

    /**
     * Check if an index is a valid index.
     *
     * @param v     a vector
     * @param index a vector index
     * @throws VectorAccessException if the index is invalid
     */
    public static void throwIfInvalidIndex(Vector v, int index) {
        if (index < 1 || index > v.size()) {
            throw new VectorAccessException(v.size(), index);
        }
    }

    /**
     * Check the equality of two vectors up to a precision.
     * Two vectors are equal if
     * <ol>
     * <li>the dimensions are the same;
     * <li>all entries are equal
     * </ol>
     *
     * @param v1      a vector
     * @param v2      another vector
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries are equal, entry by entry
     */
    public static boolean equal(Vector v1, Vector v2, double epsilon) {
        if (v1.size() != v2.size()) {
            return false;
        }

        for (int i = 1; i <= v1.size(); ++i) {
            if (DoubleUtils.compare(v1.get(i), v2.get(i), epsilon) != 0) {//compare entry-by-entry
                return false;
            }
        }

        return true;
    }
}
