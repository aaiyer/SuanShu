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
package com.numericalmethod.suanshu.interval;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a disjoint set of intervals. When adding a new interval, all overlapping intervals are merged into one interval.
 * This implementation keeps the disjoint intervals sorted in ascending order.
 * Suppose <i>I_1</i> is indexed before <i>I_2</i>.
 * The end of <i>I_1</i> is strictly smaller than the begin of <i>I_2</i>, as defined by the partial ordering relation.
 *
 * @param <T> a class that implements {@link Comparable}
 * @author Haksun Li
 */
public class Intervals<T extends Comparable<? super T>> {

    private List<Interval<T>> intervals = new ArrayList<Interval<T>>();//disjoint intervals

    /**
     * Construct an empty set of intervals.
     */
    public Intervals() {
    }

    /**
     * Construct a set that contains only one interval.
     *
     * @param interval an interval
     */
    public Intervals(Interval<T> interval) {
        add(interval);
    }

    /**
     * Construct a set that contains only one interval <i>[begin, end]</i>.
     *
     * @param begin the begin of an interval
     * @param end   the end of an interval
     */
    public Intervals(T begin, T end) {
        this(new Interval<T>(begin, end));
    }

    /**
     * Construct a set of intervals.
     *
     * @param intervals intervals
     */
    public Intervals(Interval<T>... intervals) {
        add(intervals);
    }

    /**
     * Copy constructor.
     *
     * @param that a set of intervals
     */
    public Intervals(Intervals<T> that) {
        for (int i = 0; i < that.intervals.size(); ++i) {
            this.intervals.add(that.intervals.get(i));
        }
    }

    /**
     * Get the number of disjoint intervals.
     *
     * @return the number of disjoint intervals
     */
    public int size() {
        return intervals.size();
    }

    /**
     * Get the <i>i</i>-th interval.
     * The intervals are sorted such that
     * if <i>I_1</i> is indexed before <i>I_2</i>,
     * then the end of <i>I_1</i> is strictly smaller than the begin of <i>I_2</i>.
     *
     * @param i the index, counting from 1
     * @return the <i>i</i>-th interval
     */
    public Interval<T> get(int i) {
        return intervals.get(i - 1);
    }

    /**
     * Add an interval to the set.
     * The union remains disjoint and sorted.
     *
     * @param interval an interval
     */
    public void add(Interval<T> interval) {
        ArrayList<Interval<T>> that = new ArrayList<Interval<T>>();
        that.add(interval);

        /*
         * The following algorithm works with any size ArrayList<Interval<T>>.
         * We do only one interval at a time to do an insertion sort like adding of intervals, so that they don't need to be sorted.
         */
        int pos1 = 0, pos2 = 0;
        Interval<T> i1, i2, i1_2;

        for (; pos1 < this.size() && pos2 < that.size(); ++pos2) {
            i1 = this.intervals.get(pos1);
            i2 = that.get(pos2);

            if (i1.is(IntervalRelation.DURING_INVERSE, i2) || i1.is(IntervalRelation.START_INVERSE, i2) || i1.is(IntervalRelation.FINISH_INVERSE, i2) || i1.is(IntervalRelation.EQUAL, i2)) {//i1 includes i2
                //ignore i2; do nothing
            } else if (i1.is(IntervalRelation.DURING, i2) || i1.is(IntervalRelation.START, i2) || i1.is(IntervalRelation.FINISH, i2)) {//i2 includes i1
                this.intervals.remove(pos1);//replace i1 with i2
                --pos2;
            } else if (i1.is(IntervalRelation.OVERLAP, i2) || i1.is(IntervalRelation.MEET, i2)) {//i1 begin < i2 begin < i1 end < i2 end; i1 end = i2 begin
                i1_2 = new Interval<T>(i1.begin(), i2.end());//merge i1 and i2
                this.intervals.remove(pos1);
                that.add(pos2 + 1, i1_2);
            } else if (i1.is(IntervalRelation.OVERLAP_INVERSE, i2) || i1.is(IntervalRelation.MEET_INVERSE, i2)) {//i2 begin < i1 begin < i2 end < i1 end; i2 end = i1 begin
                i1_2 = new Interval<T>(i2.begin(), i1.end());//merge i2 and i1
                this.intervals.remove(pos1);
                this.intervals.add(pos1, i1_2);
            } else if (i1.is(IntervalRelation.BEFORE, i2)) {//i1 < i2 < (next i1 or next i2)
                ++pos1;
                --pos2;
            } else if (i1.is(IntervalRelation.AFTER, i2)) {//i2 < i1 < (i1 or next i2)
                this.intervals.add(pos1, i2);
                ++pos1;
            }
        }

        //this finishes; just append the rest of that
        if (pos2 < that.size()) {
            for (int i = pos2; i < that.size(); ++i) {
                this.intervals.add(that.get(i));
            }
        }
    }

    /**
     * Add intervals to the set.
     * The union remains disjoint and sorted.
     *
     * @param intervals intervals
     */
    public void add(Interval<T>... intervals) {
        for (Interval<T> interval : intervals) {
            add(interval);
        }
    }

    @Override
    public String toString() {
        final int size = intervals.size();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; ++i) {
            sb.append(intervals.get(i).toString());
            sb.append(", ");
        }

        return sb.substring(0, sb.length() - 2).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final Intervals<T> other = (Intervals<T>) obj;
        if (this.intervals != other.intervals && (this.intervals == null || !this.intervals.equals(other.intervals))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.intervals != null ? this.intervals.hashCode() : 0);
        return hash;
    }
}
