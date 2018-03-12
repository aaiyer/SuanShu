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

import com.numericalmethod.suanshu.algorithm.bb.ActiveList;
import com.numericalmethod.suanshu.algorithm.bb.BranchAndBound;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.ILPProblem;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a Branch-and-Bound algorithm that solves Integer Linear Programming problems.
 *
 * @author Haksun Li
 * @see "Der-San Chen, Robert G. Batson, Yu Dang, "11.1.2 Branch-and-Bound Algorithm, " Applied Integer Programming: Modeling and Solution."
 */
public class ILPBranchAndBound implements IPMinimizer<ILPProblem, MinimizationSolution<Vector>> {

    /**
     * This factory constructs a new instance of {@code ActiveList} for each Integer Linear Programming problem.
     */
    public static interface ActiveListFactory {

        /**
         * Construct a new instance of {@code ActiveList} for an Integer Linear Programming problem.
         *
         * @return a new instance of {@code ActiveList}
         */
        public ActiveList newActiveList();
    }

    private final ActiveListFactory factory;

    /**
     * Construct a Branch-and-Bound minimizer to solve Integer Linear Programming problems.
     *
     * @param factory a factory that constructs a new instance of {@code ActiveList} for each problem
     */
    public ILPBranchAndBound(ActiveListFactory factory) {
        this.factory = factory;
    }

    /**
     * Construct a Branch-and-Bound minimizer to solve Integer Linear Programming problems.
     */
    public ILPBranchAndBound() {
        this(null);
    }

    @Override
    public MinimizationSolution<Vector> solve(final ILPProblem problem) throws Exception {
        return new MinimizationSolution<Vector>() {

            private final BranchAndBound bb;

            {
                bb = factory != null
                     ? new BranchAndBound(factory.newActiveList(), new ILPNode(problem))
                     : new BranchAndBound(new ILPNode(problem));
                bb.search();
            }

            @Override
            public double minimum() {
                return bb.minimum();
            }

            @Override
            public Vector minimizer() {
                return bb.minimizer();
            }
        };
    }
}
