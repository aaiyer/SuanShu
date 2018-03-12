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
import com.numericalmethod.suanshu.matrix.doubles.MatrixTable;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * These are some miscellaneous utility functions that are commonly used throughout the SuanShu library.
 *
 * @author Haksun Li
 */
public class SuanShuUtils {

    private SuanShuUtils() {
        // utility class has no constructor
    }

    /**
     * Check if an argument condition is satisfied.
     * Throw {@code IllegalArgumentException} if not.
     *
     * @param condition    the argument condition to be checked
     * @param errorMessage the error message if the condition is not satisfied (format string can be used with {@code args}, see {@link String#format(java.lang.String, java.lang.Object[]) format})
     * @param args         the arguments for the error message
     */
    public static void assertArgument(boolean condition, String errorMessage, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(
                    String.format(errorMessage, args));
        }
    }

    /**
     * This is a wrapper method that throws a {@code RuntimeException} if it is not null.
     * To use this method efficiently, we need to avoid the slow and expensive creation of an exception object on each checking.
     * For a sample usage,
     * <blockquote><code>
     * assertOrThrow(cond ? null : new RuntimeException("msg"));
     * </code></blockquote>
     * It is important that we use lazy evaluation of the {@code if} statement.
     *
     * @param error the error thrown if the condition is not satisfied
     */
    public static void assertOrThrow(RuntimeException error) {
        if (error != null) {
            throw error;
        }
    }

    /**
     * <em>Guess</em> a reasonable precision parameter.
     * In numerical computing, we often need to <em>guess</em> how small a number needs to be for it to be considered 0.
     * Changing the threshold often changes the results, e.g., the numerical rank of a matrix.
     * <p/>
     * This method suggests a more 'objective' way to determine the 'correct' epsilon from the inputs. Roughly,
     * <blockquote><i>
     * auto ε = |max(inputs)| * sqrt(number of inputs) * machine ε * 10
     * </i></blockquote>
     *
     * @param inputs {@code double}s
     * @return a precision parameter
     */
    public static double autoEpsilon(double... inputs) {
        double auto = abs(DoubleArrayMath.max(DoubleArrayMath.abs(inputs))) * sqrt(inputs.length) * Constant.EPSILON * 10;

        if (auto == 0) {
            auto = Double.MIN_NORMAL;
        }

        return auto;
    }

    /**
     * <em>Guess</em> a reasonable precision parameter.
     * In numerical computing, we often need to <em>guess</em> how small a number needs to be for it to be considered 0.
     * Changing the threshold often changes the results, e.g., the numerical rank of a matrix.
     * <p/>
     * This method suggests a more 'objective' way to determine the 'correct' epsilon from the inputs. Roughly,
     * <blockquote><i>
     * auto ε = |max(inputs)| * sqrt(number of inputs) * machine ε * 10
     * </i></blockquote>
     *
     * @param inputs arrays of {@code double[]}s
     * @return a precision parameter
     */
    public static double autoEpsilon(double[]... inputs) {
        double max = 0;

        for (double[] d : inputs) {
            double auto = autoEpsilon(d);
            if (auto > max) {
                max = auto;
            }
        }

        return max;
    }

    /**
     * <em>Guess</em> a reasonable precision parameter.
     * In numerical computing, we often need to <em>guess</em> how small a number needs to be for it to be considered 0.
     * Changing the threshold often changes the results, e.g., the numerical rank of a matrix.
     * <p/>
     * This method suggests a more 'objective' way to determine the 'correct' epsilon from the inputs. Roughly,
     * <blockquote><i>
     * auto ε = |max(inputs)| * sqrt(number of inputs) * machine ε * 10
     * </i></blockquote>
     *
     * @param A a matrix
     * @return a precision parameter
     */
    public static double autoEpsilon(MatrixTable A) {
        return autoEpsilon(MatrixUtils.to1DArray(A));
    }
}
