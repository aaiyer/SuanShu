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
package com.numericalmethod.suanshu.number.doublearray;

/**
 * These are the math functions that operate on {@code double[]}.
 * In other words, this provide an array version of {@link java.lang.Math}.
 *
 * <p>
 * These functions are static and stateless.
 *
 * @author Haksun Li
 */
public class DoubleArrayMath {

    private DoubleArrayMath() {
        // no constructor for utility class
    }

    /**
     * Get the minimum of the values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the smallest of the inputs
     */
    public static double min(double... doubles) {
        double result = Double.POSITIVE_INFINITY;

        for (double d : doubles) {
            if (d < result) {
                result = d;
            }
        }

        return result;
    }

    /**
     * Get the maximum of the values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the biggest of the inputs
     */
    public static double max(double... doubles) {
        double result = Double.NEGATIVE_INFINITY;

        for (double d : doubles) {
            if (d > result) {
                result = d;
            }
        }

        return result;
    }

    /**
     * Get the maximum of the values.
     *
     * @param integers an array of <tt>int</tt>s
     * @return the biggest of the inputs
     */
    public static int max(int... integers) {
        int result = Integer.MIN_VALUE;

        for (int d : integers) {
            if (d > result) {
                result = d;
            }
        }

        return result;
    }

    /**
     * Get the absolute values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the absolute values of the inputs
     */
    public static double[] abs(double[] doubles) {//we use double[] instead of double... to avoid conflict with Math.abs
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = Math.abs(doubles[i]);
        }

        return result;
    }

    /**
     * Get the sum of the values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the sum of the inputs
     */
    public static double sum(double... doubles) {
        double result = 0;

        for (double d : doubles) {
            result += d;
        }

        return result;
    }

    /**
     * Get the sum of the values.
     *
     * @param integers an array of <tt>int</tt>s
     * @return the sum of the inputs
     */
    public static int sum(int... integers) {
        int result = 0;

        for (int d : integers) {
            result += d;
        }

        return result;
    }

    /**
     * Get the sum of squares of the values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the sum of squares of the inputs
     */
    public static double sum2(double... doubles) {
        double result = 0;

        for (double d : doubles) {
            result += d * d;
        }

        return result;
    }

    /**
     * Get the logs of values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the logs of the inputs
     */
    public static double[] log(double[] doubles) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = Math.log(doubles[i]);
        }

        return result;
    }

    /**
     * Get the exponentials of values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the exponentials of the inputs
     */
    public static double[] exp(double[] doubles) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = Math.exp(doubles[i]);
        }

        return result;
    }

    /**
     * Get the square roots of values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the square roots of the inputs
     */
    public static double[] sqrt(double[] doubles) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = Math.sqrt(doubles[i]);
        }

        return result;
    }

    /**
     * Get the signs of values.
     *
     * @param doubles an array of <tt>double</tt>s
     * @return the signs of the inputs
     */
    public static double[] signum(double[] doubles) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = Math.signum(doubles[i]);
        }

        return result;
    }

    /**
     * Adds a double value to each element in an array.
     * 
     * @param doubles the double array
     * @param value the value to be added
     * @return the double array with the value added
     */
    public static double[] add(double[] doubles, double value) {
        double[] result = new double[doubles.length];

        for (int i = 0; i < doubles.length; ++i) {
            result[i] = doubles[i] + value;
        }

        return result;
    }
}
