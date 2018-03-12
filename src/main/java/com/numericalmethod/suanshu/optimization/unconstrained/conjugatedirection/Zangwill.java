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
 * Zangwill's algorithm is an improved version of Powell's algorithm.
 * It enhances the algorithm to generate a set of conjugate directions that are always linearly independent.
 * Despite the "improvement", in practice however,
 * Zangwill's algorithm seems to produce results that are less accurate than those of Powell's algorithm.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 6.5," Practical Optimization: Algorithms and Engineering Applications."
 */
public class Zangwill extends SteepestDescent {

    /**
     * an implementation of Zangwill's algorithm
     */
    protected class ZangwillImpl extends SteepestDescentImpl {

        private Vector dk = null;
        private Vector[] d0;
        private int m = Integer.MIN_VALUE;//not used in the first iteration
        private double akm = Double.NaN;//not used in the first iteration
        private double lambda = Double.NaN;//not used in the first iteration
        private double deltak = 1;//not used in the first iteration
        private final int n;//number of variables

        protected ZangwillImpl(C2OptimProblem problem) throws Exception {
            super(problem);
            n = problem.f().dimensionOfDomain();
            d0 = new Vector[n + 1];//d0[0] is not used
        }

        @Override
        public double getIncrement(Vector xk, Vector dk) {
            Vector xki = xk.add(dk);

            double ak1 = linesearch2(xki, dk);//<code>a<sub>k,n+1</sub></code>
            ak1 += 1;//+1 is for adding dk to xk in SteepestDescent.java to get xki = xk + dk; the linesearch starts from xki
            return ak1;
        }

        @Override
        public Vector getDirection(Vector xk) {
            Vector xki = xk;

            if (dk == null) {
                //generate the <i>n</i> initial linearly independent linesearch directions
                for (int i = 1; i <= n; ++i) {
                    d0[i] = new DenseVector(n);
                    d0[i].set(i, 1);
                }
            } else {
                double ratio = akm * deltak / lambda;

                if (ratio > epsilon2) {
                    d0[m] = dk;
                    deltak = ratio;
                }//else, do nothing, keeping the same conjugate directions
            }

            //in each iteration, <i>f</i> is minimized sequentially in each of the <i>n</i> directions
            akm = Double.NEGATIVE_INFINITY;
            for (int i = 1; i <= n; ++i) {
                double aki = linesearch2(xki, d0[i]);
                xki = xki.add(d0[i].scaled(aki));

                if (aki > akm) {
                    m = i;
                    akm = aki;//akm is the maximum of all aki's
                }
            }

            //a new linesearch direction is generated
            dk = xki.minus(xk);
            lambda = dk.norm();

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

    private final double epsilon2;

    /**
     * Construct a multivariate minimizer using the Zangwill method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param epsilon2      a precision parameter to decide whether there is a linear dependence among the conjugate directions
     * @param maxIterations the maximum number of iterations
     */
    public Zangwill(double epsilon, double epsilon2, int maxIterations) {
        super(epsilon, maxIterations);
        this.epsilon2 = epsilon2;
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new ZangwillImpl(problem);
    }
}
