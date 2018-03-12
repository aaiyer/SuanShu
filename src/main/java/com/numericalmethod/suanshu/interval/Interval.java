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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.util.EnumSet;
import java.util.Set;

/**
 * For a partially ordered set, there is a binary relation, denoted as &le;, that indicates that,
 * for certain pairs of elements in the set, one of the elements precedes the other.
 * An interval is defined as follows.
 * For <i>a &le; b</i>, an interval <i>[a,b]</i> is the set of elements <i>x</i> satisfying <i>a &le; x &le; b</i> (i.e. <i>a &le; x</i> and <i>x &le; b</i>).
 * It contains at least the elements <i>a</i> and <i>b</i>.
 * <p/>
 * An {@code Interval} is immutable.
 *
 * @param <T> a class that implements {@link Comparable}; hence a partially ordered set
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Partially_ordered_set#Interval">Wikipedia: Interval</a>
 */
public class Interval<T extends Comparable<? super T>> {

    private final T begin;//the beginning of this interval
    private final T end;//the end of this interval

    /**
     * Construct an interval.
     *
     * @param begin the beginning of this interval
     * @param end   the end of this interval
     */
    public Interval(T begin, T end) {
        SuanShuUtils.assertArgument(begin.compareTo(end) < 0, "begin must start before end");

        this.begin = begin;
        this.end = end;
    }

    /**
     * Get the beginning of this interval.
     *
     * @return the beginning of this interval
     */
    public T begin() {
        return begin;
    }

    /**
     * Get the end of this interval.
     *
     * @return the end of this interval
     */
    public T end() {
        return end;
    }

    /**
     * Determine the interval relations between {@code this} and {@code Y}.
     *
     * @param Y an interval
     * @return the set of satisfied relations
     */
    public Set<IntervalRelation> relations(Interval<T> Y) {
        Set<IntervalRelation> result = EnumSet.noneOf(IntervalRelation.class);

        for (IntervalRelation relation : EnumSet.allOf(IntervalRelation.class)) {
            if (is(relation, Y)) {
                result.add(relation);
            }
        }

        return result;
    }

    /**
     * Check whether {@code this} and {@code Y} satisfies a certain Allen's interval relation.
     *
     * @param relation an {@link IntervalRelation}
     * @param Y        an interval
     * @return {@code true} if {@code this} and {@code Y} satisfies {@code relation}
     */
    public boolean is(IntervalRelation relation, Interval<T> Y) {
        return relation.isBetween(this, Y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        /*
         * TODO: not appropriate for generics, e.g.,
         * TimeInterval != Interval<Time>
         * because TimeInterval is a subclass of Interval.
         */
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
        if (!(obj instanceof Interval<?>)) { // check if it is a subclass, otherwise the explicit cast will throw exception
            return false;
        }
        @SuppressWarnings("unchecked")
        final Interval<T> other = (Interval<T>) obj;

        if (!this.begin.getClass().isInstance(other.begin)) { // check if the type of T are the same
            return false;
        }

        if (this.begin != other.begin && (this.begin == null || this.begin.compareTo(other.begin) != 0)) {
            return false;
        }

        if (this.end != other.end && (this.end == null || this.end.compareTo(other.end) != 0)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.begin != null ? this.begin.hashCode() : 0);
        hash = 19 * hash + (this.end != null ? this.end.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(begin.toString());
        sb.append(", ");
        sb.append(end.toString());
        sb.append("]");
        return sb.toString();
    }
}
