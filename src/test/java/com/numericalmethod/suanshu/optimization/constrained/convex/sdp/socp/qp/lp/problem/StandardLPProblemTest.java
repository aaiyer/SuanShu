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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPTwoPhaseSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class StandardLPProblemTest {

    @Test
    public void test_0010() throws Exception {

        StandardLPProblem problem = new StandardLPProblem(
                new DenseVector(new double[]{-1.0, -1.0, 0, 0}),
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {7, 1, 1, 0},
                    {-1, 1, 0, 1}
                }), new DenseVector(new double[]{15.0, 1.0})));

        LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
        LPSimplexSolution soln = solver.solve(problem);
        LPSimplexMinimizer minimizer = soln.minimizer();
        assertArrayEquals(new double[]{1.75, 2.75, 0., 0.}, minimizer.minimizer().toArray(), 0.);
    }
}
