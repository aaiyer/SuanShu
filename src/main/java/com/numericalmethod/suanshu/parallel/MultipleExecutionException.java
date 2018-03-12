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
package com.numericalmethod.suanshu.parallel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This exception is thrown when any of the parallel tasks throws an exception
 * during execution. This exception contains all the exceptions caught during
 * execution.
 *
 * @author Ken Yiu
 */
public class MultipleExecutionException extends Exception {

    private static final long serialVersionUID = 1L;
    private final List<?> results;
    private final List<ExecutionException> exceptions;

    /**
     * Construct an exception with the (partial) results and all exceptions encountered during execution.
     *
     * @param results    the results obtained so far
     * @param exceptions all exceptions encountered during execution
     */
    public MultipleExecutionException(List<?> results, List<ExecutionException> exceptions) {
        this.results = results;
        this.exceptions = exceptions;
    }

    /**
     * Get the results obtained so far.
     *
     * @return the results
     */
    public List<?> getResults() {
        return Collections.unmodifiableList(results);
    }

    /**
     * Get all exceptions encountered during execution.
     *
     * @return all exceptions encountered during execution
     */
    public List<ExecutionException> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }
}
