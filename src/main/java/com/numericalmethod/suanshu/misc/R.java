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
package com.numericalmethod.suanshu.misc;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * These are some R-equivalent utility functions.
 * In general, for a R-equivalent function that correspond to a mathematical concept, SuanShu implements it as a class, e.g., {@link Rank}.
 * Otherwise, SuanShu implements it as a {@code static} utility function.
 * SuanShu uses the same (or similar) names and syntax whenever possible as in R to allow easy conversion from R code.
 *
 * @author Haksun Li
 * @see <a href="http://www.r-project.org/">The R Project for Statistical Computing</a>
 */
public class R {

    private R() {
        // utility class has no constructor
    }

    /**
     * Generate an array of {@code double}s of repeated values.
     *
     * @param value the repeated value
     * @param times the length of the array
     * @return an array of repetitions of value
     */
    public static double[] rep(double value, int times) {
        assertArgument(times >= 0, "the number of repetitions must be a natural number");

        double[] result = new double[times];
        for (int i = 0; i < times; ++i) {
            result[i] = value;
        }

        return result;
    }

    /**
     * Generate an array of {@code int}s of repeated values.
     *
     * @param value the repeated value
     * @param times the length of the array
     * @return an array of repetitions of value
     */
    public static int[] rep(int value, int times) {
        assertArgument(times >= 0, "the number of repetitions must be a natural number");

        int[] result = new int[times];
        for (int i = 0; i < times; ++i) {
            result[i] = value;
        }

        return result;
    }

    /**
     * Generate a sequence of {@code double}s from {@code from} up to {@code to} with increments {@code inc}.
     * The last number in the sequence is smaller than or equal to {@code to} for positive {@code inc}.
     * The last number in the sequence is bigger than or equal to {@code to} for negative {@code inc}.
     *
     * @param from the first number in the sequence
     * @param to   the bound of the sequence
     * @param inc  the increment
     * @return a sequence of {@code double}s
     */
    public static double[] seq(double from, double to, double inc) {
        double nInc = (to - from) / inc + 1;
        int length = (int) round(nInc + Constant.EPSILON, RoundingScheme.DOWN);
        double[] result = new double[length];

        for (int i = 0; i < length; ++i) {
            result[i] = from + i * inc;
        }

        return result;
    }

    /**
     * Generate a sequence of {@code int}s from {@code from} up to {@code to} with increments {@code inc}.
     * The last number in the sequence is smaller than or equal to {@code to} for positive {@code inc}.
     * The last number in the sequence is bigger than or equal to {@code to} for negative {@code inc}.
     *
     * @param from the first number in the sequence
     * @param to   the bound of the sequence
     * @param inc  the increment
     * @return a sequence of {@code int}s
     */
    public static int[] seq(int from, int to, int inc) {
        double nInc = (to - from) / inc + 1;
        int length = (int) round(nInc, RoundingScheme.DOWN);
        int[] result = new int[length];

        int j = 0;

        for (int i = from;;) {
            result[j++] = i;
            i += inc;

            if (((inc > 0) && (i > to)) || ((inc < 0) && (i < to))) {
                break;
            }
        }

        return result;
    }

    /**
     * Generate a sequence of {@code int}s from {@code from} up to {@code to} with increments 1.
     * That is, <i>[from, to]</i>, inclusively.
     *
     * @param from the first number in the sequence
     * @param to   the bound of the sequence
     * @return a sequence of {@code int}s
     */
    public static int[] seq(int from, int to) {
        int inc = from < to ? 1 : -1;
        return seq(from, to, inc);
    }

    /**
     * Get the lagged and iterated differences.
     *
     * @param arr   an array, {@code double[]}
     * @param lag   an integer indicating which lag to use
     * @param order an integer indicating the order of the difference.
     * This is the number of times {@code diff} is applied to the data.
     * E.g., {@code diff(x, 1, 2) = diff(diff(x, 1, 1), 1, 1)}.
     * @return the lagged and iterated differences
     */
    public static double[] diff(double[] arr, int lag, int order) {//TODO: make this iterative?
        assertArgument(arr.length > 0, "array length > 0");
        assertArgument(lag >= 1, "lag >= 1");
        assertArgument(order >= 1, "order >= 1");

        double[] result = new double[arr.length - lag];
        for (int j = 0, i = lag; i < arr.length; ++j, ++i) {
            result[j] = arr[i] - arr[j];
        }

        if (order > 1) {
            return diff(result, lag, order - 1);//recursion
        }

        return result;
    }

    /**
     * Get the first differences of an array.
     *
     * @param arr an array, {@code double[]}
     * @return the first differences
     */
    public static double[] diff(double[] arr) {
        return diff(arr, 1, 1);
    }

    /**
     * Get the lagged and iterated differences of vectors.
     *
     * @param arr   a {@code double[][]}; row view; must not be jagged
     * @param lag   an integer indicating which lag to use
     * @param order an integer indicating the order of the difference.
     * This is the number of times {@code diff} is applied to the data.
     * E.g., {@code diff(x, 1, 2) = diff(diff(x, 1, 1), 1, 1)}.
     * @return the lagged and iterated differences
     */
    public static double[][] diff(double[][] arr, int lag, int order) {//TODO: make more efficient?
        Matrix data = new DenseMatrix(arr);//each column is a time series

        double[][] columns = new double[data.nCols()][];
        for (int i = 1; i <= data.nCols(); ++i) {
            columns[i - 1] = diff(data.getColumn(i).toArray(), lag, order);
        }

        DenseMatrix result = new DenseMatrix(columns);
        return MatrixUtils.to2DArray(result.t());
    }

    /**
     * Get the first differences of an array of vectors.
     *
     * @param arr a {@code double[][]}; row view; must not be jagged
     * @return the first differences
     */
    public static double[][] diff(double[][] arr) {
        return diff(arr, 1, 1);
    }

    /**
     * Get the cumulative sums of the elements in an array.
     *
     * @param arr an array, {@code double[]}
     * @return {@code cumsum}
     */
    public static double[] cumsum(double[] arr) {
        assertArgument(arr.length > 0, "arr length > 0");

        double[] result = new double[arr.length];
        result[0] = arr[0];

        for (int i = 1; i < arr.length; ++i) {
            result[i] = result[i - 1] + arr[i];
        }

        return result;
    }

    /**
     * Get the cumulative sums of the elements in an array.
     *
     * @param arr an array, {@code int[]}
     * @return {@code cumsum}
     */
    public static int[] cumsum(int[] arr) {
        assertArgument(arr.length > 0, "arr length > 0");

        int[] result = new int[arr.length];
        result[0] = arr[0];

        for (int i = 1; i < arr.length; ++i) {
            result[i] = result[i - 1] + arr[i];
        }

        return result;
    }

    /**
     * Return a value with the same shape as {@code test}
     * which is filled with elements selected from either {@code yes} or {@code no}
     * depending on whether the element of test is {@code true} or {@code false}.
     */
    public static interface ifelse {

        /**
         * Decide whether <i>x</i> satisfies the {@code boolean} test.
         *
         * @param x a number
         * @return {@code true} if <i>x</i> satisfies the {@code boolean} test
         */
        public boolean test(double x);

        /**
         * Return value for a {@code true} element of test.
         *
         * @param x a number
         * @return a "yes" value
         */
        public double yes(double x);

        /**
         * Return value for a {@code false} element of test.
         *
         * @param x a number
         * @return a "no" value
         */
        public double no(double x);
    }

    /**
     * Return a value with the same shape as {@code test}
     * which is filled with elements selected from either {@code yes} or {@code no}
     * depending on whether the element of test is {@code true} or {@code false}.
     *
     * @param arr       an array
     * @param selection the {@code boolean} test to decide {@code true} or {@code false} and return values
     * @return "yes" or "no" return values
     */
    public static double[] ifelse(double[] arr, ifelse selection) {
        double[] result = new double[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            double x = arr[i];
            if (selection.test(x)) {
                result[i] = selection.yes(x);
            } else {
                result[i] = selection.no(x);
            }
        }

        return result;
    }

    /**
     * Decide whether <i>x</i> satisfies the {@code boolean} test.
     */
    public static interface which {

        /**
         * Decide whether <i>x</i> is to be selected.
         *
         * @param x     a number
         * @param index to index of <i>x</i>. Ignore {@code index} if you care only the value of <i>x</i> and not its position.
         * @return {@code true} if {@code which({x}, ...) = {x}}
         */
        public boolean isTrue(double x, int index);
    }

    /**
     * Get the indices of the array elements which satisfy the {@code boolean} test.
     *
     * @param arr  an array. {@code double[]}
     * @param test the {@code boolean} test to decide {@code true} or {@code false} (which to select)
     * @return the indices of the satisfying elements
     */
    public static int[] which(double[] arr, which test) {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < arr.length; ++i) {
            if (test.isTrue(arr[i], i)) {
                indices.add(i);
            }
        }

        int[] result = new int[indices.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = indices.get(i);
        }

        return result;
    }

    /**
     * Get the indices of the array elements which satisfy the {@code boolean} test.
     *
     * @param arr  an array. {@code int[]}
     * @param test the {@code boolean} test to decide {@code true} or {@code false} (which to select)
     * @return the indices of the satisfying elements
     */
    public static int[] which(int[] arr, which test) {
        double[] darr = new double[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            darr[i] = arr[i];
        }

        return which(darr, test);
    }

    /**
     * Select the array elements which satisfy the {@code boolean} test.
     * R does not have a 'select' function. The R-equivalent is {@code arr[which(...}]}.
     *
     * @param arr  an array, {@code double[]}
     * @param test the {@code boolean} test to determine which elements to be selected
     * @return the satisfying elements
     */
    public static double[] select(double[] arr, which test) {
        ArrayList<Double> result = new ArrayList<Double>();

        for (int i = 0; i < arr.length; ++i) {
            if (test.isTrue(arr[i], i)) {
                result.add(arr[i]);
            }
        }

        return collection2DoubleArray(result);
    }

    /**
     * Select the array elements which satisfy the {@code boolean} test.
     * R does not have a 'select' function. The R-equivalent is {@code arr[which(...}]}.
     *
     * @param arr  an array, {@code int[]}
     * @param test the {@code boolean} test to determine which elements to be selected
     * @return the satisfying elements
     */
    public static int[] select(int[] arr, which test) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < arr.length; ++i) {
            if (test.isTrue(arr[i], i)) {
                result.add(arr[i]);
            }
        }

        return collection2IntArray(result);
    }

    /**
     * Get a sub-array of the original array with the given indices.
     * The R-equivalent is {@code arr[indices]}.
     *
     * @param arr     an array, {@code double[]}
     * @param indices an array of indices to select
     * @return {@code arr[indices]}.
     */
    public static double[] subarray(double[] arr, int[] indices) {
        assertArgument(arr.length > 0, "arr length > 0");
        assertArgument(indices.length > 0, "indices length > 0");
        assertArgument(arr.length >= indices.length, "too many indices");

        double[] result = new double[indices.length];

        for (int i = 0; i < indices.length; ++i) {
            result[i] = arr[indices[i]];
        }

        return result;
    }

    /**
     * Get a sub-array of the original array with the given indices.
     * The R-equivalent is {@code arr[indices]}.
     *
     * @param arr     an array, {@code int[]}
     * @param indices an array of indices to select
     * @return {@code arr[indices]}.
     */
    public static int[] subarray(int[] arr, int[] indices) {
        assertArgument(arr.length > 0, "arr length > 0");
        assertArgument(indices.length > 0, "indices length > 0");
        assertArgument(arr.length >= indices.length, "too many indices");

        int[] result = new int[indices.length];

        for (int i = 0; i < indices.length; ++i) {
            result[i] = arr[indices[i]];
        }

        return result;
    }

    /**
     * Sort an array either in ascending or descending order.
     *
     * @param arr       an array, {@code int[]}
     * @param ascending {@code true} if arranging elements in ascending order; false if descending order
     * @return a sorted array
     */
    public static int[] order(double[] arr, boolean ascending) {
        assertArgument(arr.length > 0, "arr length > 0");

        double[] copy = Arrays.copyOf(arr, arr.length);
        int[] order = shellsort(copy);

        if (!ascending) {
            reverse(order);
        }

        return order;
    }

    /**
     * Sort an array in ascending order.
     *
     * @param arr an array, {@code int[]}
     * @return a sorted array in ascending order
     */
    public static int[] order(double[] arr) {
        return order(arr, true);
    }

    /**
     * Concatenate {@code String}s into one {@code String}.
     *
     * @param collection the collection of strings to be concatenated
     * @param delimiter  the separation symbol for the strings
     * @return the concatenated string
     */
    public static String paste(Collection<String> collection, String delimiter) {
        if (collection.isEmpty()) {
            return "";
        }

        Iterator<String> it = collection.iterator();
        StringBuilder buffer = new StringBuilder(it.next());
        while (it.hasNext()) {
            buffer.append(delimiter).append(it.next());
        }

        return buffer.toString();
    }
}
