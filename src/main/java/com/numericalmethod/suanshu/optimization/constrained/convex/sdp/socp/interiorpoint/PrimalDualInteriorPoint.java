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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.interiorpoint;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LinearSystemSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.problem.SOCPDualProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import static java.lang.Math.*;

/**
 * This implementation solves a Dual Second Order Conic Programming problem using the Primal Dual Interior Point algorithm.
 *
 * @author Weng Bo
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 14.5, Section 14.8.2, A primal-dual interior-point algorithm," Practical Optimization: Algorithms and Engineering Applications."
 * <li>"K. C. Toh, M. J. Todd, R. H. Tütüncü, "SDPT3 -- a MATLAB software package for semidefinite programming, version 3.0," OPTIMIZATION METHODS AND SOFTWARE, 2001."
 * </ul>
 */
public class PrimalDualInteriorPoint implements ConstrainedMinimizer<SOCPDualProblem, IterativeMinimizer<PrimalDualSolution>> {

    /**
     * This is the solution to a Dual Second Order Conic Programming problem using the Primal Dual Interior Point algorithm.
     */
    public class Solution implements IterativeMinimizer<PrimalDualSolution> {

        private PrimalDualSolution soln;
        private int iter = 0; //the number of iterations
        private final SOCPDualProblem problem;
        private final ImmutableMatrix A;
        private final ImmutableMatrix At;
        private final int N;

        private Solution(SOCPDualProblem problem) {
            this.problem = problem;
            this.A = new ImmutableMatrix(problem.A());
            this.At = new ImmutableMatrix(this.A.t());
            this.N = this.A.nCols();
        }

        @Override
        public double minimum() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public PrimalDualSolution minimizer() {
            return soln;
        }

        @Override
        public void setInitials(PrimalDualSolution... initials) {
            soln = initials[0];
        }

        @Override
        public PrimalDualSolution search(PrimalDualSolution... initials) throws Exception {
            switch (initials.length) {
                case 0:
                    return search();
                case 1:
                    return search(initials[0]);
                default:
                    throw new IllegalArgumentException("wrong number of parameters");
            }
        }

        /**
         * Search for a solution that optimizes the objective function from the
         * given starting point.
         *
         * @param initial an initial guess
         * @return an (approximate) optimizer
         * @throws Exception when an error occurs during the search
         */
        public PrimalDualSolution search(PrimalDualSolution initial) throws Exception {
            setInitials(initial);

            for (; iter < maxIterations; iter++) {
                if (!step()) {
                    break;
                }
            }

            return minimizer();
        }

        /**
         * Search for a solution that optimizes the objective function from the
         * starting point given by K. C. Toh, SDPT3 Version 3.0, p. 6.
         *
         * @return an (approximate) optimizer
         * @throws Exception when an error occurs during the search
         */
        public PrimalDualSolution search() throws Exception {
            Vector[] eq = new Vector[problem.q()];
            for (int i = 0; i < problem.q(); i++) {
                eq[i] = new DenseVector(problem.n(i + 1));//different sizes
                eq[i].set(1, 1);
            }
            Vector e = CreateVector.concat(eq);

            //compute xi
            double xi = 1;
            for (int i = 1, act = 0; i <= problem.q(); act += problem.n(i++)) {//update 'act' before updating i
                for (int j = 1; j <= Math.min(problem.n(i), problem.b().size()); j++) {
                    double term = (1 + problem.b().get(j)) / (1 + A.getColumn(j + act).norm());
                    if (term > xi) {
                        xi = term;
                    }
                }
            }

            //compute eta
            double normMax = problem.c().norm();//norm of C
            for (int j = 1; j <= At.nCols(); j++) {
                double normAt = At.getColumn(j).norm();
                if (normAt > normMax) {
                    normMax = normAt;
                }
            }
            double eta = max(1, (1 + normMax) / sqrt(N));

            Vector x = e.scaled(xi);
            Vector s = e.scaled(eta);
            LinearSystemSolver lisol = new LinearSystemSolver(SuanShuUtils.autoEpsilon(At));
            Vector y = lisol.solve(At).getParticularSolution(problem.c().minus(s));//A.t()*y + s = c
            PrimalDualSolution soln0 = new PrimalDualSolution(x, s, y);

            return search(soln0);
        }

        @Override
        public Boolean step() {
            double mu = soln.x.innerProduct(soln.s) / problem.q();
            if (mu < epsilon) {
                return false;
            }

            //eq. 14.125: solve for dx, ds, dy
            DenseMatrix F = XS(soln.x);//X
            DenseMatrix E = XS(soln.s);//S

            //solving eq. 14.125 is similiar to solving eq. 14.42
            Vector rp = problem.b().minus(A.multiply(soln.x));
            Vector rd = problem.c().minus(soln.s).minus(At.multiply(soln.y));
            Vector rc = new DenseVector(R.rep(sigma * mu, N)).minus(F.multiply(soln.s));

            Inverse Einv = new Inverse(E);
            Matrix M = A.multiply(Einv).multiply(F).multiply(At);//the Schur complement on textbook p. 462
            Inverse Minv = new Inverse(M);
            Vector dy = Minv.multiply(rp.add(A.multiply(Einv).multiply(F.multiply(rd).minus(rc))));//eq. 14.43c
            Vector ds = rd.minus(At.multiply(dy));//eq. 14.43b
            Vector dx = Einv.multiply(rc).minus(Einv.multiply(F).multiply(ds));//eq. 14.43a

            //eq. 14.126: line search
            double alpha1 = increment(soln.x, dx);//eq. 14.126b
            double alpha2 = increment(soln.s, ds);//eq. 14.126c
            Vector t = problem.c().minus(At.multiply(soln.y));//eq. 14.126d
            Vector dt = At.scaled(-1).multiply(dy);
            double alpha3 = increment(t, dt);
            double alpha = 0.75 * DoubleArrayMath.min(alpha1, alpha2, alpha3);//eq. 14.126a

            //update x, y, s
            Vector x = soln.x.add(dx.scaled(alpha));
            Vector s = soln.s.add(ds.scaled(alpha));
            Vector y = soln.y.add(dy.scaled(alpha));
            soln = new PrimalDualSolution(x, s, y);

            return true;
        }

        /** eqs. 14.125d, 14.125e */
        private DenseMatrix XS(Vector xs) {
            int act = 0;//index to xi[0] in x = [x1, x2, ... xq]
            DenseMatrix XS = new DenseMatrix(N, N);

            for (int i = 1; i <= problem.q(); i++) {
                final int ni = problem.n(i);

                DenseMatrix XSi = new DenseMatrix(ni, ni);//zero

                for (int j = 1; j <= ni; j++) {
                    XSi.set(j, 1, xs.get(act + j));//fill first column with xi
                    XSi.set(1, j, xs.get(act + j));//fill first row with xi
                }

                for (int j = 2; j <= ni; j++) {
                    XSi.set(j, j, xs.get(act + 1));//fill diagonal with t*I
                }

                for (int j = 1; j <= ni; j++) {
                    for (int k = 1; k <= ni; k++) {
                        XS.set(j + act, k + act, XSi.get(j, k));//TODO: diag
                    }
                }

                act = act + ni;
            }

            return XS;
        }

        /** eq. 14.126: find_alpha.m */
        //TODO: there is no explanation in the book on this algorithm (line search within a cone)
        private double increment(Vector x, Vector dx) {
            double[] alpha = new double[problem.q()];

            for (int i = 0, act = 0; i < problem.q(); i++) {
                final int ni = problem.n(i + 1);

                double x1 = x.get(act + 1);
                Vector xr = CreateVector.subVector(x, act + 2, act + ni);

                double dx1 = dx.get(act + 1);
                Vector dxr = CreateVector.subVector(dx, act + 2, act + ni);

                double p0 = dx1 * dx1 - dxr.norm() * dxr.norm();
                double p1 = 2 * (x1 * dx1 - xr.innerProduct(dxr));
                double p2 = x1 * x1 - xr.norm() * xr.norm();

                double alpha1 = 1;//d1 >= 0
                if (dx1 < 0) {
                    alpha1 = 0.99 * (x1 / (-dx1));
                }

                double a1 = (-p1 + sqrt(p1 * p1 - 4 * p0 * p2)) / 2 / p0;
                double a2 = (-p1 - sqrt(p1 * p1 - 4 * p0 * p2)) / 2 / p0;
                double rt1 = min(a1, a2);
                double rt2 = max(a1, a2);
                double alpha2 = rt2;//p0 < 0
                if (p0 >= 0) {
                    if (rt1 > 0) {
                        alpha2 = rt1;
                    }
                    if (rt2 < 0) {
                        alpha2 = 1;
                    }
                }
                alpha[i] = min(alpha1, alpha2);

                act += ni;
            }

            double min = DoubleArrayMath.min(alpha);
            return min;
        }
    }

    private final double sigma;
    private final double epsilon;
    private final int maxIterations;

    /**
     * Construct a Primal Dual Interior Point minimizer to solve Dual Second Order Conic Programming problems.
     *
     * @param sigma         \(0 \leq \sigma < 1\), the centering parameter
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public PrimalDualInteriorPoint(double sigma, double epsilon, int maxIterations) {
        this.sigma = sigma;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a Primal Dual Interior Point minimizer to solve Dual Second Order Conic Programming problems.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public PrimalDualInteriorPoint(double epsilon, int maxIterations) {
        this(1e-5, epsilon, maxIterations);
    }

    @Override
    public Solution solve(SOCPDualProblem problem) throws Exception {
        return new Solution(problem);
    }
}
