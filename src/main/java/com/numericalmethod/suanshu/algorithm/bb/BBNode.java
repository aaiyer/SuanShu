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
package com.numericalmethod.suanshu.algorithm.bb;

import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import java.util.List;

/**
 * A branch-and-bound algorithm maintains a tree of nodes to keep track of the search paths and the pruned paths.
 * The {@link BranchAndBound} code works in conjunction with a node class that implements this interface.
 *
 * @author Haksun Li
 */
public interface BBNode extends ConstrainedOptimProblem {

    /**
     * the solution to the sub-problem associated with this node
     *
     * @return the solution to the sub-problem associated with this node
     */
    public ImmutableVector solution();

    /**
     * the value of this node
     *
     * @return the value of this node
     */
    public double value();

    /**
     * Check if this node is a possible solution to the original problem, e.g., not pruned.
     *
     * @return {@code true} if this node is a possible solution to the original problem
     */
    public boolean isCandidate();

    /**
     * Get the children of this node by using the branching operation.
     *
     * @return the children of this node
     */
    public List<? extends BBNode> branching();
}
