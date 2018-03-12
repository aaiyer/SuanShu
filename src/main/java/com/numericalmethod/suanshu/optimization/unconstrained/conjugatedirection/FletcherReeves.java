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
package com.numericalmethod.suanshu.optimization.unconstrained.conjugatedirection;

import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Fletcher-Reeves method is a variant of the Conjugate-Gradient method.
 * Instead of finding the minimizer along a direction using an analytical formula
 * as in {@link ConjugateGradient}, it uses a line search.
 * The advantages are:
 * <ul>
 * <li>The modification renders the method more amenable to
 * the minimization of non-quadratic problems since a larger reduction can be achieved in <i>f(x)</i>
 * along <i>d<sub>k</sub></i> at points outside the neighborhood of the solution.
 * <li>The modification does not compute the Hessian.
 * </ul>
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 6.3," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Fletcher-Reeves#Nonlinear_conjugate_gradient">Wikipedia: Conjugate gradient method</a>
 * </ul>
 */
public class FletcherReeves extends ConjugateGradient {

    private class FletcherReevesImpl extends HestenesStiefel {

        protected FletcherReevesImpl(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            double ak = linesearch.linesearch(xk, dk);
            return ak;
        }
    }

    /**
     * Construct a multivariate minimizer using the Fletcher-Reeves method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public FletcherReeves(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new FletcherReevesImpl(problem);
    }
}
