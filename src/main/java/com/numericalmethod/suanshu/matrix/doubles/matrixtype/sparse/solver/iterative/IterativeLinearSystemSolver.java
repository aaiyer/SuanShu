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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative;

import com.numericalmethod.suanshu.algorithm.iterative.IterativeMethod;
import com.numericalmethod.suanshu.algorithm.iterative.monitor.IterationMonitor;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.nonstationary.*;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.IdentityPreconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.Preconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.GaussSeidelSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.JacobiSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.SuccessiveOverrelaxationSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.SymmetricSuccessiveOverrelaxationSolver;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * An iterative method for solving an N-by-N (or non-square) linear system
 * <i>Ax = b</i> involves a sequence of matrix-vector multiplications.
 * Starting with an initial guess of the solution, each iteration returns a new
 * estimate of the solution. It is hoped that the estimates converge
 * to a satisfactory solution (within a tolerance) after <i>k</i> iterations.
 * For a dense matrix <i>A</i>, each iteration takes <i>O(N<sup>2</sup>)</i>
 * operations. An iterative method takes <i>O(kN<sup>2</sup>)</i> operations to
 * converge. For a sparse system, matrix-vector multiplication takes only
 * <i>O(nNonZeros)</i> where <i>nNonZeros</i> is the number of non-zeros in the
 * sparse matrix. Therefore, an iterative method can be much faster than a
 * traditional direct method of solving a linear system such as taking inverse.
 * An iterative method using sparse matrices is much faster than one using dense
 * matrices.
 * <p/>
 * Here are some guidelines for choosing an iterative solver of a sparse system.
 *
 * For Hermitian problems, if the system is positive definite,
 * use {@linkplain ConjugateGradientSolver CG} or
 * {@linkplain MinimalResidualSolver MINRES}, otherwise use only MINRES.
 * To avoid doing inner products in CG or MINRES, we may choose the stationary
 * methods such as {@linkplain JacobiSolver Jacobi},
 * {@linkplain GaussSeidelSolver Gauss-Seidel},
 * {@linkplain SuccessiveOverrelaxationSolver SOR}, or
 * {@linkplain SymmetricSuccessiveOverrelaxationSolver SSOR}.
 * These methods saves computation costs in each iteration but the number of
 * iterations may increase unless there is a good preconditioner.
 *
 * For non-Hermitian problems, the choice is not so easy.
 * If matrix-vector multiplication is very expensive,
 * {@linkplain GeneralizedMinimalResidualSolver GMRES} is probably the best
 * choice because it performs the fewest multiplications.
 * The second best alternatives are {@linkplain QuasiMinimalResidualSolver QMR}
 * or {@linkplain BiconjugateGradientSolver BiCG}.
 * QMR is numerically more stable than BiCG.
 *
 * When the transpose of a matrix is not available, there are transpose-free
 * methods such as
 * {@linkplain ConjugateGradientSquaredSolver CGS} or
 * {@linkplain BiconjugateGradientStabilizedSolver BiCGSTAB}.
 *
 * For non-square systems, there are CG methods for solving over-determined
 * systems such as
 * {@linkplain ConjugateGradientNormalResidualSolver CGNR}, and
 * under-determined systems such as {@linkplain ConjugateGradientNormalErrorSolver CGNE}.
 * <p/>
 * The use of {@link Preconditioner} can improve the rate of convergence of an
 * iterative method. A preconditioner transforms a linear system into one that
 * is equivalent in the sense that it has the same solution. The transformed
 * system has more favorable spectral properties which affect convergence rate.
 *
 * In particular, a preconditioner <i>M</i> approximates the coefficient
 * matrix <i>A</i>, and the transformed system is easier to solve. For example,
 * <blockquote><i>
 * M<sup>-1</sup>Ax = M<sup>-1</sup>b
 * </i></blockquote>
 * has the same solution as the original system <i>Ax = b</i>. The spectral
 * properties of its coefficient matrix <i>M<sup>-1</sup>A</i> may be more
 * favorable.
 * Another way of preconditioning a system is
 * <blockquote><i>
 * M<sub>1</sub><sup>-1</sup>AM<sub>2</sub><sup>-1</sup>(M<sub>2</sub>x) =
 * M<sub>1</sub><sup>-1</sup>b
 * </i></blockquote>
 * The matrices <i>M<sub>1</sub></i> and <i>M<sub>2</sub></i> are called left-
 * and right preconditioners respectively.
 * There are 3 kinds of preconditioning: left, right, or split.
 * Left-preconditioning leaves <i>M<sub>2</sub></i> as
 * {@link IdentityPreconditioner}. Similarly, right-preconditioning leaves
 * <i>M<sub>1</sub></i> as
 * {@link IdentityPreconditioner}.
 *
 * @author Ken Yiu
 */
public interface IterativeLinearSystemSolver {

    /**
     * This is the solution to a system of linear equations using an iterative
     * solver.
     */
    public static interface Solution extends IterativeMethod<Vector> {

        @Override
        public IterationMonitor<Vector> step() throws ConvergenceFailure;//override the return and exception types

        @Override
        public Vector search(Vector... initials) throws ConvergenceFailure;//override the exception type
    }

    /**
     * Solves iteratively
     * <blockquote>
     * <i>Ax = b</i>
     * </blockquote>
     * until the solution converges, i.e., the norm of residual
     * (<i>b - Ax</i>) is less than or equal to the threshold.
     *
     * @param problem a system of linear equations
     * @param monitor an iteration monitor
     * @return an (approximate) solution to the linear problem
     * @throws ConvergenceFailure if the algorithm fails to converge
     */
    public Solution solve(LSProblem problem, IterationMonitor<Vector> monitor) throws ConvergenceFailure;
}
