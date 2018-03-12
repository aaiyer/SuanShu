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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative;

/**
 * This exception is thrown by
 * {@link IterativeLinearSystemSolver#solve(com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem)}
 * when the iterative algorithm detects a breakdown or fails to converge.
 *
 * @author Haksun Li
 */
public class ConvergenceFailure extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * the reasons for the convergence failure
     */
    public static enum Reason {

        /**
         * Thrown when the iterative algorithm fails to proceed during its
         * iterations, due to, for example, division-by-zero.
         */
        BREAKDOWN,
        /**
         * Thrown if the iterative algorithm fails to converge to a solution
         * within a specified tolerance after the specified maximum number
         * of iterations.
         */
        MAX_ITERATIONS_EXCEEDED
    }

    private final Reason reason;

    /**
     * Construct an exception with reason.
     *
     * @param reason the reason for the failure
     */
    public ConvergenceFailure(Reason reason) {
        this.reason = reason;
    }

    /**
     * Construct an exception with reason and error message.
     *
     * @param reason  the reason for the failure
     * @param message the error message
     */
    public ConvergenceFailure(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    /**
     * Get the reason for the convergence failure.
     *
     * @return the failure reason
     */
    public Reason getReason() {
        return reason;
    }
}
