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

import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BigIntegerUtilsTest {

    /**
     * Test of factorial method, of class BigIntegerUtils.
     */
    @Test
    public void test_factorial_0010() {
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerUtils.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerUtils.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerUtils.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerUtils.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerUtils.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerUtils.factorial(7));
        assertEquals(BigInteger.valueOf(40320), BigIntegerUtils.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerUtils.factorial(9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerUtils.factorial(10));
    }

    /**
     * Test of combination method, of class BigIntegerUtils.
     */
    @Test
    public void test_combination_0010() {
        assertEquals(BigInteger.valueOf(10), BigIntegerUtils.combination(5, 3));
        assertEquals(BigInteger.valueOf(2598960), BigIntegerUtils.combination(52, 5));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.combination(79, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.combination(79, 79));
        assertEquals(BigInteger.valueOf(23), BigIntegerUtils.combination(23, 1));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.combination(23, 23));
    }

    /**
     * Test of permutation method, of class BigIntegerUtils.
     */
    @Test
    public void permutation() {
        assertEquals(BigInteger.valueOf(60), BigIntegerUtils.permutation(5, 3));
        assertEquals(BigInteger.valueOf(20), BigIntegerUtils.permutation(5, 2));
        assertEquals(BigInteger.valueOf(720), BigIntegerUtils.permutation(10, 3));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.permutation(79, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.permutation(79, 79));
        assertEquals(BigInteger.valueOf(23), BigIntegerUtils.permutation(23, 1));
        assertEquals(BigInteger.valueOf(1), BigIntegerUtils.permutation(23, 23));
        assertEquals(BigInteger.valueOf(1560), BigIntegerUtils.permutation(40, 2));
    }
}
