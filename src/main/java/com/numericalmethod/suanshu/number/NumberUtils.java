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

import com.numericalmethod.suanshu.number.complex.Complex;
import java.lang.reflect.Constructor;

/**
 * These are the utility functions to manipulate {@link Number}s.
 *
 * @author Haksun Li
 */
public class NumberUtils {

    private NumberUtils() {
        // private constructor for utility class
    }

    /**
     * We need a precision parameter to determine whether two numbers are close enough to be treated as equal.
     * All subclasses of {@link Number} must implement this interface to work with {@link NumberUtils#compare(java.lang.Number, java.lang.Number, double)}.
     *
     * @param <T> a subclass of {@link Number}
     */
    public static interface Comparable<T extends Number> {

        /**
         * Compare {@code this} and {@code that} numbers up to a precision.
         *
         * @param that    a {@link Number}.
         * As a number can be represented in multiple ways, e.g., <i>0 = 0 + 0i</i>, the implementation may need to check {@link Object} type.
         * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
         * @return 0 if both numbers are close enough; +1 if {@code this} is bigger; -1 if {@code that} is bigger
         */
        public int compare(Number that, double epsilon);//TODO: can we get rid of this interface to simplify the code?
    }

    /**
     * Check the equality of two {@link Number}s, up to a precision.
     *
     * @param num1    a number
     * @param num2    a number
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the numbers are close enough, {@code false} otherwise
     */
    public static boolean equal(Number num1, Number num2, double epsilon) {
        return compare(num1, num2, epsilon) == 0;
    }

    /**
     * Compare two numbers. The two numbers must be of (possibly different) subclasses of {@link Number}.
     * <ul>
     * <li>If both can be cast to {@code double}, they are compared as {@code double}s; e.g., 2 vs. new Double(3);
     * <li>If one is a {@code double} but the other is not, the {@code double} is cast into the same field as the other number for comparison; e.g., 4 vs. 3 + 0i;
     * <li>If neither is a {@code double}, the two numbers must be of the same class for comparison; e.g., both are {@link Complex}.
     * </ul>
     *
     * @param num1    a number
     * @param num2    a number
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0; e.g., <i>1e-9</i>
     * @return 0 if {@code num1} is close enough to {@code num2}; 1 if {@code num1 &gt; num2}; -1 if {@code num1 &lt; num2}
     */
    public static int compare(Number num1, Number num2, double epsilon) {
        double d1 = Double.NaN;
        double d2 = Double.NaN;

        try {
            d1 = num1.doubleValue();
        } catch (Throwable t1) {
            d1 = Double.NaN;
        }

        try {
            d2 = num2.doubleValue();
        } catch (Throwable t2) {
            d2 = Double.NaN;
        }

        try {
            if (!Double.isNaN(d1) && !Double.isNaN(d2)) {//both numbers are double
                return DoubleUtils.compare(d1, d2, epsilon);
            } else if (Double.isNaN(d1) && Double.isNaN(d2)) {//both numbers are Object
                NumberUtils.Comparable<?> n1 = (NumberUtils.Comparable<?>) num1;
                return n1.compare(num2, epsilon);
            } else if (Double.isNaN(d1)) {//d2 is double; d1 is not
                Class<?> clazz = Class.forName(num1.getClass().getName());
                Constructor<?> ctor = clazz.getConstructor(double.class);
                Number n2 = (Number) ctor.newInstance(d2);//convert d2 to the same class as num1

                NumberUtils.Comparable<?> n1 = (NumberUtils.Comparable<?>) num1;
                return n1.compare(n2, epsilon);
            } else if (Double.isNaN(d2)) {//d1 is double; d2 is not
                Class<?> clazz = Class.forName(num2.getClass().getName());
                Constructor<?> ctor = clazz.getConstructor(double.class);
                Number n1 = (Number) ctor.newInstance(d1);//convert d1 to the same class as num2

                NumberUtils.Comparable<?> n2 = (NumberUtils.Comparable<?>) num2;
                return -n2.compare(n1, epsilon);//need to flip the sign b/c we want to compare n1 to n2
            }
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("the Number subclass must define the constructor that takes a 'double'");
        } catch (Throwable th) {
            throw new RuntimeException(String.format("unable to compare these two numbers: %s vs. %s", num1.toString(), num2.toString()));
        }

        throw new RuntimeException("unreachable");//make the complier happy
    }

    /**
     * Check if a number is a real number.
     * <p/>
     * <em>
     * TODO: this function needs to be extended or modified when a new {@link Number} subclass is implemented.
     * </em>
     *
     * @param number a number
     * @return {@code true} if the number is real, i.e., in R<sup>n</sup>
     */
    public static boolean isReal(Number number) {
        boolean result = true;//true for most NumberUtils subclasses

        if (number instanceof Complex) {
            result = Complex.isReal((Complex) number);
        }

        return result;
    }

    /**
     * Construct a number from a String. For example, some valid strings are:
     * <blockquote><pre>
     * "2"
     * "2."
     * "3 + 5i"
     * "1.23 - 4.56i"
     * "-1.23 - 4.56i"
     * "-1.23 - 4.56e-7i"
     * "-1.23 - 4.56+e7i"
     * "i"
     * </pre></blockquote>
     * Note: having spaces in the real part between sign and number is illegal, e.g.,
     * <blockquote><pre>
     * - 1.23+4.56i
     * + 1.23+4.56i
     * </pre></blockquote>
     * <em>
     * TODO: this function needs to be extended or modified when a new {@link Number} subclass is implemented.
     * </em>
     *
     * @param str a number in {@link String}
     * @return a number of type subclass-ing from {@link Number}, such as {@link Double}, {@link Complex}
     */
    public static Number parse(String str) {//TODO: extend this to Real?
        String s = str.trim();
        Number number = null;

        if (s.endsWith("i")) {//a complex number
            String realStr = "";
            String imaginaryStr = "";

            int separator = Math.max(s.lastIndexOf("+"), s.lastIndexOf("-"));
            if (separator > 1) {//check whether the +/- is part of the scientific notation in the imaginary part
                if ((s.charAt(separator - 1) == 'e') || (s.charAt(separator - 1) == 'E')) {
                    separator = Math.max(s.lastIndexOf("+", separator - 1), s.lastIndexOf("-", separator - 1));
                }
            }

            if (separator == -1) {//no + or - found; {//bi
                imaginaryStr = s.length() < 2 ? "" : s.substring(0, s.length() - 2);
            } else {//a +/- bi, +/- bi
                imaginaryStr = s.length() - 1 < separator + 1 ? "" : s.substring(separator + 1, s.length() - 1);
                realStr = separator < 0 ? "" : s.substring(0, separator);
            }

            double real = 0;
            if (realStr.trim().length() > 0) {
                real = java.lang.Double.parseDouble(realStr);
            }

            double imaginary = 1d;
            if (imaginaryStr.trim().length() > 0) {
                imaginary = java.lang.Double.parseDouble(imaginaryStr);
            }

            double sign = separator > -1 && s.substring(separator, separator + 1).equals("-") ? -1 : 1;
            number = new Complex(real, sign * imaginary);
        } else {//an integer, a double
            double d = java.lang.Double.parseDouble(s);
            number = new Double(d);
        }

        return number;
    }

    /**
     * Convert an array of numbers in {@link String} to an array of numbers in {@link Number}.
     *
     * @param strs an array of numbers in {@link String}
     * @return an array of {@link Number}s
     */
    public static Number[] parseArray(String... strs) {
        Number[] result = new Number[strs.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = parse(strs[i]);
        }
        return result;
    }
//    /**
//     * Check whether the list contains the numbers.
//     *
//     * <p>
//     * A match is {@code true} if
//     * <ol>
//     * <li>the type is the same, e.g.,
//     * <code>Double == Double</code>;
//     * <code>Double != Integer</code>
//     * <li>the value is exactly the same (by their
//     * <code>equal</code> and
//     * <code>hashcode</code> functions), e.g.,
//     * <code>(1.5 == 1.5)</code>;
//     * <code>(1.5 != 1.5 + ε)</code>
//     * </ol>
//     *
//     * @param numbers an array of {@link Number}s
//     * @return {@code true} if the matching criteria is met
//     */
//    public static boolean hasExactly(List<? extends Number> list, Number... numbers) {
//        boolean result = true;
//        for (Number num : numbers) {
//            if (!list.contains(num)) {
//                result = false;
//                break;
//            }
//        }
//        return result;
//    }
//    /**
//     * Check whether the list contains the numbers.
//     *
//     * <p>
//     * A match is {@code true} if
//     * <ul>
//     * <li>the type is the same, e.g.,
//     * <code>Double == Double</code>;
//     * <code>Double != Integer</code>
//     * <li>the value is exactly the same (by their
//     * <code>equal</code> and
//     * <code>hashcode</code> functions), e.g.,
//     * <code>(1.5 == 1.5)</code>;
//     * <code>(1.5 != 1.5 + ε)</code>
//     * </ul>
//     *
//     * @param numbers an array of numbers in {@link String}
//     * @return {@code true} if the matching criteria is met
//     */
//    public static boolean hasExactly(List<? extends Number> list, String... numbers) {
//        return hasExactly(list, parseArray(numbers));
//    }
//
//    /**
//     * Check whether the list contains the numbers.
//     *
//     * <p>
//     * A match is {@code true} if
//     * <ul>
//     * <li>the value is the same up to a precision
//     * </ul>
//     *
//     * <em>Note that the matched numbers do not need to be of the same type, or exactly identical in value.</em>
//     *
//     * @param precision two numbers are considered the same if their absolute difference is less than or equal to precision
//     * @param numbers   an array of {@link Number}s
//     * @return {@code true} if the matching criteria is met
//     */
//    public static boolean hasApproximately(List<? extends Number> list, double precision, Number... numbers) {//TODO: how to avoid modifying this after adding a new type?
//        boolean result = true;
//        for (Number input : numbers) {//inputs
//            boolean has = false;
//            for (Number n : list) {//container
//                if (equal(n, input, precision)) {
//                    has = true;
//                    break;
//                }
//            }
//            result &= has;
//            if (!result) {
//                break;
//            }
//        }
//        return result;
//    }
//
//    /**
//     * Check whether the list contains the numbers.
//     *
//     * <p>
//     * A match is {@code true} if
//     * <ul>
//     * <li>the value is the same up to a precision
//     * </ul>
//     *
//     * <em>Note that the matched numbers do not need to be of the same type, or exactly identical in value.</em>
//     *
//     * @param precision two numbers are considered the same if their absolute difference is less than or equal to precision
//     * @param numbers   an array of numbers in {@link String}
//     * @return {@code true} if the matching criteria is met
//     */
//    public static boolean hasApproximately(List<? extends Number> list, double precision, String... numbers) {
//        return hasApproximately(list, precision, parseArray(numbers));
//    }
}
