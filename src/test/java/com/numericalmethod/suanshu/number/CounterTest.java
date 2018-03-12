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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CounterTest {

    @Test
    public void test_0010() {
        Counter instance = new Counter(0);
        instance.add(new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 1.12, 1.123, 2.23});

        assertEquals(3, instance.count(1.1));
        assertEquals(2, instance.count(2.2));
        assertEquals(1, instance.count(5.5));
    }

    @Test
    public void test_0020() {
        Counter instance = new Counter(1);
        instance.add(new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 1.12, 1.123, 2.23});

        assertEquals(3, instance.count(1.1));
        assertEquals(2, instance.count(2.2));
        assertEquals(1, instance.count(5.5));
    }

    @Test
    public void test_0030() {
        Counter instance = new Counter(2);
        instance.add(new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 1.12, 1.123, 2.23});

        assertEquals(1, instance.count(1.1));
        assertEquals(2, instance.count(1.12));
        assertEquals(1, instance.count(2.2));
        assertEquals(1, instance.count(2.23));
        assertEquals(1, instance.count(5.5));
    }

    @Test
    public void test_0040() {
        Counter instance = new Counter(3);
        instance.add(new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 1.12, 1.123, 2.23});

        assertEquals(1, instance.count(1.1));
        assertEquals(1, instance.count(1.12));
        assertEquals(1, instance.count(1.123));
        assertEquals(1, instance.count(2.2));
        assertEquals(1, instance.count(2.23));
        assertEquals(1, instance.count(5.5));
    }

    @Test
    public void test_0050() {
        Counter instance = new Counter();
        instance.add(new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 1.12, 1.123, 2.23});

        assertEquals(1, instance.count(1.1));
        assertEquals(1, instance.count(1.12));
        assertEquals(1, instance.count(1.123));
        assertEquals(1, instance.count(2.2));
        assertEquals(1, instance.count(2.23));
        assertEquals(1, instance.count(5.5));
    }

    @Test
    public void test_0060() {
        Counter instance = new Counter(0);
        instance.add(new double[]{-1.1, -2.2, -3.3, -4.4, -5.5, -1.12, -1.123, -2.23});

        assertEquals(3, instance.count(-1.1));
        assertEquals(2, instance.count(-2.2));
        assertEquals(1, instance.count(-5.5));
    }

    @Test
    public void test_0070() {
        Counter instance = new Counter(1);
        instance.add(new double[]{-1.1, -2.2, -3.3, -4.4, -5.5, -1.12, -1.123, -2.23});

        assertEquals(3, instance.count(-1.1));
        assertEquals(2, instance.count(-2.2));
        assertEquals(1, instance.count(-5.5));
    }

    @Test
    public void test_0080() {
        Counter instance = new Counter(2);
        instance.add(new double[]{-1.1, -2.2, -3.3, -4.4, -5.5, -1.12, -1.123, -2.23});

        assertEquals(1, instance.count(-1.1));
        assertEquals(2, instance.count(-1.12));
        assertEquals(1, instance.count(-2.2));
        assertEquals(1, instance.count(-2.23));
        assertEquals(1, instance.count(-5.5));
    }

    @Test
    public void test_0090() {
        Counter instance = new Counter(3);
        instance.add(new double[]{-1.1, -2.2, -3.3, -4.4, -5.5, -1.12, -1.123, -2.23});

        assertEquals(1, instance.count(-1.1));
        assertEquals(1, instance.count(-1.12));
        assertEquals(1, instance.count(-1.123));
        assertEquals(1, instance.count(-2.2));
        assertEquals(1, instance.count(-2.23));
        assertEquals(1, instance.count(-5.5));
    }

    @Test
    public void test_0100() {
        Counter instance = new Counter();
        instance.add(new double[]{-1.1, -2.2, -3.3, -4.4, -5.5, -1.12, -1.123, -2.23});

        assertEquals(1, instance.count(-1.1));
        assertEquals(1, instance.count(-1.12));
        assertEquals(1, instance.count(-1.123));
        assertEquals(1, instance.count(-2.2));
        assertEquals(1, instance.count(-2.23));
        assertEquals(1, instance.count(-5.5));
    }

    @Test
    public void test_0110() {
        Counter instance = new Counter(0);
        instance.add(new double[]{Double.NaN, Double.POSITIVE_INFINITY, 1.1});

        assertEquals(1, instance.count(Double.NaN));
        assertEquals(1, instance.count(Double.POSITIVE_INFINITY));
        assertEquals(1, instance.count(1.1));
    }
}
