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
 * The Generalized Conjugate Residual method (GCR) is useful for solving
 * a non-symmetric n-by-n linear system. GCR computes the next
 * Krylov basis vector as a linear combination of the current residual and all
 * previous basis vectors.
 * <p/>
 * This implementation is a restarted version of GCR.
 * Only left preconditioning is supported.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "GCR, ORTHOMIN, and ORTHODIR," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 6, sec. 6.9, p. 182-184."
 */
public class GeneralizedConjugateResidualSolver implements IterativeLinearSystemSolver {

    private final int m0;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a GCR solver with restarts.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param m                         the solver restarts every {@code m} iterations
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public GeneralizedConjugateResidualSolver(PreconditionerFactory leftPreconditionerFactory, int m, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.m0 = m;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a GCR solver with restarts.
     *
     * @param m            the solver restarts every {@code m} iterations
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public GeneralizedConjugateResidualSolver(int m, int maxIteration, Tolerance tolerance) {
        this(
                new PreconditionerFactory() {

                    @Override
                    public Preconditioner newInstance(Matrix A) {
                        return new IdentityPreconditioner();
                    }
                },
                m, maxIteration, tolerance);
    }

    /**
     * Construct a full GCR solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public GeneralizedConjugateResidualSolver(int maxIteration, Tolerance tolerance) {
        this(Integer.MAX_VALUE,//never restart
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
            private final int maxIteration = (m0 >= problem.A().nCols())
                                             ? Math.min(problem.getMaxIteration(), problem.A().nCols()) // full: guaranteed to converge in n iterations
                                             : maxIteration0; // restarted
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private final int m = Math.min(m0, A.nCols()); // use restart or full version
            private Vector x; // initial guess
            private Vector r; // residual
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x));
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                Vector z = M.solve(r); // preconditioning
                Vector[] p = new Vector[m + 1]; // basis from Krylov subspace {v, A*v, A^2*v, ..., A^m*v}
                Vector[] w = new Vector[m];

                double[] delta = new double[m]; // dot products: (Api, Api)
                p[0] = z.scaled(1. / z.norm()); // normalize residual

                int i = 1;
                for (; i <= m && count < maxIteration && !isConverged;
                        ++i, ++count, isConverged = tolerance.isResidualSmall(r.norm())) { // inner iterations; restart every m iterations

                    monitor.addIterate(x);

                    Vector u = A.multiply(p[i - 1]);
                    w[i - 1] = M.solve(u); // preconditioning

                    delta[i - 1] = w[i - 1].innerProduct(w[i - 1]);
                    if (Double.compare(delta[i - 1], 0.) == 0) {
                        throw new ConvergenceFailure(Reason.BREAKDOWN, "<w, w> = 0");
                    }

                    double alpha = r.innerProduct(w[i - 1]) / delta[i - 1]; // TODO: complex r should conjugated before dot product

                    x = x.add(p[i - 1].scaled(alpha));
                    r = r.minus(w[i - 1].scaled(alpha));

                    Vector Ar = A.multiply(r);
                    Vector q = M.solve(Ar); // preconditioning

                    // compute p[i]
                    p[i] = r;
                    for (int j = 0; j < i; ++j) {
                        double beta = -q.innerProduct(w[j]) / delta[j];
                        p[i] = p[i].add(p[j].scaled(beta));
                    }
                }

                // compute residual
                r = b.minus(A.multiply(x));

                return monitor;
            }

            @Override
            public Vector search(Vector... initials) throws ConvergenceFailure {
                setInitials(initials);

                for (; count < maxIteration && !isConverged;
                        isConverged |= tolerance.isResidualSmall(r.norm())) {
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
