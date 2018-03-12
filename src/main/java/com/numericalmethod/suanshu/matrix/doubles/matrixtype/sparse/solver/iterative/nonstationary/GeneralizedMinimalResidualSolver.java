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
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.BackwardSubstitution;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.GivensMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure.Reason;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.IterativeLinearSystemSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.IdentityPreconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.Preconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.PreconditionerFactory;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.SubMatrixRef;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.subVector;
import java.util.Arrays;

/**
 * The Generalized Minimal Residual method (GMRES) is useful for solving
 * a non-symmetric n-by-n linear system. It computes a sequence of orthogonal
 * vectors (like {@linkplain MinimalResidualSolver MINRES}), and combines these
 * through a least-squares solve and update. However, unlike MINRES (and
 * {@linkplain ConjugateGradientSolver CG}) it
 * requires storing the whole sequence, so that a large amount of storage is
 * needed. For this reason, a restarted version of this method is often used. In
 * a restarted version, computation and storage costs are limited by specifying a
 * fixed number of vectors to be generated.
 * <p/>
 * This implementation is a restarted version.
 * In addition, it uses a Givens rotation to transform the
 * intermediate Hessenberg matrix into an upper triangular matrix,
 * such that convergence can be determined as soon as the residual norm at each
 * sub-step is small enough.
 * Only left preconditioning is supported.
 *
 * @author Ken Yiu
 * @see "Yousef Saad, "GMRES," in <i>Iterative Methods for Sparse Linear Systems</i>, 2nd ed. 2000, ch. 6, sec. 6.5, p. 157-172."
 */
public class GeneralizedMinimalResidualSolver implements IterativeLinearSystemSolver {

    /** restart parameter of GMRES */
    private final int m0;
    private final PreconditionerFactory leftPreconditionerFactory;
    private final int maxIteration0;
    private final Tolerance tolerance;

    /**
     * Construct a GMRES solver with restarts.
     *
     * @param leftPreconditionerFactory constructs a new left preconditioner
     * @param m                         the solver restarts every {@code m} iterations;
     * Practically, as {@code m} increases, the computational
     * cost increases at least by <i>O(m<sup>2</sup>)n</i> because of the
     * Gram-Schmidt orthogonalization. The memory cost increases by <i>O(mn)</i>.
     * @param maxIteration              the maximum number of iterations
     * @param tolerance                 the convergence threshold
     */
    public GeneralizedMinimalResidualSolver(PreconditionerFactory leftPreconditionerFactory, int m, int maxIteration, Tolerance tolerance) {
        this.leftPreconditionerFactory = leftPreconditionerFactory;
        this.m0 = m;
        this.maxIteration0 = maxIteration;
        this.tolerance = tolerance;
    }

    /**
     * Construct a GMRES solver with restarts.
     *
     * @param m            the solver restarts every {@code m} iterations;
     * Practically, as {@code m} increases, the computational
     * cost increases at least by <i>O(m<sup>2</sup>)n</i> because of the
     * Gram-Schmidt orthogonalization. The memory cost increases by <i>O(mn)</i>.
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public GeneralizedMinimalResidualSolver(int m, int maxIteration, Tolerance tolerance) {
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
     * Construct a full GMRES solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public GeneralizedMinimalResidualSolver(int maxIteration, Tolerance tolerance) {
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
                                             ? Math.min(maxIteration0, problem.A().nCols()) // full: guaranteed to converge in n iterations
                                             : maxIteration0; // restarted
            private final Preconditioner M = leftPreconditionerFactory.newInstance(A);
            private final int m = Math.min(m0, A.nCols()); // use restart or full version
            private Vector[] V = new Vector[m + 1]; // basis from Krylov subspace {v, A*v, A^2*v, ..., A^m*v}
            private Matrix H = new DenseMatrix(m + 1, m).ZERO(); // upper Hessenberg matrix
            private GivensMatrix[] Gs = new GivensMatrix[m]; // for Givens rotations
            private Vector x; // initial guess
            private Vector r; // residual
            private double rNorm; // residual norm
            private boolean isConverged;
            private int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x));
                r = M.solve(r); // preconditioning
                rNorm = r.norm();
                isConverged = tolerance.isResidualSmall(rNorm);
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                V[0] = r.scaled(1. / rNorm); // normalize residual as the first basis in V
                Vector s = new DenseVector(m + 1, 0.);
                s.set(1, rNorm);

                int i = 1;
                for (; i <= m && count < maxIteration && !isConverged;
                        ++i, ++count, isConverged = tolerance.isResidualSmall(rNorm)) { // inner iterations; restart every m iterations

                    monitor.addIterate(x); // Note: GMRES does not compute intermediate iterates

                    Vector w = A.multiply(V[i - 1]);
                    w = M.solve(w); // preconditioning

                    Vector Hi = new DenseVector(m + 1, 0.); // construct the i-th column of H
                    for (int k = 1; k <= i; ++k) { // Arnoldi's iteration
                        double Hki = w.innerProduct(V[k - 1]);
                        Hi.set(k, Hki);
                        w = w.minus(V[k - 1].scaled(Hki));
                    }

                    double wNorm = w.norm();
                    Hi.set(i + 1, wNorm);
                    if (Double.compare(wNorm, 0.) != 0) {
                        V[i] = w.scaled(1. / wNorm); // normalize the new basis in V
                    }

                    // apply previous Givens rotations to the new column
                    for (int k = 1; k <= i - 1; ++k) {
                        Hi = Gs[k - 1].multiply(Hi);
                    }

                    // generate the i-th rotation
                    Gs[i - 1] = GivensMatrix.CtorToRotateRows(m + 1, i, i + 1, Hi.get(i), Hi.get(i + 1));

                    // apply the rotation to the right-hand side s
                    s = Gs[i - 1].multiply(s);

                    // apply the rotation to the new column of the Hessenberg matrix H
                    Hi = Gs[i - 1].multiply(Hi); // zero out H(i + 1, i) after rotation

                    for (int k = 1; k <= i; ++k) { // assign the new rotated column to H
                        H.set(k, i, Hi.get(k));
                    }

                    rNorm = Math.abs(s.get(i + 1)); // s(i + 1) is the residual norm

                }

                i--; // undo the last increment in the for-loop
                Vector sTrunc = subVector(s, 1, i); // truncate s as the right-hand side
                UpperTriangularMatrix utmH = new UpperTriangularMatrix(new SubMatrixRef(H, 1, i, 1, i)); // truncate H as a triangular system

                // solve for the minimizer y (the linear combination of the basis to approximate x)
                Vector y = new BackwardSubstitution().solve(utmH, sTrunc);
                x = x.add(CreateMatrix.cbind(Arrays.copyOf(V, i)).multiply(y)); // xm = x0 + Vy

                // compute residual
                r = b.minus(A.multiply(x));
                r = M.solve(r); // preconditioning

                // residual norm
                rNorm = r.norm();

                return monitor;
            }

            @Override
            public Vector search(Vector... initials) throws ConvergenceFailure {
                setInitials(initials);
                for (; count < maxIteration && !isConverged;
                        isConverged |= tolerance.isResidualSmall(rNorm)) {
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
