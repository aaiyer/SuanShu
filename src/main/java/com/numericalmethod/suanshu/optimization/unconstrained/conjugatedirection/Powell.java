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
import com.numericalmethod.suanshu.optimization.unconstrained.steepestdescent.SteepestDescent;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Powell's algorithm, starting from an initial point, performs a series
 * of line searches in one iteration.
 * The line search directions, except the last one, are all linearly independent.
 * The major advantage of Powell’s algorithm is that
 * the Hessian needs not be supplied, stored or manipulated.
 * However, this algorithm has a few drawbacks and is superseded by Zangwill’s algorithm.
 * For example, in an iteration, linear dependence can sometimes arise,
 * which may fail to find complete the set of linearly independent directions that span <i>E<sup>n</sup></i>,
 * even in the case of a convex quadratic problem.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Theorem 6.5, Algorithm 6.4," Practical Optimization: Algorithms and Engineering Applications."
 */
public class Powell extends SteepestDescent {

    /**
     * an implementation of Powell's algorithm
     */
    protected class PowellImpl extends SteepestDescentImpl {

        private Vector dk = null;
        private Vector[] d0;
        private final int n;

        protected PowellImpl(C2OptimProblem problem) throws Exception {
            super(problem);
            n = problem.f().dimensionOfDomain();
            d0 = new Vector[n + 1];//d0[0] is not used
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            Vector xki = xk.add(dk);

            double ak1 = linesearch2(xki, dk);//<code>a<sub>k+1</sub></code>
            ak1 += 1;//+1 is for adding dk to xk in SteepestDescent.java to get xki = xk + dk; the linesearch starts from xki
            return ak1;
        }

        @Override
        public Vector getDirection(Vector xk) {
            Vector xki = xk;

            if (dk == null) {
                //generate the <i>n</i> initial linearly independent linesearch directions from the initial point
                for (int i = 1; i <= n; ++i) {
                    d0[i] = new DenseVector(n);
                    d0[i].set(i, xk.get(i));
                }
            } else {
                //replace the last direction with the last computed linesearch direction
                for (int i = 1; i <= n - 1; ++i) {
                    d0[i] = d0[i + 1];//shift out the first direction
                }
                d0[n] = dk;
            }

            //in each iteration, <i>f</i> is minimized sequentially in each of the <i>n</i> directions
            for (int i = 1; i <= n; ++i) {
                double aki = linesearch2(xki, d0[i]);
                xki = xki.add(d0[i].scaled(aki));
            }

            dk = xki.minus(xk);
            return dk;
        }

        /** Find alpha along two directions; take the bigger alpha (step change). */
        private double linesearch2(Vector x, Vector d) {
            double a0 = linesearch.linesearch(x, d);
            double a1 = linesearch.linesearch(x, d.scaled(-1));

            if (a0 >= a1) {
                return a0;
            } else {
                return -a1;
            }
        }
    }

    /**
     * Construct a multivariate minimizer using the Powell method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public Powell(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new PowellImpl(problem);
    }
}
