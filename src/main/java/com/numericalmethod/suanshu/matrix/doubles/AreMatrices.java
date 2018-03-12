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
package com.numericalmethod.suanshu.matrix.doubles;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * These are the boolean operators that take two or more matrices or vectors and compare their properties.
 *
 * @author Haksun Li
 */
public class AreMatrices {

    private AreMatrices() {
        // private constructor for utility class
    }

    /**
     * Check the equality of two matrices up to a precision.
     * Two matrices are equal if
     * <ol>
     * <li>the dimensions are the same;
     * <li>all entries are equal
     * </ol>
     *
     * @param A1      a matrix
     * @param A2      a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries are equal, entry by entry
     */
    public static boolean equal(Matrix A1, Matrix A2, double epsilon) {
        if (!DimensionCheck.isSameDimension(A1, A2)) {
            return false;
        }

        for (int i = 1; i <= A1.nRows(); ++i) {
            for (int j = 1; j <= A1.nCols(); ++j) {
                if (compare(A1.get(i, j), A2.get(i, j), epsilon) != 0) {//compare entry-by-entry
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if two vectors are equal, i.e., <i>v1 - v2</i> is a zero vector, up to a precision.
     *
     * @param v1      a vector
     * @param v2      a vector
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries are equal, entry by entry
     */
    public static boolean equal(Vector v1, Vector v2, double epsilon) {
        if (v1.size() != v2.size()) {
            return false;
        }

        for (int i = 1; i <= v1.size(); ++i) {
            if (compare(v1.get(i), v2.get(i), epsilon) != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if two vectors are orthogonal, i.e., <i>v1 ∙ v2 == 0</i>.
     *
     * @param v1      a vector
     * @param v2      a vector
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} the two vectors are orthogonal
     */
    public static boolean orthogonal(Vector v1, Vector v2, double epsilon) {
        return (compare(0, v1.innerProduct(v2), epsilon) == 0);
    }

    /**
     * Check if a set of vectors are orthogonal, i.e., for any <i>v1</i>, <i>v2</i> in <i>v</i>, <i>v1 ∙ v2 == 0</i>.
     *
     * @param v       a set of vectors
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if any two vectors are orthogonal
     */
    public static boolean orthogonal(Vector[] v, double epsilon) {
        for (int i = 0; i < v.length; ++i) {
            for (int j = i + 1; j < v.length; ++j) {
                if (!orthogonal(v[i], v[j], epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if two vectors are orthogonormal.
     * Two vectors are orthogonormal if
     * <ol>
     * <li><i>{v1, v2}</i> are orthogonal;
     * <li><i>|v1| = |v2| = 1</i>.
     * </ol>
     *
     * @param v1      a vector
     * @param v2      a vector
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the two vectors are orthogonormal
     */
    public static boolean orthogonormal(Vector v1, Vector v2, double epsilon) {
        return ((compare(0, v1.innerProduct(v2), epsilon) == 0)
                && (compare(1, v1.norm(), epsilon) == 0)
                && (compare(1, v2.norm(), epsilon) == 0));
    }

    /**
     * Check if a set of vectors are orthogonormal.
     *
     * @param v       a set of vectors
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if any two vectors are orthogonormal
     */
    public static boolean orthogonormal(Vector[] v, double epsilon) {
        for (int i = 0; i < v.length; ++i) {
            for (int j = i + 1; j < v.length; ++j) {
                if (!orthogonormal(v[i], v[j], epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }
}
