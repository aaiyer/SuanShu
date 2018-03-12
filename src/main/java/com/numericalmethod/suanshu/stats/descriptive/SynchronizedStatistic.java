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
package com.numericalmethod.suanshu.stats.descriptive;

/**
 * This is a thread-safe wrapper of {@link Statistic} by synchronizing all public methods
 * so that only one thread at a time can access the instance.
 * This is essentially the same principle used by Java's synchronized collection class.
 *
 * @author Haksun Li
 * @see "Brian Goetz, Tim Peierls, Joshua Bloch and Joseph Bowbeer, "Chapter 5," Java Concurrency in Practice."
 */
public class SynchronizedStatistic implements Statistic {

    /** the {@link Statistic} to be made thread-safe */
    private final Statistic stat;

    /**
     * Construct a synchronized version of a statistic.
     *
     * @param stat a statistic
     */
    SynchronizedStatistic(Statistic stat) {
        this.stat = stat;
    }

    @Override
    public synchronized void addData(double... data) {
        stat.addData(data);
    }

    @Override
    public synchronized double value() {
        return stat.value();
    }

    @Override
    public synchronized long N() {
        return stat.N();
    }
}
