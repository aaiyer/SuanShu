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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.pathfollowing;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.Cholesky;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.*;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.problem.SDPDualProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 * The Primal-Dual Path-Following algorithm is an interior point method that solves Semi-Definite Programming problems.
 * In theory, the algorithm assumes feasible starting points.
 * In practice when there is no feasible starting point, the program utilizes an infeasible interior point method.
 * Specifically, this implementation relaxes the feasible constraints and require only the initials <i>X<sub>0</sub></i> and <i>S<sub>0</sub></i> be positive definite.
 * For example, let <i>X<sub>0</sub></i> and <i>S<sub>0</sub></i> be identity matrices, <i>y<sub>0</sub></i> be a zero vector.
 * This implementation adds one more condition to the terminal condition of iterations.
 * That is,
 * <blockquote><i>
 * phi = delta(duality gap) + norm(rd) + norm(rp)
 * </i></blockquote>
 * where <i>norm(rd) + norm(rp)</i> measures the feasibility of solution.
 *
 * @author Weng Bo
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 14.1, Primal-dual path-following algorithm," Practical Optimization: Algorithms and Engineering Applications."
 */
public class PrimalDualPathFollowing implements ConstrainedMinimizer<SDPDualProblem, IterativeMinimizer<CentralPath>> {

    /**
     * This is the solution to a Semi-Definite Programming problem using the Primal-Dual Path-Following algorithm.
     */
    public class Solution implements IterativeMinimizer<CentralPath> {

        protected CentralPath path;
        protected double sigma;
        protected double gamma;
        protected double delta = Double.POSITIVE_INFINITY;
        protected double phi = Double.POSITIVE_INFINITY;
        protected int iter = 0; //num of iterations
        protected final SDPDualProblem problem;
        /**
         * This is either [A] or
         *
         * [ A]
         * [-C]
         */
        protected final Matrix A;
        protected final int n;
        protected final Matrix I;

        protected Solution(SDPDualProblem problem, double gamma0, double sigma0) {//check sigma range [0, 1)
            this.problem = problem;
            this.gamma = gamma0;

            n = problem.n();
            this.sigma = !Double.isNaN(sigma0) ? sigma0 : n / (15 * sqrt(n) + n);//eq. 14.50;

            I = problem.C().ONE();

            A = svecA();
        }

        @Override
        public void setInitials(CentralPath... initials) {
            path = initials[0];
        }

        /**
         * {@inheritDoc}
         * <p/>
         * Algorithm 14.1.
         *
         * @return the residual norm
         */
        @Override
        public Boolean step() {
            //step 2: check convergence
            delta = MatrixMeasure.tr(path.X.multiply(path.S)) / n;//duality gap
            if (delta < epsilon) {
                return false;
            }

            //step 3
            double tau = sigma * delta;//eq. 14.44

            //step 4: solve for {dX, dy, dS}
            Matrix E = new SymmetricKronecker(path.S, I);//eq. 14.40c
            Matrix F = new SymmetricKronecker(path.X, I);//eq. 14.40d
            Vector rc = new SVEC(I.scaled(tau).minus((path.X.multiply(path.S).add(path.S.multiply(path.X))).scaled(0.5)));//eq. 14.40e

            Vector x = new SVEC(path.X);//eq. 14.41b
            Vector rp = problem.b().minus(A.multiply(x));//eq. 14.41e, primal residual
            Vector rd = new SVEC(problem.C().minus(path.S).minus(new MAT(A.t().multiply(path.y))));//eq. 14.41f, dual residual

            //eq. 14.43c            
            Vector Frdrc = F.multiply(rd).minus(rc);
            Inverse Einv = new Inverse(E);
            Matrix M1 = A.multiply(Einv);
            Matrix M = M1.multiply(F).multiply(A.t());//Schur complement matrix
            Inverse Minv = new Inverse(M);
            //TODO: user a linear solver instead
            Vector rhs = rp.add(A.multiply(Einv).multiply(Frdrc));
            Vector dy = Minv.multiply(rhs);

            Vector Atdy = A.t().multiply(dy);
            Vector dx = Einv.scaled(-1).multiply(Frdrc.minus(F.multiply(Atdy)));//eq. 14.43a
            Vector ds = rd.minus(Atdy);//eq. 14.43b

            Matrix dX = new MAT(dx);//eq. 14.40a
            Matrix dS = new MAT(ds);//eq. 14.40b

            //step 5
            double alpha = increment(path.X, dX);
            double beta = increment(path.S, dS);

            //step 6
            path = new CentralPath(
                    path.X.add(dX.scaled(alpha)),
                    path.y.add(dy.scaled(beta)),
                    path.S.add(dS.scaled(beta)));

            //"r(X,y,S) = normRP + normRD" controls the feasibility, p1012, H.Roumili
            double rpNorm = rp.norm();
            double rdNorm = rd.norm();
            phi = delta + rpNorm + rdNorm;

            return phi > epsilon;
        }

        @Override
        public CentralPath search(CentralPath... initials) throws Exception {
            switch (initials.length) {
                case 0:
                    return search();
                case 1:
                    return search(initials[0]);
                default:
                    throw new IllegalArgumentException("wrong number of parameters");
            }
        }

        public CentralPath search(CentralPath initial) throws Exception {
            setInitials(initial);

            for (; step(); ++iter) {
            }

            return path;
        }

        /**
         * Search for a solution that optimizes the objective function from the
         * given starting points.
         *
         * @return an (approximate) optimizer
         * @throws Exception when an error occurs during the search
         */
        public CentralPath search() throws Exception {
            return search(new CentralPath(
                    problem.C().ONE(),
                    problem.b().ZERO(),
                    problem.C().ONE()));
        }

        @Override
        public double minimum() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public CentralPath minimizer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private double increment(Matrix M, Matrix dM) {
            Cholesky cholesky = new Cholesky(M);
            UpperTriangularMatrix M_hat = cholesky.Lt();
            Inverse M_inv = new Inverse(M_hat);
            Matrix UtDU = new CongruentMatrix(M_inv, dM);

            double minLambda = getMinEigenValue(UtDU, epsilon);
            double inc = 1;
            if (minLambda < 0) {
                inc = Math.min(1, -gamma / minLambda);//eq. 14.47
            }

            return inc;
        }

        /**
         * @see "Andreas Antoniou, Wu-Sheng Lu. "eq. 14.41a," Practical Optimization: Algorithms and Engineering Applications."
         */
        protected Matrix svecA() {
            ArrayList<Vector> svecAi = new ArrayList<Vector>();
            for (int i = 1; i <= problem.p(); i++) {
                svecAi.add(new SVEC(problem.A(i)));
            }

            Matrix svecA = CreateMatrix.rbind(svecAi);//eq. 14.41a
            return svecA;
        }
    }

    private final double gamma0;
    private final double sigma0;
    private final double epsilon;
    private final Hp Hp = new Hp();

    /**
     * Construct a Primal-Dual Path-Following minimizer to solve semi-definite programming problems.
     *
     * @param gamma0  \(0 < \gamma < 1\). It ensures the next iterates are inside the feasible set; suggested values are between 0.9 and 0.99.
     * @param sigma0  \(0 \leq \sigma < 1\), the centering parameter
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public PrimalDualPathFollowing(double gamma0, double sigma0, double epsilon) {
        this.gamma0 = gamma0;
        this.sigma0 = sigma0;
        this.epsilon = epsilon;
    }

    /**
     * Construct a Primal-Dual Path-Following minimizer to solve semi-definite programming problems.
     *
     * @param gamma0  \(0 < \gamma < 1\). It ensures the next iterates are inside the feasible set; suggested values are between 0.9 and 0.99.
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public PrimalDualPathFollowing(double gamma0, double epsilon) {
        this(gamma0, Double.NaN, epsilon);
    }

    /**
     * Construct a Primal-Dual Path-Following minimizer to solve semi-definite programming problems.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public PrimalDualPathFollowing(double epsilon) {
        this(0.9, epsilon);
    }

    @Override
    public Solution solve(SDPDualProblem problem) throws Exception {
        return new Solution(problem, gamma0, sigma0);
    }

    /**
     * Get the minimum of all the eigen values of a matrix.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return the minimum of all the eigen values of a matrix
     */
    protected static double getMinEigenValue(Matrix A, double epsilon) {//TODO: eq. 14.47
        Eigen eigen = new Eigen(A, Eigen.Method.QR, epsilon);
        double[] v = eigen.getRealEigenvalues();
        return v[v.length - 1];
    }
}
