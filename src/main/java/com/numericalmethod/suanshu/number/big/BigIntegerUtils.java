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
package com.numericalmethod.suanshu.number.big;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.math.BigInteger;

/**
 * These are the utility functions to manipulate {@link BigInteger}.
 *
 * @author Haksun Li
 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/math/BigInteger.html">Class BigInteger</a>
 */
public class BigIntegerUtils {

    private BigIntegerUtils() {
        // private constructor for utility class
    }

    /**
     * Compute the <i>n</i> factorial.
     *
     * @param n an integer
     * @return {@code n!}
     * @see <a href="http://en.wikipedia.org/wiki/Factorial">Wikipedia: Factorial</a>
     */
    public static BigInteger factorial(int n) {
        SuanShuUtils.assertArgument(n >= 0, "n must be 0 or a natural number");

        BigInteger result = BigInteger.valueOf(1);
        for (int i = 2; i <= n; ++i) {
            result = result.multiply(BigInteger.valueOf(i));//n! = n * (n-1) * (n-2) ... 2 * 1
        }

        return result;
    }

    /**
     * Compute the combination function or the binomial coefficient.
     * It is the number of <i>k</i>-combinations (each of size <i>k</i>) from a set <i>k</i> with <i>n</i> elements (size <i>n</i>).
     *
     * @param n the size of the full set
     * @param k the size of a combination
     * @return {@code n! / (n-k)! / k!}
     * @see <a href="http://en.wikipedia.org/wiki/Combination">Wikipedia: Combination</a>
     */
    public static BigInteger combination(int n, int k) {
        SuanShuUtils.assertArgument(n >= k, "n >= k");
        SuanShuUtils.assertArgument(k >= 0, "n, k must be 0s or natural numbers");

        if (n == 0 || k == 0 || k == n) {
            return BigInteger.valueOf(1);
        }

        if (k > n / 2d) {
            k = n - k;
        }

        BigInteger result = permutation(n, k);//n! / (n-k)!
        BigInteger k1 = factorial(k);//k!
        result = result.divide(k1);

        return result;
    }

    /**
     * Compute the permutation function.
     * It is the number of <i>k</i>-permutations (each of size <i>k</i>) from a set <i>k</i> with <i>n</i> elements (size <i>n</i>).
     *
     * @param n the size of the full set
     * @param k the size of a permutation
     * @return {@code n! / (n-k)!}
     * @see <a href="http://en.wikipedia.org/wiki/Permutations">Wikipedia: Permutation</a>
     */
    public static BigInteger permutation(int n, int k) {
        SuanShuUtils.assertArgument(n >= k, "n >= k");
        SuanShuUtils.assertArgument(k >= 0, "n, k must be 0s or natural numbers");

        if (n == 0 || k == 0 || k == n) {
            return BigInteger.valueOf(1);
        }

        BigInteger result = BigInteger.valueOf(1);
        for (int i = n; i > n - k; --i) {
            result = result.multiply(BigInteger.valueOf(i));//n * (n-1) * (n-2) ... (n-k+1)
        }

        return result;
    }
}
