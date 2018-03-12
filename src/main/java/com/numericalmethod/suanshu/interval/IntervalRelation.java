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

/**
 * Allen's Interval Algebra is a calculus for temporal reasoning that was introduced by James F. Allen in 1983.
 * The calculus defines possible relations between time intervals and
 * provides a composition table that can be used as a basis for reasoning about temporal descriptions of events.
 * This implementation has the 13 base relations that capture the possible relations between two intervals.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Allen's_Interval_Algebra">Wikipedia: Allen's Interval Algebra</a>
 */
public enum IntervalRelation {

    /**
     * X takes place before Y.
     */
    BEFORE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.end().compareTo(Y.begin()) < 0;
        }
    },
    /**
     * X takes place after Y.
     */
    AFTER {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.end()) > 0;
        }
    },
    /**
     * X meets Y.
     */
    MEET {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.end().compareTo(Y.begin()) == 0;
        }
    },
    /**
     * Y meets X.
     */
    MEET_INVERSE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return Y.end().compareTo(X.begin()) == 0;
        }
    },
    /**
     * X overlaps with Y.
     */
    OVERLAP {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return Y.begin().compareTo(X.begin()) > 0 && Y.begin().compareTo(X.end()) < 0 && Y.end().compareTo(X.end()) > 0;
        }
    },
    /**
     * Y overlaps with X.
     */
    OVERLAP_INVERSE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.end().compareTo(Y.end()) > 0 && X.begin().compareTo(Y.begin()) > 0 && X.begin().compareTo(Y.end()) < 0;
        }
    },
    /**
     * X starts Y.
     */
    START {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) == 0 && X.end().compareTo(Y.end()) < 0;
        }
    },
    /**
     * Y starts X.
     */
    START_INVERSE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) == 0 && X.end().compareTo(Y.end()) > 0;
        }
    },
    /**
     * X during Y.
     */
    DURING {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) > 0 && X.end().compareTo(Y.end()) < 0;
        }
    },
    /**
     * Y during X.
     */
    DURING_INVERSE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) < 0 && X.end().compareTo(Y.end()) > 0;
        }
    },
    /**
     * X finishes Y
     */
    FINISH {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) > 0 && X.end().compareTo(Y.end()) == 0;
        }
    },
    /**
     * Y finishes X.
     */
    FINISH_INVERSE {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) < 0 && X.end().compareTo(Y.end()) == 0;
        }
    },
    /**
     * X is equal to Y.
     */
    EQUAL {

        @Override
        public <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y) {
            return X.begin().compareTo(Y.begin()) == 0 && X.end().compareTo(Y.end()) == 0;
        }
    };

    /**
     * Check if <i>X</i> and <i>Y</i> satisfy a certain relation.
     *
     * @param <T> a {@link Comparable} type
     * @param X   an interval
     * @param Y   an interval
     * @return {@code true} is if <i>X</i> and <i>Y</i> satisfy the relation
     */
    public abstract <T extends Comparable<? super T>> boolean isBetween(Interval<T> X, Interval<T> Y);
}
