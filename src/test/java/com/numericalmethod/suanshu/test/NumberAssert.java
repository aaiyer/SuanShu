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
package com.numericalmethod.suanshu.test;

import com.numericalmethod.suanshu.number.NumberUtils;
import java.util.Collection;
import org.junit.Assert;

/**
 * Assertion utility for unit testing {@link Number}.
 *
 * @author Ken Yiu
 */
public class NumberAssert {

    /**
     * Assert that two {@link Number} are equal up to an epsilon.
     *
     * @param message  assertion error identifying message
     * @param expected the expected number
     * @param actual   the actual number
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public static void assertEquals(
            String message,
            Number expected,
            Number actual,
            double epsilon) {
        if (expected.equals(actual)) {
            return;
        }
        if (NumberUtils.equal(expected, actual, epsilon)) {
            return;
        }
        Assert.fail(format(message, expected.toString(), actual.toString()));
    }

    /**
     * Assert that two {@link Number} are equal up to an epsilon.
     *
     * @param expected the expected number
     * @param actual   the actual number
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public static void assertEquals(Number expected, Number actual, double epsilon) {
        assertEquals(null, expected, actual, epsilon); // pass in null if no message prefix is needed
    }

    /**
     * Assert that two collections of {@link Number}s are equal up to an epsilon.
     *
     * @param expected the expected number
     * @param actual   the actual number
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public static void assertSameList(Collection<? extends Number> expected, Collection<? extends Number> actual, double epsilon) {
        if (expected.size() != actual.size()) {
            Assert.fail(String.format("size not matched: %d vs. %d", expected.size(), actual.size()));
        }

        assertSubset(expected, actual, epsilon);
    }

    /**
     * Assert that one collection is a subset of another. Duplicates are ignored.
     *
     * @param superset the expected number
     * @param subset   the actual number
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public static void assertSubset(Collection<? extends Number> superset, Collection<? extends Number> subset, double epsilon) {
        for (Number sub : subset) {
            boolean found = false;
            for (Number sup : superset) {
                if (NumberUtils.equal(sup, sub, epsilon)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Assert.fail("superset collection does not contain " + sub);
            }
        }
    }

    static String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !message.equals("")) {
            formatted = message + " ";
        }
        return formatted
               + "expected:<" + expected.toString()
               + "> but was:<" + actual.toString() + ">";
    }
}
