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
package com.numericalmethod.suanshu.analysis.function;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;

/**
 * These are some commonly used mathematical functions.
 *
 * @author Haksun Li
 */
public class FunctionOps {

    /**
     * <pre><i>
     * b<sup>e</sup> mod m
     * </i></pre>
     * We use the fact that
     * <pre><i>
     * (a * b) mod m = ((a mod m) * (b mod m)) mod m
     * </i></pre>
     * <em>This code may fail without being noticed for very large numbers because overflow in integer operations does not throw any exception in the JVM.</em>
     *
     * @param base     positive integer <i>b</i>
     * @param exponent positive integer <i>e</i>
     * @param modulus  positive integer <i>m</i>
     * @return <i>b<sup>e</sup> mod m</i>
     * @throws IllegalArgumentException if not all inputs are positive integers
     * @see
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Modular_exponentiation">Wikipedia: Modular exponentiation</a>
     * <li><a href="http://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method">Wikipedia: Right-to-left binary method</a>
     * <li>"Bruce Schneier, "p. 244," Applied Cryptography, 2e, ISBN 0-471-11709-9."
     * </ul>
     */
    public static long modpow(long base, long exponent, long modulus) {
        assertArgument(base > 0 && exponent > 0 && modulus > 0, "all three numbers must be positive integers");

        long fx = 1;
        while (exponent > 0) {
            long bm = base % modulus;

            if ((exponent & 1) == 1) {
                // multiply in this bit's contribution while using modulus to keep result small
                fx = ((fx % modulus) * bm) % modulus;
            }
            // move to the next bit of the exponent, square (and mod) the base accordingly
            exponent >>= 1;
            base = (bm * bm) % modulus;
        }
        return fx;
    }

    /**
     * Compute the positive modulus of a number.
     * If <i>x</i> is positive, we return <i>x mod m</i>; otherwise, we return the smallest positive integer less than <i>m</i>, having the same modulo.
     * For example,
     * <pre><i>
     * -1 mod 5 = 4
     * </i></pre>
     *
     * @param x an integer
     * @param m the modulus
     * @return <i>x mod m</i>
     * @see <a href="http://en.wikipedia.org/wiki/Modulo_operator">Wikipedia: Modulo operation</a>
     */
    public static long mod(long x, long m) {
        assertArgument(m > 0, "modulus must be positive integers");

        long fx = x;
        while (fx < 0) {
            fx += m;
        }
        if (fx > m) {
            fx %= m;
        }
        return fx;
    }

    /**
     * \(x_1 \cdot x_2\)
     * <p/>
     * This operation is called inner product when used in the context of vector space.
     *
     * @param x1 a {@code long} array
     * @param x2 a {@code long} array
     * @return \(x_1 \cdot x_2\)
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia: Dot product</a>
     */
    public static long dotProduct(long[] x1, long[] x2) {
        assertArgument(x1.length == x2.length, "unmatched lengths");

        long fx = 0;
        for (int i = 0; i < x1.length; ++i) {
            fx += x1[i] * x2[i];
        }
        return fx;
    }

    /**
     * \(x_1 \cdot x_2\)
     * <p/>
     * This operation is called inner product when used in the context of vector space.
     *
     * @param x1 a {@code double} array
     * @param x2 a {@code double} array
     * @return \(x_1 \cdot x_2\)
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia: Dot product</a>
     */
    public static double dotProduct(double[] x1, double[] x2) {
        assertArgument(x1.length == x2.length, "unmatched lengths");

        double fx = 0;
        for (int i = 0; i < x1.length; ++i) {
            fx += x1[i] * x2[i];
        }
        return fx;
    }

    /**
     * <i>n!</i>
     *
     * @param n an integer
     * @return <i>n!</i>
     * @see <a href="http://en.wikipedia.org/wiki/Factorial">Wikipedia: Factorial</a>
     */
    public static double factorial(int n) {
        assertArgument(n >= 0, "n must be 0 or a natural number");

        double fx = 1d;
        for (int i = 2; i <= n; ++i) {
            fx *= i;
        }
        return fx;
    }

    /**
     * Compute the combination function or binomial coefficient.
     * It is the number of subsets of size <i>k</i> in a larger set of <i>n</i> elements.
     *
     * @param n the size of the full set
     * @param k the size of a combination
     * @return \(\frac{n!}{(n-k)! k!}\)
     * @see <a href="http://en.wikipedia.org/wiki/Combination">Wikipedia: Combination</a>
     */
    public static double combination(int n, int k) {
        assertArgument(n >= k, "n >= k");
        assertArgument(k >= 0, "n, k must be 0s or natural numbers");

        if (n == 0 || k == 0 || k == n) {
            return 1;
        }

        if (k > n / 2d) {
            k = n - k;
        }

        double fx = 1;

        for (int i = n, j = k; i > n - k || j > 1; --i, --j) {
            if (i > n - k && j > 1) {
                fx *= (double) i / j;//to avoid overflow
            } else if (i > n - k) {
                fx *= i;
            } else if (j > 1) {
                fx /= j;
            } else {
                assert false : "unreachable";
            }
        }

        return fx;
    }

    /**
     * Compute the permutation function.
     * It is the number of size-{@code k}-permutations from a larger set of <i>n</i> elements.
     *
     * @param n the size of the full set
     * @param k the size of a permutation
     * @return \(\frac{n!}{(n-k)!}\)
     * @see <a href="http://en.wikipedia.org/wiki/Permutations">Wikipedia: Permutation</a>
     */
    public static double permutation(int n, int k) {
        assertArgument(n >= k, "n >= k");
        assertArgument(k >= 0, "n, k must be 0s or natural numbers");

        if (n == 0 || k == 0 || k == n) {
            return 1;
        }

        double fx = 1;

        for (int i = n, j = (n - k); i > 1 || j > 1; --i, --j) {
            if (i > 1 && j > 1) {
                fx *= (double) i / j;//to avoid overflow
            } else if (i > 1) {
                fx *= i;
            } else {
                throw new RuntimeException("unreachable");
            }
        }

        return fx;
    }

    /**
     * Linear interpolation between two points.
     *
     * @param x  <i>x</i>
     * @param x0 <i>x0</i>
     * @param y0 <i>y0</i>
     * @param x1 <i>x1</i>
     * @param y1 <i>y1</i>
     * @return the linear interpolation between two points
     * @see <a href="http://en.wikipedia.org/wiki/Linear_interpolation#Linear_interpolation_between_two_known_points">Wikipedia: Linear interpolation between two known points</a>
     */
    public static double linearInterpolate(double x, double x0, double y0, double x1, double y1) {
        double y = y0 + (x - x0) * (y1 - y0) / (x1 - x0);
        return y;
    }
}
