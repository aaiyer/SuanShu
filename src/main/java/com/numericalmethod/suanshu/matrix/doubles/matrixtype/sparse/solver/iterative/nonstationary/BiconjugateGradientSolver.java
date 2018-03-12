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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure.Reason;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.IterativeLinearSystemSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.IdentityPreconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.Preconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.PreconditionerFactory;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Biconjugate Gradient method (BiCG) is useful for solving non-symmetric
 * n-by-n linear systems. It generates two CG-like sequences of vectors, one
 * based on a system with the original coefficient matrix <i>A</i>, and one on
 * <i>A<sup>t</sup></i>. They are made
 * mutually orthogonal, or "bi-orthogonal". This method is useful when the matrix is non-symmetric and nonsingular.
 * However, convergence may be irregular, and there is a possibility that the
 * method will break down. BiCG , like
 * {@linkplain ConjugateGradientSolver CG}, uses limited
 * storage. It requires a multiplication with the coefficient
 * matrix and with its transpose at each iteration.
 * <p/>
 * Only left preconditioning is supported in this implementation.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "The Biconjugate Gradient Algorithm," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 7, sec. 7.3.1, p. 210-211."
 */
public class BiconjugateGradientSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once
     * per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration;
    private final Tolerance tolerance;

    /**
     * Construct a Biconjugate Gradient (BiCG) solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public BiconjugateGradientSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Biconjugate Gradient (BiCG) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public BiconjugateGradientSolver(int maxIteration, Tolerance tolerance) {
        this(
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
        return new IterativeLinearSystemSolver.Solution() {

            private final Matrix A = problem.A();
            private final Vector b = problem.b();
            private final Matrix At = A.t();
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private double rho0 = 1.;
            private Vector x;
            private Vector r; // residual
            private Vector rc; // residual conjugate
            private Vector p;
            private Vector pc;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];// initial guess
                r = b.minus(A.multiply(x)); // residual
                rc = r.deepCopy(); // residual conjugate
                p = x.ZERO();
                pc = x.ZERO();
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                Vector z = M.solve(r);
                Vector zc = M.transposeSolve(rc);
                double rho1 = rc.innerProduct(z);

                if (Double.compare(rho1, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<z, rTilde> = 0");
                }

                double beta = rho1 / rho0;
                p = z.add(p.scaled(beta));
                pc = zc.add(pc.scaled(beta));

                Vector q = A.multiply(p);
                Vector qc = At.multiply(pc);

                double qtpc = q.innerProduct(pc);

                if (Double.compare(qtpc, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<Ap, pTilde> = 0");
                }

                double alpha = rho1 / qtpc;

                x = x.add(p.scaled(alpha));

                if ((count + 1) % residualRefreshRate != 0) {
                    r = r.minus(q.scaled(alpha));
                    rc = rc.minus(qc.scaled(alpha));
                } else {
                    r = b.minus(A.multiply(x));
                    rc = b.minus(At.multiply(x)); // TODO: r* = b* - t(A)x*
                }

                rho0 = rho1;

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
