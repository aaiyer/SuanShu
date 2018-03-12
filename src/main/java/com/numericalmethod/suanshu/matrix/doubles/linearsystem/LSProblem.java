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
package com.numericalmethod.suanshu.matrix.doubles.linearsystem;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.SparseVector;
import com.numericalmethod.suanshu.algorithm.iterative.tolerance.AbsoluteTolerance;
import com.numericalmethod.suanshu.algorithm.iterative.tolerance.Tolerance;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.IdentityPreconditioner;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner.Preconditioner;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is the problem of solving a system of linear equations.
 * <blockquote><i>
 * Ax = b
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class LSProblem {

    private final ImmutableMatrix A;
    private final ImmutableVector b;
    /* optional */
    private int maxIteration;
    private Tolerance tolerance;
    private Vector initialGuess;
    private Preconditioner leftPreconditioner;
    private Preconditioner rightPreconditioner;

    /**
     * Construct a system of linear equations <i>Ax = b</i>.
     *
     * @param A the the homogeneous part, the coefficient matrix, of the linear
     * system
     * @param b the non-homogeneous part, the right-hand side vector, of the
     * linear system
     */
    public LSProblem(Matrix A, Vector b) {
        SuanShuUtils.assertArgument(A.nRows() == b.size(), "A's nrows must equal to b's size");
        this.A = new ImmutableMatrix(A);
        this.b = new ImmutableVector(b);





        // initialize optional parameters to default values
        this.maxIteration = Integer.MAX_VALUE;
        this.tolerance = new AbsoluteTolerance();
        this.initialGuess = new SparseVector(A.nCols());
        this.leftPreconditioner = new IdentityPreconditioner();
        this.rightPreconditioner = new IdentityPreconditioner();
    }

    /**
     * Get the homogeneous part, the coefficient matrix, of the linear system.
     *
     * @return the coefficient matrix
     */
    public ImmutableMatrix A() {
        return A;
    }

    /**
     * Get the non-homogeneous part, the right-hand side vector, of the linear
     * system.
     *
     * @return the vector
     */
    public ImmutableVector b() {
        return b;
    }

    /**
     * Get the number of variables in the linear system.
     *
     * @return the number of variables
     */
    public int size() {
        return A.nRows();
    }

    /**
     * Overrides the maximum count of iterations.
     *
     * @param maxIteration the maximum count of iterations
     * @return the new problem with the overriden maximum count of
     * iterations
     */
    public LSProblem withMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
        return this;
    }

    /**
     * Gets the specified maximum number of iterations.
     *
     * @return the maximum number of iterations
     */
    public int getMaxIteration() {
        return maxIteration;
    }

    /**
     * Overrides the tolerance instance.
     *
     * @param tolerance the criteria which determines when the solution
     * converges and the iteration stops
     * @return the new problem with the overriden withTolerance
     */
    public LSProblem withTolerance(Tolerance tolerance) {
        this.tolerance = tolerance;
        return this;
    }

    /**
     * Gets the specified {@link Tolerance} instance.
     *
     * @return the {@link Tolerance} instance
     */
    public Tolerance getTolerance() {
        return tolerance;
    }

    /**
     * Overrides the initial guess of the solution.
     *
     * @param initialGuess the initial guess of the solution
     * @return the new problem with the overriden initial guess
     */
    public LSProblem withInitialGuess(Vector initialGuess) {
        this.initialGuess = initialGuess;
        return this;
    }

    /**
     * Gets the initial guess of the solution for the problem.
     *
     * @return the initial guess
     */
    public Vector getInitialGuess() {
        return initialGuess;
    }

    /**
     * Overrides the left preconditioner. If right-preconditioning is used,
     * leave this as its default value - {@link IdentityPreconditioner}.
     *
     * @param preconditioner the preconditioner
     * @return the new problem with the overriden left preconditioner
     */
    public LSProblem withLeftPreconditioner(Preconditioner preconditioner) {
        this.leftPreconditioner = preconditioner;
        return this;
    }

    /**
     * Gets the left preconditioner.
     *
     * @return the left preconditioner
     */
    public Preconditioner getLeftPreconditioner() {
        return leftPreconditioner;
    }

    /**
     * Overrides the right preconditioner. If left-preconditioning is used,
     * leave this as its default value - {@link IdentityPreconditioner}.
     *
     * @param preconditioner the preconditioner
     * @return the new problem with the overriden right preconditioner
     */
    public LSProblem withRightPreconditioner(Preconditioner preconditioner) {
        this.rightPreconditioner = preconditioner;
        return this;
    }

    /**
     * Gets the right preconditioner.
     *
     * @return the right preconditioner
     */
    public Preconditioner getRightPreconditioner() {
        return rightPreconditioner;
    }
}
