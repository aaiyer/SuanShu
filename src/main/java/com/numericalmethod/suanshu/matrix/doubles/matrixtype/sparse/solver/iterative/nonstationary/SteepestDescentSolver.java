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
 * The Steepest Descent method (SDM) solves a symmetric n-by-n linear system.
 * The convergence is guaranteed if <i>A</i> is symmetric positive definite.
 *
 * <p>
 * Only left preconditioning is supported in this implementation.
 * The preconditioner must be symmetric and positive definite.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Gradient_descent">Wikipedia: Gradient descent</a>
 */
public class SteepestDescentSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration;
    private final Tolerance tolerance;

    /**
     * Construct a Steepest Descent method (SDM) solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public SteepestDescentSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Steepest Descent method (SDM) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public SteepestDescentSolver(int maxIteration, Tolerance tolerance) {
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
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
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
                monitor.addIterate(x);

                Vector z = M.solve(r); // preconditioning
                Vector q = A.multiply(z);
                double alpha = r.innerProduct(z) / q.innerProduct(z);

                x = x.add(z.scaled(alpha));
                if ((count + 1) % residualRefreshRate != 0) {
                    r = r.minus(q.scaled(alpha));
                } else {
                    r = b.minus(A.multiply(x));
                }

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
