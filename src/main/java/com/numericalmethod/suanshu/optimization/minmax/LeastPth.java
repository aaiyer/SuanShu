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
package com.numericalmethod.suanshu.optimization.minmax;

import com.numericalmethod.suanshu.analysis.differentiation.multivariate.Gradient;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.abs;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.max;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import java.util.List;

/**
 * The least p-th minmax algorithm minimizes the maximal error/loss (function):
 * \[
 * \min_x \max_{\omega \in S} e(x, \omega)
 * \]
 * \(e(x, \omega)\) is the error or loss function.
 * <p/>
 * This implementation assumes the set <i>S</i> is discrete and finite.
 * To use this to solve a continuous <i>S</i> set, a sampling of <i>S</i> is required.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 8.1.," Practical Optimization: Algorithms and Engineering Applications."
 */
public class LeastPth<T> implements MinMaxMinimizer<T> {

    private class Solution implements IterativeMinimizer<Vector> {

        private Vector xmin;
        private double Ek = Double.MAX_VALUE;
        private int p = 2;
        private final int mu = 2;
        private final MinMaxProblem<T> problem;
        private final List<T> omega;
        private final T omega0;

        private Solution(MinMaxProblem<T> problem) {
            this.problem = problem;
            this.omega = problem.getOmega();
            this.omega0 = omega.get(0);
        }

        @Override
        public double minimum() {
            return Ek;
        }

        @Override
        public ImmutableVector minimizer() {
            return new ImmutableVector(xmin);
        }

        @Override
        public Vector search(Vector... initial) throws Exception {
            setInitials(initial);

            for (int iter = 1; iter <= maxIterations; ++iter, p *= mu) {
                double Ek1 = step();
                if (abs(Ek1 - Ek) < epsilon) {
                    Ek = Ek1;
                    break;
                }

                Ek = Ek1;
            }

            return minimizer();
        }

        @Override
        public void setInitials(Vector... initials) {
            Ek = Double.MAX_VALUE;
            xmin = initials[0];
        }

        @Override
        public Double step() throws Exception {
            RealScalarFunction psi = psi();
            RealVectorFunction g_psi = problem.gradient(omega0) != null ? psiGradient() : getNumericalGradient();
            BFGS optim = new BFGS(false, epsilon, maxIterations);
            IterativeMinimizer<Vector> soln = optim.solve(new C2OptimProblemImpl(psi, g_psi));
            xmin = soln.search(xmin);

            //compute e(x, ωi) and E^(x)
            double[] ei = ei(xmin);
            double Ek1 = max(abs(ei));//E<sub>k+1</sub>
            return Ek1;
        }

        /** Apply eq. 8.6b to <i>x</i> for different <i>ω ∈ S</i>. */
        private double[] ei(Vector x) {
            int K = omega.size();
            double[] e = new double[K];

            //compute e(x, ωi)
            for (int i = 0; i < K; ++i) {
                RealScalarFunction f = problem.error(omega.get(i));
                e[i] = f.evaluate(x);//eq. 8.6b
            }

            return e;
        }

        /**
         * @return <i>Ψ<sub>k</sub>(x)</i>
         * @see "Andreas Antoniou, Wu-Sheng Lu, "Eq. 8.6a," Practical Optimization: Algorithms and Engineering Applications."
         */
        private RealScalarFunction psi() {
            RealScalarFunction psi_k = new RealScalarFunction() {

                @Override
                public Double evaluate(Vector x) {
                    //compute e(x, ωi) and E^(x)
                    double[] ei = ei(x);//eq. 8.6b
                    double emax = max(abs(ei));//eq. 8.6c

                    Vector eiv = new DenseVector(ei);
                    eiv = eiv.scaled(1 / emax);
                    double result = eiv.norm(p);
                    result *= emax;

                    return result;
                }

                @Override
                public int dimensionOfDomain() {
                    RealScalarFunction f = problem.error(omega0);
                    return f.dimensionOfDomain();
                }

                @Override
                public int dimensionOfRange() {
                    return 1;
                }
            };

            return psi_k;
        }

        /**
         * The derivation assumes that E(x) is a constant.
         *
         * @return <i>∇Ψ<sub>k</sub>(x)</i>
         * @see "Andreas Antoniou, Wu-Sheng Lu, "Eq. 8.7," Practical Optimization: Algorithms and Engineering Applications."
         */
        private RealVectorFunction psiGradient() {
            RealVectorFunction g_psi_k = new RealVectorFunction() {

                @Override
                public Vector evaluate(Vector x) {
                    int K = omega.size();
                    //compute e(x, ωi) and E^(x)
                    double[] ei = ei(x);
                    double emax = max(abs(ei));

                    Vector eiv = new DenseVector(ei);
                    eiv = eiv.scaled(1 / emax);
                    double term1 = eiv.norm(p);
                    term1 = pow(term1, 1 - p);

                    int n = dimensionOfRange();
                    Vector term2 = new DenseVector(n);//0 vector

                    for (int i = 0; i < K; ++i) {
                        double c1 = pow(abs(ei[i]) / emax, p - 1);
                        RealVectorFunction g = problem.gradient(omega.get(i));
                        Vector ge = g.evaluate(x);
                        ge = ge.scaled(c1);
                        term2 = term2.add(ge);
                    }

                    Vector result = term2.scaled(term1);

                    return result;
                }

                @Override
                public int dimensionOfDomain() {
                    RealScalarFunction f = problem.error(omega0);
                    return f.dimensionOfDomain();
                }

                @Override
                public int dimensionOfRange() {
                    return dimensionOfDomain();
                }
            };

            return g_psi_k;
        }

        /** <i>∇Ψ<sub>k</sub>(x)</i> */
        private RealVectorFunction getNumericalGradient() {
            RealVectorFunction g_psi_k = new RealVectorFunction() {

                @Override
                public Vector evaluate(Vector x) {
                    RealScalarFunction f = psi();
                    Gradient g = new Gradient(f, x);
                    return g;
                }

                @Override
                public int dimensionOfDomain() {
                    RealScalarFunction f = problem.error(omega0);
                    return f.dimensionOfDomain();
                }

                @Override
                public int dimensionOfRange() {
                    return dimensionOfDomain();
                }
            };

            return g_psi_k;
        }
    }

    private final double epsilon;
    private final int maxIterations;

    /**
     * Construct a minmax minimizer using the Least p-th method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public LeastPth(double epsilon, int maxIterations) {
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    @Override
    public IterativeMinimizer<Vector> solve(MinMaxProblem<T> problem) {
        return new Solution(problem);
    }
}
