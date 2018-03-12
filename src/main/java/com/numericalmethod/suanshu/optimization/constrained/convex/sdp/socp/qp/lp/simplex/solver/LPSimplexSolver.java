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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.LPSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblem;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;

/**
 * A simplex solver works toward an LP solution by sequentially applying Jordan exchange to a simplex table.
 *
 * @param <P> a linear programming type
 * @author Haksun Li
 */
public interface LPSimplexSolver<P extends LPProblem> extends LPSolver<P, LPSimplexSolution> {

    /**
     * Solve an LP problem by a simplex algorithm on a simplex table
     *
     * @param table the initial simplex table corresponding to the LP problem
     * @return an LP solution
     * @throws Exception when there is an error
     */
    public LPSimplexSolution solve(SimplexTable table) throws Exception;
}
