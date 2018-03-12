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
 * The Biconjugate Gradient Stabilized (BiCGSTAB) method is useful for solving
 * non-symmetric n-by-n linear systems.
 * Like {@linkplain ConjugateGradientSquaredSolver CGS}, this algorithm is a
 * transpose-free variant of
 * {@linkplain BiconjugateGradientSolver BiCG}, but uses a different
 * update for the <i>A<sup>t</sup></i>-sequence to obtain a smoother
 * convergence than CGS does.
 * <p/>
 * Only left preconditioning is supported in this implementation.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "BICGSTAB," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 7, sec. 7.4.2, p. 216-219."
 */
public class BiconjugateGradientStabilizedSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a Biconjugate Gradient Stabilized solver (BiCGSTAB) .
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public BiconjugateGradientStabilizedSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Biconjugate Gradient Stabilized solver (BiCGSTAB) .
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public BiconjugateGradientStabilizedSolver(int maxIteration, Tolerance tolerance) {
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
            private Vector x;
            private Vector r; // residual
            private Vector ri;
            private Vector p;
            private Vector v;
            private double rho0 = 1.;
            private double alpha = 1.;
            private double omega = 1.;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];

                r = b.minus(A.multiply(x)); // residual
                ri = r.deepCopy();

                p = x.ZERO();
                v = x.ZERO();

                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                if (Double.compare(omega, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "omega = 0");
                }

                double rho = ri.innerProduct(r);

                if (Double.compare(rho, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<rTilde, r> = 0");
                }

                double beta = (rho / rho0) * (alpha / omega);
                p = r.add(p.minus(v.scaled(omega)).scaled(beta));

                Vector pHat = M.solve(p); // preconditioning
                v = A.multiply(pHat);

                double sigma = ri.innerProduct(v);
                if (Double.compare(sigma, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<rTilde, v> = 0");
                }

                alpha = rho / sigma;
                Vector s = r.minus(v.scaled(alpha));
                // check norm of s for convergence
                isConverged = tolerance.isResidualSmall(s.norm());
                if (isConverged) {
                    // update x and stop
                    x = x.add(pHat.scaled(alpha));
                    return monitor;
                }

                Vector sHat = M.solve(s); // preconditioning
                Vector t = A.multiply(sHat);

                omega = t.innerProduct(s) / t.innerProduct(t);

                x = x.add(pHat.scaled(alpha)).add(sHat.scaled(omega));

                if ((count + 1) % residualRefreshRate != 0) {
                    r = s.minus(t.scaled(omega));
                } else {
                    r = b.minus(A.multiply(x));
                }

                rho0 = rho;

                return monitor;
            }

            @Override
            public Vector search(Vector... initials) throws ConvergenceFailure {
                setInitials(initials);

                for (; count < maxIteration && !isConverged;
                        ++count, isConverged |= tolerance.isResidualSmall(r.norm())) {//it is important to use | (OR) otherwise "isConverged" may flip back to false
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
