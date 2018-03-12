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
package com.numericalmethod.suanshu.matrix;

/**
 * This is the runtime exception thrown when trying to access an invalid entry in a matrix, e.g., <i>A[0, 0]</i>.
 * Matrix indices count from 1. The first entry (upper left hand corner) is <i>A[1, 1]</i>.
 *
 * @author Ken Yiu
 */
public class MatrixAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of {@code MatrixAccessException}.
     */
    public MatrixAccessException() {
        super("the matrix index is out of range");
    }

    /**
     * Construct an instance of {@code MatrixAccessException} with a message.
     *
     * @param msg an error message
     */
    public MatrixAccessException(String msg) {
        super(msg);
    }
}
