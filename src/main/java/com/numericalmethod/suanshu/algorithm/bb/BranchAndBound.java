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

import com.numericalmethod.suanshu.algorithm.iterative.IterativeMethod;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.List;
import java.util.Stack;

/**
 * Branch-and-Bound (BB or B&B) is a general algorithm for finding optimal solutions of various optimization problems,
 * especially in discrete and combinatorial optimization.
 * It consists of a systematic enumeration of all candidate solutions, where large subsets of fruitless candidates are discarded en masse,
 * by using upper and lower estimated bounds of the quantity being optimized.
 * <p/>
 * This implementation is solving a <em>minimization</em> problem.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Branch_and_bound">Wikipedia: Branch and bound</a>
 */
public class BranchAndBound implements MinimizationSolution<Vector>, IterativeMethod<BBNode> {

    private ActiveList activeList;
    private BBNode incumbent;
    private double upper;

    /**
     * Solve a minimization problem using a branch-and-bound algorithm.
     *
     * @param activeList the node popping strategy, e.g., depth-first-search, best-first-search
     * @param root       the root node of a minimization problem
     */
    public BranchAndBound(ActiveList activeList, BBNode root) {
        this.activeList = activeList;
        setInitials(root);
    }

    /**
     * Solve a minimization problem using a branch-and-bound algorithm using depth-first search.
     *
     * @param root the root node of a minimization problem
     */
    public BranchAndBound(BBNode root) {
        this(
                new ActiveList() {

                    Stack<BBNode> stack = new Stack<BBNode>();

                    @Override
                    public BBNode pop() {
                        return stack.pop();
                    }

                    @Override
                    public boolean isEmpty() {
                        return stack.isEmpty();
                    }

                    @Override
                    public boolean add(BBNode e) {
                        return stack.add(e);
                    }

                    @Override
                    public void clear() {
                        stack.clear();
                    }
                },
                root);
    }

    @Override
    public double minimum() {
        return incumbent().value();
    }

    @Override
    public ImmutableVector minimizer() {


        return new ImmutableVector(incumbent().solution());
    }

    private BBNode incumbent() {
        if (incumbent == null) {
            try {
                search();
            } catch (Exception ex) {
                throw new RuntimeException(ex);//unreachable
            }
        }

        return incumbent;
    }

    @Override
    public final void setInitials(BBNode... root) {
        this.activeList.clear();
        this.activeList.add(root[0]);

        this.upper = Double.POSITIVE_INFINITY;
        this.incumbent = null;
    }

    @Override
    public Boolean step() throws Exception {
        //step 1: choose a node
        BBNode node = activeList.pop();

        //steps 2, 3, 4: prune by bound, infeasibility
        if (node.value() >= upper) {//TODO: use '>' may give a different solution
            return true;
        }

        if (node.isCandidate()) {
            incumbent = node;

            //step 2: update uppper bound
            if (node.value() < upper) {
                upper = node.value();
            }

            return true;//step 5: pruned by optimality
        }

        //not a candidate; e.g., not satisfying some constraints
        List<? extends BBNode> branches = node.branching();
        for (BBNode branch : branches) {
            activeList.add(branch);
        }

        return true;
    }

    @Override
    public BBNode search(BBNode... initials) throws Exception {//arguments not used
        //        //step 0: initialization
//        if (Double.isInfinite(root.value())) {
//            throw new Exception("problem is infeasible");
//        }

        while (!activeList.isEmpty()) {
            step();
        }

        return incumbent;
    }
}
