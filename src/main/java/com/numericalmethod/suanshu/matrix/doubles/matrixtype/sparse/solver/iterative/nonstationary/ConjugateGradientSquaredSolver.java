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
 * The Conjugate Gradient Squared method (CGS) is useful for solving
 * a non-symmetric n-by-n linear system. This method is a variant of
 * {@linkplain BiconjugateGradientSolver BiCG} that applies the updating
 * operations for the <i>A</i>-sequence and the <i>A<sup>t</sup></i>-sequence
 * both to the same vectors. Ideally, this would double the convergence rate,
 * but in practice convergence may be much more irregular than for BiCG.
 * This may sometimes lead to unreliable results.
 *
 * A practical advantage is that the CGS method does not need the multiplications
 * with the transpose of the coefficient matrix. In some applications of CG
 * methods, <i>A</i> is available only through some approximations but not
 * explicitly. In such situations, the transpose of <i>A</i>, i.e.,
 * <i>A<sup>t</sup></i>, is usually not available.
 *
 * In addition, the rounding errors in the CGS method tend to be more damaging than in the
 * standard BiCG algorithm.
 *
 * <p/>
 * Only left preconditioning is supported in this implementation.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, “Conjugate Gradient Squared,” in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 7, sec. 7.4.1, p. 215-216."
 */
public class ConjugateGradientSquaredSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a Conjugate Gradient Squared (CGS) solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public ConjugateGradientSquaredSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Conjugate Gradient Squared (CGS) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public ConjugateGradientSquaredSolver(int maxIteration, Tolerance tolerance) {
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
            private final int maxIteration = Math.min(maxIteration0, A.nCols()); // guaranteed to converge in n iterations
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private Vector x; // initial guess
            private Vector r; // residual
            private Vector rTilde;
            private Vector p;
            private Vector q;
            private double rtrTilde0;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x));
                rTilde = r.deepCopy();
                p = x.ZERO();
                q = x.ZERO();
                rtrTilde0 = r.innerProduct(rTilde);
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                double rtrTilde1 = r.innerProduct(rTilde);
                if (Double.compare(rtrTilde1, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<r, rTilde> = 0");
                }
                double beta = rtrTilde1 / rtrTilde0;

                Vector u = r.add(q.scaled(beta));
                p = u.add(q.add(p.scaled(beta)).scaled(beta));

                Vector pHat = M.solve(p); // preconditioning
                Vector vHat = A.multiply(pHat);

                double rtv = vHat.innerProduct(rTilde);
                if (Double.compare(rtv, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<vHat, rTilde> = 0");
                }
                double alpha = rtrTilde1 / rtv;
                q = u.minus(vHat.scaled(alpha));

                Vector uq = u.add(q);
                Vector uHat = M.solve(uq); // preconditioning

                x = x.add(uHat.scaled(alpha));

                Vector qHat = A.multiply(uHat);

                if ((count + 1) % residualRefreshRate != 0) {
                    r = r.minus(qHat.scaled(alpha));
                } else {
                    r = b.minus(A.multiply(x));
                }

                rtrTilde0 = rtrTilde1;

                return monitor;
            }

            @Override
            public Vector search(Vector... initials) throws ConvergenceFailure {
                for (; count < maxIteration && !isConverged;
                        ++count, isConverged = tolerance.isResidualSmall(r.norm())) {
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
