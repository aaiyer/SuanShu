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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary;

import com.numericalmethod.suanshu.algorithm.iterative.monitor.IterationMonitor;
import com.numericalmethod.suanshu.algorithm.iterative.monitor.NullMonitor;
import com.numericalmethod.suanshu.algorithm.iterative.tolerance.Tolerance;
import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.ConvergenceFailure.Reason;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.IterativeLinearSystemSolver;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The Jacobi method solves sequentially <i>n</i> equations in a linear
 * system <i>Ax = b</i> in isolation in each iteration.
 * For the <i>i</i>-th equation,
 * it solves for the value <i>x<sub>i</sub></i> while assuming the other
 * <i>x</i>'s remain fixed. The convergence is slow.
 * <p/>
 * This implementation does not support preconditioning.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Jacobi_method">Wikipedia: Jacobi method</a>
 */
public class JacobiSolver implements IterativeLinearSystemSolver {

    private final int maxIteration;
    private final Tolerance tolerance;

    /**
     * Construct a Jacobi solver.
     *
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     */
    public JacobiSolver(int maxIteration, Tolerance tolerance) {
        this.maxIteration = maxIteration;
        this.tolerance = tolerance;
    }

    public IterativeLinearSystemSolver.Solution solve(LSProblem problem) throws ConvergenceFailure {
        return solve(problem, new NullMonitor<Vector>());
    }

    @Override
    public IterativeLinearSystemSolver.Solution solve(final LSProblem problem, final IterationMonitor<Vector> monitor) throws ConvergenceFailure {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(problem.A()), "the matrix A must be square");
        checkMatrix(problem.A());

        return new IterativeLinearSystemSolver.Solution() {

            private final Matrix A = problem.A();
            private final Vector b = problem.b();
            final int n = problem.size();
            Vector x; // initial guess
            Vector r;// residual
            boolean isConverged;
            int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x)); // residual
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                Vector xNext = new DenseVector(n).ZERO();

                // solve each equation independently
                for (int i = 1; i <= n; ++i) {

                    double xi = 0.;
                    for (int j = 1; j <= n; ++j) {
                        if (j != i) {
                            xi += A.get(i, j) * x.get(j);
                        }
                    }

                    xi = (b.get(i) - xi) / A.get(i, i);

                    xNext.set(i, xi);
                }

                x = xNext;
                r = b.minus(A.multiply(x));

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
                    throw new ConvergenceFailure(Reason.MAX_ITERATIONS_EXCEEDED, maxIteration + " exceeded");
                }

                return x;
            }
        };
    }

    private static void checkMatrix(Matrix A) {
        for (int i = 1; i <= A.nCols(); ++i) {
            if (Double.compare(A.get(i, i), 0.) == 0) {
                throw new IllegalArgumentException("diagonal entries must be non-zero");
            }
        }
    }
}
