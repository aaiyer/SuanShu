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
 * For an under-determined system of linear equations, <i>Ax = b</i>, or
 * when the coefficient matrix <i>A</i> is non-symmetric and nonsingular,
 * the normal equation matrix <i>AA<sup>t</sup></i> is symmetric and
 * positive definite, and hence CG is applicable.
 * Thus, the Conjugate Gradient Normal Error method (CGNE) applies
 * the {@linkplain ConjugateGradientSolver Conjugate Gradient method (CG)}
 * to the normal equation
 * <blockquote><i>
 * (AA<sup>t</sup>)y = b
 * </i></blockquote>
 * for <i>y</i>, and then computes the solution
 * <blockquote><i>
 * x = A<sup>t</sup>y
 * </i></blockquote>
 * The equivalent symmetric system has the form:
 * \[
 * \begin{bmatrix}
 * I & A\\
 * A' & 0
 * \end{bmatrix} \times
 * \begin{bmatrix}
 * r\\
 * x
 * \end{bmatrix} =
 * \begin{bmatrix}
 * b\\
 * 0
 * \end{bmatrix}
 * \]
 * with <i>r = b - Ax</i> arising from the standard necessary conditions
 * satisfied by the solution of the constrained optimization problem,
 * \[
 * \min \left \| r - b \right \|^2 \textup{ s.t., } A'r = 0
 * \]
 * The convergence may be slow
 * as the spectrum of <i>AA<sup>t</sup></i> will be less favorable than the
 * spectrum of <i>A</i>.
 * <p/>
 * Only left preconditioning is supported in this implementation.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "Preconditioned CG for the Normal Equations," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 9, sec. 9.5, p. 259-260."
 */
public class ConjugateGradientNormalErrorSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a Conjugate Gradient Normal Error (CGNE) solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public ConjugateGradientNormalErrorSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Conjugate Gradient Normal Error (CGNE) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public ConjugateGradientNormalErrorSolver(int maxIteration, Tolerance tolerance) {
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
            private final Matrix At = A.t();
            private final Vector b = problem.b();
            private final int maxIteration = Math.min(maxIteration0, A.nCols()); // guaranteed to converge in n iterations
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private Vector x; // initial guess
            private Vector r; // residual
            private Vector p;
            private double ztr0 = 1.;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x)); // residual
                p = x.ZERO();
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                Vector z = M.solve(r); // preconditioning

                double ztr1 = z.innerProduct(r);
                if (Double.compare(ztr1, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<z, r> = 0");
                }

                double beta = ztr1 / ztr0;
                p = At.multiply(z).add(p.scaled(beta));

                Vector w = A.multiply(p);

                double ptp = p.innerProduct(p);
                if (Double.compare(ptp, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<p, p> = 0");
                }
                double alpha = ztr1 / ptp;

                x = x.add(p.scaled(alpha));
                if ((count + 1) % residualRefreshRate != 0) {
                    r = r.minus(w.scaled(alpha));
                } else {
                    r = b.minus(A.multiply(x));
                }

                ztr0 = ztr1;

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
