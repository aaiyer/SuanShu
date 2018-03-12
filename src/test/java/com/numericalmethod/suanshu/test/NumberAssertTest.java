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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class NumberAssertTest {

    @Test
    public void test_AssertEquals_0010() {
        Number expected = new Double(1);
        Number actual = new Double(1);
        double epsilon = 0.1;
        AssertionError err = null;
        try {
            NumberAssert.assertEquals("", expected, actual, epsilon);
        } catch (AssertionError ae) {
            err = ae;
        }
        assertNull(err);
    }

    @Test
    public void test_AssertEquals_0020() {
        String message = "Error Message";
        Number expected = new Double(1);
        Number actual = new Double(1.1);
        double epsilon = 0.1;
        AssertionError err = null;
        try {
            NumberAssert.assertEquals(message, expected, actual, epsilon);
        } catch (AssertionError ae) {
            err = ae;
        }
        assertNotNull(err);
        assertThat(err.getMessage(), containsString(message));
        assertThat(err.getMessage(), containsString(expected.toString()));
        assertThat(err.getMessage(), containsString(actual.toString()));
    }

    @Test
    public void test_AssertEquals_0030() {
        Number expected = new Double(1);
        Number actual = new Double(1.01);
        double epsilon = 0.1;
        AssertionError err = null;
        try {
            NumberAssert.assertEquals("", expected, actual, epsilon);
        } catch (AssertionError ae) {
            err = ae;
        }
        assertNull(err);
    }

    @Test
    public void test_AssertSameSet_0040() {
        List<Number> expected = new ArrayList<Number>();
        expected.add(1.);
        expected.add(2.);

        List<Number> actual = new ArrayList<Number>();
        actual.add(2.02);
        actual.add(1.01);

        double epsilon = 0.1;
        AssertionError err = null;
        try {
            NumberAssert.assertSameList(expected, actual, epsilon);
        } catch (AssertionError ae) {
            err = ae;
        }
        assertNull(err);
    }
}
