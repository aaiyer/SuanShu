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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.interiorpoint;

import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The vector set <i>{x, s, y}</i> is a solution to both the primal and dual SOCP problems.
 *
 * @author Haksun Li
 */
public class PrimalDualSolution {

    /**
     * This is the minimizer for the primal problem.
     */
    public final ImmutableVector x;
    /**
     * This is the maximizer for the dual problem.
     */
    public final ImmutableVector y;
    /**
     * This is the auxiliary helper to solve the dual problem.
     */
    public final ImmutableVector s;

    /**
     * Construct a solution to a primal and a dual SOCP problems.
     *
     * @param x the minimizer for the primal problem
     * @param s the auxiliary helper to solve the dual problem
     * @param y the maximizer for the dual problem
     */
    public PrimalDualSolution(Vector x, Vector s, Vector y) {
        this.x = new ImmutableVector(x);
        this.s = new ImmutableVector(s);
        this.y = new ImmutableVector(y);
    }
}
