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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.SORSweep;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary.SymmetricSuccessiveOverrelaxationSolver;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * SSOR preconditioner is derived from a symmetric coefficient matrix <i>A</i>
 * which is decomposed as
 * <blockquote><i>
 * A = D + L + L<sup>t</sup>
 * </i></blockquote>
 * The SSOR preconditioning matrix is defined as
 * <blockquote><i>
 * M = (D + L)D<sup>-1</sup>(D + L)<sup>t</sup>
 * </i></blockquote>
 * or, parameterized by <i>&omega;</i>
 * <blockquote><i>
 * M(&omega;) = (1/(2 - &omega;))(D / &omega; + L)(D / &omega;)<sup>-1</sup>(D / &omega; + L)<sup>t</sup>
 * </i></blockquote>
 *
 * <p>
 * The optimal <i>&omega;</i> reduces the number of iterations to
 * a lower order. In practice, however, the spectral information for computing
 * the optimal <i>&omega;</i> is expensive to obtain.
 *
 * @author Ken Yiu
 *
 * @see SymmetricSuccessiveOverrelaxationSolver
 */
public class SSORPreconditioner implements Preconditioner {

    private final Matrix A;
    private final double omega;

    /**
     * Construct an SSOR preconditioner with a symmetric coefficient matrix.
     *
     * @param A     a symmetric coefficient matrix
     * @param omega an extrapolation factor
     */
    public SSORPreconditioner(Matrix A, double omega) {
        this.A = A.deepCopy();
        this.omega = omega;
    }

    /**
     * Solve <i>Mz = x</i> using this SSOR preconditioner.
     *
     * @param x a vector
     * @return <i>M<sup>-1</sup>x</i>
     */
    @Override
    public Vector solve(Vector x) {
        SORSweep sweep = new SORSweep(A, x, omega);
        Vector f = sweep.forward(x.ZERO());
        Vector b = sweep.backward(f);
        return b;
    }

    /**
     * <i>M<sup>t</sup>x = M<sup>-1</sup>x</i> as <i>M</i> is symmetric.
     *
     * @param x a vector
     * @return {@code solve(x)}
     */
    @Override
    public Vector transposeSolve(Vector x) {
        return solve(x);
    }
}
