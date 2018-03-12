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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.nonstationary.BiconjugateGradientSolver;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Preconditioning reduces the condition number of the
 * coefficient matrix of a linear system to accelerate the convergence
 * when the system is solved by an iterative method.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Preconditioner">Wikipedia: Preconditioner</a>
 */
public interface Preconditioner {

    /**
     * Solve <i>Mv = x</i>, where <i>M</i> is the preconditioner matrix.
     * In effect, this method returns <i>v</i> as the product
     * <i>M<sup>-1</sup>x</i>.
     *
     * @param x a vector
     * @return <i>M<sup>-1</sup>x</i>
     */
    public Vector solve(Vector x);

    /**
     * Solve <i>M<sup>t</sup>v = x</i>, where <i>M</i> is the preconditioner
     * matrix.
     * In effect, this method returns <i>v</i> as the product
     * <i>M<sup>-t</sup>x</i>.
     * This method is usually called by a solver
     * which solves
     * a dual system <i>A<sup>t</sup>x<sup>*</sup> = b<sup>*</sup></i>.
     * {@link BiconjugateGradientSolver} is an example of such a solver.
     *
     * @param x a vector
     * @return <i>M<sup>-t</sup>x</i>
     */
    public Vector transposeSolve(Vector x);
}
