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
 * The Conjugate Gradient method (CG) is useful for solving a symmetric n-by-n
 * linear system. The method derives its name from the fact that it
 * generates a sequence of conjugate (or orthogonal) vectors. These vectors are
 * the residuals of the iterates. They are also the gradients of a quadratic
 * function, the minimization of which is equivalent to solving the linear
 * system. CG is an extremely effective method when the coefficient matrix is
 * symmetric positive definite as storage for only a limited number of
 * vectors is required. For a coefficient matrix that is not
 * symmetric, not positive-definite, and even not square, there are solvers using the CG method.
 * For example, {@linkplain ConjugateGradientNormalResidualSolver CGNR} solves an over-determined system;
 * {@linkplain ConjugateGradientNormalErrorSolver CGNE} solves an under-determined system.
 * <p/>
 * If <i>A</i> is symmetric, positive-definite and square, the CG method solves
 * <blockquote><i>
 * Ax = b
 * </i></blockquote>
 * Note that if the coefficient matrix <i>A</i> passed into the algorithm is not
 * symmetric positive-definite, the algorithm behaves unexpectedly.
 * <p/>
 * Only left preconditioning is supported in this implementation.
 * The preconditioner must be symmetric and positive definite.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "The Conjugate Gradient Algorithm," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 6, sec. 6.7, p. 174-181."
 */
public class ConjugateGradientSolver implements IterativeLinearSystemSolver {

    /**
     * The algorithm recomputes the residual as <i>b - Ax<sub>i</sub></i> once per this number of iterations
     */
    public static final int DEFAULT_RESIDUAL_REFRESH_RATE = 50;
    private final int residualRefreshRate;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a Conjugate Gradient (CG) solver.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param residualRefreshRate       the number of iterations before the next refresh
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public ConjugateGradientSolver(PreconditionerFactory leftPreconditionerFactory, int residualRefreshRate, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.residualRefreshRate = residualRefreshRate;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a Conjugate Gradient (CG) solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public ConjugateGradientSolver(int maxIteration, Tolerance tolerance) {
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
            private Vector p; // search direction
            private double rtz0 = 1.;
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x));
                p = x.ZERO();
                isConverged = tolerance.isResidualSmall(r.norm());

            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                Vector z = M.solve(r); // preconditioning
                double rtz1 = r.innerProduct(z);
                if (Double.compare(rtz1, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<r, z> = 0");
                }

                double beta = rtz1 / rtz0;
                p = z.add(p.scaled(beta));

                Vector q = A.multiply(p);

                double ptq = p.innerProduct(q);
                if (Double.compare(ptq, 0.) == 0) {
                    throw new ConvergenceFailure(Reason.BREAKDOWN, "<p, q> = 0");
                }
                double alpha = rtz1 / ptq;

                x = x.add(p.scaled(alpha));

                if ((count + 1) % residualRefreshRate != 0) {
                    r = r.minus(q.scaled(alpha));
                } else {
                    r = b.minus(A.multiply(x));
                }

                rtz0 = rtz1;

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
