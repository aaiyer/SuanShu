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
package com.numericalmethod.suanshu.optimization.unconstrained;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.initialization.DefaultSimplex;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * The Nelder–Mead method is a nonlinear optimization technique,
 * which is well-defined for twice differentiable and unimodal problems.
 * It starts with a simplex, and then uses operations
 * reflection, expansion, contraction, and reduction to search for the minimum/maximum point.
 * However, the Nelder–Mead technique is only a heuristic,
 * since it can converge to non-stationary points on problems that can be solved by alternative methods.
 * It does not always converge even for smooth problems.
 * In practice, however, the performance is generally good.
 * Note that one dimensional optimization by Nelder-Mead is unreliable.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Nelder-Mead_method">Wikipedia: Nelder–Mead method</a>
 */
public class NelderMead implements MultivariateMinimizer<IterativeMinimizer<Vector>> {//TODO: convergence criterion

    /**
     * This is the solution to an optimization problem by the Nelder-Mead method.
     */
    public class Solution implements IterativeMinimizer<Vector> {

        private Vector[] x;
        private double[] fx;
        private final RealScalarFunction f;//the objective function
        private final int N;//degree of freedom for \(f\)
        private final int N1;//1 + degree of freedom for \(f\)

        private Solution(C2OptimProblem problem) {
            this.f = problem.f();
            this.N = f.dimensionOfDomain();
            this.N1 = this.N + 1;
        }

        @Override
        public double minimum() {
            return fx[0];
        }

        @Override
        public ImmutableVector minimizer() {
            return new ImmutableVector(x[0]);
        }

        /**
         * Perform a Nelder-Mead search from an initial simplex.
         *
         * @param simplex the initial simplex for the search
         * @return the optimal point
         */
        @Override
        public Vector search(Vector... simplex) {
            setInitials(simplex);

            double last_fx = Double.POSITIVE_INFINITY;
            for (int i = 0; i < maxIterations; ++i) {//TODO: why always run up to maxIterations
                step();

                if (i > MINIMUM_NUMBER_OF_ITERATIONS && Math.abs(fx[0] - last_fx) < epsilon) {//TODO: we need enough initial iterations before breaking
                    break;
                }

                last_fx = fx[0];
            }

            //find xmin
            return minimizer();
        }

        @Override
        public void setInitials(Vector... simplex) {
            if (simplex.length != N1) {
                setInitials(new DefaultSimplex().getInitials(simplex[0]));
                return;
            }

            for (int i = 0; i < simplex.length; ++i) {
                SuanShuUtils.assertArgument(simplex[i].size() == N, "the dimension of x does not match the degree of freedom of f");
            }

            //initialize x and f(x)
            x = simplex;
            fx = new double[N1];
            for (int i = 0; i < N1; ++i) {
                fx[i] = f.evaluate(simplex[i]);//initialize the N+1 vertices of a simplex
            }

            sort();
        }

        @Override
        public Boolean step() {
            Vector xo = new DenseVector(N);//centroid = (∑(x<sub>i</sub>))/N, i excludes the worst x point
            for (int i = 0; i < N; ++i) {
                xo = xo.add(x[i]);
            }
            xo = xo.scaled(1d / N);

            //reflection
            Vector xr = xo.add(xo.minus(x[N]).scaled(alpha));//reflected pt = xo + alpha * (xo - x[N])
            double fxr = f.evaluate(xr);
            if (fx[0] <= fxr && fxr < fx[N - 1]) {//reflected point is neither best nor worst in the new simplex
                replaceWorst(xr, fxr);//replace the worst point with the reflected point
            } else if (fxr < fx[0]) {//reflected point is better than the current best; try to go farther along this direction
                //expansion
                Vector xe = xo.add(xo.minus(x[N]).scaled(gamma));//expanded point = xo + gamma * (xo - fx[N])
                double fxe = f.evaluate(xe);
                if (fxe < fxr) {
                    replaceWorst(xe, fxe);//replace the worst point with the expansion point
                } else {
                    replaceWorst(xr, fxr);//replace the worst point with the reflected point
                }
            } else {//fx[N - 1] <= fxr, i.e., reflected point is still worse than the second worst
                Vector xc;
                if (fxr < fx[N]) {
                    //outer contraction
                    xc = xo.add(xr.minus(xo).scaled(rho));//contracted point = xo + rho * (xr - xo)
                } else {//fxr >= fx[N]; wiki has only this step
                    //inner contraction
                    xc = xo.add(x[N].minus(xo).scaled(rho));//contracted point = xo + rho * (worst x - xo)
                }

                double fxc = f.evaluate(xc);
                if ((fxr < fx[N] && fxc <= fxr)//the contracted point is better than the reflected point
                    || (fxr >= fx[N] - 1e-14 && fxc < fx[N])) {//the contracted point is better than the worst point
                    replaceWorst(xc, fxc);//replace the worst point with the contracted point
                } else {
                    //Reduction
                    for (int i = 1; i < N1; ++i) {
                        x[i] = x[0].add(x[i].minus(x[0]).scaled(sigma));//x[i] = x[0] + sigma * (x[i] - x[0])
                        fx[i] = f.evaluate(x[i]);
                    }
                }
            }

            sort();
            return true;
        }

        private void sort() {
            //sort x/initials/vertices according to values fx
            int[] order = R.order(fx);
            Vector[] x = new Vector[N1];
            for (int i = 0; i < N1; ++i) {
                x[i] = this.x[order[i] - 1];//copy only the references
            }
            Arrays.sort(fx);//sort fx in ascending order to correspond to x
            this.x = x;
        }

        private void replaceWorst(Vector x, double fx) {
            this.x[N] = x;
            this.fx[N] = fx;
        }
    }

    private static final int MINIMUM_NUMBER_OF_ITERATIONS = 50;
    private final double alpha;//the reflection coefficient
    private final double gamma;//the expansion coefficient
    private final double rho;//the contraction coefficient
    private final double sigma;//the shrink/reduction coefficient
    private final double epsilon;
    private final int maxIterations;

    /**
     * Construct a Nelder-Mead multivariate minimizer.
     *
     * @param alpha         the reflection coefficient
     * @param gamma         the shrink/reduction coefficient
     * @param rho           the contraction coefficient
     * @param sigma         the shrink/reduction coefficient
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public NelderMead(double alpha, double gamma, double rho, double sigma, double epsilon, int maxIterations) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.rho = rho;
        this.sigma = sigma;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a Nelder-Mead multivariate minimizer.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public NelderMead(double epsilon, int maxIterations) {
        this(1, 2, 0.5, 0.5, epsilon, maxIterations);
    }

    @Override
    public NelderMead.Solution solve(C2OptimProblem problem) {
        return new Solution(problem);
    }
}
