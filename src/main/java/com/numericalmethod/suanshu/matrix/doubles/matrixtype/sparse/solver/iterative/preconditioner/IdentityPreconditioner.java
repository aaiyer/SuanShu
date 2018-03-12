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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner;

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This identity preconditioner is used when no preconditioning is applied. This
 * is used as the default preconditioner for an unpreconditioned linear system
 * solver.
 *
 * @author Ken Yiu
 */
public class IdentityPreconditioner implements Preconditioner {

    /**
     * Return the input vector <i>x</i>.
     *
     * @param x a vector
     * @return x the input vector
     */
    @Override
    public Vector solve(Vector x) {
        return x;
    }

    /**
     * Return the input vector <i>x</i>.
     *
     * @param x a vector
     * @return x the input vector
     */
    @Override
    public Vector transposeSolve(Vector x) {
        return x;
    }
}
