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
package com.numericalmethod.suanshu.optimization.constrained.integer.linear.bb;

import com.numericalmethod.suanshu.algorithm.bb.BBNode;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPBoundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPTwoPhaseSolver;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.ILPProblem;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.ILPProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is the branch-and-bound node used in conjunction with {@link ILPBranchAndBound} to solve an Integer Linear Programming problem.
 *
 * @author Haksun Li
 */
public class ILPNode implements BBNode {

    private final int id;//the unique identifier of the node
    private final ILPProblem problem;
    private final LPBoundedMinimizer minimizer;//the minimizer to use to solve the ILP problem in this node
    private static AtomicInteger count = new AtomicInteger();

    /**
     * Construct a BB node and associate it with an ILP problem.
     *
     * @param problem an ILP problem
     */
    public ILPNode(ILPProblem problem) {
        this.problem = problem;

        LPBoundedMinimizer minimizer0;
        try {
            LPTwoPhaseSolver solver = new LPTwoPhaseSolver();
            LPSimplexSolution soln = solver.solve(problem);
            minimizer0 = (LPBoundedMinimizer) soln.minimizer();//bounded
        } catch (Exception ex) {
            minimizer0 = null;
        }

        this.minimizer = minimizer0;
        this.id = count.addAndGet(1);
    }

    @Override
    public ImmutableVector solution() {
        return minimizer != null ? minimizer.minimizer() : null;
    }

    @Override
    public double value() {
        return minimizer != null ? minimizer.minimum()
               : Double.POSITIVE_INFINITY;//infeasible
    }

    @Override
    public boolean isCandidate() {
        return minimizer != null
               ? problem.getNonIntegralIndices(minimizer.minimizer().toArray()).length == 0//all integral constraints satisfied
               : false;
    }

    /**
     * {@inheritDoc}
     *
     * <p/>
     * This implementation assumes
     * <ul>
     * <li>the parent node has a solution;
     * <li>the parent solution has more than 1 non-satisfying integral variable.
     * </ul>
     *
     * @return
     */
    @Override
    public List<ILPNode> branching() {
        List<ILPNode> list = new ArrayList<ILPNode>();
        int i = problem.getNonIntegralIndices(minimizer.minimizer().toArray())[0];//the index of the first non-satisfying integral variable, counting from 1

        //inherit properties from parent problem
        Vector c = problem.c();
        LinearGreaterThanConstraints greater = (LinearGreaterThanConstraints) problem.getLessThanConstraints().toGreaterThanConstraints();
        int[] indices = problem.getIntegerIndices();
        double epsilon = problem.epsilon();

        //construct a less-than subproblem
        DenseVector floor = new DenseVector(Math.floor(minimizer.minimizer().get(i)));
        DenseMatrix yi = new DenseMatrix(1, minimizer.minimizer().size());
        yi.set(1, i, 1);
        LinearLessThanConstraints less = new LinearLessThanConstraints(yi, floor);//y_i < floor
        ILPProblem problemFloor = new ILPProblemImpl1(
                c,
                greater,
                less,
                null, null, indices, epsilon);
        list.add(new ILPNode(problemFloor));

        //construct a greater-than subproblem
        DenseVector ceil = new DenseVector(-Math.ceil(minimizer.minimizer().get(i)));
        yi = new DenseMatrix(1, minimizer.minimizer().size());
        yi.set(1, i, -1);
        less = new LinearLessThanConstraints(yi, ceil);//y_i > ceil
        ILPProblem problemCeil = new ILPProblemImpl1(
                c,
                greater,
                less,
                null, null, indices, epsilon);
        list.add(new ILPNode(problemCeil));

        return list;
    }

    @Override
    public LessThanConstraints getLessThanConstraints() {
        return problem.getLessThanConstraints();
    }

    @Override
    public EqualityConstraints getEqualityConstraints() {
        return problem.getEqualityConstraints();
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("node %d:\n", id));
        str.append("problem:\n");
        str.append(problem.toString()).append("\n");
        str.append("soluiton:\n");
        str.append(minimizer != null ? minimizer.toString() : "").append("\n");
        return str.toString();
    }
}
