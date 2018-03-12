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

/**
 * The Symmetric Successive Overrelaxation method (SSOR) is like
 * {@linkplain SuccessiveOverrelaxationSolver SOR}, but it performs in each
 * iteration one forward sweep followed by one backward sweep.
 * With an optimal value of <i>&omega;</i>, the convergence rate of SSOR is
 * usually slower than that of SOR with an optimal <i>&omega;</i>.
 * <p/>
 * This implementation does not support preconditioning.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Successive_over-relaxation_method">Wikipedia: Successive over-relaxation method</a>
 */
public class SymmetricSuccessiveOverrelaxationSolver implements IterativeLinearSystemSolver {

    /** the extrapolation factor, i.e., weight for weighted averaging */
    private final double omega;
    private final int maxIteration;
    private final Tolerance tolerance;

    /**
     * Construct a SSOR solver with the extrapolation factor <i>&omega;</i>.
     *
     * @param omega        the extrapolation factor
     * @param maxIteration the maximum number of iterations
     * @param tolerance    the convergence threshold
     * @see SuccessiveOverrelaxationSolver#SuccessiveOverrelaxationSolver(double)
     */
    public SymmetricSuccessiveOverrelaxationSolver(double omega, int maxIteration, Tolerance tolerance) {
        this.omega = omega;
        this.maxIteration = maxIteration;
        this.tolerance = tolerance;
    }

    public IterativeLinearSystemSolver.Solution solve(LSProblem problem) throws ConvergenceFailure {
        return solve(problem, new NullMonitor<Vector>());
    }

    @Override
    public IterativeLinearSystemSolver.Solution solve(final LSProblem problem, final IterationMonitor<Vector> monitor) throws ConvergenceFailure {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(problem.A()), "A must be a square matrix");

        return new IterativeLinearSystemSolver.Solution() {

            private final Matrix A = problem.A();
            private final Vector b = problem.b();
            private final SORSweep sweep = new SORSweep(A, b, omega);
            private Vector x; // initial guess
            private Vector r; // residual
            private boolean isConverged;
            int count = 0;

            @Override
            public void setInitials(Vector... initials) {
                x = initials[0];
                r = b.minus(A.multiply(x));
                isConverged = tolerance.isResidualSmall(r.norm());
            }

            @Override
            public IterationMonitor<Vector> step() throws ConvergenceFailure {
                monitor.addIterate(x);

                x = sweep.forward(x);
                x = sweep.backward(x);
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
}
