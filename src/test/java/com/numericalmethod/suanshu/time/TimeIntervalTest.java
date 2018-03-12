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
package com.numericalmethod.suanshu.time;

import com.numericalmethod.suanshu.interval.IntervalRelation;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class TimeIntervalTest {

    @Test
    public void test_0010() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.EST));
        assertEquals("[2010-06-04T00:00:00.000-05:00, 2010-06-05T00:00:00.000-05:00]", interval1.toString());
    }

    @Test
    public void test_0020() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        assertEquals(interval1, interval2);
    }

    @Test
    public void test_0030() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 7, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.BEFORE, interval2));
    }

    @Test
    public void test_0040() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 7, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.AFTER, interval1));
    }

    @Test
    public void test_0050() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 7, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.MEET, interval2));
    }

    @Test
    public void test_0060() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 5, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 7, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.MEET_INVERSE, interval1));
    }

    @Test
    public void test_0070() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.OVERLAP, interval2));
    }

    @Test
    public void test_0080() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.OVERLAP_INVERSE, interval1));
    }

    @Test
    public void test_0090() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.START, interval2));
    }

    @Test
    public void test_0100() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.START_INVERSE, interval1));
    }

    @Test
    public void test_0110() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.DURING, interval2));
    }

    @Test
    public void test_0120() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.DURING_INVERSE, interval1));
    }

    @Test
    public void test_0130() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.FINISH, interval2));
    }

    @Test
    public void test_0140() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.FINISH_INVERSE, interval1));
    }

    @Test
    public void test_0150() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        assertTrue(interval1.is(IntervalRelation.EQUAL, interval2));
    }

    @Test
    public void test_0160() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        assertTrue(interval2.is(IntervalRelation.EQUAL, interval1));
    }

    @Test
    public void test_0170() {
        TimeInterval interval1 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        TimeInterval interval2 = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 4, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 20, JodaTimeUtils.GMT));
        Set<IntervalRelation> set = interval1.relations(interval2);
        assertTrue(set.contains(IntervalRelation.START));
    }
}
