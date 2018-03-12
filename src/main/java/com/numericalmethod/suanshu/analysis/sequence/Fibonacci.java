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
package com.numericalmethod.suanshu.analysis.sequence;

import java.util.Arrays;

/**
 * A Fibonacci sequence starts with 0 and 1 as the first two numbers.
 * Each subsequent number is the sum of the previous two.
 * For example,
 * <blockquote>
 * 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
 * </blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Fibonacci_number">Wikipedia: Fibonacci number</a>
 */
public class Fibonacci implements Sequence {//TOOD: implement an infinite length version of this?

    private final double[] fibonacci;

    /**
     * Construct a Fibonacci sequence.
     *
     * @param length the number of terms to generate
     */
    public Fibonacci(int length) {
        fibonacci = new double[length];

        fibonacci[0] = 1.;
        fibonacci[1] = 1.;
        for (int i = 2; i < length; ++i) {
            fibonacci[i] = fibonacci[i - 1] + fibonacci[i - 2];
        }
    }

    @Override
    public int length() {
        return fibonacci.length;
    }

    @Override
    public double get(int i) {
        return fibonacci[i-1];
    }

    @Override
    public double[] getAll() {
        return Arrays.copyOf(fibonacci, fibonacci.length);
    }
}
