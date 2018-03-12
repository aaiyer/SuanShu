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

import com.numericalmethod.suanshu.algorithm.iterative.IterativeMethod;
import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.optimization.Optimizer;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A maximization problem is simply minimizing the negative of the objective function.
 * This class is simply provided as a wrapper class to solve a maximization problem using a {@link MultivariateMinimizer}.
 *
 * @author Haksun Li
 */
public class MultivariateMaximizer implements Optimizer<C2OptimProblem, MultivariateMaximizer.Solution> {

    public static interface Solution extends IterativeMethod<Vector> {

        /**
         * Get the maximum found.
         * <p>
         * Caching the result is especially useful for an objective function that takes a long time to compute.
         *
         * @return the maximum found
         */
        public double maximum();

        /**
         * Get the maximizer (solution) to the maximization problem.
         *
         * @return the maximizer
         */
        public ImmutableVector maximizer();
    }

    /** the minimizer to do maximization */
    private final MultivariateMinimizer<? extends IterativeMinimizer<Vector>> minimizer;

    /**
     * Construct a multivariate maximizer to maximize an objective function.
     *
     * @param minimizer a multivariate minimizer
     */
    public <T extends MultivariateMinimizer<? extends IterativeMinimizer<Vector>>> MultivariateMaximizer(T minimizer) {
        this.minimizer = minimizer;
    }

    /**
     * Construct a multivariate maximizer to maximize an objective function.
     * By default, we use the NelderMead algorithm.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public MultivariateMaximizer(double epsilon, int maxIterations) {
        this(new NelderMead(epsilon, maxIterations));
    }

    @Override
    public MultivariateMaximizer.Solution solve(final C2OptimProblem problem) throws Exception {
        final IterativeMinimizer<Vector> soln0 = minimizer.solve(new C2OptimProblemImpl(
                new RealScalarFunction() {//negative f

                    @Override
                    public Double evaluate(Vector x) {
                        return -1. * problem.f().evaluate(x);
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return problem.f().dimensionOfDomain();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return problem.f().dimensionOfRange();
                    }
                },
                new RealVectorFunction() {//negative g

                    @Override
                    public Vector evaluate(Vector x) {
                        return problem.g().evaluate(x).scaled(-1);
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return problem.g().dimensionOfDomain();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return problem.g().dimensionOfRange();
                    }
                },
                new RntoMatrix() {//negative H

                    @Override
                    public Matrix evaluate(Vector x) {
                        return problem.H().evaluate(x).scaled(-1);
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return problem.H().dimensionOfDomain();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return problem.H().dimensionOfRange();
                    }
                }));

        MultivariateMaximizer.Solution soln1 = new MultivariateMaximizer.Solution() {

            @Override
            public double maximum() {
                return -soln0.minimum();
            }

            @Override
            public ImmutableVector maximizer() {
                return new ImmutableVector(soln0.minimizer());
            }

            @Override
            public void setInitials(Vector... initials) {
                soln0.setInitials(initials);
            }

            @Override
            public Object step() throws Exception {
                return soln0.step();
            }

            @Override
            public Vector search(Vector... initials) throws Exception {
                return soln0.search(initials);
            }
        };

        return soln1;
    }
}
