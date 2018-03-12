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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This {@link IterationMonitor} stores all states generated during iterations.
 *
 * @author Ken Yiu
 */
public class IteratesMonitor<S> implements IterationMonitor<S> {

    private final List<S> iterates;

    /**
     * Construct a monitor to keep track of the states in all iterations.
     */
    public IteratesMonitor() {
        iterates = new ArrayList<S>();
    }

    @Override
    public void addIterate(S s) {
        iterates.add(s);
    }

    /**
     * Get a list of all iterative states.
     *
     * @return the list of iterates
     */
    public List<S> getIterates() {
        return Collections.unmodifiableList(iterates);
    }
}
