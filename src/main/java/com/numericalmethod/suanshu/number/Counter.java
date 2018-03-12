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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A counter keeps track of the number of occurrences of numbers.
 * Two numbers are considered the same if they are close enough by a user specified number of decimal points.
 *
 * @author Haksun Li
 */
public class Counter {

    /** a precision parameter for rounding a double: number of decimal points to keep */
    private final int scale;
    private final Map<Double, Integer> counts = new HashMap<Double, Integer>();

    /**
     * Construct a counter with no rounding. Two numbers are considered the same if they have the same binary representations.
     */
    public Counter() {
        this.scale = Integer.MAX_VALUE;
    }

    /**
     * Construct a counter. Two numbers are considered the same if they are close enough by a number of decimal points.
     *
     * @param scale a precision in terms of the number of decimal points
     */
    public Counter(int scale) {
        this.scale = scale;
    }

    /**
     * Add a number to the counter.
     *
     * @param number a {@code double}
     */
    public synchronized void add(double number) {
        Double n = new Double(round(number));

        int currentCount = 0;
        if (counts.containsKey(n)) {
            currentCount = counts.get(n).intValue();
        }

        counts.put(n, currentCount + 1);//add 1
    }

    /**
     * Add numbers to the counter.
     *
     * @param numbers {@code double}s
     */
    public synchronized void add(double... numbers) {
        for (int i = 0; i < numbers.length; ++i) {
            add(numbers[i]);
        }
    }

    /**
     * Get the count, i.e., the number of occurrences, of a particular number.
     * If the counter has not seen the number before, it returns 0.
     *
     * @param number a number
     * @return the count
     */
    public synchronized int count(double number) {
        Double n = new Double(round(number));

        if (counts.containsKey(n)) {
            return counts.get(n).intValue();
        }

        return 0;
    }

    /**
     * Get the set of numbers the counter has seen.
     *
     * @return a set of seen numbers
     */
    public Set<Double> keySet() {
        return counts.keySet();
    }

    /**
     * We round the number to a certain number of digits for comparison purpose.
     * Two numbers are considered the same if they are close enough by a user specified number of decimal points.
     *
     * @param number a number
     * @return a rounded number precise up to a specified number of decimal points
     */
    private double round(double number) {
        double rounded = number;
        if (scale != Integer.MAX_VALUE) {
            rounded = DoubleUtils.round(number, scale);
        }

        return rounded;
    }
}
