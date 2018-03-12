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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.nonstationary;

import com.numericalmethod.suanshu.algorithm.iterative.monitor.IterationMonitor;
import com.numericalmethod.suanshu.algorithm.iterative.monitor.NullMonitor;
import com.numericalmethod.suanshu.algorithm.iterative.tolerance.Tolerance;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.GivensMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure.Reason;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.IterativeLinearSystemSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.IdentityPreconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.Preconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.PreconditionerFactory;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * The Quasi-Minimal Residual method (QMR) is useful for solving a non-symmetric
 * n-by-n linear system. It applies a least-squares solve and update to the
 * {@linkplain BiconjugateGradientSolver BiCG} residuals, thereby smoothing out
 * the irregular convergence behavior of BiCG. Also, QMR largely avoids the
 * breakdown that can occur in BiCG. On the other hand, it does not effect a
 * true minimization of either the error or the residual. While it converges
 * smoothly, it does not essentially improve on the BiCG in terms of the number
 * of iteration steps.
 * <p/>
 * This implementation does not have a look ahead mechanism.
 * This implementation uses the split preconditioning (<i>M = M<sub>1</sub>M<sub>2</sub></i>).
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "Quasi-Minimal Residual Algorithm," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 7, sec. 7.3.2, p. 211-212."
 */
public class QuasiMinimalResidualSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final PreconditionerFactory rightPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a Quasi-Minimal Residual (QMR) solver.
     *
     * @param leftPreconditionerFactory  constructs a new left preconditioner
     * @param rightPreconditionerFactory constructs a new right preconditioner
     * @param residualRefreshRate        the number of iterations before the next refresh
     * @param maxIteration               the maximum number of iterations
     * @param tolerance                  the convergence threshold
     */
    public QuasiMinimalResidualSolver(PreconditionerFactory leftPreconditionerFactory,
                                      PreconditionerFactory rightPreconditionerFactory,
                                      int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.rightPreconditionerFactory = rightPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Quasi-Minimal Residual (QMR) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public QuasiMinimalResidualSolver(int maxIteration, Tolerance tolerance) {
        this(
                new PreconditionerFactory() {

                    @Override
                    public Preconditioner newInstance(Matrix A) {
                        return new IdentityPreconditioner();
                    }
                },
                new PreconditionerFactory() {

                    @Override
                    public Preconditioner newInstance(Matrix A) {
                        return new IdentityPreconditioner();
                    }
                },
                DEFAULT_RESIDUAL_REFRESH_RATE, maxIteration, tolerance);
    }

    public IterativeLinearSystemSolver.Solution solve(LSProblem problem) throws ConvergenceFailure {
        return solve(problem, new NullMonitor<Vector>());
    }

    @Override
    public IterativeLinearSystemSolver.Solution solve(final LSProblem problem, final IterationMonitor<Vector> monitor) throws ConvergenceFailure {
        return new Solution2(problem, monitor);
    }

    //<editor-fold defaultstate="collapsed" desc="Solution1">    
    /*
     * This implementation is due to
     * "Anne Greenbaum, "Algorithm 5," Iterative methods for solving linear systems, ch.5, pp.83."
     *
     * Note: Not used (this algorithm does not support preconditioning)
     */
    private class Solution1 implements IterativeLinearSystemSolver.Solution {

        private final IterationMonitor<Vector> monitor;
        private final Matrix A;
        private final Matrix Ah; // TODO: should be A^H (conjugate transpose)
        private final Vector b;
        private final int maxIteration; // guaranteed to converge in n iterations
        private Vector x; // initial guess
        private Vector r;// residual
        private Vector rHat;
        private double beta;
        private Vector v1;
        private Vector w1;
        private Vector v0;
        private Vector w0;
        private Vector pkm3;
        private Vector pkm2;
        private double beta0 = 0.;
        private double gamma0 = 0.;
        private double T01 = 0.;
        private double eta1 = 1.;
        private GivensMatrix G1 = GivensMatrix.Ctor2x2(1., 0.);
        private GivensMatrix G0 = GivensMatrix.Ctor2x2(1., 0.);
        private boolean isConverged;
        private int count = 0;

        private Solution1(LSProblem problem, IterationMonitor<Vector> monitor) {
            this.monitor = monitor;
            this.A = problem.A();
            this.Ah = A.t();
            this.b = problem.b();
            this.maxIteration = Math.min(maxIteration0, A.nCols());
        }

        @Override
        public void setInitials(Vector... initials) {
            x = initials[0];
            r = b.minus(A.multiply(x));
            rHat = r.deepCopy();
            beta = r.norm();
            v1 = r.scaled(1. / beta);
            w1 = rHat.scaled(1. / rHat.innerProduct(v1));
            v0 = v1.ZERO();
            w0 = w1.ZERO();
            pkm3 = w1.ZERO();
            pkm2 = w1.ZERO();
            isConverged = tolerance.isResidualSmall(r.norm());
        }

        @Override
        public IterationMonitor<Vector> step() throws ConvergenceFailure {
            monitor.addIterate(x);

            // two-sided Lanczos algorithm
            Vector Av = A.multiply(v1);
            Vector Ahw = Ah.multiply(w1);
            double alpha1 = Av.innerProduct(w1);
            Vector vTilde2 = Av.minus(v1.scaled(alpha1)).minus(v0.scaled(beta0));
            double alpha1Conjugate = alpha1; // TODO: should be complex conjugate of alpha1
            Vector wTilde2 = Ahw.minus(w1.scaled(alpha1Conjugate)).minus(w0.scaled(gamma0));
            double gamma1 = vTilde2.norm();
            Vector v2 = vTilde2.scaled(1. / gamma1);
            double beta1 = v2.innerProduct(wTilde2);
            Vector w2 = wTilde2.scaled(1. / beta1);

            double T11 = alpha1; // T(k, k)
            double T12 = beta1; // T(k, k + 1)
            double T21 = gamma1; // T(k + 1, k)

            // apply previous Givens rotations to the last (k-th) column of T
            Vector t0 = new DenseVector(new double[]{0., T01});
            t0 = G0.multiply(t0); // [T(k - 2, k) T(k - 1, k)]
            Vector t1 = new DenseVector(new double[]{t0.get(2), T11});
            t1 = G1.multiply(t1); // [T(k - 1, k) T(k, k)]

            // compute the Givens rotation to annihilate the T(k + 1, k)
            GivensMatrix G2 = GivensMatrix.CtorToRotateRows(2, 1, 2, t1.get(2), T21);

            // apply the new rotation to eta, i.e., the right-hand side
            Vector s = new DenseVector(new double[]{eta1, 0.});
            s = G2.multiply(s);
            double eta2 = s.get(2);

            // apply the new rotation to the last column of T
            Vector t2 = new DenseVector(new double[]{t1.get(2), T21});
            t2 = G2.multiply(t2); // [T(k, k) T(k + 1, k)]

            Vector pkm1 = v1.minus(pkm2.scaled(t1.get(1))).
                    minus(pkm3.scaled(t0.get(1))).
                    scaled(1. / t2.get(1));

            double a = beta * s.get(1);
            x = x.add(pkm1.scaled(a));
            r = b.minus(A.multiply(x)); // residual

            // shift variables
            T01 = T12;

            pkm3 = pkm2;
            pkm2 = pkm1;

            v0 = v1;
            v1 = v2;

            w0 = w1;
            w1 = w2;

            beta0 = beta1;
            gamma0 = gamma1;

            G0 = G1;
            G1 = G2;

            eta1 = eta2;

            return monitor;
        }

        @Override
        public Vector search(Vector... initials) throws ConvergenceFailure {
            setInitials(initials);

            for (; count < maxIteration && !isConverged;
                    ++count, isConverged |= tolerance.isResidualSmall(r.norm())) {
                step();
            }

            monitor.addIterate(x);

            if (!isConverged) {
                throw new ConvergenceFailure(Reason.MAX_ITERATIONS_EXCEEDED, maxIteration + " iterations exceeded");
            }

            return x;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Solution2">    
    /*
     * This implementation is due to
     * "Templates for the Solution of Linear Systems - Building Blocks for Iterative Methods", p.22
     *
     * The algorithm includes preconditioning, however, is less modular and less understandable. (no GivensMatrix involved)
     * Also, it only solves for real coefficient matrices.
     */
    private class Solution2 implements IterativeLinearSystemSolver.Solution {

        private final IterationMonitor<Vector> monitor;
        private final Matrix A;
        private final Matrix At;
        private final Vector b;
        private final Preconditioner M1;
        private final Preconditioner M2;
        private final int maxIteration;
        private Vector x; // initial guess
        private Vector r; // residual
        private Vector vTilde;
        private Vector y;
        private double rho;
        private Vector wTilde;
        private Vector z;
        private double xi;
        private Vector p;
        private Vector q;
        private Vector d;
        private Vector s;
        private double gamma = 1.;
        private double eta = -1.;
        private double epsilon = 1.;
        private double theta = 0.;
        private boolean isConverged;
        private int count = 0;

        private Solution2(LSProblem problem, IterationMonitor<Vector> monitor) {
            this.monitor = monitor;
            A = problem.A();
            At = A.t();
            b = problem.b();
            this.M1 = leftPreconditionerFactory.newInstance(A);
            this.M2 = rightPreconditionerFactory.newInstance(A);
            maxIteration = Math.min(maxIteration0, A.nCols());
        }

        @Override
        public void setInitials(Vector... initials) {
            x = initials[0];
            r = b.minus(A.multiply(x));
            vTilde = r.deepCopy();
            y = M1.solve(vTilde);
            rho = y.norm();
            wTilde = r.deepCopy();
            z = M2.transposeSolve(wTilde);
            xi = z.norm();
            p = r.ZERO();
            q = r.ZERO();
            d = x.ZERO();
            s = r.ZERO();
            isConverged = tolerance.isResidualSmall(r.norm());
        }

        @Override
        public IterationMonitor<Vector> step() throws ConvergenceFailure {
            monitor.addIterate(x);

            // two-sided Lanczos algorithm
            if (Double.compare(rho, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "rho = 0");
            }
            if (Double.compare(xi, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "xi = 0");
            }
            Vector v = vTilde.scaled(1. / rho);
            y = y.scaled(1. / rho);
            Vector w = wTilde.scaled(1. / xi);
            z = z.scaled(1. / xi);

            double delta = z.innerProduct(y);
            if (Double.compare(delta, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "delta = 0");
            }

            Vector yTilde = M2.solve(y);
            Vector zTilde = M1.transposeSolve(z);

            p = yTilde.minus(p.scaled(xi * delta / epsilon));
            q = zTilde.minus(q.scaled(rho * delta / epsilon));

            Vector pTilde = A.multiply(p);
            epsilon = q.innerProduct(pTilde);
            if (Double.compare(epsilon, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "epsilon = 0");
            }

            double beta = epsilon / delta;
            if (Double.compare(beta, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "beta = 0");
            }

            vTilde = pTilde.minus(v.scaled(beta));
            y = M1.solve(vTilde);
            double rho1 = y.norm();

            wTilde = At.multiply(q).minus(w.scaled(beta));
            z = M2.transposeSolve(wTilde);
            xi = z.norm();

            // compute Givens rotation to zero out T(k + 1, k)
            double theta1 = rho1 / (gamma * abs(beta));
            double gamma1 = 1. / sqrt(1 + theta1 * theta1);
            if (Double.compare(gamma1, 0.) == 0) {
                throw new ConvergenceFailure(Reason.BREAKDOWN, "gamma = 0");
            }

            eta = -eta * rho * gamma1 * gamma1 / (beta * gamma * gamma);

            double thetagammasquared = theta * gamma1;
            thetagammasquared *= thetagammasquared;
            d = p.scaled(eta).add(d.scaled(thetagammasquared));
            s = pTilde.scaled(eta).add(s.scaled(thetagammasquared));

            // compute x and r
            x = x.add(d);
            if ((count + 1) % residualRefreshRate != 0) {
                r = r.minus(s);
            } else {
                r = b.minus(A.multiply(x));
            }

            // shift variables
            rho = rho1;
            theta = theta1;
            gamma = gamma1;

            return monitor;
        }

        @Override
        public Vector search(Vector... initials) throws ConvergenceFailure {
            setInitials(initials);

            for (; count < maxIteration && !isConverged;
                    ++count, isConverged |= tolerance.isResidualSmall(r.norm())) {
                step();
            }

            monitor.addIterate(x);

            if (!isConverged) {
                throw new ConvergenceFailure(Reason.MAX_ITERATIONS_EXCEEDED, maxIteration + " iterations exceeded");
            }

            return x;
        }
    }
    //</editor-fold>
}
