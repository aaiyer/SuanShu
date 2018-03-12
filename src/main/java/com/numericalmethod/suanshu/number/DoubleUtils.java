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
package com.numericalmethod.suanshu.number;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * These are the utility functions to manipulate {@code double} and {@code int}.
 *
 * @author Haksun Li
 */
public class DoubleUtils {

    private DoubleUtils() {
        // no constructor for utility class
    }

    //<editor-fold defaultstate="collapsed" desc="comparison">
    /**
     * Compares two {@code double}s up to a precision.
     * This implementation is preferred to {@link Double#compare(double, double)} because
     * our implementation returns 0 (equality), i.e.,
     * {@code DoubleUtils.compare(0.0, -0.0)} returns {@code 0};
     * {@code Double.compare(0.0, -0.0)} returns {@code 1}.
     *
     * @param d1      a {@code double}
     * @param d2      a {@code double}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return 0 if {@code d1} is close enough to {@code d2}; 1 if <i>d1 &gt; d2</i>; -1 if <i>d1 &le; d2</i>
     */
    public static int compare(double d1, double d2, double epsilon) {
        if (d1 - d2 == 0) {
            return 0;
        }

        return (Math.abs(d1 - d2) <= epsilon) ? 0 : Double.compare(d1, d2);//it must be <= for |0 - 0| to work
    }

    /**
     * Check if {@code d} is zero.
     *
     * @param d       a {@code double}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if {@code d} is close enough to 0
     */
    public static boolean isZero(double d, double epsilon) {
        return (equal(d, 0, epsilon));
    }

    /**
     * Check if {@code d} is positive.
     *
     * @param d       a {@code double}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if {@code d} is bigger than 0 by {@code epsilon}
     */
    public static boolean isPositive(double d, double epsilon) {
        return (compare(d, 0, epsilon) == 1);
    }

    /**
     * Check if {@code d} is negative.
     *
     * @param d       a {@code double}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if {@code d} is smaller than 0 by {@code epsilon}
     */
    public static boolean isNegative(double d, double epsilon) {
        return (compare(d, 0, epsilon) == -1);
    }

    /**
     * Check if two {@code double}s are close enough, hence equal.
     *
     * @param d1      a {@code double}
     * @param d2      a {@code double}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if {@code d1} is close enough to {@code d2}, {@code false} otherwise
     */
    public static boolean equal(double d1, double d2, double epsilon) {
        return (compare(d1, d2, epsilon) == 0);
    }

    /**
     * Check if two {@code double} arrays are close enough, hence equal, entry-by-entry.
     *
     * @param d1      a {@code double[]}
     * @param d2      a {@code double[]}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries in {@code d1} are close enough to all entries in {@code d2}, {@code false} otherwise
     */
    public static boolean equal(double[] d1, double[] d2, double epsilon) {
        if (d1.length != d2.length) {
            return false;
        }

        for (int i = 0; i < d1.length; ++i) {
            if (!equal(d1[i], d2[i], epsilon)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if two 2D arrays, {@code double[][]}, are close enough, hence equal, entry-by-entry.
     *
     * @param d1      a {@code double[][]}
     * @param d2      a {@code double[][]}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries in {@code d1} are close enough to all entries in {@code d2}, {@code false} otherwise
     */
    public static boolean equal(double[][] d1, double[][] d2, double epsilon) {
        int nRow = d1.length;
        if (nRow != d2.length) {
            return false;
        }

        for (int i = 0; i < nRow; ++i) {
            if (d1[i] == null) {
                if ((d2[i] != null)) {//don't combine the two if's into one statement; or the last else won't work
                    return false;
                }
            } else if (d2[i] == null) {
                if (d1[i] != null) {
                    return false;
                }
            } else {//both d1[i] abd d2[i] are not null
                int ncol = d1[i].length;
                if (ncol != d2[i].length) {
                    return false;
                }

                for (int j = 0; j < ncol; ++j) {
                    if (!equal(d1[i][j], d2[i][j], epsilon)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Check if two {@code int} arrays, {@code int[]}, are equal, entry-by-entry.
     *
     * @param d1 an {@code int} array
     * @param d2 an {@code int} array
     * @return {@code true} if all entries in {@code d1} are the same as all entries in {@code d2}, {@code false} otherwise
     */
    public static boolean equal(int[] d1, int[] d2) {
        if (d1.length != d2.length) {
            return false;
        }

        for (int i = 0; i < d1.length; ++i) {
            if (d1[i] != d2[i]) {
                return false;
            }
        }

        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="operations on doubles">
    /**
     * Get the index of the maximum of the values.
     *
     * @param moveOnTies {@code true} if prefer the later one on ties
     * @param from       the initial index of the range to be considered
     * @param to         1 after the last index of the range to be considered
     * @param doubles    an array, {@code double[]}
     * @return the index of the biggest number
     */
    public static int maxIndex(boolean moveOnTies, int from, int to, double... doubles) {
        int result = from;

        for (int i = from + 1; i < to; ++i) {
            if (doubles[i] > doubles[result]) {
                result = i;
            }

            if (moveOnTies && doubles[i] == doubles[result]) {
                result = i;
            }
        }

        return result;
    }

    /**
     * Get the index of the maximum of the values.
     *
     * @param doubles an array, {@code double[]}
     * @return the index of the biggest number
     */
    public static int maxIndex(double... doubles) {
        return maxIndex(true, 0, doubles.length, doubles);
    }

    /**
     * Get the index of the minimum of the values.
     *
     * @param moveOnTies {@code true} if prefer the later one on ties
     * @param from       the initial index of the range to be considered
     * @param to         1 after the last index of the range to be considered
     * @param doubles    an array, {@code double[]}
     * @return the index of the smallest number
     */
    public static int minIndex(boolean moveOnTies, int from, int to, double... doubles) {
        int result = from;

        for (int i = from + 1; i < to; ++i) {
            if (doubles[i] < doubles[result]) {
                result = i;
            }

            if (moveOnTies && doubles[i] == doubles[result]) {
                result = i;
            }
        }

        return result;
    }

    /**
     * Get the index of the minimum of the values.
     *
     * @param doubles an array, {@code double[]}
     * @return the index of the smallest number
     */
    public static int minIndex(double... doubles) {
        return minIndex(true, 0, doubles.length, doubles);
    }

    /**
     * Apply a function <i>f</i> to each element in an array.
     *
     * @param doubles an array, {@code double[]}
     * @param f       a function to be applied to each element
     * @return the function outputs
     */
    public static double[] foreach(double[] doubles, UnivariateRealFunction f) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = f.evaluate(doubles[i]);
        }

        return result;
    }

    /**
     * Concatenate an array of arrays into one array.
     * The concatenated array is not sorted.
     *
     * @param arr an array, {@code double[]}
     * @return the concatenated array
     */
    public static double[] concat(double[]... arr) {
        int length = 0;
        for (int i = 0; i < arr.length; ++i) {
            length += arr[i].length;
        }

        double[] result = new double[length];
        for (int destPos = 0, i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[i].length; ++j) {
                System.arraycopy(arr[i], 0, result, destPos, arr[i].length);
            }
            destPos += arr[i].length;
        }
        return result;
    }

    /**
     * Reverse a {@code double} array.
     *
     * @param arr an array, {@code double[]}
     */
    public static void reverse(double... arr) {
        double tmp = 0;
        for (int i = 0, j = arr.length - 1; i < (arr.length / 2); i++, j--) {
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    /**
     * Reverse an {@code int} array.
     *
     * @param arr an array, {@code int[]}
     */
    public static void reverse(int... arr) {
        int tmp = 0;
        for (int i = 0, j = arr.length - 1; i < (arr.length / 2); i++, j--) {
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    /**
     * Sort an array using Shell sort.
     *
     * @param arr an array, {@code double[]}
     * @return a sorted array in ascending order
     * @see <a href="http://en.wikipedia.org/wiki/Shell_sort">Wikipedia: Shell sort</a>
     */
    public static int[] shellsort(double... arr) {
        int n = arr.length;
        int[] r = R.seq(1, n);

        int inc = Math.round(n / 2);
        while (inc > 0) {
            for (int i = inc; i < n; ++i) {
                double temp1 = arr[i];
                int temp2 = r[i];
                int j = i;
                while ((j >= inc) && (arr[j - inc] > temp1)) {
                    arr[j] = arr[j - inc];
                    r[j] = r[j - inc];
                    j -= inc;
                }
                arr[j] = temp1;
                r[j] = temp2;
            }
            inc = (int) Math.round(inc / 2.2);
        }

        return r;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="check properties">
    /**
     * Check if a {@code double} array contains only 0s, entry-by-entry.
     *
     * @param d       a {@code double} array
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if all entries in {@code d} are close enough to 0, {@code false} otherwise
     */
    public static boolean isAllZeros(double[] d, double epsilon) {
        boolean result = true;

        for (int i = 0; i < d.length; ++i) {
            if (!isZero(d[i], epsilon)) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * Check if a {@code double} array has any 0.
     *
     * @param d       a {@code double} array
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if an entry in {@code d} are close enough to 0, {@code false} otherwise
     */
    public static boolean hasZero(double[] d, double epsilon) {
        for (int i = 0; i < d.length; ++i) {
            if (isZero(d[i], epsilon)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a {@code double} is a number, i.e., it is not {@code ∞} or {@code NaN}.
     *
     * @param x a {@code double}
     * @return {@code true} if <i>x</i> is not {@code ∞} or {@code NaN}
     */
    public static boolean isNumber(double x) {
        return !Double.isInfinite(x) && !Double.isNaN(x);
    }

    /**
     * Check if an integer is a power of 2.
     *
     * @param n an integer
     * @return {@code true} if <i>n</i> is a power of 2.
     */
    public static boolean isPow2(int n) {
        return (n & (n - 1)) == 0;
    }

    /**
     * Check if a {@code double} array contains any duplicates.
     *
     * @param arr     a {@code double} array
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if there is a duplicate
     */
    public static boolean hasDuplicate(double[] arr, double epsilon) {
        double[] copy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy);

        for (int i = 1; i < copy.length; ++i) {
            if (equal(copy[i - 1], copy[i], epsilon)) {
                return true;
            }
        }

        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="casting">
    /**
     * Convert a {@code double} array to an {@code int} array, rounding down if necessary.
     *
     * @param arr a {@code double} array
     * @return an {@code int} array
     */
    public static int[] doubleArray2intArray(double... arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            result[i] = (int) arr[i];
        }

        return result;
    }

    /**
     * Convert an {@code int} array to a {@code double} array.
     *
     * @param arr an {@code int} array
     * @return a {@code double} array
     */
    public static double[] intArray2doubleArray(int... arr) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            result[i] = (double) arr[i];
        }

        return result;
    }

    /**
     * Convert a collection of numbers to a {@code double} array.
     *
     * @param numbers the collection of numbers
     * @return a {@code double} array containing the numbers in the input collection
     */
    public static double[] collection2DoubleArray(Collection<? extends Number> numbers) {
        double[] result = new double[numbers.size()];
        int i = 0;
        for (Number d : numbers) {
            result[i++] = d.doubleValue();
        }

        return result;
    }

    /**
     * Convert a {@code double} array to a list.
     *
     * @param arr a {@code double} array
     * @return a list of the numbers
     */
    public static List<Double> doubleArray2List(double... arr) {
        ArrayList<Double> result = new ArrayList<Double>(arr.length);

        for (int i = 0; i < arr.length; ++i) {
            result.add(new Double(arr[i]));
        }

        return result;
    }

    /**
     * Convert a collection of {@code Integer}s to an {@code int} array.
     *
     * @param integers a collection of integers
     * @return an {@code int} array of the integers
     */
    public static int[] collection2IntArray(Collection<Integer> integers) {
        int[] result = new int[integers.size()];
        int j = 0;
        for (Integer i : integers) {
            result[j++] = i;
        }

        return result;
    }

    /**
     * Convert a collection of {@code Long}s to a {@code long} array.
     *
     * @param integers a collection of long integers
     * @return a {@code long} array of the long integers
     */
    public static long[] collection2LongArray(Collection<Long> integers) {
        long[] result = new long[integers.size()];
        int j = 0;
        for (Long i : integers) {
            result[j++] = i;
        }

        return result;
    }

    /**
     * Convert an {@code int} array to a list.
     *
     * @param ary an {@code int} array
     * @return a list of the integers
     */
    public static List<Integer> intArray2List(int[] ary) {
        ArrayList<Integer> result = new ArrayList<Integer>(ary.length);

        for (int i = 0; i < ary.length; ++i) {
            result.add(new Integer(ary[i]));
        }

        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="rounding">
    /**
     * the schemes available to round a number.
     */
    public static enum RoundingScheme {

        /**
         * This rounding scheme is the same as in {@link java.lang.Math#round}. That is,
         * returning the closest long to the input.
         * The input is rounded to an integer by adding 1/2, taking the floor,
         * and casting it to {@code long}.
         */
        DEFAULT,
        /**
         * Always round up.
         * This is done by taking the ceiling of the input after subtracting a very tiny number.
         */
        UP,
        /**
         * Always round down.
         * This is done by taking the floor of the input after adding a very tiny number.
         */
        DOWN
    }

    /**
     * Round up or down a number to an integer.
     *
     * @param d      a number
     * @param scheme the rounding scheme
     * @return a near integer in {@code double}
     */
    public static double round(double d, RoundingScheme scheme) {
        switch (scheme) {
            case UP:
                return ceil(d - EPSILON);
            case DOWN:
                return floor(d + EPSILON);
            case DEFAULT:
            default:
                return Math.round(d);
        }
    }

    /**
     * Round a number to the precision specified.
     *
     * @param d     a number
     * @param scale the number of decimal points
     * @return an approximation of the number
     */
    public static double round(double d, int scale) {
        SuanShuUtils.assertArgument(scale >= 0, "scale >= 0");

        if (!DoubleUtils.isNumber(d)) {
            return d;
        }

        //we need exact precision here
        BigDecimal result = new BigDecimal(d);
        result = result.setScale(scale, BigDecimal.ROUND_HALF_UP);

        return result.doubleValue();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="errors">
    /**
     * Compute the absolute difference between {@code x1} and {@code x0}. This function is symmetric.
     * <blockquote><i>
     * ε = | x1 - x0 |
     * </i></blockquote>
     *
     * @param x1 {@code x1}
     * @param x0 {@code x0}
     * @return the absolute error
     */
    public static double absoluteError(double x1, double x0) {
        return Math.abs(x1 - x0);
    }

    /**
     * Compute the relative error for <i>{x1, x0}</i>.
     * This function is asymmetric.
     * <blockquote><i>
     * ε = | (x1 - x0) / x0 | = | x1/x0 - 1 |
     * </i></blockquote>
     *
     * @param x1 {@code x1}
     * @param x0 {@code x0}
     * @return the relative error
     */
    public static double relativeError(double x1, double x0) {
        return Math.abs(x1 / x0 - 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="printing">
    /**
     * Print out numbers to a string.
     *
     * @param arr a {@code double} array
     * @return a {@code String} representation of the numbers
     */
    public static String toString(double... arr) {
        final int linefeed = 10;

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            buffer.append(arr[i]).append(", ");

            if (i % linefeed == linefeed - 1) {
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }

    /**
     * Print out a 2D array, {@code double[][]} to a string.
     *
     * @param arr a {@code double[][]}
     * @return the string representation of the array
     */
    public static String toString(double[][] arr) {
        StringBuilder str = new StringBuilder();
        str.append("{");
        str.append("\n");

        for (int i = 0; i < arr.length; ++i) {
            str.append("{");

            for (int j = 0; j < arr[i].length; ++j) {
                str.append(arr[i][j]);
                if (j < arr[i].length - 1) {
                    str.append(", ");
                }
            }

            str.append("}");
            if (i < arr.length - 1) {
                str.append(",");
            }
            str.append("\n");
        }

        str.append("};\n");

        return str.toString();
    }
    //</editor-fold>
}
