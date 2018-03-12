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
package com.numericalmethod.suanshu.algorithm.iterative.monitor;

/**
 * This {@link IterationMonitor} counts the number of iterates generated, hence
 * the number of iterations.
 * Note that the initial guess of solution is usually added
 * as the first iterate. Therefore, method {@link #getCount()} normally
 * return an integer greater than or equal to 1 if the iterative method is
 * implemented properly.
 *
 * @author Ken Yiu
 */
public class CountMonitor<S> implements IterationMonitor<S> {

    private int count = 0;

    @Override
    public void addIterate(S s) {
        count++;
    }

    /**
     * Get the number of iterations.
     *
     * @return the number of iterations
     */
    public int getCount() {
        return count;
    }
}
