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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure.Frobenius;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure.tr;
import com.numericalmethod.suanshu.matrix.doubles.operation.*;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.problem.SDPDualProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.ArrayList;

/**
 * This implementation solves a Semi-Definite Programming problem using the Homogeneous Self-Dual Path-Following algorithm.
 *
 * @author Weng Bo
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Algorithm 14.1, Primal-dual path-following algorithm," Practical Optimization: Algorithms and Engineering Applications."
 * <li>"K. C. Toh, M. J. Todd, R. H. Tütüncü, "SDPT3 -- a MATLAB software package for semidefinite programming, version 2.1," OPTIMIZATION METHODS AND SOFTWARE, 1999."
 * </ul>
 */
public class HomogeneousPathFollowing implements ConstrainedMinimizer<SDPDualProblem, IterativeMinimizer<CentralPath>> {

    /**
     * This is the solution to a Semi-Definite Programming problem using the Homogeneous Self-Dual Path-Following algorithm.
     */
    public class Solution extends PrimalDualPathFollowing.Solution {

        private double kappa;
        private Vector V;
        private Matrix B;
        double tau;
        private Vector ybar;

        /** solve the semi-definite programming problem using the Homogeneous Self-Dual Path-Following algorithm */
        protected Solution(PrimalDualPathFollowing parent, SDPDualProblem problem, double gamma0, double sigma0) {
            parent.super(problem, gamma0, sigma0);

            kappa = 0.1;
            final int p = problem.p();
            V = new DenseVector(p + 1);//zero vector
            V.set(p + 1, kappa);

            //[0 -b]
            //[b' 0]
            B = new DenseMatrix(p + 1, p + 1);//zero
            for (int i = 1; i < B.nRows(); i++) {
                B.set(i, B.nCols(), -problem.b().get(i));
            }
            for (int i = 1; i < B.nCols(); i++) {
                B.set(B.nRows(), i, problem.b().get(i));
            }
        }

        @Override
        public CentralPath search(CentralPath initial) throws Exception {
            path = initial;

            tau = 0.1;
            ybar = CreateVector.concat(path.y, new DenseVector(tau));//eq. 26

            for (; step(); ++iter) {
            }

            //create the solution by scaling
            double scale = 1. / tau;
            Matrix X = path.X.scaled(scale);
            Matrix S = path.S.scaled(scale);
            Vector y = CreateVector.subVector(ybar, 1, ybar.size() - 1);
            y = y.scaled(scale);

            path = new CentralPath(X, y, S);
            return path;
        }

        @Override
        public Boolean step() {
            final int n = problem.n();

            //step 2: check convergence
            delta = (tr(path.X.multiply(path.S)) + tau * kappa) / (n + 1);//duality gap, eq. 28

            Vector AX = new DenseVector(problem.p());
            for (int i = 1; i <= AX.size(); i++) {
                AX.set(i, tr(problem.A(i).multiply(path.X)));//eq. 3
            }
            Vector primalResidual = problem.b().scaled(tau).minus(AX);
            double phi1 = primalResidual.norm() / (tau * Math.max(1, problem.b().norm()));

            Matrix Ay = new DenseMatrix(n, n);
            Ay = Ay.ZERO();
            for (int i = 1; i <= path.y.size(); i++) {
                Ay.add(problem.A(i).scaled(path.y.get(i)));
            }
            Matrix dualResidual = problem.C().scaled(tau).minus(path.S).minus(Ay);
            double phi2 = Frobenius(dualResidual) / (tau * Math.max(1, Frobenius(problem.C())));

            phi = Math.max(phi1, phi2);//eq. 39

            //stopping criterion (a), (X/tau, y/tau, S/tau) is a feasible solution
            if (delta < epsilon || phi < epsilon) {
                return false;
            }

            //step 3
            double sigmamu = sigma * delta;//eq. 14.44
            double eta = 1 - sigma;

            //step 4: solve for {dX, dy, dS}
            Matrix E = new SymmetricKronecker(path.S, I);
            Matrix F = new SymmetricKronecker(path.X, I);
            Vector Rc = new SVEC(I.scaled(sigmamu).minus(Hp.evaluate(path.X.multiply(path.S))));//eq. 28
            double rc = sigmamu - tau * kappa;//eq. 28

            Vector x = new SVEC(path.X);//eq. 14.41b
            Vector rp = V.minus(B.multiply(ybar)).minus(A.multiply(x));//eq. 27
            Vector Rd = new SVEC(path.S.scaled(-1).minus(new MAT(A.t().multiply(ybar))));//eq. 26

            //eq. 29 - 31            
            Inverse Einv = new Inverse(E);
            Matrix AEinv = A.multiply(Einv);
            Matrix M = AEinv.multiply(F).multiply(A.t());//eq. 30

            Vector h2 = V.ZERO();
            h2.set(V.size(), rc / tau);
            Vector h3 = AEinv.multiply(F).multiply(Rd).scaled(eta);
            Vector h4 = AEinv.multiply(Rc);
            Vector h = rp.scaled(eta).add(h2).add(h3).minus(h4);//eq. 31

            Matrix bbars = B;
            bbars.set(B.nRows(), B.nCols(), kappa / tau);

            Matrix N = M.add(bbars);//eq. 29, LHS
            Inverse Ninv = new Inverse(N);
            Vector dydtau = Ninv.multiply(h);//TODO: user a linear solver instead

            //eq. 32
            Vector ds = Rd.scaled(eta).minus(A.t().multiply(dydtau));
            Vector dx = Einv.scaled(-1).multiply(F.multiply(ds).minus(Rc));
            double dtau = dydtau.get(dydtau.size());
            double dkappa = (rc - kappa * dtau) / tau;

            Matrix dX = new MAT(dx);//eq. 14.40a
            Matrix dS = new MAT(ds);//eq. 14.40b

            //step 5
            double alpha = increment(path.X, dX, tau, dtau, kappa, dkappa);
            double beta = increment(path.S, dS, tau, dtau, kappa, dkappa);

            //TODO: check eq. 38


            //step 6           
            double taup = tau + alpha * dtau;//eq. 33
            double taud = tau + beta * dtau;
            tau = Math.min(taup, taud);

            //eq. 34, 41
            if (tau == taup) {
                kappa += alpha * dkappa;
            } else {
                kappa += beta * dkappa;
            }

            Matrix X = path.X.add(dX.scaled(alpha)).scaled(tau / taup);
            Matrix S = path.S.add(dS.scaled(beta)).scaled(tau / taud);
            Vector ybar1 = new DenseVector(ybar);
            ybar1.set(ybar1.size(), tau);
            for (int i = 1; i < ybar1.size(); i++) {
                ybar1.set(i, tau / taud * (ybar1.get(i) + beta * dydtau.get(i)));
            }

            //updates
            path = new CentralPath(X, CreateVector.subVector(ybar1, 1, ybar1.size() - 1), S);
            ybar = ybar1;

            V.set(V.size(), kappa);
            gamma = 0.9 + 0.09 * Math.min(alpha, beta);//eq. 42
            sigma = 1 - 0.9 * Math.min(alpha, beta);

            return true;
        }

        /** eq. 40 */
        private double increment(Matrix M, Matrix dM, double tau, double dtau, double k, double dk) {
            Inverse Sinv = new Inverse(M);
            Matrix UtDU = Sinv.multiply(dM);

            double minLambda = PrimalDualPathFollowing.getMinEigenValue(UtDU, epsilon);
            double dtt = dtau / tau;
            double dkk = dk / k;

            ArrayList<Double> inc = new ArrayList<Double>();
            inc.add(1.);
            if (minLambda < 0) {//ignore the term if -ve.
                inc.add(-gamma / minLambda);
            }
            if (dtt < 0) {//ignore the term if -ve.
                inc.add(-gamma / dtt);
            }
            if (dkk < 0) {//ignore the term if -ve.
                inc.add(-gamma / dkk);
            }

            double alpha = DoubleArrayMath.min(DoubleUtils.collection2DoubleArray(inc));
            return alpha;
        }

        /** Toh, Todd, Tütüncü, Section 3.1, A^ */
        @Override
        protected Matrix svecA() {
            Matrix svecA = super.svecA();
            Vector svecC = new SVEC(problem.C().scaled(-1));

            return CreateMatrix.rbind(svecA, new DenseMatrix(svecC).t());
        }
    }

    private final double gamma0;
    private final double sigma0;
    private final double epsilon;
    private final Hp Hp = new Hp();

    /**
     * Construct a Homogeneous Self-Dual Path-Following minimizer to solve semi-definite programming problems.
     *
     * @param gamma0  \(0 < \gamma < 1\). It ensures the next iterates are inside the feasible set; suggested values are between 0.9 and 0.99.
     * @param sigma0  \(0 \leq \sigma < 1\), the centering parameter
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public HomogeneousPathFollowing(double gamma0, double sigma0, double epsilon) {
        this.gamma0 = gamma0;
        this.sigma0 = sigma0;
        this.epsilon = epsilon;
    }

    /**
     * Construct a Homogeneous Self-Dual Path-Following minimizer to solve semi-definite programming problems.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public HomogeneousPathFollowing(double epsilon) {
        this(0.9, 0.1, epsilon);
    }

    @Override
    public Solution solve(SDPDualProblem problem) throws Exception {
        return new Solution(
                new PrimalDualPathFollowing(gamma0, sigma0, epsilon),//an enclosing instance
                problem, gamma0, sigma0);
    }
}
