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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.pathfollowing;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A central path is a solution to both the primal and dual problems of a semi-definite programming problem.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 14.3.3, Central Path," Practical Optimization: Algorithms and Engineering Applications."
 */
public class CentralPath {

    /**
     * This is the minimizer for the primal problem.
     */
    public final ImmutableMatrix X;
    /**
     * This is the maximizer for the dual problem.
     */
    public final ImmutableVector y;
    /**
     * This is the auxiliary helper to solve the dual problem.
     */
    public final ImmutableMatrix S;

    /**
     * Construct a central path.
     *
     * @param X the minimizer for the primal problem
     * @param y the maximizer for the dual problem
     * @param S the auxiliary helper to solve the dual problem
     */
    public CentralPath(Matrix X, Vector y, Matrix S) {
        this.X = new ImmutableMatrix(X);
        this.y = new ImmutableVector(y);
        this.S = new ImmutableMatrix(S);
    }
}
