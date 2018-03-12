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

/**
 * The Minimal Residual method (MINRES) is useful for solving a symmetric n-by-n
 * linear system (possibly indefinite or singular).
 * When the coefficient matrix <i>A</i> is Hermitian, the Arnoldi algorithm used in
 * {@linkplain GeneralizedMinimalResidualSolver GMRES} can be simplified to a
 * 3-term recurrence known as Lanczos algorithm. Thus, an approximate solution
 * can be computed without saving all the orthonormal basis vectors generated.
 * When <i>A</i> is singular, MINRES returns a least-squares solution with small
 * <i>|Ar|</i> (where <i>r = b - Ax</i>).
 *
 * <p>
 * Only left preconditioning is supported in this implementation.
 *
 * @author Ken Yiu
 * @see "Anne Greenbaum, "Algorithm 4P," Iterative methods for solving linear systems, ch.8, pp.122."
 */
public class MinimalResidualSolver implements IterativeLinearSystemSolver {

    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a MINRES solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public MinimalResidualSolver(PreconditionerFactory leftPreconditionerFactory, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a MINRES solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public MinimalResidualSolver(int maxIteration, Tolerance tolerance) {
        this(
                new PreconditionerFactory() {

                    @Override
                    public Preconditioner newInstance(Matrix A) {
                        return new IdentityPreconditioner();
                    }
                },
                maxIteration, tolerance);
    }

    public IterativeLinearSystemSolver.Solution solve(LSProblem problem) throws ConvergenceFailure {
        return solve(problem, new NullMonitor<Vector>());
    }

    @Override
    public IterativeLinearSystemSolver.Solution solve(final LSProblem problem, final IterationMonitor<Vector> monitor) throws ConvergenceFailure {
        return new IterativeLinearSystemSolver.Solution() {

            private final Matrix A = problem.A();
            private final Vector b = problem.b();
            private final int maxIteration = Math.min(maxIteration0, A.nCols()); // guaranteed to converge in n iterations
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private Vector x; // initial guess
            private Vector r; // residual
            private Vector v1;
            private Vector z1; // preconditioning
            private double beta0;
            private double beta;
            private double ibeta;
            private Vector q1;
            private Vector q0;
            private Vector w1;
            private Vector pkm3;
            private Vector pkm2;
            private double T01 = 0.;
            private GivensMatrix G1 = GivensMatrix.Ctor2x2(1., 0.);
            private GivensMatrix G0 = GivensMatrix.Ctor2x2(1., 0.);
            private double eta1 = 1.;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x)); // residual
                v1 = r.deepCopy();
                z1 = M.solve(r); // preconditioning
                beta0 = Math.sqrt(r.innerProduct(z1));
                beta = beta0;

                if (Double.compare(beta, 0.) == 0) {
                    isConverged = true;
                    return;
//                    monitor.addIterate(x);
//                    return x; // initial guess converges
                }
                ibeta = 1. / beta0;
                q1 = r.scaled(ibeta);
                q0 = q1.ZERO();
                w1 = z1.scaled(ibeta);
                pkm3 = w1.ZERO();
                pkm2 = w1.ZERO();
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                // preconditioned Lanczos algorithm
                v1 = A.multiply(w1).minus(q0.scaled(beta0));
                double alpha1 = v1.innerProduct(w1);
                v1 = v1.minus(q1.scaled(alpha1));
                Vector z2 = M.solve(v1); // preconditioning

                double beta1 = Math.sqrt(v1.innerProduct(z2));
                if (Double.compare(beta1, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<v, z> = 0");
                }

                ibeta = 1. / beta1;
                Vector q2 = v1.scaled(ibeta);
                Vector w2 = z2.scaled(ibeta);

                double T11 = alpha1; // T(k, k)
                double T21 = beta1; // T(k + 1, k)
                double T12 = beta1; // T(k, k + 1)

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

                Vector pkm1 = w1.minus(pkm2.scaled(t1.get(1))).
                        minus(pkm3.scaled(t0.get(1))).
                        scaled(1. / t2.get(1));

                double a = beta * s.get(1);
                x = x.add(pkm1.scaled(a));
                r = b.minus(A.multiply(x)); // residual

                // shift variables
                w1 = w2;

                T01 = T12;

                pkm3 = pkm2;
                pkm2 = pkm1;

                q0 = q1;
                q1 = q2;

                beta0 = beta1;

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
        };
    }
}
